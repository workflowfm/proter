package com.workflowfm.simulator.events

import com.workflowfm.simulator.Task
import java.util.UUID

sealed trait Event {
  val time: Long
}

case object EStart extends Event {
  override val time: Long = 0L
}

case class EResourceAdd(override val time: Long, name: String, costPerTick: Int) extends Event

case class ESimAdd(override val time: Long, name: String, start: Long) extends Event

case class ESimStart(override val time: Long, name: String) extends Event

case class ESimEnd(override val time: Long, name: String, result: String) extends Event

case class ETaskAdd(
  override val time: Long,
  task: Task
) extends Event

case class ETaskStart(
  override val time: Long,
  task: Task
) extends Event

case class ETaskAttach(
  override val time: Long,
  task: Task,
  resource: String,
) extends Event

case class ETaskDetach(
  override val time: Long,
  task: Task,
  resource: String
) extends Event

case class ETaskDone(
  override val time: Long,
  task: Task,
) extends Event

case class EDone(override val time: Long) extends Event

case class EError(override val time: Long, error: String) extends Event

object Event {
  def asString(e: Event): String = e match {
    case EStart => "=== Simulation started! ==="
    case EDone(t) => s"=== Simulation complete! Total ticks: $t ==="
    case EResourceAdd(t,n,c) => s"[$t] Added resource: $n (CPT:$c)"
    case ESimAdd(t,a,s) => s"[$t] Added simulation actor [$a] to be run at: $s"
    case ESimStart(t,n) => s"[$t] Starting simulation: $n"
    case ESimEnd(t,n,r) => s"[$t] Simulation [$n] completed. Result: $r"
    case ETaskAdd(t,task) => s"[$t] Added task [${task.name}](${task.simulation}) created at [${task.created}]. (id:${task.id})"
    case ETaskStart(t,task) => s"[$t] Starting task [${task.name}](${task.simulation}). Ticks remaining: ${task.duration}. (id:${task.id})"
    case ETaskAttach(t,task,r) => s"[$t] Attaching task [${task.name}](${task.simulation}) to [$r]. Ticks remaining: ${task.duration}. (id:${task.id})"
    case ETaskDetach(t,task,r) => s"[$t] Detaching task [${task.name}](${task.simulation}) from [$r]. (id:${task.id})"
    case ETaskDone(t,task) => s"[$t] Task [${task.name}](${task.simulation}) completed. (id:${task.id})"

    case EError(t, error) => s"[$t] !ERROR! $error !ERROR!"
  }
}
