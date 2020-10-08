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
  def setLookahead(actor: ActorRef, obj: Lookahead): Unit = { Unit }

  /**
    * Removes the lookahead structure associated with the given actor.
    *
    * @param actor The actor corresponding to the lookahead structure.
    */
  def removeLookahead(actor: ActorRef): Unit = { Unit }
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
    */
  def removeSimulation(simulation: String): Unit

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
  override def removeSimulation(simulation: String): Unit = 
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
  * A list of time intervals during which a [[TaskResource]] is scheduled to be busy.
  *
  * Each interval is represented as a pair of timestamps.
  * @example Schedule(List( (1L,2L), (3L,4L) ))
  * // The corresponding [[TaskResource]] is scheduled to be busy between `1L` and `2L` and
  * between `3L` and `4L`, and idle between `0L` and `1L`, between `2L` and `3L` and after `4L`.
  *
  * @param tasks Busy time intervals as pairs of timestamps.
  */
case class Schedule(tasks: List[(Long, Long)]) {

  /**
    * Adds another busy interval to the schedule.
    *
    * The new interval can only be added if it does not clash with any of the existing intervals.
    *
    * Uses [[Schedule.add]] for the calculation.
    *
    * @example Schedule(List((1L,2L), (6L,7L))) + (3,5) == Some(Schedule(List((1,2), (3,5), (6,7))))
    * @example Schedule(List((1L,3L), (6L,7L))) + (3,5) == Some(Schedule(List((1,5), (6,7))))
    * @example Schedule(List((1L,4L), (6L,7L))) + (3,5) == None
    *
    * @see [[Schedule.add]]
    * @param start The start timestamp of the new interval.
    * @param end The end timestamp of the new interval.
    * @return Some updated Schedule or [[scala.None]] upon failure.
    */
  def +(start: Long, end: Long): Option[Schedule] = Schedule.add(start, end, tasks) match {
    case None => None
    case Some(l) => Some(copy(tasks = l))
  }

  /**
    * Adds another busy interval to the schedule, if possible.
    *
    * The new interval can only be added if it does not clash with any of the existing intervals.
    *
    * Uses [[Schedule.add]] for the calculation. Upon failure, returns the schedule unchanged.
    *
    * @example Schedule(List((1L,2L), (6L,7L))) +> (3,5) == Schedule(List((1,2), (3,5), (6,7)))
    * @example Schedule(List((1L,3L), (6L,7L))) +> (3,5) == Schedule(List((1,5), (6,7)))
    * @example Schedule(List((1L,4L), (6L,7L))) +> (3,5) == Schedule(List((1,4), (6,7)))
    *
    * @see [[Schedule.add]]
    * @param start The start timestamp of the new interval.
    * @param end The end timestamp of the new interval.
    * @return The updated schedule, or the same schedule if the update fails.
    */
  def +>(start: Long, end: Long): Schedule = Schedule.add(start, end, tasks) match {
    case None => {
      System.err.println(s"*** Unable to add ($start,$end) to Schedule: $tasks")
      this
    }
    case Some(l) => copy(tasks = l)
  }

  /**
    * Adds the estimated interval of a [[Task]] to the schedule, if possible.
    *
    * The new interval can only be added if it does not clash with any of the existing intervals.
    *
    * Uses [[Schedule.add]] for the calculation. Upon failure, returns the schedule unchanged.
    *
    * @see [[Schedule.+>(start:Long,end:Long)* Schedule.+>]]
    * @see [[Schedule.add]]
    * @param startTime The timestamp the [[Task]] started.
    * @param t The [[Task]] to be added.
    * @return The updated schedule, or the same schedule if the update fails.
    */
  def +>(startTime: Long, t: Task): Schedule = this +> (startTime, startTime + t.estimatedDuration)

  /**
    * Finds the earliest possible start for a [[Task]] in the schedule.
    *
    * A [[Task]] fits into the schedule if the interval defined by the start time
    * and the estimated duration of the [[Task]] has no overlap with any other interval in the
    * schedule.
    *
    * Uses [[Schedule.fit]] for the calculation.
    * @see [[Schedule.fit]]
    *
    * @param currentTime The current (and thus earliest possible) time for the [[Task]].
    * @param t The [[Task]] to be checked.
    * @return The earliest possible start for the [[Task]] in this schedule.
    */
  def ?(currentTime: Long, t: Task): Long = Schedule.fit(currentTime, t.estimatedDuration, tasks)

  /**
    * Merges the schedule with another one.
    *
    * The merge involves adding all the busy intervals into a single schedule.
    *
    * Uses [[Schedule.merge]] for the calculation.
    * @see [[Schedule.merge]]
    * @example Schedule(List((1L,2L), (5L,6L))) ++ Schedule(List((3L,4L))) == Schedule(List((1,2), (3,4), (5,6))
    * @example Schedule(List((1L,2L), (3L,5L))) ++ Schedule(List((2L,4L))) == Schedule(List((1,5)))
    *
    * @param s The other schedule to merge.
    * @return The new merged schedule.
    */
  def ++(s: Schedule): Schedule = Schedule(Schedule.merge(tasks, s.tasks))

  /**
    * Checks if this is a valid schedule.
    *
    * A valid schedule contains valid, ordered intervals, that do not overlap.
    * Valid intervals have a start that is earlier than their end.
    *
    * Uses [[Schedule.isValid]] for the calculation.
    * @see [[Schedule.isValid]]
    *
    * @return
    */
  def isValid = Schedule.isValid(tasks)
}

