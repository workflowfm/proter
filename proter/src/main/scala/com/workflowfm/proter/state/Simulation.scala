package com.workflowfm.proter
package state

import cases.{ Case, CaseRef }
import events.*
import schedule.Scheduler

import cats.{ Monad, Applicative }
import cats.data.{ State, StateT }
import cats.effect.std.Random
import cats.implicits.*

import scala.annotation.tailrec
import scala.collection.immutable.{ HashSet, Set, Map, Queue }
import scala.util.{ Try, Success, Failure }
import java.util.UUID

/**
  * A simulation state.
  *
  * @param id
  *   A name for the entire simulation.
  * @param scheduler
  *   The [[schedule.Scheduler Scheduler]] being used.
  * @param time
  *   The current time.
  * @param events
  *   The [[EventQueue]] of pending discrete events.
  * @param tasks
  *   Current running [[TaskInstance]]s.
  * @param cases
  *   Indexed map of currently running [[cases.CaseRef CaseRef]]s.
  * @param resources
  *   The [[ResourceMap]] of the resources used in the simulation.
  * @param waiting
  *   Indexed map of [[cases.CaseRef CaseRef]] names and corresponding finished tasks (if any) from
  *   which we are expecting a response.
  * @param abortedTasks
  *   Set of [[TaskInstance]]s that have been aborted.
  */
