package com.workflowfm.simulator

import scala.collection.mutable.Queue
import com.workflowfm.simulator.metrics._

object TaskResource {
  sealed trait State
  case object Busy extends State
  case class Finished(t:Task) extends State
  case object Idle extends State
}

class TaskResource(val name:String,val costPerTick:Int) { 
  var currentTask :Option[(Long,Task)] = None
  
  def isIdle :Boolean = currentTask == None 
  
  def finishTask(currentTime:Long) :Option[Task] = currentTask match {
    case None => None
    case Some((startTime,task)) => 
      if (currentTime >= startTime + task.duration) {
        currentTask = None
        Some(task)
      }
      else None
  }
  
  def startTask(task:Task,currentTime:Long) = {
    currentTask match {
      case None => {
        currentTask = Some(currentTime,task)
        None
      }
      case Some((_,currentTask)) => {
        Some(currentTask)
      }
    }
  }
  
  
  def nextAvailableTimestamp(currentTime:Long) :Long = currentTask match {
    case None => currentTime
    case Some((startTime,t)) => {
      startTime + t.estimatedDuration
    }
  }
}
