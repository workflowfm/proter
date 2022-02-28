package com.workflowfm.proter

import com.workflowfm.proter._
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import com.workflowfm.proter.schedule._
import java.io.File
import scala.concurrent.duration._
import scala.concurrent.{ ExecutionContext, Future, Await }
import java.util.concurrent.TimeoutException


object Examples {
  def main(args: Array[String]): Unit = {
    implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

    // val coordinator_proter = new Coordinator(new ProterScheduler())
    // val coordinator_GreedyFCFS = new Coordinator(new GreedyFCFSScheduler())
    // val coordinator_StrictFCFS = new Coordinator(new StrictFCFSScheduler())
    // val coordinator_GreedyPriority = new Coordinator(new GreedyPriorityScheduler())
    // val coordinator_StrictPriority = new Coordinator(new StrictPriorityScheduler())
    // val coordinator_Lookahead = new Coordinator(new LookaheadScheduler())

    val coordinator_proter = new Coordinator(new LookaheadScheduler())

    val handler = SimMetricsOutputs(
    new SimMetricsPrinter(),
    new SimMetricsScore(),
    // new SimCSVFileOutput("output" + File.separator,"MainTest"),
    // new SimD3Timeline("output" + File.separator,"MainTest")
    )

    coordinator_proter.subscribe(new SimMetricsHandler(handler))

    val r1 = new TaskResource("r1",0,1)
    val r2 = new TaskResource("r2",0,1)
    val resources = Seq(r1,r2)

    coordinator_proter.addResources(resources)

    // *** Unable to add (9,14) to Schedule: List((9,12), (16,9))
    var s = new WeightedSchedule(List())
    println(s)
    s = s +> (9, 12)
    println(s)
    s = s +> (16, 9)
    println(s)
    s = s +> (9, 14)
    println(s)


// Flow: ((task1 + (task2 + (task3 + task4))) + ((task5 > task6) > (task7 + task8)))
// Resources: List(r1,  1.0, r2,  1.0)
// task1:Constant(5.0),-2,(r1,1)(r2,1) task2:Constant(5.0),-1,(r2,1) task3:Constant(3.0),0,(r1,1) task4:Constant(4.0),1,(r1,1) task5:Constant(3.0),1,(r1,1)(r2,1) task6:Constant(9.0),1,(r1,1)(r2,1) task7:Constant(7.0),-2,(r1,1)(r2,1) task8:Constant(9.0),1,(r1,1)
    val task1 = new FlowTask(Task("task1",5L).withResources(Seq("r1","r2")).withPriority(-2))//.withResourceQuantities(Seq(1)))
    val task2 = new FlowTask(Task("task2",5L).withResources(Seq("r2")).withPriority(-1))
    val task3 = new FlowTask(Task("task3",3L).withResources(Seq("r1")).withPriority(0))
    val task4 = new FlowTask(Task("task4",4L).withResources(Seq("r1")).withPriority(1))
    val task5 = new FlowTask(Task("task5",3L).withResources(Seq("r1","r2")).withPriority(1))
    val task6 = new FlowTask(Task("task6",9L).withResources(Seq("r1","r2")).withPriority(1))
    val task7 = new FlowTask(Task("task7",7L).withResources(Seq("r1","r2")).withPriority(-2))
    val task8 = new FlowTask(Task("task8",9L).withResources(Seq("r1")).withPriority(1))

    val flow = ((task1 + (task2 + (task3 + task4))) + ((task5 > task6) > (task7 + task8)))

    //Add and start simulations
    val flow_sim = new FlowSimulation("sim",coordinator_proter,flow)
    coordinator_proter.addSimulationNow(flow_sim)

    //Await end of simulations in each coordinator
    val a = coordinator_proter.start()
    try { Await.result(a, 10.second) }
    catch {
      case e: TimeoutException => {println(a); throw e}
    } 
  }
}





      // val A = new FlowTask(Task("A",2L).withResources(Seq("r1")).withPriority(Task.High).withResourceQuantities(Seq(1)))
      // val flow = (A)
      // //coordinator.addArrivalNow(10, Constant(10L), new FlowSimulationGenerator("flow", flow))
