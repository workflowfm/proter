package com.workflowfm.proter
package cases

import events.Event
import state.{ Simulationx, CaseState }

import cats.{ Monad, FlatMap, Monoid }
import cats.data.StateT
import cats.effect.std.Random
import cats.implicits.*

import java.util.UUID
import scala.collection.immutable.{ HashSet, Queue }
import scala.util.Try



/**
  * A responses of a case to an event.
  *
  */
sealed trait CaseResponse[F[_] : FlatMap] {

  def +(response: CaseResponse[F]): CaseResponse[F] = (this, response) match {
    case (CaseUpdate(s1), CaseUpdate(s2)) => CaseUpdate(s1.flatMap( evts => s2.map( evts2 => evts ++ evts2 )))
    case (CaseUpdate(_), _) => response
    case _ => this
  }
/*
  def ++(responses: Seq[CaseResponse]): CaseResponse = responses.fold(this)(_ + _)
 */ 
}


object CaseResponse {
//  def apply[F[_]](ref: CaseRef[F]): CaseResponse[F] = CaseUpdate(ref)

  def empty[F[_] : Monad]: CaseResponse[F] = CaseUpdate(StateT.empty)
}

given[F[_]](using Monad[F]): Monoid[CaseResponse[F]] with {
  def empty: CaseResponse[F] = CaseUpdate(StateT.empty)
  def combine(x: CaseResponse[F], y: CaseResponse[F]): CaseResponse[F] = x + y
}

//[F[_] : Monad]

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
case class CaseUpdate[F[_] : FlatMap](
  update: StateT[F, Simulationx[F], Seq[Event]]
) extends CaseResponse[F]

object CaseUpdate extends CaseState {
  def task[F[_]: Random : Monad](caseName: String, task: Task): CaseUpdate[F] =
    CaseUpdate(addTask(caseName)(task))
}

/**
  * Response issued when the case has completed.
  *
  * @param case
  *   The completed case.
  * @param result
  *   The (successful or failed) result of the case.
  */
case class CaseDone[F[_] : FlatMap](result: Try[Any]) extends CaseResponse[F]
