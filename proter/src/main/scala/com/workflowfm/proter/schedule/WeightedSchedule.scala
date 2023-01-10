package com.workflowfm.proter
package schedule

import scala.annotation.tailrec

/**
  * A list of time intervals indicating the amount of used capacity of a [[Resource]].
  *
  * An empty time interval indicates that no amount of capacity is being used in that interval.
  *
  * Each interval is represented as a pair of timestamps and a number indicating the amount of
  * capacity used.
  * @example
  *   Schedule(List( (1L,2L, 2), (3L,4L, 2) )) // The corresponding [[Resource]] is scheduled to
  *   have `1` capacity being used between `1L` and `2L` and `2` capacity used between `3L` and
  *   `4L`, and idle (no capacity used) between `0L` and `1L`, between `2L` and `3L` and after `4L`.
  *
  * @param tasks
  *   Busy time intervals and capacity usage as pairs of timestamps and an integer.
  */
case class WeightedSchedule(tasks: List[(Long, Long, Int)]) {

  def +(start: Long, end: Long, usage: Int): Option[WeightedSchedule] =
    WeightedSchedule.add(start, end, usage, tasks) match {
      case None => None
      case Some(l) => Some(copy(tasks = l))
    }

  def +>(start: Long, end: Long, usage: Int): WeightedSchedule =
    WeightedSchedule.add(start, end, usage, tasks) match {
      case None => {
        System.err.println(s"*** Unable to add ($start,$end) to Schedule: $tasks")
        this
      }
      case Some(l) => copy(tasks = l)
    }

  def +>(startTime: Long, t: TaskInstance, usage: Int): WeightedSchedule = {
    this +> (startTime, startTime + t.estimatedDuration, usage)
  }

  /**
    * Converts to a unweighted [[Schedule]].
    *
    * The resulting [[Schedule]] has intervals during which there is insufficient capacity in the
    * [[Resource]]. This can be used to find when the given [[Resource]] has sufficient capacity for
    * a particular task.
    *
    * @param targetCapacity
    *   The capacity required to consider the [[Resource]] available.
    * @param resourceCapacity
    *   The total [[Resource]] capacity.
    * @return
    *   The unweighted [[Schedule]].
    */
  def binary(targetCapacity: Int, resourceCapacity: Int): Schedule =
    if targetCapacity > resourceCapacity then Schedule.Full
    else
      tasks.filter(_._3 + targetCapacity > resourceCapacity).foldLeft(Schedule()) { (s, t) =>
        s +> (t._1, t._2)
      }

}

/**
  * Contains all key functions for managing [[WeightedSchedule]]s.
  */
object WeightedSchedule {
  import scala.collection.immutable.Queue

  /**
    * Shorthand for an empty schedule.
    *
    * @return
    *   An empty [[WeightedSchedule]].
    */
  def apply(): WeightedSchedule = WeightedSchedule(List.empty[(Long, Long, Int)])

  def apply(r: ResourceState): WeightedSchedule =
    r.currentTasks.values.foldLeft(WeightedSchedule()) { case (s, (start, task)) =>
      s +> (start, task, task.resourceQuantity(r.resource.name))
    }

  @tailrec
  def add(
      start: Long,
      end: Long,
      usage: Int,
      tasks: List[(Long, Long, Int)],
      result: Queue[(Long, Long, Int)] = Queue[(Long, Long, Int)]()
  ): Option[List[(Long, Long, Int)]] = {
    if (usage == 0 || start == end) return Some(tasks);
    tasks match {
      case Nil => Some((result :+ (start, end, usage)).toList)
      case (l: Long, r: Long, u: Int) :: t =>
        if (l > end) Some(result.concat((start, end, usage) :: (l, r, u) :: t).toList)
        else if (l == end && u == usage) Some(result.concat((start, r, u) :: t).toList)
        else if (l == end) Some(result.concat((start, end, usage) :: (l, r, u) :: t).toList)
        else if (r < start) add(start, end, usage, t, result :+ ((l, r, u)))
        else if (r == start && u == usage) add(l, end, u, t, result)
        else if (r == start) add(start, end, usage, t, result :+ ((l, r, u)))
        else { // overlap
          /* overlapping segments of schedule "{}" and "[]" look like this: [ A { B ] C } or [ A { B
           * } C ] */
          val times: List[Long] = List(start, l, end, r).sorted
          val usages: List[Int] = List(
            if (start < l) usage else u,
            usage + u,
            if (end < r) u else usage
          )

          val timeline =
            if (times(0) != times(1)) result :+ (times(0), times(1), usages(0))
            else result

          val finalTimeline = timeline :+ (times(1), times(2), usages(1))

          if (times(2) != times(3)) add(times(2), times(3), usages(2), t, finalTimeline)
          else Some(finalTimeline.toList)
        }
    }
  }

}
