package com.workflowfm.simulator

import scala.collection.immutable._
import java.util.UUID
import akka.actor.ActorRef
import java.{util => ju}

//todo documentation
trait LookaheadStructure{
    def -(id: UUID): this.type
    def +(function: Seq[(UUID,Long)]=>Long, generators: List[TaskGenerator]): this.type
    def uncomplete(id: UUID): this.type
    def complete(id: UUID, time: Long): this.type
    def getTaskData(scheduled: Seq[(UUID,Long)]): Seq[(TaskGenerator,Long)]
    
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
    def +(source: Set[UUID], generator: TaskGenerator): LookaheadStructure = {
        val function: Seq[(UUID,Long)]=>Long = { s=>
            val sMap = s.foldLeft(Map.empty[UUID,Long]) { (a,b) => a + ((b._1,b._2)) }
            val times = source map (sMap.get(_))
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
  override def -(id: UUID): this.type = LookaheadStructures(handlers map (_.-(id))).asInstanceOf[this.type]
  override def +(function: Seq[(UUID,Long)]=>Long, generators: List[TaskGenerator]): this.type = LookaheadStructures(handlers map (_.+(function, generators))).asInstanceOf[this.type]
  override def uncomplete(id: UUID): this.type = LookaheadStructures(handlers map (_.uncomplete(id))).asInstanceOf[this.type]
  override def complete(id: UUID, time: Long): this.type = LookaheadStructures(handlers map (_.complete(id,time))).asInstanceOf[this.type]
  override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator, Long)] = handlers flatMap (_.getTaskData(scheduled))
}

object LookaheadStructures {
  def apply(handlers: LookaheadStructure*): LookaheadStructures = LookaheadStructures(
    Queue[LookaheadStructure]() ++ handlers
  )
}

case class LookaheadObj(
        actor: ActorRef,
        val lookaheadSet: Set[(Seq[(UUID, Long)] => Long, List[(TaskGenerator)])] = Set(),
        val completed: Set[(UUID, Long)] = Set()
    ) extends LookaheadStructure {

    override def -(id: UUID): this.type = {      
        LookaheadObj(actor,
        lookaheadSet.filter { 
            //remove all entries that spawn this task
            entry => entry._2 forall ( data => data.id != id) 
        }.filter {
            //remove all entries that are spawned by this task (among others)
            entry => entry._1(Seq()++completed)<0
        }, completed).asInstanceOf[this.type]
    }
    override def +(function: Seq[(UUID,Long)]=>Long, generators: List[TaskGenerator]): this.type = {
        copy(lookaheadSet=lookaheadSet+((function,generators))).asInstanceOf[this.type]
    }

    override def uncomplete(id: UUID): this.type = LookaheadObj(actor, lookaheadSet, completed filter {x=> x._1 != id}).asInstanceOf[this.type]
    override def complete(id: UUID, time: Long): this.type = LookaheadObj(actor, lookaheadSet, completed + ((id, time))).asInstanceOf[this.type]

    override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator,Long)] = {
        val y = lookaheadSet flatMap { x: (Seq[(UUID, Long)] => Long, Seq[TaskGenerator]) => 
            x match {
                case(function, data) => 
                val l = function( (scheduled ++ completed).to[collection.immutable.Seq] )
                if (l>0) (data map ((_,l))).toSeq
                else Seq()
            }
        }
        y.to[collection.immutable.Seq]
    }
}

case object EmptyStructure extends LookaheadStructure {
  override def -(id: UUID): this.type = EmptyStructure
  override def +(function: Seq[(UUID,Long)]=>Long, generators: List[TaskGenerator]): this.type = EmptyStructure
  override def uncomplete(id: UUID): this.type = EmptyStructure
  override def complete(id: UUID, time: Long): this.type = EmptyStructure
  override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator, Long)] = Seq()
}