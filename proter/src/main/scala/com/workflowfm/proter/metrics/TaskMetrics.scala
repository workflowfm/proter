package com.workflowfm.proter
package metrics

import java.util.UUID

/**
  * Metrics for a simulated [[TaskInstance]] that consumed virtual time.
  *
  * @param id
  *   the unique ID of the [[TaskInstance]]
  * @param task
  *   the name of the [[TaskInstance]]
  * @param simulation
  *   the name of the simulation the [[TaskInstance]] belongs to
  * @param priority
  *   the priority of the [[TaskInstance]]
  * @param created
  *   the virtual timestamp when the [[TaskInstance]] was created and entered the [[Coordinator]]
  * @param started
  *   the virtual timestamp when the [[TaskInstance]] started executing, or [[scala.None]] if it has
  *   not started yet
  * @param duration
  *   the virtual duration of the [[TaskInstance]]
  * @param cost
  *   the cost associated with the [[TaskInstance]]
  * @param resources
  *   the list of names of the [[TaskResource]]s this [[TaskInstance]] used
  */
final case class TaskMetrics(
    id: UUID,
    task: String,
    caseName: String,
    priority: Int,
    created: Long,
    started: Option[Long],
    duration: Long,
    cost: Double,
    resources: Map[String, Int],
    aborted: Boolean
) {
  /** Sets the starting time for the [[TaskInstance]]. */
  def start(st: Long): TaskMetrics = copy(started = Some(st))

  /**
    * Marks the [[TaskInstance]] as aborted.
    *
    * Updates its duration to reflect the aborted time.
    *
    * @param t
    *   The time of abortion.
    * @return
    *   The updated [[TaskMetrics]].
    */
  def abort(t: Long): TaskMetrics =
    copy(aborted = true, duration = started.map(t - _).getOrElse(duration))

  /**
    * Includes additional costs such as resource costs.
    *
    * @param addedCost
    *   The extra cost to add.
    * @return
    *   The updated [[TaskMetrics]].
    */
  def addCost(addedCost: Double): TaskMetrics =
    copy(cost = cost + addedCost)

  /** Calculates the task delay as the difference of the creation and starting times. */
  def delay: Long = started match {
    case None => 0L
    case Some(s) => s - created
  }

  /** Returns the full task and simulation name. */
  def fullName: String = s"$task ($caseName)"

  /** Returns the time of completion (if any). */
  def finished: Option[Long] = started.map { s => s + duration }
}

object TaskMetrics {

  /** Generates [[TaskMetrics]] from a given [[TaskInstance]] assuming it has not started yet. */
  def apply(task: TaskInstance): TaskMetrics = TaskMetrics(
    task.id,
    task.name,
    task.caseName,
    task.priority,
    task.created,
    None,
    task.duration,
    task.cost,
    task.resources,
    false
  )
}
