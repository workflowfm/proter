package com.workflowfm.simulator

import scala.collection.immutable._
import java.util.UUID
import akka.actor.ActorRef
import java.{util => ju}

//todo documentation
trait LookaheadStructure{
    def -(id: UUID): LookaheadStructure
    def +(function: Map[UUID,Long]=>Option[Long], generators: List[TaskGenerator]): LookaheadStructure
    def uncomplete(id: UUID): LookaheadStructure //todo remove and make LookaheadObject wrapper
    def complete(id: UUID, time: Long): LookaheadStructure
    def getTaskData(scheduled: Seq[(UUID,Long)]): Seq[(TaskGenerator,Long)]
    
    def +(function: Map[UUID,Long]=>Option[Long], generator: TaskGenerator): LookaheadStructure = this.+(function, List(generator))
    def +(sourceID: UUID, generator: TaskGenerator): LookaheadStructure = {
        val function: Map[UUID,Long]=>Option[Long] = { s=>
            s.get(sourceID)
        }
        this.+(function, generator)
    }
    def +(source: Set[UUID], generator: TaskGenerator): LookaheadStructure = {
        val function: Map[UUID,Long]=>Option[Long] = { s=>
            val times = source map (s.get(_))
            if (times.contains(None)) None
            else Some((times map (_.get)).max)
        }
        this.+(function, generator)
    }
    def and(that: LookaheadStructure): LookaheadStructure = {
        LookaheadStructures(this,that)
    }
}

case class LookaheadStructures(handlers: Queue[LookaheadStructure]) extends LookaheadStructure {
  override def -(id: UUID): LookaheadStructure = LookaheadStructures(handlers map (_.-(id)))
  override def +(function: Map[UUID,Long]=>Option[Long], generators: List[TaskGenerator]): LookaheadStructure = LookaheadStructures(handlers map (_.+(function, generators)))
  override def uncomplete(id: UUID): LookaheadStructure = LookaheadStructures(handlers map (_.uncomplete(id)))
  override def complete(id: UUID, time: Long): LookaheadStructure = LookaheadStructures(handlers map (_.complete(id,time)))
  override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator, Long)] = handlers flatMap (_.getTaskData(scheduled))
  override def and(that: LookaheadStructure): LookaheadStructure = copy( handlers = handlers :+ that )
}

object LookaheadStructures {
  def apply(handlers: LookaheadStructure*): LookaheadStructures = LookaheadStructures(
    Queue[LookaheadStructure]() ++ handlers
  )
}

case class LookaheadSet(
        actor: ActorRef,
        lookaheadSet: Set[(Map[UUID, Long] => Option[Long], List[(TaskGenerator)])] = Set(),
        completed: Set[(UUID, Long)] = Set()
    ) extends LookaheadStructure {

    override def -(id: UUID): LookaheadStructure = {      
        LookaheadSet(actor,
        lookaheadSet.filter { 
            //remove all entries that spawn this task
            entry => entry._2 forall ( data => data.id != id) 
        }.filter {
            //remove all entries that are spawned by this task (among others)
            entry => ! entry._1( completed.foldLeft(Map.empty[UUID,Long]){(a,b)=>a + ((b._1,b._2))} ).isDefined
        }, completed)
    }
    override def +(function: Map[UUID,Long]=>Option[Long], generators: List[TaskGenerator]): LookaheadStructure = {
        copy(lookaheadSet=lookaheadSet+((function,generators)))
    }

    override def uncomplete(id: UUID): LookaheadStructure = LookaheadSet(actor, lookaheadSet, completed filter {x=> x._1 != id})
    override def complete(id: UUID, time: Long): LookaheadStructure = LookaheadSet(actor, lookaheadSet, completed + ((id, time)))

    override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator,Long)] = {
        val y = lookaheadSet flatMap { x: (Map[UUID, Long] => Option[Long], Seq[TaskGenerator]) => 
            x match {
                case(function, data) => 
                val l = function( (scheduled ++ completed).foldLeft(Map.empty[UUID,Long]){(a,b)=>a + ((b._1,b._2))} )
                l match {
                    case None => Seq()
                    case Some(v) => (data map ((_,v))).toSeq
                }
            }
        }
        y.to[collection.immutable.Seq]
    }
}

case object EmptyStructure extends LookaheadStructure {
  override def -(id: UUID): LookaheadStructure = EmptyStructure
  override def +(function: Map[UUID,Long]=>Option[Long], generators: List[TaskGenerator]): LookaheadStructure = EmptyStructure
  override def uncomplete(id: UUID): LookaheadStructure = EmptyStructure
  override def complete(id: UUID, time: Long): LookaheadStructure = EmptyStructure
  override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator, Long)] = Seq()
}