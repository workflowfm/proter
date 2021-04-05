package com.workflowfm.proter.akka

import java.util.UUID

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.Success

import akka.actor.ActorSystem
import akka.testkit.TestKit
import org.scalamock.scalatest.MockFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import com.workflowfm.proter._

class MockSimulationActorTests
    extends TestKit(
      ActorSystem("MockSimulationActorTests")
    )
    with WordSpecLike
    with Matchers
    with MockFactory
    with MockSimulations
    with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "The simulation actors" must {

    "interact correctly with no tasks" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())
      val sim: Simulation = mock[Simulation]

      sim.name _ expects () returning "sim" anyNumberOfTimes ()

      sim.run _ expects () onCall (_ => coordinator.simResponse(SimDone("sim", Success(Unit)))) once

      val simRef = AkkaSimulationRef.of(sim)
      coordinator.addSimulation(0L, simRef)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with just one task" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())
      val sim: Simulation = mockSingleTask("sim", coordinator, 1L, 2L, 3L)

      val simRef = AkkaSimulationRef.of(sim)
      coordinator.addSimulation(1L, simRef)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with just two tasks in sequence" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())
      val sim: Simulation = mockTwoTasks("sim", coordinator, 0L, 2L, 2L, 3L, 5L)

      val simRef = AkkaSimulationRef.of(sim)
      coordinator.addSimulation(0L, simRef)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with ten tasks in sequence" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())
      val sim: Simulation = mockRepeater("sim", coordinator, 0L, 2L, 10)

      val simRef = AkkaSimulationRef.of(sim)
      coordinator.addSimulation(0L, simRef)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation with two tasks in parallel, then one more" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())
      val sim: Simulation = mockTwoPlusOneTasks("sim", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)

      val simRef = AkkaSimulationRef.of(sim)
      coordinator.addSimulation(0L, simRef)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two interleaved single-task simulations" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 2L, 2L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      val simRef1 = AkkaSimulationRef.of(sim1)
      coordinator.addSimulation(0L, simRef1)
      val simRef2 = AkkaSimulationRef.of(sim2)
      coordinator.addSimulation(1L, simRef2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two parallel single-task simulations, a short within a long one" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())
      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 10L, 10L)
      val sim2: Simulation = mockSingleTask("sim2", coordinator, 1L, 1L, 2L)

      val simRef1 = AkkaSimulationRef.of(sim1)
      coordinator.addSimulation(0L, simRef1)
      val simRef2 = AkkaSimulationRef.of(sim2)
      coordinator.addSimulation(1L, simRef2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with two 2plus1 simulations" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())

      val sim1: Simulation = mockTwoPlusOneTasks("sim1", coordinator, 0L, 2L, 2L, 2L, 2L, 3L, 5L)
      val sim2: Simulation = mockTwoPlusOneTasks("sim2", coordinator, 1L, 1L, 2L, 1L, 2L, 3L, 5L)

      val simRef1 = AkkaSimulationRef.of(sim1)
      coordinator.addSimulation(0L, simRef1)
      val simRef2 = AkkaSimulationRef.of(sim2)
      coordinator.addSimulation(1L, simRef2)
      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with 100 2plus1 simulations" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())

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
        val simRef = AkkaSimulationRef.of(sim)
        coordinator.addSimulation(start, simRef)
      }

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with 100x10 task simulations" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())

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
        val simRef = AkkaSimulationRef.of(sim)
        coordinator.addSimulation(start, simRef)
      }

      Await.result(coordinator.start(), 10.seconds)
    }

    "interact correctly with a simulation reacting to another" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())

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

      val simRef1 = AkkaSimulationRef.of(sim1)
      coordinator.addSimulation(0, simRef1)
      val simRef2 = AkkaSimulationRef.of(sim2)
      coordinator.addSimulation(1, simRef2)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation aborting a task without resources" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())

      val sim: Simulation = mockAbort("sim", coordinator, None)

      val simRef = AkkaSimulationRef.of(sim)
      coordinator.addSimulation(0L, simRef)

      Await.result(coordinator.start(), 3.seconds)
    }

    "interact correctly with a simulation aborting a task with resources" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())

      val res = new TaskResource("R", 0)
      coordinator.addResource(res)

      val sim: Simulation = mockAbort("sim", coordinator, Some(res))

      val simRef = AkkaSimulationRef.of(sim)
      coordinator.addSimulation(0L, simRef)

      Await.result(coordinator.start(), 3.seconds)
    }

    "abort 2 simulations when the time limit is hit" in {
      val coordinator = AkkaManager.build(new DefaultScheduler())

      val sim1: Simulation = mockSingleTask("sim1", coordinator, 0L, 3L, 3L)
      val sim2: Simulation = mockAborted("sim2", coordinator, 10L)
      val sim3: Simulation = mockAborted("sim3", coordinator, 3L)

      val simRef1 = AkkaSimulationRef.of(sim1)
      val simRef2 = AkkaSimulationRef.of(sim2)
      val simRef3 = AkkaSimulationRef.of(sim3)

      coordinator.addSimulations(
        Seq(
          (0L, simRef1),
          (0L, simRef2),
          (4L, simRef3)
        )
      )

      coordinator.limit(5L)

      Await.result(coordinator.start(), 3.seconds)
      Thread.sleep(500) // The coordinator finishes before all sims are
      // confirmed stopped, so we shutdown the actor system
      // and lose the calls to stop().
    }
  }
}
