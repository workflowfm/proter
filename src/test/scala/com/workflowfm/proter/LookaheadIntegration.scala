package com.workflowfm.proter

import scala.collection.mutable.Map
import scala.concurrent._
import scala.concurrent.duration._
import scala.reflect._

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import akka.pattern.ask
import akka.testkit._
import akka.util.Timeout
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

import uk.ac.ed.inf.ppapapan.subakka.Subscriber

import com.workflowfm.proter._
import com.workflowfm.proter.events.PromiseHandler
import com.workflowfm.proter.events.{ ShutdownHandler }
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._

class LookaheadIntegrationTests
    extends TestKit(ActorSystem("LookaheadTest"))
    with LookaheadTester
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with OptionValues {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Lookahead systems" should {

    "execute a basic scenario without blocking high priority tasks [ 1 > ( (2>3) + 4 ) ]" in {
      val testMetrics = singleSimulationTest(DummySim)
      testMetrics.size should be(4)
      testMetrics.get("task1 (sim1)").value.value should be(2)
      testMetrics.get("task2 (sim1)").value.value should be(4)
      testMetrics.get("task3 (sim1)").value.value should be(8)
      testMetrics.get("task4 (sim1)").value.value should be(12)
    }

    "execute simulation where in-progress tasks must be considered [ 1 > ( (2>3) + (4>5) ) ]" in {
      val testMetrics = singleSimulationTest(DummySim2)
      testMetrics.size should be(5)
      testMetrics.get("task1 (sim2)").value.value should be(2)
      testMetrics.get("task2 (sim2)").value.value should be(6)
      testMetrics.get("task3 (sim2)").value.value should be(9)
      testMetrics.get("task4 (sim2)").value.value should be(4)
      testMetrics.get("task5 (sim2)").value.value should be(12)
    }

    "execute simulation where a many-to-one future task relationship is involved [ 1 > ( ( (2+3+4)>5 ) + 6 ) ]" in {
      val testMetrics = singleSimulationTest(DummySim3)
      testMetrics.size should be(6)
      testMetrics.get("task1 (sim3)").value.value should be(2)
      testMetrics.get("task2 (sim3)").value.value should be(6)
      testMetrics.get("task3 (sim3)").value.value should be(5)
      testMetrics.get("task4 (sim3)").value.value should be(4)
      testMetrics.get("task5 (sim3)").value.value should be(10)
      testMetrics.get("task6 (sim3)").value.value should be(20)
    }
  }

  "Lookahead flows" should {

    "execute a basic scenario without blocking high priority tasks [ 1 > ( (2>3) + 4 ) ]" in {
      val testMetrics = singleSimulationTest(FlowDummySim)
      testMetrics.size should be(4)
      testMetrics.get("task1 (sim4)").value.value should be(2)
      testMetrics.get("task2 (sim4)").value.value should be(4)
      testMetrics.get("task3 (sim4)").value.value should be(8)
      testMetrics.get("task4 (sim4)").value.value should be(12)
    }

    "execute simulation where in-progress tasks must be considered [ 1 > ( (2>3) + (4>5) ) ]" in {
      val testMetrics = singleSimulationTest(FlowDummySim2)
      testMetrics.size should be(5)
      testMetrics.get("task1 (sim5)").value.value should be(2)
      testMetrics.get("task2 (sim5)").value.value should be(6)
      testMetrics.get("task3 (sim5)").value.value should be(9)
      testMetrics.get("task4 (sim5)").value.value should be(4)
      testMetrics.get("task5 (sim5)").value.value should be(12)
    }

    "execute simulation where a many-to-one future task relationship is involved [ 1 > ( ( (2+3+4)>5 ) + 6 ) ]" in {
      val testMetrics = singleSimulationTest(FlowDummySim3)
      testMetrics.size should be(6)
      testMetrics.get("task1 (sim6)").value.value should be(2)
      testMetrics.get("task2 (sim6)").value.value should be(6)
      testMetrics.get("task3 (sim6)").value.value should be(5)
      testMetrics.get("task4 (sim6)").value.value should be(4)
      testMetrics.get("task5 (sim6)").value.value should be(10)
      testMetrics.get("task6 (sim6)").value.value should be(20)
    }
  }

}

trait LookaheadTester {
  self: TestKit =>

  implicit val executionContext: ExecutionContext = ExecutionContext.global
  implicit val timeout: Timeout = Timeout(2.seconds)

