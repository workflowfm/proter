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
       
        // val sim = system.actorOf(Props(new DummySim3("sim",coordinator)))


        // // Define resources
        // val r1 = new TaskResource("r1",0)
        // val r2 = new TaskResource("r2",0)
        // val r3 = new TaskResource("r3",0)
        // val r4 = new TaskResource("r4",0)
        // val resources = List (r1,r2,r3,r4)
        // coordinator ! Coordinator.AddResources(resources)

        // coordinator ! Coordinator.AddSim(0L,sim)

        // coordinator ! Coordinator.Start
    }

}