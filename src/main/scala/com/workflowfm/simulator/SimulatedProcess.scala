package com.workflowfm.simulator

import akka.pattern.ask
import akka.actor.ActorRef
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{ ExecutionContext, Future }
import java.util.UUID

/**
  * Behaviour of a process that interacts with a [[Simulation]] to simulate [[Task]]s
  * through [[TaskGenerator]]s.
  *
  * This is useful when the simulation is distributed in multiple actors.
  * A [[SimulatedProcess]] can live in a different actor than the [[Simulation]]
  * and send [[TaskGenerator]]s to it.
  */
trait SimulatedProcess {
  /**
    * The name of the simulation this process belongs to.
    */
  def simulationName: String

  /**
    * A reference to the [[Simulation]] of the corresponding simulation.
    */
  def simulationActor: ActorRef

  /**
    * Simulate a [[Task]] with a pre-determined ID.
    *
    * This method sends a [[TaskGenerator]] to the [[Simulation]].
    * When a corresponding [[Task]] has been simulated, the method calculates a custom
    * output of type `T` based on the [[Task]] and its completion time using `result`.
    * This is returned via a `Future` that completes when the [[Task]]
    * is completed in the simulation.
    *
    * @note Uses the Akka `ask` pattern to receive a response from the [[Simulation]]
    *       when the [[Task]] completes. This blocks other interactions the actor of the
    *       [[SimulatedProcess]] can perform from now until the [[Task]] completes.
    *
    * @param id The pre-determined ID for the [[Task]].
    * @param gen The [[TaskGenerator]] that will generate the [[Task]].
    * @param result A function returning a custom output.
    * @param executionContext
    * @return A `Future` with the calculated custom result when the [[Task]] is
    *         completed in the simulation.
    */
  def simulate[T](
      gen: TaskGenerator,
      result: (Task, Long) => T,
  )(implicit executionContext: ExecutionContext): Future[T] = {
    (simulationActor ? Simulation.AddTask(gen))(Timeout(1, TimeUnit.DAYS))
      .mapTo[(Task, Long)]
      .map { case (task, time) => result(task, time) }
  }

  def simWait() {
    (simulationActor ? Simulation.Wait)(Timeout(1, TimeUnit.DAYS))
  }
}
