package com.workflowfm.proter

import java.util.UUID

import scala.annotation.tailrec
import scala.collection.mutable.{ HashSet, Map, PriorityQueue }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.util.{ Try, Success, Failure }

import com.workflowfm.proter.events._
import com.workflowfm.proter.schedule.Scheduler

/**
  * Abstract simulation manager.
  */
trait Manager {
  /**
    * Adds a simulation to the waiting list.
    *
    * The manager needs to wait for that simulation to send a [[SimResponse]].
    *
    * @param simulation The name of the [[SimulationRef]] we need to wait for.
    */
  def waitFor(simulation: String): Unit

  /**
    * Handles a [[SimResponse]] from a simulation.
    *
    * @param response The [[SimResponse]] to handle.
    */
  def simResponse(response: SimResponse): Unit
}

/**
  * Provides coordination for discrete event simulation of multiple asynchronous simulations.
  *
  * Default implementation of [[Manager]].
  *
  * It can run in a single thread or multi-threaded depending on the `singleThread`
  * constructor parameter. In the multi-threaded setting, task completions are
  * reported to the simulations asynchronously through a new thread (`Future`)
  * each time. This allows simulations to run things concurrently, with the added overhead
  * of thread spawning.
  *
  * @groupname tasks Managing tasks
  * @groupname simulations Managing simulations
  * @groupname resources Managing resources
  * @groupname toplevel Top level
  *
  * @param scheduler The [[schedule.Scheduler Scheduler]] responsible for task allocation at any given time.
  * @param singleThread Flag to run everything in a single thread or else use `Future`s.
  * @param startingTime The starting timestamp of the entire simulation.
  */
