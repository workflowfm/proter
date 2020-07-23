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
    * @return A `Future` that completes with a custom output when the simulation is completed.
    */
  def run(): Future[Any]

  /**
    * A map of [[Task]] IDs that have been sent to the [[Coordinator]] and the `Promise`s that
    * need to be fulfilled when the tasks complete.
    */
  private val tasks: Map[UUID,Promise[(Task,Long)]] = Map()

  /**
    * A queue of [[TaskGenerator]]s due to be sent to the [[Coordinator]].
    * 
    * Each entry contains a task ID, a [[TaskGenerator]], and a list of [[TaskResource]] names
    * that need to be used by the generated [[Task]].
    */
  private val queue: Queue[(UUID, TaskGenerator, Seq[String])] = Queue()

  /**
    * Declare a new [[TaskGenerator]] that needs to be sent to the [[Coordinator]] for simulation.
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
    queue += ((id, t, resources))
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
    */
  def ready(): Unit = {
    val seq = queue.clone().toSeq
    queue.clear()
    coordinator ! Coordinator.AddTasks(seq)
  }

  def requestWait(ack: ActorRef): Unit = {
    (coordinator ? Coordinator.WaitFor(self))(Timeout(1, TimeUnit.DAYS)) pipeTo ack
  }

  def simulationActorReceive: Receive = {
    case SimulationActor.Start => start()
    case SimulationActor.Ready => ready()
    case SimulationActor.TaskCompleted(task, time) => complete(task, time)
    case SimulationActor.AddTask(id, t, r) => task(id, t, Some(sender), r)
  }

  def receive = simulationActorReceive
}


object SimulationActor {
  case object Start
  case object Ready
  case class AddTask(id: UUID, t: TaskGenerator, resources: Seq[String])
  case class TaskCompleted(task: Task, time: Long)
}


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
