package com.workflowfm.proter
package state

import cases.{ Case, CaseRef }
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

trait ScenarioState {

  def addResource[F[_]](r: Resource): State[Simulation[F], Seq[Event]] = State(sim =>
    (
      sim.copy(resources = sim.resources.addResource(r)),
      Seq(EResourceAdd(sim.id, sim.time, r))
    )
  )

  def addResources[F[_]](rs: Seq[Resource]): State[Simulation[F], Seq[Event]] = State(sim => {
    val events = rs.map { r => EResourceAdd(sim.id, sim.time, r) }
    (sim.copy(resources = sim.resources.addResources(rs)), events)
  })

  /**
    * Add a new simulation to be run.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimAdd ESimAdd]].
    *
    * @group simulations
    * @param t
    *   The timestamp when the simulation needs to start. Must be greater or equal to the current
    *   time.
    * @param simulation
    *   The [[Simulation]] to run.
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
    * Add a new simulation to be run in the current virtual time.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimAdd ESimAdd]] for each simulation.
    *
    * @group simulations
    * @param simulation
    *   The [[Simulation]] to run.
    */
  def addCaseNow[F[_] : Monad, T](name: String, t: T)(
      using ct: Case[F, T]
  ): StateT[F, Simulation[F], Seq[Event]] =
    StateT.inspect[F, Simulation[F], Long](_.time).flatMap { time => addCase(time, name, t) }

  /**
    * Adds multiple simulations at the same time.
    *
    * This is equivalent to mapping [[addSimulation]] over the given sequence, but more efficient.
    *
    * @group simulations
    * @param sims
    *   A sequence of pairs, each consisting of a starting timestamp and a [[Simulation]].
    *   Timestamps must be greater or equal to the current time.
    */
  def addCases[F[_] : Monad, T](cases: Seq[(Long, String, T)])(
      using ct: Case[F, T]
  ): StateT[F, Simulation[F], Seq[Event]] =
    cases.map((t, n, c) => addCase(t, n, c)).sequence.map(_.flatten)

  /**
    * Add multiple simulations to be run in the current virtual time.
    *
    * @group simulations
    * @param sims
    *   A sequence of [[Simulation]]s.
    */
  def addCasesNow[F[_] : Monad, T](cases: Seq[(String, T)])(
      using ct: Case[F, T]
  ): StateT[F, Simulation[F], Seq[Event]] =
    cases.map((n, c) => addCaseNow(n, c)).sequence.map(_.flatten)

  /**
    * Adds a new arrival process to the coordinator.
    *
    * @param t
    *   The virtual timestamp when the arrival process will begin.
    * @param name
    *   The name to use
    * @param limit
    *   The number of simulation instances to spawn.
    * @param rate
    *   The arrival rate of the simulation instances.
    * @param simulationGenerator
    *   The generator used to create new instances.
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
          Seq(EArrivalAdd(sim.id, sim.time, name, t, rate, limit))
        )
      )
    } else
      Monad[F].pure(
        (sim, Seq(sim.error(s"Attempted to start arrivals of [$name] in the past: $t")))
      )
  )

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
    * Sets a time limit in virtual time for the simulation to end.
    *
    * @note
    *   Once a time limit is placed it cannot be removed. Multiple time limits can be set so that
    *   the earliest one will be triggered.
    *
    * @group toplevel
    * @param t
    *   The virtual timestamp to end the simulation.
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
