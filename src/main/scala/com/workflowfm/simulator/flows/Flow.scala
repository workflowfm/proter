package com.workflowfm.simulator.flows

import com.workflowfm.simulator._
import scala.concurrent.{ ExecutionContext, Future, Promise }
import akka.actor.{ Actor, ActorRef, Props }
import java.util.UUID


sealed trait Flow {
    val id: UUID
    def +(f:Flow) = And(this,f)
    def >(f:Flow) = Then(this,f)
    def |(f:Flow) = Or(this,f)
}

case class NoTask() extends Flow { val id: UUID = java.util.UUID.randomUUID }
case class FlowTask(generator:TaskGenerator, resources:Seq[String]) extends Flow { val id: UUID = java.util.UUID.randomUUID }
case class Then(left:Flow,right:Flow) extends Flow { val id: UUID = java.util.UUID.randomUUID }
case class And(left:Flow,right:Flow) extends Flow { val id: UUID = java.util.UUID.randomUUID }
case class All(elements:Flow*) extends Flow { val id: UUID = java.util.UUID.randomUUID }
case class Or(left:Flow,right:Flow) extends Flow { val id: UUID = java.util.UUID.randomUUID }


class FlowSimulationActor (
    name: String,
    coordinator: ActorRef,
    flow: Flow
) (implicit executionContext: ExecutionContext)
        extends AsyncSimulation(name, coordinator)(executionContext) {

    override def run(): Future[Any] = {
        val promise = Promise[Any]()
        runFlow(flow, ( (_,_)=>promise.success(Unit) ) )
        ready()
        promise.future
    }

    private def runFlow(flow:Flow, callback:Callback):Unit = {
        
        flow match {
            case f: FlowTask => task(f.id, f.generator, ( (t,l)=>{callback(t,l); ack(Seq(f.id)) } ), f.resources)
            case f: Flow => { tasks += flow.id -> callback; execute(f) }
        }      
    }

    private def complete(id: UUID) = {
        tasks.get(id).map (_(null,0L))
        tasks -= id
    }


    private def execute(flow:Flow) {
        flow match {
            case f:NoTask => complete(f.id)

            case FlowTask(generator:TaskGenerator, resources:Seq[String]) => {} //this is here for the sake of case completeness, should not be called

            case f:Then => {
                val rightCallback: Callback = (_,_) => complete(f.id)
                val leftCallback: Callback = (_,_) => runFlow(f.right, rightCallback)
                runFlow(f.left, leftCallback)
            }

            case f:And => {
                val leftCallback: Callback = (_,_) => ( if (!tasks.contains(f.right.id)) complete(f.id) )
                val rightCallback: Callback = (_,_) => ( if (!tasks.contains(f.left.id)) complete(f.id) )
                runFlow(f.left,leftCallback)
                runFlow(f.right,rightCallback)
            }

            case f @ All(elem@_*) => runFlow( (elem.fold(NoTask()) { (l,r)=>And(l,r) }), (_,_)=>complete(f.id) )

            case f:Or => {
                val leftCallback: Callback = (_,_) => ( if (tasks.contains(f.right.id)) complete(f.id) )
                val rightCallback: Callback = (_,_) => ( if (tasks.contains(f.left.id)) complete(f.id) )
                runFlow(f.left,leftCallback)
                runFlow(f.right,rightCallback)
            }
        }
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
