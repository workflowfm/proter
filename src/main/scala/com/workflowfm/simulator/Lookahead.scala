package com.workflowfm.simulator

import scala.collection.immutable
import java.util.UUID
import akka.actor.ActorRef

trait LookaheadStructure{
    type LookaheadFunctions = Set[( Seq[(UUID,Long)]=>Long, List[(TaskGenerator, Seq[String])] ) ]
    val lookaheadFunctions:LookaheadFunctions = Set()
    val lookaheadThisIter:LookaheadFunctions =  Set()
    val completed = Set[(UUID,Long)]()
    val completedThisIter = Set[(UUID,Long)]()

    def add1To1Lookahead(sourceID: UUID, generator: TaskGenerator, resources: Seq[String]): Unit
    def add1ToManyLookahead(sourceID: UUID, data: List[(TaskGenerator, Seq[String])]): Unit
    def addManyTo1Lookahead(function: Seq[(UUID,Long)]=>Long, generator: TaskGenerator, resources: Seq[String]): Unit
    def addManyToManyLookahead(function: Seq[(UUID,Long)]=>Long, data: List[(TaskGenerator, Seq[String])]): Unit
    def removeIdSource(id: UUID): Unit
}

case class LookaheadS() extends LookaheadStructure {
    
}

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