  def singleSimulationTest(sim: TestObject): Map[String, Option[Long]] = {
    val coordinator = system.actorOf(Coordinator.props(new LookaheadScheduler()))
    //val shutdownActor = Subscriber.actor(new ShutdownHandler()) //not needed
    //val smh = new SimMetricsHandler
    val smh = new PromiseHandler(new SimMetricsHandler)

    Await.result(smh.subAndForgetTo(coordinator), 1.second)
    //Await.result(shutdownActor ? Subscriber.SubAndForgetTo(coordinator), 3.seconds) //not needed

    coordinator ! Coordinator.AddResources(sim.resources)

    coordinator ! Coordinator.AddSim(0L, system.actorOf(sim.props(sim.name, coordinator), sim.name))
    coordinator ! Coordinator.Start
    val metrics = Await.result(smh.future, 3.seconds)
    metrics.taskMap map { x => x._2.fullName -> x._2.finished }
  }
}

class DummySim(name: String, coordinator: ActorRef)(implicit executionContext: ExecutionContext)
    extends AsyncSimulation(name, coordinator)
    with LookingAhead {
  val promise: Promise[Any] = Promise[Any]()
  var tick = false

  override def run(): Future[Any] = {
    val id1 = java.util.UUID.randomUUID
    val id2 = java.util.UUID.randomUUID
    val id3 = java.util.UUID.randomUUID
    val id4 = java.util.UUID.randomUUID
    val generator1 = TaskGenerator(
      "task1",
      id1,
      name,
      ConstantGenerator(2L),
      ConstantGenerator(0L),
      0L,
      Seq("r3"),
      (-1),
      Task.High
    )
    val generator2 = TaskGenerator(
      "task2",
      id2,
      name,
      ConstantGenerator(2L),
      ConstantGenerator(0L),
      0L,
      Seq("r1"),
      (-1),
      Task.High
    )
    val generator3 = TaskGenerator(
      "task3",
      id3,
      name,
      ConstantGenerator(4L),
      ConstantGenerator(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator4 = TaskGenerator(
      "task4",
      id4,
      name,
      ConstantGenerator(4L),
      ConstantGenerator(0L),
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
      { (_, _) =>
        task(
          generator2,
          { (_, _) =>
            task(
              generator3,
              { (_, _) => if (tick) promise.success(Unit) else { tick = true; ack(Seq(id3)) } }
            ); ack(Seq(id2))
          }
        );
        task(
          generator4,
          { (_, _) => if (tick) promise.success(Unit) else { tick = true; ack(Seq(id4)) } }
        );
        ack(Seq(id1))
      }
    )

    ready()
    promise.future
  }
}

class DummySim2(name: String, coordinator: ActorRef)(implicit executionContext: ExecutionContext)
    extends AsyncSimulation(name, coordinator)
    with LookingAhead {
  val promise: Promise[Any] = Promise[Any]()
  var tick = false

  override def run(): Future[Any] = {
    val id1 = java.util.UUID.randomUUID
    val id2 = java.util.UUID.randomUUID
    val id3 = java.util.UUID.randomUUID
    val id4 = java.util.UUID.randomUUID
    val id5 = java.util.UUID.randomUUID
    val generator1 = TaskGenerator(
      "task1",
      id1,
      name,
      ConstantGenerator(2L),
      ConstantGenerator(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator2 = TaskGenerator(
      "task2",
      id2,
      name,
      ConstantGenerator(4L),
      ConstantGenerator(0L),
      0L,
      Seq("r1"),
      (-1),
      Task.High
    )
    val generator3 = TaskGenerator(
      "task3",
      id3,
      name,
      ConstantGenerator(3L),
      ConstantGenerator(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator4 = TaskGenerator(
      "task4",
      id4,
      name,
      ConstantGenerator(2L),
      ConstantGenerator(0L),
      0L,
      Seq("r3"),
      (-1),
      Task.Low
    )
    val generator5 = TaskGenerator(
      "task5",
      id5,
      name,
      ConstantGenerator(3L),
      ConstantGenerator(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.Low
    )

    lookahead =
      lookahead +> (id1, generator2) +> (id1, generator4) +> (id2, generator3) +> (id4, generator5)

    val task1 = task(
      generator1,
      { (_, _) =>
        task(
          generator2,
          { (_, _) =>
            task(
              generator3,
              { (_, _) => if (tick) promise.success(Unit) else { tick = true; ack(Seq(id3)) } }
            ); ack(Seq(id2))
          }
        );
        task(
          generator4,
          { (_, _) =>
            task(
              generator5,
              { (_, _) => if (tick) promise.success(Unit) else { tick = true; ack(Seq(id5)) } }
            ); ack(Seq(id4))
          }
        );
        ack(Seq(id1))
      }
    )

    ready()
    promise.future
  }
}

class DummySim3(name: String, coordinator: ActorRef)(implicit executionContext: ExecutionContext)
    extends AsyncSimulation(name, coordinator)
    with LookingAhead {
  val promise: Promise[Any] = Promise[Any]()
  var count = 0
  var tick = false

  override def run(): Future[Any] = {
    val id1 = java.util.UUID.randomUUID
    val id2 = java.util.UUID.randomUUID
    val id3 = java.util.UUID.randomUUID
    val id4 = java.util.UUID.randomUUID
    val id5 = java.util.UUID.randomUUID
    val id6 = java.util.UUID.randomUUID
    val generator1 = TaskGenerator(
      "task1",
      id1,
      name,
      ConstantGenerator(2L),
      ConstantGenerator(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator2 = TaskGenerator(
      "task2",
      id2,
      name,
      ConstantGenerator(4L),
      ConstantGenerator(0L),
      0L,
      Seq("r1"),
      (-1),
      Task.High
    )
    val generator3 = TaskGenerator(
      "task3",
      id3,
      name,
      ConstantGenerator(3L),
      ConstantGenerator(0L),
      0L,
      Seq("r2"),
      (-1),
      Task.High
    )
    val generator4 = TaskGenerator(
      "task4",
      id4,
      name,
      ConstantGenerator(2L),
      ConstantGenerator(0L),
      0L,
      Seq("r3"),
      (-1),
      Task.High
    )
    val generator5 = TaskGenerator(
      "task5",
      id5,
      name,
      ConstantGenerator(4L),
      ConstantGenerator(0L),
      0L,
      Seq("r3"),
      (-1),
      Task.High
    )
    val generator6 = TaskGenerator(
      "task6",
      id6,
      name,
      ConstantGenerator(10L),
      ConstantGenerator(0L),
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
        { (_, _) => if (tick) promise.success(Unit) else { tick = true; ack(Seq(id5)) } }
      )
    }

    val task1 = task(
      generator1,
      { (_, _) =>
        task(generator2, { (_, _) => if (count == 2) task5(); count += 1; ack(Seq(id2)) });
        task(generator3, { (_, _) => if (count == 2) task5(); count += 1; ack(Seq(id3)) });
        task(generator4, { (_, _) => if (count == 2) task5(); count += 1; ack(Seq(id4)) });
        task(
          generator6,
          { (_, _) => if (tick) promise.success(Unit) else { tick = true; ack(Seq(id6)) } }
        );
        ack(Seq(id1))
      }
    )

    ready()
    promise.future
  }
}

sealed trait TestObject {
  val name: String
  def props(name: String, coordinator: ActorRef)(implicit executionContext: ExecutionContext): Props
  def resources: Seq[TaskResource]
}

case object DummySim extends TestObject {
  val name = "sim1"

  override def props(name: String, coordinator: ActorRef)(
      implicit executionContext: ExecutionContext
  ): Props = { Props(new DummySim(name, coordinator)) }
  override def resources: Seq[TaskResource] = Seq("r1", "r2", "r3") map (x => new TaskResource(x, 0))
}

case object DummySim2 extends TestObject {
  val name = "sim2"

  override def props(name: String, coordinator: ActorRef)(
      implicit executionContext: ExecutionContext
  ): Props = { Props(new DummySim2(name, coordinator)) }
  override def resources: Seq[TaskResource] = Seq("r1", "r2", "r3") map (x => new TaskResource(x, 0))
}

case object DummySim3 extends TestObject {
  val name = "sim3"

  override def props(name: String, coordinator: ActorRef)(
      implicit executionContext: ExecutionContext
  ): Props = { Props(new DummySim3(name, coordinator)) }
  override def resources: Seq[TaskResource] = Seq("r1", "r2", "r3") map (x => new TaskResource(x, 0))
}

case object FlowDummySim extends TestObject {
  val name = "sim4"

  // Define tasks
  val task1: FlowTask = FlowTask(
    TaskGenerator("task1", name, ConstantGenerator(2L), ConstantGenerator(0L)) withResources (Seq(
          "r3"
        )) withPriority (Task.High)
  )

  val task2: FlowTask = FlowTask(
    TaskGenerator("task2", name, ConstantGenerator(2L), ConstantGenerator(0L)) withResources (Seq(
          "r1"
        )) withPriority (Task.High)
  )

  val task3: FlowTask = FlowTask(
    TaskGenerator("task3", name, ConstantGenerator(4L), ConstantGenerator(0L)) withResources (Seq(
          "r2"
        )) withPriority (Task.High)
  )

  val task4: FlowTask = FlowTask(
    TaskGenerator("task4", name, ConstantGenerator(4L), ConstantGenerator(0L)) withResources (Seq(
          "r2"
        )) withPriority (Task.Low)
  )
  val flow: Then = task1 > ((task2 > task3) + task4)

  override def props(name: String, coordinator: ActorRef)(
      implicit executionContext: ExecutionContext
  ): Props = { FlowLookaheadActor.props(name, coordinator, flow) }
  override def resources: Seq[TaskResource] = Seq("r1", "r2", "r3") map (x => new TaskResource(x, 0))
}

case object FlowDummySim2 extends TestObject {
  val name = "sim5"

  // Define tasks
  val task1: FlowTask = FlowTask(
    TaskGenerator("task1", name, ConstantGenerator(2L), ConstantGenerator(0L)) withResources (Seq(
          "r2"
        )) withPriority (Task.High)
  )

  val task2: FlowTask = FlowTask(
    TaskGenerator("task2", name, ConstantGenerator(4L), ConstantGenerator(0L)) withResources (Seq(
          "r1"
        )) withPriority (Task.High)
  )

  val task3: FlowTask = FlowTask(
    TaskGenerator("task3", name, ConstantGenerator(3L), ConstantGenerator(0L)) withResources (Seq(
          "r2"
        )) withPriority (Task.High)
  )

  val task4: FlowTask = FlowTask(
    TaskGenerator("task4", name, ConstantGenerator(2L), ConstantGenerator(0L)) withResources (Seq(
          "r3"
        )) withPriority (Task.Low)
  )

  val task5: FlowTask = FlowTask(
    TaskGenerator("task5", name, ConstantGenerator(3L), ConstantGenerator(0L)) withResources (Seq(
          "r2"
        )) withPriority (Task.Low)
  )
  val flow: Then = task1 > ((task2 > task3) + (task4 > task5))

  override def props(name: String, coordinator: ActorRef)(
      implicit executionContext: ExecutionContext
  ): Props = { FlowLookaheadActor.props(name, coordinator, flow) }
  override def resources: Seq[TaskResource] = Seq("r1", "r2", "r3") map (x => new TaskResource(x, 0))
}

case object FlowDummySim3 extends TestObject {
  val name = "sim6"

  // Define tasks
  val task1: FlowTask = FlowTask(
    TaskGenerator("task1", name, ConstantGenerator(2L), ConstantGenerator(0L)) withResources (Seq(
          "r2"
        )) withPriority (Task.High)
  )

  val task2: FlowTask = FlowTask(
    TaskGenerator("task2", name, ConstantGenerator(4L), ConstantGenerator(0L)) withResources (Seq(
          "r1"
        )) withPriority (Task.High)
  )

  val task3: FlowTask = FlowTask(
    TaskGenerator("task3", name, ConstantGenerator(3L), ConstantGenerator(0L)) withResources (Seq(
          "r2"
        )) withPriority (Task.High)
  )

  val task4: FlowTask = FlowTask(
    TaskGenerator("task4", name, ConstantGenerator(2L), ConstantGenerator(0L)) withResources (Seq(
          "r3"
        )) withPriority (Task.High)
  )

  val task5: FlowTask = FlowTask(
    TaskGenerator("task5", name, ConstantGenerator(4L), ConstantGenerator(0L)) withResources (Seq(
          "r3"
        )) withPriority (Task.High)
  )

  val task6: FlowTask = FlowTask(
    TaskGenerator("task6", name, ConstantGenerator(10L), ConstantGenerator(0L)) withResources (Seq(
          "r3"
        )) withPriority (Task.Low)
  )
  val flow: Then = task1 > (((task2 + task3 + task4) > task5) + task6)

  override def props(name: String, coordinator: ActorRef)(
      implicit executionContext: ExecutionContext
  ): Props = { FlowLookaheadActor.props(name, coordinator, flow) }
  override def resources: Seq[TaskResource] = Seq("r1", "r2", "r3") map (x => new TaskResource(x, 0))
}
