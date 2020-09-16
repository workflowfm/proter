package com.workflowfm.simulator

import scala.collection.immutable._
import java.util.UUID
import akka.actor.ActorRef
import java.{util => ju}

//todo documentation
trait LookaheadStructure{
    def -(id: UUID): this.type
    def +(function: Map[UUID,Long]=>Long, generators: List[TaskGenerator]): this.type //todo option long
    def uncomplete(id: UUID): this.type //todo remove and make LookaheadObject wrapper
    def complete(id: UUID, time: Long): this.type
    def getTaskData(scheduled: Seq[(UUID,Long)]): Seq[(TaskGenerator,Long)]
    
    def +(function: Map[UUID,Long]=>Long, generator: TaskGenerator): LookaheadStructure = this.+(function, List(generator))
    def +(sourceID: UUID, generator: TaskGenerator): LookaheadStructure = {
        val function: Map[UUID,Long]=>Long = { s=>
            s.get(sourceID) match {
                case Some(value) => value
                case None => -1
            }
        }
        this.+(function, generator)
    }
    def +(source: Set[UUID], generator: TaskGenerator): LookaheadStructure = {
        val function: Map[UUID,Long]=>Long = { s=>
            val times = source map (s.get(_))
            if (times.contains(None)) -1
            else (times map (_.get)).max
        }
        this.+(function, generator)
    }
    def and(that: LookaheadStructure): LookaheadStructure = {
        LookaheadStructures(this,that)
    }
}

case class LookaheadStructures(handlers: Queue[LookaheadStructure]) extends LookaheadStructure {
  override def -(id: UUID): this.type = LookaheadStructures(handlers map (_.-(id))).asInstanceOf[this.type] //todo maybe dont need this.type type??
  override def +(function: Map[UUID,Long]=>Long, generators: List[TaskGenerator]): this.type = LookaheadStructures(handlers map (_.+(function, generators))).asInstanceOf[this.type]
  override def uncomplete(id: UUID): this.type = LookaheadStructures(handlers map (_.uncomplete(id))).asInstanceOf[this.type]
  override def complete(id: UUID, time: Long): this.type = LookaheadStructures(handlers map (_.complete(id,time))).asInstanceOf[this.type]
  override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator, Long)] = handlers flatMap (_.getTaskData(scheduled))
  //todo override and
  override def and(that: LookaheadStructure): LookaheadStructure = { copy(handlers=handlers :+ that) }
}

object LookaheadStructures {
  def apply(handlers: LookaheadStructure*): LookaheadStructures = LookaheadStructures(
    Queue[LookaheadStructure]() ++ handlers
  )
}

case class LookaheadObj(
        actor: ActorRef,
        lookaheadSet: Set[(Map[UUID, Long] => Long, List[(TaskGenerator)])] = Set(),
        completed: Set[(UUID, Long)] = Set()
    ) extends LookaheadStructure {

    override def -(id: UUID): this.type = {      
        LookaheadObj(actor,
        lookaheadSet.filter { 
            //remove all entries that spawn this task
            entry => entry._2 forall ( data => data.id != id) 
        }.filter {
            //remove all entries that are spawned by this task (among others)
            entry => entry._1( completed.foldLeft(Map.empty[UUID,Long]){(a,b)=>a + ((b._1,b._2))} )<0
        }, completed).asInstanceOf[this.type]
    }
    override def +(function: Map[UUID,Long]=>Long, generators: List[TaskGenerator]): this.type = {
        copy(lookaheadSet=lookaheadSet+((function,generators))).asInstanceOf[this.type]
    }

    override def uncomplete(id: UUID): this.type = LookaheadObj(actor, lookaheadSet, completed filter {x=> x._1 != id}).asInstanceOf[this.type]
    override def complete(id: UUID, time: Long): this.type = LookaheadObj(actor, lookaheadSet, completed + ((id, time))).asInstanceOf[this.type]

    override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator,Long)] = {
        val y = lookaheadSet flatMap { x: (Map[UUID, Long] => Long, Seq[TaskGenerator]) => 
            x match {
                case(function, data) => 
                val l = function( (scheduled ++ completed).foldLeft(Map.empty[UUID,Long]){(a,b)=>a + ((b._1,b._2))} )
                if (l>0) (data map ((_,l))).toSeq
                else Seq()
            }
        }
        y.to[collection.immutable.Seq]
    }
}

case object EmptyStructure extends LookaheadStructure {
  override def -(id: UUID): this.type = EmptyStructure
  override def +(function: Map[UUID,Long]=>Long, generators: List[TaskGenerator]): this.type = EmptyStructure
  override def uncomplete(id: UUID): this.type = EmptyStructure
  override def complete(id: UUID, time: Long): this.type = EmptyStructure
  override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator, Long)] = Seq()
}