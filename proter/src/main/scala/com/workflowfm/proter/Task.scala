package com.workflowfm.proter

import java.util.UUID
import cats.effect.std.Random
import cats.Monad
import cats.implicits.*

/**
  * An instance of a task to be performed in virtual time.
  *
  * @note
  *   We expect that task instances are only generated through a [[Task]].
  *
  * The samples of the parameters are generated during simulation. The duration can be
  * specified through random sampling in a [[Distribution]]. We store the estimated value as well as
  * the actual sampled value.
  *
  * @param id
  *   A unique id that separates this task from any other task. A case may spawn multiple tasks
  *   with the same name, so this is necessary.
  * @param name
  *   A descriptive name.
  * @param caseName
  *   The unique name of the case this task belongs to.
  * @param created
  *   The timestamp when this task was created.
  * @param minStartTime
  *   The earliest starting time of this task. Used for scheduling tasks in the future.
  * @param resources
  *   The names and required capacities of the [[Resource]]s for this task.
  * @param duration
  *   The actual duration of the task.
  * @param estimatedDuration
  *   The estimated duration of the task.
  * @param initialCost
  *   A one-off cost of performing this task.
  * @param interrupt
  *   The number of times the task can be interrupted in favour of a more urgent one. This parameter
  *   is not currently being used.
  * @param priority
  *   The explicit priority of the task.
  */
final case class TaskInstance(
    val id: UUID,
    val name: String,
    val caseName: String,
    val created: Long,
    val minStartTime: Long,
    val resources: Map[String, Int],
    val duration: Long,
    val estimatedDuration: Long,
    val cost: Double,
    val interrupt: Int = Int.MaxValue,
    val priority: Int = 0
) extends Ordered[TaskInstance] {

  def resourceQuantity(r: String): Int =
    resources.get(r).getOrElse(0)

  /**
    * Ordering of tasks.
    *
    * This method essentially dictates the priority of tasks in the queue, highest to lowest.
    *
    * Uses the following criteria, in order:
    *
    *   1. '''Priority''': Putting this parameter first makes it a very strong influencer of
    *      priority. A task with higher explicit priority will always be executed before any other
    *      task with lower priority, even if, for example, the latter has been queueing for a long
    *      time.
    *   1. '''Age''': Tasks who were created earlier take priority, in an attempt to minimize
    *      waiting times.
    *   1. '''Resources''': Tasks that involve a higher number of resources take priority. The
    *      intuition is that it is generally harder to achieve a state where more resources are
    *      available at the same time, so we do not want to postpone the task.
    *   1. '''Estimated duration''': Tasks that require more time are prioritized.
    *   1. '''Interrupt''': Inflexible tasks that can not be interrupted are prioritized.
    *   1. Tasks are considered of equal priority at this point, so we use their id as a
    *      deterministic ordering.
    *
    * @note
    *   You can change this order by subclassing [[TaskInstance]] and creating a corresponding
    *   subclass of [[Task]].
    *
    * @param that
    *   The task instance to compare to.
    * @return
    *   A comparative measure (lower means higher priority).
    */
  def compare(that: TaskInstance): Int = {
    lazy val cPriority = that.priority.compare(this.priority)
    lazy val cResources = that.resources.size.compare(this.resources.size)
    lazy val cAge = this.created.compare(that.created)
    lazy val cDuration = that.estimatedDuration.compare(this.estimatedDuration)
    lazy val cInterrupt = this.interrupt.compare(that.interrupt)
    lazy val cID = this.id.compareTo(that.id)

    if cPriority != 0 then cPriority
    else if cAge != 0 then cAge
    else if cResources != 0 then cResources
    else if cDuration != 0 then cDuration
    else if cInterrupt != 0 then cInterrupt
    else cID
  }

  override def toString: String = {
    val res = resources.mkString(",")
    s"Task($id,$name,$caseName,$created,[$res],d$duration($estimatedDuration),c$cost,i$interrupt,$priority)"
  }
}

/**
  * A descriptor of tasks and generator/factory of [[TaskInstance]]s.
  *
  * As a case class, it can be easily manipulated dynamically to alter its values.
  *
  * Uses [[Distribution]]s for the duration and cost. These may be based on random variables. The
  * actual values are sampled at task creation time.
  *
  * Although typically only 1 task is generated, in principle it can generate multiple task
  * instances with the same properties. However, each task generated will have different samples of
  * the [[Distribution]]s.
  *
  * @param name
  *   The name of the task.
  * @param id
  *   An optional unique id of the task. Otherwise a random ID will be used.
  * @param duration
  *   The generator of the duration.
  * @param cost
  *   The generator of the cost.
  * @param minStartTime
  *   The earliest starting time of the task. Used for scheduling tasks in the future.
  * @param resources
  *   The resources and required capacities for this task.
  * @param interrupt
  *   The [[TaskInstance.interrupt]] property of the task.
  * @param priority
  *   The explicit priority of the task.
  * @param createTime
  *   A custom creation time. Negative values correspond to the current time.
  */