case class Simulation[F[_]](
    id: String,
    scheduler: Scheduler,
    time: Long,
    events: EventQueue,
    tasks: Set[TaskInstance],
    cases: Map[String, CaseRef[F]],
    resources: ResourceMap,
    waiting: Map[String, Seq[TaskInstance]],
    abortedTasks: HashSet[UUID]
) {

  /**
    * Convenience method to produce an [[events.EError EError]] event.
    *
    * @param description
    *   A description of the error that occurred.
    * @return
    *   An [[events.EError EError]] event.
    */
  def error(description: String): EError = EError(id, time, description)

  /**
    * Convenience method to produce an [[events.EError EError]] event, followed by
    * [[events.EDone EDone]].
    *
    * Essentially tells the event stream that the simulation has completed after the error,
    * i.e. that the error was unrecoverable.
    *
    * @param description
    *   A description of the error that occurred.
    * @return
    *   The generated sequence of 2 events.
    */
  def fatalError(description: String): Seq[Event] =
    Seq(EError(id, time, description), EDone(id, time))

  /**
    * Removes a [[cases.CaseRef CaseRef]] name from the waiting list.
    *
    * @param description
    *   A description of the error that occurred.
    * @return
    *   An [[events.EError EError]] event.
    */
  def ready(name: String): Simulation[F] = copy(waiting = waiting - name)

  /**
    * Uses the [[schedule.Scheduler Scheduler]] to compute the next [[TaskInstance]]s to run based
    * on resource availability.
    *
    * @param description
    *   A description of the error that occurred.
    * @return
    *   An [[events.EError EError]] event.
    */
  def nextTasks(): Iterable[TaskInstance] = scheduler.getNextTasks(time, tasks, resources)

  /**
    * Yields a state change from a [[cases.CaseRef CaseRef]].
    *
    * This happens when we are waiting from a response from the [[cases.CaseRef CaseRef]], either
    * because it has just started or because one or more of its [[TaskInstance]]s has completed.
    *
    * @param waiting
    *   The name of the [[cases.CaseRef CaseRef]] to notify, paired with a sequence of completed
    *   [[TaskInstance]]s or an empty sequence if the case just started.
    * @return
    *   The state update produced by the [[cases.CaseRef CaseRef]].
    */
  def notifyCase(waiting: (String, Seq[TaskInstance]))(using Monad[F]): F[Simulation.SimState[F]] =
    waiting match {
      case (caseName, tasks) =>
        cases.get(caseName) match {
          case None => Monad[F].pure(Simulation.unknownCaseToNotify(caseName))
          case Some(c) => if tasks.isEmpty then c.run() else c.completed(time, tasks)
        }
    }

  /**
    * Notifies all [[cases.CaseRef CaseRef]]s in the waiting list.
    *
    * @see
    *   [[Simulation.notifyCase notifyCase]]
    *
    * Composes and applies all state updates produced by all notified [[cases.CaseRef CaseRef]]s.
    *
    * @return
    *   The updated simulation state and produced [[events.Event Event]]s.
    */
  def notifyCases(using Monad[F]): F[(Simulation[F], Seq[Event])] =
    waiting.map(notifyCase).toSeq.sequence.map(Simulation.compose(_: _*).run(this)).flatten

  /**
    * Starts the simulation with an initial state update.
    *
    * Produces an [[events.EStart EStart]] event.
    *
    * @param state
    *   The state update to apply.
    * @return
    *   The updated state and produced [[events.Event Event]]s.
    */
  def start(
      state: StateT[F, Simulation[F], Seq[Event]]
  )(using Monad[F]): F[(Simulation[F], Seq[Event])] =
    state
      .flatMap(events =>
        // Issue the EStart event at the time set by the initial state
        // (e.g. through `Scenario.withStartingTime`)
        StateT.inspect[F, Simulation[F], Long](_.time).map(t => events :+ EStart(id, t))
      )
      .run(this)

  /**
    * Progresses the simulation time.
    *
    * This is the main simulation cycle:
    *   1. Finish with an [[events.EDone EDone]] if no events, tasks, or cases remain.
    *   1. Pop the next group of discrete events in the queue.
    *   1. Handle the events using [[Simulation.handleDiscreteEvent]].
    *   1. Stop any tasks that completed ysing [[Simulation.stopTasks]].
    *   1. Notify the cases that need to be notified with [[Simulation.notifyCases notifyCases]].
    *   1. Allocate new tasks to the resources using [[Simulation.allocateTasks]].
    *   1. Terminate the simulation using [[Simulation.stop]] if a termination condition (e.g. time
    *      limit) has been met.
    *
    * @return
    *   Either the updated state or `Unit` if the simulation terminated, and any produced
    *   [[events.Event Event]]s.
    */
  def tick(using Monad[F]): F[(Either[Simulation[F], Unit], Seq[Event])] = {
    /* if !waiting.isEmpty // still waiting for responses!
     * then Monad[F].pure((Right(()), fatalError(s"Called `tick()` even though I am still waiting
     * for: ${waiting}"))) else */
    if (events.isEmpty && tasks.isEmpty && cases.isEmpty) // finished!
    then Monad[F].pure((Right(()), Seq(EDone(id, time))))
    else
      events.next() match {
        case Some((nextTick, toHandle, updatedEventQueue)) => { // Are events pending?
          val updateEventQueue: StateT[F, Simulation[F], Queue[Event]] = StateT { sim =>
            Monad[F].pure(
              (
                sim.copy(time = nextTick, events = updatedEventQueue, waiting = Map()),
                Queue[Event]()
              )
            )
          }
          val (handledState, tasks, terminate) =
            toHandle.foldLeft((updateEventQueue, Queue[TaskInstance](), false))(
              Simulation.handleDiscreteEvent
            )

          val stopState = StateT.fromState(Simulation.stopTasks[F](tasks).map(Monad[F].pure))

          val notifyState = StateT { (sim: Simulation[F]) => sim.notifyCases }

          val allocateState = Simulation.allocateTasks()

          val terminateState = if terminate then Simulation.stop() else Simulation.idState

          Seq(
            handledState.map(_.toSeq),
            stopState,
            notifyState,
            allocateState,
            terminateState
          ).sequence
            .bimap(Left(_), _.flatten)
            .run(this)
        }
        case None =>
          if !tasks.isEmpty // this may happen if handleDiscreteEvent fails
          then Monad[F].pure((Left(this), Seq()))
          else
            Monad[F].pure(
              (
                Right(()),
                fatalError(s"No tasks or events left, but cases have not finished: ${cases.keys}")
              )
            )
      }
  }

}

object Simulation extends StateOps {
  type SimState[F[_]] = StateT[F, Simulation[F], Seq[Event]]

