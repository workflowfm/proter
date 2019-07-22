package com.worklflowfm.simulator.events

import java.util.UUID

sealed trait Event {
  val time: Long
}

case object EStart extends Event

case class ESimAdd(override val time: Long, name: String, start: Long) extends Event

case class ESimStart(override val time: Long, name: String) extends Event

case class ESimEnd(override val time: Long, name: String, result: String) extends Event

case class ETaskAdd(
  override val time: Long,
  id: UUID,
  creation: Long,
  name: String,
  simulation: String
) extends Event

case class ETaskStart(
  override val time: Long,
  id: UUID,
  name: String,
  simulation: String
) extends Event

case class ETaskAttach(
  override val time: Long,
  id: UUID,
  name: String,
  simulation: String,
  resource: String,
  duration: Long
) extends Event

case class ETaskDetach(
  override val time: Long,
  id: UUID,
  name: String,
  simulation: String,
  resource: String
) extends Event

case class EDone(override val time: Long) extends Event

case class EError(override val time: Long, error: String) extends Event

object Event {
  def asString(e: Event): String = e match {
    case EStart => "=== Simulation started! ==="
    case EDone(t) => s"=== Simulation complete! Total ticks: $t ==="
    case ESimAdd(t,n,s) => s"[$t] Added simulation [$n] to be run at: $s"
    case ESimStart(t,n) => s"[$t] Starting simulation: $n"
    case ESimEnd(t,n,r) => s"[$t] Simulation [$n] completed. Result: $r"
    case ETaskAdd(t,id,c,n,s) => s"[$t] Added task [$n]($s) created at [$c]. (id:$id)"
    case ETaskStart(t,id,n,s) => s"[$t] Starting task [$n]($s). (id:$id)"
    case ETaskAttach(t,id,n,s,r,d) => s"[$t] Attaching task [$n]($s) to [$r]. Ticks remaining: $d. (id:$id)"
    case ETaskDetach(t,id,n,s,r) => s"[$t] Detaching task [$n]($s) from [$r]. (id:$id)"
    case EError(t, error) => s"[$t] !ERROR! $error !ERROR!"
  }
}
