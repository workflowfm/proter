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
    * A string representing the [[State]] that generated the event.
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
  * A time limit was set.
  *
  * @param limit
  *   The limit in time units/
  */
case class ETimeLimit(
    override val source: String,
    override val time: Long,
    limit: Long
) extends Event

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
    resource: Resource
) extends Event

/**
  * A case was added.
  *
  * @param name
  *   The name of the case.
  * @param start
  *   The timestamp when this case is scheduled to start.
  */
case class ECaseAdd(
    override val source: String,
    override val time: Long,
    name: String,
    start: Long
) extends Event

/**
  * An arrival was added.
  *
  * @param name
  *   The name of the arriving cases.
  * @param start
  *   The timestamp when arrivals is scheduled to start.
  * @param rate
  *   The arrival rate.
  * @param limit
  *   The optional limit of the number of cases to generate.
  */
case class EArrivalAdd(
    override val source: String,
    override val time: Long,
    name: String,
    start: Long,
    rate: LongDistribution,
    limit: Option[Int]
) extends Event

/**
  * A case was started.
  *
  * @param name
  *   The name of the case.
  */
case class ECaseStart(override val source: String, override val time: Long, name: String)
    extends Event

/**
  * A case was completed.
  *
  * @param name
  *   The name of the case.
  * @param result
  *   The output of the case (if any).
  */
case class ECaseEnd(
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
    resource: ResourceState
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
    resource: ResourceState,
    cost: Double
) extends Event

object ETaskDetach {

  def resourceState(source: String, time: Long, taskIds: Seq[UUID])(resourceState: ResourceState): Seq[ETaskDetach] =
    resourceState
      .currentTasks
      .view
      .filterKeys(taskIds.contains(_))
      .map { case (id, (start, task)) =>
        ETaskDetach(
          source,
          time,
          task,
          resourceState,
          resourceState.resource.costPerTick * (time - start)
        )
      }
      .toSeq
}

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
    case ETimeLimit(src, t, l) => s"[$t $src] Set time limit at: $l"
    case EResourceAdd(src, t, r) => s"[$t $src] Added resource: [${r.name}]. Capacity: ${r.capacity} - Cost/tick: ${r.costPerTick})"
    case ECaseAdd(src, t, a, s) => s"[$t $src] Added case [$a] to start at: $s"
    case EArrivalAdd(src, t, a, s, r, l) =>
      s"[$t $src] Added arrival for cases [$a] with rate [$r] limit [$l] to start at: $s"
    case ECaseStart(src, t, n) => s"[$t $src] Starting case: $n"
    case ECaseEnd(src, t, n, r) => s"[$t $src] Case [$n] completed. Result: $r"
    case ETaskAdd(src, t, task) =>
      s"[$t $src] Added task [${task.name}](${task.simulation}) created at [${task.created}]. (id:${task.id})"
    case ETaskStart(src, t, task) =>
      s"[$t $src] Starting task [${task.name}](${task.simulation}). Ticks: ${task.duration}. (id:${task.id})"
    case ETaskAttach(src, t, task, r) =>
      s"[$t $src] Attaching task [${task.name}](${task.simulation}) to [${r.resource.name}]. Ticks: ${task.duration} - Capacity left: ${r.remainingCapacity}. (id:${task.id})"
    case ETaskDetach(src, t, task, r, c) =>
      s"[$t $src] Detaching task [${task.name}](${task.simulation}) from [${r.resource.name}]. Cost: $c - Capacity left: ${r.detach(task.id).remainingCapacity}. (id:${task.id})"
    case ETaskDone(src, t, task) =>
      s"[$t $src] Task [${task.name}](${task.simulation}) completed. (id:${task.id})"
    case ETaskAbort(src, t, id) =>
      s"[$t $src] Task id [$id] was aborted."

    case EError(src, t, error) => s"[$t $src] !ERROR! $error !ERROR!"
  }
}
