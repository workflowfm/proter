package com.workflowfm.proter

import java.util.UUID

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }

import org.scalamock.scalatest.MockFactory
import org.scalatest.{ Matchers, WordSpecLike }

import com.workflowfm.proter.events.PrintEventHandler
import com.workflowfm.proter.schedule.ProterScheduler

class CoordinatorTests extends WordSpecLike with Matchers with MockFactory with MockSimulations {
  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global //system.dispatchers.lookup("akka.my-dispatcher")

  "The Coordinator" must {

    "interact correctly with a simulation with no tasks" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: Simulation = mock[Simulation]

      sim.name _ expects () returning "sim" anyNumberOfTimes ()

      sim.run _ expects () onCall (_ => coordinator.simResponse(SimDone("sim", Success(Unit)))) once

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with just one task" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: Simulation = mockSingleTask("sim", coordinator, 1L, 2L, 3L)

      coordinator.addSimulation(1L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with just two tasks in sequence" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: Simulation = mockTwoTasks("sim", coordinator, 0L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with ten tasks in sequence" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: Simulation = mockRepeater("sim", coordinator, 0L, 2L, 10)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with two tasks in parallel, then one more" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim: Simulation = mockTwoPlusOneTasks("sim", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two interleaved single-task simulations" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 2L, 2L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two parallel single-task simulations, a short within a long one" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 10L, 10L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two 2plus1 simulations" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val sim1: Simulation = mockTwoPlusOneTasks("sim1", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)
      val sim2: Simulation = mockTwoPlusOneTasks("sim2", coordinator, 1L, 1L, 2L, 1L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with 100 2plus1 simulations" in {
      val coordinator = new Coordinator(new ProterScheduler())

//      val handler = new com.workflowfm.proter.events.PrintEventHandler
//      coordinator.subscribe(handler)
      for (i <- 1 to 100) {
        val start = i % 10
        val sim: Simulation = mockTwoPlusOneTasks(
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
        coordinator.addSimulation(start, sim)
      }

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with 100x10 task simulations" in {
      val coordinator = new Coordinator(new ProterScheduler())

//      val handler = new com.workflowfm.proter.events.PrintEventHandler
//      coordinator.subscribe(handler)
      for (i <- 1 to 100) {
        val start = i % 10
        val sim: Simulation = mockRepeater(
          "sim" + i + "(" + start + ")",
          coordinator,
          start,
          2L,
          10
        )
        coordinator.addSimulation(start, sim)
      }

      Await.result(coordinator.start(), 10.seconds)
    }

    "interact correctly with a simulation reacting to another" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val sim1: Simulation = mock[Simulation]
      sim1.name _ expects () returning "sim1" anyNumberOfTimes ()
      val sim2: Simulation = mock[Simulation]
      sim2.name _ expects () returning "sim2" anyNumberOfTimes ()

      val id1a = UUID.randomUUID()
      val tg1a = Task("T1a", 10) withID id1a
      val expected1a = tg1a.create("sim1", 0L)

      val id1b = UUID.randomUUID()
      val tg1b = Task("T1b", 3L) withID id1b
      val expected1b = tg1b.create("sim1", 2L)

      val id2 = UUID.randomUUID()
      val tg2 = Task("T2", 1) withID id2
      val expected2 = tg2.create("sim2", 1L)

      inSequence {
        sim1.run _ expects () onCall (_ => {
          coordinator.simResponse(SimReady("sim1", Seq(tg1a)))
        }) once ()

        sim2.run _ expects () onCall (_ => {
          coordinator.simResponse(SimReady("sim2", Seq(tg2)))
        }) once ()

        sim2.completed _ expects (
          where { (time, tasks) =>
            {
              tasks.size == 1 && containsTask(tasks, expected2) && time == 2L
            }
          }
        ) onCall { (_, _) =>
          {
            coordinator.waitFor("sim1")
            coordinator.simResponse(SimDone("sim2", Success(Unit)))
            Thread.sleep(500)
            coordinator.simResponse(SimReady("sim1", Seq(tg1b)))
          }
        } once ()

        sim1.completed _ expects (
          where { (time, tasks) =>
            {
              tasks.size == 1 && containsTask(tasks, expected1b) && time == 5L
            }
          }
        ) onCall { (_, _) =>
          {
            coordinator.simResponse(SimReady("sim1", Seq()))
          }
        } once ()

        sim1.completed _ expects (
          where { (time, tasks) =>
            tasks.size == 1 && containsTask(tasks, expected1a) && time == 10L
          }
        ) onCall (_ => coordinator.simResponse(SimDone("sim1", Success(Unit)))) once ()

      }

      coordinator.addSimulation(0, sim1)
      coordinator.addSimulation(1, sim2)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation aborting a task without resources" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val sim: Simulation = mockAbort("sim", coordinator, None)

      coordinator.addSimulation(0L, sim)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation aborting a task with resources" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val res = new TaskResource("R", 0)
      coordinator.addResource(res)

      val sim: Simulation = mockAbort("sim", coordinator, Some(res))

      coordinator.addSimulation(0L, sim)

      Await.result(coordinator.start(), 3.seconds)
    }

    "abort 2 simulations when the time limit is hit" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: Simulation = mockAborted("sim2", coordinator, 10L)
      val sim3: Simulation = mockAborted("sim3", coordinator, 3L)

      coordinator.addSimulations(
        Seq(
          (0L, sim1),
          (0L, sim2),
          (4L, sim3)
        )
      )

      coordinator.limit(5L)

      Await.result(coordinator.start(), 3.seconds)
    }

    "correctly work with a simulation starting at the time limit" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: Simulation = mockAborted("sim2", coordinator, 10L)

      coordinator.addSimulations(
        Seq(
          (0L, sim1),
          (4L, sim2)
        )
      )

      coordinator.limit(4L)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen: SimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 2L, 5)

