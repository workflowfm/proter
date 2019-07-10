package com.workflowfm.simulator

import akka.actor.{ Actor, ActorRef, Props }
import com.workflowfm.simulator.metrics.TaskMetrics
import scala.collection.mutable.{ Map, Queue }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import java.util.UUID

trait SimulationActor extends Actor {
  implicit val executionContext: ExecutionContext

  def simulationName: String
  val coordinator: ActorRef

  private val tasks: Map[UUID,Promise[TaskMetrics]] = Map()
  private val queue: Queue[(UUID, TaskGenerator, Seq[String])] = Queue()

  def run(): Future[Any]


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
    coordinator ! Coordinator.AddTasks(simulationName, seq)
  }


  private def start(): Unit = {
    run().onComplete { x =>
      coordinator ! Coordinator.SimDone(simulationName, x)
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
   def simulationName: String
   def actor: SimulationActor

   def simulate[T](
     gen: TaskGenerator,
     result:T,
     resources:String*
   )(implicit executionContext: ExecutionContext = ExecutionContext.global):Future[T] = {
     actor.task(gen, resources:_*).map(_ => result)
   }
}


class TaskSimulatorActor(
  override val simulationName:String,
  override val coordinator:ActorRef,
  resources:Seq[String],
  duration:ValueGenerator[Long],
  val cost:ValueGenerator[Long]=new ConstantGenerator(0L),
  interrupt:Int=(-1),
  priority:Task.Priority=Task.Medium
) extends SimulationActor {
  def run() = {
    val generator = TaskGenerator(
      simulationName + "Task",
      simulationName,
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
    simulationName:String,
    coordinator:ActorRef,
    resources:Seq[String],
    duration:ValueGenerator[Long],
    cost:ValueGenerator[Long]=new ConstantGenerator(0L),
    interrupt:Int=(-1),
    priority:Task.Priority=Task.Medium
  ): Props =
    Props(
      new TaskSimulatorActor(
        simulationName,
        coordinator,
        resources,
        duration,
        cost,
        interrupt,
        priority
      )
  )
}