  /**
    * Initialize a simulation state with a name and [[schedule.Scheduler Scheduler]].
    *
    * @param id
    *   The name to use for the simulation.
    * @param scheduler
    *   The [[schedule.Scheduler Scheduler]] to use.
    * @return
    *   The initialised [[Simulation]] state.
    */
  def apply[F[_]](id: String, scheduler: Scheduler): Simulation[F] = Simulation[F](
    id,
    scheduler,
    0L,
    EventQueue(),
    Set(),
    Map(),
    ResourceMap(Map()),
    Map(),
    HashSet()
  )

  /**
    * Start a [[cases.CaseRef CaseRef]].
    *
    * Once the case starts, we expect to hear from it in case it wants to add some [[Task]]s. We
    * therefore add it to the waiting queue.
    *
    * Publishes a [[events.ECaseStart ECaseStart]].
    *
    * @param caseRef
    *   The [[cases.CaseRef CaseRef]] to start.
    * @return
    *   The state update.
    */
  def startCase[F[_]](caseRef: CaseRef[F]): State[Simulation[F], Event] = State(sim =>
    (
      sim.copy(
        waiting = sim.waiting + (caseRef.caseName -> Seq()),
        cases = sim.cases + (caseRef.caseName -> caseRef)
      ),
      ECaseStart(sim.id, sim.time, caseRef.caseName)
    )
  )

  /**
    * Stops a [[cases.CaseRef CaseRef]] when it is done.
    *
    *   - Removes it from the list of running cases and the waiting list.
    *   - Publishes a [[events.ECaseEnd ECaseEnd]].
    *
    * @param name
    *   The name of the completed [[cases.CaseRef CaseRef]].
    * @param result
    *   A string representation of the resulting output of the case.
    * @return
    *   The state update.
    */
  def stopCase[F[_] : Monad](name: String, result: String): StateT[F, Simulation[F], Seq[Event]] =
    StateT { sim =>
      Monad[F].pure(
        (
          sim.copy(
            waiting = sim.waiting - name,
            cases = sim.cases - name
          ),
          Seq(ECaseEnd(sim.id, sim.time, name, result))
        )
      )
    }
  // scheduler.removeLookahead(name)
  // log.debug(s"[COORD:$time] Finished: [$name]")
  // ready(name)

  /**
    * Stops/aborts a [[cases.CaseRef CaseRef]] before it is done.
    *
    *   - Removes it from the list of running cases and from the waiting list.
    *   - Aborts and removes all tasks of the case.
    *   - Publishes a [[com.workflowfm.proter.events.ECaseEnd ECaseEnd]] with a "Simulation Aborted"
    *     result.
    *   - Tells the [[cases.CaseRef CaseRef]] to stop.
    *
    * @param caseRef
    *   The [[cases.CaseRef CaseRef]] to stop.
    * @return
    *   The state update.
    */
  def abortCase[F[_] : Applicative](caseRef: CaseRef[F]): StateT[F, Simulation[F], Seq[Event]] =
    StateT(sim => {
      val name = caseRef.caseName
      val tasksToAbort = sim.events.tasksOf(name)

      val (abortedTasks, abortEvents) = abortTasks(tasksToAbort.toSeq).run(sim).value

      val result = abortedTasks.copy(
        cases = abortedTasks.cases - name,
        waiting = abortedTasks.waiting - name,
        tasks = abortedTasks.tasks.filter(_.caseName != name)
      )

      val events = ECaseEnd(sim.id, sim.time, name, "[Simulation Aborted]") :: abortEvents.toList

      caseRef.stop().map(_ => (result, events))
    })

  /**
    * Aborts all currently running cases.
    *
    * Calls [[abortCase]] for each of them.
    *
    * @return
    *   The state update.
    */
  protected def abortAll[F[_] : Monad](): StateT[F, Simulation[F], Seq[Event]] = StateT(sim => {
    sim.cases.values.toSeq.traverse(abortCase[F]).map(_.flatten).run(sim)
  })

