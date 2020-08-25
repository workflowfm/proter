package com.workflowfm.simulator

import akka.actor.{ActorSystem, ActorRef,Props}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent._
import scala.concurrent.duration._
import com.workflowfm.simulator._
import com.workflowfm.simulator.metrics._
import com.workflowfm.simulator.events.{ ShutdownHandler }
import uk.ac.ed.inf.ppapapan.subakka.Subscriber
import java.io.File

object LookaheadTest {
    //toggle for debug
    val DEBUG = false

    def main(args: Array[String]): Unit = {

        implicit val system: ActorSystem = ActorSystem("LookaheadTest")
        implicit val executionContext: ExecutionContext = ExecutionContext.global
        implicit val timeout = Timeout(2.seconds)

        val coordinator = system.actorOf(Coordinator.props(LookaheadScheduler))
        val shutdownActor = Subscriber.actor(new ShutdownHandler())

        val handler = SimMetricsOutputs(
	        new SimMetricsPrinter(),
            new SimCSVFileOutput("lookaheadtest" + File.separator + "output" + File.separator,"LookaheadTest"),
	        new SimD3Timeline("lookaheadtest" + File.separator + "output" + File.separator,"LookaheadTest")
        )

        Await.result(new SimOutputHandler(handler).subAndForgetTo(coordinator,Some("MetricsHandler")), 3.seconds)
        Await.result(shutdownActor ? Subscriber.SubAndForgetTo(coordinator), 3.seconds)

        if (DEBUG) {
            println(s"Cores: ${Runtime.getRuntime().availableProcessors()}")
            val config = system.settings.config.getConfig("akka.actor.default-dispatcher")
            println(s"Parallelism: ${config.getInt("fork-join-executor.parallelism-min")}-${config.getInt("fork-join-executor.parallelism-max")} x ${config.getDouble("fork-join-executor.parallelism-factor")}")
            val printer = new com.workflowfm.simulator.events.PrintEventHandler
            Await.result(printer.subAndForgetTo(coordinator), 3.seconds)
        }

//=========================================================================================
       
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
                
                add1To1Lookahead(id1,id2,generator2,resources2)
                add1To1Lookahead(id1,id4,generator4,resources4)
                add1To1Lookahead(id2,id3,generator3,resources3)

                //Equal to flow: task1 > ( (task2 > task3) + task4 )
                // i.e. the sequence task2>task3 happens in parallel to task4
                val task1 = task(id1,generator1, 
                    {(_,_)=> task(id2,generator2,
                        {(_,_)=> task(id3,generator3, 
                            {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id3))}
                            }, 
                        resources3, None, Seq()); ack(Seq(id2)) 
                    }, 
                    resources2, None, Seq()
                    ); 
                    task(id4,generator4, 
                        {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id4))}
                        }, 
                    resources4, None, Seq());
                    ack(Seq(id1)) 
                }, 
                resources1, None, Seq())

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
                
                add1To1Lookahead(id1,id2,generator2,resources2)
                add1To1Lookahead(id1,id4,generator4,resources4)
                add1To1Lookahead(id2,id3,generator3,resources3)
                add1To1Lookahead(id4,id5,generator5,resources5)

                val task1 = task(id1,generator1, 
                    {(_,_)=> task(id2,generator2,
                        {(_,_)=> task(id3,generator3, 
                            {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id3))}
                            }, 
                        resources3, None, Seq()); ack(Seq(id2)) 
                    }, 
                    resources2, None, Seq()
                    ); 
                    task(id4,generator4, 
                        {(_,_)=> task(id5,generator5, 
                            {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id5))}
                            }, 
                        resources5, None, Seq()); ack(Seq(id4)) 
                    }, 
                    resources4, None, Seq());
                    ack(Seq(id1)) 
                }, 
                resources1, None, Seq())

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
                
                add1To1Lookahead(id1,id2,generator2,resources2)
                add1To1Lookahead(id1,id3,generator3,resources3)
                add1To1Lookahead(id1,id4,generator4,resources4)
                add1To1Lookahead(id1,id6,generator6,resources6)

                def function(s: Seq[(java.util.UUID,Long)]): Long = {
                    val prerequisites= Set(id2,id3,id4) forall (x=>s.exists {case(id,l)=>id==x} )
                    if (prerequisites) println("AAAAAAAAAAAA")
                    if (prerequisites) ( s filter (x=> Set(id2,id3,id4) contains x._1) map (_._2) ).max
                    else -1
                }
                addManyTo1Lookahead(function, id5, generator5, resources5)

                def task5(){
                    task(id5,generator5,
                        {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id5))}},
                        resources5, None, Seq()
                    )
                }

                val task1 = task(id1,generator1, 
                    {(_,_)=> task(id2,generator2,
                        {(_,_)=> if (count==2) task5(); count+=1; ack(Seq(id2))},
                        resources2, None, Seq()
                    ); 
                    task(id3,generator3, 
                        {(_,_)=> if (count==2) task5(); count+=1; ack(Seq(id3))}, 
                        resources3, None, Seq()
                    );
                    task(id4,generator4, 
                        {(_,_)=> if (count==2) task5(); count+=1; ack(Seq(id4))},
                        resources4, None, Seq()
                    );
                    task(id6,generator6, 
                        {(_,_)=> if (tick) promise.success(Unit) else {tick=true; ack(Seq(id6))}},
                        resources6, None, Seq()
                    );
                    ack(Seq(id1)) 
                }, 
                resources1, None, Seq())

                ready()
                promise.future
            } 
        }

        val sim = system.actorOf(Props(new DummySim2("sim",coordinator)))


        // Define resources
        val r1 = new TaskResource("r1",0)
        val r2 = new TaskResource("r2",0)
        val r3 = new TaskResource("r3",0)
        val r4 = new TaskResource("r4",0)
        val resources = List (r1,r2,r3,r4)
        coordinator ! Coordinator.AddResources(resources)

        coordinator ! Coordinator.AddSim(0L,sim)

        coordinator ! Coordinator.Start
    }

}