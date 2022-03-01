package com.workflowfm.proter

import java.util.UUID

case class Resource(name: String, costPerTick: Double) {
  def start: ResourceState = ResourceState(this, None)
}

case class ResourceState(resource: Resource, currentTask: Option[(Long, TaskInstance)] ) {

  /**
    * True if the resource is idle, false otherwise.
    *
    * @return
    *   true if the resource is idle, false otherwise.
    */
  def isIdle: Boolean = currentTask == None

  /**
    * Attach a [[TaskInstance]] to this resource.
    *
    * If the resource is already attached to another [[TaskInstance]], the attached task is
    * returned. Otherwise, we return [[scala.None]].
    *
    * @param task
    *   The [[TaskInstance]] to attach.
    * @param currentTime
    *   The current (virtual) time.
    * @return
    *   [[scala.None None]] if the task was attached, or some [[TaskInstance]] that was already
    *   attached before
    */
  def startTask(task: TaskInstance, currentTime: Long): Either[TaskInstance, ResourceState] = {
    currentTask match {
      case None => {
        Right(this.copy(currentTask = Some(currentTime, task)))
      }
      case Some((_, runningTask)) => {
        Left(runningTask)
      }
    }
  }


  /**
    * Detaches the current [[TaskInstance]] (if any) if it has completed.
    *
    * Checks to ensure the task is completed with respect to the current time and the task duration.
    *
    * Does not do anything to the task itself. It merely detaches it and becomes idle.
    *
    * @param currentTime
    *   The current (virtual) time.
    * @return
    *   The [[TaskInstance]] that was detached, if any.
    */
  def finishTask(currentTime: Long): Option[ResourceState] = currentTask match {
    case None => None
    case Some((startTime, task)) =>
      if currentTime >= startTime + task.duration then {
        Some(copy(currentTask = None))
      } else None
  }

  def detach(taskID: UUID): ResourceState = currentTask match {
    case Some((_, someTask)) if taskID == someTask.id => copy(currentTask = None)
    case _ => this
  }

  def detach(taskIDs: Seq[UUID]): ResourceState = currentTask match {
    case Some((_, someTask)) if taskIDs.contains(someTask.id) => copy(currentTask = None)
    case _ => this
  }

  /**
    * Estimates the earliest time the resource will become available.
    *
    * Lets the [[schedule.Scheduler Scheduler]] (via [[TaskInstance.nextPossibleStart]]) know an
    * '''estimate''' of when we expect to have this resource available again.
    *
    * This is based off of [[TaskInstance.estimatedDuration]] so may not be the accurate, but is
    * more realistic in terms of what we know at a specific given point in time.
    *
    * @param currentTime
    * @return
    *   the estimated earliest time the resource will become available
    */
  def nextAvailableTimestamp(currentTime: Long): Long = currentTask match {
    case None => currentTime
    case Some((startTime, t)) => {
      startTime + t.estimatedDuration
    }
  }
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
  def addResources(resourcesToAdd: Seq[Resource]): Unit =       
    copy(resources ++ (resourcesToAdd.map { r => r.name -> r.start } ))

  def finishTask(task: TaskInstance): ResourceMap = {
    copy(resources = resources.map { (n, r) => n -> r.detach(task.id) } )
  }

  def startTask(task: TaskInstance, time: Long): ResourceMap = {

    task.resources.flatMap { name => resources.get(name).map { state => state.resource.name -> state.startTask(task, time) } }
    // TODO how do we deal with the Either here?
  }
}