      coordinator.addArrivalNow(5, Constant(3), gen)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(15L) // Last task finishes at 14L but the next arrival is still queued.
    }

    "interact correctly with two simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen1: SimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 2L, 5)
      val gen2: SimulationGenerator = mockSingleTaskGenerator("sim2", 1L, 3L, 2L, 4)

      coordinator.addArrivalNow(5, Constant(3), gen1)
      coordinator.addArrival(1, 4, Constant(3), gen2)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(15L)
    }

    "interact correctly with two simulation generators using addArrivalNext" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen1: SimulationGenerator = mockSingleTaskGenerator("sim1", 3L, 3L, 2L, 4)
      val gen2: SimulationGenerator = mockSingleTaskGenerator("sim2", 2L, 2L, 2L, 3)

      coordinator.addArrivalNext(4, Constant(3), gen1)
      coordinator.addArrivalNext(3, Constant(2), gen2)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(15L)
    }

    "interact correctly with an infinite simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen: SimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 1L, 5)

      coordinator.addInfiniteArrivalNow(Constant(3), gen)
      coordinator.limit(14L)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(14L)
    }

    "interact correctly with two infinite simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler())
      val gen1: SimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 1L, 5)
      val gen2: SimulationGenerator = mockSingleTaskGenerator("sim2", 3L, 4L, 1L, 3)

      coordinator.addInfiniteArrivalNow(Constant(3), gen1)
      coordinator.addInfiniteArrival(3L, Constant(4), gen2)
      coordinator.limit(14L)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(14L)
    }
  }

  "The single-threaded Coordinator" must {

    "interact correctly with a simulation with no tasks" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: Simulation = mock[Simulation]

      sim.name _ expects () returning "sim" anyNumberOfTimes ()

      sim.run _ expects () onCall (_ => coordinator.simResponse(SimDone("sim", Success(Unit)))) once

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with just one task" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: Simulation = mockSingleTask("sim", coordinator, 1L, 2L, 3L)

      coordinator.addSimulation(1L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with just two tasks in sequence" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: Simulation = mockTwoTasks("sim", coordinator, 0L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with ten tasks in sequence" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: Simulation = mockRepeater("sim", coordinator, 0L, 2L, 10)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with two tasks in parallel, then one more" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim: Simulation = mockTwoPlusOneTasks("sim", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two interleaved single-task simulations" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 2L, 2L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two parallel single-task simulations, a short within a long one" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 10L, 10L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two 2plus1 simulations" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

      val sim1: Simulation = mockTwoPlusOneTasks("sim1", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)
      val sim2: Simulation = mockTwoPlusOneTasks("sim2", coordinator, 1L, 1L, 2L, 1L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with 100 2plus1 simulations" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

//      val handler = new com.workflowfm.proter.events.PrintEventHandler
//      coordinator.subscribe(handler)
      for (i <- 1 to 100) {
        val start = i % 10
        val sim: Simulation = mockTwoPlusOneTasks(
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
        coordinator.addSimulation(start, sim)
      }

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with 100x10 task simulations" in {
      val coordinator = new Coordinator(new ProterScheduler())

//      val handler = new com.workflowfm.proter.events.PrintEventHandler
//      coordinator.subscribe(handler)
      for (i <- 1 to 100) {
        val start = i % 10
        val sim: Simulation = mockRepeater(
          "sim" + i + "(" + start + ")",
          coordinator,
          start,
          2L,
          10
        )
        coordinator.addSimulation(start, sim)
      }

      Await.result(coordinator.start(), 10.seconds)
    }

    "interact correctly with a simulation reacting to another" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val sim1: Simulation = mock[Simulation]
      sim1.name _ expects () returning "sim1" anyNumberOfTimes ()
      val sim2: Simulation = mock[Simulation]
      sim2.name _ expects () returning "sim2" anyNumberOfTimes ()

      val id1a = UUID.randomUUID()
      val tg1a = Task("T1a", 10) withID id1a
      val expected1a = tg1a.create("sim1", 0L)

      val id1b = UUID.randomUUID()
      val tg1b = Task("T1b", 3L) withID id1b
      val expected1b = tg1b.create("sim1", 2L)

      val id2 = UUID.randomUUID()
      val tg2 = Task("T2", 1) withID id2
      val expected2 = tg2.create("sim2", 1L)

      inSequence {
        sim1.run _ expects () onCall (_ => {
          coordinator.simResponse(SimReady("sim1", Seq(tg1a)))
        }) once ()

        sim2.run _ expects () onCall (_ => {
          coordinator.simResponse(SimReady("sim2", Seq(tg2)))
        }) once ()

        sim2.completed _ expects (
          where { (time, tasks) =>
            {
              tasks.size == 1 && containsTask(tasks, expected2) && time == 2L
            }
          }
        ) onCall { (_, _) =>
          {
            coordinator.waitFor("sim1")
            coordinator.simResponse(SimDone("sim2", Success(Unit)))
            Thread.sleep(500)
            coordinator.simResponse(SimReady("sim1", Seq(tg1b)))
          }
        } once ()

        sim1.completed _ expects (
          where { (time, tasks) =>
            {
              tasks.size == 1 && containsTask(tasks, expected1b) && time == 5L
            }
          }
        ) onCall { (_, _) =>
          {
            coordinator.simResponse(SimReady("sim1", Seq()))
          }
        } once ()

        sim1.completed _ expects (
          where { (time, tasks) =>
            tasks.size == 1 && containsTask(tasks, expected1a) && time == 10L
          }
        ) onCall (_ => coordinator.simResponse(SimDone("sim1", Success(Unit)))) once ()

      }

      coordinator.addSimulation(0, sim1)
      coordinator.addSimulation(1, sim2)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation aborting a task without resources" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

      val sim: Simulation = mockAbort("sim", coordinator, None)

      coordinator.addSimulation(0L, sim)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation aborting a task with resources" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

      val res = new TaskResource("R", 0)
      coordinator.addResource(res)

      val sim: Simulation = mockAbort("sim", coordinator, Some(res))

      coordinator.addSimulation(0L, sim)

      Await.result(coordinator.start(), 3.seconds)
    }

    "abort 2 simulations when the time limit is hit" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: Simulation = mockAborted("sim2", coordinator, 10L)
      val sim3: Simulation = mockAborted("sim3", coordinator, 3L)

      coordinator.addSimulations(
        Seq(
          (0L, sim1),
          (0L, sim2),
          (4L, sim3)
        )
      )

      coordinator.limit(5L)

      Await.result(coordinator.start(), 3.seconds)
    }

    "correctly work with a simulation starting at the time limit" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)

      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: Simulation = mockAborted("sim2", coordinator, 10L)

      coordinator.addSimulations(
        Seq(
          (0L, sim1),
          (4L, sim2)
        )
      )

      coordinator.limit(4L)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen: SimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 2L, 5)

      coordinator.addArrivalNow(5, Constant(3), gen)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(15L) // Last task finishes at 14L but the next arrival is still queued.
    }

    "interact correctly with two simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen1: SimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 2L, 5)
      val gen2: SimulationGenerator = mockSingleTaskGenerator("sim2", 1L, 3L, 2L, 4)

      coordinator.addArrivalNow(5, Constant(3), gen1)
      coordinator.addArrival(1, 4, Constant(3), gen2)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(15L)
    }

    "interact correctly with two simulation generators using addArrivalNext" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen1: SimulationGenerator = mockSingleTaskGenerator("sim1", 3L, 3L, 2L, 4)
      val gen2: SimulationGenerator = mockSingleTaskGenerator("sim2", 2L, 2L, 2L, 3)

      coordinator.addArrivalNext(4, Constant(3), gen1)
      coordinator.addArrivalNext(3, Constant(2), gen2)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(15L)
    }

    "interact correctly with an infinite simulation generator" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen: SimulationGenerator = mockSingleTaskGenerator("sim", 0L, 3L, 1L, 5)

      coordinator.addInfiniteArrivalNow(Constant(3), gen)
      coordinator.limit(14L)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(14L)
    }

    "interact correctly with two infinite simulation generators" in {
      val coordinator = new Coordinator(new ProterScheduler(), true)
      val gen1: SimulationGenerator = mockSingleTaskGenerator("sim1", 0L, 3L, 1L, 5)
      val gen2: SimulationGenerator = mockSingleTaskGenerator("sim2", 3L, 4L, 1L, 3)

      coordinator.addInfiniteArrivalNow(Constant(3), gen1)
      coordinator.addInfiniteArrival(3L, Constant(4), gen2)
      coordinator.limit(14L)

      Await.result(coordinator.start(), 3.seconds)
      coordinator.getTime() should be(14L)
    }
  }
}

