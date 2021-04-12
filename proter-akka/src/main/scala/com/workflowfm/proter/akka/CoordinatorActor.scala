package com.workflowfm.proter.akka

import java.util.concurrent.TimeUnit

import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration._

import akka.actor.{ Actor, ActorRef, ActorSystem, Props }
import akka.event.LoggingReceive
import akka.pattern.{ ask, pipe }
import akka.util.Timeout

import com.workflowfm.proter._
import com.workflowfm.proter.schedule.Scheduler

/**
  * Akka-based [[com.workflowfm.proter.Manager Manager]] implementation.
  *
  * Delegates all interaction to an actor via messaging.
  *
  * @param manager The `ActorRef` of the actor.
  * @param timeout A timeout for all `ask` operations.
  */
case class AkkaManager(manager: ActorRef, timeout: Timeout = Timeout(1, TimeUnit.MINUTES))
    extends Manager {

  override def waitFor(simulation: String): Unit =
    Await.result((manager ? CoordinatorActor.WaitFor(simulation))(timeout), timeout.duration)

  override def simResponse(response: SimResponse): Unit =
    manager ! CoordinatorActor.Response(response)

  def addResource(r: TaskResource): Unit = manager ! CoordinatorActor.AddResource(r)
  def addResources(r: TaskResource*): Unit = manager ! CoordinatorActor.AddResources(r)

  def addSimulation(t: Long, simulation: SimulationRef): Unit =
    manager ! CoordinatorActor.AddSim(t, simulation)

  def addSimulationNow(simulation: SimulationRef): Unit =
    manager ! CoordinatorActor.AddSimNow(simulation)

  def addSimulations(sims: Seq[(Long, SimulationRef)]): Unit =
    manager ! CoordinatorActor.AddSims(sims)
  def addSimulationsNow(sims: SimulationRef*): Unit = manager ! CoordinatorActor.AddSimsNow(sims)

  def addInfiniteArrival(
      t: Long,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = manager ! CoordinatorActor.AddInfiniteArrival(t, rate, simulationGenerator)

  def addInfiniteArrivalNow(rate: Distribution, simulationGenerator: SimulationRefGenerator): Unit =
    manager ! CoordinatorActor.AddInfiniteArrivalNow(rate, simulationGenerator)

  def addInfiniteArrivalNext(
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = manager ! CoordinatorActor.AddInfiniteArrivalNext(rate, simulationGenerator)

  def addArrival(
      t: Long,
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = manager ! CoordinatorActor.AddArrival(t, limit, rate, simulationGenerator)

  def addArrivalNow(
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = manager ! CoordinatorActor.AddArrivalNow(limit, rate, simulationGenerator)

  def addArrivalNext(
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = manager ! CoordinatorActor.AddArrivalNext(limit, rate, simulationGenerator)

  def start(): Future[Any] = (manager ? CoordinatorActor.Start)(timeout)
  def stop(): Unit = manager ! CoordinatorActor.Stop
  def limit(t: Long): Unit = manager ! CoordinatorActor.LimitTime(t)

  def ping(): Future[CoordinatorActor.Time] =
    (manager ? CoordinatorActor.Ping)(timeout).mapTo[CoordinatorActor.Time]
}

object AkkaManager {

  def build(
      scheduler: Scheduler,
      startingTime: Long = 0L,
      timeout: Timeout = Timeout(1, TimeUnit.MINUTES)
  )(implicit system: ActorSystem): AkkaManager = {
    AkkaManager(
      system.actorOf(CoordinatorActor.props(scheduler, startingTime)(system)),
      timeout
    )
  }
}

/**
  * Actor wrapper for [[com.workflowfm.proter.Coordinator Coordinator]].
  *
  * Provides access to all functionality via messaging.
  *
  * @param scheduler The [[com.workflowfm.proter.schedule.Scheduler Scheduler]] responsible
  *                  for task allocation at any given time.
  * @param startingTime The starting timestamp of the entire simulation.
  */
class CoordinatorActor(
    scheduler: Scheduler,
    startingTime: Long
)(implicit actorSystem: ActorSystem)
    extends Actor {

  implicit val executionContext: ExecutionContext = actorSystem.dispatcher

  val coordinator: Coordinator = new Coordinator(scheduler, true, startingTime)

  def receive: Receive = {
    case CoordinatorActor.AddSim(t, s) => coordinator.addSimulation(t, s)
    case CoordinatorActor.AddSims(l) => coordinator.addSimulations(l)
    case CoordinatorActor.AddSimNow(s) => coordinator.addSimulationNow(s)
    case CoordinatorActor.AddSimsNow(l) => coordinator.addSimulationsNow(l)

    case CoordinatorActor.AddInfiniteArrival(t, r, g) =>
      coordinator.addInfiniteArrival(t, r, AkkaSimulationGenerator.of(g))
    case CoordinatorActor.AddInfiniteArrivalNow(r, g) =>
      coordinator.addInfiniteArrivalNow(r, AkkaSimulationGenerator.of(g))
    case CoordinatorActor.AddInfiniteArrivalNext(r, g) =>
      coordinator.addInfiniteArrivalNext(r, AkkaSimulationGenerator.of(g))
    case CoordinatorActor.AddArrival(t, l, r, g) =>
      coordinator.addArrival(t, l, r, AkkaSimulationGenerator.of(g))
    case CoordinatorActor.AddArrivalNow(l, r, g) =>
      coordinator.addArrivalNow(l, r, AkkaSimulationGenerator.of(g))
    case CoordinatorActor.AddArrivalNext(l, r, g) =>
      coordinator.addArrivalNext(l, r, AkkaSimulationGenerator.of(g))

    case CoordinatorActor.AddResource(r) => coordinator.addResource(r)
    case CoordinatorActor.AddResources(r) => r foreach coordinator.addResource

    case CoordinatorActor.LimitTime(t) => coordinator.limit(t)

    case CoordinatorActor.Response(r) => coordinator.simResponse(r)

    case CoordinatorActor.WaitFor(simulation) => {
      coordinator.waitFor(simulation)
      sender() ! CoordinatorActor.AckWait
    }

    case CoordinatorActor.Start => coordinator.start().pipeTo(sender())

    case CoordinatorActor.Ping => sender() ! CoordinatorActor.Time(coordinator.getTime())

  }
}

/**
  * Companion object for [[CoordinatorActor]].
  *
  * Includes some of the actor messages that can be received and sent.
  *
  * @groupname simulations Interaction with a Simulation
  * @groupdesc simulations Messages exchanged with a [[Simulation]].
  * @groupprio simulations 2
  * @groupname toplevel General Interaction
  * @groupprio toplevel 1
  */
object CoordinatorActor {
  /**
    * Message to start the entire simulation.
    * @group toplevel
    */
  case object Start

  /**
    * Message to retrieve the current (virtual) time.
    * @group toplevel
    */
  case object Ping

  case object Stop

  /**
    * Response to [[Ping]] with current (virtual) time.
    * @group toplevel
    */
  case class Time(time: Long)

  /**
    * Message to add a simulation.
    * @group toplevel
    */
  case class AddSim(time: Long, simulation: SimulationRef)
  /**
    * Message to add a list of simulations.
    * @group toplevel
    */
  case class AddSims(l: Seq[(Long, SimulationRef)])
  /**
    * Message to add a simulation due to start right now.
    * @group toplevel
    */
  case class AddSimNow(simulation: SimulationRef)
  /**
    * Message to add a list of simulations due to start right now.
    * @group toplevel
    */
  case class AddSimsNow(l: Seq[SimulationRef])

  /**
    * Message to add an infinite arrival process.
    * @group toplevel
    */
  case class AddInfiniteArrival(
      t: Long,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  )
  /**
    * Message to add an infinite arrival process due to start right now.
    * @group toplevel
    */
  case class AddInfiniteArrivalNow(rate: Distribution, simulationGenerator: SimulationRefGenerator)
  /**
    * Message to add an infinite arrival process starting at the next arrival time.
    * @group toplevel
    */
  case class AddInfiniteArrivalNext(rate: Distribution, simulationGenerator: SimulationRefGenerator)

  /**
    * Message to add an arrival process.
    * @group toplevel
    */
  case class AddArrival(
      t: Long,
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  )

  /**
    * Message to add an arrival process due to start right now.
    * @group toplevel
    */
  case class AddArrivalNow(
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  )

  /**
    * Message to add an arrival process starting at the next arrival time.
    * @group toplevel
    */
  case class AddArrivalNext(
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  )

  /**
    * Message to add a [[TaskResource]].
    * @group toplevel
    */
  case class AddResource(r: TaskResource)
  /**
    * Message to add a list of [[TaskResource]]s.
    * @group toplevel
    */
  case class AddResources(l: Seq[TaskResource])

  /**
    * Message to introduce a time limit for all simulations.
    *
    * @group toplevel
    * @param t The timestamp when all simulations must stop.
    */
  case class LimitTime(time: Long)

  /**
    * Message from a [[Simulation]] to wait for it before proceeding.
    * @group simulations
    */
  case class WaitFor(simulation: String)

  case object AckWait

  case class Response(response: SimResponse)

  /**
    * Creates properties for a [[Coordinator]] actor.
    *
    * @param scheduler The [[com.workflowfm.proter.schedule.Scheduler Scheduler]] to be used.
    * @param startingTime The starting time of the entire simulation.
    * @return
    */
  def props(
      scheduler: Scheduler,
      startingTime: Long = 0L
  )(implicit system: ActorSystem): Props = Props(
    new CoordinatorActor(scheduler, startingTime)
  )
}
