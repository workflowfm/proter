package com.workflowfm.proteronline

import org.scalatest.funsuite.AnyFunSuite

/**
  * Contains the full end to end tests for Simulation Runner
  *
  */
class SimulationRunnerFullTests extends AnyFunSuite {

    test("Example Simulation should run from JSON producing results") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
        new IResource("R1", 0.4),
        new IResource("R2", 8.3),
        )
        val taskList: List[ITask] = List(
        new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
        new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
        )
        val flow: IFlow = new IFlow(taskList, "A->B")
        val sim: ISimulation = new ISimulation("Sim Name", flow)
        val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
        val request: IRequest = new IRequest(arrival, externalResourceList)

        val results = simRun.process(request)
        assert( //Checks the results are of the correct shape and size (and that they are there)
        (results.simulations.size == 10) &&
        (results.tasks.size == 20) &&
        (results.resources.size == 2)
        )
    }

    test("Simulation with Infinite Arrival") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
        new IResource("R1", 0.4),
        new IResource("R2", 8.3),
        )
        val taskList: List[ITask] = List(
        new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
        new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
        )
        val flow: IFlow = new IFlow(taskList, "A->B")
        val sim: ISimulation = new ISimulation("Sim Name", flow)
        val arrival = new IArrival(sim, true, new IDistribution("C", 4.3, None), None, Some(100))
        val request: IRequest = new IRequest(arrival, externalResourceList)

        val results = simRun.process(request)
        assert( //Checks the results are of the correct shape and size (and that they are there)
        (results.resources.size == 2)
        )
    }

    test("Stream Test") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
        new IResource("R1", 0.4),
        new IResource("R2", 8.3),
        )
        val taskList: List[ITask] = List(
        new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
        new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
        )
        val flow: IFlow = new IFlow(taskList, "A->B")
        val sim: ISimulation = new ISimulation("Sim Name", flow)
        val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(3), None)
        val request: IRequest = new IRequest(arrival, externalResourceList)

        simRun.streamHandler(request)
    }
}