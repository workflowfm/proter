package com.workflowfm.simulator

import akka.actor._
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Promise
import scala.concurrent.duration._
import com.workflowfm.simulator.metrics._
import java.util.UUID

/**
  * A task to be performed in virtual time.
  * @note We expect that tasks are only generated through a [[TaskGenerator]].
  *
  * The id and samples of the parameters are generated by the [[Coordinator]].
  * The duration can be specified through random sampling in a [[ValueGenerator]].
  * We store the estimated value as well as the actual sampled value.
  *
  * @param id A unique id that separates this task from any other task.
  *           A workflow may spawn multiple tasks with the same name, so this is necessary.
  * @param name A descriptive name.
  * @param simulation A unique name of the simulation this task belongs to.
  * @param actor A reference to the [[SimulationActor]] that spawned this task.
  * @param created The timestamp when this task was created.
  * @param resources The names of the [[TaskResource]]s required by this task.
  * @param duration The actual duration of the task.
  * @param estimatedDuration The estimated duration of the task.
  * @param initialCost A one-off cost of performing this task.
  * @param interrupt The number of times the task can be interrupted in favour of
  *                  a more urgent one. This parameter is not currently being used.
  * @param priority The explicit priority of the task.
  */
class Task(
    val id: UUID,
    val name: String,
    val simulation: String,
    val actor: ActorRef,
    val created: Long,
    val resources: Seq[String],
    val duration: Long,
    val estimatedDuration: Long,
    val initialCost: Long,
    val interrupt: Int = Int.MaxValue,
    val priority: Task.Priority = Task.Medium
) extends Ordered[Task] {

  /**
    * The total cost of the task.
    */
  var cost: Long = initialCost

  /**
    * Adds more costs to the task.
    * [[Coordinator.addTask]] uses this to add the cost of the resources used.
    *
    * @param extra The cost to add.
    */
  def addCost(extra: Long) = cost += extra

  /**
    * Finds the soonest this task can start.
    * This is based on the availability of the required resources.
    * We calculate the minimum possible time when we expect all resources will be free.
    * We use [[TaskResource.nextAvailableTimestamp]] as the (estimated) next available time of
    * each resource.
    *
    * @param currentTime The current timestamp.
    * @param resourceMap A map of all available [[TaskResource]]s
    * @return The (estimated) earliest timestamp when all resources are available.
    */
  def nextPossibleStart(currentTime: Long, resourceMap: collection.Map[String, TaskResource]) = {
    (currentTime /: resources) {
      case (i, rN) =>
        resourceMap.get(rN) match {
          case None => throw new RuntimeException(s"Resource $rN not found!")
          case Some(r) => Math.max(i, r.nextAvailableTimestamp(currentTime))
        }
    }
  }

  /**
    * The actual [[TaskResource]]s required.
    * Retrieves the actual objects (instead of just their names) from a map.
    *
    * @param resourceMap The map of available [[TaskResource]]s
    * @return The [[TaskResource]]s required for this task.
    */
  def taskResources(resourceMap: collection.Map[String, TaskResource]) =
    resources flatMap (resourceMap.get(_))

  /**
    * Ordering of tasks.
    * This method essentially dictates the priority of tasks in the queue, highest to lowest.
    *
    * Uses the following criteria, in order:
    *
    *   1. '''Priority''': Putting this parameter first makes it a very strong influencer of priority.
    *   A task with higher explicit Priority will always be executed before any other task with lower
    *   priority, even if, for example, the latter has been queueing for a long time.
    *   1. '''Age''': Tasks who were created earlier take priority, in an attempt to minimizw waiting times.
    *   1. '''Resources''': Tasks that involve a higher number of resources take priority.
    *   The intuition is that it is generally harder to achieve a state where more resources
    *   are available at the same time, so we do not want to postpone the task.
    *   1. '''Estimated duration''': Tasks that require more time are prioritized.
    *   1. '''Interrupt''': Inflexible tasks that can not be interrupted are prioritized.
    *   1. Tasks are considered of equal priority at this point, so we use their id as a
    *   deterministic ordering.
    *
    * @note You can change this order by subclassing [[Task]] and creating a corresponding subclass of
    * [[TaskGenerator]].
    *
    * @param that The task to compare to.
    * @return A comparative measure (lower means higher priority).
    */
  def compare(that: Task) = {
    lazy val cPriority = that.priority.compare(this.priority)
    lazy val cResources = that.resources.size.compare(this.resources.size)
    lazy val cAge = this.created.compare(that.created)
    lazy val cDuration = that.estimatedDuration.compare(this.estimatedDuration)
    lazy val cInterrupt = this.interrupt.compare(that.interrupt)
    lazy val cID = this.id.compareTo(that.id)

    if (cPriority != 0) cPriority
    else if (cAge != 0) cAge
    else if (cResources != 0) cResources
    else if (cDuration != 0) cDuration
    else if (cInterrupt != 0) cInterrupt
    else cID
  }

  override def toString = {
    val res = resources.mkString(",")
    s"Task($id,$name,$simulation,$actor,c$created,[$res],d$duration($estimatedDuration),c$initialCost,i$interrupt,$priority)"
  }
}

object Task {

