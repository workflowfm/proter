package com.workflowfm.simulator

import akka.actor._
import akka.util.Timeout
import akka.pattern.ask
import com.workflowfm.simulator.events._
import scala.concurrent.duration._
import com.workflowfm.simulator.metrics._
import scala.collection.mutable.{ Map, Queue, PriorityQueue, HashSet, SortedSet }
import scala.concurrent.{ Promise, Await, ExecutionContext }
import scala.util.{ Failure, Success, Try }
import java.util.UUID


class Coordinator(
  scheduler :Scheduler,
  startingTime:Long
)(
  override implicit val system: ActorSystem,
  implicit val executionContext:ExecutionContext
) extends PublisherActor {
  
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
  
  var starter: Option[ActorRef] = None
  var time = startingTime
  
  val metrics = new SimMetricsAggregator()
  
  //implicit val timeout = Timeout(timeoutMillis.millis)
  
  def addResource(r:TaskResource) = if (!resourceMap.contains(r.name)) {
    publish(EResourceAdd(self, time, r.name, r.costPerTick))
    resourceMap += r.name -> r
    metrics.addResource(r.name, r.costPerTick)
  }

  protected def handleCEvent(event:CEvent) = event match {
    //println(s"[$time] ========= CEvent! ========= ")
    // A task is finished
    case FinishingTask(t,task) if (t == time) => stopTask(time,task)

    // A simulation (workflow) is starting now
    case StartingSim(t, sim) if (t == time)=> startSimulationActor(sim)

    case _ => publish(EError(self, time, s"Failed to handle event: $event"))
  }

  protected def addSimulation(t: Long, actor: ActorRef) = {
    publish(ESimAdd(self, time,actor.toString(),t))
    if (t >= time) events += StartingSim(t, actor)
  }

  protected def startSimulationActor(simActor: ActorRef) = {
    waiting += simActor
    simActor ! SimulationActor.Start
  }

  protected def startSimulation(name: String, simActor: ActorRef): Unit = {
    publish(ESimStart(self, time,name))
    metrics.addSim(name,time)
    simulations += name
  }

  protected def stopSimulation(name: String, result: String, actor: ActorRef) = {
    metrics.simulation(name) (_.done(result,time))
    simulations -= name
    publish(ESimEnd(self, time,name,result))
    ready(actor)
  }
  
  //  protected def updateSimulation(s:String, f:WorkflowMetricTracker=>WorkflowMetricTracker) = simulations = simulations map {
  //    case (n,m,e) if n.equals(s) => (n,f(m),e)
  //    case x => x
  //  }
  
  protected def resourceIdle(r:TaskResource) = if (r.isIdle) {
    metrics.resource(r.name)(_.idle(time))
  }

  protected def addTasks(actor: ActorRef, l: Seq[(UUID, TaskGenerator, Seq[String])]) {
    l map { case (i,g,r) => addTask(i,g,r) }
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
    metrics.addTask(t)

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
    metrics.task(task)(_.start(time))
    task.taskResources(resourceMap) map { r =>
      // Update idle time if resource has been idle
      if (r.isIdle) metrics.resource(r.name)(_.idle(time))
      // Bind each resource to this task
      r.startTask(task, time) match {
        case None =>
          publish(ETaskAttach(self, time,task,r.name))
        case Some(other) =>
          publish(EError(self, time,s"Tried to attach task [${task.name}](${task.simulation}) to [${r.name}], but it was already attached to [${other.name}](${other.simulation}) "))
      }
      // Add the task and resource cost to the resource metrics
      metrics.resource(r.name)(_.task(time, task))
    }
    // Add the task to the simulation metrics
    metrics.simulation(task.simulation)(_.task(task).addDelay(time - task.created))
    // Generate a FinishTask event to be triggered at the end of the event
    events += FinishingTask(time+task.duration,task)
  }

  protected def detach(r: TaskResource) = {
    r.finishTask(time) match {
      case None => Unit
      case Some(task) => publish(ETaskDetach(self, time,task,r.name))
    }
  }

  protected def stopTask(t: Long, task: Task) {
    // Unbind the resources
    task.taskResources(resourceMap).foreach(detach)

    val resultMetrics = metrics.taskMap.getOrElse(task.id, TaskMetrics(task).start(time - task.duration))

    waiting += task.actor
    publish(ETaskDone(self, time,task))
    task.actor ! SimulationActor.TaskCompleted(task.id, resultMetrics)
  }

  //  /**
  //    * Runs all tasks that require no resources
  //    */
  //  protected def runNoResourceTasks(): Int = tasks.dequeueAll(_.resources.isEmpty).map(startTask).length

  protected def ready(actor: ActorRef): Unit = {
    waiting -= actor
    // Are all actors ready?
    if (waiting.isEmpty) {
      allocateTasks()
      tick()
    }
  }

  def start(a:ActorRef) = if (starter.isEmpty) {
    starter = Some(a)
    publish(EStart(self))
    metrics.started
    tick()
  }

  protected def tick(): Unit = {
    //println("***TICK!***")

    // Update idle resources
    resourceMap.values.foreach(resourceIdle)
    // Are events pending?
    if (!events.isEmpty) {
      // Grab the first event
      val event = events.dequeue()
      // Did we somehow go past the event time? This should never happen.
      if (event.time < time) {
        publish(EError(self, time, s"Unable to handle past event for time: [${event.time}]"))
      } else {
        // Jump ahead to the event time. This is a priority queue so we shouldn't skip any events
    	time = event.time
        handleCEvent(event)

        // Handle all events that are supposed to happen now, so that we free resources for the Scheduler
        while (events.headOption.map(_.time == time).getOrElse(false))
          handleCEvent(events.dequeue)
      }
    }
    else if (tasks.isEmpty && simulations.isEmpty) {
      publish(EDone(self, time))
      metrics.ended
      // Tell whoever started us that we are done
      starter map { a => a ! Coordinator.Done(time,metrics) }

    } else if (waiting.isEmpty && !tasks.isEmpty) { // this may happen if handleCEvent fails
      allocateTasks()
      tick()
    } else {
      publish(EError(self, time, "No tasks left to run, but simulations have not finished."))
    }
  }

  protected def allocateTasks() = {
    // Assign the next tasks
    scheduler.getNextTasks(tasks, time, resourceMap).foreach(startTask)
  }

  override def receiveBehaviour = {
    case Coordinator.AddSim(t, s) => addSimulation(t,s)
    case Coordinator.AddSims(l) => l map { case (t,s) => addSimulation(t,s) }
    case Coordinator.AddSimNow(s) => addSimulation(time, s)
    case Coordinator.AddSimsNow(l) => l map { s => addSimulation(time,s) }

    case Coordinator.AddResource(r) => addResource(r)
    case Coordinator.AddResources(r) => r foreach addResource
      
    case Coordinator.AddTasks(l) => addTasks(sender, l)
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
    case Coordinator.Start => start(sender)
    case Coordinator.Ping => sender() ! Coordinator.Time(time)

  }
}

object Coordinator {
  case object Start
  //TODO case object Stop
  case class Done(time: Long, metrics: SimMetricsAggregator)
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

  //case class AddTask(id: UUID, t: TaskGenerator, resources: Seq[String])
  case class AddTasks(l: Seq[(UUID, TaskGenerator, Seq[String])])
  case object AckTask

  def props(
    scheduler: Scheduler,
    startingTime: Long = 0L
  )(implicit system: ActorSystem, executionContext:ExecutionContext
  ): Props = Props(new Coordinator(scheduler,startingTime)(system,executionContext))
}
