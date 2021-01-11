package com.workflowfm.proter.akka

import java.util.UUID
import java.util.concurrent.TimeUnit

import scala.collection.mutable.Map
import scala.concurrent.{ Future, Promise }
import scala.util.{ Try, Success, Failure }

import akka.actor.{ Actor, ActorRef, Props }
import akka.pattern.ask
import akka.util.Timeout

import com.workflowfm.proter.{ TaskGenerator, Task, Lookahead, LookaheadSet, ValueGenerator, ConstantGenerator }

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
abstract class Simulation(
    name: String,
    protected val coordinator: ActorRef
) extends Actor {

  /**
    * Initiates the execution of the simulation.
    *
    * @group act
    */
  def run(): Unit

  /**
    * Manages a completed [[Task]].
    *
    * The simulation logic must react to this by either registering more tasks or finishing.
    *
    * If new tasks are produced, the completed [[Task]] must be ``acknowledged`` to the
    * [[Coordinator]] via [[ack]]. Alternatively, if we do not want to ``ack`` all completed
    * tasks, we can just call [[ready]].
    *
    * In other words, after a [[Task]] completes, the [[Coordinator]] will expect either a
    * `AckTasks` (via [[ack]]) or `SimDone` (via [[done]], [[succeed]] or [[fail]]) before
    * it continues.
    *
    * @group react
    * @param task The completed [[Task]].
    * @param time The timestamp of completion and current time.
    */
  def complete(task: Task, time: Long): Unit = Unit

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
  def simulationReceive: Receive = {
    case Simulation.Start => start()
    case Simulation.Ready => ready()
    case Simulation.TaskCompleted(task, time) => complete(task, time)
    case Simulation.AckTasks(tasks) => ack(tasks)
    case Simulation.AbortTasks(ids) => abort(ids: _*)
    case Simulation.Wait => coordinator.forward(Coordinator.WaitFor(self))
    case Simulation.Done(result) => done(result)
    case Simulation.Succeed(result) => succeed(result)
    case Simulation.Fail(exception) => fail(exception)
    case Simulation.Stop => stop()
  }

  def receive = simulationReceive
}

/**
  * Defines the messages a [[Simulation]] can receive by default.
  *
  * @groupname coordinator Sent by Coordinator
  * @groupname process Sent by SimulatedProcess
  */
object Simulation {
  /**
    * Instructs the start of simulation logic execution.
    *
    * @group coordinator
    */
  case object Start
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
  case class AddTask(t: TaskGenerator)

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
  case class TaskCompleted(task: Task, time: Long)

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
  * @param interrupt The [[TaskGenerator.interrupt]] parameter.
  * @param priority The explicit priority of the [[Task]].
  */
class SingleTaskSimulation(
    name: String,
    coordinator: ActorRef,
    resources: Seq[String],
    duration: ValueGenerator[Long],
    cost: ValueGenerator[Long] = new ConstantGenerator(0L),
    interrupt: Int = (-1),
    priority: Task.Priority = Task.Medium
) extends Simulation(name, coordinator) {

  /**
    * @inheritdoc
    *
    * Creates and adds the corresponding [[TaskGenerator]], then calls [[ready]] immediately.
    */
  override def run(): Unit = {
    val generator = TaskGenerator(s"${name}Task", name, duration, cost)
      .withResources(resources)
      .withInterrupt(interrupt)
      .withPriority(priority)
    task(generator)
    ready()
  }

  override def complete(task: Task, time: Long): Unit = succeed((task, time))

  override def stop(): Unit = Unit
}

object SingleTaskSimulation {

