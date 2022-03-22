package com.workflowfm.proter
package state

import cases.CaseRef
import events.*
import schedule.Scheduler

import cats.Monad
import cats.implicits.*

import scala.collection.immutable.HashSet
import java.util.UUID


case class State[F[_] : Monad, C[T] <: Iterable[T]](
  id: String,
  scheduler: Scheduler,
  time: Long, 
  events: EventQueue,
  tasks: C[TaskInstance],
  cases: Map[String, CaseRef[F]], 
  resources: ResourceMap,
  waiting: HashSet[String], 
  abortedTasks: HashSet[UUID],
) {

  def +(r: Resource): State[F, C] = copy(resources = resources.addResource(r))
  def ++(r: Seq[Resource]): State[F, C] = copy(resources = resources.addResources(r))


  def tick(publisher: Publisher[F]): F[StateResult[F, C]] = {
    if !waiting.isEmpty // still waiting for responses!
    then for {
      _ <- publisher.publish(
        EError(id, time, s"Called `tick()` even though I am still waiting for: $waiting") 
      ) } yield (StateDone(this))

    else if (events.isEmpty && tasks.isEmpty && cases.isEmpty) // finished!
    then for {
      _ <- publisher.publish(EDone(id, time)) 
    } yield (StateDone(this)) 
/*  
    else if !events.isEmpty // Are events pending?
    then { 
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

    } else if waiting.isEmpty && !scheduler.noMoreTasks() then { // this may happen if handleDiscreteEvent fails
      allocateTasks()
      tick()
    } // else {
    // publish(EError(self, time, "No tasks left to run, but simulations have not finished."))
    // }*/
    else Monad[F].pure(StateDone(this))
  }


}
