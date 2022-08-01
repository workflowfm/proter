package com.workflowfm.proter
package metrics

import java.util.UUID
import scala.collection.immutable.{ Map, Queue }

import events.*

/**
  * Collects/aggregates metrics across multiple tasks, resources, and cases.
  *
  * @groupprio Start/End 2
  * @groupprio Set 3
  * @groupprio Update 4
  * @groupprio Get 5
  * 
  * @param start
  *   The system time in milliseconds when the simulation started.
  * @param end
  *   The system time in milliseconds when the simulation ended.
  * @param startTicks
  *   The timestamp when the simulation started.
  * @param endTicks
  *   The timestamp when the simulation ended.
  * @param tasks
  *   A map of all tracked [[TaskMetrics]].
  * @param cases
  *   A map of all tracked [[CaseMetrics]].
  * @param resources
  *   A map of all tracked [[ResourceMetrics]].
  * @param errors
  *   A queue of all errors caught.
  */
final case class Metrics(
    startTime: Option[Long],
    endTime: Option[Long],
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
    *
    * @group Start/End
    */
  def started(t: Long): Metrics = startTime match {
    case None =>
      copy(
        startTime = Some(System.currentTimeMillis()),
        startTicks = t
      )
    case _ => this
  }

  /**
    * Marks the end of metrics measurement with the current system time.
    *
    * @group Start/End
    */
  def ended(t: Long): Metrics = copy(
    endTime = Some(System.currentTimeMillis()),
    endTicks = t
  )

  /**
    * Adds a new [[TaskMetrics]] instance.
    *
    * Overwrites any previous instance with the same ID.
    *
    * @group Set
    */
  def +=(m: TaskMetrics): Metrics = copy(tasks = tasks + (m.id -> m))

  /**
    * Adds a new [[CaseMetrics]] instance.
    *
    * Overwrites any previous instance with the same name.
    *
    * @group Set
    */
  def +=(m: CaseMetrics): Metrics = copy(cases = cases + (m.name -> m))

  /**
    * Adds a new [[ResourceMetrics]] instance.
    *
    * Overwrites any previous instance with the same name.
    *
    * @group Set
    */
  def +=(m: ResourceMetrics): Metrics = copy(resources = resources + (m.name -> m))

  /**
    * Adds a new [[TaskMetrics]] instance given a new [[TaskInstance]].
    *
    * @group Set
    */
  def addTask(task: TaskInstance): Metrics = this += TaskMetrics(task)
  /**
    * Adds a new [[CaseMetrics]] instance given a named case starting now and current time.
    *
    * @group Set
    */
  def addCase(s: String, t: Long): Metrics = this += CaseMetrics(s, t)

  /**
    * Adds a new [[ResourceMetrics]] instance given a new [[Resource]].
    *
    * @group Set
    */
  def addResource(t: Long, r: Resource): Metrics =
    this += ResourceMetrics(t, r)

  /**
    * Adds a new [[Metrics.MetricsException MetricsException]].
    *
    * @group Set
    */
  def exception(e: MetricsException): Metrics =
    copy(errors = errors :+ e)

  // Update
  /**
    * Updates a [[TaskMetrics]] instance.
    *
    * @return
    *   The [[Metrics]] updated either with the new [[TaskMetrics]] or an error if the instance does
    *   not exist.
    * @param taskID
    *   The task ID for the involved [[TaskInstance]].
    * @param u
    *   A function to update the [[TaskMetrics]].
    *
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
    *   The [[Metrics]] updated either with the new [[TaskMetrics]] or an error if the instance does
    *   not exist.
    * @param task
    *   The involved [[TaskInstance]].
    * @param u
    *   A function to update the [[TaskMetrics]].
    *
    * @group Update
    */
  def updateTask(task: TaskInstance)(u: TaskMetrics => TaskMetrics): Metrics =
    updateTask(task.id)(u)

  /**
    * Updates a [[CaseMetrics]] instance.
    *
    * @return
    *   The [[Metrics]] updated either with the new [[CaseMetrics]] or an error if the instance does
    *   not exist.
    * @param caseName
    *   The name of the involved case.
    * @param u
    *   A function to update the [[CaseMetrics]].
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
    *   The [[Metrics]] updated either with the new [[ResourceMetrics]] or an error if the instance
    *   does not exist.
    * @param resource
    *   The name of the involved resource.
    * @param u
    *   A function to update the [[ResourceMetrics]].
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
    *   The updated [[Metrics]].
    * @param u
    *   A function to update each of the [[ResourceMetrics]].
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
    *
    * @group Get
    */
  def taskMetrics: Seq[TaskMetrics] = tasks.values.toSeq.sortBy(_.started)

  /**
    * Returns all the tracked instances of [[CaseMetrics]] sorted by name.
    *
    * @group Get
    */
  def caseMetrics: Seq[CaseMetrics] = cases.values.toSeq.sortBy(_.name)

  /**
    * Returns all the tracked instances of [[ResourceMetrics]] sorted by name.
    *
    * @group Get
    */
  def resourceMetrics: Seq[ResourceMetrics] = resources.values.toSeq.sortBy(_.name)

  /**
    * Returns a `Set` of all task names being tracked.
    *
    * This is useful when using task names as a category, for example to colour code tasks in the
    * timeline.
    *
    * @group Get
    */
  def taskSet: Set[String] = tasks.values.map(_.task).toSet[String]

  /**
    * All the tracked [[TaskMetrics]] instances associated with a [[Resource]], sorted by
    * starting time.
    *
    * @param r
    *   The tracked [[ResourceMetrics]] of the resource.
    *
    * @group Get
    */
  // TODO: we used to have 2 levels of sorting!
  def taskMetricsOf(r: ResourceMetrics): Seq[TaskMetrics] =
    tasks.values.toSeq.filter(_.resources.contains(r.name)).sortBy(_.started)

  /**
    * Returns all the tracked instances of [[TaskMetrics]] associated with a particular simulation,
    * sorted by starting time.
    * @param r
    *   The tracked [[TaskMetrics]] related to the resource.
    * @group Get
    */
  def taskMetricsOf(s: CaseMetrics): Seq[TaskMetrics] =
    tasks.values.toSeq.filter(_.caseName.equals(s.name)).sortBy(_.started)

  /**
    * Convert [[events.Event Event]]s to metrics updates.
    */
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
    case ETaskDetach(_, t, tstart, task, r) => {
      val q = task.resourceQuantity(r.resource.name)
      val cost = r.resource.costOf(t - tstart, q)
      updateResource(r.resource.name)(_.endTask(t, tstart, cost, q))
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

  final case class TaskNotFound(taskId: UUID)
      extends MetricsException(s"Tried to update metrics for task that does not exist: $taskId")

  final case class CaseNotFound(caseName: String)
      extends MetricsException(s"Tried to update metrics for task that does not exist: $caseName")

  final case class ResourceNotFound(resourceName: String)
      extends MetricsException(s"Tried to update metrics for task that does not exist: $resourceName")

  import io.circe.generic.semiauto.*
  import io.circe.Encoder
  import io.circe.syntax.*

  given Encoder[TaskMetrics] = deriveEncoder[TaskMetrics]
  given taskMetricsMapEncoder: Encoder[Map[UUID, TaskMetrics]] = (collection: Map[UUID, TaskMetrics]) =>
    collection.values.map(_.asJson).toList.asJson

  given Encoder[CaseMetrics] = deriveEncoder[CaseMetrics]
  given caseMetricsMapEncoder: Encoder[Map[String, CaseMetrics]] = (collection: Map[String, CaseMetrics]) =>
    collection.values.map(_.asJson).toList.asJson

  given Encoder[ResourceMetrics] = deriveEncoder[ResourceMetrics]
  given resourceMetricsMapEncoder: Encoder[Map[String, ResourceMetrics]] = (collection: Map[String, ResourceMetrics]) =>
    collection.values.map(_.asJson).toList.asJson

  given Encoder[TaskNotFound] = deriveEncoder[TaskNotFound]
  given Encoder[CaseNotFound] = deriveEncoder[CaseNotFound]
  given Encoder[ResourceNotFound] = deriveEncoder[ResourceNotFound]
  given Encoder[MetricsException] = Encoder.instance {
    case t @ TaskNotFound(_) => t.asJson
    case c @ CaseNotFound(_) => c.asJson
    case r @ ResourceNotFound(_) => r.asJson
  }

  given Encoder[Metrics] = deriveEncoder[Metrics]

}
