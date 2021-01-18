package com.workflowfm.proter

import scala.collection.mutable.Map
import scala.concurrent._
import scala.concurrent.duration._

import akka.actor.ActorSystem
import akka.pattern.ask
import akka.testkit._
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

import uk.ac.ed.inf.ppapapan.subakka.Subscriber

import com.workflowfm.proter._
import com.workflowfm.proter.events.{ PrintEventHandler, PromiseHandler }
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._

@RunWith(classOf[JUnitRunner])
class FlowsTest extends FlowsIntegrationTester {
  "Flows" must {
    "execute a single flow" in {
      val r1 = new TaskResource("r1", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val flow1 = task1
      val testMetrics = singleFlowTest(flow1, List(r1))

      testMetrics.get("task1 (sim1)").value.value should be(1)
    }

    "execute an AND of two tasks which use different resources" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val flow1 = And(task1, task2)
      val testMetrics = singleFlowTest(flow1, List(r1, r2))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
    }

    "execute an AND of two tasks which use the same resources" in {
      val r1 = new TaskResource("r1", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val flow1 = And(task1, task2)
      val testMetrics = singleFlowTest(flow1, List(r1))

      val task1time = testMetrics.get("task1 (sim1)").value.value
      if (task1time == 1) testMetrics.get("task2 (sim1)").value.value should be(3)
      else testMetrics.get("task2 (sim1)").value.value should be(2)

    }

    "execute a THEN of two tasks" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val flow1 = Then(task1, task2)
      val testMetrics = singleFlowTest(flow1, List(r1, r2))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(3)
    }

    "execute nested ANDs (left associativity)" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)
      val r4 = new TaskResource("r4", 0)
      val r5 = new TaskResource("r5", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim1", ConstantGenerator(4L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val task4 = FlowTask(
        TaskGenerator("task4", "sim1", ConstantGenerator(8L), ConstantGenerator(0L)).withResources(
          Seq("r4")
        )
      )
      val task5 = FlowTask(
        TaskGenerator("task5", "sim1", ConstantGenerator(16L), ConstantGenerator(0L)).withResources(
          Seq("r5")
        )
      )
      val flow1 = And(And(And(And(task1, task2), task3), task4), task5)
      val testMetrics = singleFlowTest(flow1, List(r1, r2, r3, r4, r5))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      testMetrics.get("task3 (sim1)").value.value should be(4)
      testMetrics.get("task4 (sim1)").value.value should be(8)
      testMetrics.get("task5 (sim1)").value.value should be(16)
    }

    "execute nested ANDs (right associativity)" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)
      val r4 = new TaskResource("r4", 0)
      val r5 = new TaskResource("r5", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim1", ConstantGenerator(4L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val task4 = FlowTask(
        TaskGenerator("task4", "sim1", ConstantGenerator(8L), ConstantGenerator(0L)).withResources(
          Seq("r4")
        )
      )
      val task5 = FlowTask(
        TaskGenerator("task5", "sim1", ConstantGenerator(16L), ConstantGenerator(0L)).withResources(
          Seq("r5")
        )
      )
      val flow1 = And(task1, And(task2, And(task3, And(task4, task5))))
      val testMetrics = singleFlowTest(flow1, List(r1, r2, r3, r4, r5))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      testMetrics.get("task3 (sim1)").value.value should be(4)
      testMetrics.get("task4 (sim1)").value.value should be(8)
      testMetrics.get("task5 (sim1)").value.value should be(16)
    }

    "execute nested THENs (left associativity)" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)
      val r4 = new TaskResource("r4", 0)
      val r5 = new TaskResource("r5", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim1", ConstantGenerator(4L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val task4 = FlowTask(
        TaskGenerator("task4", "sim1", ConstantGenerator(8L), ConstantGenerator(0L)).withResources(
          Seq("r4")
        )
      )
      val task5 = FlowTask(
        TaskGenerator("task5", "sim1", ConstantGenerator(16L), ConstantGenerator(0L)).withResources(
          Seq("r5")
        )
      )
      val flow1 = Then(Then(Then(Then(task1, task2), task3), task4), task5)
      val testMetrics = singleFlowTest(flow1, List(r1, r2, r3, r4, r5))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(3)
      testMetrics.get("task3 (sim1)").value.value should be(7)
      testMetrics.get("task4 (sim1)").value.value should be(15)
      testMetrics.get("task5 (sim1)").value.value should be(31)
    }

    "execute nested THENs (right associativity)" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)
      val r4 = new TaskResource("r4", 0)
      val r5 = new TaskResource("r5", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim1", ConstantGenerator(4L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val task4 = FlowTask(
        TaskGenerator("task4", "sim1", ConstantGenerator(8L), ConstantGenerator(0L)).withResources(
          Seq("r4")
        )
      )
      val task5 = FlowTask(
        TaskGenerator("task5", "sim1", ConstantGenerator(16L), ConstantGenerator(0L)).withResources(
          Seq("r5")
        )
      )
      val flow1 = Then(task1, Then(task2, Then(task3, Then(task4, task5))))
      val testMetrics = singleFlowTest(flow1, List(r1, r2, r3, r4, r5))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(3)
      testMetrics.get("task3 (sim1)").value.value should be(7)
      testMetrics.get("task4 (sim1)").value.value should be(15)
      testMetrics.get("task5 (sim1)").value.value should be(31)
    }

    "execute mixed AND/THEN flows" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)
      val r4 = new TaskResource("r4", 0)
      val r5 = new TaskResource("r5", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim1", ConstantGenerator(4L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val task4 = FlowTask(
        TaskGenerator("task4", "sim1", ConstantGenerator(8L), ConstantGenerator(0L)).withResources(
          Seq("r4")
        )
      )
      val task5 = FlowTask(
        TaskGenerator("task5", "sim1", ConstantGenerator(16L), ConstantGenerator(0L)).withResources(
          Seq("r5")
        )
      )
      val flow1 = Then(And(task1, Then(task2, task3)), And(task4, task5))
      val testMetrics = singleFlowTest(flow1, List(r1, r2, r3, r4, r5))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      testMetrics.get("task3 (sim1)").value.value should be(6)
      testMetrics.get("task4 (sim1)").value.value should be(14)
      testMetrics.get("task5 (sim1)").value.value should be(22)
    }

    "execute OR followed by a THEN" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim1", ConstantGenerator(4L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val flow1 = Then(Or(task1, task2), task3)
      val testMetrics = singleFlowTest(flow1, List(r1, r2, r3))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      testMetrics.get("task3 (sim1)").value.value should be(5)
    }

    "execute mixed AND/THEN/OR flows" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)
      val r4 = new TaskResource("r4", 0)
      val r5 = new TaskResource("r5", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim1", ConstantGenerator(4L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val task4 = FlowTask(
        TaskGenerator("task4", "sim1", ConstantGenerator(8L), ConstantGenerator(0L)).withResources(
          Seq("r4")
        )
      )
      val task5 = FlowTask(
        TaskGenerator("task5", "sim1", ConstantGenerator(16L), ConstantGenerator(0L)).withResources(
          Seq("r5")
        )
      )
      val flow1 = Then(Then(task1, Or(task2, task3)), And(task4, task5))
      val testMetrics = singleFlowTest(flow1, List(r1, r2, r3, r4, r5))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(3)
      testMetrics.get("task3 (sim1)").value.value should be(5)
      testMetrics.get("task4 (sim1)").value.value should be(11)
      testMetrics.get("task5 (sim1)").value.value should be(19)
    }

    "execute multpile tasks of same duration" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)
      val r4 = new TaskResource("r4", 0)
      val r5 = new TaskResource("r5", 0)
      val r6 = new TaskResource("r6", 0)
      val r7 = new TaskResource("r7", 0)
      val r8 = new TaskResource("r8", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val task4 = FlowTask(
        TaskGenerator("task4", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r4")
        )
      )
      val task5 = FlowTask(
        TaskGenerator("task5", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r5")
        )
      )
      val task6 = FlowTask(
        TaskGenerator("task6", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r6")
        )
      )
      val task7 = FlowTask(
        TaskGenerator("task7", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r7")
        )
      )
      val task8 = FlowTask(
        TaskGenerator("task8", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r8")
        )
      )

      val flow1 = Then(
        And(Then(task1, task2), Then(task3, task4)),
        And(Then(task5, task6), Then(task7, task8))
      )

      val testMetrics = singleFlowTest(flow1, List(r1, r2, r3, r4, r5, r6, r7, r8))

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      testMetrics.get("task3 (sim1)").value.value should be(1)
      testMetrics.get("task4 (sim1)").value.value should be(2)
      testMetrics.get("task5 (sim1)").value.value should be(3)
      testMetrics.get("task6 (sim1)").value.value should be(4)
      testMetrics.get("task7 (sim1)").value.value should be(3)
      testMetrics.get("task8 (sim1)").value.value should be(4)
    }

    "execute a flow which uses the same task multiple times" in {
      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim1", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val flow1 = Then(And(task1, task2), task1)
      val testMetrics = singleFlowTest(flow1, List(r1, r2))

      //testMetrics.get("task1 (sim1)").value.value should be (1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      //testMetrics.get("task1 (sim1)").value.value should be (5)

    }

    "execute two simulations which use different tasks" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      val smh = new PromiseHandler(new SimMetricsHandler)
      smh.subAndForgetTo(coordinator)

      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)

      coordinator ! Coordinator.AddResources(List(r1, r2, r3))

      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim2", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim2", ConstantGenerator(4L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val flow1 = task1
      val flow2 = Then(task2, task3)

      coordinator ! Coordinator.AddSim(
        0L,
        system.actorOf(FlowSimulationActor.props("sim1", coordinator, flow1))
      )
      coordinator ! Coordinator.AddSim(
        0L,
        system.actorOf(FlowSimulationActor.props("sim2", coordinator, flow2))
      )
      coordinator ! Coordinator.Start

      val metrics = Await.result(smh.future, 3.seconds)
      val testMetrics = metrics.taskMap.map {
        case (_, tm) => tm.fullName -> tm.finished
      }

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim2)").value.value should be(2)
      testMetrics.get("task3 (sim2)").value.value should be(6)
    }

    "execute two simulations which use the same tasks" in {
      val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

      val smh = new PromiseHandler(new SimMetricsHandler)
      smh.subAndForgetTo(coordinator)

      val r1 = new TaskResource("r1", 0)
      val r2 = new TaskResource("r2", 0)
      val r3 = new TaskResource("r3", 0)

      coordinator ! Coordinator.AddResources(List(r1, r2, r3))

      val task1 = FlowTask(
        TaskGenerator("task1", "sim1", ConstantGenerator(1L), ConstantGenerator(0L)).withResources(
          Seq("r1")
        )
      )
      val task2 = FlowTask(
        TaskGenerator("task2", "sim2", ConstantGenerator(2L), ConstantGenerator(0L)).withResources(
          Seq("r2")
        )
      )
      val task3 = FlowTask(
        TaskGenerator("task3", "sim2", ConstantGenerator(4L), ConstantGenerator(0L)).withResources(
          Seq("r3")
        )
      )
      val flow1 = task1
      val flow2 = Then(task2, task1)

      coordinator ! Coordinator.AddSim(
        0L,
        system.actorOf(FlowSimulationActor.props("sim1", coordinator, flow1))
      )
      coordinator ! Coordinator.AddSim(
        0L,
        system.actorOf(FlowSimulationActor.props("sim2", coordinator, flow2))
      )
      coordinator ! Coordinator.Start

      val metrics = Await.result(smh.future, 3.seconds)
      val testMetrics = metrics.taskMap.map {
        case (_, tm) => tm.fullName -> tm.finished
      }

      //testMetrics.get("task1 (sim1)").value.value should be (1)
      testMetrics.get("task2 (sim2)").value.value should be(2)
      //testMetrics.get("task1 (sim2)").value.value should be (3)
    }
  }
}

