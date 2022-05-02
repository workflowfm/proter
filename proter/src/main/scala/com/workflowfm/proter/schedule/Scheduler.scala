package com.workflowfm.proter.schedule

import java.util.UUID

import scala.annotation.tailrec
import scala.collection.immutable.Map
import scala.collection.mutable.SortedSet

import com.workflowfm.proter.{ TaskInstance, TaskResource, Lookahead, NoLookahead }

/**
  * A scheduler selects the next [[TaskInstance]]s to be started by the [[Coordinator]] at a given
  * time.
  */
trait Scheduler {

  /**
    * Determines which [[TaskInstance]]s to start next.
    *
    * [[TaskInstance]]s are assumed to be sorted by priority.
    *
    * @param tasks
    *   The queue of [[TaskInstance]]s waiting to be started.
    * @param currentTime
    *   The current timestamp.
    * @param resourceMap
    *   The map of available [[TaskResource]]s.
    * @return
    *   The sequence of [[TaskInstance]]s to start now.
    */
  def getNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource]
  ): Seq[TaskInstance]

  /**
    * Checks if a named [[TaskResource]] is idle.
    *
    * @param r
    *   The name of the [[TaskResource]].
    * @param resourceMap
    *   The map of available [[TaskResource]]s.
    * @return
    *   true if the resource is idle, false otherwise.
    */
  def isIdleResource(r: String, resourceMap: Map[String, TaskResource]): Boolean =
    resourceMap.get(r) match {
      case None => false
      case Some(s) => s.isIdle
    }

  /**
    * Sets the lookahead structure for the specified actor.
    *
    * @param actor
    *   The actor that created this lookahead structure.
    * @param obj
    *   The lookahead structure.
    */
  def setLookahead(simulation: String, obj: Lookahead): Unit = ()

  /**
    * Removes the lookahead structure associated with the given actor.
    *
    * @param actor
    *   The actor corresponding to the lookahead structure.
    */
  def removeLookahead(simulation: String): Unit = ()

  /**
    * Adds an Task described by an (ID,time) pair to the list of completed IDs
    *
    * @param id
    *   The ID to be added
    * @param time
    *   The time at which the task completed
    * @return
    *   A LookaheadStructure with this (ID,time) pair added to the list of completed tasks
    */
  def complete(task: TaskInstance, time: Long): Unit = ()

  /**
    * Retrieves an iterable collection of queued [[TaskInstance]]s.
    *
    * @return
    *   The [[TaskInstance]]s in the scheduling queue.
    */
  def getTasks(): Iterable[TaskInstance]

  /**
    * Adds a [[TaskInstance]] to be scheduled.
    *
    * @param task
    *   The [[TaskInstance]] to add.
    */
  def addTask(task: TaskInstance): Unit

  /**
    * Removes a [[TaskInstance]] that no longer needs scheduling.
    *
    * @param task
    *   The [[TaskInstance]] to remove.
    */
  def removeTask(task: TaskInstance): Unit

  /**
    * Removes all [[TaskInstance]]s belonging to an (presumably aborted) simulation.
    *
    * @param simulation
    *   The name of the simulation that was aborted.
    */
  def removeSimulation(simulation: String): Unit

  /**
    * Checks if all [[Task]]s have been scheduled.
    *
    * @return
    *   true if there are no [[Task]]s remaining.
    */
  def noMoreTasks(): Boolean

}

trait QueueScheduler extends Scheduler {
  import scala.collection.mutable.Queue

  /**
    * A queue of tasks that need to be run.
    */
  val tasks: Queue[TaskInstance] = Queue()

  /**
    * @inheritdoc
    */
  override def getTasks(): Iterable[TaskInstance] = tasks

  /**
    * @inheritdoc
    */
  override def addTask(task: TaskInstance): Unit = tasks += task

  /**
    * @inheritdoc
    */
  override def removeTask(task: TaskInstance): Unit = tasks.dequeueFirst(_.id.equals(task.id))

  /**
    * @inheritdoc
    */
  override def removeSimulation(simulation: String): Unit =
    tasks.dequeueAll(_.simulation == simulation)

  /**
    * @inheritdoc
    */
  override def noMoreTasks(): Boolean = tasks.isEmpty

}

