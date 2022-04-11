package com.workflowfm.proter

import java.util.UUID

import scala.annotation.tailrec
import scala.collection.mutable.{ HashSet, Map, PriorityQueue }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.util.{ Try, Success, Failure }

import com.workflowfm.proter.events.*
//import com.workflowfm.proter.schedule.Scheduler

/*

class Coordinator(
    scheduler: Scheduler,
    singleThread: Boolean = false,
    startingTime: Long = 0L
)(using ExecutionContext)
    extends Manager
    with HashMapPublisher {


  /**
    * Progress virtual time by processing the next [[DiscreteEvent]] in the queue.
    *
    * This is called when we are done waiting for any simulations to respond. We take the first
    * event from the queue and then use [[dequeueEvents]] to get all events with the same timestamp.
    *
    *   - First, [[filterFinishingTasks]] releases all resources that are no longer in use. It is
    *     useful to do this before notifying task completion to ensure all simulations can know the
    *     correct state of the resources if they need to. It also adds the simulations to the
    *     waiting list to ensure we will wait for all of them.
    *   - We then handle finishing tasks, grouped by simulation, with [[stopTasks]].
    *   - All other events are then handled via [[handleDiscreteEvent]].
    *   - If there are no events, no tasks to run, and all simulations have finished, the whole
    *     simulation is done, so we [[finish]].
    *   - If there are no events and no simulations to wait for, but there are still tasks to run,
    *     we attempt to allocate and run them. This may happen if something breaks when handling a
    *     previous event.
    *
    * @group toplevel
    */
  @tailrec
  final protected def tick(): Unit = {
    if !waiting.isEmpty then { // This should never happen, but we add it here as a safeguard for
      // future extensions.
      publish(EError(id, time, s"Called `tick()` even though I am still waiting for: $waiting"))
    } else if !events.isEmpty then { // Are events pending?
      processing = true

      // Grab the first event
      val firstEvent = events.head

      // Did we somehow go past the event time? This should never happen.
      if firstEvent.time < time then {
        publish(EError(id, time, s"Unable to handle past event for time: [${firstEvent.time}]"))
      } else {
        // Jump ahead to the event time. This is a priority queue so we shouldn't skip any events
        time = firstEvent.time

        // Dequeue all the events that need to happen now
        val eventsToHandle = dequeueEvents(firstEvent.time)

        eventsToHandle.flatMap(filterFinishingTasks).groupBy(_.simulation).foreach(stopTasks)

        // Handle the event
        eventsToHandle foreach handleDiscreteEvent

        processing = false
        // If we are not waiting for anything, continue
        if waiting.isEmpty then {
          allocateTasks()
          tick()
        }
      }

    } else if scheduler.noMoreTasks() && simulations.isEmpty && !promise.isCompleted then {
      finish()
    } else if waiting.isEmpty && !scheduler.noMoreTasks() then { // this may happen if handleDiscreteEvent fails
      allocateTasks()
      tick()
    } // else {
    // publish(EError(self, time, "No tasks left to run, but simulations have not finished."))
    // }
  }

  /**
    * Allocates the tasks due to start next to their resources.
    *
    * Asks the [[schedule.Scheduler Scheduler]] to determine what tasks need to start next. Removes
    * each of them from the queue and runs them using [[startTask]].
    *
    * @group resources
    */
  protected def allocateTasks(): Unit = {
    // Assign the next tasks
    scheduler.getNextTasks(time, resourceMap.toMap).foreach { task =>
      scheduler.removeTask(task)
      startTask(task)
    }
  }

  /**
    * Filters finishing tasks, releases resources that are no longer used and adds simulations to
    * the waiting list.
    *
    * If the provided event is a [[FinishingTask]] event, i.e. a [[TaskInstance]] just finished,
    * this means the resources used by that [[TaskInstance]] can now be released and that we should
    * wait for a response from their respective simulations.
    *
    * Other [[DiscreteEvent]]s are just ignored.
    *
    * @group resources
    * @param event
    *   The potential [[FinishingTask]].
    */
  protected def filterFinishingTasks(event: DiscreteEvent): Option[TaskInstance] = {
    event match {
      case FinishingTask(t, task) if (t == time) => {
        if abortedTasks.contains(task.id) then {
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
    // log.debug(s"[COORD:$time] Event!")
    event match {
      // A task is finished, but we handle this elsewhere
      case FinishingTask(t, _) if (t == time) => ()

      // A simulation (workflow) is starting now
      case StartingSim(t, sim) if (t == time) => startSimulation(sim)

      case TimeLimit(t) if (t == time) => stop()

      case arrival @ Arrival(t, _, simulationGenerator, limit, count) if (t == time) =>
        if limit.map(_ > count).getOrElse(true) then {
          events += arrival.next() // replicate self
          startSimulation(simulationGenerator.build(this, count))
        }

      case _ => publish(EError(id, time, s"Failed to handle event: $event"))
    }
  }



  /**
    * Sets or updates the [[Lookahead]] structure for a given simulation.
    *
    * @group simulations
    * @param simulation
    *   The name of the [[SimulationRef]].
    * @param lookahead
    *   The new [[Lookahead]] structure.
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
    if !promise.isCompleted then {
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
    * Adds a new infinite arrival process to the coordinator.
    *
    * @note
    *   This will generate new simulations infinitely. The [[Coordinator]] will not terminate unless
    *   you add a [[TimeLimit]] event via [[limit]].
    *
    * @param t
    *   The virtual timestamp when the arrival process will begin.
    * @param rate
    *   The arrival rate of the simulation instances.
    * @param simulationGenerator
    *   The generator used to create new instances.
    */
  def addInfiniteArrival(
      t: Long,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    if t >= time then events += Arrival(t, rate, simulationGenerator)
  }

  /**
    * Adds a new infinite arrival process to the coordinator at the current virtual time.
    *
    * @note
    *   This will generate new simulations infinitely. The [[Coordinator]] will not terminate unless
    *   you add a [[TimeLimit]] event via [[limit]].
    *
    * @param rate
    *   The arrival rate of the simulation instances.
    * @param simulationGenerator
    *   The generator used to create new instances.
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
    * @note
    *   This will generate new simulations infinitely. The [[Coordinator]] will not terminate unless
    *   you add a [[TimeLimit]] event via [[limit]].
    *
    * @param rate
    *   The arrival rate of the simulation instances.
    * @param simulationGenerator
    *   The generator used to create new instances.
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
    * @param t
    *   The virtual timestamp when the arrival process will begin.
    * @param limit
    *   The number of simulation instances to spawn.
    * @param rate
    *   The arrival rate of the simulation instances.
    * @param simulationGenerator
    *   The generator used to create new instances.
    */
  def addArrival(
      t: Long,
      limit: Int,
\      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    if t >= time then events += Arrival(t, rate, simulationGenerator, Some(limit))
  }

  /**
    * Adds a new arrival process to the coordinator at the current virtual time.
    *
    * @param limit
    *   The number of simulation instances to spawn.
    * @param rate
    *   The arrival rate of the simulation instances.
    * @param simulationGenerator
    *   The generator used to create new instances.
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
    * @param limit
    *   The number of simulation instances to spawn.
    * @param rate
    *   The arrival rate of the simulation instances.
    * @param simulationGenerator
    *   The generator used to create new instances.
    */
  def addArrivalNext(
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    events += Arrival(time + rate.get.round, rate, simulationGenerator, Some(limit))
  }


 */