  /**
    * Explicit task priority values.
    * Uses integers for ordering priorities.
    *
    * 5 priority levels are provided: [[VeryLow]], [[Low]], [[Medium]], [[High]], and [[Highest]].
    */
  sealed trait Priority extends Ordered[Priority] {
    /**
      * The integer value of the priority.
      */
    def value: Int
    /**
      * Compares priorities based on their integer value.
      *
      * @param that Priority to compare to.
      * @return The relative comparison value.
      */
    def compare(that: Priority) = this.value - that.value
  }
  case object Highest extends Priority { val value = 5 }
  case object High extends Priority { val value = 4 }
  case object Medium extends Priority { val value = 3 }
  case object Low extends Priority { val value = 2 }
  case object VeryLow extends Priority { val value = 1 }
}

/**
  * A generator/factory of a [[Task]].
  * Uses [[ValueGenerator]]s for the duration and cost. These may be based on random variables.
  * The actual values are sampled at task creation time.
  *
  * Although typically only 1 task is generated, in principle it can generate multiple
  * tasks with the same properties. However, each task generated will have different samples
  * of the [[ValueGenerator]]s.
  *
  * @param name The name of the task.
  * @param simulation The name of the simulation this task belongs to.
  * @param duration The generator of the duration.
  * @param cost The generator of the cost.
  * @param interrupt The [[Task.interrupt]] property of the task.
  * @param priority The explicit priority of the task.
  * @param createTime A custom creation time. Negative values corresponds to the current time.
  */
case class TaskGenerator(
    name: String,
    id: UUID,
    simulation: String,
    duration: ValueGenerator[Long],
    cost: ValueGenerator[Long],
    resources: Seq[String] = Seq(),
    interrupt: Int = (-1),
    priority: Task.Priority = Task.Medium,
    createTime: Long = (-1)
) {
  //todo clean this up
  def this(name: String, simulation: String, duration: ValueGenerator[Long], cost: ValueGenerator[Long]) = 
    this(name, UUID.randomUUID(), simulation, duration, cost)
  def this(name: String, simulation: String, duration: ValueGenerator[Long], cost: ValueGenerator[Long], resources: Seq[String]) =
    this(name, UUID.randomUUID(), simulation, duration, cost, resources)
  def this(name: String, simulation: String, duration: ValueGenerator[Long], cost: ValueGenerator[Long], resources: Seq[String], interrupt: Int) =
    this(name, UUID.randomUUID(), simulation, duration, cost, resources, interrupt)
  def this(name: String, simulation: String, duration: ValueGenerator[Long], cost: ValueGenerator[Long], resources: Seq[String], interrupt: Int, priority: Task.Priority) =
    this(name, UUID.randomUUID(), simulation, duration, cost, resources, interrupt, priority)
  def this(name: String, simulation: String, duration: ValueGenerator[Long], cost: ValueGenerator[Long], resources: Seq[String], interrupt: Int, priority: Task.Priority, createTime: Long) =
    this(name, UUID.randomUUID(), simulation, duration, cost, resources, interrupt, priority, createTime)

  /**
    * Generate a [[Task]].
    * As a case class, it can be easily manipulated dynamically to alter its values.
    *
    * @param id The unique ID to give to the task.
    * @param currentTime The current time.
    * @param actor A reference to the corresponding [[SimulationActor]] for the task.
    * @param resources The names of the [[TaskResource]]s that will be used by the task.
    * @return The generated [[Task]].
    */
  def create(currentTime: Long, actor: ActorRef) = {
    val creation = if (createTime >= 0) createTime else currentTime

    new Task(
      id,
      name,
      simulation,
      actor,
      creation,
      resources,
      duration.get,
      duration.estimate,
      cost.get,
      interrupt,
      priority
    )
  }
  
  /**
    * Updae the ID to use
    *
    * @param i The new ID.
    * @return An updated [[TaskGenerator]].
    */
  def withID(i: UUID) = copy(id = i)

  /**
    * Update the priority to use.
    *
    * @param p The new priority.
    * @return An updated [[TaskGenerator]].
    */
  def withPriority(p: Task.Priority) = copy(priority = p)
  /**
    * Update the interrupt to use.
    *
    * @param int The new interrupt.
    * @return An updated [[TaskGenerator]].
    */
  def withInterrupt(int: Int) = copy(interrupt = int)
  /**
    * Update the duration [[ValueGenerator]] to use.
    *
    * @param dur The new duration generator.
    * @return An updated [[TaskGenerator]].
    */
  def withDuration(dur: ValueGenerator[Long]) = copy(duration = dur)
  /**
    * Update the task name to use.
    *
    * @param n The new name.
    * @return An updated [[TaskGenerator]].
    */
  def withName(n: String) = copy(name = n)
  /**
    * Update the simulation name to use.
    *
    * @param s The new name.
    * @return An updated [[TaskGenerator]].
    */
  def withSimulation(s: String) = copy(simulation = s)
  /**
    * Update the custom creation time to use.
    *
    * @param t The new creation time.
    * @return An updated [[TaskGenerator]].
    */
  def withCreationTime(t: Long) = copy(createTime = t)
}

case object TaskGenerator {
  def apply(
    name: String,
    simulation: String,
    duration: ValueGenerator[Long],
    cost: ValueGenerator[Long]
  ):TaskGenerator =
  TaskGenerator(name, UUID.randomUUID(), simulation, duration, cost)
}
