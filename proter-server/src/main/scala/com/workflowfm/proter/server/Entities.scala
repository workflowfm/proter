package com.workflowfm.proter
package server

import flows.{ Flow, FlowTask }

/**
  * This file contains a number of case classes that are used as intermediate objects used in the
  * process of converting the JSON sent to this system to the objects and structure required by
  * Proter to run simulations.
  */

/**
  * The highest level object encoding a request for simulation.
  *
  * @param start
  *   An optional starting timestamp (default: 0).
  * @param arrival
  *   The cases and arrivals to simulate.
  * @param resources
  *   The resources to use in the simulation.
  * @param timeLimit
  *   An optional time limit for the simulation. Must be defined if an infinite arrival is provided.
  */
final case class IRequest(
    start: Option[Long],
    arrivals: List[IArrival],
    resources: List[IResource],
    timeLimit: Option[Long]
) {
  if (timeLimit.isEmpty && arrivals.exists(_.infinite))
    throw new IllegalArgumentException("Infinite arrivals require a time limit.")

  /**
    * Validates that the resources referenced in the flow are defined in the resource list.
    *
    * @return
    *   true if resource references are valid.
    */
  val matchingResources: Boolean = {
    val definedResources: Set[String] = resources.map(_.name).toSet
    val referencedResources: Set[String] =
      arrivals.flatMap(arrival => arrival.flow.resourceNames).toSet
    referencedResources.subsetOf(definedResources)
  }

  if (!matchingResources)
    throw new IllegalArgumentException("Flow resources cannot be matched to the resource list.")
}

/**
  * Defines a [[flows.Flow Flow]]-based case or arrival pattern of cases.
  *
  * @param name
  *   A unique name to use for the case(s).
  * @param flow
  *   The flow to run for the case(s).
  * @param start
  *   An optional start time for the case(s) (default: simulation starting time).
  * @param rate
  *   An optional distribution describing the duration until the next arrival (default: never).
  * @param limit
  *   An optional limit to the number of cases to generate (default: infinite).
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
  * A structural representation of a [[flows.Flow Flow]].
  */
sealed trait IFlow {
  /** 
    * Retrieve the actual [[flows.Flow Flow]].
    * 
    * @return
    *   The flow.
    */
  def flow: Flow

  /** 
    * The set of names of [[Resource]]s used in the flow.
    * 
    * This is used for validation. 
    * 
    * @return
    *   A set of [[Resource]] names.
    */
  def resourceNames: Set[String]
}

/**
  * A structural representation of a sequential [[flows.Then Then]] flow.
  * 
  * @param args
  *   The sub-flows to run in sequence. Must be non-empty.
  */
final case class IThen(args: List[IFlow]) extends IFlow {
  if (args.isEmpty)
    throw new IllegalArgumentException("Empty IThen arguments.")

  override lazy val flow: Flow = Flow.seq(args.map(_.flow))
  override def resourceNames: Set[String] = args.flatMap(_.resourceNames).toSet
}

/**
  * A structural representation of a parallel [[flows.And And]] flow.
  * 
  * @param args
  *   The sub-flows to run in parallel. Must be non-empty.
  */
final case class IPar(args: List[IFlow]) extends IFlow {
  if (args.isEmpty)
    throw new IllegalArgumentException("Empty IPar arguments.")

  override lazy val flow: Flow = Flow.par(args.map(_.flow))
  override def resourceNames: Set[String] = args.flatMap(_.resourceNames).toSet
}

/**
  * An intermediate, serializable abstraction of a [[Task]].
  *
  * @param name
  *   The name of the task.
  * @param duration
  *   A distribution describing the duration of the task.
  * @param cost
  *   A distribution describing the cost of the task.
  * @param resources
  *   A list of required resources.
  * @param priority
  *   A numerical priority for the task.
  */
final case class ITask(
    name: String,
    duration: IDistribution,
    cost: IDistribution,
    resources: List[IRequiredResource],
    priority: Int
) extends IFlow {

  /**
    * Converts the intermediate representation to the actual [[Task]].
    *
    * @return
    *   The [[Task]].
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


/**
  * An intermediate, serializable abstraction of a [[Resource]] requirement for a [[Task]].
  * 
  * @param resource
  *   The name of the required resource.
  * @param quantity
  *   The amount of resource capacity required.
  */
final case class IRequiredResource(resource: String, quantity: Int) {
  if (quantity <= 0)
    throw new IllegalArgumentException(
      s"Required resource quantity of [$resource] must be non-negative: $quantity"
    )
}

/**
  * An intermediate representation of a [[Resource]].
  *
  * @param name
  *   The name of the resource.
  * @param capacity
  *   The units of capacity of the resource. Must be greater than 0.
  * @param costPerTick
  *   The cost per time unit for the resource. Must be non-negative.
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
    * Converts this intermediate object to the actual [[Resource]].
    *
    * @return
    *   The [[Resource]].
    */
  def toProterResource(): Resource = {
    new Resource(this.name, this.capacity, this.costPerTick)
  }

}

/**
  * An intermediate, serializable representation of a [[Distribution]].
  */
sealed trait IDistribution {
  /**
    * Converts this intermediate object to the actual [[Distribution]].
    *
    * @return
    *   The [[Distribution]].
    */
  def toProterDistribution(): Distribution
}

/**
  * An intermediate, serializable representation of a [[Constant]] distribution.
  * 
  * @param value
  *   The constant value.
  */
final case class IConstant(value: Double) extends IDistribution {
  if (value < 0.0) {
    throw new IllegalArgumentException(s"Constant value must be non-negative: $value")
  }

  override def toProterDistribution(): Distribution =
    Constant(value)
}

/**
  * An intermediate, serializable representation of a [[Exponential]] distribution.
  * 
  * @param value
  *   The mean value.
  */
final case class IExp(value: Double) extends IDistribution {
  if (value < 0.0) {
    throw new IllegalArgumentException(s"Exponential value must be non-negative: $value")
  }

  override def toProterDistribution(): Distribution =
    Exponential(value)
}

/**
  * An intermediate, serializable representation of a [[Uniform]] distribution.
  * 
  * @param from
  *   The minimum value.
  * @param to
  *   The maximum value.
  */
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