/**
  * Contains all key functions for managing [[Schedule]]s.
  */
object Schedule {
  import scala.collection.immutable.Queue

  /**
    * Shorthand for an empty schedule.
    *
    * @return An empty [[Schedule]].
    */
  def apply(): Schedule = Schedule(List.empty[(Long, Long)])

  /**
    * Creates a [[Schedule]] from a [[TaskResource]] based on its currently running [[Task]] (if any).
    *
    * @param r The [[TaskResource]] to schedule for.
    * @return The initialised schedule.
    */
  def apply(r: TaskResource): Schedule = r.currentTask match {
    case None => Schedule()
    case Some((s, t)) => Schedule((s, s + t.estimatedDuration) :: Nil)
  }

  /**
    * Adds an interval to a list of intervals.
    *
    * Fails and returns [[scala.None]] if the new interval clashes with
    * any of the existing intervals, i.e. there is overlapping time.
    *
    * @example add(3, 5, List((1,2), (6,7))) == Some(List((1,2), (3,5), (6,7)))
    * @example add(3, 5, List((1,3), (6,7))) == Some(List((1,5), (6,7)))
    * @example add(3, 5, List((1,4), (6,7))) == None
    *
    * @see [[Schedule.+]]
    * @see [[Schedule.+>(start:Long,end:Long)* Schedule.+>]]
    * @param start The start timestamp of the interval.
    * @param end The end timestamp of the interval.
    * @param tasks The list of intervals to add to.
    * @param result The accumulated result so far (for tail recursion).
    * @return Some updated list of intervals, or [[scala.None]] if there was an overlap.
    */
  @tailrec
  def add(
      start: Long,
      end: Long,
      tasks: List[(Long, Long)],
      result: Queue[(Long, Long)] = Queue[(Long, Long)]()
  ): Option[List[(Long, Long)]] = tasks match {
    case Nil => Some(result :+ (start, end) toList)
    case (l: Long, r: Long) :: t =>
      if (l > end) Some(result ++ ((start, end) :: (l, r) :: t) toList)
      else if (l == end) Some(result ++ ((start, r) :: t) toList)
      else if (r < start) add(start, end, t, result :+ ((l, r)))
      else if (r == start) add(l, end, t, result)
      else /* if (r >= end) */ None
    //else None
  }

  /**
    * Finds the earliest time when a fixed task duration can fit into a list of busy intervals.
    *
    * A fixed duration fits at a particular starting time `t` if the interval `(t, t+duration)`
    * has no overlap with the existing intervals in the list. It can '''always''' fit after the
    * end of the last interval in the list, but the function returns the earliest possible start.
    *
    * @example fit(0, 2, List((0,2), (3,4), (6,7))) == 4
    * @example fit(0, 2, List((0,2), (3,4), (5,7))) == 7
    * @example fit(0, 2, List((2,4), (5,7))) == 0
    *
    * @see [[Schedule.?]]
    * @param start The initial starting time to check for.
    * @param duration The task duration to consider.
    * @param tasks The list of intervals to check against.
    * @return The earliest possible start for the given task duration.
    */
  @tailrec
  def fit(
      start: Long,
      duration: Long,
      tasks: List[(Long, Long)]
  ): Long = tasks match {
    case Nil => start
    case (l, _) :: _ if (l >= start + duration) => start
    case (_, r) :: t => fit(r, duration, t)
  }

