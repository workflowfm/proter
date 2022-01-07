package com.workflowfm.proter

import java.util.UUID

import scala.collection.mutable.Map
import scala.concurrent._
import scala.concurrent.duration._

import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import com.workflowfm.proter.events.PromiseHandler
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import com.workflowfm.proter.schedule.LookaheadScheduler

class LookaheadTests extends LookaheadTester with AnyWordSpecLike with Matchers with OptionValues {

  "Lookahead systems" should {

    "execute a basic scenario without blocking high priority tasks [ 1 > ( (2>3) + 4 ) ]" in {
      val coordinator = getCoordinator()
      val testMetrics = singleSimulationTest(coordinator, new DummySim(coordinator))
      testMetrics.size should be(4)
      testMetrics.get("task1 (sim1)").value.value should be(2)
      testMetrics.get("task2 (sim1)").value.value should be(4)
      testMetrics.get("task3 (sim1)").value.value should be(8)
      testMetrics.get("task4 (sim1)").value.value should be(12)
    }

    "consider in-progress tasks [ 1 > ( (2>3) + (4>5) ) ]" in {
      val coordinator = getCoordinator()
      val testMetrics = singleSimulationTest(coordinator, new DummySim2(coordinator))
      testMetrics.size should be(5)
      testMetrics.get("task1 (sim1)").value.value should be(2)
      testMetrics.get("task2 (sim1)").value.value should be(6)
      testMetrics.get("task3 (sim1)").value.value should be(9)
      testMetrics.get("task4 (sim1)").value.value should be(4)
      testMetrics.get("task5 (sim1)").value.value should be(12)
    }

    "execute many-to-one future task relationships [ 1 > ( ( (2+3+4)>5 ) + 6 ) ]" in {
      val coordinator = getCoordinator()
      val testMetrics = singleSimulationTest(coordinator, new DummySim3(coordinator))
      testMetrics.size should be(6)
      testMetrics.get("task1 (sim1)").value.value should be(2)
      testMetrics.get("task2 (sim1)").value.value should be(6)
      testMetrics.get("task3 (sim1)").value.value should be(5)
      testMetrics.get("task4 (sim1)").value.value should be(4)
      testMetrics.get("task5 (sim1)").value.value should be(10)
      testMetrics.get("task6 (sim1)").value.value should be(20)
    }
  }

  "Lookahead flows" should {

    "execute a basic scenario without blocking high priority tasks [ 1 > ( (2>3) + 4 ) ]" in {
      val coordinator = getCoordinator()
      val testMetrics = singleSimulationTest(coordinator, new FlowDummySim(coordinator))
      testMetrics.size should be(4)
      testMetrics.get("task1 (sim1)").value.value should be(2)
      testMetrics.get("task2 (sim1)").value.value should be(4)
      testMetrics.get("task3 (sim1)").value.value should be(8)
      testMetrics.get("task4 (sim1)").value.value should be(12)
    }

    "execute simulation where in-progress tasks must be considered [ 1 > ( (2>3) + (4>5) ) ]" in {
      val coordinator = getCoordinator()
      val testMetrics = singleSimulationTest(coordinator, new FlowDummySim2(coordinator))
      testMetrics.size should be(5)
      testMetrics.get("task1 (sim1)").value.value should be(2)
      testMetrics.get("task2 (sim1)").value.value should be(6)
      testMetrics.get("task3 (sim1)").value.value should be(9)
      testMetrics.get("task4 (sim1)").value.value should be(4)
      testMetrics.get("task5 (sim1)").value.value should be(12)
    }

    "execute simulation where a many-to-one future task relationship is involved [ 1 > ( ( (2+3+4)>5 ) + 6 ) ]" in {
      val coordinator = getCoordinator()
      val testMetrics = singleSimulationTest(coordinator, new FlowDummySim3(coordinator))
      testMetrics.size should be(6)
      testMetrics.get("task1 (sim1)").value.value should be(2)
      testMetrics.get("task2 (sim1)").value.value should be(6)
      testMetrics.get("task3 (sim1)").value.value should be(5)
      testMetrics.get("task4 (sim1)").value.value should be(4)
      testMetrics.get("task5 (sim1)").value.value should be(10)
      testMetrics.get("task6 (sim1)").value.value should be(20)
    }
  }

}

trait LookaheadTester {

  def getCoordinator(): Coordinator = {
    val coordinator = new Coordinator(new LookaheadScheduler())

    coordinator.addResources(
      Seq(
        new TaskResource("r1", 0),
        new TaskResource("r2", 0),
        new TaskResource("r3", 0),
        new TaskResource("r4", 0),
        new TaskResource("r5", 0),
        new TaskResource("r6", 0)
      )
    )

    coordinator
  }

