package com.workflowfm.simulator

import akka.pattern.{ ask, pipe }
import akka.actor.{ Actor, ActorRef, Props }
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import scala.concurrent.duration._
import scala.collection.mutable.{ Map, Queue }
import scala.concurrent.{ ExecutionContext, Future, Promise, Await }
import java.util.UUID

abstract class SimulationActor (
  val name: String,
  val coordinator: ActorRef)
  (implicit executionContext: ExecutionContext)
    extends Actor {

  def run(): Future[Any]

  private val tasks: Map[UUID,Promise[(Task,Long)]] = Map()
  private val queue: Queue[(UUID, TaskGenerator, Seq[String])] = Queue()
  implicit val timeout = Timeout(2.seconds)
  var waitCount: Int = 0
  var moreTasks: Boolean = false

  def task(t: TaskGenerator, resources: String*): Future[(Task,Long)] = {
    val id = java.util.UUID.randomUUID
    println("adding a task")
    task(id, t, None, resources)
  }

  protected def task(id: UUID, t: TaskGenerator, caller: Option[ActorRef], resources: Seq[String]): Future[(Task,Long)] = {
    val p = Promise[(Task,Long)]()
    tasks += id -> p
    queue += ((id, t, resources))
    caller match {
      case None => p.future
      case Some(actor) => p.future pipeTo actor
    }
  }

  def changeWaitCount(i:Int,sender:ActorRef) {
    waitCount += i
    println(waitCount + " changeWaitCount")
  }

  def noMoreTasks() {
    moreTasks = false
    println("coordinator done")
    if (waitCount==0) ready()
  }

  protected def complete(task: Task, time: Long) = {
    
    waitCount += 1
    moreTasks = true
    println(waitCount+ " (complete)")
    tasks.get(task.id).map (_.success((task,time)))
    tasks -= task.id
  }

  protected def start(): Unit = {
    coordinator ! Coordinator.SimStarted(name)
    run().onComplete { x =>
      coordinator ! Coordinator.SimDone(name, x)
    }
  }

  def ready(): Unit = {
    println(waitCount + " (ready)")
    println("moreTasks: "+moreTasks)
    if (waitCount==0 && !moreTasks) {
      println("calling coordinator")
      val seq = queue.clone().toSeq
      queue.clear()
      coordinator ! Coordinator.AddTasks(seq)
    }
  }

  def ack(ack:Seq[UUID]): Future[Any] = {
    //waitCount -= ack.size
    self ! SimulationActor.ChangeWaitCount(-(ack.size))
    coordinator ? Coordinator.AckTasks(ack)
  }

  def waitForAck(ack:UUID): Unit = {
    //waitCount += 1
    self ! SimulationActor.ChangeWaitCount(1)
    coordinator ! Coordinator.WaitForAck(ack)
  }

  def requestWait(ack: ActorRef): Unit = {
    (coordinator ? Coordinator.WaitFor(self))(Timeout(1, TimeUnit.DAYS)) pipeTo ack
  }

  def simulationActorReceive: Receive = {
    case SimulationActor.Start => start()
    case SimulationActor.Ready => ready()
    case SimulationActor.TaskCompleted(task, time) => complete(task, time)
    case SimulationActor.AddTask(id, t, r) => task(id, t, Some(sender), r)
    case SimulationActor.CoordinatorDone => noMoreTasks()
    case SimulationActor.ChangeWaitCount(i) => changeWaitCount(i,sender)
    case SimulationActor.Foo(s) => println(s)
  }

  def receive = simulationActorReceive
}


object SimulationActor {
  case object Start
  case object Ready
  case class AddTask(id: UUID, t: TaskGenerator, resources: Seq[String])
  case class TaskCompleted(task: Task, time: Long)
  case object CoordinatorDone
  case class ChangeWaitCount(i:Int)
  case class Foo(s:String)
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
