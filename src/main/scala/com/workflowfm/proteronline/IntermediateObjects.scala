package com.workflowfm.proteronline

import com.workflowfm.proter
import com.workflowfm.proter.metrics._

/**
  * This file contains a number of case classes that are used as intermediate objects used in the process
  * of converting the JSON sent to this system to the objects and structure required by Proter to run simulations
  */


/**
  * IRequest is the highest level object for encoding a request for proter execution
  *
  * @param arrival Defines how simulations arrive
  * @param resources Contains the definitions of resources that are referenced in the arrival
  */
case class IRequest(arrivals: List[IArrival], resources: List[IResource])


/**
  * Defines how often simulations are to start
  *
  * @param simulation The simulation that is to be started
  * @param infinite If the arrival is infinite then this should be true
  * @param rate The rate at which new simulation should be started
  * @param simulationLimit If infinite is false then this determines how many simulations should be run in total before stopping
  * @param timeLimit If infinite is true then this determines for how long the simulation should be run before stopping
  */
case class IArrival(simulation: ISimulation, infinite: Boolean, rate: IDistribution, simulationLimit: Option[Int], timeLimit: Option[Int]) {
  if (infinite) {
    if (!timeLimit.isDefined) {
      throw new IllegalArgumentException("If the Arrival is infinite then a time limit must be defined")
    }
  } else {
    if (!simulationLimit.isDefined) {
      throw new IllegalArgumentException("If the Arrival is not infinite then a simulation limit must be defined")
    }
  }
}


/**
  * Defines a Simulation
  *
  * @param name The name of the simulation
  * @param flow The Flow that describes the tasks that make up the simulation and the order of their execution
  */
case class ISimulation(name: String, flow: IFlow)


/**
  * A flow describes a number of tasks as well as the order in which they are executed
  *
  * @param tasks A list of tasks
  * @param ordering The order in which they occur, this is encoded as a string containing the names of the tasks separated with "->" to determine order
  */
case class IFlow(tasks: List[ITask], ordering: String)


/**
  * This defines a task with all its details
  *
  * @param name The name of the task
  * @param duration A distribution describing the duration of the task
  * @param cost A distribution describing the cost of the task
  * @param resources A string containing the names of the resources the task requires (if multiple they should be separated with a comma)
  * @param priority The priority of the task (from -2 being the lowest to 2 being the highest)
  */
case class ITask(name: String, duration: IDistribution, cost: IDistribution, resources: String, priority: Int) {
  if (priority > 2 || priority < -2) {
    throw new IllegalArgumentException
  }
  /**
    * This method converts this intermediate task object into a Proter Task
    *
    * @return A proter task
    */
  def toProterTask(): proter.Task = {
    proter.Task(this.name, this.duration.toProterDistribution())
            .withCostGenerator(this.cost.toProterDistribution())
            .withResources(this.resources.split(","))
            .withPriority(this.priority)
  }
}


/**
  * This defines a resource
  *
  * @param name The name of the resource
  * @param costPerTick The Cost per time unit for the resource
  */
case class IResource(name: String, costPerTick: Double) {
  if (costPerTick < 0) {
    throw new IllegalArgumentException("Resource Cost cannot be less than 0")
  }
    /**
      * Converts this intermediate object to a Proter Resource
      *
      * @return
      */
  def toProterResource(): proter.TaskResource = {
    new proter.TaskResource(this.name, this.costPerTick)
  }

}


/**
  * This defines a distribution which can be sampled for values
  *
  * @param distType The type of distribution (Constant, Exponential, Uniform), should pass the first letter uppercase
  * @param value1 For constant distributions this is the value always returned, for exponential this is the mean, for uniform this is the start of the range
  * @param value2 For constant and Exponential this should be None, for Uniform this is the end of the range
  */
case class IDistribution(distType: String, value1: Double, value2: Option[Double]) {
  if (!Array("C", "E", "U").contains(distType)) {
    throw new IllegalArgumentException("Invalid Distribution Type: Was \'" + this.distType + "\' can only be C, E or U")
  }
  if (distType == "U" && value2.isEmpty) {
    throw new IllegalArgumentException("Uniform Distributions must have two values")
  }

  def toProterDistribution(): proter.Distribution = {
    if (this.distType == "C") {
      new proter.Constant(this.value1);
    } else if (this.distType == "E") {
      new proter.Exponential(this.value1);
    } else {
      new proter.Uniform(this.value1, this.value2.get)
    }
  }
}

/**
  * This case class is used to hold the results that should be returned to the user
  *
  * @param start The timestamp at which the coordinator started
  * @param end The timestamp as which the coordinator stopped
  * @param resources The statistics about the resources
  * @param simulations The statistics about the simulations
  * @param tasks The statistics about tasks
  */
case class Results(start: Long, end: Long, resources: List[ResourceMetrics], simulations: List[SimulationMetrics], tasks: List[TaskMetrics])