final case class Task(
    name: String,
    id: Option[UUID],
    duration: LongDistribution,
    cost: Distribution = Constant(0L),
    minStartTime: Long = 0L,
    resources: Map[String, Int] = Map(),
    interrupt: Int = (-1),
    priority: Int = 0,
    createTime: Long = (-1)
) {

  /**
    * Generate a [[TaskInstance]].
    *
    * @param caseName
    *   The name of the case that is spawning the instance.
    * @param currentTime
    *   The current time.
    * @return
    *   The generated [[TaskInstance]].
    */
  def create[F[_] : Random : Monad](caseName: String, currentTime: Long): F[TaskInstance] = {
    val creation = if createTime >= 0 then createTime else currentTime
    for {
      dur <- duration.getLong
      c <- cost.get
    } yield (
      TaskInstance(
        id.getOrElse(UUID.randomUUID()),
        name,
        caseName,
        creation,
        minStartTime,
        resources,
        dur,
        duration.longEstimate,
        c,
        interrupt,
        priority
      )
    )
  }

  /**
    * Update the ID to use.
    *
    * @param i
    *   The new ID.
    * @return
    *   An updated [[Task]].
    */
  def withID(i: UUID): Task = copy(id = Some(i))

  /**
    * Add required resources to use, each with required capacity 1.
    *
    * @param r
    *   The new resources.
    * @return
    *   An updated [[Task]].
    */
  def withResources(r: Seq[String]): Task = copy(resources = resources ++ r.map(_ -> 1))

  /**
    * Add resources and required capacities to use.
    *
    * @param r
    *   The new resources.
    * @return
    *   An updated [[Task]].
    */
  def withResourceQuantities(s: Map[String, Int]): Task = copy(resources = resources ++ s)

  /**
    * Update the priority to use.
    *
    * @param p
    *   The new priority.
    * @return
    *   An updated [[Task]].
    */
  def withPriority(p: Int): Task = copy(priority = p)

  /**
    * Update the interrupt to use.
    *
    * @param int
    *   The new interrupt.
    * @return
    *   An updated [[Task]].
    */
  def withInterrupt(int: Int): Task = copy(interrupt = int)

  /**
    * Update the duration distribution to use.
    *
    * @param dur
    *   The new duration distribution.
    * @return
    *   An updated [[Task]].
    */
  def withDuration(dur: LongDistribution): Task = copy(duration = dur)

  /**
    * Update the duration distribution to a constant value.
    *
    * @param dur
    *   The constant duration value.
    * @return
    *   An updated [[Task]].
    */
  def withDuration(dur: Long): Task = copy(duration = ConstantLong(dur))

  /**
    * Update the cost distribution to use.
    *
    * @param cost
    *   The new cost distribution.
    * @return
    *   An updated [[Task]].
    */
  def withCost(cost: Distribution): Task = copy(cost = cost)

  /**
    * Update the cost [[Distribution]] to a constant value.
    *
    * @param cost
    *   The constant cost value.
    * @return
    *   An updated [[Task]].
    */
  def withCost(cost: Double): Task = copy(cost = Constant(cost))

  /**
    * Update the task name to use.
    *
    * @param n
    *   The new name.
    * @return
    *   An updated [[Task]].
    */
  def withName(n: String): Task = copy(name = n)

  /**
    * Update the custom creation time to use.
    *
    * @param t
    *   The new creation time.
    * @return
    *   An updated [[Task]].
    */
  def withCreationTime(t: Long): Task = copy(createTime = t)

  /**
    * Update the custom minimum starting time to use.
    *
    * @param t
    *   The new minimum starting time.
    * @return
    *   An updated [[Task]].
    */
  def withMinStartTime(t: Long): Task = copy(minStartTime = t)
}

object Task {

  /**
    * An alternative constructor of a [[Task]].
    *
    * Uses [[Distribution]]s for the duration. These may be based on random variables. The actual
    * values are sampled at task creation time.
    *
    * @param name
    *   The name of the task.
    * @param duration
    *   The generator of the duration.
    * @return
    *   The corresponding [[Task]].
    */
  def apply(
      name: String,
      duration: LongDistribution
  ): Task =
    Task(name, None, duration)

  /**
    * An alternative constructor of a [[Task]].
    *
    * Uses a constant duration.
    *
    * @param name
    *   The name of the task.
    * @param duration
    *   The duration of the task.
    * @return
    *   The corresponding [[Task]].
    */
  def apply(
      name: String,
      duration: Long
  ): Task =
    Task(name, None, ConstantLong(duration))

  /**
    * An alternative constructor of a [[Task]].
    *
    * Uses a provided unique ID and a constant duration.
    *
    * @param name
    *   The name of the task.
    * @param duration
    *   The duration of the task.
    * @return
    *   The corresponding [[Task]].
    */
  def apply(
      name: String,
      id: UUID,
      duration: Long
  ): Task =
    Task(name, Some(id), ConstantLong(duration))

  val Highest: Int = 2
  val High: Int = 1
  val Medium: Int = 0
  val Low: Int = -1
  val VeryLow: Int = -2
}
