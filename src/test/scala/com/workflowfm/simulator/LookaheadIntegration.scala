package com.workflowfm.simulator

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import akka.testkit._

import akka.actor.{ ActorSystem, Actor, ActorRef, Props }
import akka.pattern.ask
import scala.concurrent._
import scala.concurrent.duration._
import com.workflowfm.simulator.metrics._
import uk.ac.ed.inf.ppapapan.subakka.Subscriber
import com.workflowfm.simulator.events.{ ShutdownHandler }
import akka.util.Timeout
import com.workflowfm.simulator.flows._
import com.workflowfm.simulator._
import scala.collection.mutable.Map
import scala.reflect._

class LookaheadIntegrationTests extends LookaheadTester {
    "Lookahead systems" should {

        "execute a basic scenario without blocking high priority tasks [ 1 > ( (2>3) + 4 ) ]" in {
            val testMetrics = singleSimulationTest(DummySim)
            testMetrics.size should be (4)
            testMetrics.get("task1 (sim)").get.get should be (2)
            testMetrics.get("task2 (sim)").get.get should be (4)
            testMetrics.get("task3 (sim)").get.get should be (8)
            testMetrics.get("task4 (sim)").get.get should be (12)
        }

        "execute simulation where in-progress tasks must be considered [ 1 > ( (2>3) + (4>5) ) ]" in {
            val testMetrics = singleSimulationTest(DummySim2)
            testMetrics.size should be (5)
            testMetrics.get("task1 (sim)").get.get should be (2)
            testMetrics.get("task2 (sim)").get.get should be (6)
            testMetrics.get("task3 (sim)").get.get should be (9)
            testMetrics.get("task4 (sim)").get.get should be (4)
            testMetrics.get("task5 (sim)").get.get should be (12)
        }

        "execute simulation where a many-to-one future task relationship is involved [ 1 > ( ( (2+3+4)>5 ) + 6 ) ]" in {
            val testMetrics = singleSimulationTest(DummySim3)
            testMetrics.size should be (6)
            testMetrics.get("task1 (sim)").get.get should be (2)
            testMetrics.get("task2 (sim)").get.get should be (6)
            testMetrics.get("task3 (sim)").get.get should be (5)
            testMetrics.get("task4 (sim)").get.get should be (4)
            testMetrics.get("task5 (sim)").get.get should be (10)
            testMetrics.get("task6 (sim)").get.get should be (20)
        }
    }

}

class LookaheadTester
    extends TestKit(ActorSystem("LookaheadTest"))
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  def singleSimulationTest(sim: TestObject): Map[String, Option[Long]] = {
    val testMetrics = Map[String, Option[Long]]()
    implicit val system: ActorSystem = ActorSystem("FlowsMain")
    implicit val executionContext: ExecutionContext = ExecutionContext.global
    implicit val timeout = Timeout(2.seconds)
    val coordinator = system.actorOf(Coordinator.props(LookaheadScheduler))
    val shutdownActor = Subscriber.actor(new ShutdownHandler())
    val smh = new SimMetricsHandler

    Await.result(smh.subAndForgetTo(coordinator), 1.second)
    Await.result(shutdownActor ? Subscriber.SubAndForgetTo(coordinator), 3.seconds)

    coordinator ! Coordinator.AddResources(sim.resources)
    
    coordinator ! Coordinator.AddSim(0L, system.actorOf(sim.props("sim",coordinator), "sim") )
    coordinator ! Coordinator.Start
    Await.result(system.whenTerminated, 3.seconds)
    smh.metrics.taskMap map { x => testMetrics += (x._2.fullName -> x._2.finished) }
    testMetrics
  }
}

