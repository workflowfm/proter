package com.workflowfm.proteronline

import org.scalatest.funsuite.AnyFunSuite

/**
  * Contains the partial tests for SimulationRunner primarily testing the validation and utility functions of that class
  *
  */
class SimulationRunnerTests extends AnyFunSuite {

  test("matchingResources, should identify simple matching resource lists") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3)
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(List(arrival), externalResourceList)
    
    assert(simRun.matchingResources(request))
  }

  test("matchingResources, should identify matching resource list with multiple arrivals") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
      new IResource("R3", 11),
      new IResource("R4", 0.8)
    )

    val taskList1: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow1: IFlow = new IFlow(taskList1, "A->B->C")
    val sim1: ISimulation = new ISimulation("Sim Name", flow1)
    val arrival1 = new IArrival(sim1, false, new IDistribution("C", 4.3, None), Some(10), None)

    val taskList2: List[ITask] = List(
      new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R3", 0),
      new ITask("E", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R4", 0),
      new ITask("F", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow2: IFlow = new IFlow(taskList2, "D->E->F")
    val sim2: ISimulation = new ISimulation("Sim Name", flow2)
    val arrival2 = new IArrival(sim2, false, new IDistribution("C", 4.3, None), Some(10), None)

    
    val request: IRequest = new IRequest(List(arrival1, arrival2), externalResourceList)

    assert(simRun.matchingResources(request))
  }

  test("matchingResources, multiple resources per task") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3)
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(List(arrival), externalResourceList)
    
    assert(simRun.matchingResources(request))
  }

  test("matchingResources, missing task in defined resources") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(List(arrival), externalResourceList)
    
    assert(simRun.matchingResources(request) == false)
  }

  test("matchingResources, missing task in defined resources with multiple arrivals") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
      new IResource("R4", 0.8)
    )

    val taskList1: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow1: IFlow = new IFlow(taskList1, "A->B->C")
    val sim1: ISimulation = new ISimulation("Sim Name", flow1)
    val arrival1 = new IArrival(sim1, false, new IDistribution("C", 4.3, None), Some(10), None)

    val taskList2: List[ITask] = List(
      new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R3", 0),
      new ITask("E", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R4", 0),
      new ITask("F", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow2: IFlow = new IFlow(taskList2, "D->E->F")
    val sim2: ISimulation = new ISimulation("Sim Name", flow2)
    val arrival2 = new IArrival(sim2, false, new IDistribution("C", 4.3, None), Some(10), None)

    
    val request: IRequest = new IRequest(List(arrival1, arrival2), externalResourceList)

    assert(!simRun.matchingResources(request))
  }

  test("matchingResources, unused tasks in specified resources") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
      new IResource("R3", 6.2)
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(List(arrival), externalResourceList)
    
    assert(simRun.matchingResources(request))
  }

  test("matchingTasks, should match simple instance") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(List(arrival), externalResourceList)
    
    assert(simRun.matchingTasks(request))
  }

  test("matchingTasks, should match from multiple arrivals") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
      new IResource("R3", 11),
      new IResource("R4", 0.8)
    )

    val taskList1: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow1: IFlow = new IFlow(taskList1, "A->B->C")
    val sim1: ISimulation = new ISimulation("Sim Name", flow1)
    val arrival1 = new IArrival(sim1, false, new IDistribution("C", 4.3, None), Some(10), None)

    val taskList2: List[ITask] = List(
      new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R3", 0),
      new ITask("E", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R4", 0),
      new ITask("F", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow2: IFlow = new IFlow(taskList2, "D->E->F")
    val sim2: ISimulation = new ISimulation("Sim Name", flow2)
    val arrival2 = new IArrival(sim2, false, new IDistribution("C", 4.3, None), Some(10), None)

    
    val request: IRequest = new IRequest(List(arrival1, arrival2), externalResourceList)
    
    assert(simRun.matchingTasks(request))
  }

  test("matchingTasks, should spot missing task in defined tasks") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(List(arrival), externalResourceList)
    
    assert(!simRun.matchingTasks(request))
  }

    test("matchingTasks, should spot missing task in defined tasks, multiple arrivals") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
      new IResource("R3", 11),
      new IResource("R4", 11)
    )

    val taskList1: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow1: IFlow = new IFlow(taskList1, "A->B->C")
    val sim1: ISimulation = new ISimulation("Sim Name", flow1)
    val arrival1 = new IArrival(sim1, false, new IDistribution("C", 4.3, None), Some(10), None)

    val taskList2: List[ITask] = List(
      new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R3", 0),
      new ITask("E", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R4", 0),
      new ITask("F", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow2: IFlow = new IFlow(taskList2, "D->E->F->G")
    val sim2: ISimulation = new ISimulation("Sim Name", flow2)
    val arrival2 = new IArrival(sim2, false, new IDistribution("C", 4.3, None), Some(10), None)

    
    val request: IRequest = new IRequest(List(arrival1, arrival2), externalResourceList)
    
    assert(!simRun.matchingTasks(request))
  }

  test("matchingTasks, unused task defined") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
      new IResource("R3", 6.2)
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0),
      new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(List(arrival), externalResourceList)
    
    assert(simRun.matchingTasks(request))
  }

}