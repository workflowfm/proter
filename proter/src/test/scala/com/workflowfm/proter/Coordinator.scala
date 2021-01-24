package com.workflowfm.proter

import java.util.UUID

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }

import org.scalatest.{ Matchers, WordSpecLike }

import org.scalamock.scalatest.MockFactory

class CoordinatorTests
    extends WordSpecLike
    with Matchers
    with MockFactory
    with MockSimulations {
  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global //system.dispatchers.lookup("akka.my-dispatcher")
  
  "The Coordinator" must {

    "interact correctly with a simulation with no tasks" in {
      val coordinator = new Coordinator(new DefaultScheduler())
      val sim: Simulation = mock[Simulation]

      sim.name _ expects () returning "sim" anyNumberOfTimes()

      sim.run _ expects () onCall( _ => coordinator.simResponse(SimDone("sim", Success(Unit))) ) once

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with one task" in {
      val coordinator = new Coordinator(new DefaultScheduler())
      val sim: Simulation = mockSingleTask("sim", coordinator, 1L, 2L, 3L)

      coordinator.addSimulation(1L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with two tasks in sequence" in {
      val coordinator = new Coordinator(new DefaultScheduler())
      val sim: Simulation = mockTwoTasks("sim", coordinator, 0L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with two tasks in parallel, then one more" in {
      val coordinator = new Coordinator(new DefaultScheduler())
      val sim: Simulation = mockTwoPlusOneTasks("sim", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two interleaved single-task simulations" in {
      val coordinator = new Coordinator(new DefaultScheduler())
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 2L, 2L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two parallel single-task simulations, a short within a long one" in {
      val coordinator = new Coordinator(new DefaultScheduler())
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 10L, 10L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two 2plus1 simulations" in {
      val coordinator = new Coordinator(new DefaultScheduler())

      val sim1: Simulation = mockTwoPlusOneTasks("sim1", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)
      val sim2: Simulation = mockTwoPlusOneTasks("sim2", coordinator, 1L, 1L, 2L, 1L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with 100 2plus1 simulations" in {
      val coordinator = new Coordinator(new DefaultScheduler())

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

    "interact correctly with a simulation reacting to another" in {
      val coordinator = new Coordinator(new DefaultScheduler())

      val sim1: Simulation = mock[Simulation]
      sim1.name _ expects () returning "sim1" anyNumberOfTimes()
      val sim2: Simulation = mock[Simulation]
      sim2.name _ expects () returning "sim2" anyNumberOfTimes()

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
        sim1.run _ expects () onCall ( _ => {
          coordinator.simResponse(SimReady("sim1", Seq(tg1a)))
        }) once()

        sim2.run _ expects () onCall ( _ => {
          coordinator.simResponse(SimReady("sim2", Seq(tg2)))
        }) once()
        
        sim2.completed _ expects (
          where { (time, tasks) => {
            tasks.size == 1 && containsTask(tasks, expected2) && time == 2L
          }}
        ) onCall { (_, _) => {
          coordinator.waitFor("sim1")
          coordinator.simResponse(SimDone("sim2", Success(Unit)))
          Thread.sleep(500)
          coordinator.simResponse(SimReady("sim1", Seq(tg1b)))
        }} once()

       sim1.completed _ expects (
          where { (time, tasks) => {
            tasks.size == 1 && containsTask(tasks, expected1b) && time == 5L
          }}
        ) onCall { (_, _) => {
          coordinator.simResponse(SimReady("sim1", Seq()))
        }} once()

        sim1.completed _ expects (
          where { (time, tasks) => tasks.size == 1 && containsTask(tasks, expected1a) && time == 10L }
        ) onCall ( _ => coordinator.simResponse(SimDone("sim1", Success(Unit))) ) once()

      }

      coordinator.addSimulation(0, sim1)
      coordinator.addSimulation(1, sim2)

      Await.result(coordinator.start(), 3.seconds)
    }
    
    "interact correctly with a simulation aborting a task without resources" in {
      val coordinator = new Coordinator(new DefaultScheduler())

      val sim: Simulation = mockAbort("sim", coordinator, None)

      coordinator.addSimulation(0L, sim)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation aborting a task with resources" in {
      val coordinator = new Coordinator(new DefaultScheduler())

      val res = new TaskResource("R", 0)
      coordinator.addResource(res)

      val sim: Simulation = mockAbort("sim", coordinator, Some(res))

      coordinator.addSimulation(0L, sim)

      Await.result(coordinator.start(), 3.seconds)
    }

    "abort 2 simulations when the time limit is hit" in {
      val coordinator = new Coordinator(new DefaultScheduler())

      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: Simulation = mockAborted("sim2", coordinator, 10L)
      val sim3: Simulation = mockAborted("sim3", coordinator, 3L)

      coordinator.addSimulations(Seq(
        (0L, sim1),
        (0L, sim2),
        (4L, sim3)
      ))

      coordinator.limit(5L)

      Await.result(coordinator.start(), 3.seconds)
    }
  }

  "The single-threaded Coordinator" must {

    "interact correctly with a simulation with no tasks" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)
      val sim: Simulation = mock[Simulation]

      sim.name _ expects () returning "sim" anyNumberOfTimes()

      sim.run _ expects () onCall( _ => coordinator.simResponse(SimDone("sim", Success(Unit))) ) once

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with one task" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)
      val sim: Simulation = mockSingleTask("sim", coordinator, 1L, 2L, 3L)

      coordinator.addSimulation(1L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with two tasks in sequence" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)
      val sim: Simulation = mockTwoTasks("sim", coordinator, 0L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with two tasks in parallel, then one more" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)
      val sim: Simulation = mockTwoPlusOneTasks("sim", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two interleaved single-task simulations" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 2L, 2L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two parallel single-task simulations, a short within a long one" in {
       val coordinator = new Coordinator(new DefaultScheduler(), true)
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 10L, 10L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two 2plus1 simulations" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)

      val sim1: Simulation = mockTwoPlusOneTasks("sim1", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)
      val sim2: Simulation = mockTwoPlusOneTasks("sim2", coordinator, 1L, 1L, 2L, 1L, 2L, 3L, 5L)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with 100 2plus1 simulations" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)

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

    "interact correctly with a simulation reacting to another" in {
      val coordinator = new Coordinator(new DefaultScheduler())

      val sim1: Simulation = mock[Simulation]
      sim1.name _ expects () returning "sim1" anyNumberOfTimes()
      val sim2: Simulation = mock[Simulation]
      sim2.name _ expects () returning "sim2" anyNumberOfTimes()

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
        sim1.run _ expects () onCall ( _ => {
          println("HIHIHI")
          coordinator.simResponse(SimReady("sim1", Seq(tg1a)))
        }) once()

        sim2.run _ expects () onCall ( _ => {
          coordinator.simResponse(SimReady("sim2", Seq(tg2)))
        }) once()
        
        sim2.completed _ expects (
          where { (time, tasks) => {
            tasks.size == 1 && containsTask(tasks, expected2) && time == 2L
          }}
        ) onCall { (_, _) => {
          coordinator.waitFor("sim1")
          coordinator.simResponse(SimDone("sim2", Success(Unit)))
          Thread.sleep(500)
          coordinator.simResponse(SimReady("sim1", Seq(tg1b)))
        }} once()

       sim1.completed _ expects (
          where { (time, tasks) => {
            tasks.size == 1 && containsTask(tasks, expected1b) && time == 5L
          }}
        ) onCall { (_, _) => {
          coordinator.simResponse(SimReady("sim1", Seq()))
        }} once()

        sim1.completed _ expects (
          where { (time, tasks) => tasks.size == 1 && containsTask(tasks, expected1a) && time == 10L }
        ) onCall ( _ => coordinator.simResponse(SimDone("sim1", Success(Unit))) ) once()

      }

      coordinator.addSimulation(0, sim1)
      coordinator.addSimulation(1, sim2)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation aborting a task without resources" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)

      val sim: Simulation = mockAbort("sim", coordinator, None)

      coordinator.addSimulation(0L, sim)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation aborting a task with resources" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)

      val res = new TaskResource("R", 0)
      coordinator.addResource(res)

      val sim: Simulation = mockAbort("sim", coordinator, Some(res))

      coordinator.addSimulation(0L, sim)

      Await.result(coordinator.start(), 3.seconds)
    }

    "abort 2 simulations when the time limit is hit" in {
      val coordinator = new Coordinator(new DefaultScheduler(), true)

      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: Simulation = mockAborted("sim2", coordinator, 10L)
      val sim3: Simulation = mockAborted("sim3", coordinator, 3L)

      coordinator.addSimulations(Seq(
        (0L, sim1),
        (0L, sim2),
        (4L, sim3)
      ))

      coordinator.limit(5L)

      Await.result(coordinator.start(), 3.seconds)
    }
  }
}

trait MockSimulations { self: MockFactory =>

  def mockSingleTask(
    name: String, 
    coordinator: Coordinator, 
    expectedCreate: Long, 
    duration: Long, 
    expectedEnd: Long
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes()

    val id = UUID.randomUUID()
    val tg = Task("T", duration) withID id 
    val expected = tg.create(name, expectedCreate)

    inSequence {
      sim.run _ expects () onCall ( _ => {
        coordinator.simResponse(SimReady(name, Seq(tg)))
      }) once()
      sim.completed _ expects (
        where { (time, tasks) => tasks.size == 1 && containsTask(tasks, expected) && time == expectedEnd }
      ) onCall ( _ => coordinator.simResponse(SimDone(name, Success(Unit))) ) once()
    }

    sim
  }

  def mockTwoTasks(
    name: String, 
    coordinator: Coordinator, 
    expectedCreate1: Long,
    duration1: Long, 
    expectedEnd1: Long, 
    duration2: Long, 
    expectedEnd2: Long
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes()

    val id1 = UUID.randomUUID()
    val tg1 = Task("T1", duration1) withID id1
    val expected1 = tg1.create(name, expectedCreate1)

    val id2 = UUID.randomUUID()
    val tg2 = Task("T2", duration2) withID id2
    val expected2 = tg2.create(name, expectedEnd1)

    inSequence {
      sim.run _ expects () onCall ( _ => {
        coordinator.simResponse(SimReady(name, Seq(tg1)))
      }) once()

      sim.completed _ expects (
        where { (time, tasks) => tasks.size == 1 && containsTask(tasks, expected1) && time == expectedEnd1 }
      ) onCall ( _ => {
        coordinator.simResponse(SimReady(name, Seq(tg2)))
      }) once()

      sim.completed _ expects (
        where { (time, tasks) => tasks.size == 1 && containsTask(tasks, expected2) && time == expectedEnd2 }
      ) onCall ( _ => coordinator.simResponse(SimDone(name, Success(Unit))) ) once()

    }

    sim
  }

  def mockTwoPlusOneTasks(
    name: String, 
    coordinator: Coordinator, 
    expectedCreate1: Long,
    duration1: Long, 
    expectedEnd1: Long, 
    duration2: Long, 
    expectedEnd2: Long,
    duration3: Long,
    expectedEnd3: Long
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes()

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
      sim.run _ expects () onCall ( _ => {
        coordinator.simResponse(SimReady(name, Seq(tg1, tg2)))
      }) once()

      if (expectedEnd1 != expectedEnd2) {
        sim.completed _ expects (
          where { (time, tasks) => {
            tasks.size == 1 && 
              ((containsTask(tasks, expected1) && time == expectedEnd1)) ||
              ((containsTask(tasks, expected2) && time == expectedEnd2))
          }}
        ) onCall { (_, _) => {
          coordinator.simResponse(SimReady(name, Seq()))
        }} once()

        sim.completed _ expects (
          where { (time, tasks) => {
            tasks.size == 1 && 
              ((containsTask(tasks, expected1) && time == expectedEnd1)) ||
              ((containsTask(tasks, expected2) && time == expectedEnd2))
          }}
        ) onCall { (_, _) => {
          coordinator.simResponse(SimReady(name, Seq(tg3)))
        }} once()
      } else {
        sim.completed _ expects (
          where { (time, tasks) => {
            (tasks.size == 2 && containsTask(tasks, expected1) && containsTask(tasks, expected2) && time == expectedEnd2)
          }}
        ) onCall { (_, _) => {
          coordinator.simResponse(SimReady(name, Seq(tg3)))
        }} once()
      }

      sim.completed _ expects (
        where { (time, tasks) => tasks.size == 1 && containsTask(tasks, expected3) && time == expectedEnd3 }
      ) onCall ( _ => coordinator.simResponse(SimDone(name, Success(Unit))) ) once()

    }

    sim
  }

  def mockAbort(
    name: String,
    coordinator: Coordinator,
    resource: Option[TaskResource]
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes()

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
    val tg3 = Task("T3", 5) withID id3 withResources (resource map (_.name) toSeq) withPriority (Task.Low)
    val expected3 = tg3.create(name, 0) 

    val expectedTime = if (resource.isEmpty) 5L else 7L

    inSequence {
      sim.run _ expects () onCall ( _ => {
        coordinator.simResponse(SimReady(name, Seq(tg1, tg2, tg3)))
      }) once()

      sim.completed _ expects (
        where { (time, tasks) => {
          tasks.size == 1 && containsTask(tasks, expected2) && time == 2 
        }}
      ) onCall { (_, _) => {
        coordinator.simResponse(SimReady(name, Seq(), Seq(id1)))
      }} once()

      sim.completed _ expects (
        where { (time, tasks) => tasks.size == 1 && containsTask(tasks, expected3) && time == expectedTime }
      ) onCall ( _ => coordinator.simResponse(SimDone(name, Success(Unit))) ) once()

    }

    sim
  }

  def mockAborted(
    name: String,
    coordinator: Coordinator,
    duration: Long
  ): Simulation = {
    val sim: Simulation = mock[Simulation]
    sim.name _ expects () returning name anyNumberOfTimes()

    val id = UUID.randomUUID()
    val tg = Task("T", duration) withID id

    inSequence {
      sim.run _ expects () onCall ( _ => {
        coordinator.simResponse(SimReady(name, Seq(tg)))
      }) once()

      sim.stop _ expects () once()
    }

    sim
  }

  def containsTask(tasks: Seq[TaskInstance], t: TaskInstance): Boolean = tasks.exists(_.compare(t) == 0)
}

