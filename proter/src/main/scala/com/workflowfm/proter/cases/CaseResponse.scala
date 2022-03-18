package com.workflowfm.proter
package cases

import cats.Monad
import cats.implicits._

import java.util.UUID
import scala.collection.immutable.{ HashSet, Queue }
import scala.util.Try

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
  val caseRef: CaseRef[?]

  def +(response: CaseResponse): CaseResponse = (this, response) match {
    case (CaseReady(ref, t1, a1, _), CaseReady(_, t2, a2, l)) => CaseReady(ref, t1 ++ t2, a1 ++ a2, l)
    case (CaseReady(_, _, _, _), _) => response
    case _ => this
  }

  def ++(responses: Seq[CaseResponse]): CaseResponse = responses.fold(this)(_ + _)

}


object CaseResponse {
  def apply(ref: CaseRef[?]): CaseResponse = CaseReady(ref)

  def empty[F[_] : Monad](ref: CaseRef[?]): F[CaseResponse] = Monad[F].pure(CaseResponse(ref))
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
    override val caseRef: CaseRef[?],
    tasks: Queue[Task],
    abort: HashSet[UUID],
    lookahead: Lookahead
) extends CaseResponse 

object CaseReady {
  def apply(ref: CaseRef[?]): CaseReady = CaseReady(ref, Queue(), HashSet(), NoLookahead)
}
/**
  * Response issued when the case has completed.
  *
  * @param case
  *   The completed case.
  * @param result
  *   The (successful or failed) result of the case.
  */
case class CaseDone(override val caseRef: CaseRef[?], result: Try[Any]) extends CaseResponse



