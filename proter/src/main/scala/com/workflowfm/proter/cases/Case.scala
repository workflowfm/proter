package com.workflowfm.proter
package cases

import state.Simulationx.SimState

import cats.Monad
import cats.implicits.*
import cats.data.StateT
import cats.effect.std.{ Random, UUIDGen }

import java.util.UUID

trait Case[F[_], T] {
  def init(name: String, t: T): F[CaseRef[F]]
}

given [F[_]](using Monad[F], UUIDGen[F], Random[F]): Case[F, Task] with {

  override def init(name: String, t: Task): F[CaseRef[F]] = for {
    uuid <- UUIDGen[F].randomUUID
  } yield new CaseRef[F] {

    val id: UUID = t.id.getOrElse(uuid)
    val theTask: Task = t.withID(id)

    override val caseName: String = name
    override def run(): F[SimState[F]] = Monad[F].pure(addTask(caseName)(theTask))
    override def stop(): F[Unit] = Monad[F].pure(())

    override def completed(time: Long, tasks: Seq[TaskInstance]): F[SimState[F]] =
      tasks.find(_.id == id) match {
        case None => Monad[F].pure(StateT.pure(Seq()))
        case Some(ti) => Monad[F].pure(succeed((ti, time)))
      }
  }
}

object Case {

  /**
    * Exception used when aborting a [[Task]].
    *
    * @param cause
    *   An optional underlying cause.
    */
  final case class TaskAbortedException(private val cause: Throwable = None.orNull)
      extends Exception("Task has been aborted", cause)

  /**
    * Exception used when the case is stopping.
    *
    * @param cause
    *   An optional underlying cause.
    */
  final case class CaseStoppingException(private val cause: Throwable = None.orNull)
      extends Exception("Simulation is stopping", cause)

}
