package com.workflowfm.proter
package server

import org.scalatest.funsuite.AnyFunSuite

import flows.*

class EntityTests extends AnyFunSuite with EntityTestHelper {

  test("Distribution Conversion, correct val constant") {
    val prot = Constant(4)
    val inter: IDistribution = IConstant(4)
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted))
  }

  test("Distribution Conversion, correct val exponential") {
    val prot = Exponential(55)
    val inter: IDistribution = new IExp(55)
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted))
  }

  test("Distribution Conversion, correct val uniform") {
    val prot = Uniform(55,1009)
    val inter: IDistribution = IUniform(55, 1009)
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted))
  }

  test("Distribution Conversion, incorrect val") {
    val prot = Constant(4)
    val inter: IDistribution = IConstant(9)
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted) == false)
  }

  test("Distribution Conversion, incorrect type") {
    val prot = Uniform(4, 7)
    val inter: IDistribution = IConstant(4)
    val converted = inter.toProterDistribution()
    assert(prot.equals(converted) == false)
  }

  test("Distribution Created with Negative Value") {
    assertThrows[IllegalArgumentException] {
      IConstant(-8.3)
    }
  }
   

  test("Task Conversion, correct") {
    val proterCostDist = Uniform(1, 10)
    val interCostDist = IUniform(1, 10)
    val proterDurationDist = Constant(4)
    val interDurationDist = IConstant(4)
    val prot: Flow = FlowTask(Task("Task 1", proterDurationDist).withCost(proterCostDist).withResources(Seq("R1")).withPriority(1))
    val inter: ITask = ITask("Task 1", interDurationDist, interCostDist, require("R1"), 1)
    val converted = inter.flow
    assert(prot == converted)
  }

  test("Task Conversion should work for multiple resources") {
    val proterCostDist = Uniform(1, 10)
    val interCostDist = IUniform(1, 10)
    val proterDurationDist = Constant(4)
    val interDurationDist = IConstant(4)
    val prot: Flow = FlowTask(Task("Task 1", proterDurationDist).withCost(proterCostDist).withResources(Seq("R1", "R2")).withPriority(1))
    val inter: ITask = ITask("Task 1", interDurationDist, interCostDist, require("R1","R2"), 1)
    val converted = inter.flow
    assert(prot == converted)
  }

  test("Task Conversion with mismatched resources should not work") {
    val proterCostDist = Uniform(1, 10)
    val interCostDist = IUniform(1, 10)
    val proterDurationDist = Constant(4)
    val interDurationDist = IConstant(4)
    val prot = Task("Task 1", proterDurationDist).withCost(proterCostDist).withResources(Seq("R1", "R2", "R3")).withPriority(1)
    val inter: ITask = ITask("Task 1", interDurationDist, interCostDist, require("R1","R2","R3","R4"), 1)
    val converted = inter.flow
    assert(!(prot == converted))
  }

  test("Resource Conversion, correct") {
    val inter = IResource("Dave", 1, 10)
    val prot: Resource = new Resource("Dave", 1, 10)
    val converted: Resource = inter.toProterResource()
    assert(prot == converted)
  }

  test("Resource conversion, incorrect") {
    val inter = IResource("Frank", 1, 85)
    val prot: Resource = new Resource("Dave", 1, 10)
    val converted: Resource = inter.toProterResource()
    assert(!(prot == converted))

  }

  test("Resource Invalid Argument thrown on negative value") {
    assertThrows[IllegalArgumentException] {
      IResource("R1", 1, -4.3)
    }
  }

  test("Resource Invalid Argument not thrown on positive value") {
    IResource("R1", 1, 0)
    IResource("R2", 1, 1)
  }

  test("Infinite Arrival Correct limit") {
    val taskList: List[ITask] = List(
      ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
      ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
      ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0),
      ITask("D", IConstant(3.4), IConstant(3.4), require("R2"), 0)
    )
    val flow: IFlow = IThen(taskList)
    val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), None)
    IRequest(None, List(arrival), List(), Some(1000)) //Sets up a correct infinite arrival with time limit
  }

  test("Finite Arrival Correct limit") {
    val taskList: List[ITask] = List(
      ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
      ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
      ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0),
      ITask("D", IConstant(3.4), IConstant(3.4), require("R2"), 0)
    )
    val flow: IFlow = IThen(taskList)
    val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), Some(10))
    IRequest(None, List(arrival), List(), None) //Sets up a correct finite arrival with simulation limit
  }

  test("Infinite Arrival Incorrect limit") {
    val taskList: List[ITask] = List(
      ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
      ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
      ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0),
      ITask("D", IConstant(3.4), IConstant(3.4), require("R2"), 0)
    )
    val flow: IFlow = IThen(taskList)
    val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), None)
    assertThrows[IllegalArgumentException] {
      IRequest(None, List(arrival), List(), None) //Sets up infinite arrival without a time limit but with simulation limit
    }
  }

}

trait EntityTestHelper {
  def require(resources: String*): List[IRequiredResource] =
    resources.map(IRequiredResource(_, 1)).toList
}
