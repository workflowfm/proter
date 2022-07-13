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

case class Simulationx[F[_]](
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

  def error(description: String): EError = EError(id, time, description)

  def fatalError(description: String): Seq[Event] =
    Seq(EError(id, time, description), EDone(id, time))

  def ready(name: String): Simulationx[F] = copy(waiting = waiting - name)

  def nextTasks(): Iterable[TaskInstance] = scheduler.getNextTasks(time, tasks, resources)

  def notifyCase(waiting: (String, Seq[TaskInstance]))(using Monad[F]): F[Simulationx.SimState[F]] =
    waiting match {
      case (caseName, tasks) =>
        cases.get(caseName) match {
          case None => Monad[F].pure(Simulationx.unknownCase(caseName))
          case Some(c) => if tasks.isEmpty then c.run() else c.completed(time, tasks)
        }
    }

  def notifyCases(using Monad[F]): F[(Simulationx[F], Seq[Event])] =
    waiting.map(notifyCase).toSeq.sequence.map(Simulationx.compose(_: _*).run(this)).flatten

  def start(
      state: StateT[F, Simulationx[F], Seq[Event]]
  )(using Monad[F]): F[(Simulationx[F], Seq[Event])] =
    state
      .flatMap(events =>
        // Issue the EStart event at the time set by the initial state
        // (e.g. through `Scenario.withStartingTime`)
        StateT.inspect[F, Simulationx[F], Long](_.time).map(t => events :+ EStart(id, t))
      )
      .run(this)

  def tick(using Monad[F]): F[(Either[Simulationx[F], Unit], Seq[Event])] = {
    /* if !waiting.isEmpty // still waiting for responses!
     * then Monad[F].pure((Right(()), fatalError(s"Called `tick()` even though I am still waiting
     * for: ${waiting}"))) else */
    if (events.isEmpty && tasks.isEmpty && cases.isEmpty) // finished!
    then Monad[F].pure((Right(()), Seq(EDone(id, time))))
    else
      events.next() match {
        case Some((nextTick, toHandle, updatedEventQueue)) => { // Are events pending?
          val updateEventQueue: StateT[F, Simulationx[F], Queue[Event]] = StateT { sim =>
            Monad[F].pure(
              (
                sim.copy(time = nextTick, events = updatedEventQueue, waiting = Map()),
                Queue[Event]()
              )
            )
          }
          val (handledState, tasks, terminate) = toHandle.foldLeft((updateEventQueue, Queue[TaskInstance](), false))(
            Simulationx.handleDiscreteEvent
          )

          val stopState = StateT.fromState(Simulationx.stopTasks[F](tasks).map(Monad[F].pure))

          val notifyState = StateT { (sim: Simulationx[F]) => sim.notifyCases }

          val allocateState = Simulationx.allocateTasks()

          val terminateState = if terminate then Simulationx.stop() else Simulationx.idState

          Seq(handledState.map(_.toSeq), stopState, notifyState, allocateState, terminateState).sequence
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

object Simulationx extends StateOps {
  type SimState[F[_]] = StateT[F, Simulationx[F], Seq[Event]]

  def apply[F[_]](id: String, scheduler: Scheduler): Simulationx[F] = Simulationx[F](
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

  def applyState[F[_] : Monad](
      r: Either[Simulationx[F], Unit],
      s: StateT[F, Simulationx[F], Seq[Event]]
  ): F[(Either[Simulationx[F], Unit], Seq[Event])] = r match {
    case Left(sim) =>
      for {
        result <- s.run(sim)
        (update, events) = result
      } yield ((Left(update), events))
    case Right(u) => Monad[F].pure((r, Seq()))
  }

  /**
    * Start a [[CaseRef]].
    *
    * Once the case starts, we expect to hear from it in case it wants to add some [[Task]]s. We
    * therefore add it to the waiting queue.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimStart ESimStart]].
    *
    * @group simulations
    * @param caseRef
    *   The [[CaseRef]] to start.
    */
  def startCase[F[_]](caseRef: CaseRef[F]): State[Simulationx[F], Event] = State(sim =>
    (
      sim.copy(
        waiting = sim.waiting + (caseRef.caseName -> Seq()),
        cases = sim.cases + (caseRef.caseName -> caseRef)
      ),
      ECaseStart(sim.id, sim.time, caseRef.caseName)
    )
  )

  /**
    * Stops a case when it is done.
    *
    *   - Removes the simulation from the list of running simulations and the waiting list.
    *   - Removes any [[Lookahead]] from the [[schedule.Scheduler Scheduler]].
    *   - Publishes a [[com.workflowfm.proter.events.ESimEnd ESimEnd]].
    *   - Calls [[ready]] to handle the fact that we no longer need to wait for this simulation.
    *
    * @group simulations
    * @param name
    *   The name of the completed simulation.
    * @param result
    *   A string representation of the output of the simulation.
    */
  def stopCase[F[_] : Monad](name: String, result: String): StateT[F, Simulationx[F], Seq[Event]] =
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
    * Stops/aborts a simulation before it is done.
    *
    *   - Removes the simulation from the list of running simulations and from the waiting list.
    *   - Detaches all tasks of the simulation from the resources and adds them to the abort list.
    *   - Publishes a [[com.workflowfm.proter.events.ETaskAbort ETaskAbort]] for each aborted task
    *     and associated [[com.workflowfm.proter.events.ETaskDetach ETaskDetach]] for each released
    *     resource.
    *   - Removes all queued tasks of the simulation from the scheduler.
    *   - Publishes a [[com.workflowfm.proter.events.ESimEnd ESimEnd]].
    *   - Asks the simulation to stop.
    *   - Does '''not''' progress time.
    *
    * The bookkeeping assumes the rest of the simulation(s) can still proceed.
    *
    * @group simulations
    * @param simulation
    *   The [[Simulation]] to stop.
    */
  def abortCase[F[_] : Applicative](caseRef: CaseRef[F]): StateT[F, Simulationx[F], Seq[Event]] =
    StateT(sim => {
      val name = caseRef.caseName
      val tasksToAbort = sim.events.tasksOf(name)

      val (abortedTasks, abortEvents) = abortTasks(tasksToAbort.toSeq).run(sim).value

      val result = abortedTasks.copy(
        cases = abortedTasks.cases - name,
        waiting = abortedTasks.waiting - name,
        tasks = abortedTasks.tasks.filter(_.simulation != name)
      )

      val events = ECaseEnd(sim.id, sim.time, name, "[Simulation Aborted]") :: abortEvents.toList

      caseRef.stop().map(_ => (result, events))
    })

  /**
    * Aborts all currently running simulations.
    *
    * Calls [[abortSimulation]] for each of them.
    * @group simulations
    */
  protected def abortAll[F[_] : Monad](): StateT[F, Simulationx[F], Seq[Event]] = StateT(sim => {
    sim.cases.values.toSeq.traverse(abortCase[F]).map(_.flatten).run(sim)
  })

  /**
    * Start a [[TaskInstance]] at the current timestamp.
    *
    * A [[TaskInstance]] is started when scheduled by the [[schedule.Scheduler Scheduler]]. This
    * assumes all the [[TaskResource]]s it needs are available.
    *
    *   - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *   - Calls [[TaskResource.startTask]] for each involved [[TaskResource]] to attach this
    *     [[TaskInstance]] to them. Publishes a
    *     [[com.workflowfm.proter.events.ETaskAttach ETaskAttach]] for each successful attachment.
    *     Otherwise publishes an appropriate [[com.workflowfm.proter.events.EError EError]]. The
    *     latter would only happen if the [[schedule.Scheduler Scheduler]] tried to schedule a
    *     [[Task]] to a busy [[TaskResource]].
    *   - Creates a [[FinishingTask]] event for this [[Task]] based on its duration, and adds it to
    *     the even queue.
    *
    * @group tasks
    * @param task
    *   The [[TaskInstance]] to be started.
    */
  def startTask[F[_]](task: TaskInstance): State[Simulationx[F], Seq[Event]] = State(sim =>
    sim.resources.startTask(task, sim.time) match {
      case Left(busy) =>
        (
          sim,
          Seq(
            sim.error(
              s"Tried to attach task [${task.name}](${task.simulation}) to [${busy.resource.name}], but it was already attached to [${busy.task.name}](${busy.task.simulation}) since time [${busy.start}]."
            )
          )
        )
      case Right(startedMap) =>
        (
          sim.copy(
            events = sim.events + FinishingTask(sim.time + task.duration, task),
            tasks = sim.tasks - task
          ),
          ETaskStart(sim.id, sim.time, task) :: task.resources.map { r =>
            ETaskAttach(sim.id, sim.time, task, r)
          }.toList
        )
    }
  )

  /**
    * Aborts one or more [[TaskInstance]]s.
    *
    *   - Detaches all associated [[TaskResource]]s publishing a
    *     [[com.workflowfm.proter.events.ETaskDetach ETaskDetach]] each time.
    *   - Adds the task ID to the [[abortedTasks]] set.
    *   - Publishes a [[com.workflowfm.proter.events.ETaskAbort ETaskAbort]].
    *
    * @group tasks
    * @param id
    *   The `UUID`s of the [[TaskInstance]]s that need to be aborted.
    */
  def abortTasks[F[_]](ids: Seq[UUID]): State[Simulationx[F], Seq[Event]] = State(sim => {
    val (abortMap, abortResources) = sim.resources.stopTasks(ids)
    val abortEvents = ids.map { taskid => ETaskAbort(sim.id, sim.time, taskid) }
    val detachEvents = abortResources.flatMap(ETaskDetach.resourceState(sim.id, sim.time)).toSeq
    (
      sim.copy(resources = abortMap, abortedTasks = sim.abortedTasks ++ ids),
      detachEvents ++ abortEvents
    )
  })

  /**
    * Handles a group of [[TaskInstance]]s of the same simulation that has just finished.
    *
    * Assumes all instances belong to the same simulation.
    *
    *   - Notifies the [[schedule.Scheduler Scheduler]] about each task comleting.
    *   - Publishes a [[com.workflowfm.proter.events.ETaskDone ETaskDone]].
    *   - Notifies the [[Simulation]] that its [[TaskInstance]]s have finished. This happens in the
    *     same thread if `singleThread` is `true` or using a `Future` otherwise.
    *
    * Note that resources are detached before this in [[tick]] using [[filterFinishingTasks]].
    *
    * @group tasks
    * @param taskGroup
    *   The simulation name paired with the [[TaskInstance]]s that need to be stopped.
    */
  def stopTasks[F[_]](tasks: Seq[TaskInstance]): State[Simulationx[F], Seq[Event]] = State(sim => {
    val (someAbortedTasks, nonAbortedTasks) = tasks.partition(t => sim.abortedTasks.contains(t.id))
    val (stopMap, detachedResources) = sim.resources.stopTasks(nonAbortedTasks.map(_.id))
    val stopEvents = nonAbortedTasks.map { task => ETaskDone(sim.id, sim.time, task) }
    val detachEvents = detachedResources.flatMap(ETaskDetach.resourceState(sim.id, sim.time)).toSeq
    val waits = nonAbortedTasks.groupBy(_.simulation)
    (
      sim.copy(
        resources = stopMap,
        waiting = sim.waiting ++ waits,
        abortedTasks = sim.abortedTasks -- someAbortedTasks.map(_.id)
      ),
      detachEvents ++ stopEvents
    )
  })

  def allocateTasks[F[_] : Monad](): StateT[F, Simulationx[F], Seq[Event]] = StateT { sim =>
    StateT
      .fromState(
        sim.nextTasks().map(startTask[F]).toSeq.sequence.map(e => Monad[F].pure(e.flatten))
      )
      .run(sim)
  }

  /**
    * Handles all [[DiscreteEvent]]s other than [[FinishingTask]].
    *
    * @param event
    */
  protected def handleDiscreteEvent[F[_] : Monad](
      acc: (StateT[F, Simulationx[F], Queue[Event]], Queue[TaskInstance], Boolean),
      event: DiscreteEvent
  ): (StateT[F, Simulationx[F], Queue[Event]], Queue[TaskInstance], Boolean) = 
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
                val update: StateT[F, Simulationx[F], Event] = StateT { sim =>
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
    * @group toplevel
    */
  def stop[F[_] : Monad](): StateT[F, Simulationx[F], Seq[Event]] =
    abortAll()
      .modify(_.copy(events = EventQueue()))
      .flatMap { events => StateT.fromState(finish().map(e => Monad[F].pure(events ++ e))) }

  /**
    * Shuts down the entire simulation and shuts down the actor.
    *
    * Publishes a [[com.workflowfm.proter.events.EDone EDone]].
    *
    * Fulfils the completion [[promise]].
    *
    * @group toplevel
    */
  def finish[F[_]](): State[Simulationx[F], Seq[Event]] = State { sim =>
    (sim, Seq(EDone(sim.id, sim.time))) // TODO do we want to mark the simulation as done?
  }

  def unknownCase[F[_] : Monad](caseName: String): StateT[F, Simulationx[F], Seq[Event]] = StateT {
    sim =>
      Monad[F].pure(
        (sim.ready(caseName), Seq(sim.error(s"Skipping waiting for unknown case: $caseName")))
      )
  }

}
