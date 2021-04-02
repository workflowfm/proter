package com.workflowfm.proter
import com.workflowfm.proter.metrics._
import java.io.File
import scala.concurrent.duration._
import scala.concurrent.Await


object Evaluation {

    def main(args: Array[String]): Unit = {

        if (args.length != 5) {
            println("Usage: inputBPMN, json, output, timeLimit, rate")
            return;
        }

        val inputBPMN: String = args(0)
        val json: String = args(1)
        val output: String = args(2)
        val timeLimit = args(3).toInt
        val rate = args(4).toDouble
        val negativeExponential = if (args.length > 5)  true else false

        val coordinator = new Coordinator(new DefaultScheduler())

        val handler = SimMetricsOutputs(
        new SimMetricsPrinter(),
        new SimCSVFileOutput(output + File.separator,"EvaluationOutput"),
        new SimD3Timeline(output + File.separator,"EvaluationOutput")
        )

        // Subscribe the metrics actor
        coordinator.subscribe(new SimSubrunHandler(handler, 5, 200L))


        val r1 = new TaskResource("r1",0)
        val r2 = new TaskResource("r2",0)
        val r3 = new TaskResource("r3",0)
        val r4 = new TaskResource("r4",0)

        coordinator.addResources(Seq(r1,r2,r3,r4))

        coordinator.limit(timeLimit.toLong)
        //coordinator.addArrivalProcessNow(NegativeExponentialRate(0.9),new SingleTaskSimulationGenerator("sim",Seq("r1","r2","r3"),ConstantGenerator(1L)))
        //coordinator.addArrivalProcessNow(NegativeExponentialRate(0.17),new FlowSimulationGenerator("flowSim",flow))
        if (negativeExponential) coordinator.addArrivalProcessNow(NegativeExponentialRate(rate),new BPMNSimulationGenerator("Sim",inputBPMN, json))
        else coordinator.addArrivalProcessNow(ConstantRate(rate),new BPMNSimulationGenerator("Sim",inputBPMN, json))
        Await.result(coordinator.start(), 1.hour)
  }  
}
