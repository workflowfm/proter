package com.workflowfm.proter

import cats.Monad
import cats.data.{ State, StateT }
import cats.effect.std.Random
import cats.implicits.*

import cases.*
import cases.given_Case_F_Task
import events.*
import schedule.*
import state.*

final case class Scenario[F[_] : Monad](name: String, state: Simulationx.SimState[F])
    extends ScenarioState
    with StateOps {

  import Simulationx._

  def and(s: SimState[F]): Scenario[F] =
    copy(state = compose2(state, s))

  def withResource(r: Resource): Scenario[F] =
    and(liftSingleState(addResource(r)))

  def withResources(rs: Seq[Resource]): Scenario[F] =
    and(liftSeqState(addResources(rs)))

  def withCase[T](n: String, t: T)(using ct: Case[F, T]): Scenario[F] =
    and(liftSingleStateT(addCaseNow(n, t)))

  def withTimedCase[T](n: String, time: Long, t: T)(using ct: Case[F, T]): Scenario[F] =
    and(liftSingleStateT(addCase(time, n, t)))

  def withCases[T](cases: (String, T)*)(using ct: Case[F, T]): Scenario[F] =
    and(addCasesNow(cases))

  def withTimedCases[T](cases: (Long, String, T)*)(using ct: Case[F, T]): Scenario[F] =
    and(addCases(cases))

  def withArrival[T](
      n: String,
      t: T,
      rate: LongDistribution,
      limit: Int
  )(using ct: Case[F, T], r: Random[F]): Scenario[F] =
    and(liftSingleStateT(addArrivalNow(n, t, rate, Some(limit))))

  def withTimedArrival[T](
      n: String,
      time: Long,
      t: T,
      rate: LongDistribution,
      limit: Int
  )(using ct: Case[F, T], r: Random[F]): Scenario[F] =
    and(liftSingleStateT(addArrival(time, n, t, rate, Some(limit))))

  def withInfiniteArrival[T](
      n: String,
      t: T,
      rate: LongDistribution
  )(using ct: Case[F, T], r: Random[F]): Scenario[F] =
    and(liftSingleStateT(addArrivalNow(n, t, rate, None)))

  def withTimedInifiniteArrival[T](
      n: String,
      time: Long,
      t: T,
      rate: LongDistribution
  )(using ct: Case[F, T], r: Random[F]): Scenario[F] =
    and(liftSingleStateT(addArrival(time, n, t, rate, None)))

  def withLimit(t: Long): Scenario[F] =
    and(liftSingleState(limit(t)))

  def withStartingTime(t: Long): Scenario[F] =
    and(StateT(sim => Monad[F].pure((sim.copy(time = t), Seq()))))
}

object Scenario extends StateOps {
  def apply[F[_] : Monad](name: String): Scenario[F] = Scenario(name, idState)

  def apply[F[_] : Monad](name: String, start: Long): Scenario[F] =
    Scenario(name).withStartingTime(start)
}
