package com.workflowfm.proter
package schedule

import scala.annotation.tailrec

/**
  * A list of time intervals during which a [[Resource]] is scheduled to be busy.
  *
  * Each interval is represented as a pair of timestamps.
  * @example
  *   Schedule(List( (1L,2L), (3L,4L) )) // The corresponding [[Resource]] is scheduled to be busy
  *   between `1L` and `2L` and between `3L` and `4L`, and idle between `0L` and `1L`, between `2L`
  *   and `3L` and after `4L`.
  *
  * @param tasks
  *   Busy time intervals as pairs of timestamps.
  */
case class Schedule(tasks: List[(Long, Long)]) {

  /**
    * Adds another busy interval to the schedule.
    *
    * The new interval can only be added if it does not clash with any of the existing intervals.
    *
    * Uses [[Schedule.add]] for the calculation.
    *
    * @example
    *   Schedule(List((1L,2L), (6L,7L))) + (3,5) == Some(Schedule(List((1,2), (3,5), (6,7))))
    * @example
    *   Schedule(List((1L,3L), (6L,7L))) + (3,5) == Some(Schedule(List((1,5), (6,7))))
    * @example
    *   Schedule(List((1L,4L), (6L,7L))) + (3,5) == None
    *
    * @see
    *   [[Schedule.add]]
    * @param start
    *   The start timestamp of the new interval.
    * @param end
    *   The end timestamp of the new interval.
    * @return
    *   Some updated Schedule or [[scala.None]] upon failure.
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
    * @example
    *   Schedule(List((1L,2L), (6L,7L))) +> (3,5) == Schedule(List((1,2), (3,5), (6,7)))
    * @example
    *   Schedule(List((1L,3L), (6L,7L))) +> (3,5) == Schedule(List((1,5), (6,7)))
    * @example
    *   Schedule(List((1L,4L), (6L,7L))) +> (3,5) == Schedule(List((1,4), (6,7)))
    *
    * @see
    *   [[Schedule.add]]
    * @param start
    *   The start timestamp of the new interval.
    * @param end
    *   The end timestamp of the new interval.
    * @return
    *   The updated schedule, or the same schedule if the update fails.
    */
  def +>(start: Long, end: Long): Schedule = Schedule.add(start, end, tasks) match {
    case None => {
      System.err.println(s"*** Unable to add ($start,$end) to Schedule: $tasks")
      this
    }
    case Some(l) => copy(tasks = l)
  }

  /**
    * Adds the estimated interval of a [[TaskInstance]] to the schedule, if possible.
    *
    * The new interval can only be added if it does not clash with any of the existing intervals.
    *
    * Uses [[Schedule.add]] for the calculation. Upon failure, returns the schedule unchanged.
    *
    * @see
    *   [[Schedule.+>(start:Long,end:Long)* Schedule.+>]]
    * @see
    *   [[Schedule.add]]
    * @param startTime
    *   The timestamp the [[TaskInstance]] started.
    * @param t
    *   The [[TaskInstance]] to be added.
    * @return
    *   The updated schedule, or the same schedule if the update fails.
    */
  def +>(startTime: Long, t: TaskInstance): Schedule =
    this +> (startTime, startTime + t.estimatedDuration)

  /**
    * Finds the earliest possible start for a [[TaskInstance]] in the schedule.
    *
    * A [[TaskInstance]] fits into the schedule if the interval defined by the start time and the
    * estimated duration of the [[TaskInstance]] has no overlap with any other interval in the
    * schedule.
    *
    * Uses [[Schedule.fit]] for the calculation.
    * @see
    *   [[Schedule.fit]]
    *
    * @param currentTime
    *   The current (and thus earliest possible) time for the [[TaskInstance]].
    * @param t
    *   The [[TaskInstance]] to be checked.
    * @return
    *   The earliest possible start for the [[TaskInstance]] in this schedule.
    */
  def ?(currentTime: Long, t: TaskInstance): Long =
    Schedule.fit(currentTime, t.estimatedDuration, tasks)

  /**
    * Merges the schedule with another one.
    *
    * The merge involves adding all the busy intervals into a single schedule.
    *
    * Uses [[Schedule.merge]] for the calculation.
    * @see
    *   [[Schedule.merge]]
    * @example
    *   Schedule(List((1L,2L), (5L,6L))) ++ Schedule(List((3L,4L))) == Schedule(List((1,2), (3,4),
    *   (5,6))
    * @example
    *   Schedule(List((1L,2L), (3L,5L))) ++ Schedule(List((2L,4L))) == Schedule(List((1,5)))
    *
    * @param s
    *   The other schedule to merge.
    * @return
    *   The new merged schedule.
    */
  def ++(s: Schedule): Schedule = Schedule(Schedule.merge(tasks, s.tasks))

  /**
    * Checks if this is a valid schedule.
    *
    * A valid schedule contains valid, ordered intervals, that do not overlap. Valid intervals have
    * a start that is earlier than their end.
    *
    * Uses [[Schedule.isValid]] for the calculation.
    * @see
    *   [[Schedule.isValid]]
    *
    * @return
    */
  def isValid: Boolean = Schedule.isValid(tasks)
}

