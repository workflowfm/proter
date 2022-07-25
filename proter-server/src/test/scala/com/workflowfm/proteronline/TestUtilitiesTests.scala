package com.workflowfm.proter
package server

import org.scalatest.funsuite.AnyFunSuite

class TestUtilitiesTests extends AnyFunSuite {

  test("taskEquals should work on same object") {
    val utils = new TestUtilities()
    val costDist = new Uniform(1, 10)
    val durDist = new Uniform(5,15)
    val t1 = Task("A Task", durDist).withCost(costDist).withResources(Seq("R1")).withPriority(1)
    assert(utils.tasksEqual(t1, t1))
  }

  test("taskEquals shouldn't work on different tasks") {
    val utils = new TestUtilities()
    val costDist1 = new Uniform(1, 10)
    val durDist1 = new Uniform(5,15)
    val costDist2 = new Constant(7)
    val durDist2 = new Exponential(3)
    val t1 = Task("A Task", durDist1).withCost(costDist1).withResources(Seq("R1")).withPriority(1)
    val t2 = Task("A Task", durDist2).withCost(costDist2).withResources(Seq("R2")).withPriority(-1)
    assert(!utils.tasksEqual(t1, t2))
  }

  test("taskEquals should work on separate tasks with same values") {
    val utils = new TestUtilities()
    val costDist = new Uniform(1, 10)
    val durDist = new Uniform(5,15)
    val t1 = Task("A Task", durDist).withCost(costDist).withResources(Seq("R1")).withPriority(1)
    val t2 = Task("A Task", durDist).withCost(costDist).withResources(Seq("R1")).withPriority(1)
    assert(utils.tasksEqual(t1, t2))
  }

  test("taskEquals should work on separate tasks with same values (including separate distributions)") {
    val utils = new TestUtilities()
    val costDist1 = new Uniform(1, 10)
    val durDist1 = new Uniform(5,15)
    val costDist2 = new Uniform(1, 10)
    val durDist2 = new Uniform(5,15)
    val t1 = Task("A Task", durDist1).withCost(costDist1).withResources(Seq("R1")).withPriority(1)
    val t2 = Task("A Task", durDist2).withCost(costDist2).withResources(Seq("R1")).withPriority(1)
    assert(utils.tasksEqual(t1, t2))
  }

  test("taskEquals should work on separate tasks with same values (regardless of Seq Type)") {
    val utils = new TestUtilities()
    val costDist1 = new Uniform(1, 10)
    val durDist1 = new Uniform(5,15)
    val costDist2 = new Uniform(1, 10)
    val durDist2 = new Uniform(5,15)
    val t1 = Task("A Task", durDist1).withCost(costDist1).withResources(Seq("R1")).withPriority(1)
    val t2 = Task("A Task", durDist2).withCost(costDist2).withResources(Seq("R1")).withPriority(1)
    assert(utils.tasksEqual(t1, t2))
  }

  test("resourceEquals should work on same object") {
    val utils = new TestUtilities()
    val prot: Resource = new Resource("Dave", 1, 10)
    assert(utils.resourceEqual(prot, prot))
  }

  test("resourceEquals should work on identical objects") {
    val utils = new TestUtilities()
    val r1 = new Resource("Dave", 1, 10)
    val r2 = new Resource("Dave", 1, 10)
    assert(utils.resourceEqual(r1, r2))
  }

  test("resourceEquals shouldn't work on different resources") {
    val utils = new TestUtilities()
    val r1 = new Resource("Dave", 1, 10)
    val r2 = new Resource("Frank", 1, 982)
    assert(!utils.resourceEqual(r1, r2))
  }

}
