package com.workflowfm.proter.metrics

import java.util.UUID

import scala.collection.immutable.Map

import com.workflowfm.proter.*
import com.workflowfm.proter.events.*

/**
  * Collects/aggregates metrics across multiple tasks, resources, and simulations.
  *
  * @groupprio Values
  *   1
  * @groupprio Start/End
  *   2
  * @groupprio Set
  *   3
  * @groupprio Update
  *   4
  * @groupprio Get
  *   5
  */
final case class Metrics(
  sStart: Option[Long], 
  sEnd: Option[Long],
  tasks: Map[UUID, TaskMetrics],
  cases: Map[String, CaseMetrics],
  resources: Map[String, ResourceMetrics]
) {

  import Metrics.{ 
    MetricsException, 
    TaskNotFound, 
    CaseNotFound, 
    ResourceNotFound,
  }

  /**
    * The '''real''' (system) time that measurement started, or [[scala.None]] if it has not started
    * yet.
    * @group Start/End
    */
//  var start: Option[Long] = None
  /**
    * The '''real''' (system) time that measurement finished, or [[scala.None]] if it has not
    * finished yet.
    * @group Start/End
    */
//  var end: Option[Long] = None

  /**
    * Marks the start of metrics measurement with the current system time.
    * @group Start/End
    */
  def started: Metrics = sStart match {
    case None => copy(sStart = Some(System.currentTimeMillis()))
    case _ => this
  }

  /**
    * Marks the end of metrics measurement with the current system time.
    * @group Start/End
    */
  def ended: Metrics = copy(sEnd = Some(System.currentTimeMillis()))

  /**
    * Task metrics indexed by task ID.
    * @group Values
    */
//  val taskMap: mutable.Map[UUID, TaskMetrics] = scala.collection.mutable.Map[UUID, TaskMetrics]()

  /**
    * Simulation metrics indexed by name.
    * @group Values
    */
//  val simMap: mutable.Map[String, CaseMetrics] =
//    scala.collection.mutable.Map[String, CaseMetrics]()

  /**
    * Resource metrics indexed by name.
    * @group Values
    */
//  val resourceMap: mutable.Map[String, ResourceMetrics] =
//    scala.collection.mutable.Map[String, ResourceMetrics]()

  // Set

  /**
    * Adds a new [[TaskMetrics]] instance, taking care of indexing automatically Overwrites a
    * previous instance with the same IDs
    * @group Set
    */
  def +=(m: TaskMetrics): Metrics = copy( tasks = tasks + (m.id -> m) )
  /**
    * Adds a new [[CaseMetrics]] instance, taking care of indexing automatically Overwrites a
    * previous instance with the same IDs
    * @group Set
    */
  def +=(m: CaseMetrics): Metrics = copy( cases = cases + (m.name -> m) )
  /**
    * Adds a new [[ResourceMetrics]] instance, taking care of indexing automatically Overwrites a
    * previous instance with the same IDs
    * @group Set
    */
  def +=(m: ResourceMetrics): Metrics = copy( resources = resources + (m.name -> m) )

  /**
    * Initializes and adds a new [[TaskMetrics]] instance given a new [[TaskInstance]].
    * @group Set
    */
  def addTask(task: TaskInstance): Metrics = this += TaskMetrics(task)
  /**
    * Initializes and adds a new [[SimulationMetrics]] instance given the name of the simulation
    * starting now and the current virtual time.
    * @group Set
    */
  def addSim(s: String, t: Long): Metrics = this += CaseMetrics(s, t)

  /**
    * Initializes and adds a new [[ResourceMetrics]] instance given a new [[TaskResource]].
    * @group Set
    */
  def addResource(r: String, costPerTick: Double): Metrics =
    this += ResourceMetrics(r, costPerTick)

  // Update
  /**
    * Updates a [[TaskMetrics]] instance.
    *
    * @return
    *   the updated [[TaskMetrics]] or [[scala.None]] if the identified instance does not exist
    *
    * @param taskID
    *   the task ID for the involved [[TaskInstance]]
    * @param u
    *   a function to update the [[TaskMetrics]] instance
    *
    * @see
    *   [[com.workflowfm.pew.metrics.MetricsAggregator]] for examples in a similar context
    * @group Update
    */
  def updateTask(taskID: UUID)(u: TaskMetrics => TaskMetrics): Either[MetricsException, Metrics] =
    tasks
      .get(taskID)
      .map { m => this += u(m) }
      .toRight(TaskNotFound(taskID))

  /**
    * Updates a [[TaskMetrics]] instance.
    *
    * @return
    *   the updated [[TaskMetrics]] or [[scala.None]] if the identified instance does not exist
    *
    * @param task
    *   the involved [[TaskInstance]]
    * @param u
    *   a function to update the [[TaskMetrics]] instance
    *
    * @see
    *   [[com.workflowfm.pew.metrics.MetricsAggregator]] for examples in a similar context
    *
    * @group Update
    */
  def updateTask(task: TaskInstance)(u: TaskMetrics => TaskMetrics): Either[MetricsException, Metrics] =
    tasks
      .get(task.id)
      .map { m => this += u(m) }
      .toRight(TaskNotFound(task.id))

  /**
    * Updates a [[SimulationMetrics]] instance.
    *
    * @return
    *   the updated [[SimulationMetrics]] or [[scala.None]] if the identified instance does not
    *   exist
    *
    * @param simulation
    *   the name of the involved simulation
    * @param u
    *   a function to update the [[SimulationMetrics]] instance
    *
    * @see
    *   [[com.workflowfm.pew.metrics.MetricsAggregator]] for examples in a similar context
    *
    * @group Update
    */
  def updateCase(
      caseName: String
  )(u: CaseMetrics => CaseMetrics): Either[MetricsException, Metrics] =
    cases
      .get(caseName)
      .map { m => this += u(m) }
      .toRight(CaseNotFound(caseName))

  /**
    * Updates a [[ResourceMetrics]] instance.
    *
    * @return
    *   the updated [[ResourceMetrics]] or [[scala.None]] if the identified instance does not exist
    *
    * @param resource
    *   the involved [[TaskResource]]
    * @param u
    *   a function to update the [[ResourceMetrics]] instance
    *
    * @see
    *   [[com.workflowfm.pew.metrics.MetricsAggregator]] for examples in a similar context
    *
    * @group Update
    */
  def updateResource(resource: String)(u: ResourceMetrics => ResourceMetrics): Either[MetricsException, Metrics] =
    resources
      .get(resource)
      .map { m => this += u(m) }
      .toRight(ResourceNotFound(resource))


  /**
    * Updates all [[ResourceMetrics]] instances.
    *
    * @return
    *   the updated [[resourceMap]]
    *
    * @param u
    *   a function to update the [[ResourceMetrics]] instances
    *
    * @group Update
    */
  def updateAllResources(
      u: ResourceMetrics => ResourceMetrics
  ): Metrics = {
    copy( resources = 
      resources
        .map { (r, m) => (r, u(m)) }
    )
  }

  // Getters

  /**
    * Returns all the tracked instances of [[TaskMetrics]] sorted by starting time.
    * @group Get
    */
  def taskMetrics: Seq[TaskMetrics] = tasks.values.toSeq.sortBy(_.started)
  /**
    * Returns all the tracked instances of [[SimulationMetrics]] sorted by simulation name.
    * @group Get
    */
  def caseMetrics: Seq[CaseMetrics] = cases.values.toSeq.sortBy(_.name)
  /**
    * Returns all the tracked instances of [[ResourceMetrics]] sorted by resource time.
    * @group Get
    */
  def resourceMetrics: Seq[ResourceMetrics] = resources.values.toSeq.sortBy(_.name)
  /**
    * Returns a [[scala.collection.immutable.Set]] of all task names being tracked. This is useful
    * when using task names as a category, for example to colour code tasks in the timeline.
    * @group Get
    */
  def taskSet: Set[String] = tasks.values.map(_.task).toSet[String]

  /**
    * Returns all the tracked instances of [[TaskMetrics]] associated with a particular
    * [[TaskResource]], sorted by starting time.
    * @param r
    *   the tracked [[ResourceMetrics]] of the resource
    * @group Get
    */
  // TODO: we used to have 2 levels of sorting!
  def taskMetricsOf(r: ResourceMetrics): Seq[TaskMetrics] =
    tasks.values.toSeq.filter(_.resources.contains(r.name)).sortBy(_.started)

  /**
    * Returns all the tracked instances of [[TaskMetrics]] associated with a particular simulation,
    * sorted by starting time.
    * @param r
    *   the tracked [[SimulationMetrics]] of the resource
    * @group Get
    */
  def taskMetricsOf(s: CaseMetrics): Seq[TaskMetrics] =
    tasks.values.toSeq.filter(_.caseName.equals(s.name)).sortBy(_.started)
}

