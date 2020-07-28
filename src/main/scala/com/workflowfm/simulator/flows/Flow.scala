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
case class Wrapper(flow:Flow) extends Flow
case class FlowTask(generator:TaskGenerator, resources:Seq[String]) extends Flow { val p = Promise[(Task,Long)]() }
case class Just(flowTask:FlowTask) extends Flow
case class Then(left:Flow,right:Flow) extends Flow
case class And(left:Flow,right:Flow) extends Flow
case class All(elements:Flow*) extends Flow
case class Or(left:Flow,right:Flow) extends Flow

/*
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
                case Wrapper(flow) => {
                    val e = execute(Then(invis,flow))
                    e.onComplete{x=> ready()}
                    e
                }

                case NoTask => Future.unit

                case flowTask: FlowTask => { 
                    task(flowTask.generator, flowTask.resources:_*) map { x=> flowTask.p success x}
                    flowTask.p.future
                }

                case Just(flowTask) => { execute(Then(flowTask,NoTask)) }

                case Then(left,right) => {
                    val id = java.util.UUID.randomUUID
                    waitForAck(id)
                    val leftFuture = execute(left)
                    ack(Seq(id))
                    ready()
                    val rightFuture = leftFuture.flatMap{ x =>
                        waitForAck(id)
                        val future = execute(right)
                        ack(Seq(id))
                        left match {
                            case f:FlowTask => f.p.future map {x=> ack(Seq(x._1.id)); ready()}
                            case _ => ready()
                        }
                        future
                    }
                    rightFuture flatMap { _=>
                        right match {
                                case f:FlowTask => f.p.future map {x=> ack(Seq(x._1.id))}
                                case _ => ack(Seq())
                            }
                        
                    }
                }  

                case And(left,right) => {
                    val id = java.util.UUID.randomUUID
                    waitForAck(id)
                    val leftFuture = execute(left)
                    val rightFuture = execute(right)
                    ack(Seq(id))
                    ready()
                    val leftFuture2 = leftFuture flatMap { x=> 
                        val f = left match {
                                case f:FlowTask => f.p.future map {x=> ack(Seq(x._1.id))}
                                case _ => ack(Seq())
                            }
                        f map {_=> if (!rightFuture.isCompleted) ready()}
                        f
                    }
                    val rightFuture2 = rightFuture flatMap { x=> 
                        val f = right match {
                                case f:FlowTask => f.p.future map {x=> ack(Seq(x._1.id))}
                                case _ => ack(Seq())
                            }
                        f map {_=> if (!leftFuture.isCompleted) ready()}
                        f
                    }
                    Future.sequence(Seq(leftFuture2,rightFuture2))
                }

                case All(elem@_*) => { execute(elem.fold(NoTask) { (l, r) =>  And(l,r) }) }

                case Or(left,right) => {
                    val leftFuture = execute(left)
                    val rightFuture = execute(right)
                    val orDone = Promise[Any]
                    ack(Seq.empty[UUID])
                    val leftFuture2 = leftFuture flatMap { x=> 
                        val f =left match {
                                case f:FlowTask => f.p.future map {x=> ack(Seq(x._1.id))}
                                case _ => ack(Seq())
                            }
                        f map {_=> if (!rightFuture.isCompleted) orDone success Unit else ready() }
                        f
                    }
                    val rightFuture2 = rightFuture flatMap { x=> 
                        val f = right match {
                                case f:FlowTask => f.p.future map {x=> ack(Seq(x._1.id))}
                                case _ => ack(Seq())
                            }
                        f map {_=> if (!leftFuture.isCompleted) orDone success Unit else ready() }
                        f
                    }
                    orDone.future
                }

            }
        }
        //execute(Then(invis,flow))
        execute(Wrapper(flow))
    }
}

object FlowSimulationActor {
    def props (
        name: String,
        coordinator: ActorRef,
        flow: Flow)
    (implicit executionContext: ExecutionContext): Props =
        Props(new FlowSimulationActor(name, coordinator, flow))
}*/