trait MockSimulations { self: MockFactory =>

  def mockSingleTask(
      name: String,
      coordinator: Manager,
      expectedCreate: Long,
      duration: Long,
      expectedEnd: Long
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes ()

    val id = UUID.randomUUID()
    val tg = Task("T", duration) withID id
    val expected = tg.create(name, expectedCreate)

    inSequence {
      sim.run _ expects () onCall (_ => {
        coordinator.simResponse(SimReady(name, Seq(tg)))
      }) once ()
      sim.completed _ expects (
        where { (time, tasks) =>
          tasks.size == 1 && containsTask(tasks, expected) && time == expectedEnd
        }
      ) onCall (_ => coordinator.simResponse(SimDone(name, Success(Unit)))) once ()
    }

    sim
  }

  def mockTwoTasks(
      name: String,
      coordinator: Manager,
      expectedCreate1: Long,
      duration1: Long,
      expectedEnd1: Long,
      duration2: Long,
      expectedEnd2: Long
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes ()

    val id1 = UUID.randomUUID()
    val tg1 = Task("T1", duration1) withID id1
    val expected1 = tg1.create(name, expectedCreate1)

    val id2 = UUID.randomUUID()
    val tg2 = Task("T2", duration2) withID id2
    val expected2 = tg2.create(name, expectedEnd1)

    inSequence {
      sim.run _ expects () onCall (_ => {
        coordinator.simResponse(SimReady(name, Seq(tg1)))
      }) once ()

      sim.completed _ expects (
        where { (time, tasks) =>
          tasks.size == 1 && containsTask(tasks, expected1) && time == expectedEnd1
        }
      ) onCall (_ => {
        coordinator.simResponse(SimReady(name, Seq(tg2)))
      }) once ()

      sim.completed _ expects (
        where { (time, tasks) =>
          tasks.size == 1 && containsTask(tasks, expected2) && time == expectedEnd2
        }
      ) onCall (_ => coordinator.simResponse(SimDone(name, Success(Unit)))) once ()

    }

    sim
  }

