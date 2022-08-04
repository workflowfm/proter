package com.workflowfm.proter
package state

import cases.{ Case, CaseRef }
import discrete.*
import events.*
import schedule.Scheduler

import cats.{ Monad, Applicative }
import cats.data.{ State, StateT }
import cats.effect.std.Random
import cats.implicits.*

import scala.annotation.tailrec
import scala.collection.immutable.{ HashSet, Set, Map, Queue }
import scala.util.{ Try, Success, Failure }
import java.util.UUID

/**
  * Includes simulation state updates required when setting up a simulation [[Scenario]].
  */
trait ScenarioState {

  /**
    * Adds a [[Resource]] to the simulation.
    *
    * Produces an [[events.EResourceAdd EResourceAdd]] event.
    *
    * @param r
    *   The [[Resource]] to add.
    * @return
    *   The state update.
    */
  def addResource[F[_]](r: Resource): State[Simulation[F], Seq[Event]] = State(sim =>
    (
      sim.copy(resources = sim.resources.addResource(r)),
      Seq(EResourceAdd(sim.id, sim.time, r))
    )
  )

  /**
    * Adds a collection of [[Resource]]s to the simulation.
    *
    * Produces an [[events.EResourceAdd EResourceAdd]] event for each resource.
    *
    * @param rs
    *   The [[Resource]]s to add.
    * @return
    *   The state update.
    */
  def addResources[F[_]](rs: Seq[Resource]): State[Simulation[F], Seq[Event]] = State(sim => {
    val events = rs.map { r => EResourceAdd(sim.id, sim.time, r) }
    (sim.copy(resources = sim.resources.addResources(rs)), events)
  })

  /**
    * Adds a [[cases.CaseRef CaseRef]] to the simulation.
    *
    * Produces an [[events.ECaseAdd ECaseAdd]] event.
    *
    * @param t
    *   The timestamp when the case needs to start. Must be greater or equal to the current
    *   simulation time.
    * @return
    *   The state update.
    */
  def addCaseRef[F[_]](t: Long, caseRef: CaseRef[F]): State[Simulation[F], Seq[Event]] =
    State(sim =>
      if t >= sim.time then
        (
          sim.copy(events = sim.events + StartingCase(t, caseRef)),
          Seq(ECaseAdd(sim.id, sim.time, caseRef.caseName, t))
        )
      else (sim, Seq(sim.error(s"Attempted to start case [${caseRef.caseName}] in the past: $t")))
    )

  /**
    * Adds a [[cases.Case Case]] to the simulation.
    *
    * Produces an [[events.ECaseAdd ECaseAdd]] event.
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param time
    *   The timestamp when the case needs to start. Must be greater or equal to the current
    *   simulation time.
    * @param name
    *   A unique name for the case.
    * @param t
    *   The object to use for the simulation case.
    * @return
    *   The state update.
    */
  def addCase[F[_] : Monad, T](time: Long, name: String, t: T)(
      using ct: Case[F, T]
  ): StateT[F, Simulation[F], Seq[Event]] = StateT(sim =>
    if time >= sim.time then {
      ct.init(name, 0, time, t).map { caseRef =>
        (
          sim.copy(events = sim.events + StartingCase(time, caseRef)),
          Seq(ECaseAdd(sim.id, sim.time, caseRef.caseName, time))
        )
      }
    } else Monad[F].pure((sim, Seq(sim.error(s"Attempted to start case [$name] in the past: $t"))))
  )

  /**
    * Adds a [[cases.Case Case]] to be run in the '''current''' simulation time.
    *
    * Produces an [[events.ECaseAdd ECaseAdd]] event.
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param name
    *   A unique name for the case.
    * @param t
    *   The object to use for the simulation case.
    * @return
    *   The state update.
    */
  def addCaseNow[F[_] : Monad, T](name: String, t: T)(
      using ct: Case[F, T]
  ): StateT[F, Simulation[F], Seq[Event]] =
    StateT.inspect[F, Simulation[F], Long](_.time).flatMap { time => addCase(time, name, t) }

