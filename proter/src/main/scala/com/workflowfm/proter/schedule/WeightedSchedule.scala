package com.workflowfm.proter
package schedule

import scala.annotation.tailrec


/**
  * A list of time intervals indicating the amount of used capacity of a [[TaskResource]]. An 
  8 empty time interval also indicates that no amount of capacity is being used in that interval.
  *
  * Each interval is represented as a pair of timestamps and a number indicating the amount of 
  * capacity used.
  * @example Schedule(List( (1L,2L, 2), (3L,4L, 2) ))
  * // The corresponding [[TaskResource]] is scheduled to have `1` capacity being used between 
  * `1L` and `2L` and `2` capacity used between `3L` and `4L`, and idle (no capacity used) 
  * between `0L` and `1L`, between `2L` and `3L` and after `4L`.
  *
  * @param tasks Busy time intervals and capacity usage as pairs of timestamps and an integer.
  */
case class WeightedSchedule(tasks: List[(Long, Long, Int)]) {

  def +(start: Long, end: Long, usage: Int): Option[WeightedSchedule] = WeightedSchedule.add(start, end, usage, tasks) match {
    case None => None
    case Some(l) => Some(copy(tasks = l))
  }

  def +>(start: Long, end: Long, usage: Int): WeightedSchedule = WeightedSchedule.add(start, end, usage, tasks) match {
    case None => {
      System.err.println(s"*** Unable to add ($start,$end) to Schedule: $tasks")
      this
    }
    case Some(l) => copy(tasks = l)
  }

  def +>(startTime: Long, t: TaskInstance, usage: Int): WeightedSchedule = {
    this +> (startTime, startTime + t.estimatedDuration, usage)
  }

  def binary(targetCapacity: Int, resourceCapacity: Int): Schedule = WeightedSchedule.getBinarySchedule(tasks,targetCapacity,resourceCapacity)


}

/**
  * Contains all key functions for managing [[WeightedSchedule]]s.
  */
object WeightedSchedule {
  import scala.collection.immutable.Queue

  /**
    * Shorthand for an empty schedule.
    *
    * @return An empty [[WeightedSchedule]].
    */
  def apply(): WeightedSchedule = WeightedSchedule(List.empty[(Long, Long, Int)])

  def apply(r: ResourceState): WeightedSchedule =
    r.currentTasks.values.foldLeft(WeightedSchedule()){ case (s, (start, task)) => 
      s +> (start, task, task.resourceQuantity(r.resource.name))}


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
        else if (r == start && u == usage ) add(l, end, u, t, result)
        else if (r == start) add(start, end, usage, t, result :+ ((l, r, u)))
        else { //overlap
          // overlapping segments of schedule "{}" and "[]" look like this: [ A { B ] C } or [ A { B } C ]
          val times: List[Long] = List(start, l, end, r).sorted
          val usages: List[Int] = List(
            if (start < l) usage else u,
            usage + u,
            if (end < r) u else usage
          )
          var timeline = result // TODO wtf? fix
          if (times(0)!=times(1)) timeline = timeline :+ (times(0), times(1), usages(0))
          timeline = timeline :+ (times(1), times(2), usages(1))
          if (times(2)!=times(3)) add(times(2), times(3), usages(2), t, timeline)
          else Some(timeline.toList)
        }
    }
  }

  def getBinarySchedule(
    tasks: List[(Long, Long, Int)],
    targetCapacity: Int,
    resourceCapacity: Int    
  ): Schedule = {
    tasks.filter(_._3+targetCapacity > resourceCapacity).foldLeft(Schedule()){(s, t) => s +> (t._1, t._2)}
  }
}
