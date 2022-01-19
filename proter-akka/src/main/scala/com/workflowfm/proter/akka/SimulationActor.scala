package com.workflowfm.proter.akka

import java.util.UUID

import scala.util.Try

import akka.actor.{ ActorSystem, Actor, ActorRef, Props }

import com.workflowfm.proter._

/**
  * Akka-based [[com.workflowfm.proter.SimulationRef SimulationRef]] implementation.
  *
  * Delegates all interaction to an actor via messaging.
  *
  * @param simName
  *   The name of the simulation.
  * @param simulation
  *   The `ActorRef` of the actor.
  */
case class AkkaSimulationRef(simName: String, simulation: ActorRef) extends SimulationRef {

  override def name: String = simName
  override def run(): Unit = simulation ! SimulationRefActor.Run
  override def stop(): Unit = simulation ! SimulationRefActor.Stop

  override def completed(time: Long, tasks: Seq[TaskInstance]): Unit =
    simulation ! SimulationRefActor.Completed(time, tasks)
}

object AkkaSimulationRef {

  /**
    * Builds a [[com.workflowfm.proter.SimulationRef SimulationRef]] for an actor-based simulation.
    *
    * @param simulation
    *   A [[com.workflowfm.proter.Simulation Simulation]] to wrap within the actor.
    * @param system
    * @return
    *   The constructed [[com.workflowfm.proter.SimulationRef SimulationRef]].
    */
  def of(simulation: Simulation)(implicit system: ActorSystem): SimulationRef = {
    AkkaSimulationRef(simulation.name, system.actorOf(Props(new SimulationActor(simulation))))
  }

  /**
    * Builds a [[com.workflowfm.proter.SimulationRef SimulationRef]] for an actor-based simulation.
    *
    * @param simulationRef
    *   A [[com.workflowfm.proter.SimulationRef SimulationRef]] to wrap within the actor.
    * @param system
    * @return
    *   The constructed [[com.workflowfm.proter.SimulationRef SimulationRef]].
    */
  def ofRef(simulationRef: SimulationRef)(implicit system: ActorSystem): SimulationRef = {
    AkkaSimulationRef(
      simulationRef.name,
      system.actorOf(Props(new SimulationRefActor(simulationRef)))
    )
  }
}

/**
  * Actor wrapper for a given [[com.workflowfm.proter.SimulationRef SimulationRef]].
  *
  * Provides access to all functionality via messaging.
  *
  * @param simulation
  *   The simulation reference to wrap inside an actor.
  */
class SimulationRefActor(final val simulationRef: SimulationRef) extends Actor {

  def simulationRefReceive: Receive = {
    case SimulationRefActor.Run => simulationRef.run()
    case SimulationRefActor.Completed(time, tasks) => simulationRef.completed(time, tasks)
    case SimulationRefActor.Stop => simulationRef.stop()
  }

  def receive: Receive = simulationRefReceive
}

/**
  * Defines the messages a [[SimulationRefActor]] can receive by default.
  */
object SimulationRefActor {
  /**
    * Instructs the start of simulation logic execution.
    */
  case object Run

  /**
    * Informs a [[Task]] has completed
    *
    * @see
    *   [[Simulation.complete]]
    *
    * @param task
    *   The completed [[Task]].
    * @param time
    *   The (virtual) time of completion.
    */
  case class Completed(time: Long, tasks: Seq[TaskInstance])

  /**
    * Tells the [[Simulation]] to stop.
    *
    * @see
    *   [[Simulation.stop]]
    */
  case object Stop
}

/**
  * Actor wrapper for a given [[com.workflowfm.proter.Simulation Simulation]].
  *
  * Provides access to all functionality via messaging.
  *
  * @param simulation
  *   The simulation to wrap inside an actor.
  */
class SimulationActor(final val simulation: Simulation)
    extends SimulationRefActor(simulation)
    with Actor {

  def simulationReceive: Receive = {
    case SimulationActor.Ready => simulation.ready()
    case SimulationActor.AddTasks(tasks) => tasks foreach (simulation.task(_))
    case SimulationActor.AckTasks(tasks) => simulation.ack(tasks)
    case SimulationActor.AbortTasks(ids) => simulation.abort(ids: _*)
    case SimulationActor.Wait => simulation.wait()
    case SimulationActor.Done(result) => simulation.done(result)
    case SimulationActor.Succeed(result) => simulation.succeed(result)
    case SimulationActor.Fail(exception) => simulation.fail(exception)
  }

  override def receive: Receive = simulationRefReceive orElse simulationReceive
}

/**
  * Defines the messages a [[SimulationActor]] can receive by default.
  */
object SimulationActor {
  /**
    * Triggers [[Simulation.ready]].
    *
    * @see
    *   [[Simulation.ready]]
    */
  case object Ready

  /**
    * Adds new [[Task]]s to the simulation.
    *
    * @see
    *   [[Simulation.task]]
    *
    * @param t
    *   The [[TaskGenerator]] to generate the [[Task]].
    */
  case class AddTasks(tasks: Seq[Task])

  /**
    * Aborts a list of [[Task]]s.
    *
    * @see
    *   [[Simulation.abort]]
    *
    * @param ids
    *   The [[Task]] IDs to abort.
    */
  case class AbortTasks(ids: Seq[UUID])

  /**
    * Tells the [[Simulation]] to request that [[Coordinator]] waits.
    */
  case object Wait

  /**
    * Acknowledges a sequence of completed [[Task]] IDs has been processed.
    *
    * @see
    *   [[Simulation.ack]]
    *
    * @param task
    *   The acknowledged [[Task]] UUIDs.
    */
  case class AckTasks(tasks: Seq[UUID])

  /**
    * Tells the [[Simulation]] to complete.
    *
    * @see
    *   [[Simulation.done]]
    *
    * @param result
    *   The successful simulation result.
    */
  case class Done(result: Try[Any])

  /**
    * Tells the [[Simulation]] to complete successfully.
    *
    * @see
    *   [[Simulation.succeed]]
    *
    * @param result
    *   The successful simulation result.
    */
  case class Succeed(result: Any)

  /**
    * Tells the [[Simulation]] to fail.
    *
    * @see
    *   [[Simulation.fail]]
    *
    * @param exception
    *   The `Throwable` causing the failure.
    */
  case class Fail(exception: Throwable)

}

/**
  * Wrapper for [[SimulationGenerator]] such that produces [[AkkaSimulationRef]].
  *
  * @param gen
  *   The underlying [[SimulationGenerator]] to use.
  */
class AkkaSimulationGenerator(gen: SimulationGenerator)(implicit system: ActorSystem)
    extends SimulationRefGenerator {

  /**
    * @inheritdoc
    *
    * Creates an [[AkkaSimulationRef]].
    *
    * Encapsulation is safe here since the simulation is being constructed now.
    *
    * @param manager
    *   The [[Manager]] of generated [[SimulationRef]].
    * @param count
    *   An integer counter to help construct unique names.
    * @return
    *   A new [[SimulationRef]] instance.
    */
  override def build(manager: Manager, count: Int): SimulationRef =
    AkkaSimulationRef.of(gen.build(manager, count))
}

object AkkaSimulationGenerator {

  def of(gen: SimulationRefGenerator)(implicit system: ActorSystem): SimulationRefGenerator =
    gen match {
      case simGen: SimulationGenerator => new AkkaSimulationGenerator(simGen)
      case _ => gen
    }
}
