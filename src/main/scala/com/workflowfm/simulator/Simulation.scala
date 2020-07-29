package com.workflowfm.simulator

import akka.pattern.{ ask, pipe }
import akka.actor.{ Actor, ActorRef, Props }
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.collection.mutable.{ Map, Queue }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import java.util.UUID

/**
  * An actor managing the interaction between a simulation and a [[Coordinator]].
  * 
  * We view a simulation as a case or workflow of simulated tasks. This actor is responsible
  * for keeping track of all the tasks for this simulation, informing the [[Coordinator]] of new
  * tasks to be added and informing the simulation processes when a task is completed.
  *
  * = Interface =
  * The interface to set up a simulation has 4 key methods:
  *   1. `run`: Start the execution of the simulation logic.
  *   1. `task`: Produce new simulation tasks to be run.
  *   1. `ready`: Tell the [[Coordinator]] we are ready and waiting for virtual time to progress.
  *   1. `requestWait`: Tell the [[Coordinator]] to wait for us when reacting to another simulation.
  * 
  * The interface can be accessed in 2 modes:
  *   1. ```Direct Interface:``` The simulation logic can be coded directly in a subclass, by 
  *      implementing [[run]] and using [[task(t* task]],
  *      [[requestWait()* requestWait()]] and [[ready]].
  *   1. ```Actor Interface:``` The simulation logic can be distributed to other actors, typically
  *      [[SimulatedProcess]]es. These
  *      can interact with `SimulationActor` using the [[SimulationActor.AddTask]],
  *      [[SimulationActor.RequestWait]] and [[SimulationActor.Ready]] messages.
  * 
  * = Interaction Flow =
  * The interaction flow with the [[Coordinator]] is expected as follows:
  *   1. Receiving a [[SimulationActor.Start]] message starts the simulation. This is confirmed 
  *      via a [[Coordinator.SimStarted]] response.
  *   1. The simulation logic starts executing via [[SimulationActor.run]] and produces some 
  *      simulation ``tasks``.
  *   1. When the simulation logic finishes producing tasks, it is ``ready``. Sending a 
  *      [[Coordinator.AddTasks]] message informs the [[Coordinator]] about any new
  *      tasks and that the simulation is ready to proceed.
  *   1. Eventually, one of the tasks completes and we receive a [[SimulationActor.TaskCompleted]]
  *      message. The simulation logic resumes execution.
  *   1. The [[Coordinator]] is now waiting for either new tasks ([[Coordinator.AddTasks]]) or
  *      the simulation to end ([[Coordinator.SimDone]]):
  *      a. The simulation may generate new ``tasks`` and then become ``ready`` again as above.
  *      a. If the simulation logic completes, the [[Coordinator.SimDone]] message is sent
  *         automatically.
  *   1. The simulation may also react to things happening to other simulations. It can send a
  *      request to ``wait``. The [[Coordinator]] will then wait for the [[SimulationActor]] to 
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
abstract class SimulationActor (
  val name: String,
  val coordinator: ActorRef)
  (implicit executionContext: ExecutionContext)
    extends Actor {

  /**
    * Initiates the execution of the simulation.
    *
    * @group api
    * @return A `Future` that completes with a custom output when the simulation is completed.
    */
  def run(): Future[Any]

  /**
    * A map of [[Task]] IDs that have been sent to the [[Coordinator]] and the `Promise`s that
    * need to be fulfilled when the tasks complete.
    * 
    * @group internal
    */
  private val tasks: Map[UUID,Promise[(Task,Long)]] = Map()

  /**
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation.
    * 
    * The returned `Future` will complete when the [[Task]] simulation is completed. The simulation
    * logic must react to this by either registering more tasks and becoming ``ready`` or finishing.
    * 
    * In other words, after the `Future` completes, the [[Coordinator]] will expect either a 
    * `AddTasks` (via [[ready]]) or `SimDone` (via the [[run]] `Future` completing) before 
    * it continues.
    * 
    * @group api
    *
    * @param t The [[TaskGenerator]] to send.
    * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
    * @return A `Future` that completes when the generated [[Task]] has completed, 
    *         containing the [[Task]] and its completion time.
    */
  def task(t: TaskGenerator, resources: String*): Future[(Task,Long)] = {
    val id = java.util.UUID.randomUUID
    task(id, t, None, resources)
  }

  /**
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation
    * with a pre-determined ID.
    * 
    * The returned `Future` will complete when the [[Task]] simulation is completed. The simulation
    * logic must react to this by either registering more tasks and becoming ``ready`` or finishing.
    * 
    * In other words, after the `Future` completes, the [[Coordinator]] will expect either a 
    * `AddTasks` (via [[ready]]) or `SimDone` (via the [[run]] `Future` completing) before 
    * it continues.
    * 
    * @group api
    *
    * @param id The pre-determined task ID.
    * @param t The [[TaskGenerator]] to send.
    * @param caller Some reference to an actor that generated the task in order to notify them when
    *               the task is completed, or `None` if no actor needs to be notified.
    * @param resources The names of the [[TaskResource]]s that need to be used for the [[Task]].
    * @return A `Future` that completes when the generated [[Task]] has completed, 
    *         containing the [[Task]] and its completion time.
    */
  protected def task(id: UUID, t: TaskGenerator, caller: Option[ActorRef], resources: Seq[String]): Future[(Task,Long)] = {
    val p = Promise[(Task,Long)]()
    tasks += id -> p
    coordinator ! Coordinator.AddTask(id, t, resources)
    caller match {
      case None => p.future
      case Some(actor) => p.future pipeTo actor
    }
  }

  /**
    * Manages a [[Task]] whose simulation has completed.
    * 
    * Fulfils the corresponding `Promise` in the `tasks` map and then removes the entry.
    * 
    * @group internal
    *
    * @param task The [[Task]] that completed.
    * @param time The timestamp of its completion.
    */
  protected def complete(task: Task, time: Long) = {
    tasks.get(task.id).map (_.success((task,time)))
    tasks -= task.id
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
    * Notifies the [[Coordinator]] that the simulation has finished calculating
    * and is ready for virtual time to proceed.
    * 
    * Also sends the new tasks for simulation to the [[Coordinator]] and clears
    * the queue.
    * @todo update
    * @group api
    */
  def ack(tasks: Seq[UUID]): Unit = {
    coordinator ! Coordinator.AckTasks(tasks)
  }

  def ready(): Unit = {
    coordinator ! Coordinator.SimReady
  }

  /**
    * Requests that the [[Coordinator]] waits for this simulation before it continues.
    * 
    * The simulation needs to either register more tasks and become ``ready`` or finish.
    * 
    * In other words, the [[Coordinator]] will expect either a `AddTasks` (via [[ready]]) 
    * or `SimDone` (via the [[run]] `Future` completing) before it continues.
    * 
    * @note Uses the Akka `ask` pattern, so assumes no other interaction with the 
    *       [[Coordinator]] during this.
    * 
    * @note We assume the [[Coordinator]] is already waiting for another simulation when the
    *       request is made. Otherwise virtual time may progress unexpectedly and cause 
    *       unpredictable behaviour depending on the timing of the [[Coordinator]] messages.
    *
    * @group api
    * 
    * @return A `Future` of the acknowledgement message [[SimulationActor.AckWait]]
    */
  def requestWait(): Future[Any] = {
    (coordinator ? Coordinator.WaitFor(self))(Timeout(1, TimeUnit.DAYS))
  }

  /**
    * Requests that the [[Coordinator]] waits for this simulation before it continues, and 
    * forwards the acknowledgement.
    * 
    * Same as [[requestWait():scala\.concurrent\.Future[Any]* requestWait()]], 
    * but pipes the [[SimulationActor.AckWait]] response
    * to another actor.
    * 
    * The simulation needs to either register more tasks and become ``ready`` or finish.
    * 
    * In other words, the [[Coordinator]] will expect either a `AddTasks` (via [[ready]]) 
    * or `SimDone` (via the [[run]] `Future` completing) before it continues.
    * 
    * @note Uses the Akka `ask` pattern, so assumes no other interaction with the 
    *       [[Coordinator]] during this.
    * 
    * @note We assume the [[Coordinator]] is already waiting for another simulation when the
    *       request is made. Otherwise virtual time may progress unexpectedly and cause 
    *       unpredictable behaviour depending on the timing of the [[Coordinator]] messages.
    *
    * @see [[requestWait():scala\.concurrent\.Future[Any]* requestWait()]]
    * @group api
    * 
    * @return A `Future` of the acknowledgement message [[SimulationActor.AckWait]]
    */
  def requestWait(ack: ActorRef): Future[Any] = {
    requestWait() pipeTo ack
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
  def simulationActorReceive: Receive = {
    case SimulationActor.Start => start()
    case SimulationActor.Ready => ready()
    case SimulationActor.AckTasks(tasks) => ack(tasks)
    case SimulationActor.TaskCompleted(task, time) => complete(task, time)
    case SimulationActor.AddTask(id, t, r) => task(id, t, Some(sender), r)
    case SimulationActor.RequestWait => requestWait(sender())
  }

  def receive = simulationActorReceive
}

/**
  * Defines the messages a [[SimulationActor]] can receive by default.
  * 
  * @groupname coordinator Sent by Coordinator
  * @groupname process Sent by SimulatedProcess
  */
object SimulationActor {
  /**
    * Instructs the start of simulation logic execution.
    * 
    * @group coordinator
    */
  case object Start
  /**
    * Triggers [[SimulationActor.ready]].
    *
    * @see [[SimulationActor.ready]]
    * @group process
    */
  case object Ready
  /**
    * Produces a new [[TaskGenerator]] for simulation.
    * 
    * @see [[SimulationActor.task(i* task(id, t, resources)]]
    * @group process
    *
    * @param id The [[Task]] ID to be used.
    * @param t The [[TaskGenerator]] to generate the [[Task]].
    * @param resources The names of the [[TaskResource]]s the [[Task]] will require.
    */
  case class AddTask(id: UUID, t: TaskGenerator, resources: Seq[String])
  /**
    * Informs a [[Task]] has completed
    *
    * @see [[SimulationActor.complete]]
    * @group process
    * 
    * @param task The completed [[Task]].
    * @param time The (virtual) time of completion.
    */  
  case class TaskCompleted(task: Task, time: Long)
  /**
    * Tells the [[SimulationActor]] to request that [[Coordinator]] waits.
    *
    * @see [[SimulationActor.requestWait(a* requestWait(ActorRef)]]
    * @group process
    */  
  case object RequestWait
  /**
    * Acknowledges that the [[Coordinator]] is waiting as requested.
    *
    * @group coordinator
    */  
  case object AckWait

// TODO document
  case class AckTasks(tasks: Seq[UUID])
}

/**
  * A [[SimulationActor]] that simulates a single [[Task]].
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
class TaskSimulatorActor(
  name: String,
  coordinator: ActorRef,
  resources: Seq[String],
  duration: ValueGenerator[Long],
  cost: ValueGenerator[Long]=new ConstantGenerator(0L),
  interrupt: Int=(-1),
  priority: Task.Priority=Task.Medium
)(implicit executionContext: ExecutionContext)
    extends SimulationActor(name,coordinator) {
  /**
    * @inheritdoc
    * 
    * Creates and adds the corresponding [[TaskGenerator]], then calls [[ready]] immediately.
    *
    * @return
    */
  def run() = {
    val generator = TaskGenerator(
      name + "Task",
      name,
      duration,
      cost,
      interrupt,
      priority)
    val future = task(generator, resources: _*)
    ready()
    future
  }
}

object TaskSimulatorActor {
  /**
    * Creates props of a [[TaskSimulatorActor]].
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
  def props (
    name: String,
    coordinator: ActorRef,
    resources: Seq[String],
    duration: ValueGenerator[Long],
    cost: ValueGenerator[Long]=new ConstantGenerator(0L),
    interrupt: Int=(-1),
    priority: Task.Priority=Task.Medium
  )(implicit executionContext: ExecutionContext): Props =
    Props(
      new TaskSimulatorActor(
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
