package com.workflowfm.proter
package flows

import cases.*
import state.Simulation.SimState

import cats.Monad
import cats.data.StateT
import cats.implicits.*
import cats.effect.std.{ Random, UUIDGen }

import java.util.UUID

import scala.util.Success

/**
  * Represents a simple flow of tasks.
  *
  * Tasks can be connected either in sequence or in parallel.
  */
sealed trait Flow {

  /**
    * Adds another flow in parallel.
    */
  def +(f: Flow): Flow = f match {
    case NoTask => this
    case _ => And(this, f)
  }

  /**
    * Adds another flow in sequence.
    */
  def >(f: Flow): Flow = f match {
    case NoTask => this
    case _ => Then(this, f)
  }

  /**
    * Repeats this flow a given number of times in parallel.
    */
  def *(i: Int): Flow = Flow.par(for _ <- 1 to i yield this)

}

/**
  * Trivial flow with no tasks. Does nothing.
  */
case object NoTask extends Flow {

  override def +(f: Flow): Flow = f
  override def >(f: Flow): Flow = f
  override def *(i: Int): Flow = this
}

/**
  * Trivial flow with a single task.
  */
case class FlowTask(val task: Task) extends Flow {

  override def toString(): String = task.name
}

given Conversion[Task, FlowTask] with
  def apply(t: Task): FlowTask = FlowTask(t)

/**
  * Two flows in sequence.
  */
case class Then(val left: Flow, val right: Flow) extends Flow {

  override def toString(): String = "(" + left.toString + " > " + right.toString + ")"

  override def >(f: Flow): Then = Then(left, right > f) // make > right associative
}

case class And(val left: Flow, val right: Flow) extends Flow {

  override def toString(): String = "(" + left.toString + " + " + right.toString + ")"
}

object Flow {

  def apply(t: Task*): Flow = Flow.seq(t.map(FlowTask(_)))

  /**
    * Creates a sequence of a collection of [[Flow]]s.
    *
    * @see
    *   [[Then]]
    * @example
    *   Flow.seq(Seq(t1,t2,t3)) = t1 > t2 > t3
    * @param l
    *   The collection of [[Flow]]s
    * @return
    *   A [[Flow]] that executes the given collection in sequence
    */
  def seq(l: Seq[Flow]): Flow = l.foldRight[Flow](NoTask) { (l, r) => Then(l, r) }

  /**
    * Creates a parallel [[Flow]] from a collection of [[Flow]]s.
    *
    * @see
    *   [[And]]
    * @example
    *   Flow.par(Seq(t1,t2,t3)) = t1 | t2 | t3
    * @param l
    *   The collection of [[Flow]]s
    * @return
    *   A [[Flow]] that executes the given collection in parallel
    */
  def par(l: Seq[Flow]): Flow = l.foldRight[Flow](NoTask) { (l, r) => And(l, r) }
}

/**
  * A [[cases.CaseRef CaseRef]] for a [[Flow]].
  *
  * It uses a [[Flow]] structure which describes how and in what order certain tasks should be
  * executed. A Flow may consist of a single [[FlowTask]] or some combination of [[FlowTask]]s which
  * are joined using [[And]]s and [[Then]]s.
  *
  * @param caseName
  *   The name of the simulation being managed.
  * @param flow
  *   The flow which describes how the case should behave.
  * @param callbackMap
  *   The map of callbacks for an extended [[cases.AsyncCaseRef AsyncCaseRef]] implementation.
  */
