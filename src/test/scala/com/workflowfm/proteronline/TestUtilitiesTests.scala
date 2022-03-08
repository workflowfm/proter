package com.workflowfm.proteronline

import org.scalatest.funsuite.AnyFunSuite
import com.workflowfm.proter.Task
import com.workflowfm.proter.Uniform
import com.workflowfm.proter.Constant
import com.workflowfm.proter.Exponential
import com.workflowfm.proter.TaskResource

class TestUtilitiesTests extends AnyFunSuite {

    test("taskEquals should work on same object") {
        val utils = new TestUtilities()
        val costDist = new Uniform(1, 10)
        val durDist = new Uniform(5,15)
        val t1 = Task("A Task", durDist).withCostGenerator(costDist).withResources(Array("R1")).withPriority(1)
        assert(utils.tasksEqual(t1, t1))
    }
    
    test("taskEquals shouldn't work on different tasks") {
        val utils = new TestUtilities()
        val costDist1 = new Uniform(1, 10)
        val durDist1 = new Uniform(5,15)
        val costDist2 = new Constant(7)
        val durDist2 = new Exponential(3)
        val t1 = Task("A Task", durDist1).withCostGenerator(costDist1).withResources(Array("R1")).withPriority(1)
        val t2 = Task("A Task", durDist2).withCostGenerator(costDist2).withResources(Seq("R2")).withPriority(-1)
        assert(!utils.tasksEqual(t1, t2))
    }
    
    test("taskEquals should work on separate tasks with same values") {
        val utils = new TestUtilities()
        val costDist = new Uniform(1, 10)
        val durDist = new Uniform(5,15)
        val t1 = Task("A Task", durDist).withCostGenerator(costDist).withResources(Array("R1")).withPriority(1)
        val t2 = Task("A Task", durDist).withCostGenerator(costDist).withResources(Array("R1")).withPriority(1)
        assert(utils.tasksEqual(t1, t2))
    }

    test("taskEquals should work on separate tasks with same values (including separate distributions)") {
        val utils = new TestUtilities()
        val costDist1 = new Uniform(1, 10)
        val durDist1 = new Uniform(5,15)
        val costDist2 = new Uniform(1, 10)
        val durDist2 = new Uniform(5,15)
        val t1 = Task("A Task", durDist1).withCostGenerator(costDist1).withResources(Array("R1")).withPriority(1)
        val t2 = Task("A Task", durDist2).withCostGenerator(costDist2).withResources(Array("R1")).withPriority(1)
        assert(utils.tasksEqual(t1, t2))
    }

    test("taskEquals should work on separate tasks with same values (regardless of Seq Type)") {
        val utils = new TestUtilities()
        val costDist1 = new Uniform(1, 10)
        val durDist1 = new Uniform(5,15)
        val costDist2 = new Uniform(1, 10)
        val durDist2 = new Uniform(5,15)
        val t1 = Task("A Task", durDist1).withCostGenerator(costDist1).withResources(Seq("R1")).withPriority(1)
        val t2 = Task("A Task", durDist2).withCostGenerator(costDist2).withResources(Array("R1")).withPriority(1)
        assert(utils.tasksEqual(t1, t2))
    }

    test("resourceEquals should work on same object") {
        val utils = new TestUtilities()
        val prot: TaskResource = new TaskResource("Dave", 10)
        assert(utils.resourceEqual(prot, prot))
    }

    test("resourceEquals should work on identical objects") {
        val utils = new TestUtilities()
        val r1 = new TaskResource("Dave", 10)
        val r2 = new TaskResource("Dave", 10)
        assert(utils.resourceEqual(r1, r2))
    }

    test("resourceEquals shouldn't work on different resources") {
        val utils = new TestUtilities()
        val r1 = new TaskResource("Dave", 10)
        val r2 = new TaskResource("Frank", 982)
        assert(!utils.resourceEqual(r1, r2))
    }

}