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

trait CaseState extends ScenarioState {

  /**
    * Adds new [[Task]](s) for a simulation.
    *
    *   - Uses [[Task.create]] to create a [[TaskInstance]], which will now have a fixed duration
    *     and cost.
    *   - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *   - If the task does not require any resources, it is started immediately using [[startTask]].
    *     Otherwise, we add it to the [[schedule.Scheduler Scheduler]].
    *
    * @group tasks
    * @param simulation
    *   The name of the [[Simulation]] that owns the task(s).
    * @param task
    *   The list of [[Task]]s to be added.
    */
  def addTask[F[_] : Random : Monad](
      caseName: String
  )(task: Task): StateT[F, Simulationx[F], Seq[Event]] = StateT(sim => {
    task.create(caseName, sim.time).map { inst =>
      {
        val event = ETaskAdd(sim.id, sim.time, inst)
        if task.resources.length > 0 then (sim.copy(tasks = sim.tasks + inst), Seq(event))
        else
          Simulationx.startTask(inst).run(sim).value match { // if the task does not require resources, start it now
            case (s, events) => (s, event +: events)
          }
      }
    }
  })

  def addTasks[F[_] : Random : Monad](
      caseName: String,
      tasks: Seq[Task]
  ): StateT[F, Simulationx[F], Seq[Event]] =
    tasks.traverse(addTask(caseName)).map(_.flatten)

  def abortTasks[F[_]](ids: Seq[UUID]): State[Simulationx[F], Seq[Event]] =
    Simulationx.abortTasks(ids)

  /**
    * Handles a [[SimDone]] response from a simulation, indicating that it has completed.
    *
    * Calls [[stopSimulation]] according to the reported success or failure result.
    *
    * @group simulations
    * @param simulation
    *   The name of the simulation
    * @param result
    *   The result of completion
    */
  def caseDone[F[_] : Monad](
      caseName: String,
      result: Try[Any]
  ): StateT[F, Simulationx[F], Seq[Event]] = {
    result match {
      case Success(res) => {
        Simulationx.stopCase(caseName, res.toString)
      }
      case Failure(ex) => {
        Simulationx.stopCase(caseName, ex.getLocalizedMessage)
      }
    }
  }

  def updateCase[F[_] : Monad](
      caseName: String,
      c: CaseRef[F]
  ): StateT[F, Simulationx[F], Seq[Event]] = StateT { sim =>
    Monad[F].pure(
      (
        sim.copy(cases = sim.cases + (caseName -> c)),
        Seq()
      )
    )
  }
}
