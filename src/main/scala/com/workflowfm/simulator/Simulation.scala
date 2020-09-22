package com.workflowfm.simulator

import akka.pattern.{ ask, pipe }
import akka.actor.{ Actor, ActorRef, Props }
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.collection.mutable.{ Map, Queue }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import java.util.UUID
import scala.collection.mutable

/**
  * An actor managing the interaction between a simulation and a [[Coordinator]].
  *
  * We view a simulation as a case or workflow of simulated tasks. This actor is responsible
  * for keeping track of all the tasks for this simulation, informing the [[Coordinator]] of new
  * tasks to be added and informing the simulation processes when a task is completed.
  *
  * = Interface =
  * The interface to set up a simulation has 6 key methods:
  *   1. `run`: Start the execution of the simulation logic.
  *   1. `task`: Produce new simulation tasks to be run.
  *   1. `ack`: Tell the [[Coordinator]] we are done processing one or more finished tasks.
  *   1. `ready`: Tell the [[Coordinator]] we are ready and waiting for virtual time to progress.
  *   1. `simWait`: Tell the [[Coordinator]] to wait for us when reacting to another simulation.
  *   1. `complete`: Manage a task that just completed following the simulation logic.
  *
  * The interface can be accessed in 2 modes:
  *   1. ```Direct Interface:``` The simulation logic can be coded directly in a subclass, by
  *      implementing [[run]] and using [[task(t* task]],
  *      [[simWait]], [[ack]] and [[ready]].
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
         ([[Coordinator.AckTasks]]) or the simulation to end ([[Coordinator.SimDone]]):
  *      a. The simulation may generate new ``tasks`` and either ``ack`` the finished tasks 
  *         individually or become ``ready`` again as above.
  *      a. If the simulation logic completes, the [[Coordinator.SimDone]] message is sent
  *         automatically.
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
  * @param executionContext
  */
abstract class Simulation(
   name: String,
   protected val coordinator: ActorRef
)(implicit executionContext: ExecutionContext)
    extends Actor {
    
  /**
    * Initiates the execution of the simulation.
    *
    * @group api
    * @return A `Future` that completes with a custom output when the simulation is completed.
    */
  def run(): Future[Any]

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
    * `AckTasks` (via [[ack]]) or `SimDone` (via the [[run]] `Future` completing) before
    * it continues.
    *
    * @group api
    * @param task The completed [[Task]].
    * @param time The timestamp of completion and current time.
    */
  def complete(task: Task, time: Long): Unit

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
  protected def task(t: TaskGenerator): Unit = {
    coordinator ! Coordinator.AddTask(t)
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
    
    run().onComplete { x =>
      coordinator ! Coordinator.SimDone(name, x)
    }
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
    sendLookaheadStructure()
    coordinator ! Coordinator.AckTasks(tasks)
  }

  /**
    * Notifies the [[Coordinator]] that the simulation has finished calculating
    * and is ready for virtual time to proceed.
    *
    * @group api
    */
  def ready(): Unit = {
    sendLookaheadStructure()
    coordinator ! Coordinator.SimReady
  }

  /**
    * Requests that the [[Coordinator]] waits for this simulation before it continues.
    *
    * The simulation needs to either register more tasks and become ``ready`` or finish.
    *
    * In other words, the [[Coordinator]] will expect either a `SimReady` (via [[ready]])
    * or `SimDone` (via the [[run]] `Future` completing) before it continues.
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

  def sendLookaheadStructure():Unit = { Unit }

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
    case Simulation.TaskCompleted(task, time) => complete(task, time)
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
  * @param executionContext
  */
class SingleTaskSimulation(
    name: String,
    coordinator: ActorRef,
    resources: Seq[String],
    duration: ValueGenerator[Long],
    cost: ValueGenerator[Long] = new ConstantGenerator(0L),
    interrupt: Int = (-1),
    priority: Task.Priority = Task.Medium
)(implicit executionContext: ExecutionContext)
    extends Simulation(name, coordinator)(executionContext) {

  private val promise = Promise[(Task, Long)]()

  /**
    * @inheritdoc
    *
    * Creates and adds the corresponding [[TaskGenerator]], then calls [[ready]] immediately.
    *
    * @return
    */
  override def run() = if (promise.isCompleted) promise.future
  else {
    val generator = TaskGenerator(name + "Task", name, duration, cost).withResources(resources).withInterrupt(interrupt).withPriority(priority)
    task(generator)
    ready()
    promise.future
  }

  override def complete(task: Task, time: Long) = promise.success((task, time))
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
    * @param executionContext
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
  )(implicit executionContext: ExecutionContext): Props =
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
  * @param executionContext
  */
abstract class AsyncSimulation(
    name: String,
    coordinator: ActorRef
)(implicit executionContext: ExecutionContext)
    extends Simulation(name, coordinator)(executionContext) {

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
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation
    * with a pre-determined ID.
    *
    * The provided callback function will be called when the corresponding [[Task]] is completed.
    * When it finishes executing, it must notify the [[Coordinator]] either by acknowledging the 
    * completed task using [[ack]] or by completing the simulation (i.e. completing the `Future` 
    * returned by [[run]]).
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
      t: TaskGenerator,
      callback: Callback
  ): Unit = {
    tasks += t.id -> callback
    super.task(t)
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
  override def simulationReceive: Receive = {
    case Simulation.Start => start()
    case Simulation.Ready => ready()
    case Simulation.AckTasks(tasks) => ack(tasks)
    case Simulation.TaskCompleted(task, time) => complete(task, time)
    case Simulation.AddTask(t) => task(t, actorCallback(sender))
    case Simulation.Wait => coordinator.forward(Coordinator.WaitFor(self))
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
  * @group api
  *
  * @param t The [[TaskGenerator]] to send.
  * @return A `Future` that completes when the [[Task]] is completed.
  */
  def futureTask(t: TaskGenerator): Future[(Task, Long)] = {
    val p = Promise[(Task, Long)]()
    def call: Callback = (task, time) => p.success(task, time)
    task(t, call)
    p.future
  }
}

trait Lookahead extends Simulation {
  var lookahead: LookaheadStructure = LookaheadSet()

  override def sendLookaheadStructure():Unit = {  coordinator ! Coordinator.SetSchedulerLookaheadObject(lookahead) }

  abstract override def complete(task: Task, time: Long) = {
    lookahead = ( lookahead.complete(task.id,time) ) - task.id
    coordinator ! Coordinator.SetSchedulerLookaheadObject(lookahead) //todo optimise 
    super.complete(task,time)
  }
}
