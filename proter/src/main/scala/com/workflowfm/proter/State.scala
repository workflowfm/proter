package com.workflowfm.proter

import cases.CaseRef
//import schedule.Scheduler

import scala.collection.immutable.HashSet
import java.util.UUID


case class State[F[_]](
  time: Long, 
//  scheduler: Scheduler,
  events: EventQueue,
  cases: Map[String, CaseRef[F]], 
  resources: ResourceMap,
  waiting: HashSet[String], 
  abortedTasks: HashSet[UUID],
) {

  def +(r: Resource): State[F] = copy(resources = resources.addResource(r))
  def ++(r: Seq[Resource]): State[F] = copy(resources = resources.addResources(r))


}
