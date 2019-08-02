package com.workflowfm.simulator.events

import akka.actor.ActorRef
import com.workflowfm.simulator.Task
import java.util.UUID

sealed trait Event {
  val source: ActorRef
  val time: Long

  override def toString = Event.asString(this)
}

case class EStart(override val source:ActorRef) extends Event {
  override val time: Long = 0L
}

case class EResourceAdd(override val source:ActorRef, override val time: Long, name: String, costPerTick: Int) extends Event

case class ESimAdd(override val source:ActorRef, override val time: Long, name: String, start: Long) extends Event

case class ESimStart(override val source:ActorRef, override val time: Long, name: String) extends Event

case class ESimEnd(override val source:ActorRef, override val time: Long, name: String, result: String) extends Event

case class ETaskAdd(
  override val source:ActorRef, 
  override val time: Long,
  task: Task
) extends Event

case class ETaskStart(
  override val source:ActorRef, 
  override val time: Long,
  task: Task
) extends Event

case class ETaskAttach(
  override val source:ActorRef, 
  override val time: Long,
  task: Task,
  resource: String,
) extends Event

case class ETaskDetach(
  override val source:ActorRef, 
  override val time: Long,
  task: Task,
  resource: String
) extends Event

case class ETaskDone(
  override val source:ActorRef, 
  override val time: Long,
  task: Task,
) extends Event

case class EDone(override val source:ActorRef, override val time: Long) extends Event

case class EError(override val source:ActorRef, override val time: Long, error: String) extends Event

object Event {
  def asString(e: Event): String = e match {
    case EStart(src) => s"[$src] === Simulation started! ==="
    case EDone(src,t) => s"[$src] === Simulation complete! Total ticks: $t ==="
    case EResourceAdd(src,t,n,c) => s"[$t $src] Added resource: $n (CPT:$c)"
    case ESimAdd(src,t,a,s) => s"[$t $src] Added simulation actor [$a] to be run at: $s"
    case ESimStart(src,t,n) => s"[$t $src] Starting simulation: $n"
    case ESimEnd(src,t,n,r) => s"[$t $src] Simulation [$n] completed. Result: $r"
    case ETaskAdd(src,t,task) => s"[$t $src] Added task [${task.name}](${task.simulation}) created at [${task.created}]. (id:${task.id})"
    case ETaskStart(src,t,task) => s"[$t $src] Starting task [${task.name}](${task.simulation}). Ticks remaining: ${task.duration}. (id:${task.id})"
    case ETaskAttach(src,t,task,r) => s"[$t $src] Attaching task [${task.name}](${task.simulation}) to [$r]. Ticks remaining: ${task.duration}. (id:${task.id})"
    case ETaskDetach(src,t,task,r) => s"[$t $src] Detaching task [${task.name}](${task.simulation}) from [$r]. (id:${task.id})"
    case ETaskDone(src,t,task) => s"[$t $src] Task [${task.name}](${task.simulation}) completed. (id:${task.id})"

    case EError(src,t, error) => s"[$t $src] !ERROR! $error !ERROR!"
  }
}
