package com.workflowfm.simulator

import akka.pattern.{ ask, pipe }
import akka.actor.{ Actor, ActorRef, Props }
import akka.util.Timeout
import scala.util.{ Success, Failure }
import scala.concurrent.duration.Duration
import scala.collection.mutable.{ Map, Queue }
import scala.concurrent.{ Future, Promise }
import java.util.UUID
import java.util.concurrent.TimeUnit

/**
  * An actor managing the interaction between a simulation and a [[Coordinator]].
  *
  * We view a simulation as a case or workflow of simulated tasks. This actor is responsible
  * for keeping track of all the tasks for this simulation, informing the [[Coordinator]] of new
  * tasks to be added and informing the simulation processes when a task is completed.
  *
  * = Interface =
  * The interface to set up a simulation has 8 key methods:
  *   1. `run`: Start the execution of the simulation logic.
  *   1. `task`: Produce new simulation tasks to be run.
  *   1. `ack`: Tell the [[Coordinator]] we are done processing one or more finished tasks.
  *   1. `ready`: Tell the [[Coordinator]] we are ready and waiting for virtual time to progress.
  *   1. `simWait`: Tell the [[Coordinator]] to wait for us when reacting to another simulation.
  *   1. `complete`: Manage a task that just completed following the simulation logic.
  *   1. `done`: Tell the [[Coordinator]] the simulation is completed.
  *   1. `fail`: Tell the [[Coordinator]] the simulation failed with some exception.
  *
  * The interface can be accessed in 2 modes:
  *   1. ```Direct Interface:``` The simulation logic can be coded directly in a subclass, by
  *      implementing [[run]] and using [[task(t* task]],
  *      [[simWait]], [[ack]], [[ready]], [[done]], and [[fail]].
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
  *      ([[Coordinator.AckTasks]]) or the simulation to end ([[Coordinator.SimDone]]):
  *      a. The simulation may generate new ``tasks`` and either ``ack`` the finished tasks 
  *         individually or become ``ready`` again as above.
  *      a. If the simulation logic completes, the [[Coordinator.SimDone]] message must be sent.
  *   1. The simulation may also react to things happening to other simulations. It can send a
  *      request to ``wait``. The [[Coordinator]] will then wait for the [[Simulation]] to
  *      send new tasks or to complete, as if a task just completed. This needs to happen while
  *      the [[Coordinator]] is still waiting for the other simulation(s) we are reacting to.
  *
  * @groupname api Simulation Interface
  * @groupname internal Internal Management
  *
  * @param name The name of the simulation being managed.
  * @param coordinator A reference to the [[Coordinator]] actor running the simulation.
  */