/**
  * Contains all key functions for managing [[Schedule]]s.
  */
object Schedule {
  import scala.collection.immutable.Queue

  /**
    * Shorthand for an empty schedule.
    *
    * @return
    *   An empty [[Schedule]].
    */
  def apply(): Schedule = Schedule(List.empty[(Long, Long)])

  /**
    * Creates a [[Schedule]] from a [[ResourceState]] based on its currently running
    * [[TaskInstance]] (if any).
    *
    * @param r
    *   The [[ResourceState]] to schedule for.
    * @return
    *   The initialised schedule.
    */
  def apply(r: ResourceState): Schedule = r.currentTask match {
    case None => Schedule()
    case Some((s, t)) => Schedule((s, s + t.estimatedDuration) :: Nil)
  }

  /**
    * Adds an interval to a list of intervals.
    *
    * Fails and returns [[scala.None]] if the new interval clashes with any of the existing
    * intervals, i.e. there is overlapping time.
    *
    * @example
    *   add(3, 5, List((1,2), (6,7))) == Some(List((1,2), (3,5), (6,7)))
    * @example
    *   add(3, 5, List((1,3), (6,7))) == Some(List((1,5), (6,7)))
    * @example
    *   add(3, 5, List((1,4), (6,7))) == None
    *
    * @see
    *   [[Schedule.+]]
    * @see
    *   [[Schedule.+>(start:Long,end:Long)* Schedule.+>]]
    * @param start
    *   The start timestamp of the interval.
    * @param end
    *   The end timestamp of the interval.
    * @param tasks
    *   The list of intervals to add to.
    * @param result
    *   The accumulated result so far (for tail recursion).
    * @return
    *   Some updated list of intervals, or [[scala.None]] if there was an overlap.
    */
  @tailrec
  def add(
      start: Long,
      end: Long,
      tasks: List[(Long, Long)],
      result: Queue[(Long, Long)] = Queue[(Long, Long)]()
  ): Option[List[(Long, Long)]] = tasks match {
    case Nil => Some((result :+ (start, end)).toList)
    case (l: Long, r: Long) :: t =>
      if l > end then Some(result.concat((start, end) :: (l, r) :: t).toList)
      else if l == end then Some(result.concat((start, r) :: t).toList)
      else if r < start then add(start, end, t, result :+ ((l, r)))
      else if r == start then add(l, end, t, result)
      else /* if (r >= end) */ None
    // else None
  }