/**
  * A [[Scheduler]] trait that uses a `SortedSet`.
  *
  * Forms the basis for priority-based schedulers.
  */
abstract class PriorityScheduler(using ordering: Ordering[TaskInstance]) extends Scheduler {

  /**
    * A sorted queue of tasks that need to be run.
    */
  val tasks: SortedSet[TaskInstance] = SortedSet()(ordering)

  /**
    * @inheritdoc
    */
  override def getTasks(): Iterable[TaskInstance] = tasks

  /**
    * @inheritdoc
    */
  override def addTask(task: TaskInstance): Unit = tasks += task

  /**
    * @inheritdoc
    */
  override def removeTask(task: TaskInstance): Unit = tasks -= task

  /**
    * @inheritdoc
    */
  override def removeSimulation(simulation: String): Unit =
    tasks --= tasks.filter(_.simulation == simulation)

  /**
    * @inheritdoc
    */
  override def noMoreTasks(): Boolean = tasks.isEmpty

}

/**
  * A greedy [[Scheduler]].
  *
  * Starts all tasks whose resources are currently idle, in the given order/priority.
  *
  * This means a lower priority task may start now and block a higher priority task which is
  * currently blocked, but could have started soon.
  */
trait GreedyScheduler extends Scheduler {
  import scala.collection.immutable.Queue

  override def getNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource]
  ): Seq[TaskInstance] = {
    findNextTasks(resourceMap.filter(_._2.isIdle), getTasks(), Queue())
  }

  /**
    * Finds the [[Task]]s that can be started now.
    *
    * Goes through the priority list of tasks and returns those whose resources are idle (even after
    * starting higher priority tasks).
    *
    * @param idleResources
    *   The map of idle [[TaskResource]]s.
    * @param tasks
    *   The set of [[TaskInstance]]s that need to start.
    * @param result
    *   The accumulated [[TaskInstance]]s so far (for tail recursion).
    * @return
    *   The sequence of [[TaskInstance]]s to start now.
    */
  @tailrec
  final protected def findNextTasks(
      idleResources: Map[String, TaskResource],
      tasks: Iterable[TaskInstance],
      result: Queue[TaskInstance]
  ): Seq[TaskInstance] =
    if tasks.isEmpty then result
    else {
      val t = tasks.head
      if t.resources.forall(idleResources.contains) then
        findNextTasks(idleResources -- t.resources, tasks.tail, result :+ t)
      else findNextTasks(idleResources, tasks.tail, result)
    }
}

/**
  * A strict [[Scheduler]].
  *
  * Starts all tasks whose resources are currently idle, in the given order/priority.
  *
  * Does not consider resources of higher priority tasks. This means that idle resources may be
  * blocked by queued high priority tasks that cannot start yet.
  */
trait StrictScheduler extends Scheduler {
  import scala.collection.immutable.Queue

  override def getNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource]
  ): Seq[TaskInstance] = {
    findNextTasks(resourceMap.filter(_._2.isIdle), getTasks(), Queue())
  }

  /**
    * Finds the [[Task]]s that can be started now.
    *
    * Goes through the priority list of tasks and returns those whose resources are idle (even after
    * any queued higher priority task).
    *
    * @param idleResources
    *   The map of idle [[TaskResource]]s.
    * @param tasks
    *   The set of [[TaskInstance]]s that need to start.
    * @param result
    *   The accumulated [[TaskInstance]]s so far (for tail recursion).
    * @return
    *   The sequence of [[TaskInstance]]s to start now.
    */
  @tailrec
  final protected def findNextTasks(
      idleResources: Map[String, TaskResource],
      tasks: Iterable[TaskInstance],
      result: Queue[TaskInstance]
  ): Seq[TaskInstance] =
    if tasks.isEmpty then result
    else {
      val t = tasks.head
      if t.resources.forall(idleResources.contains) then
        findNextTasks(idleResources -- t.resources, tasks.tail, result :+ t)
      else findNextTasks(idleResources -- t.resources, tasks.tail, result)
    }
}

/**
  * A greedy First-Come-First-Served [[Scheduler]].
  *
  * Starts all tasks whose resources are currently idle, in order of arrival in the queue.
  *
  * This means a task that arrived later may start now and block an earlier task which is currently
  * blocked, but could have started soon.
  *
  * Ignores creation time. Only cares about order in the queue.
  *
  * @param initialTasks
  *   Initial [[TaskInstance]]s in the queue, if any.
  */
