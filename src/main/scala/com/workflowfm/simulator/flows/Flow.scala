package com.workflowfm.simulator.flows

import com.workflowfm.simulator._
//import com.workflowfm.simulator.flows._
import scala.concurrent.{ ExecutionContext, Future, Promise }
import akka.actor.{ Actor, ActorRef, Props }
import java.util.UUID

sealed trait Flow {
     def +(f:Flow) = And(this,f)
     def >(f:Flow) = Then(this,f)
     def |(f:Flow) = Or(this,f)
}
case object NoTask extends Flow
case class FlowTask(generator:TaskGenerator, resources:Seq[String]) extends Flow { val p = Promise[(Task,Long)]() }
case class Just(flowTask:FlowTask) extends Flow
case class Then(left:Flow,right:Flow) extends Flow
case class And(left:Flow,right:Flow) extends Flow
case class All(elements:Flow*) extends Flow
case class Or(left:Flow,right:Flow) extends Flow

class FlowSimulationActor (
    name: String,
    coordinator: ActorRef,
    flow: Flow) 
(implicit executionContext: ExecutionContext)
extends SimulationActor(name,coordinator) {
    
    override def run() = {
        val invis = FlowTask(TaskGenerator("initialise","",ConstantGenerator(0L),ConstantGenerator(0L)),Seq.empty[String])
        def execute(flow:Flow):Future[Any] = {
            flow match {
                case NoTask => Future.unit

                case flowTask: FlowTask => { 
                    task(flowTask.generator, flowTask.resources:_*) map { x=> flowTask.p success x}
                    flowTask.p.future
                }

                case Just(flowTask) => { execute(Then(flowTask,NoTask)) }

                case Then(left,right) => {
                    val leftFuture = execute(left)
                    ready(Seq.empty[UUID])
                    val rightFuture = leftFuture.flatMap{ x =>
                        val future = execute(right)
                        left match {
                            case f:FlowTask => f.p.future map {x=> ready(Seq(x._1.id))}
                            case _ => ready(Seq.empty[UUID])
                        }
                        future
                    }
                    right match {
                            case f:FlowTask => f.p.future map {x=> ready(Seq(x._1.id))}
                            case _ => rightFuture map {_=> ready(Seq.empty[UUID]) }
                        }
                    rightFuture
                }  

                case And(left,right) => {
                    val leftFuture = execute(left)
                    val rightFuture = execute(right)
                    ready(Seq.empty[UUID])
                    left match {
                            case f:FlowTask => f.p.future map {x=> ready(Seq(x._1.id))}
                            case _ => leftFuture map {_=> ready(Seq.empty[UUID]) }
                        }
                    right match {
                            case f:FlowTask => f.p.future map {x=> ready(Seq(x._1.id))}
                            case _ => rightFuture map {_=> ready(Seq.empty[UUID]) }
                        }
                    Future.sequence(Seq(leftFuture,rightFuture))
                }

                case All(elem@_*) => { execute(elem.fold(NoTask) { (l, r) =>  And(l,r) }) }

                case Or(left,right) => {
                    val leftFuture = execute(left)
                    val rightFuture = execute(right)
                    val orDone = Promise[Any]
                    ready(Seq.empty[UUID])
                    leftFuture map { x=> 
                        if (!rightFuture.isCompleted) { orDone success Unit}
                        left match {
                                case f:FlowTask => f.p.future map {x=> ready(Seq(x._1.id))}
                                case _ => ready(Seq.empty[UUID])
                            }
                    }
                    rightFuture map { x=> 
                        if (!leftFuture.isCompleted) { orDone success Unit}
                        right match {
                                case f:FlowTask => f.p.future map {x=> ready(Seq(x._1.id))}
                                case _ => ready(Seq.empty[UUID])
                            }
                    }
                    orDone.future
                }

            }
        }
        execute(Then(invis,flow))
    }
}

object FlowSimulationActor {
    def props (
        name: String,
        coordinator: ActorRef,
        flow: Flow)
    (implicit executionContext: ExecutionContext): Props =
        Props(new FlowSimulationActor(name, coordinator, flow))
}