case class FlowCaseRef[F[_] : Monad : UUIDGen : Random](
    override val caseName: String,
    flow: Flow,
    callbackMap: CallbackMap[F]
) extends AsyncCaseRef[F](callbackMap) {

  override def updateState(callbackUpdate: CallbackMap[F]): AsyncCaseRef[F] =
    copy(callbackMap = callbackUpdate)

  override def stop(): F[Unit] = Monad[F].pure(())

  override def run(): F[SimState[F]] = for {
    uuid <- UUIDGen[F].randomUUID
    cback = callback((_, _) => succeedState(()))
    updated = copy(callbackMap = callbackMap + (uuid -> cback))
    result <- updated.applyState(execute(uuid, flow))
  } yield (result)

  /**
    * Completes an id by executing its callback and then removing it from the map.
    *
    * This overloads the definition of `complete` found in [[cases.AsyncCaseRef AsyncCaseRef]] to allow for ''any''' id
    * to be used in the `tasks` map, and not just [[TaskInstance]]s.
    *
    * @param id
    *   The id to complete
    */
  protected def complete(id: UUID): StateT[F, CallbackMap[F], SimState[F]] = StateT(m => {
    m.get(id) match {
      case None => Monad[F].pure((m, StateT.pure(Seq())))
      case Some(f) => {
        val dummyTask = new TaskInstance(id, "", caseName, 0L, 0L, Map(), 0L, 0L, 0, 0, 0)
        f(Success(dummyTask, 0L)).run(m - id)
      }
    }
  })

  /**
    * Executes a flow by recusively executing sub-parts with custom `UUID`s and appropriate
    * callbacks.
    *
    * @param id
    *   The `UUID` for the final callback of the entire flow.
    * @param flow
    *   The flow to be executed.
    */
  final def execute(id: UUID, flow: Flow): StateT[F, CallbackMap[F], SimState[F]] = {
    flow match {
      case NoTask => complete(id)

      case FlowTask(someTask) => {
        val taskCallback = callback((t, l) => {
          complete(id)
        })
        task(someTask, taskCallback)
      }

      case Then(left, right) =>
        StateT(m =>
          for {
            leftID <- UUIDGen[F].randomUUID
            leftCallback: Callback = callback((_, _) => execute(id, right))
            update = m + (leftID -> leftCallback)
            result <- execute(leftID, left).run(update)
          } yield (result)
        )

      case And(left, right) =>
        StateT(m =>
          for {
            leftID: UUID <- UUIDGen[F].randomUUID
            rightID: UUID <- UUIDGen[F].randomUUID

            leftCallback: Callback =
              callback((_, _) =>
                StateT(mx =>
                  if !mx.contains(rightID) then complete(id).run(mx)
                  else idState.run(mx)
                )
              )
            rightCallback: Callback =
              callback((_, _) =>
                StateT(mx =>
                  if !mx.contains(leftID) then complete(id).run(mx)
                  else idState.run(mx)
                )
              )

            update = m + (leftID -> leftCallback) + (rightID -> rightCallback)

            state = composeStates(execute(leftID, left), execute(rightID, right))
            result <- state.run(update)
          } yield (result)
        )

    }
  }

}

given [F[_]](using Monad[F], UUIDGen[F], Random[F]): Case[F, Flow] with {

  override def init(name: String, count: Int, time: Long, flow: Flow): F[CaseRef[F]] =
    Monad[F].pure(
      FlowCaseRef(
        name,
        flow,
        CallbackMap(Map())
      )
    )
}

/*
/**
 * A trait which provides Lookahead compatibility to [[FlowSimulation]]s.
 *
 * Works by parsing the entire flow at the start of the simulation to build a [[Lookahead]]
 * automatically.
 */