object Metrics {
  sealed abstract class MetricsException(message: String) extends Exception(message)

  final case class TaskNotFound(id: UUID) 
      extends MetricsException(s"Tried to update metrics for task that does not exist: $id")

  final case class CaseNotFound(caseName: String) 
      extends MetricsException(s"Tried to update metrics for task that does not exist: $caseName")  

  final case class ResourceNotFound(name: String)
      extends MetricsException(s"Tried to update metrics for task that does not exist: $name")

}

/*
/**
 * A [[com.workflowfm.proter.events.ResultHandler ResultHandler]] that collects simulation metrics
 * to a [[SimMetricsAggregator]].
 *
 * Returns the [[SimMetricsAggregator]] with all the data as a result when done.
 *
 * Outputs the result using an (optional) [[SimMetricsOutput]].
 *
 * @param output
 *   The [[SimMetricsOutput]] to use, if any.
 */
class SimMetricsHandler(output: SimMetricsOutput = SimNoOutput)
    extends ResultHandler[SimMetricsAggregator] {
  val metrics = new SimMetricsAggregator()

  override def onEvent(evt: Event): Unit = evt match {
    case EStart(_, _) => metrics.started
    case EDone(_, t) => {
      metrics.allResources(_.idle(t))
      metrics.ended
      output(t, metrics)
    }
    case EResourceAdd(_, _, n, c) => metrics.addResource(n, c)
    case ESimAdd(_, _, _, _) => ()
    case ESimStart(_, t, n) => metrics.addSim(n, t)
    case ESimEnd(_, t, n, r) => metrics.simulation(n)(_.done(r, t))
    case ETaskAdd(_, _, task) => metrics.addTask(task)
    case ETaskStart(_, t, task) => {
      metrics.task(task)(_.start(t))
      metrics.simulation(task.simulation)(_.task(task).addDelay(t - task.created))
    }
    case ETaskAttach(_, t, task, r) => metrics.resource(r)(_.task(t, task))
    case ETaskDetach(_, _, task, _, c) => {
      metrics.task(task)(_.addCost(c))
      metrics.simulation(task.simulation)(_.addCost(c))
    }
    case ETaskDone(_, _, _) => ()
    case ETaskAbort(_, t, id) => metrics.task(id)(_.abort(t))

    case EError(_, _, _) => ()
  }

  override def result = metrics
}
 */
