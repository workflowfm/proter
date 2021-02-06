package com.workflowfm.proter

import com.workflowfm.proter.flows._

trait SimulationGenerator {
  def newSim(coordinator: Manager): SimulationRef
}

case class SingleTaskSimulationGenerator(resources: Seq[String], duration: ValueGenerator[Long]) extends SimulationGenerator {
    override def newSim(coordinator: Manager): SimulationRef = {
        //val r = new scala.util.Random()
        val name: String = "testSim" //r.nextString(10)
        new SingleTaskSimulation(name,coordinator,resources,duration)
    }
}

case class FlowSimulationGenerator() extends SimulationGenerator {
    override def newSim(coordinator: Manager): SimulationRef = {
        val task1 = new FlowTask(Task("task1",2L).withResources(Seq("r1")))
        val task2 = new FlowTask(Task("task2",2L).withResources(Seq("r2")))
        val task3 = new FlowTask(Task("task3",2L).withResources(Seq("r3")))

        val flow = task1 > task2 > task3

        new FlowSimulation("flowSim",coordinator,flow)
    }
}