  /**
    * Start a [[TaskInstance]] at the current timestamp.
    *
    * A [[TaskInstance]] is started when scheduled by the [[schedule.Scheduler Scheduler]]. This
    * assumes all the [[Resource]]s it needs are available.
    *
    *   - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *   - Calls [[ResourceMap.startTask]] to attach the task to its resources.
    *   - Produces an [[com.workflowfm.proter.events.ETaskAttach ETaskAttach]] event for each
    *     successful attachment. Otherwise publishes an appropriate
    *     [[com.workflowfm.proter.events.EError EError]]. The latter would only happen if the
    *     [[schedule.Scheduler Scheduler]] tried to schedule a [[Task]] to a [[Resource]] that is
    *     busy/does not have enough available capacity.
    *   - Creates a [[FinishingTask]] event for this [[Task]] based on its duration, and adds it to
    *     the event queue.
    *
    * @param task
    *   The [[TaskInstance]] to be started.
    * @return
    *   The state update.
    */
  def startTask[F[_]](task: TaskInstance): State[Simulation[F], Seq[Event]] = State(sim =>
    sim.resources.startTask(task, sim.time) match {
      case Left(full) =>
        (
          sim,
          Seq(
            sim.error(
              s"Tried to attach task [${task.name}](${task.caseName}) to [${full.state.resource.name}], but it was full: ${full.state.currentTasks}"
            )
          )
        )
      case Right(startedMap) =>
        (
          sim.copy(
            events = sim.events + FinishingTask(sim.time + task.duration, task),
            tasks = sim.tasks - task,
            resources = startedMap
          ),
          ETaskStart(sim.id, sim.time, task) :: startedMap
            .get(task)
            .map { r =>
              ETaskAttach(sim.id, sim.time, task, r)
            }
            .toList
        )
    }
  )

  /**
    * Aborts one or more [[TaskInstance]]s by their IDs.
    *
    *   - Detaches all associated resources using [[ResourceMap.stopTasks]].
    *   - Produces an [[com.workflowfm.proter.events.ETaskDetach ETaskDetach]] event for each
    *     resource.
    *   - Adds the task ID to the `abortedTasks` set.
    *   - Produces an [[com.workflowfm.proter.events.ETaskAbort ETaskAbort]] event.
    *
    * @param ids
    *   The `UUID`s of the [[TaskInstance]]s that need to be aborted.
    * @return
    *   The state update.
    */
  def abortTasks[F[_]](ids: Seq[UUID]): State[Simulation[F], Seq[Event]] = State(sim => {
    val (abortMap, detached) = sim.resources.stopTasks(ids)
    val abortEvents = ids.map { taskid => ETaskAbort(sim.id, sim.time, taskid) }
    val detachEvents = detached.map(d => ETaskDetach(sim.id, sim.time, d))
    (
      sim.copy(resources = abortMap, abortedTasks = sim.abortedTasks ++ ids),
      detachEvents ++ abortEvents
    )
  })

  /**
    * Handles a group of [[TaskInstance]]s that have just finished.
    *
    *   - Ignore tasks that have been aborted already.
    *   - Detaches all associated resources using [[ResourceMap.stopTasks]].
    *   - Produces an [[com.workflowfm.proter.events.ETaskDetach ETaskDetach]] event for each
    *     resource.
    *   - Produces an [[com.workflowfm.proter.events.ETaskDone ETaskDone]] event.
    *   - Adds the corresponding [[cases.CaseRef CaseRef]]s to the waiting list.
    *
    * @param tasks
    *   The [[TaskInstance]]s that need to be stopped.
    * @return
    *   The state update.
    */
  def stopTasks[F[_]](tasks: Seq[TaskInstance]): State[Simulation[F], Seq[Event]] = State(sim => {
    val (someAbortedTasks, nonAbortedTasks) = tasks.partition(t => sim.abortedTasks.contains(t.id))
    val nonAbortedIds = nonAbortedTasks.map(_.id)
    val (stopMap, detached) = sim.resources.stopTasks(nonAbortedIds)
    val stopEvents = nonAbortedTasks.map { task => ETaskDone(sim.id, sim.time, task) }
    val detachEvents = detached.map(d => ETaskDetach(sim.id, sim.time, d))
    val waits = nonAbortedTasks.groupBy(_.caseName)
    (
      sim.copy(
        resources = stopMap,
        waiting = sim.waiting ++ waits,
        abortedTasks = sim.abortedTasks -- someAbortedTasks.map(_.id)
      ),
      detachEvents ++ stopEvents
    )
  })

