package com.workflowfm.proter
package state

import cases.{ MockCaseCallMatcher, MockCaseRef } 
import events.Event
import schedule.ProterScheduler
import state.{ StateOps, ScenarioState }

import java.util.UUID

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.Success

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

import cats.data.StateT
import cats.effect.IO
import cats.effect.implicits.*
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits.*


class SimulationTests extends SimulationTester {
  import MockCaseRef._

  "A full Simulation run" must {
/*
    "interact correctly with a simulation with no tasks" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: MockSimulation = new MockSimulation("sim") {
        override def react(call: Call): Unit = call match {
          case Run => coordinator.simResponse(SimDone("sim", Success(())))
          case _ => ()
        }
        override val expectedCalls: Seq[Call] = Seq(Run)
      }

      coordinator.addSimulation(1L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }
 */
    "correctly execute a case with just one task" in {
      for {
        ref <- mockSingleTask("test", 1L, 2L, 3L)
        state = liftSingleState(addCaseRef(1L, ref))
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "correctly execute a case with 2 tasks in sequence" in {
      for {
        ref <- mockTwoTasks("test", 0L, 2L, 2L, 3L, 5L)
        state = liftSingleState(addCaseRef(0L, ref))
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "correctly execute a case with multiple tasks in sequence" in {
      for {
        ref <- mockRepeater("test", 0L, 2L, 10)
        state = liftSingleState(addCaseRef(0L, ref))
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "correctly execute a case with 2+1 tasks" in {
      for {
        ref <- mockTwoPlusOneTasks("test", 0L, 2L, 2L, 2L, 2L, 3L, 5L)
        state = liftSingleState(addCaseRef(0L, ref))
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "correctly execute 2 interleaving cases with one task each" in {
      for {
        ref1 <- mockSingleTask("test1", 0L, 2L, 2L)
        ref2 <- mockSingleTask("test2", 1L, 2L, 3L)
        state1 = liftSingleState(addCaseRef(0L, ref1))
        state2 = liftSingleState(addCaseRef(1L, ref2))
        _ <- sim("Test", compose2(state1, state2))
        _ = ref1 `should` comply
        _ = ref2 `should` comply
      } yield (())
    }

    "correctly execute a case while another is already running" in {
      for {
        ref1 <- mockSingleTask("test1", 0L, 10L, 10L)
        ref2 <- mockSingleTask("test2", 1L, 1L, 2L)
        state1 = liftSingleState(addCaseRef(0L, ref1))
        state2 = liftSingleState(addCaseRef(1L, ref2))
        _ <- sim("Test", compose2(state1, state2))
        _ = ref1 `should` comply
        _ = ref2 `should` comply
      } yield (())
    }

    "correctly execute 2x 2+1 cases" in {
      for {
        ref1 <- mockTwoPlusOneTasks("test1", 0L, 2L, 2L, 2L, 2L, 3L, 5L)
        ref2 <- mockTwoPlusOneTasks("test2", 1L, 1L, 2L, 1L, 2L, 3L, 5L)
        state1 = liftSingleState(addCaseRef(0L, ref1))
        state2 = liftSingleState(addCaseRef(1L, ref2))
        _ <- sim("Test", compose2(state1, state2))
        _ = ref1 `should` comply
        _ = ref2 `should` comply
      } yield (())
    }

    "correctly execute 100x 2+1 cases" in {
      for {
        refs <- (for i <- 1 to 100 yield (i, i % 10))
          .map((i, start) => 
            mockTwoPlusOneTasks(
              "test" + i + "(" + start + ")",
              start,
              2L,
              start + 2L,
              2L,
              start + 2L,
              3L,
              start + 5L
            ).map(ref => (ref, liftSingleState(addCaseRef(start, ref)))))
          .toList.sequence
        
        _ <- sim("Test", compose(refs.map(_._2) :_*))
        _ = refs.foreach(_._1 `should` comply)
      } yield (())
    }

    "correctly execute 100x 10-task cases" in {
      for {
        refs <- (for i <- 1 to 100 yield (i, i % 10))
          .map((i, start) => 
            mockRepeater(
              "test" + i + "(" + start + ")",
              start,
              2L,
              10
            ).map(ref => (ref, liftSingleState(addCaseRef(start, ref)))))
          .toList.sequence
        
        _ <- sim("Test", compose(refs.map(_._2) :_*))
        _ = refs.foreach(_._1 `should` comply)
      } yield (())
    }

  }
}


trait SimulationTester extends AsyncWordSpec with AsyncIOSpec with Matchers with MockCaseCallMatcher with ScenarioState with StateOps {

 def sim(
      name: String,
      state: StateT[IO, Simulationx[IO], Seq[Event]]
  ): IO[Unit] = {
    val sim = Simulationx[IO](name, ProterScheduler)
    for {
      sResult <- sim.start(state)
      (updated, events) = sResult
      x <- updated.tailRecM(simRec)
    } yield (x)
  }

  protected val simRec: Simulationx[IO] => IO[Either[Simulationx[IO], Unit]] =
    sim =>
      for {
        tResult <- sim.tick
        (ticked, tEvents) = tResult
      } yield (ticked)
}
