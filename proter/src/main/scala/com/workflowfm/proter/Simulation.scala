package com.workflowfm.proter

import java.util.UUID

import scala.collection.mutable.{ Map, HashSet, Queue }
import scala.concurrent.{ Future, Promise }
import scala.util.{ Try, Success, Failure }

/**
  * Captures the potential responses of a simulation case to an event.
  *
  * A simulation is notified by its [[Manager]] when relevant events have taken place, such as one
  * of its tasks completing. The simulation can use subclasses of this trait to respond to such
  * notification. The [[Manager]] will wait for such a response before it resumes the virtual time.
  */
sealed trait SimResponse {
  /**
    * The unique name of the simulation that issues the response.
    *
    * @return
    *   The name of the [[SimulationRef]]
    */
  val simulation: String
}

/**
  * Response issued when the simulation is still running and waiting for virtual time to pass.
  *
  * @param simulation
  *   The name of the [[SimulationRef]].
  * @param tasks
  *   A sequence of new tasks to be added for scheduling.
  * @param abort
  *   A sequence of IDs of existing tasks that need to be aborted.
  * @param lookahead
  *   An updated [[Lookahead]] structure.
  */
case class SimReady(
    override val simulation: String,
    tasks: Seq[Task],
    abort: Seq[UUID] = Seq(),
    lookahead: Lookahead = NoLookahead
) extends SimResponse

/**
  * Response issued when the simulation has completed.
  *
  * @param simulation
  *   The name of the [[SimulationRef]].
  * @param result
  *   The (successful or failed) result of the simulation.
  */
case class SimDone(override val simulation: String, result: Try[Any]) extends SimResponse

/**
  * An abstract reference to simulation logic.
  *
  * Includes the basic interface that we expect from a simulation case:
  *   1. Starting the simulation.
  *   1. Notifying when tasks complete.
  *   1. Stopping/aborting the simulation.
  */
trait SimulationRef {
  /**
    * A unique name for the simulation.
    *
    * @return
    *   The name of the [[SimulationRef]].
    */
  def name: String

  /**
    * Starts the simulation.
    */
  def run(): Unit

  /**
    * Stops/aborts the simuation.
    */
  def stop(): Unit

  /**
    * Notifies the simulation that some of its tasks completed.
    *
    * @param time
    *   The current virtual timestamp.
    * @param tasks
    *   The [[TaskInstance]]s that completed.
    */
  def completed(time: Long, tasks: Seq[TaskInstance]): Unit
}

/**
  * A manager for a single simulation case.
  *
  * We view a simulation as a case or workflow of simulated tasks. This class is responsible for
  * keeping track of all the tasks for the corresponding simulation, informing the [[Coordinator]]
  * of new tasks to be added and informing the simulation processes when a task is completed.
  *
  * =Basic Interaction Flow=
  * The interaction flow with the [[Coordinator]] is expected as follows:
  *   1. The simulation logic starts executing via [[run]].
  *   1. Tasks are added via [[task]].
  *   1. When the simulation logic finishes producing tasks, it should call [[ready]].
  *   1. Eventually, one of the tasks completes and [[complete]] is called. The simulation logic
  *      resumes execution.
  *   1. The simulation may now produce further tasks in reaction to the completed one(s). It can
  *      either handle and acknowledge each completed task individually using [[ack]] or use
  *      [[ready]] in one go as above.
  *   1. When the simulation logic completes, it can use [[succeed]] (instead of [[ack]] or
  *      [[ready]]) to notify the [[Coordinator]] of its result.
  *
  * =Additional Functions=
  * The simulation may also:
  *   1. React to things happening to other simulations. It can send a request to ``wait`` using
  *      [[simWait]]. The [[Coordinator]] will then wait for the [[Simulation]] to send new tasks or
  *      to complete, as if a task just completed. This needs to happen while the [[Coordinator]] is
  *      still waiting for the other simulation(s) we are reacting to.
  *   1. Abort previously added tasks using [[abort]].
  *   1. Report completion due to failure using [[fail]].
  *   1. Optionally maintain a [[Lookahead]] structure of future tasks, returning it through
  *      [[getLookahead]].
  *
  * @groupname act
  *   Taking Action
  * @groupname react
  *   Handling Events
  * @groupname internal
  *   Internal Management
  */