  /**
    * Allocates and starts new [[TaskInstance]]s.
    *
    * New tasks are decided by the [[schedule.Scheduler Scheduler]] given the resource availability.
    *
    * @return
    *   The state update.
    */
  def allocateTasks[F[_] : Monad](): StateT[F, Simulation[F], Seq[Event]] = StateT { sim =>
    StateT
      .fromState(
        sim.nextTasks().map(startTask[F]).toSeq.sequence.map(e => Monad[F].pure(e.flatten))
      )
      .run(sim)
  }

  /**
    * Folding function to handle [[DiscreteEvent]]s
    *
    * For each event we compute the following 3 things:
    *   1. A corresponding simulation state update.
    *   1. A collection of completed [[TaskInstance]]s (from [[FinishingTask]] events).
    *   1. A flag that becomes true if a terminating condition (e.g. time limit) has been met.
    *
    * @param acc
    *   The result accumulated from folding so far.
    * @param event
    *   The next [[DiscreteEvent]] to handle.
    * @return
    *   The 3 computed parts.
    */
  protected def handleDiscreteEvent[F[_] : Monad](
      acc: (StateT[F, Simulation[F], Queue[Event]], Queue[TaskInstance], Boolean),
      event: DiscreteEvent
  ): (StateT[F, Simulation[F], Queue[Event]], Queue[TaskInstance], Boolean) =
    acc match {
      case (state, tasks, terminate) =>
        // TODO check time?
        event match {
          // A task is finished, but we handle this elsewhere
          case FinishingTask(_, t) => (state, tasks :+ t, terminate)

          // A simulation (workflow) is starting now
          case e: StartingCase[F] =>
            (
              state.flatMap(events =>
                StateT.fromState(startCase(e.caseRef).map(e => Monad[F].pure(events :+ e)))
              ),
              tasks,
              terminate
            )

          case TimeLimit(_) => (state, tasks, true)

          case arrival: Arrival[F, ?] =>
            arrival.next() match {
              case None => (state, tasks, terminate)
              case Some(f) => {
                val update: StateT[F, Simulation[F], Event] = StateT { sim =>
                  for {
                    ret <- f
                    (next, caseRef) = ret
                    newsim = sim.copy(events = sim.events + next)

                  } yield (startCase(caseRef).run(newsim).value)
                }
                (state.flatMap(events => update.map(e => events :+ e)), tasks, terminate)
              }
            }

          case _ =>
            (
              state.flatMap(events =>
                StateT { sim =>
                  Monad[F].pure((sim, events :+ sim.error(s"Failed to handle event: $event")))
                }
              ),
              tasks,
              terminate
            )
        }
    }

  /**
    * Aborts all simulations and stops immediately.
    *
    * @return
    *   The state update.
    */
  def stop[F[_] : Monad](): StateT[F, Simulation[F], Seq[Event]] =
    abortAll()
      .modify(_.copy(events = EventQueue()))
      .flatMap { events => StateT.fromState(finish().map(e => Monad[F].pure(events ++ e))) }

  /**
    * Indicates the simulation has completed.
    *
    * Publishes a [[com.workflowfm.proter.events.EDone EDone]].
    *
    * @return
    *   The state update.
    */
  def finish[F[_]](): State[Simulation[F], Seq[Event]] = State { sim =>
    (sim, Seq(EDone(sim.id, sim.time))) // TODO do we want to mark the simulation as done?
  }

  /**
    * Produces an [[events.EError EError]] indicating a [[cases.CaseRef CaseRef]] in the waiting
    * list is unknown.
    *
    * @see
    *   [[Simulation.notifyCase notifyCase]]
    *
    * @param caseName
    *   The name of the [[cases.CaseRef CaseRef]] we were supposed to notify.
    * @return
    *   The state update.
    */
  def unknownCaseToNotify[F[_] : Monad](caseName: String): StateT[F, Simulation[F], Seq[Event]] =
    StateT { sim =>
      Monad[F].pure(
        (sim.ready(caseName), Seq(sim.error(s"Skipping waiting for unknown case: $caseName")))
      )
    }

}
