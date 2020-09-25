package com.workflowfm.simulator.flows

import com.workflowfm.simulator._
import scala.concurrent.{ ExecutionContext, Future, Promise }
import akka.actor.{ Actor, ActorRef, Props }
import java.util.UUID
import akka.protobufv3.internal.Empty

sealed trait Flow {
  val id: UUID = java.util.UUID.randomUUID
  def +(f: Flow) = And(this, f)
  def >(f: Flow) = Then(this, f)
  def |(f: Flow) = Or(this, f)
}

case class NoTask() extends Flow
case class FlowTask(generator: TaskGenerator) extends Flow {override val id = generator.id}
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
    protected val flow: Flow
)(implicit executionContext: ExecutionContext)
    extends AsyncSimulation(name, coordinator)(executionContext) {

  /**
    * Initiates the execution of the simulation.
    *
    * @return A `Future` that completes with a custom output when the simulation is completed.
    */
  override def run(): Future[Any] = {
    val promise = Promise[Any]()
    runFlow(flow, ((_, _) => promise.success(Unit)))
    ready()
    promise.future
  }

  /**
    * Runs a `Flow` by either innitiating a FlowTask or decomposing a more complex flow via the `execute` method.
    * In both cases, the callback of the flow is stored in a map.
    *
    * @param flow The flow to run.
    * @param callback The callback function which is executed once this flow completes.
    */
  protected def runFlow(flow: Flow, callback: Callback): Unit = {
    flow match {
      case f: FlowTask =>
        task(f.generator, ((t, l) => { callback(t, l); ack(Seq(f.id)) }))
      case f: Flow => { tasks += flow.id -> callback; execute(f) }
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
    tasks.get(id).map(_(null, 0L))
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

      case FlowTask(generator: TaskGenerator) => {}
      //this is here for the sake of case completeness, should not be called

      case f: Then => {
        val rightCallback: Callback = (_, _) => complete(f.id)
        val leftCallback: Callback = (_, _) => runFlow(f.right, rightCallback)
        runFlow(f.left, leftCallback)
      }

      case f: And => {
        val leftCallback: Callback = (_, _) => (if (!tasks.contains(f.right.id)) complete(f.id))
        val rightCallback: Callback = (_, _) => (if (!tasks.contains(f.left.id)) complete(f.id)) 
        runFlow(f.left, leftCallback)
        runFlow(f.right, rightCallback)
      }

      case f @ All(elem @ _*) =>
        runFlow((elem.fold(NoTask()) { (l, r) => And(l, r) }), (_, _) => complete(f.id))

      case f: Or => {
        val leftCallback: Callback = (_, _) => (if (tasks.contains(f.right.id)) complete(f.id))
        val rightCallback: Callback = (_, _) => (if (tasks.contains(f.left.id)) complete(f.id))
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
    * @param executionContext
    * @return The Props of a new flow simulation actor
    */
  def props(name: String, coordinator: ActorRef, flow: Flow)(
      implicit executionContext: ExecutionContext
  ): Props =
    Props(new FlowSimulationActor(name, coordinator, flow))
}

/**
  * A triat which provides Lookahead compatability to [[FlowSimulationActor]]s.
  * 
  * Works by parsing the entire flow at the start of the simulation to build a
  * [[LookaheadStructure]] automatically.
  */
trait FlowsLookahead extends FlowSimulationActor with Lookahead {
  /**
    * Initiates the execution of the simulation.
    * Parses the flow before running it.
    *
    * @return A `Future` that completes with a custom output when the simulation is completed.
    */
  override def run(): Future[Any] = {
    lookahead = parseFlow(flow, None, lookahead)._2
    super.run()
  }

  type IDFunction = Map[UUID,Long]=>Option[Long]
  /**
    * Parses a flow in order to build a [[LookaheadStructure]]
    * 
    * This is a recursive function which uses the fact that [[Flow]]s are tree-like structures.
    * Each iterarion it calls parseFlow on each of its children and then combines the results in 
    * a meaningful way. The goal is to build up a [[LookaheadStructure]] by combining the structures
    * of each branch. There is also this `IDFunction` which is passed up and down the tree as the
    * algorithm progresses, which is used to describe the function that will be used to add entries
    * to the lookahead structure; This function can grow/shrink over time as the algorithm progresses,
    * but it is always used to describe the most current prerequisites of any task that it might meet.
    * 
    * @see [[LookaheadStructure.+]] for details on the IDfunction itself.
    * 
    * As it works through the flow tree, the algorithim does the following:
    *   1. If the current node is a task (i.e. a leaf node), it should be registered by adding itself
    *      to the current running lookahead structure. To do this it uses the IDfunction that it recieved.
    *      If no ID function was provided, this means that there are no prior requirements to starting this task
    *      and so it is not registered.
    *   1. If the current node is a Then, first the left branch is parsed, and then the right branch is parsed.
    *      The preconditions (i.e. the IDfunction) of the left branch is the same as the preconditions of this node,
    *      however the preconditions of the right branch are that which is returned by the left branch (as opposed to
    *      the preconditons of this `Then` node). This is because, by our definiton, the right branch happens _after_
    *      the left branch, and hence the preconditons of the right branch is that the left branch is completed.
    *   1. If the current node is an And, All, or Or node, it parses all its child nodes normally, since the child
    *      branches are independant.
    * 
    * Then, depending on the node type, it returns a function and a lookahead structure. The lookeahd structure contains
    * the current lookahead entries, and may be combined with other structures later on. The function will be used
    * by other nodes to register new lookeahead entries, where it will describe the preconditons of that entry; As such,
    * the function should express if _this_ node is "done", as this is necessary in determining if some task in the future
    * may start.
    *   1. If our current node is a task (leaf node), then we create a new function; The function will return
    *      None if this task is not in the map which it recieves as input, and Some(value) otherwise. The result of this
    *      is that when the function will recieve a list of completed tasks, if this task is on the list then the function will
    *      indicate that the prerequites are met, and hence (when combined with other functions from other nodes) this function
    *      can be used to describe the prerequisites of future tasks.
    *   1. If our current node is a Then, we only need to return the IDfunction of the right branch, since the left branch
    *      was already considered when parsing the right branch. For example, if you had a flow [A THEN B THEN C], we want to
    *      express that A is a prerequisite of B, and B is a prerequisite of C. It is unnecessary to say that A is a prerequisite
    *      of C since it is already a prerequisite of B.
    *   1. If our current node is an And or All, we need to combine the functions of all the child nodes. If all the child nodes
    *      are "complete" then this node is complete, hence if any child function returns None, this function should also return
    *      None. Otherwise, the maximum value of the child functions is returned, since tasks that come after an And or an All
    *      may only begin once all of the previous tasks have finished.
    *   1. If our current node is an Or, the child node functions are also combined in a similar way to Ands and Alls, but this
    *      time we return the minum value of the child functions, since any task that comes after an Or may begin as soon as
    *      any of the branches in an Or are completed.
    * 
    * It might be easier to picture this algorithim when given some examples. Please refer to the wiki for examples and more
    * detailed explanations.
    *
    * @param flow The flow to be parsed
    * @param extraFunction an optional function which describes the current preconditions for lookahead (if any). @see [[LookaheadStructure.+]]
    * @param lookaheadStructure The [[LookaheadStructure]] built so far.
    * @return A function that describes the precondition of this node, and the current lookahead structure.
    */
  protected def parseFlow(flow: Flow, extraFunction: Option[IDFunction], lookaheadStructure: LookaheadStructure ): (IDFunction, LookaheadStructure) = {
    flow match {
      case f: NoTask => ((m:Map[UUID,Long])=> (Some(Long.MinValue)), EmptyStructure)
      case FlowTask(g) => {
        var s = lookaheadStructure
        if (extraFunction.isDefined) s = s + (extraFunction.get, g)
        ((m:Map[UUID,Long])=> (m.get(g.id)), s)
      }
      case f: Then => {
        val l = parseFlow(f.left, extraFunction, lookaheadStructure)
        parseFlow(f.right, Some(l._1), l._2)
      }
      case f: And => {
        val functions = Seq(parseFlow(f.left,extraFunction, lookaheadStructure),parseFlow(f.right,extraFunction, lookaheadStructure))
        ( (m) => { 
          val results = functions map (_._1(m))
          if (results.contains(None)) None else results.max
        }, functions.map(_._2).fold(EmptyStructure){(a,b)=>a and b})
      }
      case f @ All(elem @ _*) => {
        val functions = elem map (parseFlow(_,extraFunction,lookaheadStructure))
        ( (m) => { 
          val results = functions map (_._1(m))
          if (results.contains(None)) None else results.max
        },
        functions.map(_._2).fold(EmptyStructure){(a,b)=>a and b})
      }
      case f: Or => {
        val functions = Seq(parseFlow(f.left,extraFunction, lookaheadStructure),parseFlow(f.right,extraFunction, lookaheadStructure))
        ((m) => { 
          val results = functions.map(_._1(m))
          ( results filter (_.isDefined) headOption ).flatten
        }, functions.map(_._2).fold(EmptyStructure){(a,b)=>a and b})
      }
    }
  }
}

/**
  * A [[Flow]] actor with lookahead functionality.
  *
  * @param name The simulation name.
  * @param coordinator The [[Coordinator]].
  * @param flow The [[Flow]] to be executed
  * @param executionContext
  */
class FlowLookaheadActor(
  name: String,
  coordinator: ActorRef,
  flow: Flow
)(implicit executionContext: ExecutionContext)
  extends FlowSimulationActor(name,coordinator,flow) with FlowsLookahead


object FlowLookaheadActor {
    /**
    * Creates props of a [[FlowLookaheadActor]].
    *
    * @param name The simulation name.
    * @param coordinator The [[Coordinator]].
    * @param flow The [[Flow]] to be executed
    * @param executionContext
    * @return The Props of a new flow simulation actor
    */
  def props(name: String, coordinator: ActorRef, flow: Flow)(
      implicit executionContext: ExecutionContext
  ): Props =
    Props(new FlowLookaheadActor(name, coordinator, flow))
}