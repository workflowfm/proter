package com.workflowfm.simulator

import akka.actor.{ ActorSystem, Props, ActorRef }
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent._
import scala.concurrent.duration._
import com.workflowfm.simulator._
import com.workflowfm.simulator.metrics._
import com.workflowfm.simulator.events.{ ShutdownHandler }
import uk.ac.ed.inf.ppapapan.subakka.Subscriber
import java.io.File

object FlowsMain {
    //toggle for debug
    val DEBUG = false

    def main(args: Array[String]): Unit = {

        implicit val system: ActorSystem = ActorSystem("ChildSims")
        implicit val executionContext: ExecutionContext = ExecutionContext.global
        implicit val timeout = Timeout(2.seconds)

        val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))
        val shutdownActor = Subscriber.actor(new ShutdownHandler())

        val handler = SimMetricsOutputs(
	        new SimMetricsPrinter(),
            new SimCSVFileOutput("ChildSims" + File.separator + "output" + File.separator,"ChildSims"),
	        new SimD3Timeline("ChildSims" + File.separator + "output" + File.separator,"ChildSims")
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

        class DummySim(name: String, coordinator: ActorRef)(
        implicit executionContext: ExecutionContext
        ) extends AsyncSimulation(name, coordinator) {

            override def run(): Future[Any] = {
                val promise = Promise[Any]()

                val childSim = system.actorOf(SingleTaskSimulation.props("childSim", coordinator, Seq("r1"), ConstantGenerator(5L)))
                val callback2: Callback = (_,_)=> promise.success(Unit)

                sim(childSim,callback2)
                
                task(TaskGenerator("t1","sim",ConstantGenerator(1L),ConstantGenerator(0L)),
                (_,_)=> {task(TaskGenerator("t2","sim",ConstantGenerator(2L),ConstantGenerator(0L)), (_,_)=>ready(), "r3" ); ready() },
                "r2"
                )

                ready()

                promise.future

            }
        }

        //=========================================================================================

        // Define resources
        val r1 = new TaskResource("r1",0)
        val r2 = new TaskResource("r2",0)
        val r3 = new TaskResource("r3",0)
        val r4 = new TaskResource("r4",0)
        val r5 = new TaskResource("r5",0)
        val r6 = new TaskResource("r6",0)
        val r7 = new TaskResource("r7",0)
        val r8 = new TaskResource("r8",0)
        val resources = List (r1,r2,r3,r4,r5,r6,r7,r8)
        coordinator ! Coordinator.AddResources(resources)

        val sim = Props(new DummySim("sim", coordinator))

        coordinator ! Coordinator.AddSim(0L,system.actorOf(sim,"sim"))

        coordinator ! Coordinator.Start
    }

}