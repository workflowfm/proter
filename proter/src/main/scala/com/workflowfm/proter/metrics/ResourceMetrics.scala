package com.workflowfm.proter
package metrics

import com.workflowfm.proter.*

/**
  * Metrics for at [[Resource]].
  *
  * @note
  *   Busy and idle times are measured for each unit of capacity.
  *
  * @param name
  *   The unique name of the [[Resource]].
  * @param capacity
  *   The total capacity of the [[Resource]].
  * @param costPerTick
  *   The cost of using the [[Resource]] per unit of time.
  * @param idleUpdate
  *   The last timestamp when busy/idle times were calculated.
  * @param load
  *   The resource load since `idleUpdate`.
  * @param busyTime
  *   The total amount of virtual time that the [[Resource]] has been busy.
  * @param idleTime
  *   The total amount of virtual time that the [[Resource]] has been idle.
  * @param tasks
  *   The number of different [[TaskInstance]]s that have been attached to this [[Resource]].
  * @param cost
  *   The total cost associated with the use of this [[Resource]].
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
    if idleUpdate < t then
      copy(
        idleTime = idleTime + (t - idleUpdate) * (capacity - load),
        idleUpdate = t
      )
    else this

  /** Updates the metrics given a new [[TaskInstance]] has been attached to the [[Resource]]. */
  def task(t: Long, q: Int): ResourceMetrics = idle(t).copy(
    tasks = tasks + 1,
    load = load + q
  )

  /** Updates the metrics given a [[TaskInstance]] has been detached from the [[Resource]]. */
  def endTask(t: Long, start: Long, tcost: Double, q: Int): ResourceMetrics = idle(t).copy(
    cost = cost + tcost,
    load = load - q,
    busyTime = busyTime + (t - start) * q
  )
}

object ResourceMetrics {

  /** Initialize metrics given the name of a [[Resource]]. */
  def apply(t: Long, r: Resource): ResourceMetrics =
    ResourceMetrics(r.name, r.capacity, r.costPerTick, t, 0, 0L, 0L, 0, 0d)
}