class DummySim(name: String, coordinator: ActorRef)
(implicit executionContext: ExecutionContext) 
extends AsyncSimulation(name,coordinator) with Lookahead {
    val promise = Promise[Any]()
    var tick = false
    
    override def run():Future[Any] = { 
        val id1 = java.util.UUID.randomUUID
        val id2 = java.util.UUID.randomUUID
        val id3 = java.util.UUID.randomUUID
        val id4 = java.util.UUID.randomUUID
        val generator1 = TaskGenerator("task1","sim",ConstantGenerator(2L),ConstantGenerator(0L),(-1),Task.High)
        val generator2 = TaskGenerator("task2","sim",ConstantGenerator(2L),ConstantGenerator(0L),(-1),Task.High)
        val generator3 = TaskGenerator("task3","sim",ConstantGenerator(4L),ConstantGenerator(0L),(-1),Task.High)
        val generator4 = TaskGenerator("task4","sim",ConstantGenerator(4L),ConstantGenerator(0L),(-1),Task.Low)
        val resources1 = Seq("r3")
        val resources2 = Seq("r1")
        val resources3 = Seq("r2")
        val resources4 = Seq("r2")
        
        lookahead.add1To1Lookahead(id1,id2,generator2,resources2)
        lookahead.add1To1Lookahead(id1,id4,generator4,resources4)
        lookahead.add1To1Lookahead(id2,id3,generator3,resources3)
        coordinator ! Coordinator.SetSchedulerLookaheadObject(lookahead)

        //Equal to flow: task1 > ( (task2 > task3) + task4 )
        // i.e. the sequence task2>task3 happens in parallel to task4
        val task1 = task(id1,generator1, 
            {(_,_)=> task(id2,generator2,
                {(_,_)=> task(id3,generator3, 
                    {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id3))}
                    }, 
                resources3); ack(Seq(id2)) 
            }, 
            resources2
            ); 
            task(id4,generator4, 
                {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id4))}
                }, 
            resources4);
            ack(Seq(id1)) 
        }, 
        resources1)

        ready()
        promise.future
    } 
}

class DummySim2(name: String, coordinator: ActorRef)
(implicit executionContext: ExecutionContext) 
extends AsyncSimulation(name,coordinator) with Lookahead {
    val promise = Promise[Any]()
    var tick = false
    override def run():Future[Any] = { 
        val id1 = java.util.UUID.randomUUID
        val id2 = java.util.UUID.randomUUID
        val id3 = java.util.UUID.randomUUID
        val id4 = java.util.UUID.randomUUID
        val id5 = java.util.UUID.randomUUID
        val generator1 = TaskGenerator("task1","sim",ConstantGenerator(2L),ConstantGenerator(0L),(-1),Task.High)
        val generator2 = TaskGenerator("task2","sim",ConstantGenerator(4L),ConstantGenerator(0L),(-1),Task.High)
        val generator3 = TaskGenerator("task3","sim",ConstantGenerator(3L),ConstantGenerator(0L),(-1),Task.High)
        val generator4 = TaskGenerator("task4","sim",ConstantGenerator(2L),ConstantGenerator(0L),(-1),Task.Low)
        val generator5 = TaskGenerator("task5","sim",ConstantGenerator(3L),ConstantGenerator(0L),(-1),Task.Low)
        val resources1 = Seq("r2")
        val resources2 = Seq("r1")
        val resources3 = Seq("r2")
        val resources4 = Seq("r3")
        val resources5 = Seq("r2")
        
        lookahead.add1To1Lookahead(id1,id2,generator2,resources2)
        lookahead.add1To1Lookahead(id1,id4,generator4,resources4)
        lookahead.add1To1Lookahead(id2,id3,generator3,resources3)
        lookahead.add1To1Lookahead(id4,id5,generator5,resources5)
        coordinator ! Coordinator.SetSchedulerLookaheadObject(lookahead)

        val task1 = task(id1,generator1, 
            {(_,_)=> task(id2,generator2,
                {(_,_)=> task(id3,generator3, 
                    {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id3))}
                    }, 
                resources3); ack(Seq(id2)) 
            }, 
            resources2
            ); 
            task(id4,generator4, 
                {(_,_)=> task(id5,generator5, 
                    {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id5))}
                    }, 
                resources5); ack(Seq(id4)) 
            }, 
            resources4);
            ack(Seq(id1)) 
        }, 
        resources1)

        ready()
        promise.future
    } 
}

