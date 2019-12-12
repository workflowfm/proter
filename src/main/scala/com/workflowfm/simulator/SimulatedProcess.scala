package com.workflowfm.simulator

import akka.pattern.ask
import akka.actor.ActorRef
import akka.util.Timeout
import java.util.concurrent.TimeUnit
import scala.concurrent.duration.Duration
import scala.concurrent.{ ExecutionContext, Future }
import java.util.UUID


trait SimulatedProcess {
  def simulationName: String
  def simulationActor: ActorRef

  def simulate[T](
    gen: TaskGenerator,
    result: (Task, Long) => T,
    resources: String*
  )(implicit executionContext: ExecutionContext): Future[T] = {
    val id = java.util.UUID.randomUUID
    simulate(id,gen,result,resources:_*)
  }

  def simulate[T](
    id: UUID,
    gen: TaskGenerator,
    result: (Task, Long) => T,
    resources: String*
  )(implicit executionContext: ExecutionContext): Future[T] = {
    (simulationActor ? SimulationActor.AddTask(id, gen, resources))(Timeout(1, TimeUnit.DAYS)).
      mapTo[(Task,Long)].
      map { case (task, time) => result(task,time) }
  }
}