trait Simulation extends SimulationRef {

  protected val waiting: HashSet[UUID] = HashSet[UUID]()

  protected val tasksToAdd: Queue[Task] = Queue[Task]()

  protected val aborted: HashSet[UUID] = HashSet[UUID]()

  protected def manager: Manager

  /**
    * Manages a completed [[TaskInstance]].
    *
    * The simulation logic must react to this by either registering more tasks or finishing.
    *
    * If new tasks are produced, the completed [[TaskInstance]] must be ``acknowledged`` via
    * [[ack]]. Alternatively, if we do not want to ``ack`` all completed tasks, we can just call
    * [[ready]]. Finally, if the simulation completes, we can use [[done]], [[succeed]] or [[fail]]
    * accordingly.
    *
    * @group react
    * @param task
    *   The completed [[TaskInstance]].
    * @param time
    *   The timestamp of completion and current time.
    */
  def complete(task: TaskInstance, time: Long): Unit = ()

  // final : cannot be final because that prevents mockups in scalamock
  override def completed(time: Long, tasks: Seq[TaskInstance]): Unit = {
    this.synchronized {
      waiting ++= tasks.map(_.id)
    }
    tasks.foreach(complete(_, time))
  }

  /**
    * Declare a new [[Task]] that needs to be sent for simulation.
    *
    * @group act
    *
    * @param t
    *   The [[Task]] to send.
    */
  def task(t: Task): Unit = this.synchronized {
    tasksToAdd += t
  }

  /**
    * Declare IDs of [[TaskInstance]]s that need to be aborted.
    *
    * @group act
    *
    * @param ids
    *   The `UUID`s of the [[TaskInstance]]s.
    */
  def abort(ids: UUID*): Unit = this.synchronized {
    aborted ++= ids
  }

  /**
    * Declares that the simulation completed.
    *
    * @group act
    * @param result
    *   The result of the simulation.
    */
  def done(result: Try[Any]): Unit = {
    clear()
    manager.simResponse(SimDone(name, result))
  }

  /**
    * Declares that the simulation completed successfully.
    *
    * @group act
    * @param result
    *   The successful result of the simulation.
    */
  def succeed(result: Any): Unit = {
    clear()
    manager.simResponse(SimDone(name, Success(result)))
  }

  /**
    * Declares that the simulation has failed or has been aborted.
    *
    * @group act
    * @param exception
    *   The `Throwable` that caused the failure.
    */
  def fail(exception: Throwable): Unit = {
    clear()
    manager.simResponse(SimDone(name, Failure(exception)))
  }

  /**
    * Declares that the simulation has finished processing one or more completed [[TaskInstance]]s.
    *
    * Identifies the tasks via their UUID.
    *
    * @group act
    */
  def ack(taskIDs: Seq[UUID]): Unit = this.synchronized {
    waiting --= taskIDs
    if waiting.isEmpty then {
      val response = SimReady(name, tasksToAdd.clone.toSeq, aborted.clone.toSeq, getLookahead())
      clear()
      manager.simResponse(response)
    }
  }

  /**
    * Declares that the simulation has finished calculating and is ready for virtual time to
    * proceed.
    *
    * @group act
    */
  def ready(): Unit = {
    val response = SimReady(name, tasksToAdd.clone.toSeq, aborted.clone.toSeq, getLookahead())
    clear()
    manager.simResponse(response)
  }

  /**
    * Requests that the [[Manager]] waits for this simulation before it continues.
    *
    * The simulation needs to either register more tasks and become ``ready`` or finish.
    *
    * @note
    *   We assume the [[Manager]] is already waiting for another simulation when the request is
    *   made. Otherwise virtual time may progress unexpectedly and cause unpredictable behaviour
    *   depending on the timing of the [[Manager]] messages.
    *
    * @group act
    */
  def simWait(): Unit = {
    manager.waitFor(name)
  }