abstract class Simulation(
   name: String,
    coordinator: ActorRef
) extends Actor {

  /**
    * Initiates the execution of the simulation.
    *
    * @group api
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
    * `AckTasks` (via [[ack]]) or `SimDone` (via [[done]] or [[fail]]) before
    * it continues.
    *
    * @group api
    * @param task The completed [[Task]].
    * @param time The timestamp of completion and current time.
    */
  def complete(task: Task, time: Long): Unit

  /**
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation.
    *
    * @group api
    *
    * @param t The [[TaskGenerator]] to send.
    * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
    */
  def task(t: TaskGenerator, resources: String*): Unit = {
    val id = java.util.UUID.randomUUID
    task(id, t, resources)
  }

  /**
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation
    * with a pre-determined ID.
    *
    * @group api
    *
    * @param id The pre-determined task ID.
    * @param t The [[TaskGenerator]] to send.
    * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
    */
  protected def task(id: UUID, t: TaskGenerator, resources: Seq[String]): Unit = {
    coordinator ! Coordinator.AddTask(id, t, resources)
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
    coordinator ! Coordinator.SimStarted(name)
    run()
  }

  /**
    * Notifies the [[Coordinator]] that the simulation completed successfully.
    *
    * @group api
    * @param results The successful result of the simulation.
    */
  protected def done(result: Any): Unit = { 
    coordinator ! Coordinator.SimDone(name, Success(result))
  }

  /**
    * Notifies the [[Coordinator]] that the simulation has failed or has been aborted.
    *
    * @group api
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
    * @group api
    */
  def ack(tasks: Seq[UUID]): Unit = {
    coordinator ! Coordinator.AckTasks(tasks)
  }

  /**
    * Notifies the [[Coordinator]] that the simulation has finished calculating
    * and is ready for virtual time to proceed.
    *
    * @group api
    */
  def ready(): Unit = {
    coordinator ! Coordinator.SimReady
  }

  /**
    * Requests that the [[Coordinator]] waits for this simulation before it continues.
    *
    * The simulation needs to either register more tasks and become ``ready`` or finish.
    *
    * In other words, the [[Coordinator]] will expect either a `SimReady` (via [[ready]])
    * or `SimDone` (via [[done]] or [[fail]]) before it continues.
    *
    * @note We assume the [[Coordinator]] is already waiting for another simulation when the
    *       request is made. Otherwise virtual time may progress unexpectedly and cause
    *       unpredictable behaviour depending on the timing of the [[Coordinator]] messages.
    *
    * @group api
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
    * @group api
    *
    * @return The [[Receive]] behaviour for the simulation interface.
    */
  def simulationReceive: Receive = {
    case Simulation.Start => start()
    case Simulation.Ready => ready()
    case Simulation.TaskCompleted(task, time) => complete(task, time)
    case Simulation.AckTasks(tasks) => ack(tasks)
    case Simulation.Wait => coordinator.forward(Coordinator.WaitFor(self))
    case Simulation.Done(result) => done(result)
    case Simulation.Fail(exception) => fail(exception)
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
    * Produces a new [[TaskGenerator]] with a specified ID for simulation.
    *
    * @see [[Simulation.task(i* task(id, t, resources)]]
    * @group process
    *
    * @param id The [[Task]] ID to be used.
    * @param t The [[TaskGenerator]] to generate the [[Task]].
    * @param resources The names of the [[TaskResource]]s the [[Task]] will require.
    */
  case class AddTaskWithId(id: UUID, t: TaskGenerator, resources: Seq[String])

  /**
    * Produces a new [[TaskGenerator]] for simulation.
    *
    * @see [[Simulation.task(t* task(t, resources)]]
    * @group process
    *
    * @param id The [[Task]] ID to be used.
    * @param t The [[TaskGenerator]] to generate the [[Task]].
    * @param resources The names of the [[TaskResource]]s the [[Task]] will require.
    */
  case class AddTask(t: TaskGenerator, resources: Seq[String])

  /**
    * Informs a [[Task]] has completed
    *
    * @see [[Simulation.complete]]
    * @group process
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
    * Tells the [[Simulation]] to complete successfully.
    *
    * @see [[Simulation.done]]
    * @group process
    *
    * @param result The successful simulation result.
    */
  case class Done(result: Any)

  /**
    * Tells the [[Simulation]] to fail.
    *
    * @see [[Simulation.fail]]
    * @group process
    *
    * @param exception The `Throwable` causing the failure.
    */
  case class Fail(exception: Throwable)
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
    *
    * @return
    */
  override def run() = {
    val generator = TaskGenerator(name + "Task", name, duration, cost, interrupt, priority)
    task(generator, resources: _*)
    ready()
  }

  override def complete(task: Task, time: Long) = done((task, time))
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
    * 
    * @group api
    */
  type Callback = (Task, Long) => Unit

  /**
    * A map of [[Task]] IDs that have been sent to the [[Coordinator]] and the callback functions
    * that need to be called when the tasks complete.
    *
    * @group internal
    */
  protected val tasks: Map[UUID, Callback] = Map()

  /**
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation.
    *
    * The provided callback function will be called when the corresponding [[Task]] is completed.
    * When it finishes executing, it must notify the [[Coordinator]] either by acknowledging the 
    * completed task using [[ack]] or by completing the simulation using [[done]] or [[fail]].
    *
    * The [[ready]] method can also be called if there is no need to acknowledge completed tasks
    * individually. This is unlikely in the current scenario where each task has its own callback,
    * but it's still worth mentioning.
    *
    * @group api
    *
    * @param t The [[TaskGenerator]] to send.
    * @param callback The [[Callback]] function to be called when the corresponding [[Task]] completes.
    * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
    */
  def task(t: TaskGenerator, callback: Callback, resources: String*): Unit = {
    val id = java.util.UUID.randomUUID
    task(id, t, callback, resources)
  }

  /**
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation
    * with a pre-determined ID.
    *
    * The provided callback function will be called when the corresponding [[Task]] is completed.
    * When it finishes executing, it must notify the [[Coordinator]] either by acknowledging the 
    * completed task using [[ack]] or by completing the simulation using [[done]] or [[fail]].
    *
    * The [[ready]] method can also be called if there is no need to acknowledge completed tasks
    * individually. This is unlikely in the current scenario where each task has its own callback,
    * but it's still worth mentioning.
    *
    * @group api
    *
    * @param id The pre-determined task ID.
    * @param t The [[TaskGenerator]] to send.
    * @param callback The [[Callback]] function to be called when the corresponding [[Task]] completes.
    * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
    */
  protected def task(
      id: UUID,
      t: TaskGenerator,
      callback: Callback,
      resources: Seq[String]
  ): Unit = {
    tasks += id -> callback
    super.task(id, t, resources)
  }

  /**
    * Manages a [[Task]] whose simulation has completed.
    *
    * Calls the corresponding [[Callback]] in the `tasks` map and then removes the entry.
    *
    * @group internal
    *
    * @param task The [[Task]] that completed.
    * @param time The timestamp of its completion.
    */
  override def complete(task: Task, time: Long) = {
    tasks.get(task.id).map(_(task, time))
    tasks -= task.id
  }

/**
  * A [[Callback]] function that notifies another `Actor` about a [[Task]] completion.
  * 
  * This allows the simulation logic of a callback to be implemented in another actor.
  * The other actor can use the [[Simulation.AddTask]], [[Simulation.AddTaskWithId]],
  * [[Simulation.AckTasks]], and [[Simulation.Ready]] messages to make progress instead 
  * of calling the respective methods.
  *
  * @group api
  * 
  * @param actor
  * @return
  */
  def actorCallback(actor: ActorRef): Callback = (task, time) => {
    actor ! (task, time)
  }

  /**
    * Defines the actor receive behaviour for the simulation interface.
    *
    * This allows subclasses to extend [[receive]] with additional behaviour.
    *
    * @group api
    *
    * @return The [[Receive]] behaviour for the simulation interface.
    */
  override def simulationReceive: Receive = super.simulationReceive orElse {
    case Simulation.AddTaskWithId(id, t, r) => task(id, t, actorCallback(sender), r)
    case Simulation.AddTask(t, r) => task(t, actorCallback(sender), r: _*)
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
  * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation with
  * a `Future` instead of a [[AsyncSimulation.Callback Callback]].
  *
  * @group api
  * 
  * @param t The [[TaskGenerator]] to send.
  * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
  * @return A `Future` that completes when the [[Task]] is completed.
  */
  def futureTask(t: TaskGenerator, resources: String*): Future[(Task, Long)] = {
    val id = java.util.UUID.randomUUID
    futureTask(id, t, resources)
  }

/**
  * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation
  * with a pre-determined ID and a `Future` instead of a [[AsyncSimulation.Callback Callback]].
  *
  * @group api
  *
  * @param id The pre-determined task ID.
  * @param t The [[TaskGenerator]] to send.
  * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
  * @return A `Future` that completes when the [[Task]] is completed.
  */
  def futureTask(id: UUID, t: TaskGenerator, resources: Seq[String]): Future[(Task, Long)] = {
    val p = Promise[(Task, Long)]()
    def call: Callback = (task, time) => p.success(task, time)
    task(id, t, call, resources)
    p.future
  }
}
