package com.workflowfm.simulator

import akka.actor.{ Actor, ActorRef, Props }
import com.workflowfm.simulator.metrics.TaskMetrics
import scala.collection.mutable.{ Map, Queue }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import java.util.UUID

abstract class SimulationActor (
  val name: String,
  val coordinator: ActorRef)
  (implicit executionContext: ExecutionContext)
    extends Actor {
  //  implicit val executionContext: ExecutionContext

  def run(): Future[Any]

  private val tasks: Map[UUID,Promise[TaskMetrics]] = Map()
  private val queue: Queue[(UUID, TaskGenerator, Seq[String])] = Queue()

  def task(t: TaskGenerator, resources: String*): Future[TaskMetrics] = {
    val id = java.util.UUID.randomUUID
    val p = Promise[TaskMetrics]()
    tasks += id -> p
    queue += ((id, t, resources))
    p.future
  }

  private def complete(id: UUID, metrics: TaskMetrics) = {
    tasks.get(id).map (_.success(metrics))
    tasks -= id
  }

  private def start(): Unit = {
    coordinator ! Coordinator.SimStarted(name)
    run().onComplete { x =>
      coordinator ! Coordinator.SimDone(name, x)
    }
  }

  def ready(): Unit = {
    val seq = queue.clone().toSeq
    queue.clear()
    coordinator ! Coordinator.AddTasks(seq)
  }

  def receive = {
    case SimulationActor.Start => start()
    case SimulationActor.Ready => ready()
    case SimulationActor.TaskCompleted(id, metrics) => complete(id, metrics)
    case SimulationActor.AddTask(t, r) => task(t, r)
  }
}

object SimulationActor {
  case object Start
  case object Ready
  case class AddTask(t: TaskGenerator, resources: String*)
  case class TaskCompleted(id: UUID, metrics: TaskMetrics)
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