class GreedyFCFSScheduler(initialTasks: TaskInstance*) extends QueueScheduler with GreedyScheduler {
  tasks ++= initialTasks
}

/**
  * A strict First-Come-First-Served [[Scheduler]].
  *
  * Starts all tasks whose resources are currently idle, in order of arrival in the queue.
  *
  * Considers resources of earlier tasks as busy even if they are not. This means that idle
  * resources may be blocked by tasks earlier in the queue that cannot start yet.
  *
  * Ignores creation time. Only cares about order in the queue.
  *
  * @param initialTasks
  *   Initial [[TaskInstance]]s in the queue, if any.
  */
class StrictFCFSScheduler(initialTasks: TaskInstance*) extends QueueScheduler with StrictScheduler {
  tasks ++= initialTasks
}

/**
  * A greedy priority [[Scheduler]].
  *
  * Starts all tasks whose resources are currently idle, in order of priority.
  *
  * This means a lower priority task may start now and block a higher priority task which is
  * currently blocked, but could have started soon.
  *
  * @param initialTasks
  *   Initial [[TaskInstance]]s in the queue, if any.
  */
class GreedyPriorityScheduler(initialTasks: TaskInstance*)
    extends PriorityScheduler
    with GreedyScheduler {
  tasks ++= initialTasks
}

/**
  * A strict priority [[Scheduler]].
  *
  * Starts all tasks whose resources are currently idle, in order of priority.
  *
  * Considers resources of higher priority tasks as busy even if they are not. This means that idle
  * resources may be blocked by queued high priority tasks that cannot start yet.
  *
  * @param initialTasks
  *   Initial [[TaskInstance]]s in the queue, if any.
  */
class StrictPriorityScheduler(initialTasks: TaskInstance*)
    extends PriorityScheduler
    with StrictScheduler {
  tasks ++= initialTasks
}

/**
  * The default priority based [[Scheduler]].
  *
  * Relies on the use of [[Schedule]]s for each [[TaskResource]].
  *
  * Avoids scheduling lower priority tasks that might cause delays/waiting times to higher priority
  * ones.
  *
  * @param initialTasks
  *   Initial [[TaskInstance]]s in the queue, if any.
  */
class ProterScheduler(initialTasks: TaskInstance*) extends PriorityScheduler {
  import scala.collection.immutable.Queue

  tasks ++= initialTasks

  /**
    * @inheritdoc
    *
    * Uses [[findNextTasks]].
    * @see
    *   [[findNextTasks]]
    *
    * @param tasks
    *   The queue of [[TaskInstance]]s waiting to be started.
    * @param currentTime
    *   The current timestamp.
    * @param resourceMap
    *   The map of available [[TaskResource]]s.
    * @return
    *   The sequence of [[TaskInstance]]s to start now.
    */
  override def getNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource]
  ): Seq[TaskInstance] =
    findNextTasks(
      currentTime,
      resourceMap,
      resourceMap.view.mapValues(Schedule(_)).toMap,
      tasks,
      Queue()
    )

  /**
    * Finds the [[Task]]s that can be started now.
    *
    * [[Task]]s are assumed to be sorted by priority.
    *
    * Taking each [[TaskInstance]] from high to low priority, the algorithm does the following:
    *   1. It merges the current [[Schedule]]s of all the [[TaskResource]]s involved in the
    *      [[TaskInstance]].
    *   1. It finds the earliest possible starting time of the [[TaskInstance]] in the merged
    *      [[Schedule]].
    *   1. Takes the interval defined by the starting time and the estimated duration of the
    *      [[TaskInstance]] and adds it to the [[Schedule]]s of all the involved [[TaskResource]]s.
    *   1. If the starting time is equal to the current time, and all involved [[TaskResource]]s are
    *      idle, it adds the [[TaskInstance]] to the result.
    *
    * @param currentTime
    *   The current timestamp.
    * @param resourceMap
    *   The map of available [[TaskResource]]s.
    * @param schedules
    *   The map of [[Schedule]]s for each [[TaskResource]].
    * @param tasks
    *   The set of [[TaskInstance]]s that need to start.
    * @param result
    *   The accumulated [[TaskInstance]]s so far (for tail recursion).
    * @return
    *   The sequence of [[TaskInstance]]s to start now.
    */
  @tailrec
  final protected def findNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource],
      schedules: Map[String, Schedule],
      tasks: SortedSet[TaskInstance],
      result: Queue[TaskInstance]
  ): Seq[TaskInstance] =
    if tasks.isEmpty then result
    else {
      val t = tasks.head
      val start = Schedule.mergeSchedules(t.resources.flatMap(schedules.get(_))) ? (currentTime, t)
      val schedules2 = t.resources.foldLeft(schedules) { case (s, r) =>
        s + (r -> (s.getOrElse(r, Schedule()) +> (start, t)))
      }
      val result2 =
        if start == currentTime && t.taskResources(resourceMap).forall(_.isIdle) then result :+ t
        else result
      findNextTasks(currentTime, resourceMap, schedules2, tasks.tail, result2)
    }
}

