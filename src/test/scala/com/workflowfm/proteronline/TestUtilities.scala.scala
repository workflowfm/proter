package com.workflowfm.proteronline

import com.workflowfm.proter.Task
import com.workflowfm.proter.TaskResource

//import com.workflowfm.proter._

/**
  * These utilities are used in the IntermediateTests for ensuring my intermediate objects can correctly convert into proter objects
  */
class TestUtilities {
    
    /**
      * Used to compare two TaskInstances to ensure they contain the same values
      *
      * @param t1
      * @param t2
      * @return
      */
    def tasksEqual(t1: Task, t2: Task): Boolean = {
        val cost = t1.cost.equals(t2.cost)
        val duration = t1.duration.equals(t2.duration)
        val name = t1.name == t2.name
        val startTime = t1.minStartTime == t2.minStartTime
        val priority = t1.priority == t2.priority
        val resources = t1.resources == t2.resources
        var id = true
        if (t1.id.isDefined && t2.id.isDefined) {
            id = t1.id.get == t2.id.get
        }
        /*
        if (!(cost && duration && name && startTime && priority && resources && id)) {
            println("ISSUE")
            Array(cost, duration, name, startTime, priority, resources, id).foreach(println(_))
        }
        */
        return cost && duration && name && startTime && priority && resources && id
    }

    def resourceEqual(r1: TaskResource, r2: TaskResource): Boolean = {
        r1.costPerTick == r2.costPerTick && r1.name == r2.name
    }
    
}