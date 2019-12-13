package com.workflowfm.simulator

import akka.actor._
import akka.event.LoggingReceive
import akka.util.Timeout
import akka.pattern.ask
import com.workflowfm.simulator.events._
import scala.concurrent.duration._
import com.workflowfm.simulator.metrics._
import scala.collection.mutable.{ Map, Queue, PriorityQueue, HashSet, SortedSet }
import scala.concurrent.{ Promise, Await, ExecutionContext }
import scala.util.{ Failure, Success, Try }
import java.util.UUID
import uk.ac.ed.inf.ppapapan.subakka.HashSetPublisher


class Coordinator(
  scheduler :Scheduler,
  startingTime:Long
) extends HashSetPublisher[Event] {
  
  sealed trait CEvent extends Ordered[CEvent] {
    def time:Long
    def compare(that:CEvent) = {
      that.time.compare(time)
    }
  }
  case class FinishingTask(override val time: Long, task: Task) extends CEvent
  case class StartingSim(override val time: Long, simulation: ActorRef) extends CEvent
  
  val resourceMap: Map[String,TaskResource] = Map[String,TaskResource]()
  val waiting: HashSet[ActorRef] = HashSet[ActorRef]()
  val simulations: HashSet[String] = HashSet[String]()
  val tasks: SortedSet[Task] = SortedSet()
  
  val events = new PriorityQueue[CEvent]()
  
  var time = startingTime
 
  
  def addResource(r:TaskResource) = if (!resourceMap.contains(r.name)) {
    publish(EResourceAdd(self, time, r.name, r.costPerTick))
    resourceMap += r.name -> r
  }

  protected def dequeueEvents(t: Long): Seq[CEvent] = {
    val elems = scala.collection.immutable.Seq.newBuilder[CEvent]
    while(events.headOption.exists(_.time == t)) {
      elems += events.dequeue
    }
    elems.result()
  }

  protected def tick(): Unit = {
    // Are events pending?
    if (!events.isEmpty) {
      // Grab the first event
      val firstEvent = events.head

      // Did we somehow go past the event time? This should never happen.
      if (firstEvent.time < time) {
        publish(EError(self, time, s"Unable to handle past event for time: [${firstEvent.time}]"))
      } else {
        // Jump ahead to the event time. This is a priority queue so we shouldn't skip any events
    	time = firstEvent.time

        // Dequeue all the events that need to happen now
        val eventsToHandle = dequeueEvents(firstEvent.time)

        // Release all resources from finished tasks before you notify anyone
        eventsToHandle foreach releaseResources
        // Handle the event
        eventsToHandle foreach handleCEvent
      }

    }
    else if (tasks.isEmpty && simulations.isEmpty) {
      publish(EDone(self, time))

    } else if (waiting.isEmpty && !tasks.isEmpty) { // this may happen if handleCEvent fails
      allocateTasks()
      tick()
    } //else {
      //publish(EError(self, time, "No tasks left to run, but simulations have not finished."))
    //}
  }

  protected def allocateTasks() = {
    // Assign the next tasks
    scheduler.getNextTasks(tasks, time, resourceMap).foreach(startTask)
  }

  protected def releaseResources(event: CEvent) = {
    event match {
      case FinishingTask(t,task) if (t == time) =>
        // Unbind the resources
        task.taskResources(resourceMap).foreach(detach)
      case _ => Unit
    }
  }

  protected def handleCEvent(event:CEvent) = {
    log.debug(s"[COORD:$time] Event!")
    event match {
      // A task is finished
      case FinishingTask(t,task) if (t == time) => stopTask(time,task)

      // A simulation (workflow) is starting now
      case StartingSim(t, sim) if (t == time)=> startSimulationActor(sim)

      case _ => publish(EError(self, time, s"Failed to handle event: $event"))
    }
  }

  protected def addSimulation(t: Long, actor: ActorRef) = {
    publish(ESimAdd(self, time,actor.toString(),t))
    if (t >= time) events += StartingSim(t, actor)
  }

  protected def addSimulations(sims: Seq[(Long,ActorRef)]) = {
    events ++= sims.flatMap { case(t, actor) => {
      publish(ESimAdd(self, time,actor.toString(),t)) }
      if (t >= time)
        Some(StartingSim(t, actor))
      else
        None
    }
  }

  protected def startSimulationActor(simActor: ActorRef) = {
    waiting += simActor
    simActor ! SimulationActor.Start
  }

  protected def startSimulation(name: String, simActor: ActorRef): Unit = {
    publish(ESimStart(self, time,name))
    simulations += name
  }

  protected def stopSimulation(name: String, result: String, actor: ActorRef) = {
    simulations -= name
    publish(ESimEnd(self, time,name,result))
    log.debug(s"[COORD:$time] Finished: [${actor.path.name}]")
    ready(actor)
  }
  
  protected def addTasks(actor: ActorRef, l: Seq[(UUID, TaskGenerator, Seq[String])]) {
    l map { case (i,g,r) => addTask(i,g,r) }
    log.debug(s"[COORD:$time] Ready: [${actor.path.name}]")
    ready(actor)
  }

  protected def addTask(id: UUID, gen: TaskGenerator, resources: Seq[String]) {
    val creation = if (gen.createTime >= 0) gen.createTime else time
    // Create the task
    val t = gen.create(id, creation, sender, resources:_*)
    
    // Calculate the cost of all resource usage. We only know this now!
    val resourceCost = (0L /: t.taskResources(resourceMap)) { case (c,r) => c + r.costPerTick * t.duration }
    t.addCost(resourceCost)

    publish(ETaskAdd(self, time,t))

    if (resources.length > 0)
      tasks += t
    else
      // if the task does not require resources, start it now
      startTask(t)

    //sender() ! Coordinator.AckTask(t) //uncomment this to acknowledge AddTask
  }

  protected def startTask(task:Task) {
    tasks -= task
    publish(ETaskStart(self, time,task))
    // Mark the start of the task in the metrics
    task.taskResources(resourceMap) map { r =>
      // Bind each resource to this task
      r.startTask(task, time) match {
        case None =>
          publish(ETaskAttach(self, time,task,r.name))
        case Some(other) =>
          publish(EError(self, time,s"Tried to attach task [${task.name}](${task.simulation}) to [${r.name}], but it was already attached to [${other.name}](${other.simulation}) "))
      }
    }
    // Generate a FinishTask event to be triggered at the end of the event
    events += FinishingTask(time+task.duration,task)
  }

  protected def waitFor(actor: ActorRef) {
    waiting += actor
    log.debug(s"[COORD:$time] Wait requested: ${actor.path.name}")
    actor ! Coordinator.AckWait
  }

  protected def detach(r: TaskResource) = {
    r.finishTask(time) match {
      case None => Unit
      case Some(task) => publish(ETaskDetach(self, time,task,r.name))
    }
  }

  protected def stopTask(t: Long, task: Task) {
    waiting += task.actor
    log.debug(s"[COORD:$time] Waiting post-task: ${task.actor.path.name}")
    publish(ETaskDone(self, time,task))
    task.actor ! SimulationActor.TaskCompleted(task, time)
  }

  protected def ready(actor: ActorRef): Unit = {
    waiting -= actor
    log.debug(s"[COORD:$time] Waiting: ${waiting map (_.path.name)}")
    // Are all actors ready?
    if (waiting.isEmpty) {
      allocateTasks()
      tick()
    }
  }

  def start() = {
    publish(EStart(self))
    tick()
  }

  override def isFinalEvent(e: Event) = e match {
    case EDone(_,_) => true
    case _ => false
  }

  def receiveBehaviour: Receive = {
    case Coordinator.AddSim(t, s) => addSimulation(t,s)
    case Coordinator.AddSims(l) => addSimulations(l)
    case Coordinator.AddSimNow(s) => addSimulation(time, s)
    case Coordinator.AddSimsNow(l) => addSimulations(l.map((time,_)))

    case Coordinator.AddResource(r) => addResource(r)
    case Coordinator.AddResources(r) => r foreach addResource
      
    case Coordinator.AddTasks(l) => addTasks(sender, l)
    case Coordinator.WaitFor(actor) => waitFor(actor)
    case Coordinator.SimStarted(name) => startSimulation(name, sender)
    case Coordinator.SimDone(name, result) => result match {
      case Success(res) => {
        stopSimulation(name, res.toString, sender)
      }
      case Failure(ex) => {
        stopSimulation(name, ex.getLocalizedMessage, sender)
        ex.printStackTrace()
      }
    }
    case Coordinator.Start => start()
    case Coordinator.Ping => sender() ! Coordinator.Time(time)
  }

  override def receive = LoggingReceive { publisherBehaviour orElse receiveBehaviour }
}

object Coordinator {
  case object Start
  case object Ping
  case class Time(time: Long)

  case class AddSim(t: Long, actor: ActorRef)
  case class AddSims(l: Seq[(Long,ActorRef)])
  case class AddSimNow(actor: ActorRef)
  case class AddSimsNow(l: Seq[ActorRef])

  case class AddResource(r: TaskResource)
  case class AddResources(l: Seq[TaskResource])
  
  case class SimStarted(name: String)
  case class SimDone(name: String, result: Try[Any])

  case class AddTasks(l: Seq[(UUID, TaskGenerator, Seq[String])])
  case object AckTask
  case class WaitFor(actor: ActorRef)
  case object AckWait

  def props(
    scheduler: Scheduler,
    startingTime: Long = 0L
  )(implicit system: ActorSystem, executionContext:ExecutionContext
  ): Props = Props(new Coordinator(scheduler,startingTime))
}
