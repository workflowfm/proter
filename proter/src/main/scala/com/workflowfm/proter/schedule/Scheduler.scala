package com.workflowfm.proter
package schedule

import java.util.UUID

import scala.annotation.tailrec
import scala.collection.immutable.{ Map, SortedSet, Queue }

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
      tasks: Iterable[TaskInstance],
      resourceMap: ResourceMap
  ): Seq[TaskInstance]

}

/**
  * A greedy [[Scheduler]].
  *
  * Starts all tasks whose resources are currently idle, in the given order/priority.
  *
  * This means a lower priority task may start now and block a higher priority task which is
  * currently blocked, but could have started soon.
  */
case class GreedyScheduler(strict: Boolean) extends Scheduler {

  override def getNextTasks(
      currentTime: Long,
      tasks: Iterable[TaskInstance],
      resourceMap: ResourceMap
  ): Seq[TaskInstance] = {
    findNextTasks(resourceMap.getIdle(), tasks, Queue())
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
      idleResources: ResourceMap,
      tasks: Iterable[TaskInstance],
      result: Queue[TaskInstance]
  ): Seq[TaskInstance] =
    if tasks.isEmpty then result
    else {
      val t = tasks.head
      if t.resources.forall(idleResources.resources.contains) then
        findNextTasks(idleResources -- t.resources, tasks.tail, result :+ t)
      else if strict then findNextTasks(idleResources -- t.resources, tasks.tail, result)
      else findNextTasks(idleResources, tasks.tail, result)
    }
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
case object ProterScheduler extends Scheduler {

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
      tasks: Iterable[TaskInstance],
      resourceMap: ResourceMap
  ): Seq[TaskInstance] =
    findNextTasks(
      currentTime,
      resourceMap,
      resourceMap.resources.view.mapValues(Schedule(_)).toMap,
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
      resourceMap: ResourceMap,
      schedules: Map[String, Schedule],
      tasks: Iterable[TaskInstance],
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
        if start == currentTime && resourceMap.get(t).forall(_.isIdle) then result :+ t
        else result
      findNextTasks(currentTime, resourceMap, schedules2, tasks.tail, result2)
    }
}

/*
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
 */
