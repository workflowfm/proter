package com.workflowfm.simulator.flowsExample

import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent._
import scala.concurrent.duration._
import com.workflowfm.simulator._
import com.workflowfm.simulator.metrics._
import com.workflowfm.simulator.events.{ ShutdownHandler }
import com.workflowfm.simulator.flows._
import uk.ac.ed.inf.ppapapan.subakka.Subscriber
import java.io.File

object BPMNMain {
    //toggle for debug
    val DEBUG = false

    def main(args: Array[String]): Unit = {

        implicit val system: ActorSystem = ActorSystem("FlowsMain")
        implicit val executionContext: ExecutionContext = ExecutionContext.global
        implicit val timeout = Timeout(2.seconds)

        val coordinator = system.actorOf(Coordinator.props(new DefaultScheduler()))
        val shutdownActor = Subscriber.actor(new ShutdownHandler())

        val handler = SimMetricsOutputs(
	        new SimMetricsPrinter(),
            new SimCSVFileOutput("flowsExample" + File.separator + "output" + File.separator,"FlowsMain"),
	        new SimD3Timeline("flowsExample" + File.separator + "output" + File.separator,"FlowsMain")
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

        // // Define resources
        // val r1 = new TaskResource("r1",0)
        // val r2 = new TaskResource("r2",0)
        // val r3 = new TaskResource("r3",0)
        // val r4 = new TaskResource("r4",0)
        // val r5 = new TaskResource("r5",0)
        // val r6 = new TaskResource("r6",0)
        // val r7 = new TaskResource("r7",0)
        // val r8 = new TaskResource("r8",0)
        // val resources = List (r1,r2,r3,r4,r5,r6,r7,r8)
        // coordinator ! Coordinator.AddResources(resources)

        // // Define tasks 
        // val task1 = FlowTask(TaskGenerator("task1","sim1",ConstantGenerator(1L),ConstantGenerator(0L)).withResources(Seq("r1")))
        // val task2 = FlowTask(TaskGenerator("task2","sim1",ConstantGenerator(1L),ConstantGenerator(0L)).withResources(Seq("r2")))
        // val task3 = FlowTask(TaskGenerator("task3","sim1",ConstantGenerator(1L),ConstantGenerator(0L)).withResources(Seq("r3")))

        // val flow = task1 > task2 > task3
        
        //BPMN.runScriptProcess("myBpmn.bpmn", "MyTest.process")

        //coordinator ! Coordinator.AddSim(0L,system.actorOf(FlowSimulationActor.props("sim1",coordinator,flow),"sim1"))
                                                                                                    // "myBpmn.bpmn", "MyTest.process"
                                                                                                    // "Evaluation.bpmn", "com.sample.evaluation"
        //coordinator ! Coordinator.AddSim(0L,system.actorOf(BPMNSimulationActor.props("sim1",coordinator,"Evaluation.bpmn", "com.sample.evaluation"),"sim1"))
        //coordinator ! Coordinator.AddSim(0L,system.actorOf(BPMNSimulationActor.props("sim1",coordinator,"myBpmn.bpmn", "MyTest.process"),"sim1"))
        coordinator ! Coordinator.AddSim(0L,system.actorOf(BPMNSimulationActor.props("sim1",coordinator,"test_human2.bpmn", "MyTest.test_human"),"sim1"))
        //coordinator ! Coordinator.AddSim(0L,system.actorOf(BPMNSimulationActor.props("sim1",coordinator,"test_script2.bpmn", "MyTest.test_script"),"sim1"))
        //coordinator ! Coordinator.AddSim(0L,system.actorOf(BPMNSimulationActor.props("sim1",coordinator,"test_abstract.bpmn", "MyTest.test_abstract"),"sim1"))

        coordinator ! Coordinator.Start
    }

}