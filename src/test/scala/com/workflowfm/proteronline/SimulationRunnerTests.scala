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
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
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
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
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
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
    assert(simRun.matchingResources(request) == false)
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
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
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
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
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
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
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
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
    assert(simRun.matchingTasks(request))
  }

}