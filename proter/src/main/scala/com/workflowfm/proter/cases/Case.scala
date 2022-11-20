package com.workflowfm.proter
package cases

import state.Simulation.SimState

import cats.Monad
import cats.implicits.*
import cats.data.StateT
import cats.effect.std.{ Random, UUIDGen }

import java.util.UUID

/**
  * Typeclass for the generation of simulation cases based on a custom object.
  *
  * This functions as a builder for new [[CaseRef]]s given a custom class that can describe the
  * simulation logic.
  *
  * @tparam F
  *   The effect type for the case.
  * @tparam T
  *   The object type / argument of the type class.
  */
trait Case[F[_], T] {

  /**
    * Initialize a new [[CaseRef]].
    *
    * This method provides the case count and simulation time as additional parameters. These can be
    * useful in dynamic cases whose behaviour changes based on how many times the case has started
    * or the current time (e.g. if timing is during a virtual weekend).
    *
    * @param name
    *   A (assumed unique) name to be given to the [[CaseRef]].
    * @param count
    *   The number of cases generated in this simulation (e.g. by an arrival).
    * @param time
    *   The virtual time when this [[CaseRef]] is being generated during simulation.
    * @param t
    *   The object to use in order to generate the [[CaseRef]].
    * @return
    *   The generated [[CaseRef]].
    */
  def init(name: String, count: Int, time: Long, t: T): F[CaseRef[F]]
}

/**
  * Case consisting of a single task.
  */
given [F[_]](using Monad[F], UUIDGen[F], Random[F]): Case[F, Task] with {

  override def init(name: String, count: Int, time: Long, t: Task): F[CaseRef[F]] = for {
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
