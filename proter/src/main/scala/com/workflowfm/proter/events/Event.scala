package com.workflowfm.proter
package events

import java.util.UUID

/**
  * Output events generated from a [[Publisher]] during a simulation.
  *
  * These are events taking place during the full simulation. They can be used for logging and
  * result analysis.
  */
sealed trait Event {

  /**
    * A string representing the simulation that generated the event.
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
final case class EStart(override val source: String, override val time: Long) extends Event {}

/**
  * A time limit was set.
  *
  * @param limit
  *   The limit in time units/
  */
final case class ETimeLimit(
    override val source: String,
    override val time: Long,
    limit: Long
) extends Event

/**
  * A [[Resource]] was added.
  *
  * @param name
  *   The name of the resource.
  * @param resource
  *   The [[Resource]] that was added.
  */
final case class EResourceAdd(
    override val source: String,
    override val time: Long,
    resource: Resource
) extends Event

/**
  * A case instance was added.
  *
  * @param name
  *   The name of the case.
  * @param start
  *   The timestamp when this case is scheduled to start.
  */
final case class ECaseAdd(
    override val source: String,
    override val time: Long,
    name: String,
    start: Long
) extends Event

/**
  * An arrival was added.
  *
  * @note
  *   The arrival rate is not included as there is no standard representation for distributions.
  *
  * @param name
  *   The name of the arriving cases.
  * @param start
  *   The timestamp when arrivals is scheduled to start.
  * @param limit
  *   The optional limit of the number of cases to generate.
  */
final case class EArrivalAdd(
    override val source: String,
    override val time: Long,
    name: String,
    start: Long,
    limit: Option[Int]
) extends Event

/**
  * A case instance was started.
  *
  * @param name
  *   The name of the case instance.
  */
final case class ECaseStart(override val source: String, override val time: Long, name: String)
    extends Event

/**
  * A case instance was completed.
  *
  * @param name
  *   The name of the case instance.
  * @param result
  *   The output result of the case (if any).
  */
final case class ECaseEnd(
    override val source: String,
    override val time: Long,
    name: String,
    result: String
) extends Event

/**
  * A new [[TaskInstance]] was added in the queue.
  *
  * @param task
  *   The [[TaskInstance]] that was added.
  */
final case class ETaskAdd(
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
final case class ETaskStart(
    override val source: String,
    override val time: Long,
    task: TaskInstance
) extends Event

/**
  * A [[TaskInstance]] was attached to a [[Resource]] as it started.
  *
  * @param task
  *   The [[TaskInstance]] that was attached.
  * @param resource
  *   The corresponding [[ResourceState]] ''after'' the attachment of the task.
  */
final case class ETaskAttach(
    override val source: String,
    override val time: Long,
    task: TaskInstance,
    resource: ResourceState
) extends Event

/**
  * A [[TaskInstance]] was detached from a [[Resource]] as it finished.
  *
  * @param start
  *   The timestamp when the task had started.
  * @param task
  *   The [[TaskInstance]] that was detached.
  * @param resource
  *   The corresponding [[ResourceState]] ''after'' the detachment of the task.
  */
final case class ETaskDetach(
    override val source: String,
    override val time: Long,
    start: Long,
    task: TaskInstance,
    resource: ResourceState
) extends Event

object ETaskDetach {

  def apply(source: String, t: Long, d: DetachedTask): ETaskDetach =
    ETaskDetach(source, t, d.start, d.task, d.resource)
}

/**
  * A [[TaskInstance]] has finished.
  *
  * @param task
  *   The [[TaskInstance]] that finished.
  */
final case class ETaskDone(
    override val source: String,
    override val time: Long,
    task: TaskInstance
) extends Event

/**
  * A [[TaskInstance]] was aborted.
  *
  * @param id
  *   The `UUID` of the [[TaskInstance]] that was aborted.
  */
final case class ETaskAbort(
    override val source: String,
    override val time: Long,
    id: UUID
) extends Event

/**
  * The entire simulation is complete.
  */
final case class EDone(override val source: String, override val time: Long) extends Event

/**
  * An error or exception occurred during the simulation.
  *
  * @param error
  *   A string representation of the error.
  */
final case class EError(override val source: String, override val time: Long, error: String)
    extends Event

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
    case ETimeLimit(src, t, l) => s"[$t $src] Set time limit at: $l"
    case EResourceAdd(src, t, r) =>
      s"[$t $src] Added resource: [${r.name}]. Capacity: ${r.capacity} - Cost/tick: ${r.costPerTick})"
    case ECaseAdd(src, t, a, s) => s"[$t $src] Added case [$a] to start at: $s"
    case EArrivalAdd(src, t, a, s, l) =>
      s"[$t $src] Added arrival of [$a] with limit [$l] to start at: $s"
    case ECaseStart(src, t, n) => s"[$t $src] Starting case: $n"
    case ECaseEnd(src, t, n, r) => s"[$t $src] Case [$n] completed. Result: $r"
    case ETaskAdd(src, t, task) =>
      s"[$t $src] Added task [${task.name}](${task.caseName}) created at [${task.created}]. (id:${task.id})"
    case ETaskStart(src, t, task) =>
      s"[$t $src] Starting task [${task.name}](${task.caseName}). Ticks: ${task.duration}. (id:${task.id})"
    case ETaskAttach(src, t, task, r) =>
      s"[$t $src] Attaching task [${task.name}](${task.caseName}) to [${r.resource.name}]. Ticks: ${task.duration} - Capacity left: ${r.remainingCapacity}. (id:${task.id})"
    case ETaskDetach(src, t, start, task, r) =>
      s"[$t $src] Detaching task [${task.name}](${task.caseName}) from [${r.resource.name}]. Running since: $start - Capacity left: ${r.remainingCapacity}. (id:${task.id})"
    case ETaskDone(src, t, task) =>
      s"[$t $src] Task [${task.name}](${task.caseName}) completed. (id:${task.id})"
    case ETaskAbort(src, t, id) =>
      s"[$t $src] Task id [$id] was aborted."

    case EError(src, t, error) => s"[$t $src] !ERROR! $error !ERROR!"
  }
}
