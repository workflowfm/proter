package com.workflowfm.simulator

import scala.annotation.tailrec
import akka.actor.ActorRef
import scala.collection.Map
import scala.collection.mutable.SortedSet

/**
  * A scheduler selects the next [[Task]]s to be started by the [[Coordinator]] at a given time.
  */
trait Scheduler {

  /**
    * Determines which [[Task]]s to start next.
    *
    * [[Task]]s are assumed to be sorted by priority.
    *
    * @param tasks The queue of [[Task]]s waiting to be started.
    * @param currentTime The current timestamp.
    * @param resourceMap The map of available [[TaskResource]]s.
    * @return The sequence of [[Task]]s to start now.
    */
  def getNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource]
  ): Seq[Task]

  /**
    * Checks if a named [[TaskResource]] is idle.
    *
    * @param r The name of the [[TaskResource]].
    * @param resourceMap The map of available [[TaskResource]]s.
    * @return true if the resource is idle, false otherwise.
    */
  def isIdleResource(r: String, resourceMap: Map[String, TaskResource]) = resourceMap.get(r) match {
    case None => false
    case Some(s) => s.isIdle
  }

  /**
    * Sets the lookahead structure for the specified actor.
    *
    * @param actor The actor that created this lookahead structure.
    * @param obj The lookahead structure.
    */
  def setLookahead(actor: ActorRef, obj: Lookahead): Unit = Unit

  /**
    * Removes the lookahead structure associated with the given actor.
    *
    * @param actor The actor corresponding to the lookahead structure.
    */
  def removeLookahead(actor: ActorRef): Unit = Unit

  /**
    * Adds an Task described by an (ID,time) pair to the list of completed IDs
    *
    * @param id The ID to be added
    * @param time The time at which the task completed
    * @return A LookaheadStructure with this (ID,time) pair added to the list of completed tasks
    */
  def complete(task: Task, time: Long): Unit = Unit

  /**
    * Adds a [[Task]] to be scheduled.
    *
    * @param task The [[Task]] to add.
    */
  def addTask(task: Task): Unit

  /**
    * Removes a [[Task]] that no longer needs scheduling.
    *
    * @param task The [[Task]] to remove.
    */
  def removeTask(task: Task): Unit

  /**
    * Removes all [[Task]]s belonging to an (presumably aborted) simulation.
    *
    * @param simulation The name of the simulation that was aborted.
    * @param actor The simulation actor responsible for this simulation
    */
  def removeSimulation(simulation: String, actor: ActorRef): Unit

  /**
    * Checks if all [[Task]]s have been scheduled.
    *
    * @return true if there are no [[Task]]s remaining.
    */
  def noMoreTasks(): Boolean

}

trait SortedSetScheduler extends Scheduler {
  /**
    * A sorted queue of tasks that need to be run.
    */
  val tasks: SortedSet[Task] = SortedSet()

  /**
    * @inheritdoc
    */
  override def addTask(task: Task): Unit = tasks += task

  /**
    * @inheritdoc
    */
  override def removeTask(task: Task): Unit = tasks -= task

  /**
    * @inheritdoc
    */
  override def removeSimulation(simulation: String, actor: ActorRef): Unit = 
    tasks --= tasks.filter(_.simulation == simulation)

  /**
    * @inheritdoc
    */
  override def noMoreTasks(): Boolean = tasks.isEmpty

}

/**
  * A [[Scheduler]] to be used as the default.
  *
  * Relies on the use of [[Schedule]]s for each [[TaskResource]].
  *
  * @param initialTasks Initial [[Task]]s in the queue, if any.
  */
class DefaultScheduler(initialTasks: Task*) extends SortedSetScheduler {
  import scala.collection.immutable.Queue

  tasks ++= initialTasks

