package com.workflowfm.proter

import events.ETaskDetach

import java.util.UUID
import collection.immutable.Queue

import cats.implicits.*

case class Resource(name: String, capacity: Int, costPerTick: Double) {
  def start: ResourceState = ResourceState(this, Map())

  def costOf(actualDuration: Long, quantity: Int): Double =
    costPerTick * actualDuration * quantity
}

case class ResourceState(resource: Resource, currentTasks: Map[UUID, (Long, TaskInstance)]) {
 // TODO add explicit remaining capacity to avoid too many calcs

  /**
    * True if the resource is idle, false otherwise.
    *
    * @return
    *   true if the resource is idle, false otherwise.
    */
  def hasCapacity: Boolean = remainingCapacity > 0

  def remainingCapacity: Int = 
    resource.capacity - currentTasks.map{
      case (_, (_, ti)) => ti.resourceQuantity(resource.name)
    }.sum

  /**
    * Attach a [[TaskInstance]] to this resource.
    *
    * If the resource is already attached to another [[TaskInstance]], the attached task is
    * returned. Otherwise, we return the updated [[ResourceState]].
    *
    * @param task
    *   The [[TaskInstance]] to attach.
    * @param currentTime
    *   The current (virtual) time.
    * @return
    *   Some [[TaskInstance]] that was already attached before or An updated [[ResourceState]] if
    *   the task was attached successfully.
    */
  def startTask( // TODO double check we are not running it twice
      task: TaskInstance,
      currentTime: Long
  ): Either[ResourceState.Full, ResourceState] = {
    if remainingCapacity < task.resourceQuantity(resource.name)
    then Left(ResourceState.Full(this))
    else Right(this.copy(currentTasks = currentTasks + (task.id -> (currentTime, task))))
  }

  def detach(taskID: UUID): Option[DetachedTask] = 
    currentTasks.get(taskID).map { (start, task) => 
      DetachedTask(
        start, 
        task, 
        copy(currentTasks = currentTasks - taskID)
        ) 
      }

  def runningAnyOf(taskIDs: Seq[UUID]): Boolean = 
    taskIDs.exists(id => currentTasks.contains(id)) 

  def reduce(toReduce: Map[String, Int]): ResourceState = 
    copy(
      resource = resource.copy(
        capacity = resource.capacity - toReduce.get(resource.name).getOrElse(0)
      )
    )

}

object ResourceState {
  case class Full(state: ResourceState)
}

case class ResourceMap(resources: Map[String, ResourceState]) {

  /**
    * Add a new [[Resource]] to our map of available resources.
    *
    * @group resources
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
    * @group resources
    * @param resources
    *   The sequence of [[Resource]]s to be added.
    */
  def addResources(resourcesToAdd: Seq[Resource]): ResourceMap =
    copy(resources ++ (resourcesToAdd.map { r => r.name -> r.start }))

  //def finishTask(task: TaskInstance): ResourceMap = {
  //  copy(resources = resources.map { (n, r) => n -> r.detach(task.id) })
  //}

  def startTask(task: TaskInstance, time: Long): Either[ResourceState.Full, ResourceMap] = 
    (for {
      (name, _) <- task.resources
      state <- resources.get(name)
    } yield (
      state.startTask(task, time).map { newState => name -> newState }
    ))
      .toSeq
      .sequence
      .map { stateUpdates => copy(resources = resources ++ stateUpdates) }

  def stopTask(id: UUID): (ResourceMap, Seq[DetachedTask]) = {
    val stopping = resources
      .values
      .flatMap( _.detach(id))
    val update = stopping
      .map { d => d.resource.resource.name -> d.resource }
    (
      copy(resources ++ update),
      stopping.toSeq
    )
  }

  def stopTasks(ids: Seq[UUID]): (ResourceMap, Seq[DetachedTask]) = {
    ids.foldLeft((this, Queue[DetachedTask]())) { case ((m, q), id) => {
      val (result, ds) = m.stopTask(id)
      (result, q ++ ds)
    }}
  }

  def hasCapacity(r: String): Boolean = resources.get(r) match {
    case None => false
    case Some(resourceState) => resourceState.hasCapacity
  }

  def getAvailable(): ResourceMap = copy(resources = resources.filter(_._2.hasCapacity))

  // TODO improve to only go through selected resources
  def reduce(toReduce: Map[String, Int]): ResourceMap = copy(resources = resources.map((n, s) => n -> s.reduce(toReduce)))

  /**
    * The actual [[TaskResource]]s required. Retrieves the actual objects (instead of just their
    * names) from a map.
    *
    * @param resourceMap
    *   The map of available [[TaskResource]]s
    * @return
    *   The [[TaskResource]]s required for this task.
    */
  def get(task: TaskInstance): Seq[ResourceState] =
    task.resources.flatMap((n, _) => resources.get(n)).toSeq

  def canHandle(task: TaskInstance): Boolean = 
    task.resources.forall { (r, q) =>
      resources.get(r).map(_.remainingCapacity >= q).getOrElse(false)
    }

  def remainingCapacityOf(name: String): Int = resources.get(name).map(_.remainingCapacity).getOrElse(0)

  def capacityOf(name: String): Int = resources.get(name).map(_.resource.capacity).getOrElse(0)
}

object ResourceMap {

  def apply(resources: Seq[Resource]): ResourceMap = ResourceMap(
    Map() ++ resources.map { r => r.name -> r.start }
  )
}


final case class DetachedTask(start: Long, task: TaskInstance, resource: ResourceState)