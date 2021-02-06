package com.workflowfm.proter

import java.util.UUID

import scala.annotation.tailrec
import scala.collection.mutable.{ HashSet, Map, PriorityQueue, Queue, SortedSet }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.util.{ Try, Success, Failure }

import com.workflowfm.proter.events._

trait Manager {
  def waitFor(simulation: String): Unit
  def simResponse(response: SimResponse): Unit
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
    singleThread: Boolean = false,
    startingTime: Long = 0L
)(implicit executionContext: ExecutionContext = ExecutionContext.global)
    extends Manager
    with HashMapPublisher {

  val id: String = this.toString()

  val promise: Promise[Unit] = Promise[Unit]()

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
  protected val waiting: HashSet[String] = HashSet[String]()

  /**
    * Set of simulations that are running.
    *
    * i.e. they have already started but not finished.
    *
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
  def addResource(r: TaskResource): Unit = this.synchronized {
    if (!resourceMap.contains(r.name)) {
      publish(EResourceAdd(id, time, r.name, r.costPerTick))
      resourceMap += r.name -> r
    }
  }

  /**
    * Add multiple [[TaskResource]]s in one go.
    *
    * @group resources
    * @param r The sequence of [[TaskResource]]s to be added.
    */
  def addResources(r: Seq[TaskResource]): Unit = r foreach addResource

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
    println(time)
    // Are events pending?
    if (!events.isEmpty) {
      println(events)
      // Grab the first event
      val firstEvent = events.head

      // Did we somehow go past the event time? This should never happen.
      if (firstEvent.time < time) {
        publish(EError(id, time, s"Unable to handle past event for time: [${firstEvent.time}]"))
      } else {
        // Jump ahead to the event time. This is a priority queue so we shouldn't skip any events
        time = firstEvent.time

        // Dequeue all the events that need to happen now
        val eventsToHandle = dequeueEvents(firstEvent.time)

        // Set up waiting list
        val doWait = eventsToHandle map waitForSimOfEvent exists identity

        // Release all resources from finished tasks before you notify anyone
        eventsToHandle flatMap filterFinishingTasks groupBy (_.simulation) foreach stopTasks

        // Handle the event
        eventsToHandle foreach handleDiscreteEvent

        // If we are not waiting for anything, continue
        if (!doWait) tick()
      }

    } else if (scheduler.noMoreTasks() && simulations.isEmpty && !promise.isCompleted) {
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
  protected def filterFinishingTasks(event: DiscreteEvent): Option[TaskInstance] = {
    event match {
      case FinishingTask(t, task) if (t == time) => {
        if (abortedTasks.contains(task.id)) {
          abortedTasks -= task.id
          None
        } else {
          // Unbind the resources
          task.taskResources(resourceMap).foreach(detach)
          Some(task)
        }
      }
      case _ => None
    }
  }

  protected def waitForSimOfEvent(event: DiscreteEvent): Boolean = {
    //log.debug(s"[COORD:$time] Event!")
    event match {
      // A task is finished
      case FinishingTask(t, task) if (t == time) =>
        if (!abortedTasks.contains(task.id)) {
          waitFor(task.simulation)
          true
        } else false

      // A simulation (workflow) is starting now
      case StartingSim(t, sim) if (t == time) => {
        waitFor(sim.name)
        true
      }

      case _ => false
    }
  }

  protected def handleDiscreteEvent(event: DiscreteEvent): Unit = {
    println("     Time: " + time.toString() + "; Event: "+ event.toString())
    //log.debug(s"[COORD:$time] Event!")
    event match {
      // A task is finished, but we handle this elsewhere
      case FinishingTask(t, task) if (t == time) => Unit

      // A simulation (workflow) is starting now
      case StartingSim(t, sim) if (t == time) => startSimulation(sim)

      case TimeLimit(t) => { //TODO if (t == time) see issue on github
        stop()
      }

      case ArrivalProcess(t, rate, simulationGenerator) => {
        events += ArrivalProcess(rate.next(t).round, rate, simulationGenerator) //replivate self
        //startSimulation(simulationGenerator.newSim(this))
        addSimulationNow(simulationGenerator.newSim(this))
      }

      case _ => publish(EError(id, time, s"Failed to handle event: $event"))
    }
  }

  /**
    * Add a new simulation to be run.
    *
    * @group simulations
    * @param t The timestamp when the simulation needs to start. Must be greater or equal to the current
    *          time.
    * @param simulation The [[Simulation]] to run.
    */
  def addSimulation(t: Long, simulation: SimulationRef): Unit = this.synchronized {
    publish(ESimAdd(id, time, simulation.name, t))
    if (t >= time) events += StartingSim(t, simulation)
  }

  /**
    * Add a new simulation to be run in the current virtual time.
    *
    * @group simulations
    * @param simulation The [[Simulation]] to run.
    */
  def addSimulationNow(simulation: SimulationRef): Unit = addSimulation(time, simulation)

  /**
    * Adds multiple simulations at the same time.
    *
    * This is equivalent to mapping [[addSimulation]] over the given sequence, but more efficient.
    *
    * @group simulations
    * @param sims A sequence of pairs, each consisting of a starting timestamp and a
    * [[Simulation]]. Timestamps must be greater or equal to the current time.
    */
  def addSimulations(sims: Seq[(Long, SimulationRef)]): Unit = this.synchronized {
    events ++= sims.flatMap {
      case (t, sim) => {
          publish(ESimAdd(id, time, sim.name, t))
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
    * @param sims A sequence of [[Simulation]]s.
    */
  def addSimulationsNow(sims: Seq[SimulationRef]): Unit = addSimulations(sims.map((time, _)))

  /**
    * Start a [[Simulation]].
    *
    * Once the simulation starts, we exect to hear from it in case it wants to add some [[Task]]s.
    * We therefore add it to the waiting queue.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimStart ESimStart]].
    *
    * @group simulations
    * @param simulation The [[Simulation]] to start.
    */
  protected def startSimulation(simulation: SimulationRef): Unit = {
    publish(ESimStart(id, time, simulation.name))
    simulations += simulation.name -> simulation
    simulation.run()
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
    publish(ESimEnd(id, time, name, result))
    //log.debug(s"[COORD:$time] Finished: [$name]")
    ready(name)
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
    * @param simulation The [[Simulation]] to stop.
    */
  protected def abortSimulation(simulation: SimulationRef): Unit = {
    val name = simulation.name
    simulations -= name
    waiting -= name

    val tasksToAbort = HashSet[UUID]()

    // Detach and abort tasks of this simulation
    resourceMap.foreach {
      case (name, resource) =>
        resource.abortSimulation(name) match {
          case None => Unit
          case Some((start, task)) => {
            publish(
              ETaskDetach(id, time, task, resource.name, resource.costPerTick * (time - start))
            )
            tasksToAbort += task.id
          }
        }
    }
    tasksToAbort.map { tid => publish(ETaskAbort(id, time, tid)) }
    abortedTasks ++= tasksToAbort

    // Remove queued tasks of this simulation from the scheduler.
    scheduler.removeSimulation(name)

    publish(ESimEnd(id, time, name, "[Simulation Aborted]"))
    //log.debug(s"[COORD:$time] Aborted: [$name]")
    simulation.stop()
  }

  protected def abortAllSimulations(): Unit = {
    simulations.map(s => abortSimulation(s._2))
  }

  /**
    * Adds new [[Task]](s) for a simulation.
    *
    *  - Uses [[Task.create]] to create a [[TaskInstance]], which will now have a fixed duration and cost.
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *  - If the task does not require any resources, it is started immediately using [[startTask]].
    * Otherwise, we add it to the queue of [[TaskInstance]]s.
    *
    * @group tasks
    * @param simulation The name of the [[Simulation]] that owns the task(s).
    * @param task The list of [[Task]]s to be added.
    */
  protected def addTask(simulation: String, task: Seq[Task]): Unit = {
    task foreach { t =>
      {
        // Create the task
        val inst: TaskInstance = t.create(simulation, time)

        // Calculate the cost of all resource usage. We only know this now!
        //val resourceCost = (0L /: inst.taskResources(resourceMap)) {
        //  case (c, r) => c + r.costPerTick * inst.duration
        //}
        //inst.addCost(resourceCost)

        publish(ETaskAdd(id, time, inst))

        if (t.resources.length > 0)
          scheduler.addTask(inst)
        else
          // if the task does not require resources, start it now
          startTask(inst)
      }
    }
  }

  /**
    * Start a [[TaskInstance]] at the current timestamp.
    *
    * A [[TaskInstance]] is started when scheduled, meaning all the [[TaskResource]]s it needs are available
    * and it is the highest priority [[TaskInstance]] in the queue for those [[TaskResource]]s.
    *
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *  - Calls [[TaskResource.startTask]] for each involved [[TaskResource]] to attach this [[TaskInstnace]]
    * to them. Publishes a [[com.workflowfm.proter.events.ETaskAttach ETaskAttach]] for each successful attachment.
    * Otherwise publishes an appropriate [[com.workflowfm.proter.events.EError EError]]. The latter would
    * only happen if the [[Scheduler]] tried to schedule a [[Task]] to a busy [[TaskResource]].
    *  - Creates a [[FinishingTask]] event for this [[Task]] based on its duration, and adds it to
    * the even queue.
    *
    * @group tasks
    * @param task The [[Task]] to be started.
    */
  protected def startTask(task: TaskInstance): Unit = {
    publish(ETaskStart(id, time, task))
    // Mark the start of the task in the metrics
    task.taskResources(resourceMap) map { r =>
      // Bind each resource to this task
      r.startTask(task, time) match {
        case None =>
          publish(ETaskAttach(id, time, task, r.name))
        case Some(other) =>
          publish(
            EError(
              id,
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
  override def waitFor(simulation: String): Unit = this.synchronized {
    waiting += simulation
    //log.debug(s"[COORD:$time] Wait requested: $simulation")
  }

  protected def simDone(simulation: String, result: Try[Any]): Unit = {
    result match {
      case Success(res) => {
        stopSimulation(simulation, res.toString)
      }
      case Failure(ex) => {
        stopSimulation(simulation, ex.getLocalizedMessage)
      }
    }
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
      case Some(task) => publish(ETaskDetach(id, time, task, r.name, r.costPerTick * task.duration))
    }
  }

  /**
    * Handles a [[TaskInstance]] that has just finished.
    *
    *  - Adds the corresponding [[Simulation]] reference to the waiting list as we
    *   expect it to react to the task finishing.
    *  - Publishes a [[com.workflowfm.proter.events.ETaskDone ETaskDone]].
    *  - Notifies the [[Simulation]] that its [[Task]] has finished.
    *
    * Note that resources are detached before this in [[tick]] using [[releaseResources]].
    *
    * @group tasks
    * @param task The [[TaskInstance]] that needs to be stopped.
    */
  protected def stopTasks(taskGroup: (String, Seq[TaskInstance])): Unit = taskGroup match {
    case (simulation, tasks) => {
      //log.debug(s"[COORD:$time] Waiting post-task: ${task.simulation}")
      tasks foreach { task =>
        scheduler.complete(task, time)
        publish(ETaskDone(id, time, task))
      }
      simulations
        .get(simulation)
        .map(x =>
          if (singleThread)
            x.completed(time, tasks)
          else
            Future { x.completed(time, tasks) }
        )
    }
  }

  /**
    * Aborts one or more [[TaskInstance]]s.
    *
    *  - Adds the corresponding simulation to the waiting list as we
    *   expect it to react to the task aborting.
    *  - Detaches all associated [[TaskResource]]s.
    *  - Adds the task ID to the [[abortedTasks]] set.
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAbort ETaskAbort]].
    *
    * @group tasks
    * @param id The `UUID` of the [[TaskInstance]] that needs to be aborted.
    */
  protected def abortTask(ids: Seq[UUID]): Unit = ids foreach { id =>
    {
      val tasks = resourceMap.flatMap {
        case (_, resource) =>
          resource.abortTask(id).map {
            case (start, task) => {
              publish(
                ETaskDetach(
                  this.id,
                  time,
                  task,
                  resource.name,
                  resource.costPerTick * (time - start)
                )
              )
              task
            }
          }
      }
      abortedTasks += id
      publish(ETaskAbort(this.id, time, id))
    }
  }

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
    waiting -= name
    if (waiting.isEmpty) {
      allocateTasks()
      tick()
    }
  }

  protected def setLookahead(simulation: String, lookahead: Lookahead): Unit = {
    scheduler.setLookahead(simulation, lookahead)
  }

  def simResponse(response: SimResponse): Unit = this.synchronized {
    if (!promise.isCompleted) {
      response match {
        case SimReady(sim, tasks, abort, lookahead) => {
          addTask(sim, tasks)
          abortTask(abort)
          setLookahead(sim, lookahead)
          ready(sim)
        }
        case SimDone(sim, result) => simDone(sim, result)
      }
    }
  }

  /**
    * Starts the entire simulation scenario.
    * @group toplevel
    */
  def start(): Future[Any] = {
    Future {
      publish(EStart(id))
      tick()
    }.flatMap(_ => promise.future)
  }

  /**
    * Shuts down the entire simulation and shuts down the actor.
    *
    * @note Dead letters may occur after this, but we are ok with that, at least for now.
    * @group toplevel
    */
  protected def finish(): Unit = {
    publish(EDone(id, time))
    promise.success(Unit)
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
    * Adds a new arrival process to the coordinator.
    *
    * @param t The virtual timestamp for hen the arrival process will begin.
    * @param rate The arrival rate of the simulation instances.
    * @param simulationGenerator The generator used to create new instances.
    */
  def addArrivalProcess(t: Long, rate: ArrivalRate, simulationGenerator: SimulationGenerator): Unit = {
    if (t >= time) events += ArrivalProcess(t, rate, simulationGenerator)
  }

  /**
    * Adds a new arrival process to the coordinator at the current virtual time.
    *
    * @param rate The arrival rate of the simulation instances.
    * @param simulationGenerator The generator used to create new instances.
    */
  def addArrivalProcessNow(rate: ArrivalRate, simulationGenerator: SimulationGenerator): Unit = {
    events += ArrivalProcess(time, rate, simulationGenerator)
  }

  /**
    * Checks if a given [[com.workflowfm.proter.events.Event Event]] in the output stream is the final one.
    *
    * Causes the stream to shutdown after [[com.workflowfm.proter.events.EDone EDone]] is published.
    *
    * @group toplevel
    * @param e The [[com.workflowfm.proter.events.Event Event]] to check.
    * @return true if it is a [[com.workflowfm.proter.events.EDone EDone]], otherwise false.
    */
  override def isFinalEvent(e: Event): Boolean = e match {
    case EDone(_, _) => true
    case _ => false
  }

}
