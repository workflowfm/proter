package com.workflowfm.proter.metrics

import java.util.UUID

import com.workflowfm.proter._

/** Metrics for a simulated [[Task]] that consumed virtual time.
  *
  * @param id the unique ID of the [[Task]]
  * @param task the name of the [[Task]]
  * @param simulation the name of the simulation the [[Task]] belongs to
  * @param created the virtual timestamp when the [[Task]] was created and entered the [[Coordinator]]
  * @param started the virtual timestamp when the [[Task]] started executing, or [[scala.None]] if it has not started yet
  * @param duration the virtual duration of the [[Task]]
  * @param cost the cost associated with the [[Task]]
  * @param resources the list of names of the [[TaskResource]]s this [[Task]] used
  */
case class TaskMetrics(
    id: UUID,
    task: String,
    simulation: String,
    created: Long,
    started: Option[Long],
    duration: Long,
    cost: Long,
    resources: Seq[String]
) {
  /** Sets the starting time for the [[Task]]. */
  def start(st: Long): TaskMetrics = copy(started = Some(st))

  /** Calculates the task delay as the difference of the creation and starting times. */
  def delay: Long = started match {
    case None => 0L
    case Some(s) => s - created
  }

  /** Returns the full task and simulation name. */
  def fullName: String = s"$task ($simulation)"

  /** Returns the time of completion (if any). */
  def finished: Option[Long] = started.map { s => s + duration }
}

object TaskMetrics {

  /** Generates [[TaskMetrics]] from a given [[Task]] assuming it has not started yet. */
  def apply(task: Task): TaskMetrics = TaskMetrics(
    task.id,
    task.name,
    task.simulation,
    task.created,
    None,
    task.duration,
    task.cost,
    task.resources
  )
}

/** Metrics for a simulation that has already started.
  *
  * @param name the unique name of the simulation
  * @param started the virtual timestamp when the simulation started executing
  * @param duration the virtual duration of the simulation
  * @param delay the sum of all delays for all involved [[Task]]s
  * @param tasks the number of [[Task]]s associated with the simulation so far
  * @param cost the total cost associated with the simulation so far
  * @param result a `String` representation of the returned result from the simulation, or [[scala.None]] if it still running. In case of failure, the field is populated with the localized message of the exception thrown
  */
case class SimulationMetrics(
    name: String,
    started: Long,
    duration: Long,
    delay: Long,
    tasks: Int,
    cost: Long,
    result: Option[String]
) {
  /** Adds some time to the total duration. */
  def addDuration(d: Long): SimulationMetrics = copy(duration = duration + d)
  /** Adds some cost to the total cost. */
  def addCost(c: Long): SimulationMetrics = copy(cost = cost + c)
  /** Adds some delay to the total delay. */
  def addDelay(d: Long): SimulationMetrics = copy(delay = delay + d)
  /** Updates the metrics given a new [[Task]] that is created as part of the simulation. */
  def task(task: Task): SimulationMetrics = copy(tasks = tasks + 1, cost = cost + task.cost)
  /** Updates the metrics given that the simulation has completed with a certain result.
    * @param res the result of the simulation or localized message of the exception in case of failure
    * @param time the virtual timestamp when the simulation finished
    */
  def done(res: String, time: Long): SimulationMetrics = copy(result = Some(res), duration = duration + time - started)
}

object SimulationMetrics {

  /** Initialize metrics for a named simulation starting at the given virtual time. */
  def apply(name: String, t: Long): SimulationMetrics =
    SimulationMetrics(name, t, 0L, 0L, 0, 0L, None)
}

/** Metrics for at [[TaskResource]].
  *
  * @param name the unique name of the [[TaskResource]]
  * @param busyTime the total amount of virtual time that the [[TaskResource]] has been busy, i.e. attached to a [[Task]]
  * @param idleTime the total amount of virtual time that the [[TaskResource]] has been idle, i.e. not attached to any [[Task]]
  * @param tasks the number of different [[Task]]s that have been attached to this [[TaskResource]]
  * @param cost the total cost associated with this [[TaskResource]]
  */
case class ResourceMetrics(
    name: String,
    costPerTick: Int,
    idleUpdate: Long,
    busyTime: Long,
    idleTime: Long,
    tasks: Int,
    cost: Long
) {

  /** Adds some idle time to the total. */
  def idle(t: Long): ResourceMetrics =
    if (idleUpdate < t) copy(idleTime = idleTime + t - idleUpdate, idleUpdate = t) else this

  /** Updates the metrics given a new [[Task]] has been attached to the [[TaskResource]]. */
  def task(t: Long, task: Task): ResourceMetrics = idle(t).copy(
    tasks = tasks + 1,
    cost = cost + task.duration * costPerTick,
    busyTime = busyTime + task.duration,
    idleUpdate = t + task.duration
  )
}

object ResourceMetrics {

  /** Initialize metrics given the name of a [[TaskResource]]. */
  def apply(name: String, costPerTick: Int): ResourceMetrics =
    ResourceMetrics(name, costPerTick, 0L, 0L, 0L, 0, 0L)
}
