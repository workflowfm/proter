package com.workflowfm.simulator

import scala.collection.immutable._
import java.util.UUID
import akka.actor.ActorRef

trait LookaheadStructure{
    val lookaheadSet: Set[( Seq[(UUID,Long)]=>Long, List[(TaskGenerator)] )]
    val completed: Set[(UUID,Long)]
    def -(id: UUID): LookaheadStructure
    def +(function: Seq[(UUID,Long)]=>Long, generators: List[TaskGenerator]): LookaheadStructure
    def uncomplete(id: UUID): LookaheadStructure
    def complete(id: UUID, time: Long): LookaheadStructure
    
    def +(function: Seq[(UUID,Long)]=>Long, generator: TaskGenerator): LookaheadStructure = this.+(function, List(generator))
    def +(sourceID: UUID, generator: TaskGenerator): LookaheadStructure = {
        val function: Seq[(UUID,Long)]=>Long = { s=>
            s collectFirst { case (id,time) if id==sourceID => time} match {
                case Some(value) => value
                case None => -1
            }
        }
        this.+(function, generator)
    }
    def +(that: LookaheadStructure): LookaheadStructure = {
        val newStructure = that.lookaheadSet.foldLeft(this) (
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
    override def +(function: Seq[(UUID,Long)]=>Long, generators: List[TaskGenerator]): LookaheadStructure = {
        LookaheadObj(actor, lookaheadSet + ((function, generators)), completed)
    }

    override def uncomplete(id: UUID): LookaheadObj = LookaheadObj(actor, lookaheadSet, completed filter {x=> x._1 != id})
    override def complete(id: UUID, time: Long): LookaheadObj = LookaheadObj(actor, lookaheadSet, completed + ((id, time)))
}