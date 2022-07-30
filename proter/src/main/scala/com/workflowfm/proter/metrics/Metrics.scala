package com.workflowfm.proter
package metrics

import java.util.UUID
import scala.collection.immutable.{ Map, Queue }

import events.*

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
    startTicks: Long,
    endTicks: Long,
    tasks: Map[UUID, TaskMetrics],
    cases: Map[String, CaseMetrics],
    resources: Map[String, ResourceMetrics],
    errors: Queue[Metrics.MetricsException]
) {

  import Metrics.{ MetricsException, TaskNotFound, CaseNotFound, ResourceNotFound }

  /**
    * Marks the start of metrics measurement with the current system time.
    * @group Start/End
    */
  def started(t: Long): Metrics = sStart match {
    case None =>
      copy(
        sStart = Some(System.currentTimeMillis()),
        startTicks = t
      )
    case _ => this
  }

  /**
    * Marks the end of metrics measurement with the current system time.
    * @group Start/End
    */
  def ended(t: Long): Metrics = copy(
    sEnd = Some(System.currentTimeMillis()),
    endTicks = t
  )

  /**
    * Adds a new [[TaskMetrics]] instance, taking care of indexing automatically Overwrites a
    * previous instance with the same IDs
    * @group Set
    */
  def +=(m: TaskMetrics): Metrics = copy(tasks = tasks + (m.id -> m))
  /**
    * Adds a new [[CaseMetrics]] instance, taking care of indexing automatically Overwrites a
    * previous instance with the same IDs
    * @group Set
    */
  def +=(m: CaseMetrics): Metrics = copy(cases = cases + (m.name -> m))
  /**
    * Adds a new [[ResourceMetrics]] instance, taking care of indexing automatically Overwrites a
    * previous instance with the same IDs
    * @group Set
    */
  def +=(m: ResourceMetrics): Metrics = copy(resources = resources + (m.name -> m))

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
  def addCase(s: String, t: Long): Metrics = this += CaseMetrics(s, t)

  /**
    * Initializes and adds a new [[ResourceMetrics]] instance given a new [[TaskResource]].
    * @group Set
    */
  def addResource(t: Long, r: Resource): Metrics =
    this += ResourceMetrics(t, r)

  def exception(e: MetricsException): Metrics =
    copy(errors = errors :+ e)

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
  def updateTask(taskID: UUID)(u: TaskMetrics => TaskMetrics): Metrics =
    tasks
      .get(taskID)
      .map { m => this += u(m) }
      .getOrElse(exception(TaskNotFound(taskID)))

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
  def updateTask(task: TaskInstance)(u: TaskMetrics => TaskMetrics): Metrics =
    tasks
      .get(task.id)
      .map { m => this += u(m) }
      .getOrElse(exception(TaskNotFound(task.id)))

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
  )(u: CaseMetrics => CaseMetrics): Metrics =
    cases
      .get(caseName)
      .map { m => this += u(m) }
      .getOrElse(exception(CaseNotFound(caseName)))

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
  def updateResource(resource: String)(u: ResourceMetrics => ResourceMetrics): Metrics =
    resources
      .get(resource)
      .map { m => this += u(m) }
      .getOrElse(exception(ResourceNotFound(resource)))

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
    copy(resources = resources.map { (r, m) => (r, u(m)) })
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

  def handle(evt: Event): Metrics = evt match {
    case EStart(_, t) => this.started(t).updateAllResources(_.start(t))
    case EDone(_, t) => updateAllResources(_.idle(t)).ended(t)
    case EResourceAdd(_, t, r) => addResource(t, r)
    case ECaseAdd(_, _, _, _) => this
    case ECaseStart(_, t, n) => addCase(n, t)
    case ECaseEnd(_, t, n, r) => updateCase(n)(_.done(r, t))
    case ETaskAdd(_, _, task) => addTask(task)
    case ETaskStart(_, t, task) =>
      updateTask(task)(_.start(t))
        .updateCase(task.caseName)(_.task(t - task.created, task.cost))
    case ETaskAttach(_, t, task, r) =>
      updateResource(r.resource.name)(_.task(t, task.resourceQuantity(r.resource.name)))
    case ETaskDetach(_, t, start, task, r) => {
      val q = task.resourceQuantity(r.resource.name)
      val cost = r.resource.costOf(t - start, q)
      updateResource(r.resource.name)(_.endTask(t, start, cost, q))
        .updateTask(task)(_.addCost(cost))
        .updateCase(task.caseName)(_.addCost(cost))
    }
    case ETaskDone(_, _, task) => this
    case ETaskAbort(_, t, id) => updateTask(id)(_.abort(t))

    case EArrivalAdd(_, _, _, _, _) => this
    case ETimeLimit(_, _, _) => this
    case EError(_, _, _) => this
  }

}

object Metrics {
  def apply(): Metrics = Metrics(None, None, 0L, 0L, Map(), Map(), Map(), Queue())

  sealed abstract class MetricsException(message: String) extends Exception(message)

  final case class TaskNotFound(id: UUID)
      extends MetricsException(s"Tried to update metrics for task that does not exist: $id")

  final case class CaseNotFound(caseName: String)
      extends MetricsException(s"Tried to update metrics for task that does not exist: $caseName")

  final case class ResourceNotFound(name: String)
      extends MetricsException(s"Tried to update metrics for task that does not exist: $name")

  import io.circe.generic.semiauto._

  given io.circe.Encoder[TaskMetrics] = deriveEncoder[TaskMetrics]
  given io.circe.Encoder[CaseMetrics] = deriveEncoder[CaseMetrics]
  given io.circe.Encoder[ResourceMetrics] = deriveEncoder[ResourceMetrics]
  given io.circe.Encoder[MetricsException] = deriveEncoder[MetricsException]

  given io.circe.Encoder[Metrics] = deriveEncoder[Metrics]

}