  /**
    * Finds the earliest time when a fixed task duration can fit into a list of busy intervals.
    *
    * A fixed duration fits at a particular starting time `t` if the interval `(t, t+duration)` has
    * no overlap with the existing intervals in the list. It can '''always''' fit after the end of
    * the last interval in the list, but the function returns the earliest possible start.
    *
    * @example
    *   fit(0, 2, List((0,2), (3,4), (6,7))) == 4
    * @example
    *   fit(0, 2, List((0,2), (3,4), (5,7))) == 7
    * @example
    *   fit(0, 2, List((2,4), (5,7))) == 0
    *
    * @see
    *   [[Schedule.?]]
    * @param start
    *   The initial starting time to check for.
    * @param duration
    *   The task duration to consider.
    * @param tasks
    *   The list of intervals to check against.
    * @return
    *   The earliest possible start for the given task duration.
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
    * Intervals that overlap partially or fully, or are adjacent (the end time of one is the start
    * time of the other) are merged into one interval.
    *
    * @example
    *   merge(List((1,2), (5,6)), List((3,4))) == List((1,2), (3,4), (5,6))
    * @example
    *   merge(List((1,2), (3,5)), List((2,4))) == List((1,5))
    *
    * @see
    *   [[Schedule.++]]
    * @param g1
    *   The first list of intervals to merge.
    * @param g2
    *   The second list of intervals to merge.
    * @param result
    *   The accumulated result so far (for tail recursion).
    * @return
    *   The merged list of intervals.
    */
  @tailrec
  def merge(
      g1: List[(Long, Long)],
      g2: List[(Long, Long)],
      result: Queue[(Long, Long)] = Queue[(Long, Long)]()
  ): List[(Long, Long)] = g1 match {
    case Nil => result.concat(g2).toList
    case (l1, r1) :: t1 =>
      g2 match {
        case Nil => result.concat(g1).toList
        case (l2, r2) :: t2 => {
          if r2 < l1 then merge(g1, t2, result :+ (l2, r2))
          else if r1 < l2 then merge(t1, g2, result :+ (l1, r1))
          else if r1 == r2 then merge(t1, t2, result :+ (math.min(l1, l2), r1))
          else if r2 == l1 then merge((l2, r1) :: t1, t2, result)
          else if r1 == l2 then merge(t1, (l1, r2) :: t2, result)
          else if r1 < r2 then merge(t1, (math.min(l1, l2), r2) :: t2, result)
          else /* if (r1 > r2)*/ merge((math.min(l1, l2), r1) :: t1, t2, result)
        }
      }
  }

  /**
    * Merges a sequence of [[Schedule]]s using [[Schedule.++]].
    *
    * @param schedules
    *   The sequence of schedules to merge.
    * @return
    *   The merged schedule.
    */
  def mergeSchedules(schedules: Seq[Schedule]): Schedule = {
    schedules.foldLeft(Schedule())(_ ++ _)
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
      if l == start && end == r then fitInGaps(start, end, t, result) // event fits exactly
      else if l == start && end <= r then fitInGaps(start, end, t, result :+ ((end, r))) // add an event at the beginning of the gap
      else if l <= start && end == r then fitInGaps(start, end, t, result :+ ((l, start))) // add an event at the end of the gaps
      else if l < start && end < r then
        fitInGaps(start, end, t, result :+ ((l, start)) :+ ((end, r))) // add an event within a gap
      else if start > r || end < l then fitInGaps(start, end, t, result :+ ((l, r)))
      else None
  }

  @deprecated("No longer using gaps in Schedule", "1.2.0")
  // we assume all gap lists finish with a (t,Long.MaxValue) gap
  def mergeGaps(
      g1: List[(Long, Long)],
      g2: List[(Long, Long)],
      result: Queue[(Long, Long)] = Queue[(Long, Long)]()
  ): List[(Long, Long)] = g1 match {
    case Nil => result.toList
    case (l1, r1) :: t1 =>
      g2 match {
        case Nil => result.toList
        case (l2, r2) :: t2 => {
          if r2 <= l1 then mergeGaps(g1, t2, result)
          else if r1 <= l2 then mergeGaps(t1, g2, result)
          else if r1 == Long.MaxValue && r1 == r2 then (result :+ (math.max(l1, l2), r1)).toList
          else if r2 <= r1 then mergeGaps(g1, t2, result :+ (math.max(l1, l2), r2))
          else /* if (r1 < r2) */ mergeGaps(t1, g2, result :+ (math.max(l1, l2), r1))
        }
      }
  }

  /**
    * Checks if a list of intervals is valid
    *
    * For it to be valid it must:
    *   1. Contain valid intervals, i.e. intervals with a start that is strictly before the end.
    *   1. The intervals are strictly ordered, i.e. the start of an interval is always strictly
    *      after the end of the previous one.
    *
    * @param gaps
    *   The list of intervals.
    * @param end
    *   The current end timestamp to check against (for tail recursion).
    * @return
    */
  @tailrec
  def isValid(gaps: List[(Long, Long)], end: Long = Long.MinValue): Boolean = gaps match {
    case Nil => true
    case (l, r) :: t if end < l && l < r => isValid(t, r)
    case _ => false
  }
}
