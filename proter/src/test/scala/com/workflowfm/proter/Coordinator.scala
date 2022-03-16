package com.workflowfm.proter
/*
import java.util.UUID

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.Success

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import com.workflowfm.proter.schedule.ProterScheduler

class CoordinatorTests extends MockSimulationCallMatcher with AnyWordSpecLike with Matchers { // with MockSimulations {
  given ExecutionContextExecutor = ExecutionContext.global // system.dispatchers.lookup("akka.my-dispatcher")

  import MockSimulation._

  "The Coordinator" must {

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

    "interact correctly with a simulation with just one task" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: MockSimulation = mockSingleTask("sim", coordinator, 1L, 2L, 3L)

      coordinator.addSimulation(1L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with a simulation with just two tasks in sequence" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: MockSimulation = mockTwoTasks("sim", coordinator, 0L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with a simulation with ten tasks in sequence" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: MockSimulation = mockRepeater("sim", coordinator, 0L, 2L, 10)

      coordinator.addSimulation(0L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with a simulation with two tasks in parallel, then one more" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: MockSimulation = mockTwoPlusOneTasks("sim", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with two interleaved single-task simulations" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim1: MockSimulation = mockSingleTask("sim1", coordinator, 0L, 2L, 2L)
      val sim2: MockSimulation = mockSingleTask("sim2", coordinator, 1L, 2L, 3L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with two parallel single-task simulations, a short within a long one" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim1: MockSimulation = mockSingleTask("sim1", coordinator, 0L, 10L, 10L)
      val sim2: MockSimulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with two 2plus1 simulations" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim1: MockSimulation =
        mockTwoPlusOneTasks("sim1", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)
      val sim2: MockSimulation =
        mockTwoPlusOneTasks("sim2", coordinator, 1L, 1L, 2L, 1L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with 100 2plus1 simulations" in {
      val coordinator = new Coordinator(new ProterScheduler())

//      val handler = new com.workflowfm.proter.events.PrintEventHandler
//      coordinator.subscribe(handler)
      val sims = for i <- 1 to 100 yield {
        val start = i % 10
        val sim: MockSimulation = mockTwoPlusOneTasks(
          "sim" + i + "(" + start + ")",
          coordinator,
          start,
          2L,
          start + 2L,
          2L,
          start + 2L,
          3L,
          start + 5L
        )
        (start.toLong, sim)
      }
      coordinator.addSimulations(sims)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sims.foreach(_._2 should comply)
      }
    }

    "interact correctly with 100x10 task simulations" in {
      val coordinator = new Coordinator(new ProterScheduler())

//      val handler = new com.workflowfm.proter.events.PrintEventHandler
//      coordinator.subscribe(handler)
      val sims = for i <- 1 to 100 yield {
        val start = i % 10
        val sim: MockSimulation = mockRepeater(
          "sim" + i + "(" + start + ")",
          coordinator,
          start,
          2L,
          10
        )
        (start.toLong, sim)
      }
      coordinator.addSimulations(sims)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sims.foreach(_._2 should comply)
        ()
      }
    }

    "interact correctly with a simulation reacting to another" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val id1a = UUID.randomUUID()
      val tg1a = Task("T1a", 10).withID(id1a)
      val expected1a = tg1a.create("sim1", 0L)

      val id1b = UUID.randomUUID()
      val tg1b = Task("T1b", 3L).withID(id1b)
      val expected1b = tg1b.create("sim1", 2L)

      val id2 = UUID.randomUUID()
      val tg2 = Task("T2", 1).withID(id2)
      val expected2 = tg2.create("sim2", 1L)

      val sim1: MockSimulation = new MockSimulation("sim1") {
        override def react(call: Call): Unit = call match {
          case Run => coordinator.simResponse(SimReady("sim1", Seq(tg1a)))
          case Complete(time, Seq(task)) if time == 5L && task.compare(expected1b) == 0 =>
            coordinator.simResponse(SimReady("sim1", Seq()))
          case Complete(time, Seq(task)) if time == 10L && task.compare(expected1a) == 0 =>
            coordinator.simResponse(SimDone("sim1", Success(())))
          case _ => ()
        }

        override def expectedCalls: Seq[Call] =
          Seq(Run, Complete(5L, Seq(expected1b)), Complete(10L, Seq(expected1a)))
      }

      val sim2: MockSimulation = new MockSimulation("sim2") {
        override def react(call: Call): Unit = call match {
          case Run => coordinator.simResponse(SimReady("sim2", Seq(tg2)))
          case Complete(time, Seq(task)) if time == 2L && task.compare(expected2) == 0 => {
            coordinator.waitFor("sim1")
            coordinator.simResponse(SimDone("sim2", Success(())))
            Thread.sleep(500)
            coordinator.simResponse(SimReady("sim1", Seq(tg1b)))
          }
          case _ => ()
        }

        override def expectedCalls: Seq[Call] = Seq(Run, Complete(2L, Seq(expected2)))
      }

      coordinator.addSimulation(0, sim1)
      coordinator.addSimulation(1, sim2)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with a simulation aborting a task without resources" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: MockSimulation = mockAbort("sim", coordinator, None)

      coordinator.addSimulation(0L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with a simulation aborting a task with resources" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val res = new TaskResource("R", 0)
      coordinator.addResource(res)

      val sim: MockSimulation = mockAbort("sim", coordinator, Some(res))

      coordinator.addSimulation(0L, sim)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "abort 2 simulations when the time limit is hit" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val sim1: MockSimulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: MockSimulation = mockAborted("sim2", coordinator, 10L)
      val sim3: MockSimulation = mockAborted("sim3", coordinator, 3L)

      coordinator.addSimulations(
        Seq(
          (0L, sim1),
          (0L, sim2),
          (4L, sim3)
        )
      )

      coordinator.limit(5L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        sim3 should comply
        ()
      }
    }

    "correctly work with a simulation starting at the time limit" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val sim1: MockSimulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: MockSimulation = mockAborted("sim2", coordinator, 10L)

      coordinator.addSimulations(
        Seq(
          (0L, sim1),
          (4L, sim2)
        )
      )

      coordinator.limit(4L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with a simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen: MockSimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 2L, 5)

      coordinator.addArrivalNow(5, Constant(3), gen)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen.sims.foreach(_ should comply)
        coordinator.getTime() should be(15L) // Last task finishes at 14L but the next arrival is still queued.
        ()
      }

    }

    "interact correctly with two simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen1: MockSimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 2L, 5)
      val gen2: MockSimulationGenerator = mockSingleTaskGenerator("sim2", 1L, 3L, 2L, 4)

      coordinator.addArrivalNow(5, Constant(3), gen1)
      coordinator.addArrival(1, 4, Constant(3), gen2)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen1.sims.foreach(_ should comply)
        gen2.sims.foreach(_ should comply)
        coordinator.getTime() should be(15L)
        ()
      }
    }

    "interact correctly with two simulation generators using addArrivalNext" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen1: MockSimulationGenerator = mockSingleTaskGenerator("sim1", 3L, 3L, 2L, 4)
      val gen2: MockSimulationGenerator = mockSingleTaskGenerator("sim2", 2L, 2L, 2L, 3)

      coordinator.addArrivalNext(4, Constant(3), gen1)
      coordinator.addArrivalNext(3, Constant(2), gen2)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen1.sims.foreach(_ should comply)
        gen2.sims.foreach(_ should comply)
        coordinator.getTime() should be(15L)
        ()
      }
    }

    "interact correctly with an infinite simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen: MockSimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 1L, 5)

      coordinator.addInfiniteArrivalNow(Constant(3), gen)
      coordinator.limit(16L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen.sims.dropRight(1).foreach(_ should comply)
        coordinator.getTime() should be(16L)
        ()
      }
    }

    "interact correctly with two infinite simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen1: MockSimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 1L, 5)
      val gen2: MockSimulationGenerator = mockSingleTaskGenerator("sim2", 3L, 4L, 1L, 3)

      coordinator.addInfiniteArrivalNow(Constant(3), gen1)
      coordinator.addInfiniteArrival(3L, Constant(4), gen2)
      coordinator.limit(14L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen1.sims.dropRight(1).foreach(_ should comply)
        gen2.sims.dropRight(1).foreach(_ should comply)
        coordinator.getTime() should be(14L)
        ()
      }
    }
  }

  "The single-threaded Coordinator" must {

    "interact correctly with a simulation with no tasks" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
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

    "interact correctly with a simulation with just one task" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: MockSimulation = mockSingleTask("sim", coordinator, 1L, 2L, 3L)

      coordinator.addSimulation(1L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with a simulation with just two tasks in sequence" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: MockSimulation = mockTwoTasks("sim", coordinator, 0L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with a simulation with ten tasks in sequence" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: MockSimulation = mockRepeater("sim", coordinator, 0L, 2L, 10)

      coordinator.addSimulation(0L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with a simulation with two tasks in parallel, then one more" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: MockSimulation = mockTwoPlusOneTasks("sim", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with two interleaved single-task simulations" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim1: MockSimulation = mockSingleTask("sim1", coordinator, 0L, 2L, 2L)
      val sim2: MockSimulation = mockSingleTask("sim2", coordinator, 1L, 2L, 3L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with two parallel single-task simulations, a short within a long one" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim1: MockSimulation = mockSingleTask("sim1", coordinator, 0L, 10L, 10L)
      val sim2: MockSimulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with two 2plus1 simulations" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim1: MockSimulation =
        mockTwoPlusOneTasks("sim1", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)
      val sim2: MockSimulation =
        mockTwoPlusOneTasks("sim2", coordinator, 1L, 1L, 2L, 1L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with 100 2plus1 simulations" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

//      val handler = new com.workflowfm.proter.events.PrintEventHandler
//      coordinator.subscribe(handler)
      val sims = for i <- 1 to 100 yield {
        val start = i % 10
        val sim: MockSimulation = mockTwoPlusOneTasks(
          "sim" + i + "(" + start + ")",
          coordinator,
          start,
          2L,
          start + 2L,
          2L,
          start + 2L,
          3L,
          start + 5L
        )
        (start.toLong, sim)
      }
      coordinator.addSimulations(sims)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sims.foreach(_._2 should comply)
      }
    }

    "interact correctly with 100x10 task simulations" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

//      val handler = new com.workflowfm.proter.events.PrintEventHandler
//      coordinator.subscribe(handler)
      val sims = for i <- 1 to 100 yield {
        val start = i % 10
        val sim: MockSimulation = mockRepeater(
          "sim" + i + "(" + start + ")",
          coordinator,
          start,
          2L,
          10
        )
        (start.toLong, sim)
      }
      coordinator.addSimulations(sims)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sims.foreach(_._2 should comply)
        ()
      }
    }

    "interact correctly with a simulation reacting to another" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

      val id1a = UUID.randomUUID()
      val tg1a = Task("T1a", 10).withID(id1a)
      val expected1a = tg1a.create("sim1", 0L)

      val id1b = UUID.randomUUID()
      val tg1b = Task("T1b", 3L).withID(id1b)
      val expected1b = tg1b.create("sim1", 2L)

      val id2 = UUID.randomUUID()
      val tg2 = Task("T2", 1).withID(id2)
      val expected2 = tg2.create("sim2", 1L)

      val sim1: MockSimulation = new MockSimulation("sim1") {
        override def react(call: Call): Unit = call match {
          case Run => coordinator.simResponse(SimReady("sim1", Seq(tg1a)))
          case Complete(time, Seq(task)) if time == 5L && task.compare(expected1b) == 0 =>
            coordinator.simResponse(SimReady("sim1", Seq()))
          case Complete(time, Seq(task)) if time == 10L && task.compare(expected1a) == 0 =>
            coordinator.simResponse(SimDone("sim1", Success(())))
          case _ => ()
        }

        override def expectedCalls: Seq[Call] =
          Seq(Run, Complete(5L, Seq(expected1b)), Complete(10L, Seq(expected1a)))
      }

      val sim2: MockSimulation = new MockSimulation("sim2") {
        override def react(call: Call): Unit = call match {
          case Run => coordinator.simResponse(SimReady("sim2", Seq(tg2)))
          case Complete(time, Seq(task)) if time == 2L && task.compare(expected2) == 0 => {
            coordinator.waitFor("sim1")
            coordinator.simResponse(SimDone("sim2", Success(())))
            Thread.sleep(500)
            coordinator.simResponse(SimReady("sim1", Seq(tg1b)))
          }
          case _ => ()
        }

        override def expectedCalls: Seq[Call] = Seq(Run, Complete(2L, Seq(expected2)))
      }

      coordinator.addSimulation(0, sim1)
      coordinator.addSimulation(1, sim2)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with a simulation aborting a task without resources" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: MockSimulation = mockAbort("sim", coordinator, None)

      coordinator.addSimulation(0L, sim)
      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "interact correctly with a simulation aborting a task with resources" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

      val res = new TaskResource("R", 0)
      coordinator.addResource(res)

      val sim: MockSimulation = mockAbort("sim", coordinator, Some(res))

      coordinator.addSimulation(0L, sim)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim should comply
        ()
      }
    }

    "abort 2 simulations when the time limit is hit" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

      val sim1: MockSimulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: MockSimulation = mockAborted("sim2", coordinator, 10L)
      val sim3: MockSimulation = mockAborted("sim3", coordinator, 3L)

      coordinator.addSimulations(
        Seq(
          (0L, sim1),
          (0L, sim2),
          (4L, sim3)
        )
      )

      coordinator.limit(5L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        sim3 should comply
        ()
      }
    }

    "correctly work with a simulation starting at the time limit" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

      val sim1: MockSimulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: MockSimulation = mockAborted("sim2", coordinator, 10L)

      coordinator.addSimulations(
        Seq(
          (0L, sim1),
          (4L, sim2)
        )
      )

      coordinator.limit(4L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        sim1 should comply
        sim2 should comply
        ()
      }
    }

    "interact correctly with a simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen: MockSimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 2L, 5)

      coordinator.addArrivalNow(5, Constant(3), gen)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen.sims.foreach(_ should comply)
        coordinator.getTime() should be(15L) // Last task finishes at 14L but the next arrival is still queued.
        ()
      }

    }

    "interact correctly with two simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen1: MockSimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 2L, 5)
      val gen2: MockSimulationGenerator = mockSingleTaskGenerator("sim2", 1L, 3L, 2L, 4)

      coordinator.addArrivalNow(5, Constant(3), gen1)
      coordinator.addArrival(1, 4, Constant(3), gen2)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen1.sims.foreach(_ should comply)
        gen2.sims.foreach(_ should comply)
        coordinator.getTime() should be(15L)
        ()
      }
    }

    "interact correctly with two simulation generators using addArrivalNext" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen1: MockSimulationGenerator = mockSingleTaskGenerator("sim1", 3L, 3L, 2L, 4)
      val gen2: MockSimulationGenerator = mockSingleTaskGenerator("sim2", 2L, 2L, 2L, 3)

      coordinator.addArrivalNext(4, Constant(3), gen1)
      coordinator.addArrivalNext(3, Constant(2), gen2)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen1.sims.foreach(_ should comply)
        gen2.sims.foreach(_ should comply)
        coordinator.getTime() should be(15L)
        ()
      }
    }

    "interact correctly with an infinite simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen: MockSimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 1L, 5)

      coordinator.addInfiniteArrivalNow(Constant(3), gen)
      coordinator.limit(16L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen.sims.dropRight(1).foreach(_ should comply)
        coordinator.getTime() should be(16L)
        ()
      }
    }

    "interact correctly with two infinite simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen1: MockSimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 1L, 5)
      val gen2: MockSimulationGenerator = mockSingleTaskGenerator("sim2", 3L, 4L, 1L, 3)

      coordinator.addInfiniteArrivalNow(Constant(3), gen1)
      coordinator.addInfiniteArrival(3L, Constant(4), gen2)
      coordinator.limit(14L)

      try {
        Await.result(coordinator.start(), 3.seconds)
      } finally {
        gen1.sims.dropRight(1).foreach(_ should comply)
        gen2.sims.dropRight(1).foreach(_ should comply)
        coordinator.getTime() should be(14L)
        ()
      }
    }
  }
}
 */
