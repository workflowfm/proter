package com.workflowfm.proter

import java.util.UUID

import scala.collection.mutable.{ Map, HashSet, Queue }
import scala.concurrent.{ Future, Promise }
import scala.util.{ Try, Success, Failure }


sealed trait SimResponse {
  val simulation: String
}
case class SimReady(
  override val simulation: String, 
  tasks: Seq[Task], 
  abort: Seq[UUID] = Seq(), 
  lookahead: Lookahead = NoLookahead
) extends SimResponse
case class SimDone(override val simulation: String, result: Try[Any]) extends SimResponse


/**
  * An actor managing the interaction between a simulation and a [[Coordinator]].
  *
  * We view a simulation as a case or workflow of simulated tasks. This actor is responsible
  * for keeping track of all the tasks for this simulation, informing the [[Coordinator]] of new
  * tasks to be added and informing the simulation processes when a task is completed.
  *
  * = Interface =
  * The interface to set up a simulation has 9 key methods:
  *   1. `run`: Start the execution of the simulation logic.
  *   1. `task`: Produce new simulation tasks to be run.
  *   1. `ack`: Tell the [[Coordinator]] we are done processing one or more finished tasks.
  *   1. `ready`: Tell the [[Coordinator]] we are ready and waiting for virtual time to progress.
  *   1. `simWait`: Tell the [[Coordinator]] to wait for us when reacting to another simulation.
  *   1. `complete`: Manage a task that just completed following the simulation logic.
  *   1. `done`: Tell the [[Coordinator]] the simulation is completed.
  *   1. `succeed`: Tell the [[Coordinator]] the simulation is completed successfully.
  *   1. `fail`: Tell the [[Coordinator]] the simulation failed with some exception.
  *
  * The interface can be accessed in 2 modes:
  *   1. ```Direct Interface:``` The simulation logic can be coded directly in a subclass, by
  *      implementing [[run]] and using [[task(t* task]],
  *      [[simWait]], [[ack]], [[ready]], [[done]], [[succeed]] and [[fail]].
  *   1. ```Actor Interface:``` The simulation logic can be distributed to other actors, typically
  *      [[SimulatedProcess]]es. These
  *      can interact with `Simulation` using the [[Simulation.AddTask]],
  *      [[Simulation.Wait]], [[Simulation.AckTasks]] and [[Simulation.Ready]] messages.
  *
  * = Interaction Flow =
  * The interaction flow with the [[Coordinator]] is expected as follows:
  *   1. Receiving a [[Simulation.Start]] message starts the simulation. This is confirmed
  *      via a [[Coordinator.SimStarted]] response.
  *   1. The simulation logic starts executing via [[Simulation.run]] and produces some
  *      simulation ``tasks``.
  *   1. Newly produced tasks can be sent to the [[Coordinator]] via the [[Coordinator.AddTasks]]
  *      message.
  *   1. When the simulation logic finishes producing tasks, it is ``ready``. It then sends a
  *      [[Coordinator.SimReady]] message to the [[Coordinator]].
  *   1. Eventually, one of the tasks completes and we receive a [[Simulation.TaskCompleted]]
  *      message. The simulation logic resumes execution.
  *   1. The [[Coordinator]] is now waiting for either an acknowledgement of the completed task
  *         ([[Coordinator.AckTasks]]) or the simulation to end ([[Coordinator.SimDone]]):
  *      a. The simulation may generate new ``tasks`` and either ``ack`` the finished tasks
  *         individually or become ``ready`` again as above.
  *      a. If the simulation logic completes, the [[Coordinator.SimDone]] message must be sent.
  *   1. The simulation may also react to things happening to other simulations. It can send a
  *      request to ``wait``. The [[Coordinator]] will then wait for the [[Simulation]] to
  *      send new tasks or to complete, as if a task just completed. This needs to happen while
  *      the [[Coordinator]] is still waiting for the other simulation(s) we are reacting to.
  *
  * @groupname act Taking Action
  * @groupname react Handling Events
  * @groupname internal Internal Management
  *
  * @param name The name of the simulation being managed.
  * @param coordinator A reference to the [[Coordinator]] actor running the simulation.
  */
trait Simulation {

  val waiting: HashSet[UUID] = HashSet[UUID]()

  val tasksToAdd: Queue[Task] = Queue[Task]()

