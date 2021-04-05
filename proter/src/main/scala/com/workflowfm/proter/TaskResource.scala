package com.workflowfm.proter

import java.util.UUID

/**
  * A persistent resource to be used by [[Task]]s/[[TaskInstance]]s.
  *
  * A [[TaskResource]], or simply "resource", corresponds to a persistent resource.
  * Typical examples are human actors or persistent machinery.
  *
  * Each [[Task]] may require a different combination of resources.
  *
  * Each [[TaskResource]] is assumed to have a unique name and a `costPerTick`, i.e. the
  * cost (if any) of using the resource per unit of time.
  *
  * We say a [[TaskResource]] is ''attached'' to a [[TaskInstance]] when the task that uses this
  * resource is being performed. The `currentTask` variable holds the task that is currently
  * attached to the resource (if any) coupled with the virtual timestamp of when it started.
  * A [[TaskResource]] is ''idle''' when it has no tasks attached to it.
  *
  * The [[Coordinator]] calls upon [[startTask]] to attach a task to the resource and
  * [[finishTask]] to detach it. The [[Coordinator]] has full control over the resource and
  * makes all necessary checks to ensure consistent behaviour (e.g. not starting a [[TaskInstance]] when
  * another [[TaskInstance]] is already attached).
  *
  * @param name The name of the resource.
  * @param costPerTick The cost of using the resource per unit of time.
  */
class TaskResource(val name: String, val costPerTick: Double) {
  /**
    * Some [[TaskInstance]] currently attached to the resource, or [[scala.None]] if idle.
    */
  var currentTask: Option[(Long, TaskInstance)] = None

  /**
    * True if the resource is idle, false otherwise.
    *
    * @return true if the resource is idle, false otherwise.
    */
  def isIdle: Boolean = currentTask == None

  /**
    * Detaches the current [[TaskInstance]] (if any) if it has completed.
    *
    * Checks to ensure the task is completed with respect to the current time and the task duration.
    *
    * Does not do anything to the task itself. It merely detaches it and becomes idle.
    *
    * @param currentTime The current (virtual) time.
    * @return The [[TaskInstance]] that was detached, if any.
    */
  def finishTask(currentTime: Long): Option[TaskInstance] = currentTask match {
    case None => None
    case Some((startTime, task)) =>
      if (currentTime >= startTime + task.duration) {
        currentTask = None
        Some(task)
      } else None
  }

  /**
    * Aborts the current [[TaskInstance]] if it matches a given `UUID`.
    *
    * Does not do anything to the task itself. It merely detaches it and becomes idle.
    *
    * @param id The `UUID` of the [[TaskInstance]] to abort.
    * @return The starting time and [[TaskInstance]] if it was aborted successfully or [[scala.None None]] in any other case.
    */
  def abortTask(id: UUID): Option[(Long, TaskInstance)] = {
    currentTask match {
      case Some((startTime, task)) if task.id == id => {
        currentTask = None
        Some((startTime, task))
      }
      case _ => None
    }
  }

  /**
    * Aborts the current [[TaskInstance]] if it belongs to the given simulation.
    *
    * Does not do anything to the task itself. It merely detaches it and becomes idle.
    *
    * @param simulation The name of the simulation whose tasks to abort.
    * @return The starting time and [[TaskInstance]] if it was aborted successfully or [[scala.None None]] in any other case.
    */
  def abortSimulation(simulation: String): Option[(Long, TaskInstance)] = {
    currentTask match {
      case Some((startTime, task)) if task.simulation == simulation => {
        currentTask = None
        Some((startTime, task))
      }
      case _ => None
    }
  }

  /**
    * Attach a [[TaskInstance]] to this resource.
    *
    * If the resource is already attached to another [[TaskInstance]], the attached task
    * is returned. Otherwise, we return [[scala.None]].
    *
    * @param task The [[TaskInstance]] to attach.
    * @param currentTime The current (virtual) time.
    * @return [[scala.None None]] if the task was attached, or some [[TaskInstance]] that
    *         was already attached before
    */
  def startTask(task: TaskInstance, currentTime: Long): Option[TaskInstance] = {
    currentTask match {
      case None => {
        currentTask = Some(currentTime, task)
        None
      }
      case Some((_, currentTask)) => {
        Some(currentTask)
      }
    }
  }

  /**
    * Estimates the earliest time the resource will become available.
    *
    * Lets the [[Scheduler]] (via [[TaskInstance.nextPossibleStart]]) know an
    * '''estimate''' of when we expect to have this resource available again.
    *
    * This is based off of [[TaskInstance.estimatedDuration]] so may not be the accurate, but is more
    * realistic in terms of what we know at a specific given point in time.
    *
    * @param currentTime
    * @return the estimated earliest time the resource will become available
    */
  def nextAvailableTimestamp(currentTime: Long): Long = currentTask match {
    case None => currentTime
    case Some((startTime, t)) => {
      startTime + t.estimatedDuration
    }
  }
}
