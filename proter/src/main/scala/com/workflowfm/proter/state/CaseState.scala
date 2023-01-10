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

/**
  * Includes simulation state updates required when executing a [[cases.CaseRef CaseRef]].
  */
trait CaseState extends ScenarioState {

  /**
    * Adds a new [[Task]].
    *
    *   - Uses [[Task.create]] to create a [[TaskInstance]], which will now have a fixed duration
    *     and cost.
    *   - Publishes a [[com.workflowfm.proter.events.ETaskAdd ETaskAdd]].
    *   - If the task does not require any resources, it is started immediately using
    *     [[Simulation.startTask]].
    *
    * @group tasks
    * @param caseName
    *   The name of the [[cases.CaseRef CaseRef]] that owns the task.
    * @param task
    *   The [[Task]] to be added.
    * @return
    *   The state update.
    */
  def addTask[F[_] : Random : Monad](
      caseName: String
  )(task: Task): StateT[F, Simulation[F], Seq[Event]] = StateT(sim => {
    task.create(caseName, sim.time).map { inst =>
      {
        val event = ETaskAdd(sim.id, sim.time, inst)
        if task.resources.size > 0 then (sim.copy(tasks = sim.tasks + inst), Seq(event))
        else
          Simulation.startTask(inst).run(sim).value match { /* if the task does not require
             * resources, start it now */
            case (s, events) => (s, event +: events)
          }
      }
    }
  })

  /**
    * Adds a collection of new [[Task]]s.
    *
    * @see
    *   [[addTask]]
    *
    * @group tasks
    * @param caseName
    *   The name of the [[cases.CaseRef CaseRef]] that owns the task.
    * @param tasks
    *   The [[Task]]s to be added.
    * @return
    *   The state update.
    */
  def addTasks[F[_] : Random : Monad](
      caseName: String,
      tasks: Seq[Task]
  ): StateT[F, Simulation[F], Seq[Event]] =
    tasks.traverse(addTask(caseName)).map(_.flatten)

  /**
    * Aborts a list of [[TaskInstance]]s by their IDs.
    *
    * @see
    *   [[Simulation.abortTasks]]
    *
    * @group tasks
    * @param ids
    *   The `UUID`s of the [[TaskInstance]]s to abort.
    * @return
    *   The state update.
    */
  def abortTasks[F[_]](ids: Seq[UUID]): State[Simulation[F], Seq[Event]] =
    Simulation.abortTasks(ids)

  /**
    * Updates the simulation with a completed [[cases.CaseRef CaseRef]].
    *
    * @see
    *   [[Simulation.stopCase]]
    *
    * @group cases
    * @param caseName
    *   The name of the [[cases.CaseRef CaseRef]].
    * @param result
    *   The result produced by the completed [[cases.CaseRef CaseRef]].
    * @return
    *   The state update.
    */
  def caseDone[F[_] : Monad](
      caseName: String,
      result: Try[Any]
  ): StateT[F, Simulation[F], Seq[Event]] = {
    result match {
      case Success(res) => {
        Simulation.stopCase(caseName, res.toString)
      }
      case Failure(ex) => {
        Simulation.stopCase(caseName, ex.getLocalizedMessage)
      }
    }
  }

  /**
    * Updates a [[cases.CaseRef CaseRef]].
    *
    * @group cases
    * @param caseName
    *   The name of the [[cases.CaseRef CaseRef]].
    * @param c
    *   The new [[cases.CaseRef CaseRef]] reflecting the new state of the case.
    * @return
    *   The state update.
    */
  def updateCase[F[_] : Monad](
      caseName: String,
      c: CaseRef[F]
  ): StateT[F, Simulation[F], Seq[Event]] = StateT { sim =>
    Monad[F].pure(
      (
        sim.copy(cases = sim.cases + (caseName -> c)),
        Seq()
      )
    )
  }
}