  /**
    * @inheritdoc
    *
    * Uses [[DefaultScheduler.findNextTasks]].
    * @see [[DefaultScheduler.findNextTasks]]
    *
    * @param tasks The queue of [[Task]]s waiting to be started.
    * @param currentTime The current timestamp.
    * @param resourceMap The map of available [[TaskResource]]s.
    * @return The sequence of [[Task]]s to start now.
    */
  override def getNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource]
  ): Seq[Task] =
    findNextTasks(currentTime, resourceMap, resourceMap.mapValues(Schedule(_)), tasks, Queue())

  /**
    * Finds the [[Task]]s that can be started now.
    *
    * [[Task]]s are assumed to be sorted by priority.
    *
    * Taking each [[Task]] from high to low priority, the algorithm does the following:
    *   1. It merges the current [[Schedule]]s of all the [[TaskResource]]s involved in the [[Task]].
    *   1. It finds the earliest possible starting time of the [[Task]] in the merged [[Schedule]].
    *   1. Takes the interval defined by the starting time and the estimated duration of the [[Task]]
    * and adds it to the [[Schedule]]s of all the involved [[TaskResource]]s.
    *   1. If the starting time is equal to the current time, and all involved [[TaskResource]]s are
    * idle, it adds the [[Task]] to the result.
    *
    * @param currentTime The current timestamp.
    * @param resourceMap The map of available [[TaskResource]]s.
    * @param schedules The map of [[Schedule]]s for each [[TaskResource]].
    * @param tasks The set of [[Task]]s that need to start.
    * @param result The accumulated [[Task]]s so far (for tail recursion).
    * @return The sequence of [[Task]]s to start now.
    */
  @tailrec
  private def findNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource],
      schedules: Map[String, Schedule],
      tasks: SortedSet[Task],
      result: Queue[Task]
  ): Seq[Task] =
    if (tasks.isEmpty) result
    else {
      val t = tasks.head
      val start = Schedule.mergeSchedules(t.resources.flatMap(schedules.get(_))) ? (currentTime, t)
      val schedules2 = (schedules /: t.resources) {
        case (s, r) => s + (r -> (s.getOrElse(r, Schedule()) +> (start, t)))
      }
      val result2 =
        if (start == currentTime && t.taskResources(resourceMap).forall(_.isIdle)) result :+ t
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
class LookaheadScheduler(initialTasks: Task*) extends SortedSetScheduler {
  import scala.collection.immutable.Queue
  import scala.concurrent.{ Await, ExecutionContext, Future }

  tasks ++= initialTasks

  protected val lookaheadObjects: collection.mutable.Map[ActorRef, Lookahead] =
    collection.mutable.Map()
  protected val completed: collection.mutable.Set[(java.util.UUID, Long)] = collection.mutable.Set()

  /**
    * Adds / updates the lookahead structure corresponding to a particular actor.
    *
    * @param actor The actor that owns the lookahead structure.
    * @param obj The lookahead structure to be added.
    */
  override def setLookahead(actor: ActorRef, obj: Lookahead) = lookaheadObjects += actor -> obj
  /**
    * Removes the lookahead structure bcorresponding to an actor.
    *
    * @param actor The actor corresponding to the lookahead structure that should be removed.
    */
  override def removeLookahead(actor: ActorRef): Unit = lookaheadObjects -= actor

  /**
    * @inheritdoc
    */
  override def removeSimulation(simulation: String, actor: ActorRef): Unit = {
    super.removeSimulation(simulation, actor)
    removeLookahead(actor)
  }

  /**
    * @inheritdoc
    */
  override def complete(task: Task, time: Long): Unit = completed += ((task.id, time))

  /**
    * @inheritdoc
    *
    * Uses [[LookaheadScheduler.findNextTasks]].
    * @see [[LookaheadScheduler.findNextTasks]]
    *
    * Finds currently running tasks by using the resourceMap and also considers these
    * for scheduling.
    *
    * @param currentTime The current timestamp.
    * @param resourceMap The map of available [[TaskResource]]s.
    * @return The sequence of [[Task]]s to start now.
    */
  override def getNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource]
  ): Seq[Task] = {
    implicit val executionContext = ExecutionContext.global
    //combine lookahead structures
    var lookaheadSetThisIter = lookaheadObjects.values.fold(NoLookahead) { (a, b) => a and b }
    //get future tasks from currently running tasks
    var futureTasksFoundSoFar = Seq[(java.util.UUID, Long)]()
    val inProgressFutureTasks = resourceMap.flatMap {
      case (_, x) =>
        if (!x.currentTask.isDefined) Seq()
        else {
          futureTasksFoundSoFar = futureTasksFoundSoFar :+ (
                  (
                    x.currentTask.get._2.id,
                    x.nextAvailableTimestamp(currentTime)
                  )
                )
          tasksAfterThis(
            x.currentTask.get._2.actor,
            futureTasksFoundSoFar,
            lookaheadSetThisIter
          )
        }
    }
    val t = findNextTasks(
      currentTime,
      resourceMap,
      resourceMap.mapValues(Schedule(_)),
      tasks ++ inProgressFutureTasks,
      Seq(),
      lookaheadSetThisIter,
      Queue()
    )
    t.intersect(tasks.toSeq)
  }

  /**
    * Finds the [[Task]]s that can be started now.
    *
    * The algorithm is recursive, and works by starting with the original lookahead structure and slowly reducing it
    * as the algorithm progresses. Every time a task is scheduled, the subsequent tasks are discovered via the
    * lookahead structure and are added to the task list, while the structure itself removes these tasks such that
    * a single task cannot be returned more than once.
    *
    * [[Task]]s are assumed to be sorted by priority.
    *
    * Taking each [[Task]] from high to low priority, the algorithm does the following:
    *   1. It merges the current [[Schedule]]s of all the [[TaskResource]]s involved in the [[Task]].
    *   1. It finds the earliest possible starting time of the [[Task]] in the merged [[Schedule]], using the task's minimum starting time.
    *   1. It finds the tasks that will start after the [[Task]] using the lookahead structure, and updates the structure.
    *   1. Takes the interval defined by the starting time and the estimated duration of the [[Task]]
    * and adds it to the [[Schedule]]s of all the involved [[TaskResource]]s.
    *   1. If the starting time is equal to the current time, and all involved [[TaskResource]]s are
    * idle, it adds the [[Task]] to the result.
    *
    * @param currentTime The current timestamp.
    * @param resourceMap The map of available [[TaskResource]]s.
    * @param schedules The map of [[Schedule]]s for each [[TaskResource]].
    * @param tasks The set of [[Task]]s that need to be considered.
    * @param scheduledThisIter The set of [[Task]]s that have been scheduled so far and the times at which they are scheduled.
    * @param lookaheadSetThisIter The lookahead structure to be used. Is reduced over time as tasks are scheduled.
    * @param result The accumulated [[Task]]s so far (for tail recursion).
    * @return The sequence of [[Task]]s to start now.
    */
  @tailrec
  private def findNextTasks(
      currentTime: Long,
      resourceMap: Map[String, TaskResource],
      schedules: Map[String, Schedule],
      tasks: SortedSet[Task],
      scheduledThisIter: Seq[(java.util.UUID, Long)],
      lookaheadSetThisIter: Lookahead,
      result: Queue[Task]
  ): Seq[Task] =
    if (tasks.isEmpty) result
    else {
      val t = tasks.head
      val start = Schedule.mergeSchedules(t.resources.flatMap(schedules.get(_))) ? (Math.max(
              currentTime,
              t.minStartTime
            ), t)
      val scheduledThisIter2 = scheduledThisIter :+ ((t.id, start + t.estimatedDuration))
      val lookaheadSetThisIter2 = lookaheadSetThisIter - t.id
      val futureTasks = tasksAfterThis(t.actor, scheduledThisIter2, lookaheadSetThisIter2)
      val schedules2 = (schedules /: t.resources) {
        case (s, r) => s + (r -> (s.getOrElse(r, Schedule()) +> (start, t)))
      }
      val result2 =
        if (start == currentTime && t.taskResources(resourceMap).forall(_.isIdle)) result :+ t
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
    * @param actor The actor associated with the task.
    * @param id The id of the completed task.
    * @param scheduled The set of already scheduled tasks and corresponding starting times.
    * @param lookaheadStructureThisIter The lookahead structure to be queried about future tasks.
    * @return The set of future tasks that can start.
    */
  private def tasksAfterThis(
      actor: ActorRef,
      scheduled: Seq[(java.util.UUID, Long)],
      lookaheadStructureThisIter: Lookahead
  ): Seq[Task] = {
    val taskData = lookaheadStructureThisIter.getTaskData((scheduled ++ completed))
    (taskData map (x => x._1.withMinStartTime(x._2).create(x._1.createTime, actor))).toSeq
    //todo warning if time <= currentTime. coordinator could pass log. Logging adaptor?
  }
}
