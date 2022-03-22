package com.workflowfm.proter
package state

import cases.CaseRef
import events.*
import schedule.Scheduler

import cats.Monad
import cats.implicits.*

import scala.annotation.tailrec
import scala.collection.immutable.{ HashSet, Map }
import java.util.UUID


case class State[F[_] : Monad, C[T] <: Iterable[T]](
  id: String,
  scheduler: Scheduler,
  time: Long, 
  events: EventQueue,
  tasks: C[TaskInstance],
  cases: Map[String, CaseRef[F]], 
  resources: ResourceMap,
  waiting: Map[String, Seq[TaskInstance]], 
  abortedTasks: HashSet[UUID],
) {

  private def publishThen[T](publisher: Publisher[F], events: Event*) (v: T): F[T] =
    for {
      _ <- events.map(publisher.publish(_)).sequence
    } yield (v)

  private def errorThen[T](publisher: Publisher[F], error: String) (v: T): F[T] = publishThen(publisher, EError(id, time, error))(v)


  def addResource(publisher: Publisher[F], r: Resource): F[State[F, C]] = 
    publishThen(publisher, EResourceAdd(id, time, r.name, r.costPerTick))(
      copy(resources = resources.addResource(r))
    )
  def addResources(publisher: Publisher[F], rs: Seq[Resource]): F[State[F, C]] = {
    val events = rs.map { r => EResourceAdd(id, time, r.name, r.costPerTick) }
    publishThen(publisher, events *)(
      copy(resources = resources.addResources(rs))
    )
  }

  /**
    * Add a new simulation to be run.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimAdd ESimAdd]].
    *
    * @group simulations
    * @param t
    *   The timestamp when the simulation needs to start. Must be greater or equal to the current
    *   time.
    * @param simulation
    *   The [[Simulation]] to run.
    */
  def addCase(publisher: Publisher[F], t: Long, caseRef: CaseRef[F]): F[State[F, C]] = 
    if t >= time
    then publishThen(publisher, ECaseAdd(id, time, caseRef.caseName, t))(
      copy(events = events + StartingCase(t, caseRef))
    )
    else errorThen(publisher, s"Attempted to start case [${caseRef.caseName}] in the past: $t")(this)

  /**
    * Add a new simulation to be run in the current virtual time.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimAdd ESimAdd]] for each simulation.
    *
    * @group simulations
    * @param simulation
    *   The [[Simulation]] to run.
    */
  def addCaseNow(publisher: Publisher[F], caseRef: CaseRef[F]): F[State[F, C]] = addCase(publisher, time, caseRef)

  /**
    * Adds multiple simulations at the same time.
    *
    * This is equivalent to mapping [[addSimulation]] over the given sequence, but more efficient.
    *
    * @group simulations
    * @param sims
    *   A sequence of pairs, each consisting of a starting timestamp and a [[Simulation]].
    *   Timestamps must be greater or equal to the current time.
    */
  def addCases(publisher: Publisher[F], cases: Seq[(Long, CaseRef[F])]): F[State[F, C]] = cases match {
    case Nil => Monad[F].pure(this)
    case (t, c) :: cs => addCase(publisher, t, c).flatMap(_.addCases(publisher,cs)) // TODO this can be more efficient
  }


  /**
    * Add multiple simulations to be run in the current virtual time.
    *
    * @group simulations
    * @param sims
    *   A sequence of [[Simulation]]s.
    */
  def addCasesNow(publisher: Publisher[F], cases: Seq[CaseRef[F]]): F[State[F, C]] = addCases(publisher, cases.map((time, _)))




  /**
    * Start a [[CaseRef]].
    *
    * Once the case starts, we expect to hear from it in case it wants to add some [[Task]]s.
    * We therefore add it to the waiting queue.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimStart ESimStart]].
    *
    * @group simulations
    * @param caseRef
    *   The [[CaseRef]] to start.
    */
  protected def startCase(publisher: Publisher[F], caseRef: CaseRef[F]): F[State[F, C]] = 
    publishThen(publisher, ECaseStart(id, time, caseRef.caseName))(
      copy(
        waiting = waiting + (caseRef.caseName -> Seq()),
        cases = cases + (caseRef.caseName -> caseRef)
      ))
    // simulation.run() !!!

  /**
    * Stops a case when it is done.
    *
    *   - Removes the simulation from the list of running simulations and the waiting list.
    *   - Removes any [[Lookahead]] from the [[schedule.Scheduler Scheduler]].
    *   - Publishes a [[com.workflowfm.proter.events.ESimEnd ESimEnd]].
    *   - Calls [[ready]] to handle the fact that we no longer need to wait for this simulation.
    *
    * @group simulations
    * @param name
    *   The name of the completed simulation.
    * @param result
    *   A string representation of the output of the simulation.
    */
  protected def stopCase(publisher: Publisher[F], name: String, result: String): F[State[F, C]] = 
    publishThen(publisher, ECaseEnd(id, time, name, result))(
      copy(
        waiting = waiting - name,
        cases = cases - name,
      ))
    //scheduler.removeLookahead(name)
    // log.debug(s"[COORD:$time] Finished: [$name]")
    //ready(name)
  

/*
  /**
    * Handles a [[SimDone]] response from a simulation, indicating that it has completed.
    *
    * Calls [[stopSimulation]] according to the reported success or failure result.
    *
    * @group simulations
    * @param simulation
    *   The name of the simulation
    * @param result
    *   The result of completion
    */
  protected def caseDone(caseName: String, result: Try[Any]): Unit = {
    result match {
      case Success(res) => {
        stopSimulation(simulation, res.toString)
      }
      case Failure(ex) => {
        stopSimulation(simulation, ex.getLocalizedMessage)
      }
    }
  }
 */
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
