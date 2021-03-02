package com.workflowfm.proter

import com.workflowfm.proter.flows._

trait SimulationGenerator {
    def newSim(coordinator: Manager): SimulationRef
}

class SingleTaskSimulationGenerator(baseName: String, resources: Seq[String], duration: ValueGenerator[Long]) extends SimulationGenerator {
    var count = 0
    override def newSim(coordinator: Manager): SimulationRef = {
        val name = baseName + count.toString()
        count += 1
        new SingleTaskSimulation(name,coordinator,resources,duration)
    }
}

class FlowSimulationGenerator(baseName: String, flow: Flow) extends SimulationGenerator {
    var count = 0
    override def newSim(coordinator: Manager): SimulationRef = {
        val name = baseName + count.toString()
        count += 1
        new FlowSimulation(name,coordinator,flow.copy())
    }
}

class BPMNSimulationGenerator(baseName: String, src: String) extends SimulationGenerator {
        var count = 0
    override def newSim(coordinator: Manager): SimulationRef = {
        val name = baseName + count.toString()
        count += 1
        new BPMNSimulation(name,coordinator,src)
    }
}