class FlowLookahead(
    name: String,
    manager: Manager,
    flow: Flow
) extends FlowSimulation(name, manager, flow)
    with LookingAhead {

  /**
 * Initiates the execution of the simulation. Parses the flow before running it.
 *
 * @return
 *   A `Future` that completes with a custom output when the simulation is completed.
 */
  override def run(): Unit = {
    lookahead = parseFlow(flow, None, lookahead)._2
    super.run()
  }

  type IDFunction = Map[UUID, Long] => Option[Long]

  /**
 * Parses a flow in order to build a [[Lookahead]]
 *
 * This is a recursive function which uses the fact that [[Flow]]s are tree-like structures. Each
 * iteration it calls parseFlow on each of its children and then combines the results in a
 * meaningful way. The goal is to build up a [[Lookahead]] by combining the structures of each
 * branch. There is also this `IDFunction` which is passed up and down the tree as the algorithm
 * progresses, which is used to describe the function that will be used to add entries to the
 * lookahead structure; This function can grow/shrink over time as the algorithm progresses, but
 * it is always used to describe the most current prerequisites of any task that it might meet.
 *
 * @see
 *   [[Lookahead.+]] for details on the IDfunction itself.
 *
 * As it works through the flow tree, the algorithm does the following:
 *   1. If the current node is a task (i.e. a leaf node), it should be registered by adding
 *      itself to the current running lookahead structure. To do this it uses the IDFunction that
 *      it received. If no ID function was provided, this means that there are no prior
 *      requirements to starting this task and so it is not registered.
 *   1. If the current node is a Then, first the left branch is parsed, and then the right branch
 *      is parsed. The preconditions (i.e. the IDFunction) of the left branch is the same as the
 *      preconditions of this node, however the preconditions of the right branch are that which
 *      is returned by the left branch (as opposed to the preconditions of this `Then` node).
 *      This is because, by our definition, the right branch happens _after_ the left branch, and
 *      hence the preconditions of the right branch is that the left branch is completed.
 *   1. If the current node is an And, or Or node, it parses all its child nodes normally, since
 *      the child branches are independent.
 *
 * Then, depending on the node type, it returns a function and a lookahead structure. The
 * lookahead structure contains the current lookahead entries, and may be combined with other
 * structures later on. The IDFunction will be used by other nodes to register new lookahead
 * entries, where it will describe the preconditions of that entry; As such, the function should
 * express if _this_ node is "done", as this is necessary in determining if some task in the
 * future may start.
 *   1. If our current node is a task (leaf node), then we create a new function; The function
 *      will return None if this task is not in the map which it receives as input, and
 *      Some(value) otherwise. The result of this is that when the function will receive a list
 *      of completed tasks, if this task is on the list then the function will indicate that the
 *      prerequires are met, and hence (when combined with other functions from other nodes) this
 *      function can be used to describe the prerequisites of future tasks.
 *   1. If our current node is a Then, we only need to return the IDFunction of the right branch,
 *      since the left branch was already considered when parsing the right branch. For example,
 *      if you had a flow [A THEN B THEN C], we want to express that A is a prerequisite of B,
 *      and B is a prerequisite of C. It is unnecessary to say that A is a prerequisite of C
 *      since it is already a prerequisite of B.
 *   1. If our current node is an And, we need to combine the functions of all the child nodes.
 *      If all the child nodes are "complete" then this node is complete, hence if any child
 *      function returns None, this function should also return None. Otherwise, the maximum
 *      value of the child functions is returned, since tasks that come after an And may only
 *      begin once all of the previous tasks have finished.
 *   1. If our current node is an Or, the child node functions are also combined in a similar way
 *      to Ands, but this time we return the minimum value of the child functions, since any task
 *      that comes after an Or may begin as soon as any of the branches in an Or are completed.
 *
 * It might be easier to picture this algorithim when given some examples. Please refer to the
 * wiki for examples and more detailed explanations.
 *
 * @param flow
 *   The flow to be parsed
 * @param extraFunction
 *   an optional function which describes the current preconditions for lookahead (if any). @see
 *   [[Lookahead.+]]
 * @param lookaheadStructure
 *   The [[Lookahead]] built so far.
 * @return
 *   A function that describes the precondition of this node, and the current lookahead
 *   structure.
 */
  protected def parseFlow(
      flow: Flow,
      extraFunction: Option[IDFunction],
      lookaheadStructure: Lookahead
  ): (IDFunction, Lookahead) = {
    flow match {
      case NoTask => ((_: Map[UUID, Long]) => (Some(Long.MinValue)), NoLookahead)
      case FlowTask(someTask) => {
        val id = someTask.id.getOrElse(UUID.randomUUID)
        val s = extraFunction.map( fn => lookaheadStructure + (fn, someTask.withID(id)) ).getOrElse(lookaheadStructure)
        ((m: Map[UUID, Long]) => (m.get(id)), s) // TODO f.id?
      }
      case Then(left, right) => {
        val (fn, l) = parseFlow(left, extraFunction, lookaheadStructure)
        parseFlow(right, Some(fn), l)
      }
      case And(left, right) => {
        val functions = Seq(
          parseFlow(left, extraFunction, lookaheadStructure),
          parseFlow(right, extraFunction, lookaheadStructure)
        )
        (
          (m) => {
            val results = functions map (_._1(m))
            if results.contains(None) then None else results.max
          },
          functions.map(_._2).fold(NoLookahead) { (a, b) => a `and` b }
        )
      }
    }
  }
}

case class FlowSimulationGenerator(baseName: String, flow: Flow) extends SimulationGenerator {

  override def build(manager: Manager, count: Int): Simulation = {
    val name = baseName + count.toString()
    new FlowSimulation(name, manager, flow.copy())
  }
}
 */