  val abort: HashSet[UUID] = HashSet[UUID]()

  def name: String

  protected def manager: Manager

  /**
    * Initiates the execution of the simulation.
    *
    * @group act
    */
  def run(): Unit

  /**
    * Manages a completed [[TaskInstance]].
    *
    * The simulation logic must react to this by either registering more tasks or finishing.
    *
    * If new tasks are produced, the completed [[TaskInstance]] must be ``acknowledged`` to the
    * [[Coordinator]] via [[ack]]. Alternatively, if we do not want to ``ack`` all completed
    * tasks, we can just call [[ready]].
    *
    * In other words, after a [[TaskInstance]] completes, the [[Coordinator]] will expect either a
    * `AckTasks` (via [[ack]]) or `SimDone` (via [[done]], [[succeed]] or [[fail]]) before
    * it continues.
    *
    * @group react
    * @param task The completed [[Task]].
    * @param time The timestamp of completion and current time.
    */
  def complete(task: TaskInstance, time: Long): Unit = Unit

  def completed(time: Long, tasks: Seq[TaskInstance]): Unit = {
    this.synchronized {
      waiting ++= tasks.map(_.id)
    }
    tasks.foreach(complete(_, time))
  }

  /**
    * Stops/aborts the simulation.
    *
    * All the necessary steps for a gentle shutdown should be taken here.
    *
    * @group react
    */
  def stop(): Unit

  /**
    * Declare a new [[Task]] that needs to be sent to the [[Coordinator]] for simulation.
    *
    * @group act
    *
    * @param t The [[Task]] to send.
    * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
    */
  def task(t: Task): Unit = this.synchronized {
    tasksToAdd += t
  }

  /**
    * Notifies the [[Coordinator]] that some [[Task]]s should be aborted.
    *
    * @group act
    *
    * @param id The `UUID` of the [[Task]]s.
    */
  protected def abort(ids: UUID*): Unit = this.synchronized {
    abort ++= ids
  }

  /**
    * Notifies the [[Coordinator]] that the simulation completed.
    *
    * @group act
    * @param result The result of the simulation.
    */
  protected def done(result: Try[Any]): Unit = {
    clear()
    manager.simResponse(SimDone(name, result))
  }

  /**
    * Notifies the [[Coordinator]] that the simulation completed successfully.
    *
    * @group act
    * @param result The successful result of the simulation.
    */
  protected def succeed(result: Any): Unit = {
    clear()
    manager.simResponse(SimDone(name, Success(result)))
  }

  /**
    * Notifies the [[Coordinator]] that the simulation has failed or has been aborted.
    *
    * @group act
    * @param exception The `Throwable` that caused the failure.
    */
  protected def fail(exception: Throwable): Unit = {
    clear()
    manager.simResponse(SimDone(name, Failure(exception)))
  }

  /**
    * Notifies the [[Coordinator]] that the simulation has finished processing
    * one or more completed [[Task]]s.
    *
    * Identifies the tasks via their UUID.
    *
    * @group act
    */
  def ack(taskIDs: Seq[UUID]): Unit = this.synchronized {
    waiting --= taskIDs
    if (waiting.isEmpty) {
      val response = SimReady(name, tasksToAdd.clone.toSeq, abort.clone.toSeq, getLookahead())
      clear()
      manager.simResponse(response)
    }
  }

  /**
    * Notifies the [[Coordinator]] that the simulation has finished calculating
    * and is ready for virtual time to proceed.
    *
    * @group act
    */
  def ready(): Unit = {
    val response = SimReady(name, tasksToAdd.clone.toSeq, abort.clone.toSeq, getLookahead())
    clear()
    manager.simResponse(response)  
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
//  def simWait(): Unit = {
//    manager.waitFor(name)
 // }

  def getLookahead(): Lookahead = NoLookahead

  protected def clear(): Unit = {
    waiting.clear()
    tasksToAdd.clear()
    abort.clear()
  }
}


object Simulation {
  /**
    * Exception used when aborting a [[Task]].
    *
    * @param cause An optional underlying cause.
    */
  final case class TaskAbortedException(private val cause: Throwable = None.orNull)
      extends Exception("Task has been aborted", cause)