  def mockRepeater(
      name: String,
      coordinator: Manager,
      expectedCreate: Long,
      duration: Long,
      repeat: Int
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes ()

    var i = 0
    var tg = Task("T" + i, duration) withID UUID.randomUUID
    var expected = tg.create(name, expectedCreate)
    i += 1

    inSequence {
      sim.run _ expects () onCall (_ => {
        coordinator.simResponse(SimReady(name, Seq(tg)))
      }) once ()

      sim.completed _ expects (
        where { (time, tasks) =>
          tasks.size == 1 && containsTask(
            tasks,
            expected
          ) && time == (expectedCreate + i * duration)
        }
      ) onCall (_ =>
        if (i < repeat) {
          tg = Task("T" + i, duration) withID UUID.randomUUID
          expected = tg.create(name, expectedCreate + i * duration)
          i += 1
          coordinator.simResponse(SimReady(name, Seq(tg)))
        } else {
          coordinator.simResponse(SimDone(name, Success(Unit)))
        }
      ) repeat (repeat)
    }

    sim
  }

  def mockTwoPlusOneTasks(
      name: String,
      coordinator: Manager,
      expectedCreate1: Long,
      duration1: Long,
      expectedEnd1: Long,
      duration2: Long,
      expectedEnd2: Long,
      duration3: Long,
      expectedEnd3: Long
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes ()

    val id1 = UUID.randomUUID()
    val tg1 = Task("T1", duration1) withID id1
    val expected1 = tg1.create(name, expectedCreate1)

    val id2 = UUID.randomUUID()
    val tg2 = Task("T2", duration2) withID id2
    val expected2 = tg2.create(name, expectedCreate1)

    val id3 = UUID.randomUUID()
    val tg3 = Task("T3", duration3) withID id3
    val expected3 = tg3.create(name, Math.max(expectedEnd1, expectedEnd2))

    inSequence {
      sim.run _ expects () onCall (_ => {
        coordinator.simResponse(SimReady(name, Seq(tg1, tg2)))
      }) once ()

      if (expectedEnd1 != expectedEnd2) {
        sim.completed _ expects (
          where { (time, tasks) =>
            {
              tasks.size == 1 &&
              ((containsTask(tasks, expected1) && time == expectedEnd1)) ||
              ((containsTask(tasks, expected2) && time == expectedEnd2))
            }
          }
        ) onCall { (_, _) =>
          {
            coordinator.simResponse(SimReady(name, Seq()))
          }
        } once ()

        sim.completed _ expects (
          where { (time, tasks) =>
            {
              tasks.size == 1 &&
              ((containsTask(tasks, expected1) && time == expectedEnd1)) ||
              ((containsTask(tasks, expected2) && time == expectedEnd2))
            }
          }
        ) onCall { (_, _) =>
          {
            coordinator.simResponse(SimReady(name, Seq(tg3)))
          }
        } once ()
      } else {
        sim.completed _ expects (
          where { (time, tasks) =>
            {
              (tasks.size == 2 && containsTask(tasks, expected1) && containsTask(
                tasks,
                expected2
              ) && time == expectedEnd2)
            }
          }
        ) onCall { (_, _) =>
          {
            coordinator.simResponse(SimReady(name, Seq(tg3)))
          }
        } once ()
      }

      sim.completed _ expects (
        where { (time, tasks) =>
          tasks.size == 1 && containsTask(tasks, expected3) && time == expectedEnd3
        }
      ) onCall (_ => coordinator.simResponse(SimDone(name, Success(Unit)))) once ()

    }

    sim
  }

