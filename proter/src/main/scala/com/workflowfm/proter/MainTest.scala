package com.workflowfm.proter

import com.workflowfm.proter._
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import java.io.File
import scala.concurrent.duration._
import scala.concurrent.Await


object MainTest {
    def main(args: Array[String]): Unit = {
        //implicit val context: ExecutionContext = ExecutionContext.global

        val coordinator = new Coordinator(new DefaultScheduler())

        val handler = SimMetricsOutputs(
        new SimMetricsPrinter(),
        new SimCSVFileOutput("output" + File.separator,"MainTest"),
        new SimD3Timeline("output" + File.separator,"MainTest")
        )

        // Subscribe the metrics actor
        coordinator.subscribe(new SimSubrunHandler(handler, 5, 100L))


        val r1 = new TaskResource("r1",0)
        val r2 = new TaskResource("r2",0)
        val r3 = new TaskResource("r3",0)

        coordinator.addResources(Seq(r1,r2,r3))

        val task1 = new FlowTask(Task("task1",2L).withResources(Seq("r1")).withCost(1L))
        val task2 = new FlowTask(Task("task2",2L).withResources(Seq("r2")))
        val task3 = new FlowTask(Task("task3",2L).withResources(Seq("r3")))

        val flow = task1 > task2 > task3

        // val single_sim = new SingleTaskSimulation("singleSim",coordinator,Seq("r1","r2","r3"),ConstantGenerator(4L))
        // val flow_sim = new FlowSimulation("flowSim",coordinator,flow)

        // coordinator.addSimulationNow(single_sim)
        // coordinator.addSimulationNow(flow_sim)


        coordinator.limit(100L)
        //coordinator.addArrivalProcessNow(NegativeExponentialRate(0.9),new SingleTaskSimulationGenerator("sim",Seq("r1","r2","r3"),ConstantGenerator(1L)))
        coordinator.addArrivalProcessNow(NegativeExponentialRate(0.17),new FlowSimulationGenerator("flowSim",flow))

        Await.result(coordinator.start(), 1.hour)
  }  
}
