package com.workflowfm.proter

import events.ETaskDetach

import java.util.UUID
import collection.immutable.Queue

import cats.implicits.*

/**
  * A peristent resource that can be used for simulated tasks.
  *
  * @param name
  *   A unique name.
  * @param capacity
  *   The resource capacity.
  * @param costPerTick
  *   The cost of using 1 unit of resource capacity per unit of time.
  */
case class Resource(name: String, capacity: Int, costPerTick: Double) {

  /**
    * Initial state.
    */
  def start: ResourceState = ResourceState(this, Map())

  /**
    * Calculates the cost of using certain units of capacity over a given time.
    *
    * @param actualDuration
    *   The duration to calculate.
    * @param quantity
    *   The units of capacity to use.
    * @return
    *   The calculated total cost.
    */
  def costOf(actualDuration: Long, quantity: Int): Double =
    costPerTick * actualDuration * quantity
}

/**
  * The state of a [[Resource]] during simulation.
  *
  * Includes the [[TaskInstance]]s that are attached to the resource at a given time.
  *
  * @param resource
  *   The associated [[Resource]].
  * @param currentTasks
  *   A `Map` of attached [[TaskInstance]]s paired with their starting times and indexed by their
  *   IDs.
  */
case class ResourceState(resource: Resource, currentTasks: Map[UUID, (Long, TaskInstance)]) {

  /**
    * The remaining available capacity of the resource.
    */
  lazy val remainingCapacity: Int =
    resource.capacity - currentTasks.map { case (_, (_, ti)) =>
      ti.resourceQuantity(resource.name)
    }.sum

  /**
    * Attach a [[TaskInstance]] to this [[Resource]].
    *
    * @param task
    *   The [[TaskInstance]] to attach.
    * @param currentTime
    *   The current (virtual) time.
    * @return
    *   The updated state (`Right`) or a [[ResourceState.Full]] if there is not enough capacity.
    */
  def startTask( // TODO double check we are not running it twice
      task: TaskInstance,
      currentTime: Long
  ): Either[ResourceState.Full, ResourceState] = {
    if remainingCapacity < task.resourceQuantity(resource.name) then Left(ResourceState.Full(this))
    else Right(this.copy(currentTasks = currentTasks + (task.id -> (currentTime, task))))
  }

  /**
    * Detach a [[TaskInstance]] from the [[Resource]].
    *
    * @param taskID
    *   The `UUID` of the task to detach.
    * @return
    *   The [[DetachedTask]] or `None` if the ID was not found attached here.
    */
  def detach(taskID: UUID): Option[DetachedTask] =
    currentTasks.get(taskID).map { (start, task) =>
      DetachedTask(
        start,
        task,
        copy(currentTasks = currentTasks - taskID)
      )
    }

  /**
    * Checks if any task IDs in a given list are attached to this resource.
    *
    * @param taskIDs
    *   The IDs of the tasks we are interested in.
    * @return
    *   True if any of the task IDs are attached to this resource.
    */
  def runningAnyOf(taskIDs: Seq[UUID]): Boolean =
    taskIDs.exists(id => currentTasks.contains(id))

  /**
    * Reduces the capacity of the resource based on a given map of named resource quantities.
    *
    * This is used when scheduling tasks which are not yet attached.
    *
    * @param A
    *   `Map` of resource names and quantities to reduce.
    * @return
    *   The updated state.
    */
  def reduce(toReduce: Map[String, Int]): ResourceState =
    copy(
      resource = resource.copy(
        capacity = resource.capacity - toReduce.get(resource.name).getOrElse(0)
      )
    )

}

object ResourceState {

  /**
    * Wrapper around a [[ResourceState]] that does not have enough capacity for some task.
    *
    * @param state
    *   The corresponding [[ResourceState]].
    */
  case class Full(state: ResourceState)
}

/**
  * A map of all simulated [[Resource]]s.
  *
  * @param resources
  *   A `Map` of [[Resource]] names to their [[ResourceState]].
  */
