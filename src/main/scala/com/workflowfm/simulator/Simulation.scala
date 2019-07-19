package com.workflowfm.simulator

import akka.actor.{ Actor, ActorRef, Props }
import com.workflowfm.simulator.metrics.TaskMetrics
import scala.collection.mutable.{ Map, Queue }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import java.util.UUID

abstract class SimulationActor(name: String, coordinator: ActorRef) extends Actor {
  implicit val executionContext: ExecutionContext

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

  def ready(): Unit = {
    val seq = queue.clone().toSeq
    queue.clear()
    coordinator ! Coordinator.AddTasks(seq)
  }

  private def start(): Unit = {
    coordinator ! Coordinator.SimStarted(name)
    run().onComplete { x =>
      coordinator ! Coordinator.SimDone(name, x)
    }
  }

  private def complete(id: UUID, metrics: TaskMetrics) = {
    tasks.get(id).map (_.success(metrics))
    tasks -= id
  }

  def receive = {
    case SimulationActor.Start => start()
    case SimulationActor.TaskCompleted(id, metrics) => complete(id, metrics)
  }
}

object SimulationActor {
  case object Start
  case class TaskCompleted(id: UUID, metrics: TaskMetrics)


}


trait SimulatedProcess {
   def name: String
   def actor: SimulationActor

   def simulate[T](
     gen: TaskGenerator,
     result:TaskMetrics => T,
     resources:String*
   )(implicit executionContext: ExecutionContext):Future[T] = {
     actor.task(gen, resources:_*).map(m => result(m))
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
)(override implicit val executionContext: ExecutionContext) extends SimulationActor(name, coordinator) {
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
