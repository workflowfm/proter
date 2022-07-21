package com.workflowfm.proter
package metrics

/**
  * Metrics for a simulation that has already started.
  *
  * @param name
  *   the unique name of the simulation
  * @param started
  *   the virtual timestamp when the simulation started executing
  * @param duration
  *   the virtual duration of the simulation
  * @param delay
  *   the sum of all delays for all involved [[TaskInstance]]s
  * @param tasks
  *   the number of [[TaskInstance]]s associated with the simulation so far
  * @param cost
  *   the total cost associated with the simulation so far
  * @param result
  *   a `String` representation of the returned result from the simulation, or [[scala.None]] if it
  *   still running. In case of failure, the field is populated with the localized message of the
  *   exception thrown
  */
final case class CaseMetrics(
    name: String,
    started: Long,
    duration: Long,
    delay: Long,
    tasks: Int,
    cost: Double,
    result: Option[String]
) {
  /** Adds some time to the total duration. */
  def addDuration(d: Long): CaseMetrics = copy(duration = duration + d)
  /** Adds some cost to the total cost. */
  def addCost(c: Double): CaseMetrics = copy(cost = cost + c)
  /** Adds some delay to the total delay. */
  def task(tdelay: Long, tcost: Double): CaseMetrics = copy(
    tasks = tasks + 1, 
    delay = delay + tdelay,
    cost = cost + tcost  
  )

  /**
    * Updates the metrics given that the simulation has completed with a certain result.
    * @param res
    *   the result of the simulation or localized message of the exception in case of failure
    * @param time
    *   the virtual timestamp when the simulation finished
    */
  def done(res: String, time: Long): CaseMetrics =
    copy(result = Some(res), duration = duration + time - started)
}

object CaseMetrics {

  /** Initialize metrics for a named simulation starting at the given virtual time. */
  def apply(name: String, t: Long): CaseMetrics =
    CaseMetrics(name, t, 0L, 0L, 0, 0L, None)
}
