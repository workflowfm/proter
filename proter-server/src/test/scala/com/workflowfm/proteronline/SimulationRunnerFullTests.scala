package com.workflowfm.proteronline

import org.scalatest.funsuite.AnyFunSuite

/**
  * Contains the full end to end tests for Simulation Runner
  *
  */
class SimulationRunnerFullTests extends AnyFunSuite {

    test("Example, finite, Simulation should run producing results") {
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
        val request: IRequest = new IRequest(List(arrival), externalResourceList)

        val results = simRun.process(request)
        assert( //Checks the results are of the correct shape and size (and that they are there)
        (results.simulations.size == 10) &&
        (results.tasks.size == 20) &&
        (results.resources.size == 2)
        )
    }

    test("Example, finite, multiple arrival simulation should run producing results") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
        new IResource("R1", 0.4),
        new IResource("R2", 8.3),
        new IResource("R3", 8.3),
        new IResource("R4", 0.8)
        )

        val taskList1: List[ITask] = List(
        new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
        new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
        new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
        )
        val flow1: IFlow = new IFlow(taskList1, "A->B->C")
        val sim1: ISimulation = new ISimulation("Sim NameA", flow1)
        val arrival1 = new IArrival(sim1, false, new IDistribution("C", 4.3, None), Some(10), None)

        val taskList2: List[ITask] = List(
        new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R3", 0),
        new ITask("E", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R4", 0),
        new ITask("F", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
        )
        val flow2: IFlow = new IFlow(taskList2, "D->E->F")
        val sim2: ISimulation = new ISimulation("Sim NameB", flow2)
        val arrival2 = new IArrival(sim2, false, new IDistribution("C", 4.3, None), Some(10), None)

        
        val request: IRequest = new IRequest(List(arrival1, arrival2), externalResourceList)

        val results = simRun.process(request)
        assert( //Checks the results are of the correct shape and size (and that they are there)
            (results.simulations.size == 20) &&
            (results.tasks.size == 60) &&
            (results.resources.size == 4)
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
        val request: IRequest = new IRequest(List(arrival), externalResourceList)

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
        val request: IRequest = new IRequest(List(arrival), externalResourceList)

        simRun.streamHandler(request)
    }

    test("Stream Test, multiple arrivals") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
        new IResource("R1", 0.4),
        new IResource("R2", 8.3),
        new IResource("R3", 8.3),
        new IResource("R4", 0.8)
        )

        val taskList1: List[ITask] = List(
        new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
        new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
        new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
        )
        val flow1: IFlow = new IFlow(taskList1, "A->B->C")
        val sim1: ISimulation = new ISimulation("Sim NameA", flow1)
        val arrival1 = new IArrival(sim1, false, new IDistribution("C", 4.3, None), Some(10), None)

        val taskList2: List[ITask] = List(
        new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R3", 0),
        new ITask("E", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R4", 0),
        new ITask("F", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
        )
        val flow2: IFlow = new IFlow(taskList2, "D->E->F")
        val sim2: ISimulation = new ISimulation("Sim NameB", flow2)
        val arrival2 = new IArrival(sim2, false, new IDistribution("C", 4.3, None), Some(10), None)

        
        val request: IRequest = new IRequest(List(arrival1, arrival2), externalResourceList)

        simRun.streamHandler(request)
    }

    test("Simulation that fails resource checks") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
            new IResource("R1", 0.4),
            new IResource("R2", 8.3),
        )
        val taskList: List[ITask] = List(
            new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
            new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
            new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R3", 0)

        )
        val flow: IFlow = new IFlow(taskList, "A->B")
        val sim: ISimulation = new ISimulation("Sim Name", flow)
        val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
        val request: IRequest = new IRequest(List(arrival), externalResourceList)

        assertThrows[IllegalArgumentException] {
            simRun.process(request)
        }
    }

    test("Simulation that fails task checks") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
            new IResource("R1", 0.4),
            new IResource("R2", 8.3),
        )
        val taskList: List[ITask] = List(
            new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
            new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
            new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)

        )
        val flow: IFlow = new IFlow(taskList, "A->B->C->GGG")
        val sim: ISimulation = new ISimulation("Sim Name", flow)
        val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
        val request: IRequest = new IRequest(List(arrival), externalResourceList)

        assertThrows[IllegalArgumentException] {
            simRun.process(request)
        }
    }

    test("Streaming Simulation that fails resource checks") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
            new IResource("R1", 0.4),
            new IResource("R2", 8.3),
        )
        val taskList: List[ITask] = List(
            new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
            new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
            new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R3", 0)

        )
        val flow: IFlow = new IFlow(taskList, "A->B->C")
        val sim: ISimulation = new ISimulation("Sim Name", flow)
        val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
        val request: IRequest = new IRequest(List(arrival), externalResourceList)

        assertThrows[IllegalArgumentException] {
            simRun.streamHandler(request)
        }
    }

    test("Streaming Simulation that fails task checks") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
            new IResource("R1", 0.4),
            new IResource("R2", 8.3),
        )
        val taskList: List[ITask] = List(
            new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
            new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
            new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)

        )
        val flow: IFlow = new IFlow(taskList, "A->B->C->GGG")
        val sim: ISimulation = new ISimulation("Sim Name", flow)
        val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
        val request: IRequest = new IRequest(List(arrival), externalResourceList)

        assertThrows[IllegalArgumentException] {
            simRun.streamHandler(request)
        }
    }

    test("Single Task Simulation") {
        val simRun = new SimulationRunner()
        val externalResourceList: List[IResource] = List(
            new IResource("R1", 0.4)
        )
        val taskList: List[ITask] = List(
            new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0)
        )
        val flow: IFlow = new IFlow(taskList, "A")
        val sim: ISimulation = new ISimulation("Sim Name", flow)
        val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
        val request: IRequest = new IRequest(List(arrival), externalResourceList)

        val results = simRun.process(request)
        assert( //Checks the results are of the correct shape and size (and that they are there)
            (results.simulations.size == 10) &&
            (results.tasks.size == 10) &&
            (results.resources.size == 1)
        )
    }
}