class FlowsIntegrationTester
    extends TestKit(
      ActorSystem(
        "FlowsTest",
        ConfigFactory.parseString("""
akka {
    log-dead-letters = off
    log-dead-letters-during-shutdown = off
    stdout-loglevel = "OFF"
    loglevel = "OFF"    
    actor {
      debug {
        receive = off
        unhandled = off
      }
    }
}
    """)
      )
    )
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with OptionValues {

  implicit val executionContext: ExecutionContext = ExecutionContext.global
  implicit val timeout: Timeout = Timeout(2.seconds)

  def singleFlowTest(
      flow: Flow,
      resources: List[TaskResource],
      simName: String = "sim1"
  ): Map[String, Option[Long]] = {

    val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))

    val smh = new PromiseHandler(new SimMetricsHandler)
    Await.result(smh.subAndForgetTo(coordinator), 1.second)
    //Await.result(new PrintEventHandler().subAndForgetTo(coordinator), 1.second)

    coordinator ! Coordinator.AddResources(resources)
    coordinator ! Coordinator.AddSim(
      0L,
      system.actorOf(FlowSimulationActor.props(simName, coordinator, flow))
    )
    coordinator ! Coordinator.Start

    val metrics = Await.result(smh.future, 3.seconds)
    metrics.taskMap.map {
      case (_, tm) => tm.fullName -> tm.finished
    }
  }

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }
}