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

    val coordinator_proter = new Coordinator(new ProterScheduler())

    val handler = SimMetricsOutputs(
    // new SimMetricsPrinter(),
    // new SimMetricsScore(),
    // new SimCSVFileOutput("output" + File.separator,"MainTest"),
    // new SimD3Timeline("output" + File.separator,"MainTest")
    )

    coordinator_proter.subscribe(new SimMetricsHandler(handler))

    val r1 = new TaskResource("r1",0,5)
    val r2 = new TaskResource("r2",0,5)
    val r3 = new TaskResource("r3",0,5)
    val resources = Seq(r1,r2,r3)

    coordinator_proter.addResources(resources)

    // *** Unable to add (9,14) to Schedule: List((9,12), (16,9))
    // var s = new WeightedSchedule(List())
    // println(s)
    // s = s +> (9, 12)
    // println(s)
    // s = s +> (16, 9)
    // println(s)
    // s = s +> (9, 14)
    // println(s)


// Flow: (task1 > (task2 + (task3 + (task4 + task5))))
// Resources: List(r1,  5.0, r2,  5.0, r3,  5.0)
// task1:Constant(10.0),-1,(r3,4) task2:Constant(10.0),1,(r1,1)(r3,4) task3:Constant(7.0),1,(r1,4)(r2,2) task4:Constant(10.0),-1,(r1,3)(r3,1) task5:Constant(9.0),-2,(r1,1)(r2,1)(r3,1)
// Queue(Task(58afd86e-2db4-4918-8afd-ba4230e546b9,task1,sim10,0,[r3],d10(10),c0.0,i-1,-1))
// 4 and 5.0,
// Queue(Task(2f5e5ccc-7132-4b2f-9be3-3313a279af8f,task2,sim10,10,[r1,r3],d10(10),c0.0,i-1,1), Task(59fc0d82-e984-4d08-84e6-8a341a30a655,task3,sim10,10,[r1,r2],d7(7),c0.0,i-1,1))
// 1 and 5.0,
// 4 and 5.0,
// 4 and 4.0,
// 2 and 5.0,
// Queue(Task(127082d2-f4db-4fd2-b6e1-dbe99a11e4eb,task4,sim10,10,[r1,r3],d10(10),c0.0,i-1,-1), Task(24fa97f5-32c2-41f9-99ce-2fcdb78aec24,task5,sim10,10,[r1,r2,r3],d9(9),c0.0,i-1,-2))
// 3 and 4.0,
// 1 and 1.0,
// 1 and 1.0,
// 1 and 5.0,
// 1 and 0.0,
// WARNING: NO CAPACITY
// Queue()
// Queue()
// Queue()

    val task1 = new FlowTask(Task("task1",10L).withResources(Seq("r3")).withPriority(-1).withResourceQuantities(Seq(4)))
    val task2 = new FlowTask(Task("task2",10L).withResources(Seq("r1","r3")).withPriority(1).withResourceQuantities(Seq(1,4)))
    val task3 = new FlowTask(Task("task3",7L).withResources(Seq("r1","r2")).withPriority(1).withResourceQuantities(Seq(4,2)))
    val task4 = new FlowTask(Task("task4",10L).withResources(Seq("r1","r3")).withPriority(-1).withResourceQuantities(Seq(3,1)))
    val task5 = new FlowTask(Task("task5",9L).withResources(Seq("r1","r2","r3")).withPriority(-2).withResourceQuantities(Seq(1,1,1)))

    val flow = (task1 > (task2 + (task3 + (task4 + task5))))

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
