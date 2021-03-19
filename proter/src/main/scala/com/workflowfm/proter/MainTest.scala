package com.workflowfm.proter

import com.workflowfm.proter._
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import java.io.File
import scala.concurrent.duration._
import scala.concurrent.Await
import org.camunda.bpm.model.bpmn.Bpmn


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
        coordinator.subscribe(new SimSubrunHandler(handler, 5, 200L))


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
        //coordinator.addSimulation(0L,new BPMNSimulation("sim1",coordinator))


        coordinator.limit(200L)
        //coordinator.addArrivalProcessNow(NegativeExponentialRate(0.9),new SingleTaskSimulationGenerator("sim",Seq("r1","r2","r3"),ConstantGenerator(1L)))
        //coordinator.addArrivalProcessNow(NegativeExponentialRate(0.17),new FlowSimulationGenerator("flowSim",flow))

        coordinator.addArrivalProcessNow(NegativeExponentialRate(0.09),new BPMNSimulationGenerator("BPMNSim","1/1.bpmn", "../../project/project/report/evaluation/examples/1/proter/modelData.json"))

        // case class BpmnData1(
        //     id: String,
        //     duration: ValueGenerator[Long],
        //     cost: Int,
        //     priority: Task.Priority,
        //     resources: List[String]
        // )
    
        // println("JSON TEST")

        // import spray.json._
        // import DefaultJsonProtocol._

        // //implicit val priorityFormat = jsonFormat1(Task.Priority)

        // implicit object priorityFormat extends RootJsonFormat[Task.Priority] {
        //     // deserialization code
        //     override def read(json: JsValue): Task.Priority = {
        //         val priority = json.asInstanceOf[JsString].value
        //         priority match {
        //             case "VeryLow" => return Task.VeryLow
        //             case "Low" => return Task.Low
        //             case "Medium" => return Task.Medium
        //             case "High" => return Task.High
        //             case "Highest" => return Task.Highest
        //         }
        //     }

        //     // serialization code
        //     override def write(priority: Task.Priority): JsValue = JsString((priority.toString()))
        // }

        // implicit object valueGeneratorFormat extends RootJsonFormat[ValueGenerator[Long]] {
        //     override def write(generator: ValueGenerator[Long]): JsValue = {
        //         if (generator.isInstanceOf[ConstantGenerator[Long]]) {
        //             return JsObject(
        //                 "type" -> "ConstantGenerator".toJson,
        //                 "val" -> generator.asInstanceOf[ConstantGenerator[Long]].value.toJson
        //             )
        //         }
        //         else {
        //             return JsObject(
        //                 "type" -> "UniformGenerator".toJson,
        //                 "min" -> generator.asInstanceOf[UniformGenerator].min.toJson,
        //                 "max" -> generator.asInstanceOf[UniformGenerator].max.toJson
        //             )
        //         }
                
        //     }

        //     override def read(json: JsValue): ValueGenerator[Long] = {
        //         val fields = json.asJsObject("ValueGenerator object expected").fields
        //         fields("type").convertTo[String] match {
        //             case "ConstantGenerator" => {
        //                 return ConstantGenerator(fields("val").convertTo[Long])
        //             } 
        //             case "UniformGenerator" => {
        //                 return UniformGenerator(fields("min").convertTo[Long], fields("max").convertTo[Long])
        //             }  
        //         }
        //     }
        // }


        // implicit val bpmnFormat = jsonFormat5(BpmnData1)
        
        // val res = List("r1","r2")
        // val b = BpmnData1("id1", ConstantGenerator[Long](3), 2, Task.Low, res)

        // val j = b.toJson
        // println(j.prettyPrint)

        // val thingo = """
        //             {
        //     "cost": 2,
        //     "duration": {
        //         "type": "UniformGenerator",
        //         "min": 3,
        //         "max": 4
        //     },
        //     "id": "id1",
        //     "priority": "Low",
        //     "resources": ["r1", "r2"]
        //     }
        // """

        // val j2 = thingo.parseJson
        // println(j2.convertTo[BpmnData1])


        Await.result(coordinator.start(), 1.hour)
  }  
}
