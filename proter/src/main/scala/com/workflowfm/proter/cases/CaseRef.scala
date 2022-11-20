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
  * A single instance of a simulation case.
  *
  * @tparam F
  *   The effect type.
  */
trait CaseRef[F[_] : Monad] extends CaseState {

  /**
    * A unique name for the instance.
    *
    * @return
    *   The name of the instance.
    */
  def caseName: String

  /**
    * Starts the case.
    *
    * @return
    *   The effect starting should have to the simulation state.
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
    * @return
    *   The effect this should have to the simulation state.
    */
  def completed(time: Long, tasks: Seq[TaskInstance]): F[SimState[F]]

  /**
    * A simulation state where the case instance has completed.
    *
    * @param result
    *   The final result of the case instance.
    * @return
    *   The effect to the simulation state.
    */
  def done(result: Try[Any]): SimState[F] = caseDone(caseName, result)

  /**
    * A simulation state where the case instance has completed successfully.
    *
    * @param result
    *   The successful result of the case instance.
    */
  def succeed(result: Any): SimState[F] = done(Success(result))

  /**
    * A simulation state where the case instance has failed or was aborted.
    *
    * @param exception
    *   The `Throwable` that caused the failure.
    */
  def fail(exception: Throwable): SimState[F] = done(Failure(exception))

  /**
    * A simulation state that updates the instance itself in the simulation.
    *
    * @param c
    *   The updated case instance.
    */
  def update(c: CaseRef[F]): SimState[F] = updateCase(caseName, c)

}

/**
  * A [[CaseRef]] with an explicit state parameter.
  *
  * @tparam F
  *   The effect type.
  * @param state
  *   The current state.
  */
abstract class StatefulCaseRef[F[_] : Monad, S](val state: S) extends CaseRef[F] {

  /**
    * A copy constructor for this case instance.
    *
    * This is used to construct copies of subclasses of `StatefulCaseRef`.
    *
    * @param newState
    *   The new state to use in the copy.
    * @return
    *   A copy of the instance with the new state.
    */
  def updateState(newState: S): StatefulCaseRef[F, S]

  /**
    * Computes the simulation and internal state update from a single completed task.
    *
    * @param task
    *   The [[TaskInstance]] that completed.
    * @param time
    *   The current virtual timestamp.
    * @return
    *   A `StateT` monad of the effect to the internal state and the resulting simulation state
    *   effect.
    */
  def complete(task: TaskInstance, time: Long): StateT[F, S, SimState[F]]

  /**
    * @inheritdoc
    *
    * Updates the internal state based on the effect from each individual completed task.
    */
  override def completed(time: Long, tasks: Seq[TaskInstance]): F[SimState[F]] = {
    val fold = tasks.foldLeft(StateT.pure[F, S, SimState[F]](StateT.pure(Seq()))) { (state, task) =>
      composeStates(state, complete(task, time))
    }
    applyState(fold)
  }

  /**
    * Shortcut to succeed directly within [[complete]].
    *
    * @see
    *   [[succeed]]
    * @param result
    *   The successful result of the case instance.
    */
  def succeedState(result: Any): StateT[F, S, SimState[F]] = StateT.pure(succeed(result))

  /**
    * Shortcut to fail directly within [[complete]].
    *
    * @see
    *   [[fail]]
    * @param exception
    *   The `Throwable` that caused the failure.
    */
  def failState(exception: Throwable): StateT[F, S, SimState[F]] = StateT.pure(fail(exception))

  /**
    * Compose 2 state updates together.
    *
    * State types are the ones returned by [[complete]].
    *
    * @param s1
    *   The first state.
    * @param s2
    *   The second state.
    * @return
    *   The composed single state.
    */
  def composeStates(
      s1: StateT[F, S, SimState[F]],
      s2: StateT[F, S, SimState[F]]
  ): StateT[F, S, SimState[F]] =
    s1.flatMap { r1 => s2.map { r2 => Simulation.compose(r1, r2) } }

  /**
    * A state update that does nothing.
    */
  val idState: StateT[F, S, SimState[F]] = StateT.pure(StateT.pure(Seq()))

  /**
    * Update the internal state and then the simulation state.
    *
    * @see
    *   [[update]]
    * @param s
    *   The state update to use.
    * @return
    *   The resulting simulation state update.
    */
  def applyState(s: StateT[F, S, SimState[F]]): F[SimState[F]] =
    s.modify(s => update(updateState(s))).run(state).map((s1, s2) => Simulation.compose(s1, s2))

}

/**
  * A [[CaseRef]] that can asynchronously react to completed tasks using callback functions.
  *
  * @tparam F
  *   The effect type.
  * @param callbackMap
  *   A map of callback functions to be used.
  */
abstract class AsyncCaseRef[F[_] : Monad : UUIDGen : Random](callbackMap: CallbackMap[F])
    extends StatefulCaseRef[F, CallbackMap[F]](callbackMap) {

  /**
    * A shortcut to the type of a callback function.
    */
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
    * State update that adds a new [[Task]] for simulation and a corresponding callback.
    *
    * The provided callback function will be called when the corresponding [[Task]] is completed.
    *
    * @param t
    *   The [[Task]] to add.
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
    * Calls the corresponding [[Callback]] in the map and then removes the entry.
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

  /**
    * The type for task callbacks.
    *
    * A callback is a function from a [[TaskInstance]] and its completion timestamp to a state
    * update.
    */
  type Callback[F[_], S] = Monad[F] ?=> Try[(TaskInstance, Long)] => StateT[F, S, SimState[F]]
}

/**
  * Convenience wrapper for a map of callbacks.
  *
  * @param m
  *   The internal map.
  */
case class CallbackMap[F[_]](m: Map[UUID, AsyncCaseRef.Callback[F, CallbackMap[F]]]) {
  type Callback = AsyncCaseRef.Callback[F, CallbackMap[F]]
  def +(kv: (UUID, Callback)): CallbackMap[F] = copy(m = m + kv)
  def get(k: UUID): Option[Callback] = m.get(k)
  def -(k: UUID): CallbackMap[F] = copy(m = m - k)
  def contains(k: UUID): Boolean = m.contains(k)
}
