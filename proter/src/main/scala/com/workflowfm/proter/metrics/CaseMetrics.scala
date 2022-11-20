package com.workflowfm.proter
package metrics

/**
  * Metrics for a case instance.
  *
  * @param name
  *   The unique name of the instance.
  * @param started
  *   The virtual timestamp when the case instance started.
  * @param duration
  *   The duration of the case instance.
  * @param delay
  *   The total [[TaskInstance]] delays in the case instance.
  * @param tasks
  *   The number of started [[TaskInstance]]s associated with the case instance.
  * @param cost
  *   The total cost associated with the case instance.
  * @param result
  *   A `String` representation of the returned result from the case, or [[scala.None]] if it still
  *   running. In case of failure, the field is populated with the localized message of the
  *   exception thrown.
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

  /** Adds a starting task with some given delay and cost. */
  def task(tdelay: Long, tcost: Double): CaseMetrics = copy(
    tasks = tasks + 1,
    delay = delay + tdelay,
    cost = cost + tcost
  )

  /**
    * Updates the metrics given that the case has completed with a certain result.
    *
    * @param res
    *   The result of the case or localized message of the exception in case of failure.
    * @param time
    *   The virtual timestamp when the case finished.
    */
  def done(res: String, time: Long): CaseMetrics =
    copy(result = Some(res), duration = duration + time - started)
}

object CaseMetrics {

  /** Initialize metrics for a named case starting at the given virtual time. */
  def apply(name: String, t: Long): CaseMetrics =
    CaseMetrics(name, t, 0L, 0L, 0, 0L, None)
}