  /**
    * Exception used when the simulation is stopping.
    *
    * @param cause An optional underlying cause.
    */
  final case class SimulationStoppingException(private val cause: Throwable = None.orNull)
      extends Exception("Simulation is stopping", cause)
}

/**
  * A [[Simulation]] that simulates a single [[Task]].
  *
  * @param name The simulation name.
  * @param coordinator The [[Coordinator]].
  * @param resources The names of the [[TaskResource]]s the [[Task]] will require.
  * @param duration A [[ValueGenerator]] for the duration of the [[Task]].
  * @param cost A [[ValueGenerator]] for the cost of the [[Task]].
  * @param interrupt The [[Task.interrupt]] parameter.
  * @param priority The explicit priority of the [[Task]].
  */
class SingleTaskSimulation(
    override val name: String,
    override protected val manager: Manager,
    resources: Seq[String],
    duration: ValueGenerator[Long],
    cost: ValueGenerator[Long] = new ConstantGenerator(0L),
    interrupt: Int = (-1),
    priority: Task.Priority = Task.Medium
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

  override def complete(task: TaskInstance, time: Long): Unit = succeed((task, time))

  override def stop(): Unit = Unit
}

/**
  * Asynchronous Simulation implementation with callback functions.
  *
  * Each task needs to be accompanied by a callback function that implements the
  * simulation logic to be followed when the task completes.
  *
  * @param name The name of the simulation.
  * @param coordinator A reference to the [[Coordinator]] actor running the simulation.
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
    * @param f The function to call on success.
    * @return The created [[Callback]].
    */
  def callback(f: (TaskInstance, Long) => Unit): Callback =
    t => t.foreach { arg => f(arg._1, arg._2) }

  /**
    * A map of [[Task]] IDs that have been sent to the [[Coordinator]] and the callback functions
    * that need to be called when the tasks complete.
    *
    * @group internal
    */
  protected val tasks: Map[UUID, Callback] = Map()

  /**
    * Declare a new [[Task]] that needs to be sent to the [[Coordinator]] for simulation
    * with a pre-determined ID.
    *
    * The provided callback function will be called when the corresponding [[Task]] is completed.
    *
    * When it finishes executing, it must notify the [[Coordinator]] either by acknowledging the
    * completed task using [[ack]] or by completing the simulation using [[done]], [[succeed]] or [[fail]].
    *
    * The [[ready]] method can also be called if there is no need to acknowledge completed tasks
    * individually. This is unlikely in the current scenario where each task has its own callback,
    * but it's still worth mentioning.
    *
    * @group act
    *
    * @param t The [[Task]] to send.
    * @param callback The [[Callback]] function to be called when the corresponding [[Task]] completes.
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
    * @param task The [[Task]] that completed.
    * @param time The timestamp of its completion.
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
    * @param id The `UUID` of the [[Task]]s.
    */
  override protected def abort(ids: UUID*): Unit = {
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
  * This can be helpful for simulation implementations that use `Future`s to
  * capture their asynchronous logic.
  */
trait FutureTasks { self: AsyncSimulation =>

  /**
    * Declare a new [[Task]] that needs to be sent to the [[Coordinator]] for simulation
    * with a pre-determined ID and a `Future` instead of a [[AsyncSimulation.Callback Callback]].
    *
    * @group act
    *
    * @param t The [[Task]] to send.
    * @return A `Future` that completes when the [[Task]] is completed.
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
  * Works in conjunction with [[LookaheadScheduler]].
  *
  * Provides a lookahead structure that can be built up by the simulation and then sent to the scheduler
  * for use in making schedules which look into the future to consider upcoming tasks in scheduling.
  */
trait LookingAhead { self: Simulation =>
  var lookahead: Lookahead = LookaheadSet()

  override def getLookahead(): Lookahead = lookahead


  val completed: collection.mutable.Set[(UUID, Long)] = collection.mutable.Set()


  /**
    * Manages a [[Task]] whose simulation has completed.
    *
    * Removes the task from the lookahead structure and sends this updated structure to the
    * scheduler before calling the `complete` method implementation of the parent class.
    *
    * @param task The [[Task]] that completed.
    * @param time The timestamp of its completion.
    */
  override def complete(task: TaskInstance, time: Long): Unit = {
    completed += ((task.id, time))
    lookahead = lookahead - task.id
    lookahead.getTaskData(completed).flatMap(_._1.id) foreach { id => lookahead = lookahead - id }
    self.complete(task, time)
  }
}
