package com.workflowfm.proter
package server

import flows.{ Flow, FlowTask }

/**
  * This file contains a number of case classes that are used as intermediate objects used in the
  * process of converting the JSON sent to this system to the objects and structure required by
  * Proter to run simulations
  */

/**
  * IRequest is the highest level object for encoding a request for proter execution
  *
  * @param arrival
  *   Defines how simulations arrive
  * @param resources
  *   Contains the definitions of resources that are referenced in the arrival
  */
final case class IRequest(
    start: Option[Long],
    arrivals: List[IArrival],
    resources: List[IResource],
    timeLimit: Option[Long]
) {
  if (timeLimit.isEmpty && arrivals.exists(_.infinite))
    throw new IllegalArgumentException("Infinite arrivals require a time limit.")

}

/**
  * Defines how often simulations are to start
  *
  * @param simulation
  *   The simulation that is to be started
  * @param infinite
  *   If the arrival is infinite then this should be true
  * @param rate
  *   The rate at which new simulation should be started
  * @param simulationLimit
  *   If infinite is false then this determines how many simulations should be run in total before
  *   stopping
  * @param timeLimit
  *   If infinite is true then this determines for how long the simulation should be run before
  *   stopping
  */
final case class IArrival(
    name: String,
    flow: IFlow,
    start: Option[Long],
    rate: Option[IDistribution],
    limit: Option[Int]
) {

  def infinite: Boolean =
    rate.isDefined && limit.map(_ <= 0).getOrElse(true)
}

/**
  * A flow describes a number of tasks as well as the order in which they are executed
  *
  * @param tasks
  *   A list of tasks
  * @param ordering
  *   The order in which they occur, this is encoded as a string containing the names of the tasks
  *   separated with "->" to determine order
  */
sealed trait IFlow {
  def flow: Flow
  def resourceNames: Set[String]
}

final case class IThen(args: List[IFlow]) extends IFlow {
  if (args.isEmpty)
    throw new IllegalArgumentException("Empty IThen arguments.")

  override lazy val flow: Flow = Flow.seq(args.map(_.flow))
  override def resourceNames: Set[String] = args.flatMap(_.resourceNames).toSet
}

final case class IPar(args: List[IFlow]) extends IFlow {
  if (args.isEmpty)
    throw new IllegalArgumentException("Empty IPar arguments.")

  override lazy val flow: Flow = Flow.par(args.map(_.flow))
  override def resourceNames: Set[String] = args.flatMap(_.resourceNames).toSet
}

/**
  * This defines a task with all its details
  *
  * @param name
  *   The name of the task
  * @param duration
  *   A distribution describing the duration of the task
  * @param cost
  *   A distribution describing the cost of the task
  * @param resources
  *   A string containing the names of the resources the task requires (if multiple they should be
  *   separated with a comma)
  * @param priority
  *   The priority of the task (from -2 being the lowest to 2 being the highest)
  */
final case class ITask(
    name: String,
    duration: IDistribution,
    cost: IDistribution,
    resources: List[IRequiredResource],
    priority: Int
) extends IFlow {

  /**
    * This method converts this intermediate task object into a Proter Task
    *
    * @return
    *   A proter task
    */
  lazy val task: Task = {
    Task(this.name, this.duration.toProterDistribution())
      .withCost(this.cost.toProterDistribution())
      .withResourceQuantities(Map() ++ this.resources.map(r => r.resource -> r.quantity))
      .withPriority(this.priority)
  }

  override lazy val flow: Flow = FlowTask(task)
  override def resourceNames: Set[String] = resources.map(_.resource).toSet

}

final case class IRequiredResource(resource: String, quantity: Int) {
  if (quantity <= 0)
    throw new IllegalArgumentException(
      s"Required resource quantity of [$resource] must be non-negative: $quantity"
    )
}

/**
  * This defines a resource
  *
  * @param name
  *   The name of the resource
  * @param costPerTick
  *   The Cost per time unit for the resource
  */
case class IResource(name: String, capacity: Int, costPerTick: Double) {
  if (costPerTick < 0)
    throw new IllegalArgumentException(
      s"Resource Cost of [$name] cannot be less than 0: $costPerTick"
    )
  if (capacity < 1)
    throw new IllegalArgumentException(
      s"Resource Capacity of [$name] cannot be less than 1: $capacity"
    )

  /**
    * Converts this intermediate object to a Proter Resource
    *
    * @return
    */
  def toProterResource(): Resource = {
    new Resource(this.name, this.capacity, this.costPerTick)
  }

}

sealed trait IDistribution {
  def toProterDistribution(): Distribution
}

final case class IConstant(value: Double) extends IDistribution {
  if (value < 0.0) {
    throw new IllegalArgumentException(s"Constant value must be non-negative: $value")
  }

  override def toProterDistribution(): Distribution =
    Constant(value)
}

final case class IExp(value: Double) extends IDistribution {
  if (value < 0.0) {
    throw new IllegalArgumentException(s"Exponential value must be non-negative: $value")
  }

  override def toProterDistribution(): Distribution =
    Exponential(value)
}

final case class IUniform(from: Double, to: Double) extends IDistribution {
  if (from < 0.0 || to < 0.0) {
    throw new IllegalArgumentException(s"Uniform range must be non-negative: $from-$to")
  }
  if (from > to) {
    throw new IllegalArgumentException(s"Uniform range must be valid: $from<=$to")
  }

  override def toProterDistribution(): Distribution =
    Uniform(from, to)
}

import io.circe.*
import io.circe.generic.semiauto.*

object Entities {
  given Decoder[IConstant] = deriveDecoder[IConstant]
  given Decoder[IExp] = deriveDecoder[IExp]
  given Decoder[IUniform] = deriveDecoder[IUniform]
  given Decoder[IDistribution] = deriveDecoder[IDistribution]
  given Decoder[IRequiredResource] = deriveDecoder[IRequiredResource]
  given Decoder[IThen] = deriveDecoder[IThen]
  given Decoder[IPar] = deriveDecoder[IPar]
  given Decoder[ITask] = deriveDecoder[ITask]
  given Decoder[IFlow] = deriveDecoder[IFlow]
  given Decoder[IArrival] = deriveDecoder[IArrival]
  given Decoder[IResource] = deriveDecoder[IResource]
  given Decoder[IRequest] = deriveDecoder[IRequest]

  given Encoder[IConstant] = deriveEncoder[IConstant]
  given Encoder[IExp] = deriveEncoder[IExp]
  given Encoder[IUniform] = deriveEncoder[IUniform]
  given Encoder[IDistribution] = deriveEncoder[IDistribution]
  given Encoder[IRequiredResource] = deriveEncoder[IRequiredResource]
  given Encoder[IThen] = deriveEncoder[IThen]
  given Encoder[IPar] = deriveEncoder[IPar]
  given Encoder[ITask] = deriveEncoder[ITask]
  given Encoder[IFlow] = deriveEncoder[IFlow]
  given Encoder[IArrival] = deriveEncoder[IArrival]
  given Encoder[IResource] = deriveEncoder[IResource]
  given Encoder[IRequest] = deriveEncoder[IRequest]

}
