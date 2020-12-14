package com.workflowfm.proter.metrics

import java.util.UUID

import scala.collection.mutable

import com.workflowfm.proter._
import com.workflowfm.proter.events._

/**
  * Collects/aggregates metrics across multiple tasks, resources, and simulations.
  *
  * @groupprio Values 1
  * @groupprio Start/End 2
  * @groupprio Set 3
  * @groupprio Update 4
  * @groupprio Get 5
  */
class SimMetricsAggregator {import scala.collection.immutable.Map

  /**
    * The '''real''' (system) time that measurement started, or [[scala.None]] if it has not started yet.
    * @group Start/End
    */
  var start: Option[Long] = None
  /**
    * The '''real''' (system) time that measurement finished, or [[scala.None]] if it has not finished yet.
    * @group Start/End
    */
  var end: Option[Long] = None

  /**
    * Marks the start of metrics measurement with the current system time.
    * @group Start/End
    */
  def started: Unit = start match {
    case None => start = Some(System.currentTimeMillis())
    case _ => ()
  }

  /**
    * Marks the end of metrics measurement with the current system time.
    * @group Start/End
    */
  def ended: Unit = end = Some(System.currentTimeMillis())

  /**
    * Task metrics indexed by task ID.
    * @group Values
    */
  val taskMap: mutable.Map[UUID, TaskMetrics] = scala.collection.mutable.Map[UUID, TaskMetrics]()

  /**
    * Simulation metrics indexed by name.
    * @group Values
    */
  val simMap: mutable.Map[String, SimulationMetrics] =
    scala.collection.mutable.Map[String, SimulationMetrics]()

  /**
    * Resource metrics indexed by name.
    * @group Values
    */
  val resourceMap: mutable.Map[String, ResourceMetrics] =
    scala.collection.mutable.Map[String, ResourceMetrics]()

  // Set

  /**
    * Adds a new [[TaskMetrics]] instance, taking care of indexing automatically
    * Overwrites a previous instance with the same IDs
    * @group Set
    */
  def +=(m: TaskMetrics): TaskMetrics = { taskMap += (m.id -> m); m }
  /**
    * Adds a new [[SimulationMetrics]] instance, taking care of indexing automatically
    * Overwrites a previous instance with the same IDs
    * @group Set
    */
  def +=(m: SimulationMetrics): SimulationMetrics = { simMap += (m.name -> m); m }
  /**
    * Adds a new [[ResourceMetrics]] instance, taking care of indexing automatically
    * Overwrites a previous instance with the same IDs
    * @group Set
    */
  def +=(m: ResourceMetrics): ResourceMetrics = { resourceMap += (m.name -> m); m }

  /**
    * Initializes and adds a new [[TaskMetrics]] instance given a new [[Task]].
    * @group Set
    */
  def addTask(task: Task): TaskMetrics = this += TaskMetrics(task)
  /**
    * Initializes and adds a new [[SimulationMetrics]] instance given the name of the simulation starting now
    * and the current virtual time.
    * @group Set
    */
  def addSim(s: String, t: Long): SimulationMetrics = this += SimulationMetrics(s, t)

  /**
    * Initializes and adds a new [[ResourceMetrics]] instance given a new [[TaskResource]].
    * @group Set
    */
  def addResource(r: String, costPerTick: Int): ResourceMetrics =
    this += ResourceMetrics(r, costPerTick)

  // Update
  /** Updates a [[TaskMetrics]] instance.
    *
    * @return the updated [[TaskMetrics]] or [[scala.None]] if the identified instance does not exist
    *
    * @param taskID the task ID for the involved [[Task]]
    * @param u a function to update the [[TaskMetrics]] instance
    *
    * @see [[com.workflowfm.pew.metrics.MetricsAggregator]] for examples in a similar context
    * @group Update
    */
  def task(taskID: UUID)(u: TaskMetrics => TaskMetrics): Option[TaskMetrics] =
    taskMap.get(taskID).map { m => this += u(m) }

  /** Updates a [[TaskMetrics]] instance.
    *
    * @return the updated [[TaskMetrics]] or [[scala.None]] if the identified instance does not exist
    *
    * @param task the involved [[Task]]
    * @param u a function to update the [[TaskMetrics]] instance
    *
    * @see [[com.workflowfm.pew.metrics.MetricsAggregator]] for examples in a similar context
    *
    * @group Update
    */
  def task(task: Task)(u: TaskMetrics => TaskMetrics): Option[TaskMetrics] =
    taskMap.get(task.id).map { m => this += u(m) }

