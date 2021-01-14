package com.workflowfm.proter

import java.util.UUID

import scala.annotation.tailrec
import scala.collection.mutable.{ HashSet, Map, PriorityQueue, Queue, SortedSet }
import scala.concurrent.{ Await, ExecutionContext, Promise }
import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }

import uk.ac.ed.inf.ppapapan.subakka.HashSetPublisher

import com.workflowfm.proter.events._
import com.workflowfm.proter.metrics._


trait Manager {
  def addTask(simulation: String, task: Task*): Unit
  def ackTask(simulation: String, ids: UUID*): Unit
  def abortTask(ids: UUID*): Unit
  def waitFor(simulation: String): Unit
  def lookahead(simulation: String, lookahead: Lookahead)
  def simReady(simulation: String): Unit
  def simDone(simulation:String, result: Try[Any]): Unit
}


/**
  * Provides coordination for discrete event simulation of multiple asynchronous simulations.
  *
  * @groupname tasks Managing tasks
  * @groupname simulations Managing simulations
  * @groupname resources Managing resources
  * @groupname toplevel Top level
  *
  * @param scheduler The [[Scheduler]] responsible for task allocation at any given time.
  * @param startingTime The starting timestamp of the entire simulation.
  */
class Coordinator(
    scheduler: Scheduler,
    startingTime: Long
) extends HashSetPublisher[Event] {

  /**
    * Map of the available [[TaskResource]]s
    * @group resources
    */
  val resourceMap: Map[String, TaskResource] = Map[String, TaskResource]()

  /**
    * Map of [[Task]] UUIDs we expect to be acknowledged by each [[Simulation]] actor before we can progress time.
    *
    * Each [[Simulation]] actor is given a chance to react individually to each associated [[Task]] that was completed.
    *
    * An empty list as a value means we still need to wait for some action by the actor, without it being asssociated to a
    * particular [[Task]].
    *
    * @group simulations
    */
  protected val waiting: Map[String, List[UUID]] = Map[String, List[UUID]]()

  /**
    * Set of simulation names that are running.
    *
    * i.e. they have already started but not finished.
    *
    * This is currently used to send abort messages.
    * @group simulations
    */
  protected val simulations: Map[String, SimulationRef] = Map[String, SimulationRef]()

  /**
    * [[scala.collection.mutable.PriorityQueue PriorityQueue]] of [[DiscreteEvent]]s to be processed,
    * ordered by (future) timestamp.
    * @group toplevel
    */
  protected val events: PriorityQueue[DiscreteEvent] =
    new PriorityQueue[DiscreteEvent]()(Ordering[DiscreteEvent].reverse)

  /**
    * [[scala.collection.mutable.HashSet HashSet]] of [[Task]] IDs that have been aborted.
    *
    * This allows us to skip [[FinishingTask]] events for aborted tasks. We cannot remove those
    * events from the middle of the priority queue, so this is a computationally cheap way
    * to handle this.
    *
    * @group tasks
    */
  protected val abortedTasks: HashSet[UUID] = HashSet()

  /**
    * The current virtual time.
    * @group toplevel
    */
  protected var time: Long = startingTime

  /**
    * Returns the current virtual time.
    *
    * @return The current virtual time.
    */
  def getTime(): Long = time

  /**
    * Add a new [[TaskResource]] to our map of available resources.
    * 
    * @group resources
    * @param r The [[TaskResource]] to be added.
    */
  def addResource(r: TaskResource): Unit = if (!resourceMap.contains(r.name)) {
    publish(EResourceAdd(self, time, r.name, r.costPerTick))
    resourceMap += r.name -> r
  }

  /**
    * Add multiple [[TaskResource]]s in one go.
    *
    * @group resources
    * @param r The sequence of [[TaskResource]]s to be added.
    */
  def addResources(r: TaskResource*): Unit = r foreach addResource

  /**
    * Extract all [[DiscreteEvent]]s in the queue that need to be processed in a given timestamp.
    *
    * Sequentially builds a [[scala.collection.immutable.Seq Seq]].
    * At the same time it dequeues the events from the event queue.
    *
    * @group toplevel
    * @param t The given timestamp to look out for. We assume it is less than or equal to
    *          the timestamp of the first [[DiscreteEvent]] in the queue.
    */
  protected def dequeueEvents(t: Long): Seq[DiscreteEvent] = {
    val elems = scala.collection.immutable.Seq.newBuilder[DiscreteEvent]
    while (events.headOption.exists(_.time == t)) {
      elems += events.dequeue
    }
    elems.result()
  }

  /**
    * Progress virtual time by processing the next [[DiscreteEvent]] in the queue.
    *
    * This is called when we are done waiting for any simulations to respond.
    * We take the first event from the queue and then use [[dequeueEvents]] to get
    * all events with the same timestamp.
    *
    *  - First, [[releaseResources]] releases all resources that are no longer in use.
    *   It is useful to do this before notifying task completion to ensure all simulations can know the
    *   correct state of the resources if they need to.
    *  - We then handle the events with [[handleDiscreteEvent]].
    *  - If there are no events, no tasks to run, and all simulations have finished, the whole
    *   simulation is done, so we publish [[com.workflowfm.proterevents.EDone EDone]].
    *  - If there are no events and no simulations to wait for, but there are still tasks to run, we
    *   attempt to allocate and run them. This may happen if something breaks when handling a previous
    *   event.
    *
    * @group toplevel
    */
  @tailrec
  final protected def tick(): Unit = {
    // Are events pending?
    if (!events.isEmpty) {
      // Grab the first event
      val firstEvent = events.head

      // Did we somehow go past the event time? This should never happen.
      if (firstEvent.time < time) {
        publish(EError(self, time, s"Unable to handle past event for time: [${firstEvent.time}]"))
      } else {
        // Jump ahead to the event time. This is a priority queue so we shouldn't skip any events
        time = firstEvent.time

        // Dequeue all the events that need to happen now
        val eventsToHandle = dequeueEvents(firstEvent.time)

        // Release all resources from finished tasks before you notify anyone
        eventsToHandle foreach releaseResources
        // Handle the event
        eventsToHandle foreach handleDiscreteEvent

        // If we are not waiting for anything, continue
        if (waiting.isEmpty) tick()
      }

    } else if (scheduler.noMoreTasks() && simulations.isEmpty) {
      finish()
    } else if (waiting.isEmpty && !scheduler.noMoreTasks()) { // this may happen if handleDiscreteEvent fails
      allocateTasks()
      tick()
    } //else {
    //publish(EError(self, time, "No tasks left to run, but simulations have not finished."))
    //}
  }

  /**
    * Allocates the tasks due to start next to their resources.
    *
    * Asks the [[Scheduler]] to determine what tasks need to start next.
    * Removes each of them from the queue and runs them using [[startTask]].
    *
    * @group resources
    */
  protected def allocateTasks(): Unit = {
    // Assign the next tasks
    scheduler.getNextTasks(time, resourceMap).foreach { task =>
      scheduler.removeTask(task)
      startTask(task)
    }
  }

  /**
    * Releases any resources that are not longer used based on the given event.
    *
    * If the provided event is a [[FinishingTask]] event, i.e. a [[Task]] just finished,
    * this means the resources used by that [[Task]] can now be released.
    *
    * Other [[DiscreteEvent]]s are just ignored.
    *
    * @group resources
    * @param event The [[DiscreteEvent]] that potentially released resources.
    */
  protected def releaseResources(event: DiscreteEvent): Any = {
    event match {
      case FinishingTask(t, task) if (t == time && !abortedTasks.contains(task.id)) =>
        // Unbind the resources
        task.taskResources(resourceMap).foreach(detach)
      case _ => Unit
    }
  }

  /**
    * Processes a [[DiscreteEvent]].
    *
    *  - [[FinishingTask]] means a task finished and we need to stop it with [[stopTask]].
    *  - [[StartingSim]] means a simulation needs to start and we do this with [[startSimulation]].
    *  - [[TimeLimit]] means we need to abort all simulations with [[abortAllSimulations]] and clear the queue.
    *
    * @group toplevel
    * @param event The [[DiscreteEvent]] to process.
    */
  protected def handleDiscreteEvent(event: DiscreteEvent): Unit = {
    log.debug(s"[COORD:$time] Event!")
    event match {
      // A task is finished
      case FinishingTask(t, task) if (t == time) =>
        if (abortedTasks.contains(task.id)) abortedTasks -= task.id
        else stopTask(task)

      // A simulation (workflow) is starting now
      case StartingSim(t, sim) if (t == time) => startSimulation(sim)

      case TimeLimit(t) if (t == time) => stop()

      case _ => publish(EError(self, time, s"Failed to handle event: $event"))
    }
  }

  /**
    * Add a new simulation to be run.
    *
    * @group simulations
    * @param t The timestamp when the simulation needs to start. Must be greater or equal to the current
    *          time.
    * @param sim The reference to the [[Simulation]] corresponding to the simulation.
    */
  def addSimulation(t: Long, sim: SimulationRef): Unit = {
    publish(ESimAdd(self, time, sim.toString(), t))
    if (t >= time) events += StartingSim(t, sim)
  }

  /**
    * Add a new simulation to be run in the current virtual time.
    * 
    * @group simulations
    * @param sim The reference to the [[Simulation]].
    */
  def addSimulationNow(sim: SimulationRef): Unit = addSimulation(time, sim)

  /**
    * Adds multiple simulations at the same time.
    *
    * This is equivalent to mapping [[addSimulation]] over the given sequence, but more efficient.
    *
    * @group simulations
    * @param sims A sequence of pairs, each consisting of a starting timestamp and a
    * reference to a [[Simulation]]. Timestamps must be greater or equal to the current time.
    */
  protected def addSimulations(sims: Seq[(Long, SimulationRef)]): PriorityQueue[DiscreteEvent] = {
    events ++= sims.flatMap {
      case (t, sim) => {
          publish(ESimAdd(self, time, sim.toString(), t))
        }
        if (t >= time)
          Some(StartingSim(t, sim))
        else
          None
    }
  }

  /**
    * Add multiple simulations to be run in the current virtual time.
    * 
    * @group simulations
    * @param sims A sequence of [[Simulation]] references.
    */
  def addSimulationsNow(sims: SimulationRef*): Unit = addSimulations(sims.map((time, _)))

  /**
    * Start a simulation via the reference to its [[Simulation]].
    *
    * Once the simulation starts, we exect to hear from it in case it wants to add some [[Task]]s.
    * We therefore add it to the waiting queue.
    *
    * @group simulations
    * @param simActor The [[akka.actor.SimulationRef]] of the [[Simulation]].
    */
  protected def startSimulation(simActor: SimulationRef): Unit = {
    waiting += simActor -> List()
    simActor ! Simulation.Start
  }

  /**
    * Registers that a simulation has started.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimStart ESimStart]].
    *
    * Also records the actor that manages the simulation.
    *
    * @param name The name of the simulation.
    * @param actor The actor running the simulation
    */
  protected def simulationStarted(name: String, actor: SimulationRef): Unit = {
    publish(ESimStart(self, time, name))
    simulations += name -> actor
  }

  /**
    * Stops a simulation when it is done.
    *
    *  - Removes the simulation from the list of running simulations.
    *  - Publishes a [[com.workflowfm.proter.events.ESimEnd ESimEnd]].
    *  - Calls [[ready]] to handle the fact that we no longer need to wait for this simulation.
    *
    * @group simulations
    * @param name The name of the completed simulation.
    * @param result A string representation of the output of the simulation.
    */
  protected def stopSimulation(name: String, result: String): Unit = {
    simulations -= name
    waiting -= name
    scheduler.removeLookahead(name)
    publish(ESimEnd(self, time, name, result))
    log.debug(s"[COORD:$time] Finished: [$name]")
    ready(name)
  }

  /**
    * Stops/aborts a named simulation before it is done.
    *
    * Looks up the simulation actor and then calls
    * [[abortSimulation(name:String,actor:akka\.actor\.SimulationRef)* abortSimulation]].
    *
    * @group simulations
    * @param name The name of the simulation to abort.
    */
  protected def abortSimulation(name: String): Unit = {
    simulations.get(name).map { actor => abortSimulation(name, actor) }
  }

  /**
    * Stops/aborts a simulation before it is done.
    *
    *  - Removes the simulation from the list of running simulations and from the waiting list.
    *  - Detaches all tasks of the simulation from the resources and adds them to the abort list.
    *  - Removes all queued tasks of the simulation from the scheduler.
    *  - Publishes a [[com.workflowfm.proter.events.ESimEnd ESimEnd]].
    *  - Asks the simulation actor to stop.
    *  - Does '''not''' progress time.
    *
    * The bookkeeping assumes the rest of the simulation(s) can still proceed.
    *
    * @group simulations
    * @param name The name of the completed simulation.
    * @param actor The [[akka.actor.SimulationRef]] of the corresponding [[Simulation]].
    */
  protected def abortSimulation(name: String, actor: SimulationRef): Unit = {
    simulations -= name
    waiting -= name

    val tasksToAbort = HashSet[UUID]()

    // Detach and abort tasks of this simulation
    resourceMap.foreach {
      case (name, resource) =>
        resource.abortSimulation(name) match {
          case None => Unit
          case Some(task) => {
            publish(ETaskDetach(self, time, task, resource.name))
            tasksToAbort += task.id
          }
        }
    }
    tasksToAbort.map { id => publish(ETaskAbort(self, time, id)) }
    abortedTasks ++= tasksToAbort

    // Remove queued tasks of this simulation from the scheduler.
    scheduler.removeSimulation(name, actor)

    publish(ESimEnd(self, time, name, "[Simulation Aborted]"))
    log.debug(s"[COORD:$time] Aborted: [${actor.path.name}]")
    actor ! Simulation.Stop
  }

  protected def abortAllSimulations(): Unit = {
    simulations.map(s => abortSimulation(s._1, s._2))
  }

  /**
    * Adds new [[Task]]s for a simulation.
    *
    *  - Calls [[addTask]] for each [[Task]] to be generated.
    *  - Calls [[ready]] to handle the fact that we no longer need to wait for this simulation.
    *
    * @group tasks
    * @param actor The [[akka.actor.SimulationRef]] of the [[Simulation]] that needs to generate the tasks.
    * @param l The list of tasks to be generated, each represented by a triplet with its unique ID,
    *          [[TaskGenerator]] and list of [[TaskResource]] names that need to be used.
    */
  def addTasks(actor: SimulationRef, l: Seq[TaskGenerator]): Unit = {
    l foreach addTask
  }

  /**
    * Adds a single new [[Task]].
    *
    *  - Uses [[TaskGenerator.create]] to create the [[Task]], which will now have a fixed duration and cost.
    *
    *  - Calculates the cost of the involved resources by adding the [[TaskResource.costPerTick]]
    * multipled by the [[Task.duration]]. Adds this to the [[Task.cost]].
    * This is done at runtime instead of at creation time to support variable resources.
    *
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *
    *  - If the task does not require any resources, it is started immediately using [[startTask]].
    * Otherwise, we add it to the queue of [[Task]]s.
    *
    * @group tasks
    * @param gen The [[TaskGenerator]] that will generate the [[Task]].
    */
  def addTask(gen: TaskGenerator): Unit = {
    // Create the task
    val t: Task = gen.create(time, sender)

    // Calculate the cost of all resource usage. We only know this now!
    val resourceCost = (0L /: t.taskResources(resourceMap)) {
      case (c, r) => c + r.costPerTick * t.duration
    }
    t.addCost(resourceCost)

    publish(ETaskAdd(self, time, t))

    if (gen.resources.length > 0)
      scheduler.addTask(t)
    else
      // if the task does not require resources, start it now
      startTask(t)

    //sender() ! Coordinator.AckTask(t) //uncomment this to acknowledge AddTask
  }

  /**
    * Acknowledges that a [[Simulation]] actor has finished reacting to some associated [[Task]]s.
    *
    * Throws a warning if we were not waiting on that actor at all.
    * @group tasks
    * @param actor The [[Simulation]] actor.
    * @param ack The sequence of [[Task]] UUIDs being acknowledged.
    */
  protected def ackTasks(sim: SimulationRef, ack: Seq[UUID]): Unit = {
    log.debug(s"[COORD:$time] Ack [${sim.toString()}]: $ack")
    waiting.get(sim) match {
      case None =>
        log.warning(s"[COORD:$time] Unexpected tasks acknowledged by ${sim.toString()}: $ack")
      case Some(l) => {
        waiting.update(sim, l.diff(ack))
        ready(sim)
      }
    }
  }

  /**
    * Acknowledges that a [[Simulation]] actor has finished processing everything.
    *
    * Removes the actor from the waiting list completely, without the need to
    * acknowledge individual [[Task]]s.
    *
    * @group simulations
    * @param actor The [[Simulation]] actor that is done.
    */
  protected def ackAll(actor: SimulationRef): Unit = {
    log.debug(s"[COORD:$time] Ack ALL [${actor.path.name}]")
    waiting -= actor
    ready(actor)
  }

  /**
    * Start a [[Task]] at the current timestamp.
    *
    * A [[Task]] is started when scheduled, meaning all the [[TaskResource]]s it needs are available
    * and it is the highest priority [[Task]] in the queue for those [[TaskResource]]s.
    *
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *  - Calls [[TaskResource.startTask]] for each involved [[TaskResource]] to attach this [[Task]]
    * to them. Publishes a [[com.workflowfm.proter.events.ETaskAttach ETaskAttach]] for each successful attachment.
    * Otherwise publishes an appropriate [[com.workflowfm.proter.events.EError EError]]. The latter would
    * only happen if the [[Scheduler]] tried to schedule a [[Task]] to a busy [[TaskResource]].
    *  - Creates a [[FinishingTask]] event for this [[Task]] based on its duration, and adds it to
    * the even queue.
    *
    * @group tasks
    * @param task The [[Task]] to be started.
    */
  protected def startTask(task: Task): Unit = {
    publish(ETaskStart(self, time, task))
    // Mark the start of the task in the metrics
    task.taskResources(resourceMap) map { r =>
      // Bind each resource to this task
      r.startTask(task, time) match {
        case None =>
          publish(ETaskAttach(self, time, task, r.name))
        case Some(other) =>
          publish(
            EError(
              self,
              time,
              s"Tried to attach task [${task.name}](${task.simulation}) to [${r.name}], but it was already attached to [${other.name}](${other.simulation}) "
            )
          )
      }
    }
    // Generate a FinishTask event to be triggered at the end of the event
    events += FinishingTask(time + task.duration, task)
  }

  /**
    * Adds a simulation to the waiting list.
    *
    * We will wait for that simulation to send tasks or finish before we progress time.
    * This is used when a simulation wants to react externally to events from another simulation.
    *
    * Other events that warrant waiting for a simulation to react do not call this method, but
    * add the simulation to the waiting list on their own.
    *
    * @group simulations
    * @param actor The reference to the [[Simulation]] we need to wait for.
    */
  protected def waitFor(actor: SimulationRef, ack: SimulationRef): Unit = {
    waiting += actor -> List()
    log.debug(s"[COORD:$time] Wait requested: ${actor.path.name}")
    ack ! Simulation.AckWait
  }

  /**
    * Detaches the task attached to the given [[TaskResource]].
    *
    * A wrapper of [[TaskResource.finishTask]] that
    * publishes a [[com.workflowfm.proter.events.ETaskDetach ETaskDetach]].
    *
    * @group resources
    * @param r The [[TaskResource]] to free up.
    */
  protected def detach(r: TaskResource): Any = {
    r.finishTask(time) match {
      case None => Unit
      case Some(task) => publish(ETaskDetach(self, time, task, r.name))
    }
  }

  /**
    * Handles a [[Task]] that has just finished.
    *
    *  - Adds the corresponding [[Simulation]] reference to the waiting list as we
    *   expect it to react to the task finishing.
    *  - Publishes a [[com.workflowfm.proter.events.ETaskDone ETaskDone]].
    *  - Notifies the [[Simulation]] that its [[Task]] has finished.
    *
    * Note that resources are detached before this in [[tick]] using [[releaseResources]].
    *
    * @group tasks
    * @param task The [[Task]] that needs to be stopped.
    */
  protected def stopTask(task: Task): Unit = {
    waiting.get(task.actor) match {
      case None => waiting += task.actor -> List(task.id)
      case Some(l) => waiting.update(task.actor, task.id :: l)
    }
    log.debug(s"[COORD:$time] Waiting post-task: ${task.actor.path.name}")
    scheduler.complete(task, time)
    publish(ETaskDone(self, time, task))
    task.actor ! Simulation.TaskCompleted(task, time)
  }

  /**
    * Aborts a [[Task]].
    *
    *  - Adds the corresponding actor o the waiting list as we
    *   expect it to react to the task aborting.
    *  - Detaches all associated [[TaskResource]]s.
    *  - Adds the task ID to the [[abortedTasks]] set.
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAbort ETaskAbort]].
    *
    * @group tasks
    * @param id The `UUID` of the [[Task]] that needs to be aborted.
    * @param source The actor that caused the abort.
    */
  protected def abortTask(id: UUID, source: String): Unit = {
    // Need to wait for the source if we are not already.
    waiting.get(source) match {
      case None => waiting += source -> List()
      case Some(_) => Unit
    }
    log.debug(s"[COORD:$time] Waiting post-abort: ${source}")
    resourceMap.foreach {
      case (name, resource) =>
        resource.abortTask(id) match {
          case None => Unit
          case Some(task) => publish(ETaskDetach(self, time, task, resource.name))
        }
    }
    abortedTasks += id
    publish(ETaskAbort(self, time, id))
  }

  /**
    * Abort multiple [[Task]]s in one go.
    * 
    * @see [[abortTask]]
    *
    * @param source The owner of the [[Task]]s.
    * @param ids The ids of the [[Task]]s to abort.
    */
  def abortTasks(source: String, ids: UUID*): Unit = ids map (abortTask(_, source))

  /**
    * Reacts to the fact that we no longer need to wait for a simulation.
    *
    * This can happen when the simulation has finished or has added its new tasks.
    * We remove it from the waiting queue and then check if the waiting queue is empty.
    * If it is, we can progress time. First, we allocate new tasks, because some of them
    * may be able to start immediately. We then progress time with [[tick]].
    *
    * @group simulations
    * @param actor The [[akka.actor.SimulationRef SimulationRef]] of the [[Simulation]] that is ready.
    */
  protected def ready(name: String): Unit = {
    // Is there at least one task to wait for?
    waiting.get(name).flatMap(_.headOption) match {
      case Some(_) => Unit
      case _ => {
        waiting -= name
        log.debug(s"[COORD:$time] Waiting: ${waiting.keys}")
        // Are all actors ready?
        if (waiting.isEmpty) {
          allocateTasks()
          tick()
        }
      }
    }
  }

  /**
    * Starts the entire simulation scenario.
    * @group toplevel
    */
  def start(): Unit = {
    publish(EStart(self))
    tick()
  }

  /**
    * Shuts down the entire simulation and shuts down the actor.
    *
    * @note Dead letters may occur after this, but we are ok with that, at least for now.
    * @group toplevel
    */
  protected def finish(): Unit = {
    publish(EDone(self, time))
    context.stop(self)
  }

  /**
    * Aborts all simulations and stops immediately.
    * 
    * @group toplevel
    */
  def stop(): Unit = {
    abortAllSimulations()
    events.clear()
    finish()
  }

  /**
    * Sets a time limit in virtual time for the simulation to end.
    * 
    * @note Once a time limit is placed it cannot be removed. Multiple time limits can be set
    * so that the earliest one will be triggered.
    *
    * @param t The virtual timestamp to end the simulation.
    */
  def limit(t: Long): Unit = if (t >= time) events += TimeLimit(t)

  /**
    * Checks if a given [[com.workflowfm.proter.events.Event Event]] in the output stream is the final one.
    *
    * Causes the stream to shutdown after [[com.workflowfm.proter.events.EDone EDone]] is published.
    *
    * @group toplevel
    * @param e The [[com.workflowfm.proter.events.Event Event]] to check.
    * @return true if it is a [[com.workflowfm.proter.events.EDone EDone]], otherwise false.
    */
  override protected def isFinalEvent(e: Event): Boolean = e match {
    case EDone(_, _) => true
    case _ => false
  }

}
/*
    case Coordinator.AckTasks(ack, lookahead) => {
      lookahead.map(scheduler.setLookahead(sender(), _))
      ackTasks(sender, ack)
    }
    case Coordinator.SimReady(lookahead) => {
      lookahead.map(scheduler.setLookahead(sender(), _))
      ackAll(sender)
    }
    case Coordinator.WaitFor(actor) => waitFor(actor, sender())
    case Coordinator.SimStarted(name, actor) => simulationStarted(name, actor)
    case Coordinator.SimDone(name, result) =>
      result match {
        case Success(res) => {
          stopSimulation(name, res.toString, sender)
        }
        case Failure(ex) => {
          stopSimulation(name, ex.getLocalizedMessage, sender)
          ex.printStackTrace()
        }
      }

    case Coordinator.UpdateLookahead(obj) => scheduler.setLookahead(sender(), obj)
  }
*/


trait SimulationRef {
  def name: String
}