class DummySim3(name: String, coordinator: ActorRef)
(implicit executionContext: ExecutionContext) 
extends AsyncSimulation(name,coordinator) with Lookahead {
    val promise = Promise[Any]()
    var count = 0
    var tick = false
    override def run():Future[Any] = { 
        val id1 = java.util.UUID.randomUUID
        val id2 = java.util.UUID.randomUUID
        val id3 = java.util.UUID.randomUUID
        val id4 = java.util.UUID.randomUUID
        val id5 = java.util.UUID.randomUUID
        val id6 = java.util.UUID.randomUUID
        val generator1 = TaskGenerator("task1","sim",ConstantGenerator(2L),ConstantGenerator(0L),(-1),Task.High)
        val generator2 = TaskGenerator("task2","sim",ConstantGenerator(4L),ConstantGenerator(0L),(-1),Task.High)
        val generator3 = TaskGenerator("task3","sim",ConstantGenerator(3L),ConstantGenerator(0L),(-1),Task.High)
        val generator4 = TaskGenerator("task4","sim",ConstantGenerator(2L),ConstantGenerator(0L),(-1),Task.High)
        val generator5 = TaskGenerator("task5","sim",ConstantGenerator(4L),ConstantGenerator(0L),(-1),Task.High)
        val generator6 = TaskGenerator("task6","sim",ConstantGenerator(10L),ConstantGenerator(0L),(-1),Task.Low)
        val resources1 = Seq("r2")
        val resources2 = Seq("r1")
        val resources3 = Seq("r2")
        val resources4 = Seq("r3")
        val resources5 = Seq("r3")
        val resources6 = Seq("r3")
        
        lookahead.add1To1Lookahead(id1,id2,generator2,resources2)
        lookahead.add1To1Lookahead(id1,id3,generator3,resources3)
        lookahead.add1To1Lookahead(id1,id4,generator4,resources4)
        lookahead.add1To1Lookahead(id1,id6,generator6,resources6)

        def function(s: Seq[(java.util.UUID,Long)]): Long = {
            val prerequisites= Set(id2,id3,id4) forall (x=>s.exists {case(id,l)=>id==x} )
            if (prerequisites) ( s filter (x=> Set(id2,id3,id4) contains x._1) map (_._2) ).max
            else -1
        }
        lookahead.addManyTo1Lookahead(function, id5, generator5, resources5)

        coordinator ! Coordinator.SetSchedulerLookaheadObject(lookahead)

        def task5(){
            task(id5,generator5,
                {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id5))}},
                resources5
            )
        }

        val task1 = task(id1,generator1, 
            {(_,_)=> task(id2,generator2,
                {(_,_)=> if (count==2) task5(); count+=1; ack(Seq(id2))},
                resources2
            ); 
            task(id3,generator3, 
                {(_,_)=> if (count==2) task5(); count+=1; ack(Seq(id3))}, 
                resources3
            );
            task(id4,generator4, 
                {(_,_)=> if (count==2) task5(); count+=1; ack(Seq(id4))},
                resources4
            );
            task(id6,generator6, 
                {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id6))}},
                resources6
            );
            ack(Seq(id1)) 
        }, 
        resources1)

        ready()
        promise.future
    } 
}

sealed trait TestObject {
    def props(name: String, coordinator: ActorRef)(implicit executionContext: ExecutionContext): Props
    def resources: Seq[TaskResource] 
}

case object DummySim extends TestObject {
    override def props(name: String, coordinator: ActorRef)
        (implicit executionContext: ExecutionContext): Props = {Props(new DummySim(name, coordinator))}
    override def resources = Seq("r1","r2","r3") map (x => new TaskResource(x,0))
}

case object DummySim2 extends TestObject {
    override def props(name: String, coordinator: ActorRef)
        (implicit executionContext: ExecutionContext): Props = {Props(new DummySim2(name, coordinator))}
    override def resources = Seq("r1","r2","r3") map (x => new TaskResource(x,0))
}

case object DummySim3 extends TestObject {
    override def props(name: String, coordinator: ActorRef)
        (implicit executionContext: ExecutionContext): Props = {Props(new DummySim3(name, coordinator))}
    override def resources = Seq("r1","r2","r3") map (x => new TaskResource(x,0))
}