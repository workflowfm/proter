package com.workflowfm.proter

import scala.collection.mutable.Map
import scala.concurrent._
import scala.concurrent.duration._

import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import com.workflowfm.proter._
import com.workflowfm.proter.events.{ PrintEventHandler, PromiseHandler }
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import com.workflowfm.proter.schedule.ProterScheduler

class FlowTests extends FlowsTester {
  "Flows" should {
    "execute a single flow" in {
      val task1 = new FlowTask(Task("task1", 1L))
      val flow1 = task1
      val testMetrics = singleFlowTest(flow1, List())

      testMetrics.get("task1 (sim1)").value.value should be(1)
    }

    "execute an AND of two tasks which use different resources" in {
      val task1 = new FlowTask(Task("task1", 1L))
      val task2 = new FlowTask(Task("task2", 2L))
      val flow1 = new And(task1, task2)
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
    }

    "execute an AND of two tasks which use the same resources" in {
      val r1 = new TaskResource("r1", 0)
      val task1 = new FlowTask(
        Task("task1", 1L).withResources(
          Seq("r1")
        )
      )
      val task2 = new FlowTask(
        Task("task2", 2L).withResources(
          Seq("r1")
        )
      )
      val flow1 = new And(task1, task2)
      val testMetrics = singleFlowTest(flow1, List(r1))

      val task1time = testMetrics.get("task1 (sim1)").value.value
      if task1time == 1 then testMetrics.get("task2 (sim1)").value.value should be(3)
      else testMetrics.get("task2 (sim1)").value.value should be(2)

    }

    "execute a THEN of two tasks" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val flow1 = new Then(task1, task2)
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(3)
    }

