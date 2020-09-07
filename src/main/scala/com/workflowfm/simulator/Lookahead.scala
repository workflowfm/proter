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
    def complete(entry: (UUID,Long)): LookaheadStructure
    
    def +(function: Seq[(UUID,Long)]=>Long, generator: TaskGenerator): LookaheadStructure = this.+(function, List(generator))
    def +(sourceID: UUID, generator: TaskGenerator): LookaheadStructure = {
        val function: Seq[(UUID,Long)]=>Long = { s => (s collect { 
                case (id,time) if id==sourceID => time 
            }).headOption match {
                case Some(value) => value
                case None => -1L
            } 
        }
        this.+(function, generator)
    }
}

case class LookaheadObj(
        actor: ActorRef,
        override val lookaheadSet: Set[(Seq[(UUID, Long)] => Long, List[(TaskGenerator)])] = Set(),
        override val completed: Set[(UUID, Long)] = Set()
    ) extends LookaheadStructure {

    override def -(id: UUID): LookaheadStructure = {      
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

    override def uncomplete(id: UUID): LookaheadStructure = LookaheadObj(actor, lookaheadSet, completed filter {x=> x._1 != id})
    override def complete(entry: (UUID, Long)): LookaheadStructure = LookaheadObj(actor, lookaheadSet, completed + (entry))

    def lookaheadNextIter() = Unit
    def tasksAfterThis(task: UUID, time: Long, official: Boolean = true) = Seq()
}

/*
class LookaheadObj(protected val simulation: ActorRef) {
    type LookaheadFunctions = mutable.Set[( Seq[(UUID,Long)]=>Long, List[(TaskGenerator, Seq[String])] ) ]

    protected val lookaheadFunctions:LookaheadFunctions = mutable.Set()
    protected val completed = mutable.Set[(UUID,Long)]()
    protected val completedThisIter = mutable.Set[(UUID,Long)]()
    protected val lookaheadThisIter:LookaheadFunctions = mutable.Set()

    def tasksAfterThis(task: UUID, time: Long, official: Boolean=true): Seq[Task] = {
        completedThisIter += ((task, time))
        getTasks(if (official) lookaheadThisIter else lookaheadFunctions, official)
    }

    private def getTasks(functions: LookaheadFunctions, official: Boolean): Seq[Task] = {
        val taskData: List[(TaskGenerator, Seq[String], Long)] = ( 
        functions flatMap { 
            case(function, data) => 
                val l = function(Seq() ++ completed ++ completedThisIter )
                if (l>=0) { 
                    if (official) lookaheadThisIter.-=((function, data))
                    (data map (d => (d._1, d._2, l))) 
                }
                else List()
        } ).toList
        ( taskData map (x => x._1.create(x._3,simulation,x._2:_*)) ).toSeq
    }

    def complete(task: Task, time: Long) = {
        completed += ((task.id, time))
        removeIdSource(task.id)
    }

    def lookaheadNextIter: Unit = {
        completedThisIter.clear()
        lookaheadThisIter.clear()
        lookaheadThisIter ++= lookaheadFunctions
    }

    def add1To1Lookahead(sourceID: UUID, generator: TaskGenerator, resources: Seq[String]) {
        lookaheadFunctions += ( ( (s:Seq[(UUID,Long)]) => (s collect { case (id,time) if id==sourceID => time }).headOption match {
        case Some(value) => value
        case None => -1L
        } ), List((generator, resources)) ) 
    } //todo could express this in terms of `addManyTo1Lookahead()`

    def add1ToManyLookahead(sourceID: UUID, data: List[(TaskGenerator, Seq[String])]) {
        data foreach ( d => add1To1Lookahead( sourceID, d._1, d._2 ) )
    }

    def addManyTo1Lookahead(function: Seq[(UUID,Long)]=>Long, generator: TaskGenerator, resources: Seq[String]) {
        lookaheadFunctions += ( (function, List((generator, resources)) ) )
    }

    def addManyToManyLookahead(function: Seq[(UUID,Long)]=>Long, data: List[(TaskGenerator, Seq[String])]) {
        data foreach ( d => addManyTo1Lookahead( function, d._1, d._2 ) )
    }

    def removeIdSource(id: UUID) {
        //remove all entries that spawn this task
        lookaheadFunctions retain { entry => entry._2 forall ( data => data._1.id != id) }
        //remove all entries that are spawned by this task (among others)
        lookaheadFunctions retain { entry => entry._1(Seq()++completed)<0}
    }
}

*/