case class ResourceMap(resources: Map[String, ResourceState]) {

  /**
    * Add a new [[Resource]] to our map of available resources.
    *
    * @param r
    *   The [[Resource]] to be added.
    */
  def addResource(r: Resource): ResourceMap = {
    if !resources.contains(r.name) then {
      copy(resources + (r.name -> r.start))
    } else this
  }

  /**
    * Add multiple [[Resource]]s in one go.
    *
    * @param resources
    *   The sequence of [[Resource]]s to be added.
    */
  def addResources(resourcesToAdd: Seq[Resource]): ResourceMap =
    copy(resources ++ (resourcesToAdd.map { r => r.name -> r.start }))

  /**
    * Attach a [[TaskInstance]] to all associated [[ResourceState]]s.
    *
    * @param task
    *   The [[TaskInstance]] to attach.
    * @param time
    *   The current timestamp.
    * @return
    *   The updated structure or a `Left` of a [[ResourceState.Full]] if any of the resources did
    *   not have enough capacity.
    */
  def startTask(task: TaskInstance, time: Long): Either[ResourceState.Full, ResourceMap] =
    (for {
      (name, _) <- task.resources
      state <- resources.get(name)
    } yield (
      state.startTask(task, time).map { newState => name -> newState }
    )).toSeq.sequence.map { stateUpdates => copy(resources = resources ++ stateUpdates) }

  /**
    * Detach a [[TaskInstance]] from all associated [[ResourceState]]s.
    *
    * @param id
    *   The ID of the [[TaskInstance]] to detach.
    * @return
    *   The updated structure paired with the resulting sequence of [[DetachedTask]]s.
    */
  def stopTask(id: UUID): (ResourceMap, Seq[DetachedTask]) = {
    val stopping = resources.values
      .flatMap(_.detach(id))
    val update = stopping.map { d => d.resource.resource.name -> d.resource }
    (
      copy(resources ++ update),
      stopping.toSeq
    )
  }

  /**
    * Detach multiple [[TaskInstance]]s from all associated [[ResourceState]]s.
    *
    * @param ids
    *   The IDs of the [[TaskInstance]]s to detach.
    * @return
    *   The updated structure paired with the resulting sequence of [[DetachedTask]]s.
    */
  def stopTasks(ids: Seq[UUID]): (ResourceMap, Seq[DetachedTask]) = {
    ids.foldLeft((this, Queue[DetachedTask]())) {
      case ((m, q), id) => {
        val (result, ds) = m.stopTask(id)
        (result, q ++ ds)
      }
    }
  }

  /**
    * Filters the structure to keep only resources with some available capacity.
    */
  def getAvailable(): ResourceMap = copy(resources = resources.filter(_._2.remainingCapacity > 0))

  /**
    * Reduces the capacity of the resources based on a given map of named resource quantities.
    *
    * This is used when scheduling tasks which are not yet attached.
    *
    * @param A
    *   `Map` of resource names and quantities to reduce.
    * @return
    *   The updated resource map.
    */
  def reduce(toReduce: Map[String, Int]): ResourceMap =
    // TODO improve to only go through selected resources
    copy(resources = resources.map((n, s) => n -> s.reduce(toReduce)))

  /**
    * Retrieves the states of the resources required by a [[TaskInstance]]s.
    *
    * @param task
    *   The associated [[TaskInstance]]s
    * @return
    *   The [[ResourceState]]s required for this task.
    */
  def get(task: TaskInstance): Seq[ResourceState] =
    task.resources.flatMap((n, _) => resources.get(n)).toSeq

  /**
    * Checks if the available resources have enough capacity to run a given [[TaskInstance]].
    *
    * @param task
    *   The [[TaskInstance]] at hand.
    * @return
    *   True if the task can be handled by the current resources.
    */
  def canHandle(task: TaskInstance): Boolean =
    task.resources.forall { (r, q) =>
      resources.get(r).map(_.remainingCapacity >= q).getOrElse(false)
    }

  /**
    * Retrieves the remaining capacity of a named resource.
    *
    * @param name
    *   The name of the resource to check.
    * @return
    *   The current capacity.
    */
  def remainingCapacityOf(name: String): Int =
    resources.get(name).map(_.remainingCapacity).getOrElse(0)

  /**
    * Retrieves the total capacity of a named resource.
    *
    * @param name
    *   The name of the resource to check.
    * @return
    *   Its total capacity.
    */
  def capacityOf(name: String): Int = resources.get(name).map(_.resource.capacity).getOrElse(0)
}

object ResourceMap {

  def apply(resources: Seq[Resource]): ResourceMap = ResourceMap(
    Map() ++ resources.map { r => r.name -> r.start }
  )
}

/**
  * A structure of a [[TaskInstance]] that has been detached and the corresponding
  * [[ResourceState]].
  *
  * @param start
  *   The timestamp when the task was attached originally.
  * @param task
  *   The detached [[TaskInstance]].
  * @param resource
  *   The updated [[ResourceState]].
  */
final case class DetachedTask(start: Long, task: TaskInstance, resource: ResourceState)
