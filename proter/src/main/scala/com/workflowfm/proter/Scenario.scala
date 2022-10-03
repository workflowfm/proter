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

/**
  * Convenience class to construct simulation scenarios.
  *
  * Provides various functions to set simulation parameters and compile them in a
  * [[state.Simulation.SimState SimState]].
  *
  * Parameters include resources, cases/arrivals, and start and ending times.
  *
  * @note
  *   Ordering of method calls matters, especially when using [[withStartingTime]].
  *
  * @param name
  *   A name to identify the scenario.
  * @param state
  *   The current simulation state update corresponding to the parameters provided so far.
  */
final case class Scenario[F[_] : Monad](name: String, state: Simulation.SimState[F])
    extends ScenarioState
    with StateOps {

  import Simulation._

  /**
    * Composes another simulation state update directly.
    *
    * @param s
    *   The [[state.Simulation.SimState SimState]] to compose.
    * @return
    *   The updated scenario.
    */
  def and(s: SimState[F]): Scenario[F] =
    copy(state = compose2(state, s))

  /**
    * Adds a [[Resource]] to the simulation scenario.
    *
    * @param r
    *   The [[Resource]] to add.
    * @return
    *   The updated scenario.
    */
  def withResource(r: Resource): Scenario[F] =
    and(lift(addResource(r)))

  /**
    * Adds a collection of [[Resource]]s to the simulation scenario.
    *
    * @param rs
    *   The [[Resource]]s to add.
    * @return
    *   The updated scenario.
    */
  def withResources(rs: Seq[Resource]): Scenario[F] =
    and(lift(addResources(rs)))

  /**
    * Adds a [[cases.Case Case]] to the simulation scenario.
    *
    * The case will be scheduled to start in the '''current''' start time of the scenario.
    *
    * Manipulating the scenario start time ''after'' adding the case may have unintended effects,
    * such as having a case start time in the past.
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param n
    *   A unique name for the case.
    * @param t
    *   The object to use for the simulation case.
    * @return
    *   The updated scenario.
    */
  def withCase[T](n: String, t: T)(using ct: Case[F, T]): Scenario[F] =
    and(addCaseNow(n, t))

  /**
    * Adds a [[cases.Case Case]] with a specified starting time to the simulation scenario.
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param n
    *   A unique name for the case.
    * @param time
    *   The timestamp when the case should start.
    * @param t
    *   The object to use for the simulation case.
    * @return
    *   The updated scenario.
    */
  def withTimedCase[T](n: String, time: Long, t: T)(using ct: Case[F, T]): Scenario[F] =
    and(addCase(time, n, t))

  /**
    * Adds a collectiion of [[cases.Case Case]]s to the simulation scenario.
    *
    * The cases will be scheduled to start in the '''current''' start time of the scenario.
    *
    * Manipulating the scenario start time ''after'' adding the cases may have unintended effects,
    * such as having a case start time in the past.
    *
    * @tparam T
    *   The type of the objects used for the simulation cases.
    * @param cases
    *   The objects to use for each simulation case, each paired with its own unique name.
    * @return
    *   The updated scenario.
    */
  def withCases[T](cases: (String, T)*)(using ct: Case[F, T]): Scenario[F] =
    and(addCasesNow(cases))

  /**
    * Adds a collection of [[cases.Case Case]]s, each with a specified starting time, to the simulation
    * scenario.
    *
    * @tparam T
    *   The type of the objects used for the simulation case.
    * @param cases
    *   The objects to use for the simulation case, each paired with its own unique name and
    *   starting timestamp.
    * @return
    *   The updated scenario.
    */
  def withTimedCases[T](cases: (Long, String, T)*)(using ct: Case[F, T]): Scenario[F] =
    and(addCases(cases))

  /**
    * Adds a [[cases.Case Case]] with an arrival pattern to the simulation scenario.
    *
    * The first case will be scheduled to arrive in the '''current''' start time of the scenario.
    *
    * Manipulating the scenario start time ''after'' adding the cases may have unintended effects,
    * such as having a case start time in the past.
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param n
    *   A unique name for the cases.
    * @param t
    *   The object to use for the simulation case.
    * @param rate
    *   The probability ditribution determining the duration until the next arrival.
    * @param limit
    *   The maximum number of cases to generate.
    * @return
    *   The updated scenario.
    */
  def withArrival[T](
      n: String,
      t: T,
      rate: LongDistribution,
      limit: Int
  )(using ct: Case[F, T], r: Random[F]): Scenario[F] =
    and(addArrivalNow(n, t, rate, Some(limit)))

  /**
    * Adds a [[cases.Case Case]] with an arrival pattern and specified start time to the simulation scenario.
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param n
    *   A unique name for the cases.
    * @param time
    *   The timestamp when the first case should arrive.
    * @param t
    *   The object to use for the simulation case.
    * @param rate
    *   The probability ditribution determining the duration until the next arrival.
    * @param limit
    *   The maximum number of cases to generate.
    * @return
    *   The updated scenario.
    */
  def withTimedArrival[T](
      n: String,
      time: Long,
      t: T,
      rate: LongDistribution,
      limit: Int
  )(using ct: Case[F, T], r: Random[F]): Scenario[F] =
    and(addArrival(time, n, t, rate, Some(limit)))

  /**
    * Adds a [[cases.Case Case]] with an infinite arrival pattern to the simulation scenario.
    *
    * The first case will be scheduled to arrive in the '''current''' start time of the scenario.
    *
    * Manipulating the scenario start time ''after'' adding the cases may have unintended effects,
    * such as having a case start time in the past.
    *
    * The scenario will continue to generate cases infinitely until the time limit has been reached.
    *
    * @see
    *   [[Scenario.withLimit]]
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param n
    *   A unique name for the cases.
    * @param t
    *   The object to use for the simulation case.
    * @param rate
    *   The probability ditribution determining the duration until the next arrival.
    * @return
    *   The updated scenario.
    */
  def withInfiniteArrival[T](
      n: String,
      t: T,
      rate: LongDistribution
  )(using ct: Case[F, T], r: Random[F]): Scenario[F] =
    and(addArrivalNow(n, t, rate, None))

  /**
    * Adds a [[cases.Case Case]] with an infinite arrival pattern and specified start time.
    *
    * The scenario will continue to generate cases infinitely until the time limit has been reached.
    *
    * @see
    *   [[Scenario.withLimit]]
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param n
    *   A unique name for the cases.
    * @param time
    *   The timestamp when the first case should arrive.
    * @param t
    *   The object to use for the simulation case.
    * @param rate
    *   The probability ditribution determining the duration until the next arrival.
    * @return
    *   The updated scenario.
    */
  def withTimedInfiniteArrival[T](
      n: String,
      time: Long,
      t: T,
      rate: LongDistribution
  )(using ct: Case[F, T], r: Random[F]): Scenario[F] =
    and(addArrival(time, n, t, rate, None))

  /**
    * Sets a time limit for the simulation.
    *
    * All running cases will be aborted at that timestamp and the simulation will stop.
    *
    * Calling this method multiple times will result in the earliest timestamp becoming the time
    * limit.
    *
    * Setting the time limit after the start time will result in an error.
    *
    * @param t
    *   The timestamp to use as the time limit.
    * @return
    *   The updated scenario.
    */
  def withLimit(t: Long): Scenario[F] =
    and(lift(limit(t)))

  /**
    * Sets the start time for the simulation.
    *
    * This will be the first timestamp in the simulation. Cases with no specified start time will
    * start at the time set here.
    *
    * Setting the start time after some cases are already scheduled will result in an error.
    *
    * Calling this method multiple times will result in the timestamp given in the last call
    * becoming the start time.
    *
    * Setting the start time after the time limit will result in an error.
    *
    * @param t
    *   The timestamp to use as the start time.
    * @return
    *   The updated scenario.
    */
  def withStartingTime(t: Long): Scenario[F] =
    and(StateT(sim => Monad[F].pure((sim.copy(time = t), Seq()))))
}

object Scenario extends StateOps {
  /**
    * Initialise a scenario by its name.
    *
    * @param name
    *   The name to use for the scenario.
    * @return
    *   An empty scenario.
    */
  def apply[F[_] : Monad](name: String): Scenario[F] = Scenario(name, idState)

  /**
    * Initialise a scenario by its name and start time.
    *
    * Using this as a constructor is preferred as it reduces the chance of misusing
    * `withStartingTime`.
    *
    * @param name
    *   The name to use for the scenario.
    * @param start
    *   The timestamp to use as the start time.
    * @return
    *   An empty scenario.
    */
  def apply[F[_] : Monad](name: String, start: Long): Scenario[F] =
    Scenario(name).withStartingTime(start)
}