    "execute nested ANDs (left associativity)" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val task3 = new FlowTask(
        Task("task3", 4L)
      )
      val task4 = new FlowTask(
        Task("task4", 8L)
      )
      val task5 = new FlowTask(
        Task("task5", 16L)
      )
      val flow1 = new And(new And(new And(new And(task1, task2), task3), task4), task5)
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      testMetrics.get("task3 (sim1)").value.value should be(4)
      testMetrics.get("task4 (sim1)").value.value should be(8)
      testMetrics.get("task5 (sim1)").value.value should be(16)
    }

    "execute nested ANDs (right associativity)" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val task3 = new FlowTask(
        Task("task3", 4L)
      )
      val task4 = new FlowTask(
        Task("task4", 8L)
      )
      val task5 = new FlowTask(
        Task("task5", 16L)
      )
      val flow1 = new And(task1, new And(task2, new And(task3, new And(task4, task5))))
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      testMetrics.get("task3 (sim1)").value.value should be(4)
      testMetrics.get("task4 (sim1)").value.value should be(8)
      testMetrics.get("task5 (sim1)").value.value should be(16)
    }

    "execute nested THENs (left associativity)" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val task3 = new FlowTask(
        Task("task3", 4L)
      )
      val task4 = new FlowTask(
        Task("task4", 8L)
      )
      val task5 = new FlowTask(
        Task("task5", 16L)
      )
      val flow1 = new Then(new Then(new Then(new Then(task1, task2), task3), task4), task5)
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(3)
      testMetrics.get("task3 (sim1)").value.value should be(7)
      testMetrics.get("task4 (sim1)").value.value should be(15)
      testMetrics.get("task5 (sim1)").value.value should be(31)
    }

    "execute nested THENs (right associativity)" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val task3 = new FlowTask(
        Task("task3", 4L)
      )
      val task4 = new FlowTask(
        Task("task4", 8L)
      )
      val task5 = new FlowTask(
        Task("task5", 16L)
      )
      val flow1 = new Then(task1, new Then(task2, new Then(task3, new Then(task4, task5))))
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(3)
      testMetrics.get("task3 (sim1)").value.value should be(7)
      testMetrics.get("task4 (sim1)").value.value should be(15)
      testMetrics.get("task5 (sim1)").value.value should be(31)
    }

    "execute nested THENs (right associativity) (operator)" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val task3 = new FlowTask(
        Task("task3", 4L)
      )
      val task4 = new FlowTask(
        Task("task4", 8L)
      )
      val task5 = new FlowTask(
        Task("task5", 16L)
      )
      val flow1 = task1 > task2 > task3 > task4 > task5
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(3)
      testMetrics.get("task3 (sim1)").value.value should be(7)
      testMetrics.get("task4 (sim1)").value.value should be(15)
      testMetrics.get("task5 (sim1)").value.value should be(31)
    }

    "execute mixed AND/THEN flows" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val task3 = new FlowTask(
        Task("task3", 4L)
      )
      val task4 = new FlowTask(
        Task("task4", 8L)
      )
      val task5 = new FlowTask(
        Task("task5", 16L)
      )
      val flow1 = new Then(new And(task1, new Then(task2, task3)), new And(task4, task5))
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      testMetrics.get("task3 (sim1)").value.value should be(6)
      testMetrics.get("task4 (sim1)").value.value should be(14)
      testMetrics.get("task5 (sim1)").value.value should be(22)
    }

    "execute OR followed by a THEN" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val task3 = new FlowTask(
        Task("task3", 4L)
      )
      val flow1 = new Then(new Or(task1, task2), task3)
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      testMetrics.get("task3 (sim1)").value.value should be(5)
    }

    "execute mixed AND/THEN/OR flows" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val task3 = new FlowTask(
        Task("task3", 4L)
      )
      val task4 = new FlowTask(
        Task("task4", 8L)
      )
      val task5 = new FlowTask(
        Task("task5", 16L)
      )
      val flow1 = new Then(new Then(task1, new Or(task2, task3)), new And(task4, task5))
      val testMetrics = singleFlowTest(flow1)

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim1)").value.value should be(3)
      testMetrics.get("task3 (sim1)").value.value should be(5)
      testMetrics.get("task4 (sim1)").value.value should be(11)
      testMetrics.get("task5 (sim1)").value.value should be(19)
    }

    "execute multpile tasks of same duration" in {
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 1L)
      )
      val task3 = new FlowTask(
        Task("task3", 1L)
      )
      val task4 = new FlowTask(
        Task("task4", 1L)
      )
      val task5 = new FlowTask(
        Task("task5", 1L)
      )
      val task6 = new FlowTask(
        Task("task6", 1L)
      )
      val task7 = new FlowTask(
        Task("task7", 1L)
      )
      val task8 = new FlowTask(
        Task("task8", 1L)
      )

      val flow1 = new Then(
        new And(new Then(task1, task2), new Then(task3, task4)),
        new And(new Then(task5, task6), new Then(task7, task8))
      )

      val testMetrics = singleFlowTest(flow1)

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
      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val flow1 = new Then(new And(task1, task2), task1)
      val testMetrics = singleFlowTest(flow1)

      // testMetrics.get("task1 (sim1)").value.value should be (1)
      testMetrics.get("task2 (sim1)").value.value should be(2)
      // testMetrics.get("task1 (sim1)").value.value should be (5)

    }

    "execute two simulations which use different tasks" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val smh = new PromiseHandler(new SimMetricsHandler)
      coordinator.subscribe(smh)

      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val task3 = new FlowTask(
        Task("task3", 4L)
      )
      val flow1 = task1
      val flow2 = new Then(task2, task3)

      coordinator.addSimulation(
        0L,
        new FlowSimulation("sim1", coordinator, flow1)
      )
      coordinator.addSimulation(
        0L,
        new FlowSimulation("sim2", coordinator, flow2)
      )
      coordinator.start()

      val metrics = Await.result(smh.future, 3.seconds)
      val testMetrics = metrics.taskMap.map { case (_, tm) =>
        tm.fullName -> tm.finished
      }

      testMetrics.get("task1 (sim1)").value.value should be(1)
      testMetrics.get("task2 (sim2)").value.value should be(2)
      testMetrics.get("task3 (sim2)").value.value should be(6)
    }

    "execute two simulations which use the same tasks" in {
      val coordinator = new Coordinator(new ProterScheduler())

      val smh = new PromiseHandler(new SimMetricsHandler)
      coordinator.subscribe(smh)

      val task1 = new FlowTask(
        Task("task1", 1L)
      )
      val task2 = new FlowTask(
        Task("task2", 2L)
      )
      val flow1 = task1
      val flow2 = new Then(task2, task1)

      coordinator.addSimulation(
        0L,
        new FlowSimulation("sim1", coordinator, flow1)
      )
      coordinator.addSimulation(
        0L,
        new FlowSimulation("sim2", coordinator, flow2)
      )
      coordinator.start()

      val metrics = Await.result(smh.future, 3.seconds)
      val testMetrics = metrics.taskMap.map { case (_, tm) =>
        tm.fullName -> tm.finished
      }

      // testMetrics.get("task1 (sim1)").value.value should be (1)
      testMetrics.get("task2 (sim2)").value.value should be(2)
      // testMetrics.get("task1 (sim2)").value.value should be (3)
    }
  }
}

class FlowsTester extends AnyWordSpecLike with Matchers with OptionValues {

  def singleFlowTest(
      flow: Flow,
      resources: List[TaskResource] = List(),
      simName: String = "sim1"
  ): Map[String, Option[Long]] = {

    val coordinator = new Coordinator(new ProterScheduler())

    val smh = new PromiseHandler(new SimMetricsHandler)
    coordinator.subscribe(smh)

    coordinator.addResources(resources)
    coordinator.addSimulation(
      0L,
      new FlowSimulation(simName, coordinator, flow)
    )
    coordinator.start()

    val metrics = Await.result(smh.future, 3.seconds)
    metrics.taskMap.map { case (_, tm) =>
      tm.fullName -> tm.finished
    }
  }
}
