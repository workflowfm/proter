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

import cats.Monad
import cats.data.StateT
import cats.effect.IO
import cats.effect.implicits.*
import cats.effect.std.Random
import cats.effect.testing.scalatest.AsyncIOSpec
import cats.implicits.*

class SimulationTests extends SimulationTester {
  import MockCaseRef._

  "A full Simulation run" should {
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
        state = lift(addCaseRef(1L, ref))
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "correctly execute a case with 2 tasks in sequence" in {
      for {
        ref <- mockTwoTasks("test", 0L, 2L, 2L, 3L, 5L)
        state = lift(addCaseRef(0L, ref))
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "correctly execute a case with multiple tasks in sequence" in {
      for {
        ref <- mockRepeater("test", 0L, 2L, 10)
        state = lift(addCaseRef(0L, ref))
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "correctly execute a case with 2+1 tasks" in {
      for {
        ref <- mockTwoPlusOneTasks("test", 0L, 2L, 2L, 2L, 2L, 3L, 5L)
        state = lift(addCaseRef(0L, ref))
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "correctly execute 2 interleaving cases with one task each" in {
      for {
        ref1 <- mockSingleTask("test1", 0L, 2L, 2L)
        ref2 <- mockSingleTask("test2", 1L, 2L, 3L)
        state1 = lift(addCaseRef(0L, ref1))
        state2 = lift(addCaseRef(1L, ref2))
        _ <- sim("Test", compose2(state1, state2))
        _ = ref1 `should` comply
        _ = ref2 `should` comply
      } yield (())
    }

    "correctly execute a case while another is already running" in {
      for {
        ref1 <- mockSingleTask("test1", 0L, 10L, 10L)
        ref2 <- mockSingleTask("test2", 1L, 1L, 2L)
        state1 = lift(addCaseRef(0L, ref1))
        state2 = lift(addCaseRef(1L, ref2))
        _ <- sim("Test", compose2(state1, state2))
        _ = ref1 `should` comply
        _ = ref2 `should` comply
      } yield (())
    }

    "correctly execute 2x 2+1 cases" in {
      for {
        ref1 <- mockTwoPlusOneTasks("test1", 0L, 2L, 2L, 2L, 2L, 3L, 5L)
        ref2 <- mockTwoPlusOneTasks("test2", 1L, 1L, 2L, 1L, 2L, 3L, 5L)
        state1 = lift(addCaseRef(0L, ref1))
        state2 = lift(addCaseRef(1L, ref2))
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
            ).map(ref => (ref, lift(addCaseRef(start, ref))))
          )
          .toList
          .sequence

        _ <- sim("Test", compose(refs.map(_._2): _*))
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
            ).map(ref => (ref, lift(addCaseRef(start, ref))))
          )
          .toList
          .sequence

        _ <- sim("Test", compose(refs.map(_._2): _*))
        _ = refs.foreach(_._1 `should` comply)
      } yield (())
    }

    "correctly execute a case aborting a task without resources" in {
      for {
        ref <- mockAbort("test", None)
        state = lift(addCaseRef(0L, ref))
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "correctly execute a case aborting a task with resources" in {
      val res = Resource("R", 1, 0)
      for {
        ref <- mockAbort("test", Some(res))
        state = compose2(
          lift(addResource[IO](res)),
          lift(addCaseRef(0L, ref))
        )
        _ <- sim("Test", state)
        _ = ref `should` comply
      } yield (())
    }

    "abort 2 simulations when the time limit is hit" in {
      for {
        ref1 <- mockSingleTask("test1", 0L, 3L, 3L)
        ref2 <- mockAborted("test2", 10L)
        ref3 <- mockAborted("test3", 3L)
        state = compose(
          lift(addCaseRef(0L, ref1)),
          lift(addCaseRef(0L, ref2)),
          lift(addCaseRef(4L, ref3)),
          lift(limit(5L))
        )
        _ <- sim("Test", state)
        _ = ref1 `should` comply
        _ = ref2 `should` comply
        _ = ref3 `should` comply
      } yield (())
    }

    "correctly execute a case starting at the time limit" in {
      for {
        ref1 <- mockSingleTask("test1", 0L, 3L, 3L)
        ref2 <- mockAborted("test2", 10L)
        state = compose(
          lift(addCaseRef(0L, ref1)),
          lift(addCaseRef(4L, ref2)),
          lift(limit(4L))
        )
        _ <- sim("Test", state)
        _ = ref1 `should` comply
        _ = ref2 `should` comply
      } yield (())
    }

    "correctly execute an arrival" in {
      for {
        random <- Random.scalaUtilRandom[IO]
        arrival = MockSingleTaskArrival(0L, 3L, 2L)

        state = addArrivalNow("test", (), ConstantLong(arrival.interval), Some(5))(
          using Monad[IO],
          random,
          arrival
        )

        _ <- sim("Test", state)
        _ = arrival.cases.length `should` be(5)
        _ = arrival.cases.foreach(c => c `should` comply)
      } yield (())
    }

    "correctly execute 2 arrivals" in {
      for {
        random <- Random.scalaUtilRandom[IO]
        arrival1 = MockSingleTaskArrival(0L, 3L, 2L)
        arrival2 = MockSingleTaskArrival(1L, 3L, 2L)

        state = compose2(
          addArrivalNow("test1", (), ConstantLong(arrival1.interval), Some(5))(
            using Monad[IO],
            random,
            arrival1
          ),
          addArrival(1L, "test2", (), ConstantLong(arrival2.interval), Some(4))(
            using Monad[IO],
            random,
            arrival2
          )
        )
        _ <- sim("Test", state)
        _ = arrival1.cases.length `should` be(5)
        _ = arrival1.cases.foreach(c => c `should` comply)
        _ = arrival2.cases.length `should` be(4)
        _ = arrival2.cases.foreach(c => c `should` comply)
      } yield (())
    }

    /* "correctly execute 2 arrivals using `addArrivalNext`" in { for { random <-
     * Random.scalaUtilRandom[IO] arrival1 = MockSingleTaskArrival(3L, 3L, 2L) arrival2 =
     * MockSingleTaskArrival(2L, 3L, 2L)
     *
     * state = compose2( liftSingleStateT( addArrivalNext("test1", (),
     * ConstantLong(arrival1.interval), Some(4))(using Monad[IO], random, arrival1) ),
     * liftSingleStateT( addArrivalNext("test2", (), ConstantLong(arrival2.interval), Some(3))(using
     * Monad[IO], random, arrival2) ) ) _ <- sim("Test", state) _ = arrival1.cases.length `should`
     * be (5) _ = arrival1.cases.foreach(c => c `should` comply) _ = arrival2.cases.length `should`
     * be (4) _ = arrival2.cases.foreach(c => c `should` comply) } yield (()) } */

    "correctly execute an infinite arrival" in {
      for {
        random <- Random.scalaUtilRandom[IO]
        arrival = MockSingleTaskArrival(0L, 3L, 1L)

        state = compose2(
          addArrivalNow("test", (), ConstantLong(arrival.interval), Some(5))(
            using Monad[IO],
            random,
            arrival
          ),
          lift(limit(16L))
        )
        _ <- sim("Test", state)
        _ = arrival.cases.length `should` be(5)
        _ = arrival.cases.foreach(c => c `should` comply)
      } yield (())
    }

    "correctly execute 2 infinite arrivals" in {
      for {
        random <- Random.scalaUtilRandom[IO]
        arrival1 = MockSingleTaskArrival(0L, 3L, 1L)
        arrival2 = MockSingleTaskArrival(3L, 4L, 1L)

        state = compose(
          addArrivalNow("test1", (), ConstantLong(arrival1.interval), None)(
            using Monad[IO],
            random,
            arrival1
          ),
          addArrival(3L, "test2", (), ConstantLong(arrival2.interval), None)(
            using Monad[IO],
            random,
            arrival2
          ),
          lift(limit(14L))
        )
        _ <- sim("Test", state)
        _ = arrival1.cases.length `should` be(5)
        _ = arrival1.cases.foreach(c => c `should` comply)
        _ = arrival2.cases.length `should` be(3)
        _ = arrival2.cases.foreach(c => c `should` comply)
      } yield (())
    }

  }
}

trait SimulationTester
    extends AsyncWordSpec
    with AsyncIOSpec
    with Matchers
    with MockCaseCallMatcher
    with ScenarioState
    with StateOps {

  def sim(
      name: String,
      state: StateT[IO, Simulation[IO], Seq[Event]]
  ): IO[Unit] = {
    val sim = Simulation[IO](name, ProterScheduler)
    for {
      sResult <- sim.start(state)
      (updated, _) = sResult
      x <- updated.tailRecM(simRec)
    } yield (x)
  }

  protected val simRec: Simulation[IO] => IO[Either[Simulation[IO], Unit]] =
    sim =>
      for {
        tResult <- sim.tick
        (ticked, tEvents) = tResult
      } yield (ticked)
}
