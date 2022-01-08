package com.workflowfm.proter.akka

import java.util.UUID

import scala.concurrent._
import scala.util.{ Failure, Success, Try }

import akka.actor.ActorSystem
import akka.testkit.{ TestKit, TestProbe }
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import com.workflowfm.proter._

class SimulationActorTests
    extends TestKit(ActorSystem("SimulationActorTests"))
    with SimulationTester
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  implicit val executionContext: ExecutionContext = system.dispatcher

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Simulation Actors" must {

    "interact correctly having no tasks" in {
      val probe = TestProbe()
      val coordinator: AkkaManager = new AkkaManager(probe.ref)

      val sim = AkkaSimulationRef.of(new NoTasks("sim", coordinator))

      sim.run()
      probe.expectMsg(CoordinatorActor.Response(SimDone("sim", Success(()))))
      probe.expectNoMessage()
    }

    "interact correctly having one task" in {
      val probe = TestProbe()
      val coordinator: AkkaManager = new AkkaManager(probe.ref)

      val sim = AkkaSimulationRef.of(
        new SingleTaskSimulation("sim", coordinator, Seq("r1"), Constant(2L))
      )

      sim.run()

      val CoordinatorActor.Response(response) = probe.expectMsgType[CoordinatorActor.Response]
      response shouldBe a[SimReady]
      val SimReady(name, tasks, aborts, _) = response
      name should be("sim")
      tasks.length should be(1)
      aborts.length should be(0)

      val task = tasks.head.withID(UUID.randomUUID).create("sim", 0)
      sim.completed(2L, Seq(task))

      probe.expectMsg(CoordinatorActor.Response(SimDone("sim", Success((task, 2L)))))
      probe.expectNoMessage()
    }

    "interact correctly having 2 tasks in sequence with callback" in {
      val probe = TestProbe()
      val coordinator: AkkaManager = new AkkaManager(probe.ref)

      val sim = AkkaSimulationRef.of(new TwoTasks("sim", coordinator))

      sim.run()

      // task1
      val CoordinatorActor.Response(response1) = probe.expectMsgType[CoordinatorActor.Response]
      response1 shouldBe a[SimReady]
      val SimReady(name1, tasks1, aborts1, _) = response1
      name1 should be("sim")
      tasks1.length should be(1)
      aborts1.length should be(0)

      val task1 = tasks1.head.create("sim", 0)
      sim.completed(2L, Seq(task1))

      // task2
      val CoordinatorActor.Response(response2) = probe.expectMsgType[CoordinatorActor.Response]
      response2 shouldBe a[SimReady]
      val SimReady(name2, tasks2, aborts2, _) = response2
      name2 should be("sim")
      tasks2.length should be(1)
      aborts2.length should be(0)

      val task2 = tasks2.head.create("sim", 2L)
      sim.completed(4L, Seq(task2))

      probe.expectMsg(CoordinatorActor.Response(SimDone("sim", Success((task2, 4L)))))
      probe.expectNoMessage()
    }

    "interact correctly having 3 tasks in sequence with Futures" in {
      val probe = TestProbe()
      val coordinator: AkkaManager = new AkkaManager(probe.ref)

      val sim = AkkaSimulationRef.of(new ThreeFutureTasks("sim", coordinator))

      sim.run()

      // task1
      val CoordinatorActor.Response(response1) = probe.expectMsgType[CoordinatorActor.Response]
      response1 shouldBe a[SimReady]
      val SimReady(name1, tasks1, aborts1, _) = response1
      name1 should be("sim")
      tasks1.length should be(1)
      aborts1.length should be(0)

      val task1 = tasks1.head.create("sim", 0)
      sim.completed(2L, Seq(task1))

      // task2
      val CoordinatorActor.Response(response2) = probe.expectMsgType[CoordinatorActor.Response]
      response2 shouldBe a[SimReady]
      val SimReady(name2, tasks2, aborts2, _) = response2
      name2 should be("sim")
      tasks2.length should be(1)
      aborts2.length should be(0)

      val task2 = tasks2.head.create("sim", 2L)
      sim.completed(4L, Seq(task2))

      // task3
      val CoordinatorActor.Response(response3) = probe.expectMsgType[CoordinatorActor.Response]
      response3 shouldBe a[SimReady]
      val SimReady(name3, tasks3, aborts3, _) = response3
      name3 should be("sim")
      tasks3.length should be(1)
      aborts3.length should be(0)

      val task3 = tasks3.head.create("sim", 4L)
      sim.completed(7L, Seq(task3))

      probe.expectMsg(CoordinatorActor.Response(SimDone("sim", Success((task3, 7L)))))
      probe.expectNoMessage()
    }

    "stop between 2 tasks in sequence" in {
      val probe = TestProbe()
      val coordinator: AkkaManager = new AkkaManager(probe.ref)

      val sim = AkkaSimulationRef.of(new TwoTasks("sim", coordinator))

      sim.run()

      // task1
      val CoordinatorActor.Response(response1) = probe.expectMsgType[CoordinatorActor.Response]
      response1 shouldBe a[SimReady]
      val SimReady(name1, tasks1, aborts1, _) = response1
      name1 should be("sim")
      tasks1.length should be(1)
      aborts1.length should be(0)

      val task1 = tasks1.head.create("sim", 0)
      sim.completed(2L, Seq(task1))

      // task2
      val CoordinatorActor.Response(response2) = probe.expectMsgType[CoordinatorActor.Response]
      response2 shouldBe a[SimReady]
      val SimReady(name2, tasks2, aborts2, _) = response2
      name2 should be("sim")
      tasks2.length should be(1)
      aborts2.length should be(0)

      val task2 = tasks2.head.create("sim", 2L)
      sim.stop()
      sim.completed(4L, Seq(task2))

      val CoordinatorActor.Response(response3) = probe.expectMsgType[CoordinatorActor.Response]
      response3 shouldBe a[SimDone]
      val SimDone(name3, Failure(ex)) = response3
      name3 should be("sim")
      ex shouldBe a[Simulation.SimulationStoppingException]
    }

    "stop after the 1st task in a sequence of 3 tasks with futures" in {
      val probe = TestProbe()
      val coordinator: AkkaManager = new AkkaManager(probe.ref)

      val sim = AkkaSimulationRef.of(new ThreeFutureTasks("sim", coordinator))

      sim.run()

      // task1
      val CoordinatorActor.Response(response1) = probe.expectMsgType[CoordinatorActor.Response]
      response1 shouldBe a[SimReady]
      val SimReady(name1, tasks1, aborts1, _) = response1
      name1 should be("sim")
      tasks1.length should be(1)
      aborts1.length should be(0)

      val task1 = tasks1.head.create("sim", 0)
      sim.stop()
      sim.completed(2L, Seq(task1))

      val CoordinatorActor.Response(response2) = probe.expectMsgType[CoordinatorActor.Response]
      response2 shouldBe a[SimDone]
      val SimDone(name2, Failure(ex)) = response2
      name2 should be("sim")
      ex shouldBe a[Simulation.SimulationStoppingException]
    }

    "stop after the 2nd task in a sequence of 3 tasks with futures" in {
      val probe = TestProbe()
      val coordinator: AkkaManager = new AkkaManager(probe.ref)

      val sim = AkkaSimulationRef.of(new ThreeFutureTasks("sim", coordinator))

      sim.run()

      // task1
      val CoordinatorActor.Response(response1) = probe.expectMsgType[CoordinatorActor.Response]
      response1 shouldBe a[SimReady]
      val SimReady(name1, tasks1, aborts1, _) = response1
      name1 should be("sim")
      tasks1.length should be(1)
      aborts1.length should be(0)

      val task1 = tasks1.head.create("sim", 0)
      sim.completed(2L, Seq(task1))

      // task2
      val CoordinatorActor.Response(response2) = probe.expectMsgType[CoordinatorActor.Response]
      response2 shouldBe a[SimReady]
      val SimReady(name2, tasks2, aborts2, _) = response2
      name2 should be("sim")
      tasks2.length should be(1)
      aborts2.length should be(0)

      val task2 = tasks2.head.create("sim", 2L)
      sim.stop()
      sim.completed(4L, Seq(task2))

      val CoordinatorActor.Response(response3) = probe.expectMsgType[CoordinatorActor.Response]
      response3 shouldBe a[SimDone]
      val SimDone(name3, Failure(ex)) = response3
      name3 should be("sim")
      ex shouldBe a[Simulation.SimulationStoppingException]
    }
  }
}
