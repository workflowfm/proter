package com.workflowfm.proter.akka

import java.util.UUID

import scala.util.Try

import akka.actor.{ ActorSystem, Actor, ActorRef, Props }

import com.workflowfm.proter._


case class AkkaSimulationRef(simName: String, simulation: ActorRef) extends SimulationRef {

  override def name: String = simName
  override def run(): Unit = simulation ! SimulationActor.Run
  override def stop(): Unit = simulation ! SimulationActor.Stop

  override def completed(time: Long, tasks: Seq[TaskInstance]): Unit =
    simulation ! SimulationActor.Completed(time, tasks)
}

object AkkaSimulationRef {

  def of(simulation: Simulation)(implicit system: ActorSystem): SimulationRef = {
    AkkaSimulationRef(simulation.name, system.actorOf(Props(new SimulationActor(simulation))))
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
