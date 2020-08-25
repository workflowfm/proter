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
  *      can interact with `Simulation` using the [[Simulation.AddTask]],
  *      [[Simulation.RequestWait]] and [[Simulation.Ready]] messages.
  *
  * = Interaction Flow =
  * The interaction flow with the [[Coordinator]] is expected as follows:
  *   1. Receiving a [[Simulation.Start]] message starts the simulation. This is confirmed
  *      via a [[Coordinator.SimStarted]] response.
  *   1. The simulation logic starts executing via [[Simulation.run]] and produces some
  *      simulation ``tasks``.
  *   1. When the simulation logic finishes producing tasks, it is ``ready``. Sending a
  *      [[Coordinator.AddTasks]] message informs the [[Coordinator]] about any new
  *      tasks and that the simulation is ready to proceed.
  *   1. Eventually, one of the tasks completes and we receive a [[Simulation.TaskCompleted]]
  *      message. The simulation logic resumes execution.
  *   1. The [[Coordinator]] is now waiting for either new tasks ([[Coordinator.AddTasks]]) or
  *      the simulation to end ([[Coordinator.SimDone]]):
  *      a. The simulation may generate new ``tasks`` and then become ``ready`` again as above.
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
    coordinator: ActorRef
)(implicit executionContext: ExecutionContext)
    extends Actor {
    
  type Prereq = Seq[UUID] => Boolean


  /**
    * Initiates the execution of the simulation.
    *
    * @group api
    * @return A `Future` that completes with a custom output when the simulation is completed.
    */
  def run(): Future[Any]

  def complete(task: Task, time: Long): Unit

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
  def task(t: TaskGenerator, resources: String*): Unit = {
    val id = java.util.UUID.randomUUID
    task(id, t, resources)
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
  protected def task(id: UUID, t: TaskGenerator, resources: Seq[String]): Unit = {
    coordinator ! Coordinator.AddTask(id, t, resources)
  }

  protected def task(id: UUID, t: TaskGenerator, resources: Seq[String], time: Long, prerequisites: Prereq = (_)=>true): Unit = {
    coordinator ! Coordinator.AddTaskInFuture(id, t, resources, time, prerequisites)
  }

  protected def task(id: UUID, t: TaskGenerator, resources: Seq[String], time: Long, prerequisites: Seq[UUID] ): Unit = {
    val function: Seq[UUID]=>Boolean = (x:Seq[UUID])=> prerequisites forall (x.contains(_))
    coordinator ! Coordinator.AddTaskInFuture(id, t, resources, time, function)
  }

  protected def removeFutureTask(id: UUID) {
    coordinator ! Coordinator.RemoveTaskInFuture(id)
  }

  protected def editFutureTask(id: UUID, time: Long, prerequisites: Prereq) {
    coordinator ! Coordinator.EditFutureTask(id, time, prerequisites)
  }

  protected def tasksAfterThis(task: UUID, time: Long, sender: ActorRef, official: Boolean): Seq[Task] = Seq()
  protected def lookaheadNextItter(sender: ActorRef) = Unit

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
    case Simulation.TasksAfterThis(task,time, official) => tasksAfterThis(task,time,sender(), official)
    case Simulation.LookaheadNextItter => lookaheadNextItter(sender)
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
    * @see [[Simulation.task(i* task(id, t, resources)]]
    * @group process
    *
    * @param id The [[Task]] ID to be used.
    * @param t The [[TaskGenerator]] to generate the [[Task]].
    * @param resources The names of the [[TaskResource]]s the [[Task]] will require.
    */
  case class AddTaskWithId(id: UUID, t: TaskGenerator, resources: Seq[String])

  case class AddTask(t: TaskGenerator, resources: Seq[String])

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

  case class TasksAfterThis(id: UUID, endTime: Long, official: Boolean = true)
  case object LookaheadNextItter
  
  /**
    * Tells the [[Simulation]] to request that [[Coordinator]] waits.
    *
    * @see [[Simulation.requestWait(a* requestWait(ActorRef)]]
    * @group process
    */
  case object Wait
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
    val generator = TaskGenerator(name + "Task", name, duration, cost, interrupt, priority)
    task(generator, resources: _*)
    ready()
    promise.future
  }

  override def complete(task: Task, time: Long) = promise.success((task, time))
}

object SingleTaskSimulation {

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

abstract class AsyncSimulation(
    name: String,
    coordinator: ActorRef
)(implicit executionContext: ExecutionContext)
    extends Simulation(name, coordinator)(executionContext) {

  type Callback = (Task, Long) => Unit

  /**
    * A map of [[Task]] IDs that have been sent to the [[Coordinator]] and the `Promise`s that
    * need to be fulfilled when the tasks complete.
    *
    * @group internal
    */
  private val tasks: Map[UUID, Callback] = Map()

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
  def task(t: TaskGenerator, callback: Callback, resources: String*): Unit = {
    val id = java.util.UUID.randomUUID
    task(id, t, callback, resources, None, Seq())
  }

  def task(t: TaskGenerator, callback: Callback, time: Long, prerequisites: Seq[UUID], resources: String*): Unit = {
    val id = java.util.UUID.randomUUID
    task(id, t, callback, resources, Some(time), prerequisites)
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
  protected def task(
      id: UUID,
      t: TaskGenerator,
      callback: Callback,
      resources: Seq[String],
      time: Option[Long],
      prerequisites: Seq[UUID]
  ): Unit = {
    tasks += id -> callback
    time match {
      case None => super.task(id, t, resources)
      case Some(value) =>  super.task(id, t, resources, value, prerequisites)
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
  override def complete(task: Task, time: Long) = {
    tasks.get(task.id).map(_(task, time))
    tasks -= task.id
  }

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
    case Simulation.AddTaskWithId(id, t, r) => task(id, t, actorCallback(sender), r, None, Seq())
    case Simulation.AddTask(t, r) => task(t, actorCallback(sender), r: _*)
    case Simulation.Wait => coordinator.forward(Coordinator.WaitFor(self))
    case Simulation.TasksAfterThis(task,time, official) => tasksAfterThis(task,time,sender(), official)
    case Simulation.LookaheadNextItter => lookaheadNextItter(sender)
  }
}

trait FutureTasks { self: AsyncSimulation =>

  def futureTask(t: TaskGenerator, resources: String*): Future[(Task, Long)] = {
    val id = java.util.UUID.randomUUID
    futureTask(id, t, resources, None, Seq())
  }

  def futureTask(id: UUID, t: TaskGenerator, resources: String*): Future[(Task, Long)] = {
    futureTask(id, t, resources, None, Seq())
  }

  def futureTask(id: UUID, t: TaskGenerator, resources: Seq[String], time: Option[Long]=None, prerequisites: Seq[UUID]): Future[(Task, Long)] = {
    val p = Promise[(Task, Long)]()
    def call: Callback = (task, time) => p.success(task, time)
    task(id, t, call, resources, None, Seq())
    p.future
  }
}

trait Lookahead extends Simulation {
  type LookaheadFunctions = mutable.Set[( Seq[(UUID,Long)]=>Long, List[(UUID, TaskGenerator, Seq[String])] ) ]

  protected val lookaheadFunctions:LookaheadFunctions = mutable.Set()
  protected val completed = mutable.Set[(UUID,Long)]()
  protected val completedThisItter = mutable.Set[(UUID,Long)]()
  protected val lookaheadThisItter:LookaheadFunctions = mutable.Set()

  override protected def tasksAfterThis(task: UUID, time: Long, sender: ActorRef, official: Boolean): Seq[Task] = {
    completedThisItter += ((task, time))
    val tasks = getTasks(if (official) lookaheadThisItter else lookaheadFunctions, official)
    sender ! tasks
    //Thread.sleep(2000)
    tasks //this return is not used?
  }

  private def getTasks(functions: LookaheadFunctions, official: Boolean): Seq[Task] = {
    val taskData: List[(UUID, TaskGenerator, Seq[String], Long)] = ( 
      functions flatMap { 
        case(function, data) => 
        val l = function(Seq() ++ completed ++ completedThisItter )
        if (l>=0) { 
          if (official) lookaheadThisItter.-=((function, data))
          data foreach (d=> println("sim getTasks: " + d._2.name))
          (data map (d => (d._1, d._2, d._3, l))) 
        }
        else List()
      } ).toList
    
    Seq[Task]() ++ ( taskData map (x => x._2.create(x._1,x._4,self,x._3:_*)) )
  }

  abstract override def complete(task: Task, time: Long) = {
    completed += ((task.id, time))
    removeIdSource(task.id)
    super.complete(task,time)
  }

  override protected def lookaheadNextItter(sender: ActorRef) = {
    completedThisItter.clear()
    lookaheadThisItter.clear()
    lookaheadThisItter ++= lookaheadFunctions
    sender ! Unit
    Unit
  }

  protected def add1To1Lookahead(sourceID: UUID, resultID: UUID, generator: TaskGenerator, resources: Seq[String]) {
    lookaheadFunctions += ( ( (s:Seq[(UUID,Long)]) => (s collect { case (id,time) if id==sourceID => time }).headOption match {
      case Some(value) => value
      case None => -1L
    } ), List((resultID, generator, resources)) ) 
  } //could express this in terms of `addManyTo1Lookahead()`

  protected def add1ToManyLookahead(sourceID: UUID, data: List[(UUID, TaskGenerator, Seq[String])]) {
    data foreach ( d => add1To1Lookahead( sourceID, d._1, d._2, d._3 ) )
  }

  protected def addManyTo1Lookahead(function: Seq[(UUID,Long)]=>Long, resultID: UUID, generator: TaskGenerator, resources: Seq[String]) {
    lookaheadFunctions += ( (function, List((resultID, generator, resources)) ) )
  }

  protected def removeIdSource(id: UUID) {
    //remove all entries that spawn this task
    lookaheadFunctions retain { entry => entry._2 forall ( data => data._1 != id) }
    //remove all entries that are spawned by this task (among others)
    lookaheadFunctions retain { entry => entry._1(Seq()++completed)<0}
  }
}
