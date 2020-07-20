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

object FlowsMain {
    //toggle for debug
    val DEBUG = true

    def main(args: Array[String]): Unit = {

        implicit val system: ActorSystem = ActorSystem("FlowsMain")
        implicit val executionContext: ExecutionContext = ExecutionContext.global
        implicit val timeout = Timeout(2.seconds)

        val coordinator = system.actorOf(Coordinator.props(DefaultScheduler))
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

        // Define resources
        val r1 = new TaskResource("r1",0)
        val r2 = new TaskResource("r2",0)
        val r3 = new TaskResource("r3",0)
        val r4 = new TaskResource("r4",0)
        val r5 = new TaskResource("r5",0)
        val r6 = new TaskResource("r6",0)
        val resources = List (r1,r2,r3,r4,r5,r6)
        coordinator ! Coordinator.AddResources(resources)

        // Define tasks 
        val invis = FlowTask(TaskGenerator("invis","sim1",ConstantGenerator(0L),ConstantGenerator(0L)),Seq.empty[String])
        val task1 = FlowTask(TaskGenerator("task1","sim1",ConstantGenerator(3L),ConstantGenerator(0L)),Seq("r1"))
        val task2 = FlowTask(TaskGenerator("task2","sim1",ConstantGenerator(6L),ConstantGenerator(0L)),Seq("r2"))
        val task3 = FlowTask(TaskGenerator("task3","sim1",ConstantGenerator(7L),ConstantGenerator(0L)),Seq("r3"))
        val task4 = FlowTask(TaskGenerator("task4","sim1",ConstantGenerator(7L),ConstantGenerator(0L)),Seq("r3","r2"))
        val task5 = FlowTask(TaskGenerator("task5","sim1",ConstantGenerator(6L),ConstantGenerator(0L)),Seq("r4"))
        val task6 = FlowTask(TaskGenerator("task6","sim1",ConstantGenerator(6L),ConstantGenerator(0L)),Seq("r5"))
        val task7 = FlowTask(TaskGenerator("task7","sim1",ConstantGenerator(7L),ConstantGenerator(0L)),Seq("r6"))

        // val flow1 = And(And(And(And(And(task2,task3),task4),task5),task6),task7)
        // val flow2 = And(task2,And(task3,And(task4,And(task5,And(task6,NoTask)))))
        // val flow3 = All(task1,task2,task3,task4,task5,task6,task7)
        // val flow4 = And(Then(task5,task1),Then(task2,task3))
        val flow = (task1 | task2) > (task3 | task5) > task6

        val generator = new ConstantGenerator[Long](3L)
        //coordinator ! Coordinator.AddSim(0L,system.actorOf(FlowSimulationActor.props("sim1",coordinator,flow5),"sim1"))
        //coordinator ! Coordinator.AddSim(15L,system.actorOf(FlowSimulationActor.props("sim2",coordinator,flow1),"sim2"))
        coordinator ! Coordinator.AddSim(20L,system.actorOf(FlowSimulationActor.props("sim1",coordinator,flow),"sim1"))
        coordinator ! Coordinator.Start
    }

}