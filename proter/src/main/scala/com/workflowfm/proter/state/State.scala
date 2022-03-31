package com.workflowfm.proter
package state

import cases.CaseRef
import events.*
import schedule.Scheduler

import cats.Monad
import cats.data.State
import cats.implicits.*

import scala.annotation.tailrec
import scala.collection.immutable.{ HashSet, Set, Map }
import java.util.UUID


case class Simulationx[F[_]](
  id: String,
  scheduler: Scheduler,
  time: Long, 
  events: EventQueue,
  tasks: Set[TaskInstance],
  cases: Map[String, CaseRef[F]], 
  resources: ResourceMap,
  waiting: Map[String, Seq[TaskInstance]], 
  abortedTasks: HashSet[UUID],
) {
  def error(description: String): EError = EError(id, time, description)
}

object Simulationx {

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
  def startCase[F[_]](caseRef: CaseRef[F]): State[Simulationx[F], Event] = State ( sim =>
    (sim.copy(
      waiting = sim.waiting + (caseRef.caseName -> Seq()),
      cases = sim.cases + (caseRef.caseName -> caseRef)
    ),
      ECaseStart(sim.id, sim.time, caseRef.caseName))
  )

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
  def stopCase(name: String, result: String): State[Simulationx[?], Event] = State ( sim => 
    (sim.copy(
      waiting = sim.waiting - name,
      cases = sim.cases - name,
    ),
      ECaseEnd(sim.id, sim.time, name, result))
  )
    //scheduler.removeLookahead(name)
    // log.debug(s"[COORD:$time] Finished: [$name]")
    //ready(name)
  

  /**
    * Aborts one or more [[TaskInstance]]s.
    *
    *   - Detaches all associated [[TaskResource]]s publishing a
    *     [[com.workflowfm.proter.events.ETaskDetach ETaskDetach]] each time.
    *   - Adds the task ID to the [[abortedTasks]] set.
    *   - Publishes a [[com.workflowfm.proter.events.ETaskAbort ETaskAbort]].
    *
    * @group tasks
    * @param id
    *   The `UUID`s of the [[TaskInstance]]s that need to be aborted.
    */
  def abortTasks(ids: Seq[UUID]): State[Simulationx[?], Seq[Event]] = State ( sim => {
    val (abortMap, abortResources) = sim.resources.abortTasks(ids)
    val abortEvents = ids.map { taskid => ETaskAbort(sim.id, sim.time, taskid) }
    val detachEvents = abortResources.flatMap(ETaskDetach.resourceState(sim.id, sim.time))
    (sim.copy(resources = abortMap, abortedTasks = sim.abortedTasks ++ ids), abortEvents ++ detachEvents)
  })

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
  def tick[F[_]](): State[Simulationx[F], Seq[Event]] = State ( sim => 
    if !sim.waiting.isEmpty // still waiting for responses!
    then (sim, Seq(sim.error(s"Called `tick()` even though I am still waiting for: ${sim.waiting}")))
    else if (sim.events.isEmpty && sim.tasks.isEmpty && sim.cases.isEmpty) // finished!
    then (sim, Seq(EDone(sim.id, sim.time))) 
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
    else (sim, Seq())
  )

}


trait CaseState {
/*
  private def publishThen[T](publisher: Publisher[F], events: Event*) (v: T): F[T] =
    for {
      _ <- events.map(publisher.publish(_)).sequence
    } yield (v)

  private def errorThen[T](publisher: Publisher[F], error: String) (v: T): F[T] = publishThen(publisher, EError(id, time, error))(v)
 */

  def addResource(r: Resource): State[Simulationx[?], Event] = State( sim =>
    (sim.copy(resources = sim.resources.addResource(r)), EResourceAdd(sim.id, sim.time, r.name, r.costPerTick))
  )

  def addResources(rs: Seq[Resource]): State[Simulationx[?], Seq[Event]] = State( sim => {
    val events = rs.map { r => EResourceAdd(sim.id, sim.time, r.name, r.costPerTick) }
    (sim.copy(resources = sim.resources.addResources(rs)), events)
  })

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
  def addCase[F[_]](t: Long, caseRef: CaseRef[F]): State[Simulationx[F], Event] = State( sim =>
    if t >= sim.time
    then (sim.copy(events = sim.events + StartingCase(t, caseRef)), ECaseAdd(sim.id, sim.time, caseRef.caseName, t))
    else (sim, sim.error(s"Attempted to start case [${caseRef.caseName}] in the past: $t"))
  )

  /**
    * Add a new simulation to be run in the current virtual time.
    *
    * Publishes a [[com.workflowfm.proter.events.ESimAdd ESimAdd]] for each simulation.
    *
    * @group simulations
    * @param simulation
    *   The [[Simulation]] to run.
    */
  def addCaseNow[F[_]](caseRef: CaseRef[F]): State[Simulationx[F], Event] = State ( sim => 
    addCase(sim.time, caseRef).run(sim).value
  )

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
  def addCases[F[_]](cases: Seq[(Long, CaseRef[F])]): State[Simulationx[F], Seq[Event]] =
    cases.map( (t, c) => addCase(t, c) ).sequence

  /**
    * Add multiple simulations to be run in the current virtual time.
    *
    * @group simulations
    * @param sims
    *   A sequence of [[Simulation]]s.
    */
  def addCasesNow[F[_]](cases: Seq[CaseRef[F]]): State[Simulationx[F], Seq[Event]] = State ( sim => 
    addCases(cases.map((sim.time, _))).run(sim).value
  )


  /**
    * Sets a time limit in virtual time for the simulation to end.
    *
    * @note
    *   Once a time limit is placed it cannot be removed. Multiple time limits can be set so that
    *   the earliest one will be triggered.
    *
    * @group toplevel
    * @param t
    *   The virtual timestamp to end the simulation.
    */
  def limit(t: Long):  State[Simulationx[?], Event] = State ( sim => 
    if t >= sim.time
    then (sim.copy(events = sim.events + TimeLimit(t)), ETimeLimit(sim.id, sim.time, t))
    else (sim, sim.error(s"Attempted to set time limit in the past. Limit: [$t] < Current time: [${sim.time}]."))
  )
}