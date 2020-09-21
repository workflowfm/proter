package com.workflowfm.simulator.flows

import com.workflowfm.simulator._
import akka.actor.{ Actor, ActorRef, Props }
import java.util.UUID
import scala.util.{ Try, Success, Failure }

sealed trait Flow {
  val id: UUID = java.util.UUID.randomUUID
  def +(f: Flow) = And(this, f)
  def >(f: Flow) = Then(this, f)
  def |(f: Flow) = Or(this, f)
}

case class NoTask() extends Flow
case class FlowTask(generator: TaskGenerator, resources: Seq[String]) extends Flow
case class Then(left: Flow, right: Flow) extends Flow
case class And(left: Flow, right: Flow) extends Flow
case class All(elements: Flow*) extends Flow
case class Or(left: Flow, right: Flow) extends Flow

/**
  * An actor which implements the "Flows" interface which can be used to create simple workflow simulations.
  *
  * This actor uses a [[Flow]] structure which describes how and in what order certain tasks should be executed.
  * A Flow may consist of a single FlowTask or some combination of FlowTasks which are joined using `And`s, `Then`s,
  * and `Or`s.
  *
  * @param name The name of the simulation being managed.
  * @param coordinator A reference to the [[Coordinator]] actor running the simulation.
  * @param flow The flow which describes how the simulation behaves
  * @param executionContext
  */
class FlowSimulationActor(
    name: String,
    coordinator: ActorRef,
    flow: Flow
) extends AsyncSimulation(name, coordinator) {

  /**
    * Initiates the execution of the simulation.
    */
  override def run(): Unit = {
    runFlow(flow, callback((_, _) => done(Unit)) )
    ready()
  }

  /**
    * Runs a `Flow` by either innitiating a FlowTask or decomposing a more complex flow via the `execute` method.
    * In both cases, the callback of the flow is stored in a map.
    *
    * @param flow The flow to run.
    * @param flowCallback The callback function which is executed once this flow completes.
    */
  protected def runFlow(flow: Flow, flowCallback: Callback): Unit = {
    flow match {
      case f: FlowTask =>
        task(f.id, f.generator, callback((t, l) => { flowCallback(Success(t, l)); ack(Seq(f.id)) }), f.resources)
      case f: Flow => { tasks += flow.id -> flowCallback; execute(f) }
    }
  }

  /**
    * Completes an id by executing its callback and then removing it from the map
    *
    * This overloads the definition of `complete` found in [[Simulation]] to allow for any id to be used in the
    * `tasks` map, and not just [[Task]] objects.
    *
    * @param id The id to complete
    */
  protected def complete(id: UUID) = {
    tasks.get(id).map(_(Success(null, 0L)))
    tasks -= id
  }

  /**
    * Executes a flow by translating from a flow object to its sub-parts and appropriate
    * callbacks, then calling `runFlow`.
    *
    * @param flow The flow to be executed
    */
  protected def execute(flow: Flow) {
    flow match {
      case f: NoTask => complete(f.id)

      case FlowTask(generator: TaskGenerator, resources: Seq[String]) => {}
      //this is here for the sake of case completeness, should not be called

      case f: Then => {
        val rightCallback: Callback = callback((_, _) => complete(f.id))
        val leftCallback: Callback = callback((_, _) => runFlow(f.right, rightCallback))
        runFlow(f.left, leftCallback)
      }

      case f: And => {
        val leftCallback: Callback = callback((_, _) => (if (!tasks.contains(f.right.id)) complete(f.id)))
        val rightCallback: Callback = callback((_, _) => (if (!tasks.contains(f.left.id)) complete(f.id)))
        runFlow(f.left, leftCallback)
        runFlow(f.right, rightCallback)
      }

      case f @ All(elem @ _*) =>
        runFlow((elem.fold(NoTask()) { (l, r) => And(l, r) }), callback((_, _) => complete(f.id)))

      case f: Or => {
        val leftCallback: Callback = callback((_, _) => (if (tasks.contains(f.right.id)) complete(f.id)))
        val rightCallback: Callback = callback((_, _) => (if (tasks.contains(f.left.id)) complete(f.id)))
        runFlow(f.left, leftCallback)
        runFlow(f.right, rightCallback)
      }
    }
  }
}

object FlowSimulationActor {

  /**
    * Creates props of a [[FlowSimulationActor]].
    *
    * @param name The simulation name.
    * @param coordinator The [[Coordinator]].
    * @param flow The [[Flow]] to be executed
    * @return The Props of a new flow simulation actor
    */
  def props(name: String, coordinator: ActorRef, flow: Flow): Props =
    Props(new FlowSimulationActor(name, coordinator, flow))
}
