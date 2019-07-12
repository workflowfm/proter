package com.workflowfm.simulator

import akka.actor._
import scala.concurrent.duration._
import scala.concurrent.Await
import scala.concurrent.Future
import scala.util.Success
import scala.util.Failure
import scala.concurrent.ExecutionContext


abstract class Simulation(val name:String)  { //extends SimulationMetricTracker
  def run():Future[Any]

  /**
    *  This should check all executing PiInstances if they are simulationReady.
    *  This means that all possible execution has been performed and they are all
    *  waiting for simulation time to pass.
    *  @return true if all PiInstances are simulationReady
    */
  def simulationReady:Boolean
}

class TaskSimulation(simulationName:String, coordinator:ActorRef, resources:Seq[String], duration:ValueGenerator[Long], val cost:ValueGenerator[Long]=new ConstantGenerator(0L), interrupt:Int=(-1), priority:Task.Priority=Task.Medium)(implicit system: ActorSystem) extends Simulation(simulationName) {
  override def run() = {
    TaskGenerator(simulationName + "Task", simulationName, duration, cost, interrupt, priority).addTo(coordinator,resources :_*)
  }

  override def simulationReady = true
}

trait SimulatedProcess {
   def simulationName:String
   def isSimulatedProcess = true
   
   def simulate[T](gen:TaskGenerator, coordinator:ActorRef, result:T, resources:String*)(implicit system: ActorSystem, context: ExecutionContext = ExecutionContext.global):Future[T] ={
     gen.addTo(coordinator, resources:_*).map(_ => result)
   }
}
