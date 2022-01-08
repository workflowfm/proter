package com.workflowfm.proter.akka

import java.util.UUID

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.Success

import akka.actor.ActorSystem
import akka.testkit.{ TestKit, TestProbe }
import org.scalatest.BeforeAndAfterAll
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import com.workflowfm.proter._
import com.workflowfm.proter.schedule.ProterScheduler

class CoordinatorActorTests
    extends TestKit(
      ActorSystem("CoordinatorActorTests")
    )
    with AnyWordSpecLike
    with Matchers
    with BeforeAndAfterAll {
  /* implicit val executionContext: ExecutionContext = this.system.dispatcher
   * //system.dispatchers.lookup("akka.my-dispatcher") */

  override def afterAll(): Unit = {
    TestKit.shutdownActorSystem(system)
  }

  def containsTask(tasks: Seq[TaskInstance], t: TaskInstance): Boolean =
    tasks.exists(_.compare(t) == 0)

  "The Coordinator Actor" must {

    "interact correctly with a simulation with no tasks" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe = TestProbe()
      val sim = new AkkaSimulationRef("sim", probe.ref)

      coordinator.addSimulation(0L, sim)
      coordinator.start()
      probe.expectMsg(SimulationActor.Run)
      coordinator.simResponse(SimDone("sim", Success(())))
      probe.expectNoMessage()
    }

    "interact correctly with a simulation with just a ping" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe = TestProbe()
      val sim = new AkkaSimulationRef("sim", probe.ref)

      coordinator.addSimulation(0L, sim)
      coordinator.start()
      probe.expectMsg(SimulationActor.Run)

      Await.result(coordinator.ping(), 3.seconds) should be(CoordinatorActor.Time(0L))

      coordinator.simResponse(SimDone("sim", Success(())))
      probe.expectNoMessage()
    }

    "interact correctly with a simulation with one task" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe = TestProbe()
      val sim = new AkkaSimulationRef("sim", probe.ref)

      coordinator.addSimulation(0L, sim)
      coordinator.start()
      probe.expectMsg(SimulationActor.Run)

      val id = UUID.randomUUID()
      val tg = Task("T", 2L) withID id
      val expected = tg.create("sim", 0L)

      coordinator.simResponse(SimReady("sim", Seq(tg)))
      val SimulationActor.Completed(time, tasks) = probe.expectMsgType[SimulationActor.Completed]

      time should be(2L)
      containsTask(tasks, expected) should be(true)

      Await.result(coordinator.ping(), 3.seconds) should be(CoordinatorActor.Time(2L))
      coordinator.simResponse(SimDone("sim", Success(())))
    }

    "interact correctly with a simulation with two tasks in sequence" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe = TestProbe()
      val sim = new AkkaSimulationRef("sim", probe.ref)

      coordinator.addSimulation(0L, sim)
      coordinator.start()
      probe.expectMsg(SimulationActor.Run)

      val tg1 = Task("T1", 2L) withID UUID.randomUUID()
      val expected1 = tg1.create("sim", 0L)

      coordinator.simResponse(SimReady("sim", Seq(tg1)))
      val SimulationActor.Completed(time1, tasks1) = probe.expectMsgType[SimulationActor.Completed]
      time1 should be(2L)
      containsTask(tasks1, expected1) should be(true)

      val tg2 = Task("T2", 3L) withID UUID.randomUUID()
      val expected2 = tg2.create("sim", 2L)

      coordinator.simResponse(SimReady("sim", Seq(tg2)))
      val SimulationActor.Completed(time2, tasks2) = probe.expectMsgType[SimulationActor.Completed]
      time2 should be(5L)
      containsTask(tasks2, expected2) should be(true)

      coordinator.simResponse(SimDone("sim", Success(())))
    }

    "interact correctly with two single-task simulations in parallel" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe1 = TestProbe()
      val sim1 = new AkkaSimulationRef("sim1", probe1.ref)
      val probe2 = TestProbe()
      val sim2 = new AkkaSimulationRef("sim2", probe2.ref)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      coordinator.start()

      // sim1 starts
      probe1.expectMsg(SimulationActor.Run)

      // T1a 0..2
      val tg1a = Task("T1a", 2L) withID UUID.randomUUID()
      val expected1a = tg1a.create("sim1", 0L)

      coordinator.simResponse(SimReady("sim1", Seq(tg1a)))

      // sim2 starts
      probe2.expectMsg(SimulationActor.Run)

      // T2a 0..2
      val tg2a = Task("T2a", 1L) withID UUID.randomUUID()
      val expected2a = tg2a.create("sim2", 1L)

      coordinator.simResponse(SimReady("sim2", Seq(tg2a)))

      // T1a completes
      val SimulationActor.Completed(time1a, tasks1a) =
        probe1.expectMsgType[SimulationActor.Completed]
      time1a should be(2L)
      containsTask(tasks1a, expected1a) should be(true)

      // T2a completes
      val SimulationActor.Completed(time2a, tasks2a) =
        probe2.expectMsgType[SimulationActor.Completed]
      time2a should be(2L)
      containsTask(tasks2a, expected2a) should be(true)

      coordinator.simResponse(SimDone("sim1", Success(())))
      coordinator.simResponse(SimDone("sim2", Success(())))
    }

    "interact correctly with two two-task simulations in parallel" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe1 = TestProbe()
      val sim1 = new AkkaSimulationRef("sim1", probe1.ref)
      val probe2 = TestProbe()
      val sim2 = new AkkaSimulationRef("sim2", probe2.ref)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      coordinator.start()

      // sim1 starts
      probe1.expectMsg(SimulationActor.Run)

      // T1a 0..2
      val tg1a = Task("T1a", 2L) withID UUID.randomUUID()
      val expected1a = tg1a.create("sim1", 0L)

      coordinator.simResponse(SimReady("sim1", Seq(tg1a)))

      // sim2 starts
      probe2.expectMsg(SimulationActor.Run)

      // T2a 0..2
      val tg2a = Task("T2a", 1L) withID UUID.randomUUID()
      val expected2a = tg2a.create("sim2", 1L)

      coordinator.simResponse(SimReady("sim2", Seq(tg2a)))

      // T1a completes
      val SimulationActor.Completed(time1a, tasks1a) =
        probe1.expectMsgType[SimulationActor.Completed]
      time1a should be(2L)
      containsTask(tasks1a, expected1a) should be(true)

      // T2a completes
      val SimulationActor.Completed(time2a, tasks2a) =
        probe2.expectMsgType[SimulationActor.Completed]
      time2a should be(2L)
      containsTask(tasks2a, expected2a) should be(true)

      // T1b 2..6
      val tg1b = Task("T1b", 4L) withID UUID.randomUUID()
      val expected1b = tg1b.create("sim1", 2L)

      coordinator.simResponse(SimReady("sim1", Seq(tg1b)))

      // T2b 2..4
      val tg2b = Task("T2b", 2L) withID UUID.randomUUID()
      val expected2b = tg2b.create("sim2", 2L)

      coordinator.simResponse(SimReady("sim2", Seq(tg2b)))

      // T2b completes
      val SimulationActor.Completed(time2b, tasks2b) =
        probe2.expectMsgType[SimulationActor.Completed]
      time2b should be(4L)
      containsTask(tasks2b, expected2b) should be(true)

      coordinator.simResponse(SimDone("sim2", Success(())))

      // T1b completes
      val SimulationActor.Completed(time1b, tasks1b) =
        probe1.expectMsgType[SimulationActor.Completed]
      time1b should be(6L)
      containsTask(tasks1b, expected1b) should be(true)

      coordinator.simResponse(SimDone("sim1", Success(())))

    }

    "interact correctly with a simulation reacting to another" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe1 = TestProbe()
      val sim1 = new AkkaSimulationRef("sim1", probe1.ref)
      val probe2 = TestProbe()
      val sim2 = new AkkaSimulationRef("sim2", probe2.ref)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      coordinator.start()

      // sim1 starts
      probe1.expectMsg(SimulationActor.Run)

      // T1a 0..10
      val tg1a = Task("T1a", 10L) withID UUID.randomUUID()
      val expected1a = tg1a.create("sim1", 0L)

      coordinator.simResponse(SimReady("sim1", Seq(tg1a)))

      // sim2 starts
      probe2.expectMsg(SimulationActor.Run)

      // T2a 1..2
      val tg2a = Task("T2a", 1L) withID UUID.randomUUID()
      val expected2a = tg2a.create("sim2", 1L)

      coordinator.simResponse(SimReady("sim2", Seq(tg2a)))

      // T2a completes
      val SimulationActor.Completed(time2a, tasks2a) =
        probe2.expectMsgType[SimulationActor.Completed]
      time2a should be(2L)
      containsTask(tasks2a, expected2a) should be(true)

      // sim1 requests wait
      coordinator.waitFor("sim1")

      // sim2 completes
      coordinator.simResponse(SimDone("sim2", Success(())))

      // Coordinator must wait
      Thread.sleep(500)
      Await.result(coordinator.ping(), 3.seconds) should be(CoordinatorActor.Time(2L))

      // T1b 2..3
      val tg1b = Task("T1b", 1L) withID UUID.randomUUID()
      val expected1b = tg1b.create("sim1", 2L)
      coordinator.simResponse(SimReady("sim1", Seq(tg1b)))

      // T1b completes
      val SimulationActor.Completed(time1b, tasks1b) =
        probe1.expectMsgType[SimulationActor.Completed]
      time1b should be(3L)
      containsTask(tasks1b, expected1b) should be(true)

      coordinator.simResponse(SimReady("sim1", Seq()))

      // T1a completes
      val SimulationActor.Completed(time1a, tasks1a) =
        probe1.expectMsgType[SimulationActor.Completed]
      time1a should be(10L)
      containsTask(tasks1a, expected1a) should be(true)

      coordinator.simResponse(SimDone("sim1", Success(())))
    }

    "interact correctly with a simulation aborting a task without resources" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe1 = TestProbe()
      val sim1 = new AkkaSimulationRef("sim1", probe1.ref)

      coordinator.addSimulation(0L, sim1)
      coordinator.start()

      // sim1 starts
      probe1.expectMsg(SimulationActor.Run)

      // T1a 0..3
      val id1a = UUID.randomUUID()
      val tg1a = Task("T1a", 3L) withID id1a
      // T1b 0..2
      val tg1b = Task("T1b", 2L) withID UUID.randomUUID()
      // T1c 0..5
      val tg1c = Task("T1c", 5L) withID UUID.randomUUID()
      val expected1c = tg1c.create("sim1", 0L)

      // Add all
      coordinator.simResponse(SimReady("sim1", Seq(tg1a, tg1b, tg1c)))

      // T1b completes
      probe1.expectMsgType[SimulationActor.Completed]

      // Abort T1a
      coordinator.simResponse(SimReady("sim1", Seq(), Seq(id1a)))

      // T1c completes
      val SimulationActor.Completed(time1c, tasks1c) =
        probe1.expectMsgType[SimulationActor.Completed]
      time1c should be(5L)
      containsTask(tasks1c, expected1c) should be(true)

      coordinator.simResponse(SimDone("sim1", Success(())))
    }

    "interact correctly with a simulation aborting a task with resources" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe1 = TestProbe()
      val sim1 = new AkkaSimulationRef("sim1", probe1.ref)

      coordinator.addSimulation(0L, sim1)
      coordinator.start()

      // sim1 starts
      probe1.expectMsg(SimulationActor.Run)

      val res = new TaskResource("R", 0)
      coordinator.addResource(res)

      // T1a 0..3
      val id1a = UUID.randomUUID()
      val tg1a = Task("T1a", 3L) withID id1a withResources Seq(res.name) withPriority Task.High
      // T1b 0..2
      val tg1b = Task("T1b", 2L) withID UUID.randomUUID()
      // T1c 0..5
      val tg1c = Task("T1c", 5L) withID UUID.randomUUID() withResources Seq(res.name)
      val expected1c = tg1c.create("sim1", 0L)

      // Add all
      coordinator.simResponse(SimReady("sim1", Seq(tg1a, tg1b, tg1c)))

      // T1b completes
      probe1.expectMsgType[SimulationActor.Completed]

      // Abort T1a
      coordinator.simResponse(SimReady("sim1", Seq(), Seq(id1a)))

      // T1c completes
      val SimulationActor.Completed(time1c, tasks1c) =
        probe1.expectMsgType[SimulationActor.Completed]
      time1c should be(7L)
      containsTask(tasks1c, expected1c) should be(true)

      coordinator.simResponse(SimDone("sim1", Success(())))
    }

    "abort 2 simulations when the time limit is hit" in {
      val coordinator = AkkaManager.build(new ProterScheduler())

      val probe1 = TestProbe()
      val sim1 = new AkkaSimulationRef("sim1", probe1.ref)
      val probe2 = TestProbe()
      val sim2 = new AkkaSimulationRef("sim2", probe2.ref)

      coordinator.addSimulation(0L, sim1)
      coordinator.addSimulation(1L, sim2)
      coordinator.limit(5L)
      coordinator.start()

      // sim1 starts
      probe1.expectMsg(SimulationActor.Run)

      // T1a 0..10
      val tg1a = Task("T1a", 10L) withID UUID.randomUUID()

      coordinator.simResponse(SimReady("sim1", Seq(tg1a)))

      // sim2 starts
      probe2.expectMsg(SimulationActor.Run)

      // T2a 1..9
      val tg2a = Task("T2a", 8L) withID UUID.randomUUID()

      coordinator.simResponse(SimReady("sim2", Seq(tg2a)))

      probe1.expectMsg(SimulationActor.Stop)
      probe2.expectMsg(SimulationActor.Stop)
    }

  }
}
