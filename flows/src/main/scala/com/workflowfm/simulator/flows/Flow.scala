package com.workflowfm.simulator.flows

import com.workflowfm.simulator._
import scala.concurrent.{ ExecutionContext, Future }
import akka.actor.{ Actor, ActorRef, Props }

sealed trait Flow
case object NoTask extends Flow
case class FlowTask(generator:TaskGenerator, resources:Seq[String]) extends Flow
case class Just(flowTask:FlowTask) extends Flow
case class Then(left:Flow,right:Flow) extends Flow
case class And(left:Flow,right:Flow) extends Flow
case class All(elements:Flow*) extends Flow

class FlowSimulationActor (
    name: String,
    coordinator: ActorRef,
    flow: Flow) 
(implicit executionContext: ExecutionContext)
extends SimulationActor(name,coordinator) {
    
    override def run() = {
        //the first output is the future that is completed when the coordinator is done with the task
        //the second output is the future that is completed once the flow element and all of its children are ready
        def execute(flow:Flow,block:Boolean=false):(Future[Any],Future[Any]) = {
            flow match {
                case NoTask => (Future.unit,Future.unit)

                case FlowTask(generator,resources) => {
                    println("registered: "+generator.name)   
                    (task(generator,resources:_*),Future.unit)
                }

                case Just(flowTask) => {
                    val future = execute(flowTask)
                    ready()
                    (future._1,future._2)
                }

                case Then(left,right) => {
                    val leftFuture = execute(left,block)
                    if(!block) {
                        leftFuture._2.onComplete{ _=> ready() }
                    }
                    val rightFuture = leftFuture._1.flatMap{ x =>
                        val future = execute(right,block)
                        ready()
                        future._1
                    }
                    (rightFuture,leftFuture._2) //TODO
                                        
                }  

                case And(left,right) => {
                    val futureLeft = execute(left,true)
                    val futureRight = execute(right,true)
                    if(!block) { 
                        Future.sequence(Seq(futureLeft._2,futureRight._2)).onComplete{ _=> ready() }
                    }
                    if (!(left==NoTask || right==NoTask)) {
                        futureLeft._1.onComplete{ x => if(!futureRight._1.isCompleted) ready() }
                        futureRight._1.onComplete{ x => if(!futureLeft._1.isCompleted) ready() }
                    }

                    (Future.sequence(Seq(futureLeft._1,futureRight._1)),Future.sequence(Seq(futureLeft._2,futureRight._2)))
                }

                case All(elem@_*) => {
                    val ands = elem.fold(NoTask) { (l, r) =>  And(l,r) }
                    execute(ands,block)
                }

            }
        }
        execute(flow)._1
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