  /**
    * Calculates an optional [[Lookahead]] structure for tasks that are expected in the future.
    *
    * This is called via [[ready]].
    *
    * @group act
    * @return
    *   The current [[Lookahead]] structure.
    */
  def getLookahead(): Lookahead = NoLookahead

  /**
    * Clears structures to initiate a new reaction.
    *
    * @group internal
    */
  protected def clear(): Unit = {
    waiting.clear()
    tasksToAdd.clear()
    aborted.clear()
  }
}

object Simulation {

  /**
    * Exception used when aborting a [[Task]].
    *
    * @param cause
    *   An optional underlying cause.
    */
  final case class TaskAbortedException(private val cause: Throwable = None.orNull)
      extends Exception("Task has been aborted", cause)

  /**
    * Exception used when the simulation is stopping.
    *
    * @param cause
    *   An optional underlying cause.
    */
  final case class SimulationStoppingException(private val cause: Throwable = None.orNull)
      extends Exception("Simulation is stopping", cause)
}

/**
  * A [[Simulation]] that simulates a single [[Task]].
  *
  * @param name
  *   The simulation name.
  * @param manager
  *   The [[Manager]] of the simulation.
  * @param resources
  *   The names of the [[TaskResource]]s the [[Task]] will require.
  * @param duration
  *   A [[Distribution]] for the duration of the [[Task]].
  * @param cost
  *   A [[Distribution]] for the cost of the [[Task]].
  * @param interrupt
  *   The [[Task.interrupt]] parameter.
  * @param priority
  *   The explicit priority of the [[Task]].
  */
class SingleTaskSimulation(
    override val name: String,
    override protected val manager: Manager,
    resources: Seq[String],
    duration: Distribution,
    cost: Distribution = Constant(0),
    interrupt: Int = (-1),
    priority: Int = 0
) extends Simulation {

  lazy val theTask: Task = Task(s"${name}Task", duration)
    .withCostGenerator(cost)
    .withResources(resources)
    .withInterrupt(interrupt)
    .withPriority(priority)

  /**
    * @inheritdoc
    *
    * Creates and adds the corresponding [[Task]], then calls [[ready]] immediately.
    */
  override def run(): Unit = {
    task(theTask)
    ready()
  }

  /**
    * @inheritdoc
    *
    * Completes immediately with [[succeed]].
    */
  override def complete(task: TaskInstance, time: Long): Unit = succeed((task, time))

  override def stop(): Unit = ()
}

/**
  * Asynchronous Simulation implementation with callback functions.
  *
  * Each task needs to be accompanied by a callback function that implements the simulation logic to
  * be followed when the task completes.
  */
trait AsyncSimulation extends Simulation {

  /**
    * The type of the callback function.
    *
    * Its input consists of the generated [[TaskInstance]] and the timestamp when it was completed.
    * A `Failure` input corresponds to an exception happening or an aborted task.
    *
    * @group react
    */
  type Callback = Try[(TaskInstance, Long)] => Unit

  /**
    * Creates a simple success callback from a function.
    *
    * Does nothing on failure.
    *
    * @param f
    *   The function to call on success.
    * @return
    *   The created [[Callback]].
    */
  def callback(f: (TaskInstance, Long) => Unit): Callback =
    t => t.foreach { arg => f(arg._1, arg._2) }

  /**
    * A map of [[TaskInstance]] IDs that have been sent to the [[Manager]] and the callback
    * functions that need to be called when the tasks complete.
    *
    * @group internal
    */
  protected val tasks: Map[UUID, Callback] = Map()

