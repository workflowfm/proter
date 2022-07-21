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
  capacity: Int,
  costPerTick: Double,
  idleUpdate: Long,
  load: Int,
  busyTime: Long,
  idleTime: Long,
  tasks: Int,
  cost: Double
) {

  def start(t: Long): ResourceMetrics = copy(idleUpdate = t)

  /** Adds some idle time to the total. */
  def idle(t: Long): ResourceMetrics =
    if idleUpdate < t then copy(
      idleTime = idleTime + (t - idleUpdate) * (capacity - load), 
      idleUpdate = t
    ) 
    else this

  /** Updates the metrics given a new [[TaskInstance]] has been attached to the [[TaskResource]]. */
  def task(t: Long, q: Int): ResourceMetrics = idle(t).copy(
    tasks = tasks + 1,
    load = load + q
  )

  /** Updates the metrics given a new [[TaskInstance]] has been attached to the [[TaskResource]]. */
  def endTask(t: Long, start: Long, tcost: Double, q: Int): ResourceMetrics = idle(t).copy(
    cost = cost + tcost,
    load = load - q,
    busyTime = busyTime + (t - start) * q
  )
}

object ResourceMetrics {

  /** Initialize metrics given the name of a [[TaskResource]]. */
  def apply(t: Long, r: Resource): ResourceMetrics =
    ResourceMetrics(r.name, r.capacity, r.costPerTick, t, 0, 0L, 0L, 0, 0D)
}