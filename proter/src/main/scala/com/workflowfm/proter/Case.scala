package com.workflowfm.proter

import cats.Monad
import cats.implicits._
import cats.effect.Ref

import java.util.UUID

import scala.collection.immutable.{ HashSet, Queue }
import scala.util.{ Try, Success, Failure }


/**
  * A responses of a case to an event.
  *
  */
sealed trait CaseResponse {
  /**
    * The case that issues the response.
    *
    * @return
    *   The [[CaseRef]].
    */
  val caseRef: CaseRef[?,?]

  def +(response: CaseResponse): CaseResponse = (this, response) match {
    case (CaseReady(ref, t1, a1, _), CaseReady(_, t2, a2, l)) => CaseReady(ref, t1 ++ t2, a1 ++ a2, l)
    case (CaseReady(_, _, _, _), _) => response
    case _ => this
  }

  def ++(responses: Seq[CaseResponse]): CaseResponse = responses.fold(this)(_ + _)

}


object CaseResponse {
  def apply(ref: CaseRef[?, ?]): CaseResponse = CaseReady(ref)

  def empty[F[_] : Monad](ref: CaseRef[?, ?]): F[CaseResponse] = Monad[F].pure(CaseResponse(ref))
}



/**
  * Response issued when the case has finished reacting and is now waiting for virtual time to pass.
  *
  * @param caseRef
  *   The (updated) case that issues the response.
  * @param tasks
  *   A sequence of new tasks to be added for scheduling.
  * @param abort
  *   A sequence of IDs of existing tasks that need to be aborted.
  * @param lookahead
  *   An updated [[Lookahead]] structure.
  */
case class CaseReady(
    override val caseRef: CaseRef[?,?],
    tasks: Queue[Task],
    abort: HashSet[UUID],
    lookahead: Lookahead
) extends CaseResponse 

object CaseReady {
  def apply(ref: CaseRef[?, ?]): CaseReady = CaseReady(ref, Queue(), HashSet(), NoLookahead)
}
/**
  * Response issued when the case has completed.
  *
  * @param case
  *   The completed case.
  * @param result
  *   The (successful or failed) result of the case.
  */
case class CaseDone(override val caseRef: CaseRef[?,?], result: Try[Any]) extends CaseResponse




/**
  * An abstract reference to simulation caselogic.
  *
  * Includes the basic interface that we expect from a simulation case:
  *   1. Starting the case.
  *   1. Notifying when tasks complete.
  *   1. Stopping/aborting the case.
  */
trait CaseRef[F[_], T] {
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
  def run(): Unit

  /**
    * Stops/aborts the case.
    */
  def stop(): Unit

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




abstract class StatefulCase[F[_] : Monad, S, T](stateRef: Ref[F, S]) extends CaseRef[F,T] {

  def complete(task: TaskInstance, time: Long): S => (S, Seq[CaseResponse])

  // final : cannot be final because that prevents mockups in scalamock
  //override 
  def completed(time: Long, tasks: Seq[TaskInstance]): F[CaseResponse] = {
    tasks.foldLeft(CaseResponse.empty(this)) { (acc, task) =>
      for {
        result <- acc
        updates <- stateRef.modify(complete(task, time))
      } yield (result ++ updates)
    }
  }

}