  /**
    * Declare a new [[Task]] that needs to be sent to the [[Coordinator]] for simulation with a
    * pre-determined ID.
    *
    * The provided callback function will be called when the corresponding [[Task]] is completed.
    *
    * When it finishes executing, it must notify the [[Coordinator]] either by acknowledging the
    * completed task using [[ack]] or by completing the simulation using [[done]], [[succeed]] or
    * [[fail]].
    *
    * The [[ready]] method can also be called if there is no need to acknowledge completed tasks
    * individually. This is unlikely in the current scenario where each task has its own callback,
    * but it's still worth mentioning.
    *
    * @group act
    *
    * @param t
    *   The [[Task]] to send.
    * @param callback
    *   The [[Callback]] function to be called when the corresponding [[Task]] completes.
    */
  def task(
      t: Task,
      callback: Callback
  ): Unit = {
    val id = t.id.getOrElse(UUID.randomUUID())
    tasks += id -> callback
    super.task(t.withID(id))
  }

  /**
    * @inheritdoc
    *
    * Calls the corresponding [[Callback]] in the `tasks` map and then removes the entry.
    *
    * @group react
    *
    * @param task
    *   The [[TaskInstance]] that completed.
    * @param time
    *   The timestamp of its completion.
    */
  override def complete(task: TaskInstance, time: Long): Unit = {
    val callback = tasks.get(task.id)
    tasks -= task.id
    callback.map(_(Success(task, time)))
  }

  /**
    * @inheritdoc
    *
    * Calls respective [[Callback]]s with `Failure`.
    *
    * @group act
    *
    * @param id
    *   The `UUID` of the [[Task]]s.
    */
  override def abort(ids: UUID*): Unit = {
    super.abort(ids: _*)
    ids map { id => tasks.get(id).map(_(Failure(Simulation.TaskAbortedException()))) }
  }

  /**
    * @inheritdoc
    *
    * Triggers all callbacks with a `Failure`.
    */
  override def stop(): Unit = {
    tasks.foreach { case (_, c) => c(Failure(Simulation.SimulationStoppingException())) }
    tasks.clear()
  }

}

/**
  * Extends [[AsyncSimulation]] with callbacks that fulfil a `Promise`/`Future`.
  *
  * This can be helpful for simulation implementations that use `Future`s to capture their
  * asynchronous logic.
  */
trait FutureTasks { self: AsyncSimulation =>

  /**
    * Declare a new [[Task]] that needs to be sent for simulation with a pre-determined ID and a
    * `Future` instead of a [[AsyncSimulation.Callback Callback]].
    *
    * @group act
    *
    * @param t
    *   The [[Task]] to send.
    * @return
    *   A `Future` that completes when the [[Task]] is completed.
    */
  def futureTask(t: Task): Future[(TaskInstance, Long)] = {
    val p = Promise[(TaskInstance, Long)]()
    def call: Callback = t => p.complete(t)
    task(t, call)
    p.future
  }
}

/**
  * A trait that adds Lookahead capabilities to a simulation.
  *
  * Works in conjunction with [[schedule.LookaheadScheduler LookaheadScheduler]].
  *
  * Provides a lookahead structure that can be built up by the simulation and then sent to the
  * scheduler for use in making schedules which look into the future to consider upcoming tasks in
  * scheduling.
  */
trait LookingAhead extends Simulation {
  var lookahead: Lookahead = LookaheadSet()

  override def getLookahead(): Lookahead = lookahead

  val completedTasks: collection.mutable.Set[(UUID, Long)] = collection.mutable.Set()

  /**
    * Manages a [[Task]] whose simulation has completed.
    *
    * Removes the task from the lookahead structure and sends this updated structure to the
    * scheduler before calling the `complete` method implementation of the parent class.
    *
    * @param task
    *   The [[TaskInstance]] that completed.
    * @param time
    *   The timestamp of its completion.
    */
  override def complete(task: TaskInstance, time: Long): Unit = {
    completedTasks += ((task.id, time))
    lookahead = lookahead - task.id
    lookahead.getTaskData(completedTasks).flatMap(_._1.id) foreach { id =>
      lookahead = lookahead - id
    }
    super.complete(task, time)
  }
}
