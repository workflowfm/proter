package com.workflowfm.simulator
import scala.collection.mutable.Queue

import com.workflowfm.simulator.metrics._

/**
  * A persistent resource to be used by [[Task]]s.
  * A [[TaskResource]], or simply "resource", corresponds to a persistent resource.
  * Typical examples are human actors or persistent machinery.
  * Each [[TaskGenerator]] may require a different combination of resources.
  *
  * Each [[TaskResource]] is assumed to have a unique name and a `costPerTick`, i.e. the
  * cost (if any) of using the resource per unit of time.
  *
  * We say a [[TaskResource]] is ''attached'' to a [[Task]] when the task that uses this
  * resource is being performed. The `currentTask` variable holds the task that is currently
  * attached to the resource (if any) coupled with the virtual timestamp of when it started.
  * A [[TaskResource]] is ''idle''' when it has no tasks attached to it.
  *
  * The [[Coordinator]] calls upon [[startTask]] to attach a task to the resource and
  * [[finishTask]] to detach it. The [[Coordinator]] has full control over the resource and
  * makes all necessary checks to ensure consistent behaviour (e.g. not starting a [[Task]] when
  * another [[Task]] is already attached).
  *
  * @param name The name of the resource.
  * @param costPerTick The cost of using the resource per unit of time.
  */
class TaskResource(val name: String, val costPerTick: Int) {
  /**
    * Some [[Task]] currently attached to the resource, or [[scala.None]] if idle.
    */
  var currentTask: Option[(Long, Task)] = None

  /**
    * True if the resource is idle, false otherwise.
    *
    * @return true if the resource is idle, false otherwise.
    */
  def isIdle: Boolean = currentTask == None

  /**
    * Detaches the current [[Task]] (if any) if it has completed.
    * Checks to ensure the task is completed with respect to the current time and the task duration.
    *
    * Does not actually do anything to the task itself. It merely detaches it and becomes idle.
    * @param currentTime The current (virtual) time.
    * @return The [[Task]] that was detached, if any.
    */
  def finishTask(currentTime: Long): Option[Task] = currentTask match {
    case None => None
    case Some((startTime, task)) =>
      if (currentTime >= startTime + task.duration) {
        currentTask = None
        Some(task)
      } else None
  }

  /**
    * Attach a [[Task]] to this resource.
    * If the resource is already attached to another [[Task]], the attached task
    * is returned. Otherwise, we return [[scala.None]].
    *
    * @param task The [[Task]] to attach.
    * @param currentTime The current (virtual) time.
    * @return [[scala.None]] if the task was attached, or some [[Task]] that
    *         was already attached before
    */
  def startTask(task: Task, currentTime: Long): Option[Task] = {
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
    * Lets the [[Scheduler]] (via [[Task.nextPossibleStart]]) know an
    * '''estimate''' of when we expect to have this resource available again.
    * This is based off of [[Task.estimatedDuration]] so may not be the correct, but is more
    * realistic in terms of what we know at a specific given point in time.
    *
    * @param currentTime
    * @return
    */
  def nextAvailableTimestamp(currentTime: Long): Long = currentTask match {
    case None => currentTime
    case Some((startTime, t)) => {
      startTime + t.estimatedDuration
    }
  }
}
