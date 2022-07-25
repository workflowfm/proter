package com.workflowfm.proter
package server

import org.scalatest.funsuite.AnyFunSuite

class IntermediateTests extends AnyFunSuite {

  test("Distribution Conversion, correct val constant") {
    val prot = new Constant(4)
    val inter: IDistribution = new IDistribution("C", 4, None)
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted))
  }

  test("Distribution Conversion, correct val exponential") {
    val prot = new Exponential(55)
    val inter: IDistribution = new IDistribution("E", 55, None)
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted))
  }

  test("Distribution Conversion, correct val uniform") {
    val prot = new Uniform(55,1009)
    val inter: IDistribution = new IDistribution("U", 55, Some(1009))
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted))
  }

  test("Distribution Conversion, incorrect val") {
    val prot = new Constant(4)
    val inter: IDistribution = new IDistribution("C", 9, None)
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted) == false)
  }

  test("Distribution Conversion, incorrect type") {
    val prot = new Uniform(4, 7)
    val inter: IDistribution = new IDistribution("C", 4, None)
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted) == false)
  }

  test("Distribution creation with invalid type") {
    assertThrows[IllegalArgumentException] {
      new IDistribution("F", 5, None)
    }
  }

  test("Uniform Distribution created without second value") {
    assertThrows[IllegalArgumentException] {
      new IDistribution("U", 5, None)
    }
  }
  /*
   test("Distribution Created with Negative Value") {
   assertThrows[IllegalArgumentException] {
   new IDistribution("C", -8.3, None)
   }
   }
   */

  test("Task Conversion, correct") {
    val utils = new TestUtilities()
    val proterCostDist = new Uniform(1, 10)
    val interCostDist = new IDistribution("U", 1, Some(10))
    val proterDurationDist = new Constant(4)
    val interDurationDist = new IDistribution("C", 4, None)
    val prot = Task("Task 1", proterDurationDist).withCost(proterCostDist).withResources(Seq("R1")).withPriority(1)
    val inter: ITask = new ITask("Task 1", interDurationDist, interCostDist, "R1", 1)
    val converted = inter.toProterTask()
    assert(utils.tasksEqual(prot, converted))
  }

  test("Task Conversion should work for multiple resources") {
    val utils = new TestUtilities()
    val proterCostDist = new Uniform(1, 10)
    val interCostDist = new IDistribution("U", 1, Some(10))
    val proterDurationDist = new Constant(4)
    val interDurationDist = new IDistribution("C", 4, None)
    val prot = Task("Task 1", proterDurationDist).withCost(proterCostDist).withResources(Seq("R1", "R2")).withPriority(1)
    val inter: ITask = new ITask("Task 1", interDurationDist, interCostDist, "R1,R2", 1)
    val converted = inter.toProterTask()
    assert(utils.tasksEqual(prot, converted))
  }

  test("Task Conversion with mismatched resources should not work") {
    val utils = new TestUtilities()
    val proterCostDist = new Uniform(1, 10)
    val interCostDist = new IDistribution("U", 1, Some(10))
    val proterDurationDist = new Constant(4)
    val interDurationDist = new IDistribution("C", 4, None)
    val prot = Task("Task 1", proterDurationDist).withCost(proterCostDist).withResources(Seq("R1", "R2", "R3")).withPriority(1)
    val inter: ITask = new ITask("Task 1", interDurationDist, interCostDist, "R1,R2,R3,R4", 1)
    val converted = inter.toProterTask()
    assert(!utils.tasksEqual(prot, converted))
  }

  test("Resource Conversion, correct") {
    val utils = new TestUtilities()
    val inter = new IResource("Dave", 10)
    val prot: Resource = new Resource("Dave", 1, 10)
    val converted: Resource = inter.toProterResource()
    assert(utils.resourceEqual(prot, converted))
  }

  test("Resource conversion, incorrect") {
    val utils = new TestUtilities()
    val inter = new IResource("Frank", 85)
    val prot: Resource = new Resource("Dave", 1, 10)
    val converted: Resource = inter.toProterResource()
    assert(!utils.resourceEqual(prot, converted))

  }

  test("Resource Invalid Argument thrown on negative value") {
    assertThrows[IllegalArgumentException] {
      new IResource("R1", -4.3)
    }
  }

  test("Resource Invalid Argument not thrown on positive value") {
    new IResource("R1", 0)
    new IResource("R2", 1)
  }

  test("Task invalid priority > valid") {
    val interCostDist = new IDistribution("U", 1, Some(10))
    val interDurationDist = new IDistribution("C", 4, None)

    assertThrows[IllegalArgumentException] {
      new ITask("Dave", interDurationDist, interCostDist, "R1", 7)
    }
  }

  test("Task invalid priority < valid") {
    val interCostDist = new IDistribution("U", 1, Some(10))
    val interDurationDist = new IDistribution("C", 4, None)

    assertThrows[IllegalArgumentException] {
      new ITask("Dave", interDurationDist, interCostDist, "R1", -3)
    }
  }

  test("Infinite Arrival Correct limit") {
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0),
      new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    new IArrival(sim, true, new IDistribution("C", 4.3, None), None, Some(1000)) //Sets up a correct infinite arrival with time limit
  }

  test("Finite Arrival Correct limit") {
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0),
      new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None) //Sets up a correct finite arrival with simulation limit
  }

  test("Infinite Arrival Incorrect limit") {
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0),
      new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    assertThrows[IllegalArgumentException] {
      new IArrival(sim, true, new IDistribution("C", 4.3, None), Some(1000), None) //Sets up infinite arrival without a time limit but with simulation limit
    }
  }

  test("Finite Arrival Incorrect limit") {
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0),
      new ITask("D", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    assertThrows[IllegalArgumentException] {
      new IArrival(sim, false, new IDistribution("C", 4.3, None), None, Some(10)) //Sets up finite arrival without a simulation limit but with time limit
    }
  }

}