  def mockAbort(
      name: String,
      coordinator: Manager,
      resource: Option[TaskResource]
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes ()

    // T1 0..3 - to be aborted at 2
    val id1 = UUID.randomUUID()
    val tg1 = Task("T1", 3) withID id1 withResources (resource map (_.name) toSeq)
    //val expected1 = tg1.create(name, 0)

    // T2 0..2
    val id2 = UUID.randomUUID()
    val tg2 = Task("T2", 2) withID id2
    val expected2 = tg2.create(name, 0)

    // T3 0..5
    val id3 = UUID.randomUUID()
    val tg3 =
      Task("T3", 5) withID id3 withResources (resource map (_.name) toSeq) withPriority (Task.Low)
    val expected3 = tg3.create(name, 0)

    val expectedTime = if (resource.isEmpty) 5L else 7L

    inSequence {
      sim.run _ expects () onCall (_ => {
        coordinator.simResponse(SimReady(name, Seq(tg1, tg2, tg3)))
      }) once ()

      sim.completed _ expects (
        where { (time, tasks) =>
          {
            tasks.size == 1 && containsTask(tasks, expected2) && time == 2
          }
        }
      ) onCall { (_, _) =>
        {
          coordinator.simResponse(SimReady(name, Seq(), Seq(id1)))
        }
      } once ()

      sim.completed _ expects (
        where { (time, tasks) =>
          tasks.size == 1 && containsTask(tasks, expected3) && time == expectedTime
        }
      ) onCall (_ => coordinator.simResponse(SimDone(name, Success(Unit)))) once ()

    }

    sim
  }

  def mockAborted(
      name: String,
      coordinator: Manager,
      duration: Long
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes ()
    sim.completed _ expects (*, *) never ()

    val id = UUID.randomUUID()
    val tg = Task("T", duration) withID id

    inSequence {
      sim.run _ expects () onCall (_ => {
        coordinator.simResponse(SimReady(name, Seq(tg)))
      }) once ()

      sim.stop _ expects () once ()
    }

    sim
  }

  def mockSingleTaskGenerator(
      name: String,
      start: Long,
      interval: Long,
      duration: Long,
      limit: Int
  ): SimulationGenerator = {
    val gen: SimulationGenerator = mock[SimulationGenerator]
    (gen.build _) expects (*, *) onCall ({ (manager: Manager, count: Int) =>
      mockSingleTask(
        name + ":" + count,
        manager,
        start + interval * count,
        duration,
        start + interval * count + duration
      )
    }) repeat (limit)
    gen
  }

  def containsTask(tasks: Seq[TaskInstance], t: TaskInstance): Boolean =
    tasks.exists(_.compare(t) == 0)
}
