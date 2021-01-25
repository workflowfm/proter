package com.workflowfm.proter.akka

import java.util.UUID

import scala.util.Try

import akka.actor.{ ActorSystem, Actor, ActorRef, Props }

import com.workflowfm.proter._

/*
class AkkaSimulation {



  /**
 * Stops/aborts the simulation.
 *
 * All the necessary steps for a gentle shutdown should be taken here.
 *
 * @group react
 */
  def stop(): Unit

  /**
 * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation.
 *
 * @group act
 *
 * @param t The [[TaskGenerator]] to send.
 * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
 */
  def task(t: TaskGenerator): Unit = {
    coordinator ! Coordinator.AddTask(t)
  }

  /**
 * Notifies the [[Coordinator]] that some [[Task]]s should be aborted.
 *
 * @group act
 *
 * @param id The `UUID` of the [[Task]]s.
 */
  protected def abort(ids: UUID*): Unit = {
    coordinator ! Coordinator.AbortTasks(ids)
  }

  /**
 * Starts the simulation via the [[run]] function.
 *
 * Notifies the [[Coordinator]] that the simulation started. Also makes sure
 * the [[Coordinator]] is notified when the simulation completes.
 *
 * @group internal
 */
  protected def start(): Unit = {
    coordinator ! Coordinator.SimStarted(name, self)
    run()
  }

  /**
 * Notifies the [[Coordinator]] that the simulation completed.
 *
 * @group act
 * @param result The result of the simulation.
 */
  protected def done(result: Try[Any]): Unit = {
    coordinator ! Coordinator.SimDone(name, result)
  }

  /**
 * Notifies the [[Coordinator]] that the simulation completed successfully.
 *
 * @group act
 * @param result The successful result of the simulation.
 */
  protected def succeed(result: Any): Unit = {
    coordinator ! Coordinator.SimDone(name, Success(result))
  }

  /**
 * Notifies the [[Coordinator]] that the simulation has failed or has been aborted.
 *
 * @group act
 * @param exception The `Throwable` that caused the failure.
 */
  protected def fail(exception: Throwable): Unit = {
    coordinator ! Coordinator.SimDone(name, Failure(exception))
  }

  /**
 * Notifies the [[Coordinator]] that the simulation has finished processing
 * one or more completed [[Task]]s.
 *
 * Identifies the tasks via their UUID.
 *
 * @group act
 */
  def ack(tasks: Seq[UUID], lookahead: Option[Lookahead] = None): Unit = {
    coordinator ! Coordinator.AckTasks(tasks, lookahead)
  }

  /**
 * Notifies the [[Coordinator]] that the simulation has finished calculating
 * and is ready for virtual time to proceed.
 *
 * @group act
 */
  def ready(lookahead: Option[Lookahead] = None): Unit = {
    coordinator ! Coordinator.SimReady(lookahead)
  }

  /**
 * Requests that the [[Coordinator]] waits for this simulation before it continues.
 *
 * The simulation needs to either register more tasks and become ``ready`` or finish.
 *
 * In other words, the [[Coordinator]] will expect either a `SimReady` (via [[ready]])
 * or `SimDone` (via [[done]], [[succeed]] or [[fail]]) before it continues.
 *
 * @note We assume the [[Coordinator]] is already waiting for another simulation when the
 *       request is made. Otherwise virtual time may progress unexpectedly and cause
 *       unpredictable behaviour depending on the timing of the [[Coordinator]] messages.
 *
 * @group act
 *
 * @return A `Future` of the acknowledgement message [[Simulation.AckWait]]
 */
  def simWait(): Future[Any] = {
    (coordinator ? Coordinator.WaitFor(self))(Timeout(1, TimeUnit.DAYS))
  }

  /**
 * Defines the actor receive behaviour for the simulation interface.
 *
 * This allows subclasses to extend [[receive]] with additional behaviour.
 *
 * @group internal
 *
 * @return The [[Receive]] behaviour for the simulation interface.
 */

}*/

class AkkaSimulationRef(simName: String, simulation: ActorRef) extends SimulationRef {

