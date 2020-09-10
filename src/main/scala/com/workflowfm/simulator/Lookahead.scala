package com.workflowfm.simulator

import scala.collection.immutable._
import java.util.UUID
import akka.actor.ActorRef

//todo documentation
trait LookaheadStructure{
    def lookaheadSet: Set[( Seq[(UUID,Long)]=>Long, List[(TaskGenerator)] )] //todo consider removing this
    val completed: Set[(UUID,Long)] //todo see above
    def -(id: UUID): LookaheadStructure
    def +(function: Seq[(UUID,Long)]=>Long, generators: List[TaskGenerator]): this.type
    def uncomplete(id: UUID): this.type
    def complete(id: UUID, time: Long): this.type
    //todo method for getting
    
    def +(function: Seq[(UUID,Long)]=>Long, generator: TaskGenerator): LookaheadStructure = this.+(function, List(generator))
    def +(sourceID: UUID, generator: TaskGenerator): LookaheadStructure = {
        val function: Seq[(UUID,Long)]=>Long = { s=> //todo use Map instead
            s collectFirst { case (id,time) if id==sourceID => time} match {
                case Some(value) => value
                case None => -1
            }
        }
        this.+(function, generator)
    }
    def +(that: LookaheadStructure): LookaheadStructure = { //relying on set is bad, consider moving this to lookaheadObj
        val newStructure = that.lookaheadSet.foldLeft(this) (   //see output.scala simMetricsOutput
            (structure,tuple) => structure + (tuple._1,tuple._2)
        )
        that.completed.foldLeft(newStructure) (
            (structure,completedEntry) => structure.complete(completedEntry._1,completedEntry._2)
        )
    }
}

case class LookaheadObj(
        actor: ActorRef,
        override val lookaheadSet: Set[(Seq[(UUID, Long)] => Long, List[(TaskGenerator)])] = Set(),
        override val completed: Set[(UUID, Long)] = Set()
    ) extends LookaheadStructure {

    override def -(id: UUID): LookaheadObj = {      
        LookaheadObj(actor,
        lookaheadSet.filter { 
            //remove all entries that spawn this task
            entry => entry._2 forall ( data => data.id != id) 
        }.filter {
            //remove all entries that are spawned by this task (among others)
            entry => entry._1(Seq()++completed)<0
        }, completed)
    }
    override def +(function: Seq[(UUID,Long)]=>Long, generators: List[TaskGenerator]): this.type = {
        //LookaheadObj(actor, lookaheadSet + ((function, generators)), completed)
        copy(lookaheadSet=lookaheadSet+((function,generators))).asInstanceOf[this.type] //todo this
    }

    override def uncomplete(id: UUID): this.type = LookaheadObj(actor, lookaheadSet, completed filter {x=> x._1 != id}).asInstanceOf[this.type]
    override def complete(id: UUID, time: Long): this.type = LookaheadObj(actor, lookaheadSet, completed + ((id, time))).asInstanceOf[this.type]
}

//todo empty lookaheadStructure