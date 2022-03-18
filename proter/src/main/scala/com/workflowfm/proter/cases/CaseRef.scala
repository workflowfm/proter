package com.workflowfm.proter
package cases

import cats.Monad
import cats.implicits._
import cats.effect.Ref
import cats.effect.std.UUIDGen

import java.util.UUID
import scala.collection.immutable.{ HashSet, Queue }
import scala.util.{ Try, Success, Failure }

/**
  * An abstract reference to simulation caselogic.
  *
  * Includes the basic interface that we expect from a simulation case:
  *   1. Starting the case.
  *   1. Notifying when tasks complete.
  *   1. Stopping/aborting the case.
  */
trait CaseRef[F[_]] {
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
  def run(): F[CaseResponse]

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
  def completed(time: Long, tasks: Seq[TaskInstance]): F[CaseResponse]

  /**
    * Declare a new [[Task]] that needs to be sent for simulation.
    *
    * @group act
    *
    * @param t
    *   The [[Task]] to send.
    */
  def task(t: Task): CaseResponse = CaseReady(this).copy(tasks = Queue(t))

  /**
    * Declare IDs of [[TaskInstance]]s that need to be aborted.
    *
    * @group act
    *
    * @param ids
    *   The `UUID`s of the [[TaskInstance]]s.
    */
  def abort(ids: UUID*): CaseResponse = CaseReady(this).copy(abort = HashSet(ids:_*))


  /**
    * Declares that the simulation completed.
    *
    * @group act
    * @param result
    *   The result of the simulation.
    */
  def done(result: Try[Any]): CaseResponse = CaseDone(this, result)


  /**
    * Declares that the simulation completed successfully.
    *
    * @group act
    * @param result
    *   The successful result of the simulation.
    */
  def succeed(result: Any): CaseResponse = CaseDone(this, Success(result))

  /**
    * Declares that the simulation has failed or has been aborted.
    *
    * @group act
    * @param exception
    *   The `Throwable` that caused the failure.
    */
  def fail(exception: Throwable): CaseResponse = CaseDone(this, Failure(exception))

}

abstract class StatefulCaseRef[F[_] : Monad, S](stateRef: Ref[F, S]) extends CaseRef[F] {

/*
  override def run(): F[CaseResponse] = stateRef.modify(runState).flatten

  override def stop(): F[Unit] = stateRef.modify(stopState).flatten

  def runState: S => (S, F[CaseResponse])

  def stopState: S => (S, F[Unit])
 */
  def complete(task: TaskInstance, time: Long): S => (S, F[Seq[CaseResponse]])

  def completed(time: Long, tasks: Seq[TaskInstance]): F[CaseResponse] = {
    tasks.foldLeft(CaseResponse.empty(this)) { (acc, task) =>
      for {
        result <- acc
        update <- stateRef.modify(complete(task, time))
        responses <- update
      } yield (result ++ responses)
    }
  }

}

/*
trait SyncCase[F[_] : Sync, T, S] extends Case[F, T] { self =>
  override def init(name: String, t: T): F[CaseRef[F]] = for {
    ref <- Ref[F].of(initState(t))
  } yield new StatefulCaseRef[F, S](ref) {
    override val caseName: String = name

    override def runState: S =>(S, F[CaseResponse]) = self.runState(this)

    override def stopState: S => (S, F[Unit]) = self.stopState

    override def complete(task: TaskInstance, time: Long): S => (S, F[Seq[CaseResponse]]) = self.complete(this, task, time)

  }

  def initState(t: T) : S

  def runState(ref: CaseRef[F]): S => (S, F[CaseResponse])

  def stopState: S => (S, F[Unit])

  def complete(ref: CaseRef[F], task: TaskInstance, time: Long): S => (S, F[Seq[CaseResponse]])

}
 */

abstract class AsyncCaseRef[F[_] : Monad : UUIDGen](callbacks: Ref[F, Map[UUID, AsyncCaseRef.Callback[F]]]) extends StatefulCaseRef[F, Map[UUID, AsyncCaseRef.Callback[F]]](callbacks) {

  type Callback = AsyncCaseRef.Callback[F]

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
  def callback(f: (TaskInstance, Long) => F[Seq[CaseResponse]]): Callback =
    t => t.map { arg => f(arg._1, arg._2) }.getOrElse(Monad[F].pure(Seq()))


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
  ): F[CaseResponse] = for {
    id <- t.id.map(Monad[F].pure).getOrElse(UUIDGen[F].randomUUID)
    _ <- callbacks.update( map => map + (id -> callback))
  } yield (super.task(t.withID(id)))

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
  override def complete(task: TaskInstance, time: Long): Map[UUID, Callback] => (Map[UUID, Callback], F[Seq[CaseResponse]]) = m => {
    val updatedMap = m - task.id
    m.get(task.id) match {
      case None => (m, Monad[F].pure(Seq()))
      case Some(f) => (updatedMap, f(Success(task, time)))
    }
  }

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
    val response = super.abort(ids: _*)
    for {
      fs <- callbacks.modify { m => (m -- ids, ids.flatMap( id => m.get(id) )) }
      _ = fs.map( f => f(Failure(Simulation.TaskAbortedException())) )
    } yield (response)
  }

  /**
    * @inheritdoc
    *
    * Triggers all callbacks with a `Failure`.
    */
  override def stop(): F[Unit] = for {
    fs <- callbacks.modify { m => (Map(), m.values) }
    _ = fs.map( f => f(Failure(Simulation.SimulationStoppingException())) )
  } yield ()
}

object AsyncCaseRef {
  type Callback[F[_]] = Monad[F] ?=> Try[(TaskInstance, Long)] => F[Seq[CaseResponse]]
}
 