  /**
    * Adds a collection of [[cases.Case Case]]s to the simulation.
    *
    * Produces an [[events.ECaseAdd ECaseAdd]] event for each case added.
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param cases
    *   The objects to use for each simulation case, each paired with its own unique name and
    *   starting timestamp.
    * @return
    *   The state update.
    */
  def addCases[F[_] : Monad, T](cases: Seq[(Long, String, T)])(
      using ct: Case[F, T]
  ): StateT[F, Simulation[F], Seq[Event]] =
    cases.map((t, n, c) => addCase(t, n, c)).sequence.map(_.flatten)

  /**
    * Adds a collection of [[cases.Case Case]]s to be run in the '''current''' simulation time.
    *
    * Produces an [[events.ECaseAdd ECaseAdd]] event for each case added.
    *
    * @tparam T
    *   The type of the object used for the simulation case.
    * @param cases
    *   The objects to use for each simulation case, each paired with its own unique name.
    * @return
    *   The state update.
    */
  def addCasesNow[F[_] : Monad, T](cases: Seq[(String, T)])(
      using ct: Case[F, T]
  ): StateT[F, Simulation[F], Seq[Event]] =
    cases.map((n, c) => addCaseNow(n, c)).sequence.map(_.flatten)

  /**
    * Adds a [[cases.Case Case]] with an arrival pattern to the simulation.
    *
    * Produces an [[events.EArrivalAdd EArrivalAdd]] event.
    *
    * @tparam T
    *   The type of the object used for the simulation cases.
    * @param t
    *   The timestamp when the first case should arrive.
    * @param name
    *   A unique name for the cases.
    * @param item
    *   The object to use for the simulation case.
    * @param rate
    *   The probability ditribution determining the duration until the next arrival.
    * @param limit
    *   An optional maximum number of cases to generate.
    * @return
    *   The state update.
    */
  def addArrival[F[_] : Monad : Random, T](
      t: Long,
      name: String,
      item: T,
      rate: LongDistribution,
      limit: Option[Int]
  )(using ct: Case[F, T]): StateT[F, Simulation[F], Seq[Event]] = StateT(sim =>
    if t >= sim.time then {
      val arrival = Arrival(t, name, item, rate, limit, 0)
      Monad[F].pure(
        (
          sim.copy(events = sim.events + arrival),
          Seq(EArrivalAdd(sim.id, sim.time, name, t, limit))
        )
      )
    } else
      Monad[F].pure(
        (sim, Seq(sim.error(s"Attempted to start arrivals of [$name] in the past: $t")))
      )
  )

  /**
    * Adds a [[cases.Case Case]] with an arrival pattern to start in the '''current''' simulation time.
    *
    * Produces an [[events.EArrivalAdd EArrivalAdd]] event.
    *
    * @tparam T
    *   The type of the object used for the simulation cases.
    * @param name
    *   A unique name for the cases.
    * @param item
    *   The object to use for the simulation case.
    * @param rate
    *   The probability ditribution determining the duration until the next arrival.
    * @param limit
    *   An optional maximum number of cases to generate.
    * @return
    *   The state update.
    */
  def addArrivalNow[F[_] : Monad : Random, T](
      name: String,
      item: T,
      rate: LongDistribution,
      limit: Option[Int]
  )(using ct: Case[F, T]): StateT[F, Simulation[F], Seq[Event]] =
    StateT.inspect[F, Simulation[F], Long](_.time).flatMap { time =>
      addArrival(time, name, item, rate, limit)
    }

  /**
    * Sets a time limit for the simulation.
    *
    * Produces an [[events.ETimeLimit ETimeLimit]] event.
    *
    * @note
    *   Once a time limit is placed it cannot be removed. Multiple time limits can be set so that
    *   the earliest one will be triggered.
    *
    * Setting the time limit after the start time will result in an error.
    *
    * @param t
    *   The timestamp to use as the time limit.
    * @return
    *   The state update.
    */
  def limit[F[_]](t: Long): State[Simulation[F], Seq[Event]] = State(sim =>
    if t >= sim.time then
      (sim.copy(events = sim.events + TimeLimit(t)), Seq(ETimeLimit(sim.id, sim.time, t)))
    else
      (
        sim,
        Seq(
          sim.error(
            s"Attempted to set time limit in the past. Limit: [$t] < Current time: [${sim.time}]."
          )
        )
      )
  )

}
