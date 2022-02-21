package com.workflowfm.proteronline


import org.scalatest.funsuite.AnyFunSuite
import com.workflowfm.proter.Constant
import com.workflowfm.proter.Uniform
import com.workflowfm.proter.Exponential
import com.workflowfm.proter

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

    //Not currently working
    test("Task Conversion, correct") {
        val proterCostDist = new Uniform(1, 10)
        val interCostDist = new IDistribution("U", 1, Some(10))
        val proterDurationDist = new Constant(4)
        val interDurationDist = new IDistribution("C", 4, None)
        val prot = proter.Task("Task 1", proterDurationDist).withCostGenerator(proterCostDist).withResources(Array("R1")).withPriority(1)
        val inter: ITask = new ITask("Task 1", interCostDist, interDurationDist, "R1", 1)
        val converted = inter.toProterTask()
        assert(prot.equals(converted))
    }

    //Not Currently working
    test("Resource Conversion, correct") {
        val inter = IResource("Dave", 10)
        val prot = new proter.TaskResource("Dave", 10)
        val converted = inter.toProterResource()
        assert(prot.equals(converted))
    }
}