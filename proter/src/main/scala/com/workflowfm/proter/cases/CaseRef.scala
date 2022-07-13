package com.workflowfm.proter
package cases

import events.Event
import state.{ Simulation, CaseState }
import state.Simulation.SimState

import cats.{ Monad, Monoid }
import cats.data.{ StateT, State }
import cats.implicits.*
import cats.effect.std.{ Random, UUIDGen }

import java.util.UUID
import scala.collection.immutable.{ HashSet, Queue }
import scala.util.{ Try, Success, Failure }

/**
  * An abstract reference to simulation case logic.
  *
  * Includes the basic interface that we expect from a simulation case:
  *   1. Starting the case.
  *   1. Notifying when tasks complete.
  *   1. Stopping/aborting the case.
  */
trait CaseRef[F[_] : Monad] extends CaseState {
  /**
    * A unique name for the case.
    *
    * @return
    *   The name of the case.
    */
  def caseName: String

  /**
    * Starts the case.
    */
  def run(): F[SimState[F]]

  /**
    * Stops/aborts the case.
    */
  def stop(): F[Unit]

  /**
    * Notifies the case that some of its tasks completed.
    *
    * @param time
    *   The current virtual timestamp.
    * @param tasks
    *   The [[TaskInstance]]s that completed.
    */
  def completed(time: Long, tasks: Seq[TaskInstance]): F[SimState[F]]

  /**
    * Declares that the simulation completed.
    *
    * @group act
    * @param result
    *   The result of the simulation.
    */
  def done(result: Try[Any]): SimState[F] = caseDone(caseName, result)

  /**
    * Declares that the simulation completed successfully.
    *
    * @group act
    * @param result
    *   The successful result of the simulation.
    */
  def succeed(result: Any): SimState[F] = done(Success(result))

  /**
    * Declares that the simulation has failed or has been aborted.
    *
    * @group act
    * @param exception
    *   The `Throwable` that caused the failure.
    */
  def fail(exception: Throwable): SimState[F] = done(Failure(exception))

  def update(c: CaseRef[F]): SimState[F] = updateCase(caseName, c)

}

abstract class StatefulCaseRef[F[_] : Monad, S](val state: S) extends CaseRef[F] {

  def updateState(newState: S): StatefulCaseRef[F, S]

  def complete(task: TaskInstance, time: Long): StateT[F, S, SimState[F]]

  override def completed(time: Long, tasks: Seq[TaskInstance]): F[SimState[F]] = {
    val fold = tasks.foldLeft(StateT.pure[F, S, SimState[F]](StateT.pure(Seq()))) { (state, task) =>
      composeStates(state, complete(task, time))
    }
    compose(fold)
  }

  def succeedState(result: Any): StateT[F, S, SimState[F]] = StateT.pure(succeed(result))

  def failState(exception: Throwable): StateT[F, S, SimState[F]] = StateT.pure(fail(exception))

  def composeStates(
      s1: StateT[F, S, SimState[F]],
      s2: StateT[F, S, SimState[F]]
  ): StateT[F, S, SimState[F]] =
    s1.flatMap { r1 => s2.map { r2 => Simulation.compose(r1, r2) } }

  val idState: StateT[F, S, SimState[F]] = StateT.pure(StateT.pure(Seq()))

  def compose(s: StateT[F, S, SimState[F]]): F[SimState[F]] =
    s.modify(s => update(updateState(s))).run(state).map((s1, s2) => Simulation.compose(s1, s2))

}

abstract class AsyncCaseRef[F[_] : Monad : UUIDGen : Random](callbackMap: CallbackMap[F])
    extends StatefulCaseRef[F, CallbackMap[F]](callbackMap) {

  type Callback = AsyncCaseRef.Callback[F, CallbackMap[F]]

  /**
    * Creates a simple success callback from a function.
    *
    * Does nothing on failure.
    *
    * @param f
    *   The function to call on success.
    * @return
    *   The created [[Callback]].
    */
  def callback(f: (TaskInstance, Long) => StateT[F, CallbackMap[F], SimState[F]]): Callback =
    t => t.map { arg => f(arg._1, arg._2) }.getOrElse(StateT.pure(StateT.pure(Seq())))

  /**
    * Declare a new [[Task]] that needs to be sent to the [[Coordinator]] for simulation with a
    * pre-determined ID.
    *
    * The provided callback function will be called when the corresponding [[Task]] is completed.
    *
    * When it finishes executing, it must notify the [[Coordinator]] either by acknowledging the
    * completed task using [[ack]] or by completing the simulation using [[done]], [[succeed]] or
    * [[fail]].
    *
    * The [[ready]] method can also be called if there is no need to acknowledge completed tasks
    * individually. This is unlikely in the current scenario where each task has its own callback,
    * but it's still worth mentioning.
    *
    * @group act
    *
    * @param t
    *   The [[Task]] to send.
    * @param callback
    *   The [[Callback]] function to be called when the corresponding [[Task]] completes.
    */

  def task(
      t: Task,
      callback: Callback
  ): StateT[F, CallbackMap[F], SimState[F]] = StateT(m =>
    for {
      id <- t.id.map(Monad[F].pure).getOrElse(UUIDGen[F].randomUUID)
      updated = CallbackMap(m.m + (id -> callback))
    } yield ((updated, addTask(caseName)(t.withID(id))))
  )

  /**
    * @inheritdoc
    *
    * Calls the corresponding [[Callback]] in the `tasks` map and then removes the entry.
    *
    * @group react
    *
    * @param task
    *   The [[TaskInstance]] that completed.
    * @param time
    *   The timestamp of its completion.
    */

  override def complete(task: TaskInstance, time: Long): StateT[F, CallbackMap[F], SimState[F]] =
    StateT(m => {
      m.m.get(task.id) match {
        case None => Monad[F].pure((m, StateT.pure(Seq())))
        case Some(f) => f(Success(task, time)).run(CallbackMap(m.m - task.id))
      }
    })

  /*
  /**
   * @inheritdoc
   *
   * Calls respective [[Callback]]s with `Failure`.
   *
   * @group act
   *
   * @param id
   *   The `UUID` of the [[Task]]s.
   */
  def abortTask(ids: UUID*): F[CaseResponse] = {
    val response = super.abort(ids *)
    for {
      fs <- callbacks.modify { m => (m -- ids, ids.flatMap( id => m.get(id) )) }
      _ = fs.map( f => f(Failure(Case.TaskAbortedException())) )
    } yield (response)
  }
   */

  /**
    * @inheritdoc
    *
    * Triggers all callbacks with a `Failure`.
    */
  override def stop(): F[Unit] = {
    val update = state.m.map((_, f) => f(Failure(Case.CaseStoppingException()))).toList.sequence
    update.run(state).map(_ => ())
  }
}

object AsyncCaseRef {
  type Callback[F[_], S] = Monad[F] ?=> Try[(TaskInstance, Long)] => StateT[F, S, SimState[F]]
}

case class CallbackMap[F[_]](m: Map[UUID, AsyncCaseRef.Callback[F, CallbackMap[F]]]) {
  type Callback = AsyncCaseRef.Callback[F, CallbackMap[F]]
  def +(kv: (UUID, Callback)): CallbackMap[F] = copy(m = m + kv)
  def get(k: UUID): Option[Callback] = m.get(k)
  def -(k: UUID): CallbackMap[F] = copy(m = m - k)
  def contains(k: UUID): Boolean = m.contains(k)
}