/**
  * A [[Scheduler]] to be used for look-ahead.
  *
  * Should be used in conjunction with simulations which use the lookahead trait.
  *
  * Relies on the use of [[Schedule]]s for each [[TaskResource]].
  */
class LookaheadScheduler(initialTasks: TaskInstance*) extends PriorityScheduler {
  import scala.collection.immutable.Queue

  tasks ++= initialTasks

  protected val lookaheadObjects: collection.mutable.Map[String, Lookahead] =
    collection.mutable.Map()

  protected val completed: collection.mutable.Set[(UUID, Long)] = collection.mutable.Set()

  /**
    * Adds / updates the lookahead structure corresponding to a particular actor.
    *
    * @param simulation
    *   The name of the simulation that owns the lookahead structure.
    * @param obj
    *   The lookahead structure to be added.
    */
  override def setLookahead(simulation: String, obj: Lookahead): Unit =
    lookaheadObjects += simulation -> obj
  /**
    * Removes the lookahead structure bcorresponding to an actor.
    *
    * @param simulation
    *   The name of the simulation whose lookahead structure should be removed.
    */
  override def removeLookahead(simulation: String): Unit = lookaheadObjects -= simulation

  /**
    * @inheritdoc
    */
  override def removeSimulation(simulation: String): Unit = {
    super.removeSimulation(simulation)
    removeLookahead(simulation)
  }

  /**
    * @inheritdoc
    */
  override def complete(task: TaskInstance, time: Long): Unit = completed += ((task.id, time))

