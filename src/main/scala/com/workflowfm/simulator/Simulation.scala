package com.workflowfm.simulator

import akka.pattern.{ pipe, ask }
import akka.actor.{ Actor, ActorRef, Props }
import akka.util.Timeout
import com.workflowfm.simulator.metrics.TaskMetrics
import java.util.concurrent.TimeUnit
import scala.collection.mutable.{ Map, Queue }
import scala.concurrent.duration.Duration
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
    task(id, t, None, resources)
  }

  protected def task(id: UUID, t: TaskGenerator, caller: Option[ActorRef], resources: Seq[String]): Future[TaskMetrics] = {
    val p = Promise[TaskMetrics]()
    tasks += id -> p
    queue += ((id, t, resources))
    println(s"QQ: $queue")
    caller match {
      case None => p.future
      case Some(actor) => p.future pipeTo actor
    }
  }

  protected def complete(id: UUID, metrics: TaskMetrics) = {
    tasks.get(id).map (_.success(metrics))
    tasks -= id
  }

  protected def start(): Unit = {
    coordinator ! Coordinator.SimStarted(name)
    run().onComplete { x =>
      coordinator ! Coordinator.SimDone(name, x)
    }
  }

  def ready(): Unit = {
    val seq = queue.clone().toSeq
    println(s"Q SEND: $seq")
    queue.clear()
    coordinator ! Coordinator.AddTasks(seq)
  }

  def simulationActorReceive: Receive = {
    case SimulationActor.Start => start()
    case SimulationActor.Ready => ready()
    case SimulationActor.TaskCompleted(id, metrics) => complete(id, metrics)
    case SimulationActor.AddTask(id, t, r) => task(id, t, Some(sender), r)
  }

  def receive = simulationActorReceive
}


object SimulationActor {
  case object Start
  case object Ready
  case class AddTask(id: UUID, t: TaskGenerator, resources: Seq[String])
  case class TaskCompleted(id: UUID, metrics: TaskMetrics)
}


trait SimulatedProcess {
  def simulationName: String
  def simulationActor: ActorRef

  def simulate[T](
    gen: TaskGenerator,
    result:TaskMetrics => T,
    resources:String*
  )(implicit executionContext: ExecutionContext):Future[T] = {
    val id = java.util.UUID.randomUUID
    simulate(id,gen,result,resources:_*)
  }

  def simulate[T](
    id: UUID,
    gen: TaskGenerator,
    result:TaskMetrics => T,
    resources:String*
  )(implicit executionContext: ExecutionContext):Future[T] = {
    (simulationActor ? SimulationActor.AddTask(id, gen, resources))(Timeout(1, TimeUnit.DAYS)).
      mapTo[TaskMetrics].
      map (result(_))
  }
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
