package com.workflowfm.simulator

import scala.collection.mutable
import java.util.UUID
import akka.actor.ActorRef

class LookaheadObj(protected val simulation: ActorRef) {
    type LookaheadFunctions = mutable.Set[( Seq[(UUID,Long)]=>Long, List[(UUID, TaskGenerator, Seq[String])] ) ]

    protected val lookaheadFunctions:LookaheadFunctions = mutable.Set()
    protected val completed = mutable.Set[(UUID,Long)]()
    protected val completedThisItter = mutable.Set[(UUID,Long)]()
    protected val lookaheadThisItter:LookaheadFunctions = mutable.Set()

    def tasksAfterThis(task: UUID, time: Long, official: Boolean=true): Seq[Task] = {
        completedThisItter += ((task, time))
        getTasks(if (official) lookaheadThisItter else lookaheadFunctions, official)
    }

    private def getTasks(functions: LookaheadFunctions, official: Boolean): Seq[Task] = {
        val taskData: List[(UUID, TaskGenerator, Seq[String], Long)] = ( 
        functions flatMap { 
            case(function, data) => 
                val l = function(Seq() ++ completed ++ completedThisItter )
                if (l>=0) { 
                    if (official) lookaheadThisItter.-=((function, data))
                    (data map (d => (d._1, d._2, d._3, l))) 
                }
                else List()
        } ).toList
        ( taskData map (x => x._2.create(x._1,x._4,simulation,x._3:_*)) ).asInstanceOf[Seq[Task]]
    }

    def complete(task: Task, time: Long) = {
        completed += ((task.id, time))
        removeIdSource(task.id)
    }

    def lookaheadNextItter: Unit = {
        completedThisItter.clear()
        lookaheadThisItter.clear()
        lookaheadThisItter ++= lookaheadFunctions
    }

    def add1To1Lookahead(sourceID: UUID, resultID: UUID, generator: TaskGenerator, resources: Seq[String]) {
        lookaheadFunctions += ( ( (s:Seq[(UUID,Long)]) => (s collect { case (id,time) if id==sourceID => time }).headOption match {
        case Some(value) => value
        case None => -1L
        } ), List((resultID, generator, resources)) ) 
    } //could express this in terms of `addManyTo1Lookahead()`

    def add1ToManyLookahead(sourceID: UUID, data: List[(UUID, TaskGenerator, Seq[String])]) {
        data foreach ( d => add1To1Lookahead( sourceID, d._1, d._2, d._3 ) )
    }

    def addManyTo1Lookahead(function: Seq[(UUID,Long)]=>Long, resultID: UUID, generator: TaskGenerator, resources: Seq[String]) {
        lookaheadFunctions += ( (function, List((resultID, generator, resources)) ) )
    }

    def addManyToManyLookahead(function: Seq[(UUID,Long)]=>Long, data: List[(UUID, TaskGenerator, Seq[String])]) {
        data foreach ( d => addManyTo1Lookahead( function, d._1, d._2, d._3 ) )
    }

    def removeIdSource(id: UUID) {
        //remove all entries that spawn this task
        lookaheadFunctions retain { entry => entry._2 forall ( data => data._1 != id) }
        //remove all entries that are spawned by this task (among others)
        lookaheadFunctions retain { entry => entry._1(Seq()++completed)<0}
    }
}