package com.workflowfm.proter

import com.workflowfm.proter._
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import com.workflowfm.proter.schedule._
import java.io.File
import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future, Await }
import java.util.concurrent.TimeoutException


object Evaluation {
  def main(args: Array[String]): Unit = {
    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    val coordinator_proter = new Coordinator(new ProterScheduler())
    val coordinator_GreedyFCFS = new Coordinator(new GreedyFCFSScheduler())
    val coordinator_StrictFCFS = new Coordinator(new StrictFCFSScheduler())
    val coordinator_GreedyPriority = new Coordinator(new GreedyPriorityScheduler())
    val coordinator_StrictPriority = new Coordinator(new StrictPriorityScheduler())
    val coordinator_Lookahead = new Coordinator(new LookaheadScheduler())
    val coordinators = Seq(coordinator_proter, coordinator_GreedyFCFS, coordinator_StrictFCFS, coordinator_GreedyPriority, coordinator_StrictPriority, coordinator_Lookahead)

    val handler = SimMetricsOutputs(
    // new SimMetricsPrinter(),
    new SimMetricsScore(),
    // new SimCSVFileOutput("output" + File.separator,"MainTest"),
    // new SimD3Timeline("output" + File.separator,"MainTest")
    )

    coordinators foreach(_.subscribe(new SimMetricsHandler(handler)))

    val r1 = new TaskResource("r1",0,1)
    val r2 = new TaskResource("r2",0,1)
  
    val resources = Seq(r1,r2)
    coordinators foreach(_.addResources(resources))


    val flow = new RandomFlowFactory(0.5f, resources).withTasks(Uniform(1, 10)).withDurations(Uniform(1,10)).withNumResources(Uniform(1,2)).build
    println(s"Flow: ${flow}")
    print("Resources: "); println(resources map{r=> s"${r.name},  ${r.capacity}"})
    printTasks(flow)
    println()

    //Add and start simulations
    coordinators foreach{c =>
      val flow_sim = new FlowSimulation("sim",c,flow)
      c.addSimulationNow(flow_sim)
    }

    //Await end of simulations in each coordinator
    val a = coordinators map(_.start())
    val b = Future.sequence(a)
    try { Await.result(b, 10.second) }
    catch {
      case e: TimeoutException => {println(a); throw e}
    } 
  }

  def printTasks(flow: Flow): Unit = {
    flow match {
      case _: NoTask => return
      case f: FlowTask => {
        print(s"${f.task.name}:")
        if (f.task.resourceQuantities.isEmpty) {
          f.task.resources.foreach{x=>print(s"(${x},1)")}
        } else {
          val a = f.task.resources zip f.task.resourceQuantities
          a foreach{x=>print(s"(${x._1},${x._2})")}
        }
        print(" ")
      }
      case f: Then => {printTasks(f.left); printTasks(f.right)}
      case f: And => {printTasks(f.left); printTasks(f.right)}
    }   
  }
}





      // val A = new FlowTask(Task("A",2L).withResources(Seq("r1")).withPriority(Task.High).withResourceQuantities(Seq(1)))
      // val flow = (A)
      // //coordinator.addArrivalNow(10, Constant(10L), new FlowSimulationGenerator("flow", flow))