  /**
    * Merges two lists of intervals into one.
    *
    * Intervals that overlap partially or fully, or are adjacent (the end time of one is
    * the start time of the other) are merged into one interval.
    *
    * @example merge(List((1,2), (5,6)), List((3,4))) == List((1,2), (3,4), (5,6))
    * @example merge(List((1,2), (3,5)), List((2,4))) == List((1,5))
    *
    * @see [[Schedule.++]]
    * @param g1 The first list of intervals to merge.
    * @param g2 The second list of intervals to merge.
    * @param result The accumulated result so far (for tail recursion).
    * @return The merged list of intervals.
    */
  @tailrec
  def merge(
      g1: List[(Long, Long)],
      g2: List[(Long, Long)],
      result: Queue[(Long, Long)] = Queue[(Long, Long)]()
  ): List[(Long, Long)] = g1 match {
    case Nil => result ++ g2 toList
    case (l1, r1) :: t1 =>
      g2 match {
        case Nil => result ++ g1 toList
        case (l2, r2) :: t2 => {
          if (r2 < l1) merge(g1, t2, result :+ (l2, r2))
          else if (r1 < l2) merge(t1, g2, result :+ (l1, r1))
          else if (r1 == r2) merge(t1, t2, result :+ (math.min(l1, l2), r1))
          else if (r2 == l1) merge((l2, r1) :: t1, t2, result)
          else if (r1 == l2) merge(t1, (l1, r2) :: t2, result)
          else if (r1 < r2) merge(t1, (math.min(l1, l2), r2) :: t2, result)
          else /* if (r1 > r2)*/ merge((math.min(l1, l2), r1) :: t1, t2, result)
        }
      }
  }

  /**
    * Merges a sequence of [[Schedule]]s using [[Schedule.++]].
    *
    * @param schedules The sequence of schedules to merge.
    * @return The merged schedule.
    */
  def mergeSchedules(schedules: Seq[Schedule]): Schedule = {
    (Schedule() /: schedules)(_ ++ _)
  }

  @deprecated("No longer using gaps in Schedule", "1.2.0")
  def fitInGaps(
      start: Long,
      end: Long,
      gaps: List[(Long, Long)],
      result: Queue[(Long, Long)] = Queue[(Long, Long)]()
  ): Option[List[(Long, Long)]] = gaps match {
    case Nil => Some(result.toList)
    case (l: Long, r: Long) :: t =>
      if (l == start && end == r) fitInGaps(start, end, t, result) // event fits exactly
      else if (l == start && end <= r) fitInGaps(start, end, t, result :+ ((end, r))) // add an event at the beginning of the gap
      else if (l <= start && end == r) fitInGaps(start, end, t, result :+ ((l, start))) // add an event at the end of the gaps
      else if (l < start && end < r) fitInGaps(start, end, t, result :+ ((l, start)) :+ ((end, r))) // add an event within a gap
      else if (start > r || end < l) fitInGaps(start, end, t, result :+ ((l, r)))
      else None
  }

  @deprecated("No longer using gaps in Schedule", "1.2.0")
  // we assume all gap lists finish with a (t,Long.MaxValue) gap
  def mergeGaps(
      g1: List[(Long, Long)],
      g2: List[(Long, Long)],
      result: Queue[(Long, Long)] = Queue[(Long, Long)]()
  ): List[(Long, Long)] = g1 match {
    case Nil => result toList
    case (l1, r1) :: t1 =>
      g2 match {
        case Nil => result toList
        case (l2, r2) :: t2 => {
          if (r2 <= l1) mergeGaps(g1, t2, result)
          else if (r1 <= l2) mergeGaps(t1, g2, result)
          else if (r1 == Long.MaxValue && r1 == r2) result :+ (math.max(l1, l2), r1) toList
          else if (r2 <= r1) mergeGaps(g1, t2, result :+ (math.max(l1, l2), r2))
          else /* if (r1 < r2) */ mergeGaps(t1, g2, result :+ (math.max(l1, l2), r1))
        }
      }
  }

  /**
    * Checks if a list of intervals is valid
    *
    * For it to be valid it must:
    *   1. Contain valid intervals, i.e. intervals with a start that is strictly before the end.
    *   1. The intervals are strictly ordered, i.e. the start of an interval is always strictly after the end of
    * the previous one.
    *
    * @param gaps The list of intervals.
    * @param end The current end timestamp to check against (for tail recursion).
    * @return
    */
  @tailrec
  def isValid(gaps: List[(Long, Long)], end: Long = Long.MinValue): Boolean = gaps match {
    case Nil => true
    case (l, r) :: t if end < l && l < r => isValid(t, r)
    case _ => false
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
