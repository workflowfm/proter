package com.workflowfm.simulator.flows

import com.workflowfm.simulator._
import scala.concurrent.{ ExecutionContext, Future, Promise }
import akka.actor.{ Actor, ActorRef, Props }
import java.util.UUID


sealed trait Flow {
    val id: UUID
    def +(f:Flow) = And(this,f)
    def >(f:Flow) = Then(this,f)
    //def |(f:Flow) = Or(this,f)
}

case class NoTask() extends Flow { val id: UUID = java.util.UUID.randomUUID }
case class FlowTask(generator:TaskGenerator, resources:Seq[String]) extends Flow { val id: UUID = java.util.UUID.randomUUID }
//case class Just(flowTask:FlowTask) extends Flow
case class Then(left:Flow,right:Flow) extends Flow { val id: UUID = java.util.UUID.randomUUID }
case class And(left:Flow,right:Flow) extends Flow { val id: UUID = java.util.UUID.randomUUID }
case class All(elements:Flow*) extends Flow { val id: UUID = java.util.UUID.randomUUID }
//case class Or(left:Flow,right:Flow) extends Flow


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

    def runFlow(flow:Flow, callback:Callback):Unit = {
        
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

            //case class Or(left:Flow,right:Flow) extends Flow
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

/*

Then (id) task1 task2 : 
run(task1) callback: run(task 2) callback: complete(id); ready()


And (id) task1 task2: 
run(task1) callback f: if (!tasks.contains(task2)) complete(id))
run(task2) callback g: if (!tasks.contains(task1)) complete(id))


Then (id) task1 (And (aid) task2 task3)
run(task1) callback: run((And (aid) task2 task3)) callback: complete(id); ready()
ready()

tasks:
task1.id -> [ run((And (aid) task2 task3)) callback: complete(id); ready() ]

run(task2) callback f: if (!tasks.contains(task3)) complete(aid))
run(task3) callback g: if (!tasks.contains(task2)) complete(aid))
ready()

aid -> complete(id)
task2.id -> if (!tasks.contains(task3)) complete(aid))
task3.id -> if (!tasks.contains(task2)) complete(aid))

aid -> complete(id)
task3.id -> if (!tasks.contains(task2)) complete(aid))



And (aid) (Then (leftid) t1 t2) (Then (rightid) t3 t4)

aid -> promise.success(...)

run(Then (leftid) t1 t2) callback f: if (!tasks.contains(rightid)) complete(id))
   run(t1) callback: run(t2) callback: complete(leftid); ready()
run(Then (rightid) t3 t4) callback g: if (!tasks.contains(leftid)) complete(id))
   run(t3) callback: run(t4) callback: complete(rightid); ready()
ready()

leftid ->  if (!tasks.contains(rightid)) complete(aid))
t1 -> run(t2) callback: complete(leftid); ready()
rightid ->  if (!tasks.contains(leftid)) complete(aid))
t3 -> run(t4) callback: complete(rightid); ready()

t1!
leftid ->  if (!tasks.contains(rightid)) complete(aid))
t2 -> complete(leftid)
rightid ->  if (!tasks.contains(leftid)) complete(aid))
t3 -> run(t4) callback: complete(rightid)

t2!
rightid ->  if (!tasks.contains(leftid)) complete(aid))
t3 -> run(t4) callback: complete(rightid)

t3! 
rightid ->  if (!tasks.contains(leftid)) complete(aid))
t4 -> complete(rightid)

t4!
complete(aid)


Then (aid) t1 (Then (bid) t2 t3)

run(t1) callback: { run(Then (bid) t2 t3) callback: complete(aid); ready() }

t1 -> run(Then (bid) t2 t3) callback: complete(aid); ready()


start (flow) {
 create promise
 run(flow) callback: promise.success
 ready()
 }

run (x, callback) {
 add x.id -> callback in tasks
 x is a task -> add to queue
 x is a flow -> flow.execute()
 }

 */


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