  def singleSimulationTest(
      coordinator: Coordinator,
      sim: Simulation with LookingAhead
  ): Map[String, Option[Long]] = {
    val smh = new PromiseHandler(new SimMetricsHandler)
    coordinator.subscribe(smh)

    coordinator.addSimulation(0L, sim)
    coordinator.start()
    val metrics = Await.result(smh.future, 3.seconds)
    metrics.taskMap map { x => x._2.fullName -> x._2.finished }
  }
}

abstract class DummyLookaheadSim(
    override val name: String,
    override protected val manager: Manager
) extends AsyncSimulation
    with LookingAhead

class DummySim(coordinator: Manager) extends DummyLookaheadSim("sim1", coordinator) {
  var tick = false

  override def run(): Unit = {
    val id1 = UUID.randomUUID
    val id2 = UUID.randomUUID
    val id3 = UUID.randomUUID
    val id4 = UUID.randomUUID
    val generator1 = Task(
      "task1",
      Some(id1),
      Constant(2L),
      Constant(0L),
      0L,
      Seq("r3"),
      (-1),
      Task.High
    )
    val generator2 = Task(
      "task2",
      Some(id2),
      Constant(2L),
      Constant(0L),
      0L,
      Seq("r1"),
      (-1),
      Task.High
    )
    val generator3 = Task(
      "task3",
      Some(id3),
      Constant(4L),
      Constant(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator4 = Task(
      "task4",
      Some(id4),
      Constant(4L),
      Constant(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.Low
    )

    lookahead = lookahead +> (id1, generator2) +> (id1, generator4) +> (id2, generator3)

    //Equal to flow: task1 > ( (task2 > task3) + task4 )
    // i.e. the sequence task2>task3 happens in parallel to task4
    val task1 = task(
      generator1,
      callback((_, _) => {
        task(
          generator2,
          callback((_, _) => {
            task(
              generator3,
              callback((_, _) => { if (tick) succeed(Unit) else { tick = true; ack(Seq(id3)) } })
            ); ack(Seq(id2))
          })
        )
        task(
          generator4,
          callback((_, _) => { if (tick) succeed(Unit) else { tick = true; ack(Seq(id4)) } })
        )
        ack(Seq(id1))
      })
    )

    ready()
  }
}

class DummySim2(coordinator: Manager) extends DummyLookaheadSim("sim1", coordinator) {
  var tick = false

  override def run(): Unit = {
    val id1 = UUID.randomUUID
    val id2 = UUID.randomUUID
    val id3 = UUID.randomUUID
    val id4 = UUID.randomUUID
    val id5 = UUID.randomUUID
    val generator1 = Task(
      "task1",
      Some(id1),
      Constant(2L),
      Constant(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator2 = Task(
      "task2",
      Some(id2),
      Constant(4L),
      Constant(0L),
      0L,
      Seq("r1"),
      (-1),
      Task.High
    )
    val generator3 = Task(
      "task3",
      Some(id3),
      Constant(3L),
      Constant(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator4 = Task(
      "task4",
      Some(id4),
      Constant(2L),
      Constant(0L),
      0L,
      Seq("r3"),
      (-1),
      Task.Low
    )
    val generator5 = Task(
      "task5",
      Some(id5),
      Constant(3L),
      Constant(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.Low
    )

    lookahead =
      lookahead +> (id1, generator2) +> (id1, generator4) +> (id2, generator3) +> (id4, generator5)

    val task1 = task(
      generator1,
      callback((_, _) => {
        task(
          generator2,
          callback((_, _) => {
            task(
              generator3,
              callback((_, _) => { if (tick) succeed(Unit) else { tick = true; ack(Seq(id3)) } })
            )
            ack(Seq(id2))
          })
        )
        task(
          generator4,
          callback((_, _) => {
            task(
              generator5,
              callback((_, _) => { if (tick) succeed(Unit) else { tick = true; ack(Seq(id5)) } })
            )
            ack(Seq(id4))
          })
        )
        ack(Seq(id1))
      })
    )
    ready()
  }
}

class DummySim3(coordinator: Manager) extends DummyLookaheadSim("sim1", coordinator) {
  var count = 0
  var tick = false

  override def run(): Unit = {
    val id1 = UUID.randomUUID
    val id2 = UUID.randomUUID
    val id3 = UUID.randomUUID
    val id4 = UUID.randomUUID
    val id5 = UUID.randomUUID
    val id6 = UUID.randomUUID
    val generator1 = Task(
      "task1",
      Some(id1),
      Constant(2L),
      Constant(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator2 = Task(
      "task2",
      Some(id2),
      Constant(4L),
      Constant(0L),
      0L,
      Seq("r1"),
      (-1),
      Task.High
    )
    val generator3 = Task(
      "task3",
      Some(id3),
      Constant(3L),
      Constant(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator4 = Task(
      "task4",
      Some(id4),
      Constant(2L),
      Constant(0L),
      0L,
      Seq("r3"),
      (-1),
      Task.High
    )
    val generator5 = Task(
      "task5",
      Some(id5),
      Constant(4L),
      Constant(0L),
      0L,
      Seq("r3"),
      (-1),
      Task.High
    )
    val generator6 = Task(
      "task6",
      Some(id6),
      Constant(10L),
      Constant(0L),
      0L,
      Seq("r3"),
      (-1),
      Task.Low
    )

    lookahead =
      lookahead +> (id1, generator2) +> (id1, generator3) +> (id1, generator4) +> (id1, generator6)

    def function(s: collection.immutable.Map[java.util.UUID, Long]): Option[Long] = {
      val prerequisites = Set(id2, id3, id4) map (s.get(_))
      if (prerequisites.contains(None)) None
      else Some((prerequisites map (_.get)).max)
    }
    lookahead = lookahead + (function _, generator5)

    def task5(): Unit = {
      task(
        generator5,
        callback((_, _) => { if (tick) succeed(Unit) else { tick = true; ack(Seq(id5)) } })
      )
    }

    val task1 = task(
      generator1,
      callback((_, _) => {
        task(generator2, callback((_, _) => { if (count == 2) task5(); count += 1; ack(Seq(id2)) }))
        task(generator3, callback((_, _) => { if (count == 2) task5(); count += 1; ack(Seq(id3)) }))
        task(generator4, callback((_, _) => { if (count == 2) task5(); count += 1; ack(Seq(id4)) }))
        task(
          generator6,
          callback((_, _) => { if (tick) succeed(Unit) else { tick = true; ack(Seq(id6)) } })
        )
        ack(Seq(id1))
      })
    )

    ready()
  }
}

class FlowDummySim(coordinator: Manager)
    extends FlowLookahead("sim1", coordinator, FlowDummySim.flow)

object FlowDummySim {

  // Define tasks
  val task1: FlowTask = new FlowTask(
    Task("task1", 2L) withResources (Seq(
          "r3"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task2: FlowTask = new FlowTask(
    Task("task2", 2L) withResources (Seq(
          "r1"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task3: FlowTask = new FlowTask(
    Task("task3", 4L) withResources (Seq(
          "r2"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task4: FlowTask = new FlowTask(
    Task("task4", 4L) withResources (Seq(
          "r2"
        )) withPriority (Task.Low) withID UUID.randomUUID
  )
  val flow: Flow = task1 > ((task2 > task3) + task4)

}

class FlowDummySim2(coordinator: Manager)
    extends FlowLookahead("sim1", coordinator, FlowDummySim2.flow)

object FlowDummySim2 {

  // Define tasks
  val task1: FlowTask = new FlowTask(
    Task("task1", 2L) withResources (Seq(
          "r2"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task2: FlowTask = new FlowTask(
    Task("task2", 4L) withResources (Seq(
          "r1"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task3: FlowTask = new FlowTask(
    Task("task3", 3L) withResources (Seq(
          "r2"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task4: FlowTask = new FlowTask(
    Task("task4", 2L) withResources (Seq(
          "r3"
        )) withPriority (Task.Low) withID UUID.randomUUID
  )

  val task5: FlowTask = new FlowTask(
    Task("task5", 3L) withResources (Seq(
          "r2"
        )) withPriority (Task.Low) withID UUID.randomUUID
  )
  val flow: Flow = task1 > ((task2 > task3) + (task4 > task5))

}

class FlowDummySim3(coordinator: Manager)
    extends FlowLookahead("sim1", coordinator, FlowDummySim3.flow)

object FlowDummySim3 {

  // Define tasks
  val task1: FlowTask = new FlowTask(
    Task("task1", 2L) withResources (Seq(
          "r2"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task2: FlowTask = new FlowTask(
    Task("task2", 4L) withResources (Seq(
          "r1"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task3: FlowTask = new FlowTask(
    Task("task3", 3L) withResources (Seq(
          "r2"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task4: FlowTask = new FlowTask(
    Task("task4", 2L) withResources (Seq(
          "r3"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task5: FlowTask = new FlowTask(
    Task("task5", 4L) withResources (Seq(
          "r3"
        )) withPriority (Task.High) withID UUID.randomUUID
  )

  val task6: FlowTask = new FlowTask(
    Task("task6", 10L) withResources (Seq(
          "r3"
        )) withPriority (Task.Low) withID UUID.randomUUID
  )
  val flow: Flow = task1 > (((task2 + task3 + task4) > task5) + task6)

}
