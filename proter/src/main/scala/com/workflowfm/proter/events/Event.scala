package com.workflowfm.proter.events

import java.util.UUID

import com.workflowfm.proter.TaskInstance

/**
  * Output events generated from a [[Publisher]] during a simulation.
  *
  * These are events taking place during the full simulation. They can be used for logging and
  * result analysis.
  */
sealed trait Event {
  /**
    * A string representing the [[Publisher]] that generated the event.
    */
  val source: String

  /**
    * The (virtual) timestamp of the event.
    */
  val time: Long

  override def toString: String = Event.asString(this)
}

/**
  * The entire simulation started.
  */
case class EStart(override val source: String, override val time: Long) extends Event {}

/**
  * A [[TaskResource]] was added.
  *
  * @param name
  *   The name of the resource.
  * @param costPerTick
  *   The [[TaskResource.costPerTick]] of the resource.
  */
case class EResourceAdd(
    override val source: String,
    override val time: Long,
    name: String,
    costPerTick: Double
) extends Event

/**
  * A simulation was added.
  *
  * @param name
  *   The name of the simulation.
  * @param start
  *   The timestamp when this simulation is scheduled to start.
  */
case class ESimAdd(
    override val source: String,
    override val time: Long,
    name: String,
    start: Long
) extends Event

/**
  * A simulation was started.
  *
  * @param name
  *   The name of the simulation.
  */
case class ESimStart(override val source: String, override val time: Long, name: String)
    extends Event

/**
  * A simulation was finished.
  *
  * @param name
  *   The name of the simulation.
  * @param result
  *   The output of the simulation (if any).
  */
case class ESimEnd(
    override val source: String,
    override val time: Long,
    name: String,
    result: String
) extends Event

/**
  * A new [[Task]] was added in the queue.
  *
  * @param task
  *   The [[TaskInstance]] that was added.
  */
case class ETaskAdd(
    override val source: String,
    override val time: Long,
    task: TaskInstance
) extends Event

/**
  * A [[TaskInstance]] was started.
  *
  * @param task
  *   The [[TaskInstance]] that was started.
  */
case class ETaskStart(
    override val source: String,
    override val time: Long,
    task: TaskInstance
) extends Event

/**
  * A [[TaskInstance]] was attached to a [[TaskResource]] as it started.
  *
  * @param task
  *   The [[TaskInstance]] that was attached.
  * @param resource
  *   The involved [[TaskResource]].
  */
case class ETaskAttach(
    override val source: String,
    override val time: Long,
    task: TaskInstance,
    resource: String
) extends Event

/**
  * A [[TaskInstance]] was detached from a [[TaskResource]] as it finished.
  *
  * @param task
  *   The [[TaskInstance]] that was detached.
  * @param resource
  *   The involved [[TaskResource]].
  * @param cost
  *   The resource cost associated with this task and resource.
  */
case class ETaskDetach(
    override val source: String,
    override val time: Long,
    task: TaskInstance,
    resource: String,
    cost: Double
) extends Event

/**
  * A [[TaskInstance]] has finished.
  *
  * @param task
  *   The [[TaskInstance]] that finished.
  */
case class ETaskDone(
    override val source: String,
    override val time: Long,
    task: TaskInstance
) extends Event

/**
  * A [[Task]] was aborted.
  *
  * @param id
  *   The `UUID` of the [[TaskInstance]] that was aborted.
  */
case class ETaskAbort(
    override val source: String,
    override val time: Long,
    id: UUID
) extends Event

/**
  * The entire simulation is complete.
  */
case class EDone(override val source: String, override val time: Long) extends Event

/**
  * An error or exception occurred during the simulation.
  *
  * @param error
  *   A string representation of the error.
  */
case class EError(override val source: String, override val time: Long, error: String) extends Event

object Event {

  /**
    * Generates a string representation of an [[Event]].
    *
    * @param e
    *   The event to convert.
    * @return
    *   The string representation.
    */
  def asString(e: Event): String = e match {
    case EStart(src, t) => s"[$t $src] === Simulation started! ==="
    case EDone(src, t) => s"[$t $src] === Simulation complete! ==="
    case EResourceAdd(src, t, n, c) => s"[$t $src] Added resource: $n (CPT:$c)"
    case ESimAdd(src, t, a, s) => s"[$t $src] Added simulation actor [$a] to be run at: $s"
    case ESimStart(src, t, n) => s"[$t $src] Starting simulation: $n"
    case ESimEnd(src, t, n, r) => s"[$t $src] Simulation [$n] completed. Result: $r"
    case ETaskAdd(src, t, task) =>
      s"[$t $src] Added task [${task.name}](${task.simulation}) created at [${task.created}]. (id:${task.id})"
    case ETaskStart(src, t, task) =>
      s"[$t $src] Starting task [${task.name}](${task.simulation}). Ticks remaining: ${task.duration}. (id:${task.id})"
    case ETaskAttach(src, t, task, r) =>
      s"[$t $src] Attaching task [${task.name}](${task.simulation}) to [$r]. Ticks remaining: ${task.duration}. (id:${task.id})"
    case ETaskDetach(src, t, task, r, c) =>
      s"[$t $src] Detaching task [${task.name}](${task.simulation}) from [$r] with cost [$c]. (id:${task.id})"
    case ETaskDone(src, t, task) =>
      s"[$t $src] Task [${task.name}](${task.simulation}) completed. (id:${task.id})"
    case ETaskAbort(src, t, id) =>
      s"[$t $src] Task id [$id] was aborted."

    case EError(src, t, error) => s"[$t $src] !ERROR! $error !ERROR!"
  }
}
