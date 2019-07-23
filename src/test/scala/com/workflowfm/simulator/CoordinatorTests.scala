package com.workflowfm.simulator

import akka.actor.ActorSystem
import akka.testkit.{ ImplicitSender, TestActors, TestKit }
import com.workflowfm.simulator.events.{ PrintEventHandler, Publisher }
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.concurrent._
import scala.concurrent.Await
import scala.concurrent.duration._
import com.workflowfm.simulator.metrics._
import com.workflowfm.simulator.events.{ MockObserver, CounterHandler }


@RunWith(classOf[JUnitRunner])
class CoordinatorTests extends TestKit(ActorSystem("CoordinatorTests")) with WordSpecLike with Matchers with BeforeAndAfterAll with ImplicitSender {
  implicit val executionContext = ExecutionContext.global //system.dispatchers.lookup("akka.my-dispatcher")  
  implicit val timeout:FiniteDuration = 10.seconds

  override def afterAll:Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "The Coordinator" must {
 		
    val handler = SimMetricsOutputs(new SimMetricsPrinter())
  
            //expectNoMessage(200.millis)
    "execute a simple task" in {   
      val resA = new TaskResource("A",1)
      val resB = new TaskResource("B",1)

      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))
      val s1 = system.actorOf(TaskSimulatorActor.props(
        "S", coordinator, Seq("A","B"), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      
      coordinator ! Coordinator.AddResources(Seq(resA,resB))
      coordinator ! Coordinator.AddSim(0L,s1)
      coordinator ! Coordinator.Start
      
      val done = expectMsgType[Coordinator.Done](20.seconds)
      done.time should be (2L)
    }
    
    "execute two independent tasks in parallel" in {   
      val resA = new TaskResource("A",1)
      val resB = new TaskResource("B",1)

      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))
      val s1 = system.actorOf(TaskSimulatorActor.props(
        "S1", coordinator, Seq("A"), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      val s2 = system.actorOf(TaskSimulatorActor.props(
        "S2", coordinator, Seq("B"), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      
      coordinator ! Coordinator.AddResources(Seq(resA,resB))
      coordinator ! Coordinator.AddSim(0L,s1)
      coordinator ! Coordinator.AddSim(0L,s2)
      coordinator ! Coordinator.Start
      
      val done = expectMsgType[Coordinator.Done](20.seconds)
      done.time should be (2L)
    }

    "queue two tasks with the same resource" in {   
      val resA = new TaskResource("A",1)

      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))      
      val s1 = system.actorOf(TaskSimulatorActor.props(
        "S1", coordinator, Seq("A"), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      val s2 = system.actorOf(TaskSimulatorActor.props(
        "S2", coordinator, Seq("A"), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      
      coordinator ! Coordinator.AddResources(Seq(resA))
      coordinator ! Coordinator.AddSim(0L,s1)
      coordinator ! Coordinator.AddSim(0L,s2)
      coordinator ! Coordinator.Start
      
      val done = expectMsgType[Coordinator.Done](20.seconds)
      done.time should be (4L)
    }
    
    "measure delays and idling appropriately" in {   
      val resA = new TaskResource("A",1)
      val resB = new TaskResource("B",1)
      
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))      
      val s1 = system.actorOf(TaskSimulatorActor.props(
        "SI1", coordinator, Seq("A"), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      val s2 = system.actorOf(TaskSimulatorActor.props(
        "SI2", coordinator, Seq("A","B"), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Medium
      ))
      
      coordinator ! Coordinator.AddResources(Seq(resA,resB))
      coordinator ! Coordinator.AddSim(0L,s1)
      coordinator ! Coordinator.AddSim(0L,s2)
      coordinator ! Coordinator.Start
      
      val done = expectMsgType[Coordinator.Done](20.seconds)
      new SimMetricsPrinter().apply(done.time, done.metrics)
      done.time should be (4L)
      val metricB = done.metrics.resourceMetrics.find { x => x.name.equals("B") } 
      metricB should not be empty 
      //metricB map { x => x.idleTime should be (2L) }
      val metricS2T = done.metrics.taskMetrics.find { x => x.fullName.equals("SI2Task (SI2)") } 
      metricS2T should not be empty
      metricS2T map { x => x.delay should be (2L) }
    }
    /*
    "measure intermediate delays and idling appropriately" in {   
      val resA = new TaskResource("A",1)
      val resB = new TaskResource("B",1)

      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))      
      val s1 = new TaskSimulation("S1", coordinator, Seq("A"), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest)
      val s2 = new TaskSimulation("S2", coordinator, Seq("B"), new ConstantGenerator(3), new ConstantGenerator(2), -1, Task.Highest)
      val s3 = new TaskSimulation("S3", coordinator, Seq("A","B"), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Medium)
      
      coordinator ! Coordinator.AddResources(Seq(resA,resB))
      coordinator ! Coordinator.AddSim(1,s1)
      coordinator ! Coordinator.AddSim(1,s2)
      coordinator ! Coordinator.AddSim(2,s3)
      coordinator ! Coordinator.Start
      
      val done = expectMsgType[Coordinator.Done](20.seconds)
      
      done.time should be (6L)
      val metricA = done.metrics.resourceMetrics.find { x => x.name.equals("A") } 
      metricA should not be empty 
      metricA map { x => x.idleTime should be (1L) }
      val metricS3T = done.metrics.taskMetrics.find { x => x.fullName.equals("S3Task(S3)") } 
      metricS3T should not be empty
      metricS3T map { x => x.delay should be (2L) }
    }
     */  
    "run a task with no resources" in {
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))
      val s1 = system.actorOf(TaskSimulatorActor.props(
        "S1", coordinator, Seq(), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      
      coordinator ! Coordinator.AddSim(0L,s1)
      coordinator ! Coordinator.Start
      
      val done = expectMsgType[Coordinator.Done](20.seconds)
      
      done.time should be (2L)
      done.metrics.resourceMetrics.isEmpty should be (true)
      done.metrics.simulationMetrics.size should be (1)
      done.metrics.taskMetrics.size should be (1)
    }
    
    "run multiple tasks with no resources" in {
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))      
      val s1 = system.actorOf(TaskSimulatorActor.props(
        "S1", coordinator, Seq(), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      val s2 = system.actorOf(TaskSimulatorActor.props(
        "S2", coordinator, Seq(), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      
      coordinator ! Coordinator.AddSim(0L,s1)
      coordinator ! Coordinator.AddSim(0L,s2)
      coordinator ! Coordinator.Start
      
      val done = expectMsgType[Coordinator.Done](20.seconds)
      
      done.time should be (2L)
      done.metrics.resourceMetrics.isEmpty should be (true)
      done.metrics.simulationMetrics.size should be (2)
      done.metrics.taskMetrics.size should be (2)
    }
    
    "publish the right number of events from 1 simulation" in {
      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))
      //coordinator ! Publisher.SubHandler(new PrintEventHandler(), None)

      //Thread.sleep(1000)
      val f1 = MockObserver.build(new CounterHandler())(system,coordinator)
     // val f2 = MockObserver.build(new CounterHandler())(system,coordinator)
     // val f3 = MockObserver.build(new CounterHandler())(system,coordinator)
     // val f4 = MockObserver.build(new CounterHandler())(system,coordinator)

      println("Here we go!")
      val s1 = system.actorOf(TaskSimulatorActor.props(
        "S1", coordinator, Seq(), new ConstantGenerator(2), new ConstantGenerator(2), -1, Task.Highest
      ))
      
      coordinator ! Coordinator.AddSim(0L,s1)
      coordinator ! Coordinator.Start

      // Start, AddSim, StartSim, AddTask, StartTask, EndTask, EndSim, Done
      Await.result(f1, 1.seconds) should be (8)
    // Await.result(f2, 1.seconds) should be (8)
     // Await.result(f3, 1.seconds) should be (8)
     // Await.result(f4, 1.seconds) should be (8)
    }
/*  }
  
  "The MetricsActor" must {
	
    val handler = SimMetricsOutputs(new SimMetricsPrinter())
   
            //expectNoMessage(200.millis)
    "work properly" in {   
      println ("*** MetricsActor results should appear here:")
      
      val resA = new TaskResource("A",1)

      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))      
      val s = new TaskSimulation("S", coordinator, Seq("A"), new ConstantGenerator(1), new ConstantGenerator(2), -1, Task.Highest)
      
      coordinator ! Coordinator.AddResources(Seq(resA))
      coordinator ! Coordinator.AddSim(1,s)
      
      val metricsActor = system.actorOf(SimMetricsActor.props(handler,Some(self)))
      metricsActor ! SimMetricsActor.Start(coordinator)
      
      expectMsgType[Coordinator.Done](20.seconds)
    }
    
    "start multiple simulations with one message" in {   
      println ("*** MetricsActor multiple results should appear here:")
      
      val resA = new TaskResource("A",1)

      val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))      
      val s1 = new TaskSimulation("S1", coordinator, Seq("A"), new ConstantGenerator(1), new ConstantGenerator(2), -1, Task.Highest)
      val s2 = new TaskSimulation("S2", coordinator, Seq("A"), new ConstantGenerator(1), new ConstantGenerator(2), -1, Task.Highest)
      
      coordinator ! Coordinator.AddResources(Seq(resA))
      
      val metricsActor = system.actorOf(SimMetricsActor.props(handler,Some(self)))
      metricsActor ! SimMetricsActor.StartSims(coordinator,Seq((1,s1),(1,s2)))
      
      expectMsgType[Coordinator.Done](20.seconds)
    }*/
  }
}

 