  override def name: String = simName
  override def run(): Unit = simulation ! SimulationActor.Run
  override def stop(): Unit = simulation ! SimulationActor.Stop

  override def completed(time: Long, tasks: Seq[TaskInstance]): Unit =
    simulation ! SimulationActor.Completed(time, tasks)
}

object AkkaSimulationRef {

  def of(simulation: Simulation)(implicit system: ActorSystem): SimulationRef = {
    new AkkaSimulationRef(simulation.name, system.actorOf(Props(new SimulationActor(simulation))))
  }
}

class SimulationActor(final val simulation: Simulation) extends Actor {

  def receive: Receive = {
    case SimulationActor.Run => simulation.run()
    case SimulationActor.Ready => simulation.ready()
    case SimulationActor.Completed(time, tasks) => simulation.completed(time, tasks)
    case SimulationActor.AddTasks(tasks) => tasks foreach (simulation.task(_))
    case SimulationActor.AckTasks(tasks) => simulation.ack(tasks)
    case SimulationActor.AbortTasks(ids) => simulation.abort(ids: _*)
    case SimulationActor.Wait => simulation.wait()
    case SimulationActor.Done(result) => simulation.done(result)
    case SimulationActor.Succeed(result) => simulation.succeed(result)
    case SimulationActor.Fail(exception) => simulation.fail(exception)
    case SimulationActor.Stop => simulation.stop()
  }

}

/**
  * Defines the messages a [[Simulation]] can receive by default.
  *
  * @groupname coordinator Sent by Coordinator
  * @groupname process Sent by SimulatedProcess
  */
object SimulationActor {
  /**
    * Instructs the start of simulation logic execution.
    *
    * @group coordinator
    */
  case object Run
  /**
    * Triggers [[Simulation.ready]].
    *
    * @see [[Simulation.ready]]
    * @group process
    */
  case object Ready

  /**
    * Produces a new [[TaskGenerator]] for simulation.
    *
    * @see [[Simulation.task(t* task(t, resources)]]
    * @group process
    *
    * @param t The [[TaskGenerator]] to generate the [[Task]].
    */
  case class AddTasks(tasks: Seq[Task])

  /**
    * Aborts a list of [[Task]]s.
    *
    * @see [[Simulation.abort]]
    * @group process
    *
    * @param ids The [[Task]] IDs to abort.
    */
  case class AbortTasks(ids: Seq[UUID])

  /**
    * Informs a [[Task]] has completed
    *
    * @see [[Simulation.complete]]
    * @group coordinator
    *
    * @param task The completed [[Task]].
    * @param time The (virtual) time of completion.
    */
  case class Completed(time: Long, tasks: Seq[TaskInstance])

  /**
    * Tells the [[Simulation]] to request that [[Coordinator]] waits.
    *
    * @group process
    */
  case object Wait
  /**
    * Acknowledges that the [[Coordinator]] is waiting as requested.
    *
    * @group coordinator
    */
  case object AckWait

  /**
    * Acknowledges a sequence of completed [[Task]] IDs has been processed.
    *
    * @see [[Simulation.ack]]
    * @group process
    *
    * @param task The acknowledged [[Task]] UUIDs.
    */
  case class AckTasks(tasks: Seq[UUID])

  /**
    * Tells the [[Simulation]] to complete.
    *
    * @see [[Simulation.done]]
    * @group process
    *
    * @param result The successful simulation result.
    */
  case class Done(result: Try[Any])

  /**
    * Tells the [[Simulation]] to complete successfully.
    *
    * @see [[Simulation.succeed]]
    * @group process
    *
    * @param result The successful simulation result.
    */
  case class Succeed(result: Any)

  /**
    * Tells the [[Simulation]] to fail.
    *
    * @see [[Simulation.fail]]
    * @group process
    *
    * @param exception The `Throwable` causing the failure.
    */
  case class Fail(exception: Throwable)

  /**
    * Tells the [[Simulation]] to stop.
    *
    * @see [[Simulation.stop]]
    * @group coordinator
    */
  case object Stop

}
