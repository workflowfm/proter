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
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test")
      coordinator ! Coordinator.SimDone("Test", Success(Unit))
      expectNoMsg()
    }

    "interact correctly with a simulation with just a ping" in {
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test")
      coordinator ! Coordinator.Ping
      expectMsgType[Coordinator.Time].time should be(0L)
      coordinator ! Coordinator.SimDone("Test", Success(Unit))
    }

    "interact correctly with a simulation with one task" in {
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test")

      val id = UUID.randomUUID()
      val tg = TaskGenerator("T", id, "Test", ConstantGenerator(2L), ConstantGenerator(5L))
      val expected = new Task(id, "T", "Test", self, 0L, Seq(), 2L, 2L, 5L, -1, Task.Medium)
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
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test")

      // Add task T1 0..2
      val id1 = UUID.randomUUID()
      val tg1 = TaskGenerator("T1", id1, "Test", ConstantGenerator(2L), ConstantGenerator(5L))
      val expected1 = new Task(id1, "T1", "Test", self, 0L, Seq(), 2L, 2L, 5L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg1)))
      coordinator ! Coordinator.SimReady

      // T1 completes
      val Simulation.TaskCompleted(task1, time1) = expectMsgType[Simulation.TaskCompleted]
      time1 should be(2L)
      task1.compare(expected1) should be(0)

      // Add T2 2..5
      val id2 = UUID.randomUUID()
      val tg2 = TaskGenerator("T2", id2, "Test", ConstantGenerator(3L), ConstantGenerator(6L))
      val expected2 = new Task(id2, "T2", "Test", self, 2L, Seq(), 3L, 3L, 6L, -1, Task.Medium)
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
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.Start
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test")

      // T1 0..2
      val id1 = UUID.randomUUID()
      val tg1 = TaskGenerator("T1", id1, "Test", ConstantGenerator(2L), ConstantGenerator(5L))
      val expected1 = new Task(id1, "T1", "Test", self, 0L, Seq(), 2L, 2L, 5L, -1, Task.Medium)

      // T2 0..2
      val id2 = UUID.randomUUID()
      val tg2 = TaskGenerator("T2", id2, "Test", ConstantGenerator(2L), ConstantGenerator(6L))
      val expected2 = new Task(id2, "T2", "Test", self, 0L, Seq(), 2L, 2L, 6L, -1, Task.Medium)

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
      val expected3 = new Task(id3, "T3", "Test", self, 2L, Seq(), 3L, 3L, 6L, -1, Task.Medium)
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
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))

      // probe represents a 2nd simulation starting at 1
      val probe = TestProbe()

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.AddSim(1L, probe.ref)
      coordinator ! Coordinator.Start

      // Test1 starts
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test1")

      // T1a 0..2
      val id1a = UUID.randomUUID()
      val tg1a = TaskGenerator("T1a", id1a, "Test1", ConstantGenerator(2L), ConstantGenerator(5L))
      val expected1a = new Task(id1a, "T1a", "Test1", self, 0L, Seq(), 2L, 2L, 5L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg1a)))
      coordinator ! Coordinator.SimReady

      // Test2 starts
      probe.expectMsg(Simulation.Start)
      probe.reply(Coordinator.SimStarted("Test2"))

      // T2a 1..2
      val id2a = UUID.randomUUID()
      val tg2a = TaskGenerator("T2a", id2a, "Test2", ConstantGenerator(1L), ConstantGenerator(5L))
      val expected2a = new Task(id2a, "T2a", "Test2", self, 1L, Seq(), 1L, 1L, 5L, -1, Task.Medium)
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
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))

      // probe represents a 2nd simulation starting at 1
      val probe = TestProbe()

      coordinator ! Coordinator.AddSim(0L, self)
      coordinator ! Coordinator.AddSim(1L, probe.ref)
      coordinator ! Coordinator.Start

      // Test1 starts
      expectMsg(Simulation.Start)
      coordinator ! Coordinator.SimStarted("Test1")

      // T1a 0..10
      val id1a = UUID.randomUUID()
      val tg1a = TaskGenerator("T1a", id1a, "Test1", ConstantGenerator(10L), ConstantGenerator(5L))
      val expected1a =
        new Task(id1a, "T1a", "Test1", self, 0L, Seq(), 10L, 10L, 5L, -1, Task.Medium)
      coordinator ! Coordinator.AddTasks(Seq((tg1a)))
      coordinator ! Coordinator.SimReady

      // Test2 starts
      probe.expectMsg(Simulation.Start)
      probe.reply(Coordinator.SimStarted("Test2"))

      // T2a 1..2
      val id2a = UUID.randomUUID()
      val tg2a = TaskGenerator("T2a", id2a, "Test2", ConstantGenerator(1L), ConstantGenerator(5L))
      val expected2a = new Task(id2a, "T2a", "Test2", self, 1L, Seq(), 1L, 1L, 5L, -1, Task.Medium)
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
      val expected1b = new Task(id1b, "T1b", "Test1", self, 2L, Seq(), 1L, 1L, 5L, -1, Task.Medium)
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

    // "interact correctly with a simulation adding tasks in the future" in {
    //   val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))

    //   coordinator ! Coordinator.AddSim(0L, self)
    //   coordinator ! Coordinator.Start
      
    //   expectMsg(Simulation.Start)
    //   coordinator ! Coordinator.SimStarted("Test")

    //   val id = UUID.randomUUID()
    //   val tg = TaskGenerator("T", "Test", ConstantGenerator(2L), ConstantGenerator(5L))
    //   val id2 = UUID.randomUUID()
    //   val tg2 = TaskGenerator("T2", "Test", ConstantGenerator(2L), ConstantGenerator(5L))
    //   val id3 = UUID.randomUUID()
    //   val tg3 = TaskGenerator("T3", "Test", ConstantGenerator(2L), ConstantGenerator(5L))

    //   coordinator ! Coordinator.AddTaskInFuture(id, tg, Seq(), 0L, (_)=>true)
    //   coordinator ! Coordinator.AddTaskInFuture(id2, tg2, Seq(), 0L, (x:Seq[UUID])=>x.contains(id))
    //   coordinator ! Coordinator.AddTaskInFuture(id3, tg3, Seq(), 0L, (x:Seq[UUID])=>x.contains(id2))
    //   coordinator ! Coordinator.SimReady

    //   val Simulation.TaskCompleted(task, time) = expectMsgType[Simulation.TaskCompleted]
    //   coordinator ! Coordinator.AckTasks(Seq(task.id))
    //   time should be(2L)

    //   val Simulation.TaskCompleted(task2, time2) = expectMsgType[Simulation.TaskCompleted]
    //   coordinator ! Coordinator.AckTasks(Seq(task2.id))
    //   time2 should be(4L)

    //   val Simulation.TaskCompleted(task3, time3) = expectMsgType[Simulation.TaskCompleted]
    //   coordinator ! Coordinator.AckTasks(Seq(task3.id))
    //   time3 should be(6L)

    //   coordinator ! Coordinator.SimDone("Test", Success(Unit))
    // }

  }

  /* "The Coordinator" must {
   *
   * val handler = SimMetricsOutputs(new SimMetricsPrinter())
   *
   * //expectNoMessage(200.millis) "execute a simple task" in { val resA = new TaskResource("A",1)
   * val resB = new TaskResource("B",1)
   *
   * val coordinator = system.actorOf(Coordinator.props(DefaultScheduler)) val s1 =
   * system.actorOf(TaskSimulatorActor.props( "S", coordinator, Seq("A","B"), new
   * ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest ))
   *
   * coordinator ! Coordinator.AddResources(Seq(resA,resB)) coordinator ! Coordinator.AddSim(0L,s1)
   * coordinator ! Coordinator.Start
   *
   * val done = expectMsgType[Coordinator.Done](20.seconds) done.time should be (2L) }
   *
   * "execute two independent tasks in parallel" in { val resA = new TaskResource("A",1) val resB =
   * new TaskResource("B",1)
   *
   * val coordinator = system.actorOf(Coordinator.props(DefaultScheduler)) val s1 =
   * system.actorOf(TaskSimulatorActor.props( "S1", coordinator, Seq("A"), new ConstantGenerator(2),
   * new ConstantGenerator(2), -1, Task.Highest )) val s2 = system.actorOf(TaskSimulatorActor.props(
   * "S2", coordinator, Seq("B"), new ConstantGenerator(2), new ConstantGenerator(2), -1,
   * Task.Highest ))
   *
   * coordinator ! Coordinator.AddResources(Seq(resA,resB)) coordinator ! Coordinator.AddSim(0L,s1)
   * coordinator ! Coordinator.AddSim(0L,s2) coordinator ! Coordinator.Start
   *
   * val done = expectMsgType[Coordinator.Done](20.seconds) done.time should be (2L) }
   *
   * "queue two tasks with the same resource" in { val resA = new TaskResource("A",1)
   *
   * val coordinator = system.actorOf(Coordinator.props(DefaultScheduler)) val s1 =
   * system.actorOf(TaskSimulatorActor.props( "S1", coordinator, Seq("A"), new ConstantGenerator(2),
   * new ConstantGenerator(2), -1, Task.Highest )) val s2 = system.actorOf(TaskSimulatorActor.props(
   * "S2", coordinator, Seq("A"), new ConstantGenerator(2), new ConstantGenerator(2), -1,
   * Task.Highest ))
   *
   * coordinator ! Coordinator.AddResources(Seq(resA)) coordinator ! Coordinator.AddSim(0L,s1)
   * coordinator ! Coordinator.AddSim(0L,s2) coordinator ! Coordinator.Start
   *
   * val done = expectMsgType[Coordinator.Done](20.seconds) done.time should be (4L) }
   *
   * "measure delays and idling appropriately" in { val resA = new TaskResource("A",1) val resB =
   * new TaskResource("B",1)
   *
   * val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))
   *
   * implicit val timeout = Timeout(2.seconds) val mObserver =
   * system.actorOf(SimMetricsActor.props(new SimMetricsPrinter())) Await.result(mObserver ?
   * Observer.SubscribeTo(coordinator), 3.seconds)
   *
   * val s1 = system.actorOf(TaskSimulatorActor.props( "SI1", coordinator, Seq("A"), new
   * ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest )) val s2 =
   * system.actorOf(TaskSimulatorActor.props( "SI2", coordinator, Seq("A","B"), new
   * ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Medium ))
   *
   * coordinator ! Coordinator.AddResources(Seq(resA,resB)) coordinator ! Coordinator.AddSim(0L,s1)
   * coordinator ! Coordinator.AddSim(0L,s2) coordinator ! Coordinator.Start
   *
   * val done = expectMsgType[Coordinator.Done](20.seconds) //new
   * SimMetricsPrinter().apply(done.time, done.metrics) done.time should be (4L) val metricB =
   * done.metrics.resourceMetrics.find { x => x.name.equals("B") } metricB should not be empty
   * metricB map { x => x.idleTime should be (2L) } val metricS2T = done.metrics.taskMetrics.find {
   * x => x.fullName.equals("SI2Task (SI2)") } metricS2T should not be empty metricS2T map { x =>
   * x.delay should be (2L) } }
   *
   * "measure intermediate delays and idling appropriately" in { val resA = new TaskResource("A",1)
   * val resB = new TaskResource("B",1)
   *
   * val coordinator = system.actorOf(Coordinator.props(DefaultScheduler)) implicit val timeout =
   * Timeout(2.seconds) val mObserver = system.actorOf(SimMetricsActor.props(new
   * SimMetricsPrinter())) Await.result(mObserver ? Observer.SubscribeTo(coordinator), 3.seconds)
   *
   * val s1 = system.actorOf(TaskSimulatorActor.props( "Si1", coordinator, Seq("A"), new
   * ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest )) val s2 =
   * system.actorOf(TaskSimulatorActor.props( "Si2", coordinator, Seq("B"), new
   * ConstantGenerator(3), new ConstantGenerator(2), -1, Task.Highest )) val s3 =
   * system.actorOf(TaskSimulatorActor.props( "Si3", coordinator, Seq("A","B"), new
   * ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Medium ))
   *
   * coordinator ! Coordinator.AddResources(Seq(resA,resB)) coordinator ! Coordinator.AddSim(0L,s1)
   * coordinator ! Coordinator.AddSim(0L,s2) coordinator ! Coordinator.AddSim(1L,s3) coordinator !
   * Coordinator.Start
   *
   * val done = expectMsgType[Coordinator.Done](20.seconds)
   *
   * done.time should be (5L) val metricA = done.metrics.resourceMetrics.find { x =>
   * x.name.equals("A") } metricA should not be empty metricA map { x => x.idleTime should be (1L) }
   * val metricS3T = done.metrics.taskMetrics.find { x => x.fullName.equals("Si3Task (Si3)") }
   * metricS3T should not be empty metricS3T map { x => x.delay should be (2L) } }
   *
   * "run a task with no resources" in { val coordinator =
   * system.actorOf(Coordinator.props(DefaultScheduler)) val s1 =
   * system.actorOf(TaskSimulatorActor.props( "S1", coordinator, Seq(), new ConstantGenerator(2),
   * new ConstantGenerator(2), -1, Task.Highest ))
   *
   * coordinator ! Coordinator.AddSim(0L,s1) coordinator ! Coordinator.Start
   *
   * val done = expectMsgType[Coordinator.Done](20.seconds)
   *
   * done.time should be (2L) done.metrics.resourceMetrics.isEmpty should be (true)
   * done.metrics.simulationMetrics.size should be (1) done.metrics.taskMetrics.size should be (1) }
   *
   * "run multiple tasks with no resources" in { val coordinator =
   * system.actorOf(Coordinator.props(DefaultScheduler)) val s1 =
   * system.actorOf(TaskSimulatorActor.props( "S1", coordinator, Seq(), new ConstantGenerator(2),
   * new ConstantGenerator(2), -1, Task.Highest )) val s2 = system.actorOf(TaskSimulatorActor.props(
   * "S2", coordinator, Seq(), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
   * ))
   *
   * coordinator ! Coordinator.AddSim(0L,s1) coordinator ! Coordinator.AddSim(0L,s2) coordinator !
   * Coordinator.Start
   *
   * val done = expectMsgType[Coordinator.Done](20.seconds)
   *
   * done.time should be (2L) done.metrics.resourceMetrics.isEmpty should be (true)
   * done.metrics.simulationMetrics.size should be (2) done.metrics.taskMetrics.size should be (2) }
   *
   * "publish the right number of events from 1 simulation" in { val coordinator =
   * system.actorOf(Coordinator.props(DefaultScheduler)) //coordinator ! Publisher.SubHandler(new
   * PrintEventHandler(), None)
   *
   * //Thread.sleep(1000) val f1 = MockObserver.observer(new CounterHandler())(system,coordinator)
   * // val f2 = MockObserver.build(new CounterHandler())(system,coordinator) // val f3 =
   * MockObserver.build(new CounterHandler())(system,coordinator) // val f4 = MockObserver.build(new
   * CounterHandler())(system,coordinator)
   *
   * println("Here we go!") val s1 = system.actorOf(TaskSimulatorActor.props( "S1", coordinator,
   * Seq(), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest ))
   *
   * coordinator ! Coordinator.AddSim(0L,s1) coordinator ! Coordinator.Start
   *
   * // Start, AddSim, StartSim, AddTask, StartTask, EndTask, EndSim, Done Await.result(f1,
   * 1.seconds) should be (8) // Await.result(f2, 1.seconds) should be (8) // Await.result(f3,
   * 1.seconds) should be (8) // Await.result(f4, 1.seconds) should be (8) } } */
}
