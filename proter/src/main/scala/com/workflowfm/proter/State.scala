package com.workflowfm.proter

import cases.CaseRef
import schedule.Scheduler

import scala.collection.immutable.HashSet
import java.util.UUID


case class State[F[_], C[T] <: Iterable[T]](
  time: Long, 
  scheduler: Scheduler,
  events: EventQueue,
  tasks: C[TaskInstance],
  cases: Map[String, CaseRef[F]], 
  resources: ResourceMap,
  waiting: HashSet[String], 
  abortedTasks: HashSet[UUID],
) {

  def +(r: Resource): State[F, C] = copy(resources = resources.addResource(r))
  def ++(r: Seq[Resource]): State[F, C] = copy(resources = resources.addResources(r))

/*
  final def tick(): Either[Unit, State[F, C]] = {
    if !waiting.isEmpty then { // This should never happen, but we add it here as a safeguard for
      // future extensions.
      publish(EError(id, time, s"Called `tick()` even though I am still waiting for: $waiting"))
    } else if !events.isEmpty then { // Are events pending?
      processing = true

      // Grab the first event
      val firstEvent = events.head

      // Did we somehow go past the event time? This should never happen.
      if firstEvent.time < time then {
        publish(EError(id, time, s"Unable to handle past event for time: [${firstEvent.time}]"))
      } else {
        // Jump ahead to the event time. This is a priority queue so we shouldn't skip any events
        time = firstEvent.time

        // Dequeue all the events that need to happen now
        val eventsToHandle = dequeueEvents(firstEvent.time)

        eventsToHandle.flatMap(filterFinishingTasks).groupBy(_.simulation).foreach(stopTasks)

        // Handle the event
        eventsToHandle foreach handleDiscreteEvent

        processing = false
        // If we are not waiting for anything, continue
        if waiting.isEmpty then {
          allocateTasks()
          tick()
        }
      }

    } else if scheduler.noMoreTasks() && simulations.isEmpty && !promise.isCompleted then {
      finish()
    } else if waiting.isEmpty && !scheduler.noMoreTasks() then { // this may happen if handleDiscreteEvent fails
      allocateTasks()
      tick()
    } // else {
    // publish(EError(self, time, "No tasks left to run, but simulations have not finished."))
    // }
  }
 */

}
