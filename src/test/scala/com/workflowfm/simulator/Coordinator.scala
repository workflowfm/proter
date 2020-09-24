package com.workflowfm.simulator

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit, TestProbe }
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import scala.concurrent._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }
import java.util.UUID
import com.workflowfm.simulator.metrics._
import uk.ac.ed.inf.ppapapan.subakka.MockPublisher

class CoordinatorTests
    extends TestKit(
      ActorSystem("CoordinatorTests", ConfigFactory.parseString(MockPublisher.config))
    )
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ImplicitSender {
  implicit val executionContext = ExecutionContext.global //system.dispatchers.lookup("akka.my-dispatcher")
  implicit val timeout: FiniteDuration = 10.seconds

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "The Coordinator" must {

    "interact correctly with a simulation with no tasks" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test", self)
      coordinator ! Coordinator.SimDone("Test", Success(Unit))
      expectNoMsg()
    }

    "interact correctly with a simulation with just a ping" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test", self)
      coordinator ! Coordinator.Ping
      expectMsgType[Coordinator.Time].time should be(0L)
      coordinator ! Coordinator.SimDone("Test", Success(Unit))
    }

    "interact correctly with a simulation with one task" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test", self)

      val id = UUID.randomUUID()
      val tg = TaskGenerator("T", id, "Test", ConstantGenerator(2L), ConstantGenerator(5L))
      val expected = new Task(id, "T", "Test", self, 0L, 0L, Seq(), 2L, 2L, 5L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg)))
      coordinator ! Coordinator.SimReady

      val Simulation.TaskCompleted(task, time) = expectMsgType[Simulation.TaskCompleted]
      time should be(2L)
      task.compare(expected) should be(0)
      coordinator ! Coordinator.Ping
      expectMsgType[Coordinator.Time].time should be(2L)
      coordinator ! Coordinator.SimDone("Test", Success(Unit))
    }

    "interact correctly with a simulation with two tasks in sequence" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test", self)

      // Add task T1 0..2
      val id1 = UUID.randomUUID()
      val tg1 = TaskGenerator("T1", id1, "Test", ConstantGenerator(2L), ConstantGenerator(5L))
      val expected1 = new Task(id1, "T1", "Test", self, 0L, 0L, Seq(), 2L, 2L, 5L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg1)))
      coordinator ! Coordinator.SimReady

      // T1 completes
      val Simulation.TaskCompleted(task1, time1) = expectMsgType[Simulation.TaskCompleted]
      time1 should be(2L)
      task1.compare(expected1) should be(0)

      // Add T2 2..5
      val id2 = UUID.randomUUID()
      val tg2 = TaskGenerator("T2", id2, "Test", ConstantGenerator(3L), ConstantGenerator(6L))
      val expected2 = new Task(id2, "T2", "Test", self, 2L, 0L, Seq(), 3L, 3L, 6L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg2)))
      coordinator ! Coordinator.AckTasks(Seq(id1))

      // T2 completes
      val Simulation.TaskCompleted(task2, time2) = expectMsgType[Simulation.TaskCompleted]
      time2 should be(5L)
      task2.compare(expected2) should be(0)

      // Done
      coordinator ! Coordinator.SimDone("Test", Success(Unit))
    }

    "interact correctly with a simulation with two tasks in parallel, then one more" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test", self)

      // T1 0..2
      val id1 = UUID.randomUUID()
      val tg1 = TaskGenerator("T1", id1, "Test", ConstantGenerator(2L), ConstantGenerator(5L))
      val expected1 = new Task(id1, "T1", "Test", self, 0L, 0L, Seq(), 2L, 2L, 5L, -1, Task.Medium)

      // T2 0..2
      val id2 = UUID.randomUUID()
      val tg2 = TaskGenerator("T2", id2, "Test", ConstantGenerator(2L), ConstantGenerator(6L))
      val expected2 = new Task(id2, "T2", "Test", self, 0L, 0L, Seq(), 2L, 2L, 6L, -1, Task.Medium)

      // Add both
      coordinator ! Coordinator.AddTasks(Seq(tg1, tg2))
      coordinator ! Coordinator.SimReady

      // T1 and T2 complete
      expectMsgType[Simulation.TaskCompleted]
      expectMsgType[Simulation.TaskCompleted]

      coordinator ! Coordinator.Ping
      expectMsgType[Coordinator.Time].time should be(2L)

      // T3 2..5
      val id3 = UUID.randomUUID()
      val tg3 = TaskGenerator("T3", id3, "Test", ConstantGenerator(3L), ConstantGenerator(6L))
      val expected3 = new Task(id3, "T3", "Test", self, 2L, 0L, Seq(), 3L, 3L, 6L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg3)))

      // Ack T1 first
      coordinator ! Coordinator.AckTasks(Seq(id1))

      // Coordinator should wait
      Thread.sleep(500)
      coordinator ! Coordinator.Ping
      expectMsgType[Coordinator.Time].time should be(2L)

      // Ack T2
      coordinator ! Coordinator.AckTasks(Seq(id2))

      // T3 completes
      val Simulation.TaskCompleted(task3, time3) = expectMsgType[Simulation.TaskCompleted]
      time3 should be(5L)
      task3.compare(expected3) should be(0)

      coordinator ! Coordinator.SimDone("Test", Success(Unit))
    }

    "interact correctly with two simulations in parallel" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      // probe represents a 2nd simulation starting at 1
      val probe = TestProbe()

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.AddSim(1L, probe.ref)
      coordinator ! Coordinator.Start

      // Test1 starts
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test1", self)

      // T1a 0..2
      val id1a = UUID.randomUUID()
      val tg1a = TaskGenerator("T1a", id1a, "Test1", ConstantGenerator(2L), ConstantGenerator(5L))
      val expected1a = new Task(id1a, "T1a", "Test1", self, 0L, 0L, Seq(), 2L, 2L, 5L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg1a)))
      coordinator ! Coordinator.SimReady

      // Test2 starts
      probe.expectMsg(Simulation.Start)
      probe.reply(Coordinator.SimStarted("Test2", self))

      // T2a 1..2
      val id2a = UUID.randomUUID()
      val tg2a = TaskGenerator("T2a", id2a, "Test2", ConstantGenerator(1L), ConstantGenerator(5L))
      val expected2a = new Task(id2a, "T2a", "Test2", self, 1L, 0L, Seq(), 1L, 1L, 5L, -1, Task.Medium)
      probe.send(coordinator, Coordinator.AddTasks(Seq((tg2a))))
      probe.send(coordinator, Coordinator.SimReady)

      // T1a completes
      val Simulation.TaskCompleted(task1a, time1a) =
        expectMsgType[Simulation.TaskCompleted]
      time1a should be(2L)
      task1a.compare(expected1a) should be(0)

      // T2a completes
      val Simulation.TaskCompleted(task2a, time2a) =
        probe.expectMsgType[Simulation.TaskCompleted]
      time2a should be(2L)
      task2a.compare(expected2a) should be(0)

      coordinator ! Coordinator.SimDone("Test1", Success(Unit))
      probe.send(coordinator, Coordinator.SimDone("Test2", Success(Unit)))
    }

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
      coordinator ! Coordinator.SimReady

      // Test2 starts
      probe.expectMsg(Simulation.Start)
      probe.reply(Coordinator.SimStarted("Test2", self))

      // T2a 1..2
      val id2a = UUID.randomUUID()
      val tg2a = TaskGenerator("T2a", id2a, "Test2", ConstantGenerator(1L), ConstantGenerator(5L))
      val expected2a = new Task(id2a, "T2a", "Test2", self, 1L, 0L, Seq(), 1L, 1L, 5L, -1, Task.Medium)
      probe.send(coordinator, Coordinator.AddTasks(Seq((tg2a))))
      probe.send(coordinator, Coordinator.SimReady)

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
      val expected1b = new Task(id1b, "T1b", "Test1", self, 2L, 0L, Seq(), 1L, 1L, 5L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg1b)))
      coordinator ! Coordinator.SimReady

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

    "interact correctly with a simulation aborting a task without" in {
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
      coordinator ! Coordinator.SimReady

      // T2 completes
      expectMsgType[Simulation.TaskCompleted]

      // Abort T1
      coordinator ! Coordinator.AbortTasks(Seq(id1))
      coordinator ! Coordinator.SimReady

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
      val tg1 = TaskGenerator("T1", id1, "Test", ConstantGenerator(3L), ConstantGenerator(5L)) withResources (Seq(res.name))
      val expected1 = new Task(id1, "T1", "Test", self, 0L, 0L, Seq(res.name), 3L, 3L, 5L, -1, Task.Medium)

      // T2 0..2
      val id2 = UUID.randomUUID()
      val tg2 = TaskGenerator("T2", id2, "Test", ConstantGenerator(2L), ConstantGenerator(6L))
      val expected2 = new Task(id2, "T2", "Test", self, 0L, 0L, Seq(), 2L, 2L, 6L, -1, Task.Medium)

      // T3 0..5
      val id3 = UUID.randomUUID()
      val tg3 = TaskGenerator("T3", id3, "Test", ConstantGenerator(5L), ConstantGenerator(6L)) withResources (Seq(res.name))
      val expected3 = new Task(id3, "T3", "Test", self, 0L, 0L, Seq(res.name), 5L, 5L, 6L, -1, Task.Medium)

      // Add all
      coordinator ! Coordinator.AddTasks(Seq(tg1, tg2, tg3))
      coordinator ! Coordinator.SimReady

      // T2 completes
      expectMsgType[Simulation.TaskCompleted]

      // Abort T1
      coordinator ! Coordinator.AbortTasks(Seq(id1))
      coordinator ! Coordinator.SimReady

      // T3 completes
      val Simulation.TaskCompleted(task3, time3) = expectMsgType[Simulation.TaskCompleted]
      task3.compare(expected3) should be(0)
      time3 should be(5L)

      coordinator ! Coordinator.SimDone("Test", Success(Unit))
    }
  }
}
