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

    /*
    "interact correctly with a simulation reacting to another" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      // probe represents a 2nd simulation starting at 1
      val probe = TestProbe()

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.AddSim(1L, probe.ref)
      coordinator ! Coordinator.Start

      // Test1 starts
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test1", self)

      // T1a 0..10
      val id1a = UUID.randomUUID()
      val tg1a = TaskGenerator("T1a", id1a, "Test1", ConstantGenerator(10L), ConstantGenerator(5L))
      val expected1a =
        new Task(id1a, "T1a", "Test1", self, 0L, 0L, Seq(), 10L, 10L, 5L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg1a)))
      coordinator ! Coordinator.SimReady(None)

      // Test2 starts
      probe.expectMsg(Simulation.Start)
      probe.reply(Coordinator.SimStarted("Test2", self))

      // T2a 1..2
      val id2a = UUID.randomUUID()
      val tg2a = TaskGenerator("T2a", id2a, "Test2", ConstantGenerator(1L), ConstantGenerator(5L))
      val expected2a =
        new Task(id2a, "T2a", "Test2", self, 1L, 0L, Seq(), 1L, 1L, 5L, -1, Task.Medium)
      probe.send(coordinator, Coordinator.AddTasks(Seq((tg2a))))
      probe.send(coordinator, Coordinator.SimReady(None))

      // T2a completes
      val Simulation.TaskCompleted(task2a, time2a) =
        probe.expectMsgType[Simulation.TaskCompleted]
      time2a should be(2L)
      task2a.compare(expected2a) should be(0)

      // Test1 requests wait
      coordinator ! Coordinator.WaitFor(self)
      expectMsg(Simulation.AckWait)

      // Test2 completes
      probe.send(coordinator, Coordinator.SimDone("Test2", Success(Unit)))

      // Coordinator must wait
      Thread.sleep(500)
      coordinator ! Coordinator.Ping
      expectMsgType[Coordinator.Time].time should be(2L)

      // T1b 2..3
      val id1b = UUID.randomUUID()
      val tg1b = TaskGenerator("T1b", id1b, "Test1", ConstantGenerator(1L), ConstantGenerator(5L))
      val expected1b =
        new Task(id1b, "T1b", "Test1", self, 2L, 0L, Seq(), 1L, 1L, 5L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg1b)))
      coordinator ! Coordinator.SimReady(None)

      // T1b completes
      val Simulation.TaskCompleted(task1b, time1b) =
        expectMsgType[Simulation.TaskCompleted]
      time1b should be(3L)
      task1b.compare(expected1b) should be(0)
      coordinator ! Coordinator.AckTasks(Seq(id1b))

      // T1a completes
      val Simulation.TaskCompleted(task1a, time1a) =
        expectMsgType[Simulation.TaskCompleted]
      time1a should be(10L)
      task1a.compare(expected1a) should be(0)

      coordinator ! Coordinator.SimDone("Test1", Success(Unit))
    }

    "interact correctly with a simulation aborting a task without resources" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test", self)

      // T1 0..3 - to be aborted at 2
      val id1 = UUID.randomUUID()
      val tg1 = TaskGenerator("T1", id1, "Test", ConstantGenerator(3L), ConstantGenerator(5L))
      val expected1 = new Task(id1, "T1", "Test", self, 0L, 0L, Seq(), 3L, 3L, 5L, -1, Task.Medium)

      // T2 0..2
      val id2 = UUID.randomUUID()
      val tg2 = TaskGenerator("T2", id2, "Test", ConstantGenerator(2L), ConstantGenerator(6L))
      val expected2 = new Task(id2, "T2", "Test", self, 0L, 0L, Seq(), 2L, 2L, 6L, -1, Task.Medium)

      // T3 0..5
      val id3 = UUID.randomUUID()
      val tg3 = TaskGenerator("T3", id3, "Test", ConstantGenerator(5L), ConstantGenerator(6L))
      val expected3 = new Task(id3, "T3", "Test", self, 0L, 0L, Seq(), 5L, 5L, 6L, -1, Task.Medium)

      // Add all
      coordinator ! Coordinator.AddTasks(Seq(tg1, tg2, tg3))
      coordinator ! Coordinator.SimReady()

      // T2 completes
      expectMsgType[Simulation.TaskCompleted]

      // Abort T1
      coordinator ! Coordinator.AbortTasks(Seq(id1))
      coordinator ! Coordinator.SimReady()

      // T3 completes
      val Simulation.TaskCompleted(task3, time3) = expectMsgType[Simulation.TaskCompleted]
      task3.compare(expected3) should be(0)
      time3 should be(5L)

      coordinator ! Coordinator.SimDone("Test", Success(Unit))
    }

    "interact correctly with a simulation aborting a task with resources" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test", self)

      val res = new TaskResource("R", 0)
      coordinator ! Coordinator.AddResource(res)

      // T1 0..3 - to be aborted at 2
      val id1 = UUID.randomUUID()
      val tg1 = TaskGenerator(
          "T1",
          id1,
          "Test",
          ConstantGenerator(3L),
          ConstantGenerator(5L)
        ) withResources (Seq(res.name))
      val expected1 =
        new Task(id1, "T1", "Test", self, 0L, 0L, Seq(res.name), 3L, 3L, 5L, -1, Task.Medium)

      // T2 0..2
      val id2 = UUID.randomUUID()
      val tg2 = TaskGenerator("T2", id2, "Test", ConstantGenerator(2L), ConstantGenerator(6L))
      val expected2 = new Task(id2, "T2", "Test", self, 0L, 0L, Seq(), 2L, 2L, 6L, -1, Task.Medium)

      // T3 0..5
      val id3 = UUID.randomUUID()
      val tg3 = TaskGenerator(
          "T3",
          id3,
          "Test",
          ConstantGenerator(5L),
          ConstantGenerator(6L)
        ) withResources (Seq(res.name))
      val expected3 =
        new Task(id3, "T3", "Test", self, 0L, 0L, Seq(res.name), 5L, 5L, 6L, -1, Task.Medium)

      // Add all
      coordinator ! Coordinator.AddTasks(Seq(tg1, tg2, tg3))
      coordinator ! Coordinator.SimReady()

      // T2 completes
      expectMsgType[Simulation.TaskCompleted]

      // Abort T1
      coordinator ! Coordinator.AbortTasks(Seq(id1))
      coordinator ! Coordinator.SimReady()

      // T3 completes
      val Simulation.TaskCompleted(task3, time3) = expectMsgType[Simulation.TaskCompleted]
      task3.compare(expected3) should be(0)
      time3 should be(5L)

      coordinator ! Coordinator.SimDone("Test", Success(Unit))
    }


    "abort 2 simulations when the time limit is hit" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      val probe1 = TestProbe()
      val probe2 = TestProbe()

      coordinator ! Coordinator.AddSim(0L, probe1.ref)
      coordinator ! Coordinator.AddSim(1L, probe2.ref)
      coordinator ! Coordinator.LimitTime(5L)
      coordinator ! Coordinator.Start

      // Test1 starts
      probe1.expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test1", probe1.ref)

      // T1 0..10
      val id1 = UUID.randomUUID()
      val tg1 = TaskGenerator("T1", id1, "Test1", ConstantGenerator(10L), ConstantGenerator(5L))
      probe1.send(coordinator, Coordinator.AddTasks(Seq((tg1))))
      probe1.send(coordinator, Coordinator.SimReady(None))

      // Test2 starts
      probe2.expectMsg(Simulation.Start)
      probe2.reply(Coordinator.SimStarted("Test2", probe2.ref))

      // T2 1..2
      val id2 = UUID.randomUUID()
      val tg2 = TaskGenerator("T2", id2, "Test2", ConstantGenerator(8L), ConstantGenerator(5L))
      probe2.send(coordinator, Coordinator.AddTasks(Seq((tg2))))
      probe2.send(coordinator, Coordinator.SimReady(None))

      probe1.expectMsg(Simulation.Stop)
      probe2.expectMsg(Simulation.Stop)
    }
 */
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

  def containsTask(tasks: Seq[TaskInstance], t: TaskInstance): Boolean = tasks.exists(_.compare(t) == 0)
}