  /**
    * @inheritdoc
    *
    * Uses [[findNextTasks]].
    * @see
    *   [[findNextTasks]]
    *
    * Finds currently running tasks by using the resourceMap and also considers these for
    * scheduling.
    *
    * @param currentTime
    *   The current timestamp.
    * @param resourceMap
    *   The map of available [[TaskResource]]s.
    * @return
    *   The sequence of [[TaskInstance]]s to start now.
    */
  override def getNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource]
  ): Seq[TaskInstance] = {
    // combine lookahead structures
    val lookaheadSetThisIter = lookaheadObjects.values.fold(NoLookahead) { (a, b) => a and b }
    // get future tasks from currently running tasks
    var futureTasksFoundSoFar = Seq[(UUID, Long)]()
    val inProgressFutureTasks = resourceMap.flatMap { case (_, x) =>
      if !x.currentTask.isDefined then Seq()
      else {
        futureTasksFoundSoFar = futureTasksFoundSoFar :+ (
          (
            x.currentTask.get._2.id,
            x.nextAvailableTimestamp(currentTime)
          )
        )
        tasksAfterThis(
          x.currentTask.get._2.simulation,
          futureTasksFoundSoFar,
          lookaheadSetThisIter
        )
      }
    }
    val t = findNextTasks(
      currentTime,
      resourceMap,
      resourceMap.view.mapValues(Schedule(_)).toMap,
      tasks ++ inProgressFutureTasks,
      Seq(),
      lookaheadSetThisIter,
      Queue()
    )
    t.intersect(tasks.toSeq)
  }

  /**
    * Finds the [[TaskInstance]]s that can be started now.
    *
    * The algorithm is recursive, and works by starting with the original lookahead structure and
    * slowly reducing it as the algorithm progresses. Every time a task is scheduled, the subsequent
    * tasks are discovered via the lookahead structure and are added to the task list, while the
    * structure itself removes these tasks such that a single task cannot be returned more than
    * once.
    *
    * [[TaskInstance]]s are assumed to be sorted by priority.
    *
    * Taking each [[TaskInstance]] from high to low priority, the algorithm does the following:
    *   1. It merges the current [[Schedule]]s of all the [[TaskResource]]s involved in the
    *      [[Task]].
    *   1. It finds the earliest possible starting time of the [[TaskInstance]] in the merged
    *      [[Schedule]], using the task's minimum starting time.
    *   1. It finds the tasks that will start after the [[TaskInstance]] using the lookahead
    *      structure, and updates the structure.
    *   1. Takes the interval defined by the starting time and the estimated duration of the
    *      [[TaskInstance]] and adds it to the [[Schedule]]s of all the involved [[TaskResource]]s.
    *   1. If the starting time is equal to the current time, and all involved [[TaskResource]]s are
    *      idle, it adds the [[TaskInstance]] to the result.
    *
    * @param currentTime
    *   The current timestamp.
    * @param resourceMap
    *   The map of available [[TaskResource]]s.
    * @param schedules
    *   The map of [[Schedule]]s for each [[TaskResource]].
    * @param tasks
    *   The set of [[TaskInstance]]s that need to be considered.
    * @param scheduledThisIter
    *   The set of [[TaskInstance]]s that have been scheduled so far and the times at which they are
    *   scheduled.
    * @param lookaheadSetThisIter
    *   The lookahead structure to be used. Is reduced over time as tasks are scheduled.
    * @param result
    *   The accumulated [[TaskInstance]]s so far (for tail recursion).
    * @return
    *   The sequence of [[TaskInstance]]s to start now.
    */
  @tailrec
  final protected def findNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource],
      schedules: Map[String, Schedule],
      tasks: SortedSet[TaskInstance],
      scheduledThisIter: Seq[(UUID, Long)],
      lookaheadSetThisIter: Lookahead,
      result: Queue[TaskInstance]
  ): Seq[TaskInstance] =
    if tasks.isEmpty then result
    else {
      val t = tasks.head
      val start = Schedule.mergeSchedules(t.resources.flatMap(schedules.get(_))) ? (Math.max(
        currentTime,
        t.minStartTime
      ), t)
      val scheduledThisIter2 = scheduledThisIter :+ ((t.id, start + t.estimatedDuration))
      val lookaheadSetThisIter2 = lookaheadSetThisIter - t.id
      val futureTasks = tasksAfterThis(t.simulation, scheduledThisIter2, lookaheadSetThisIter2)
      val schedules2 = t.resources.foldLeft(schedules) { case (s, r) =>
        s + (r -> (s.getOrElse(r, Schedule()) +> (start, t)))
      }
      val result2 =
        if start == currentTime && t.taskResources(resourceMap).forall(_.isIdle) then result :+ t
        else result
      findNextTasks(
        currentTime,
        resourceMap,
        schedules2,
        tasks.tail ++ futureTasks,
        scheduledThisIter2,
        lookaheadSetThisIter2,
        result2
      )
    }

  /**
    * Finds the tasks that can start once the specified tasks have been scheduled.
    *
    * @param simulation
    *   The name of the simulation associated with the task.
    * @param id
    *   The id of the completed task.
    * @param scheduled
    *   The set of already scheduled tasks and corresponding starting times.
    * @param lookaheadStructureThisIter
    *   The lookahead structure to be queried about future tasks.
    * @return
    *   The set of future tasks that can start.
    */
  private def tasksAfterThis(
      simulation: String,
      scheduled: Seq[(UUID, Long)],
      lookaheadStructureThisIter: Lookahead
  ): Seq[TaskInstance] = {
    val taskData = lookaheadStructureThisIter.getTaskData((scheduled ++ completed))
    (taskData map (x => x._1.withMinStartTime(x._2).create(simulation, x._1.createTime))).toSeq
    // todo warning if time <= currentTime. coordinator could pass log. Logging adaptor?
  }
}