  /**
    * Creates props of a [[SingleTaskSimulation]] actor.
    *
    * @param name The simulation name.
    * @param coordinator The [[Coordinator]].
    * @param resources The names of the [[TaskResource]]s the [[Task]] will require.
    * @param duration A [[ValueGenerator]] for the duration of the [[Task]].
    * @param cost A [[ValueGenerator]] for the cost of the [[Task]].
    * @param interrupt The [[TaskGenerator.interrupt]] parameter.
    * @param priority The explicit priority of the [[Task]].
    * @return
    */
  def props(
      name: String,
      coordinator: ActorRef,
      resources: Seq[String],
      duration: ValueGenerator[Long],
      cost: ValueGenerator[Long] = new ConstantGenerator(0L),
      interrupt: Int = (-1),
      priority: Task.Priority = Task.Medium
  ): Props =
    Props(
      new SingleTaskSimulation(
        name,
        coordinator,
        resources,
        duration,
        cost,
        interrupt,
        priority
      )
    )
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
abstract class AsyncSimulation(
    name: String,
    coordinator: ActorRef
) extends Simulation(name, coordinator) {

  /**
    * The type of the callback function.
    *
    * Its input consists of the generated [[Task]] and the timestamp when it was completed.
    * A `Failure` input corresponds to an exception happening or an aborted task.
    *
    * @group react
    */
  type Callback = Try[(Task, Long)] => Unit

  /**
    * Creates a simple success callback from a function.
    *
    * Does nothing on failure.
    *
    * @param f The function to call on success.
    * @return The created [[Callback]].
    */
  def callback(f: (Task, Long) => Unit): Callback =
    t => t.foreach { arg => f(arg._1, arg._2) }

  /**
    * A map of [[Task]] IDs that have been sent to the [[Coordinator]] and the callback functions
    * that need to be called when the tasks complete.
    *
    * @group internal
    */
  protected val tasks: Map[UUID, Callback] = Map()

  /**
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation
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
    * @param t The [[TaskGenerator]] to send.
    * @param callback The [[Callback]] function to be called when the corresponding [[Task]] completes.
    */
  def task(
      t: TaskGenerator,
      callback: Callback
  ): Unit = {
    tasks += t.id -> callback
    super.task(t)
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
  override def complete(task: Task, time: Long): Unit = {
    tasks.get(task.id).map(_(Success(task, time)))
    tasks -= task.id
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

  /**
    * A [[Callback]] function that notifies another `Actor` about a [[Task]] completion.
    *
    * This allows the simulation logic of a callback to be implemented in another actor.
    * The other actor can use the [[Simulation.AddTask]], [[Simulation.AddTaskWithId]],
    * [[Simulation.AckTasks]], and [[Simulation.Ready]] messages to make progress instead
    * of calling the respective methods.
    *
    * @group react
    *
    * @param actor
    * @return
    */
  def actorCallback(actor: ActorRef): Callback = t => {
    actor ! t
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
  override def simulationReceive: Receive = super.simulationReceive orElse {
      case Simulation.AddTask(t) => task(t, actorCallback(sender))
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
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation
    * with a pre-determined ID and a `Future` instead of a [[AsyncSimulation.Callback Callback]].
    *
    * @group act
    *
    * @param t The [[TaskGenerator]] to send.
    * @return A `Future` that completes when the [[Task]] is completed.
    */
  def futureTask(t: TaskGenerator): Future[(Task, Long)] = {
    val p = Promise[(Task, Long)]()
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
trait LookingAhead extends Simulation {
  var lookahead: Lookahead = LookaheadSet()

  /**
    * Sends the lookahead structure to the scheduler
    */
  def sendLookahead(): Unit = { coordinator ! Coordinator.UpdateLookahead(lookahead) }

  /**
    * Manages a [[Task]] whose simulation has completed.
    *
    * Removes the task from the lookahead structure and sends this updated structure to the
    * scheduler before calling the `complete` method implementation of the parent class.
    *
    * @param task The [[Task]] that completed.
    * @param time The timestamp of its completion.
    */
  val completed: collection.mutable.Set[(java.util.UUID, Long)] = collection.mutable.Set()

  override def complete(task: Task, time: Long): Unit = {
    completed += ((task.id, time))
    lookahead = lookahead - task.id
    lookahead.getTaskData(completed) foreach { x => lookahead = lookahead - x._1.id }
    sendLookahead()
    super.complete(task, time)
  }
}
