package com.workflowfm.proter
package metrics

import com.workflowfm.proter.*

/**
  * Metrics for at [[TaskResource]].
  *
  * @param name
  *   the unique name of the [[TaskResource]]
  * @param busyTime
  *   the total amount of virtual time that the [[TaskResource]] has been busy, i.e. attached to a
  *   [[TaskInstance]]
  * @param idleTime
  *   the total amount of virtual time that the [[TaskResource]] has been idle, i.e. not attached to
  *   any [[TaskInstance]]
  * @param tasks
  *   the number of different [[TaskInstance]]s that have been attached to this [[TaskResource]]
  * @param cost
  *   the total cost associated with this [[TaskResource]]
  */
final case class ResourceMetrics(
    name: String,
    costPerTick: Double,
    idleUpdate: Long,
    busyTime: Long,
    idleTime: Long,
    tasks: Int,
    cost: Double
) {

  /** Adds some idle time to the total. */
  def idle(t: Long): ResourceMetrics =
    if idleUpdate < t then copy(idleTime = idleTime + t - idleUpdate, idleUpdate = t) else this

  /** Updates the metrics given a new [[TaskInstance]] has been attached to the [[TaskResource]]. */
  def task(t: Long, task: TaskInstance): ResourceMetrics = idle(t).copy(
    tasks = tasks + 1,
    cost = cost + task.duration * costPerTick,
    busyTime = busyTime + task.duration,
    idleUpdate = t + task.duration
  )
}

object ResourceMetrics {

  /** Initialize metrics given the name of a [[TaskResource]]. */
  def apply(r: Resource): ResourceMetrics =
    ResourceMetrics(r.name, r.costPerTick, 0L, 0L, 0L, 0, 0)
}