class Coordinator(
    scheduler: Scheduler,
    singleThread: Boolean = false,
    startingTime: Long = 0L
)(implicit executionContext: ExecutionContext = ExecutionContext.global)
    extends Manager
    with HashMapPublisher {

  /**
    * A unique id/name for logging purposes.
    *
    * @group toplevel
    */
  val id: String = this.toString()

  /**
    * A promise to fulfil when the simulation is complete.
    *
    * @group toplevel
    */
  protected val promise: Promise[Unit] = Promise[Unit]()

  /**
    * Flag to prevent multiple starts.
    */
  protected var started: Boolean = false

  /**
    * Flag to prevent continuing the virtual time before we have notified
    * all simulations about completed tasks in the current time.
    *
    * @group toplevel
    */
  protected var processing: Boolean = false

  /**
    * Map of the available [[TaskResource]]s
    *
    * @group resources
    */
  val resourceMap: Map[String, TaskResource] = Map[String, TaskResource]()

  /**
    * Set of names of [[Simulation]]s we need to wait for before we can progress time.
    *
    * @group simulations
    */
  protected val waiting: HashSet[String] = HashSet[String]()

  /**
    * Map of currently running simulation names and respective [[SimulationRef]]s.
    *
    * i.e. they have already started but not finished.
    *
    * @group simulations
    */
  protected val simulations: Map[String, SimulationRef] = Map[String, SimulationRef]()

  /**
    * Priority queue of [[DiscreteEvent]]s to be processed,
    * ordered by (future) timestamp.
    *
    * @group toplevel
    */
  protected val events: PriorityQueue[DiscreteEvent] =
    // This is a priority queue, i.e. bigger comes earlier, so we reverse the ordering.
    new PriorityQueue[DiscreteEvent]()(Ordering[DiscreteEvent].reverse)

  /**
    * Set of [[Task]] IDs that have been aborted.
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
    * @group toplevel
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
    * Sequentially builds a `Seq` as it dequeues the events from the event queue.
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
    *  - First, [[filterFinishingTasks]] releases all resources that are no longer in use.
    *   It is useful to do this before notifying task completion to ensure all simulations can know the
    *   correct state of the resources if they need to.
    *   It also adds the simulations to the waiting list to ensure we will wait for all of them.
    *  - We then handle finishing tasks, grouped by simulation, with [[stopTasks]].
    *  - All other events are then handled via [[handleDiscreteEvent]].
    *  - If there are no events, no tasks to run, and all simulations have finished, the whole
    *   simulation is done, so we [[finish]].
    *  - If there are no events and no simulations to wait for, but there are still tasks to run, we
    *   attempt to allocate and run them. This may happen if something breaks when handling a previous
    *   event.
    *
    * @group toplevel
    */
  @tailrec
  final protected def tick(): Unit = {
    if (!waiting.isEmpty) { // This should never happen, but we add it here as a safeguard for
      // future extensions.
      publish(EError(id, time, s"Called `tick()` even though I am still waiting for: $waiting"))
    } else if (!events.isEmpty) { // Are events pending?
      processing = true

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

        eventsToHandle flatMap filterFinishingTasks groupBy (_.simulation) foreach stopTasks

        // Handle the event
        eventsToHandle foreach handleDiscreteEvent

        processing = false
        // If we are not waiting for anything, continue
        if (waiting.isEmpty) {
          allocateTasks()
          tick()
        }
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
    * Asks the [[schedule.Scheduler Scheduler]] to determine what tasks need to start next.
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
    * Filters finishing tasks, releases resources that are no longer used
    * and adds simulations to the waiting list.
    *
    * If the provided event is a [[FinishingTask]] event, i.e. a [[TaskInstance]] just finished,
    * this means the resources used by that [[TaskInstance]] can now be released and
    * that we should wait for a response from their respective simulations.
    *
    * Other [[DiscreteEvent]]s are just ignored.
    *
    * @group resources
    * @param event The potential [[FinishingTask]].
    */
  protected def filterFinishingTasks(event: DiscreteEvent): Option[TaskInstance] = {
    event match {
      case FinishingTask(t, task) if (t == time) => {
        if (abortedTasks.contains(task.id)) {
          abortedTasks -= task.id
          None
        } else {
          waitFor(task.simulation)
          // Unbind the resources
          task.taskResources(resourceMap).foreach(detach)
          Some(task)
        }
      }
      case _ => None
    }
  }

  /**
    * Handles all [[DiscreteEvent]]s other than [[FinishingTask]].
    *
    * @param event
    */
  protected def handleDiscreteEvent(event: DiscreteEvent): Unit = {
    //log.debug(s"[COORD:$time] Event!")
    event match {
      // A task is finished, but we handle this elsewhere
      case FinishingTask(t, task) if (t == time) => Unit

      // A simulation (workflow) is starting now
      case StartingSim(t, sim) if (t == time) => startSimulation(sim)

      case TimeLimit(t) if (t == time) => stop()

      case arrival @ Arrival(t, _, simulationGenerator, limit, count) if (t == time) =>
        if (limit.map(_ > count).getOrElse(true)) {
          events += arrival.next() //replicate self
          startSimulation(simulationGenerator.build(this, count))
        }

      case _ => publish(EError(id, time, s"Failed to handle event: $event"))
    }
  }

  /**
    * Add a new simulation to be run.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimAdd ESimAdd]].
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
    * Publishes a [[com.workflowfm.proter.events.ESimAdd ESimAdd]] for each simulation.
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
    * Once the simulation starts, we expect to hear from it in case it wants to add some [[Task]]s.
    * We therefore add it to the waiting queue.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimStart ESimStart]].
    *
    * @group simulations
    * @param simulation The [[Simulation]] to start.
    */
  protected def startSimulation(simulation: SimulationRef): Unit = {
    waitFor(simulation.name)
    publish(ESimStart(id, time, simulation.name))
    simulations += simulation.name -> simulation
    simulation.run()
  }

  /**
    * Stops a simulation when it is done.
    *
    *  - Removes the simulation from the list of running simulations and the waiting list.
    *  - Removes any [[Lookahead]] from the [[schedule.Scheduler Scheduler]].
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
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAbort ETaskAbort]] for each aborted task
    *    and associated [[com.workflowfm.proter.events.ETaskDetach ETaskDetach]] for each released
    *    resource.
    *  - Removes all queued tasks of the simulation from the scheduler.
    *  - Publishes a [[com.workflowfm.proter.events.ESimEnd ESimEnd]].
    *  - Asks the simulation to stop.
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

  /**
    * Aborts all currently running simulations.
    *
    * Calls [[abortSimulation]] for each of them.
    * @group simulations
    */
  protected def abortAllSimulations(): Unit = {
    simulations.map(s => abortSimulation(s._2))
  }

  /**
    * Adds new [[Task]](s) for a simulation.
    *
    *  - Uses [[Task.create]] to create a [[TaskInstance]], which will now have a fixed duration and cost.
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *  - If the task does not require any resources, it is started immediately using [[startTask]].
    * Otherwise, we add it to the [[schedule.Scheduler Scheduler]].
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
    * A [[TaskInstance]] is started when scheduled by the [[schedule.Scheduler Scheduler]].
    * This assumes all the [[TaskResource]]s it needs are available.
    *
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *  - Calls [[TaskResource.startTask]] for each involved [[TaskResource]] to attach this [[TaskInstance]]
    *    to them. Publishes a [[com.workflowfm.proter.events.ETaskAttach ETaskAttach]] for each successful attachment.
    *    Otherwise publishes an appropriate [[com.workflowfm.proter.events.EError EError]]. The latter would
    *    only happen if the [[schedule.Scheduler Scheduler]] tried to schedule a [[Task]] to a busy [[TaskResource]].
    *  - Creates a [[FinishingTask]] event for this [[Task]] based on its duration, and adds it to
    *    the even queue.
    *
    * @group tasks
    * @param task The [[TaskInstance]] to be started.
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
    * @inheritdoc
    *
    * @group simulations
    */
  override def waitFor(simulation: String): Unit = this.synchronized {
    waiting += simulation
    //log.debug(s"[COORD:$time] Wait requested: $simulation")
  }

  /**
    * Handles a [[SimDone]] response from a simulation, indicating that it has completed.
    *
    * Calls [[stopSimulation]] according to the reported success or failure result.
    *
    * @group simulations
    * @param simulation The name of the simulation
    * @param result The result of completion
    */
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
  protected def detach(r: TaskResource): Unit = {
    r.finishTask(time) match {
      case None => Unit
      case Some(task) => publish(ETaskDetach(id, time, task, r.name, r.costPerTick * task.duration))
    }
  }

  /**
    * Handles a group of [[TaskInstance]]s of the same simulation that has just finished.
    *
    * Assumes all instances belong to the same simulation.
    *
    *  - Notifies the [[schedule.Scheduler Scheduler]] about each task comleting.
    *  - Publishes a [[com.workflowfm.proter.events.ETaskDone ETaskDone]].
    *  - Notifies the [[Simulation]] that its [[TaskInstance]]s have finished.
    *    This happens in the same thread if `singleThread` is `true` or using a `Future` otherwise.
    *
    * Note that resources are detached before this in [[tick]] using [[filterFinishingTasks]].
    *
    * @group tasks
    * @param taskGroup The simulation name paired with the [[TaskInstance]]s that need to be stopped.
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
    *  - Detaches all associated [[TaskResource]]s publishing a
    *    [[com.workflowfm.proter.events.ETaskDetach ETaskDetach]] each time.
    *  - Adds the task ID to the [[abortedTasks]] set.
    *  - Publishes a [[com.workflowfm.proter.events.ETaskAbort ETaskAbort]].
    *
    * @group tasks
    * @param id The `UUID`s of the [[TaskInstance]]s that need to be aborted.
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
    * This can happen when the simulation has responded and we finished handling the response.
    * We remove it from the waiting queue and then check if the waiting queue is empty and if we
    * have finished processing events.
    * If so, we can progress time.
    *
    * First, we allocate new tasks, because some of them
    * may be able to start immediately. We then progress time with [[tick]].
    *
    * @group simulations
    * @param actor The name of the [[SimulationRef]] that is ready.
    */
  protected def ready(name: String): Unit = {
    waiting -= name
    if (waiting.isEmpty && !processing) {
      allocateTasks()
      tick()
    }
  }

  /**
    * Sets or updates the [[Lookahead]] structure for a given simulation.
    *
    * @group simulations
    * @param simulation The name of the [[SimulationRef]].
    * @param lookahead The new [[Lookahead]] structure.
    */
  protected def setLookahead(simulation: String, lookahead: Lookahead): Unit = {
    scheduler.setLookahead(simulation, lookahead)
  }

  /**
    * @inheritdoc
    *
    * @group simulations
    */
  override def simResponse(response: SimResponse): Unit = this.synchronized {
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
    *
    * Publishes a [[com.workflowfm.proter.events.EStart EStart]].
    *
    * @group toplevel
    */
  def start(): Future[Any] = if (started) promise.future else {
    started = true
    Future {
      publish(EStart(id, time))
      tick()
    }.flatMap(_ => promise.future)
  }

  /**
    * Shuts down the entire simulation and shuts down the actor.
    *
    * Publishes a [[com.workflowfm.proter.events.EDone EDone]].
    *
    * Fulfils the completion [[promise]].
    *
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
    * @group toplevel
    * @param t The virtual timestamp to end the simulation.
    */
  def limit(t: Long): Unit = if (t >= time) events += TimeLimit(t)

  /**
    * Adds a new infinite arrival process to the coordinator.
    *
    * @note This will generate new simulations infinitely. The [[Coordinator]] will not terminate
    *       unless you add a [[TimeLimit]] event via [[limit]].
    *
    * @param t The virtual timestamp when the arrival process will begin.
    * @param rate The arrival rate of the simulation instances.
    * @param simulationGenerator The generator used to create new instances.
    */
  def addInfiniteArrival(
      t: Long,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    if (t >= time) events += Arrival(t, rate, simulationGenerator)
  }

  /**
    * Adds a new infinite arrival process to the coordinator at the current virtual time.
    *
    * @note This will generate new simulations infinitely. The [[Coordinator]] will not terminate
    *       unless you add a [[TimeLimit]] event via [[limit]].
    *
    * @param rate The arrival rate of the simulation instances.
    * @param simulationGenerator The generator used to create new instances.
    */
  def addInfiniteArrivalNow(
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    events += Arrival(time, rate, simulationGenerator)
  }

  /**
    * Adds a new infinite arrival process to the coordinator at the next arrival time.
    *
    * @note This will generate new simulations infinitely. The [[Coordinator]] will not terminate
    *       unless you add a [[TimeLimit]] event via [[limit]].
    *
    * @param rate The arrival rate of the simulation instances.
    * @param simulationGenerator The generator used to create new instances.
    */
  def addInfiniteArrivalNext(
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    events += Arrival(time + rate.get.round, rate, simulationGenerator)
  }

  /**
    * Adds a new arrival process to the coordinator.
    *
    * @param t The virtual timestamp when the arrival process will begin.
    * @param limit The number of simulation instances to spawn.
    * @param rate The arrival rate of the simulation instances.
    * @param simulationGenerator The generator used to create new instances.
    */
  def addArrival(
      t: Long,
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    if (t >= time) events += Arrival(t, rate, simulationGenerator, Some(limit))
  }

  /**
    * Adds a new arrival process to the coordinator at the current virtual time.
    *
    * @param limit The number of simulation instances to spawn.
    * @param rate The arrival rate of the simulation instances.
    * @param simulationGenerator The generator used to create new instances.
    */
  def addArrivalNow(
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    events += Arrival(time, rate, simulationGenerator, Some(limit))
  }

  /**
    * Adds a new arrival process to the coordinator at the next arrival time.
    *
    * @param limit The number of simulation instances to spawn.
    * @param rate The arrival rate of the simulation instances.
    * @param simulationGenerator The generator used to create new instances.
    */
  def addArrivalNext(
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    events += Arrival(time + rate.get.round, rate, simulationGenerator, Some(limit))
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