  /** Updates a [[SimulationMetrics]] instance.
    *
    * @return the updated [[SimulationMetrics]] or [[scala.None]] if the identified instance does not exist
    *
    * @param simulation the name of the involved simulation
    * @param u a function to update the [[SimulationMetrics]] instance
    *
    * @see [[com.workflowfm.pew.metrics.MetricsAggregator]] for examples in a similar context
    *
    * @group Update
    */
  def simulation(
      simulation: String
  )(u: SimulationMetrics => SimulationMetrics): Option[SimulationMetrics] =
    simMap.get(simulation).map { m => this += u(m) }

  /** Updates a [[ResourceMetrics]] instance.
    *
    * @return the updated [[ResourceMetrics]] or [[scala.None]] if the identified instance does not exist
    *
    * @param resource the involved [[TaskResource]]
    * @param u a function to update the [[ResourceMetrics]] instance
    *
    * @see [[com.workflowfm.pew.metrics.MetricsAggregator]] for examples in a similar context
    *
    * @group Update
    */
  def resource(resource: String)(u: ResourceMetrics => ResourceMetrics): Option[ResourceMetrics] =
    resourceMap.get(resource).map { m => this += u(m) }

  /** Updates all [[ResourceMetrics]] instances.
    *
    * @return the updated [[resourceMap]]
    *
    * @param u a function to update the [[ResourceMetrics]] instances
    *
    * @group Update
    */
  def allResources(
      u: ResourceMetrics => ResourceMetrics
  ): scala.collection.mutable.Map[String, ResourceMetrics] = {
    resourceMap.iterator.foreach { case (r, m) => resourceMap.update(r, u(m)) }
    resourceMap
  }

  // Getters

  /**
    * Returns all the tracked instances of [[TaskMetrics]] sorted by starting time.
    * @group Get
    */
  def taskMetrics: Seq[TaskMetrics] = taskMap.values.toSeq.sortBy(_.started)
  /**
    * Returns all the tracked instances of [[SimulationMetrics]] sorted by simulation name.
    * @group Get
    */
  def simulationMetrics: Seq[SimulationMetrics] = simMap.values.toSeq.sortBy(_.name)
  /**
    * Returns all the tracked instances of [[ResourceMetrics]] sorted by resource time.
    * @group Get
    */
  def resourceMetrics: Seq[ResourceMetrics] = resourceMap.values.toSeq.sortBy(_.name)
  /**
    * Returns a [[scala.collection.immutable.Set]] of all task names being tracked.
    * This is useful when using task names as a category, for example to colour code tasks in the timeline.
    * @group Get
    */
  def taskSet: Set[String] = taskMap.values.map(_.task).toSet[String]

  /**
    * Returns all the tracked instances of [[TaskMetrics]] associated with a particular [[TaskResource]], sorted by starting time.
    * @param r the tracked [[ResourceMetrics]] of the resource
    * @group Get
    */
  // TODO: we used to have 2 levels of sorting!
  def taskMetricsOf(r: ResourceMetrics): Seq[TaskMetrics] =
    taskMap.values.toSeq.filter(_.resources.contains(r.name)).sortBy(_.started)

  /**
    * Returns all the tracked instances of [[TaskMetrics]] associated with a particular simulation, sorted by starting time.
    * @param r the tracked [[SimulationMetrics]] of the resource
    * @group Get
    */
  def taskMetricsOf(s: SimulationMetrics): Seq[TaskMetrics] =
    taskMap.values.toSeq.filter(_.simulation.equals(s.name)).sortBy(_.started)
}

/**
  * A [[ResultHandler]] that collects simulation metrics to a [[SimMetricsAggregator]].
  *
  * Returns the [[SimMetricsAggregator]] with all the data as a result when done.
  */
class SimMetricsHandler extends ResultHandler[SimMetricsAggregator] {
  val metrics = new SimMetricsAggregator()

  override def onEvent(evt: Event): Unit = evt match {
    case EStart(src) => metrics.started
    case EDone(src, t) => {
      metrics.allResources(_.idle(t))
      metrics.ended
    }
    case EResourceAdd(src, t, n, c) => metrics.addResource(n, c)
    case ESimAdd(src, t, a, s) => Unit
    case ESimStart(src, t, n) => metrics.addSim(n, t)
    case ESimEnd(src, t, n, r) => metrics.simulation(n)(_.done(r, t))
    case ETaskAdd(src, t, task) => metrics.addTask(task)
    case ETaskStart(src, t, task) => {
      metrics.task(task)(_.start(t))
      metrics.simulation(task.simulation)(_.task(task).addDelay(t - task.created))
    }
    case ETaskAttach(src, t, task, r) => metrics.resource(r)(_.task(t, task))
    case ETaskDetach(src, t, task, r) => Unit
    case ETaskDone(src, t, task) => Unit
    case ETaskAbort(src, t, id) => metrics.task(id)(_.abort(t))

    case EError(src, t, error) => Unit
  }

  override def result = metrics
}
