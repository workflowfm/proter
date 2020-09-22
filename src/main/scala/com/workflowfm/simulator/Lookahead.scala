package com.workflowfm.simulator

import scala.collection.immutable._
import java.util.UUID
import akka.actor.ActorRef

/**
  * A Lookahead Strucuture contains information about the order in which tasks run.
  * 
  * A structure is immutable. It built by the simulation and passed to the scheduler where it is used
  * for scheduling by revealing information about future tasks so that look-ahead can be achieved.
  */
trait LookaheadStructure{
    /**
      * Removes a task with the corresponding ID from the structure
      * 
      * Once removed, the task with this ID will never be returned, but also
      * all entries where this task is a prerequisite are also removed. This 
      * prevents the structure from returning tasks that have already completed
      * as well as returning tasks that are no longer "in the future".
      *
      * @param id The id of the task
      * @return A LookaheadStructure with the specified task removed.
      */
    def -(id: UUID): LookaheadStructure
    /**
      * Adds an entry to the strucutre, comprised of a function that describes the 
      * prerequisites and resultant starting time of a set of tasks, and a list
      * of [[TaskGenerator]]s that describe the tasks that should start if this
      * function returns `Some(value)`.
      * 
      * The function has type `Map[UUID,Long]=>Option[Long]`. It should take a map that
      * contains the IDs of completed/scheduled tasks and their respective finishing times,
      * and it should return `None` if the prerequisites of this entry are met, and
      * `Some(value)` if the prerequisites are met, where `value` should be the starting time
      * of the tasks contained in this entry. This allows us to express a complex relationship
      * whereby the starting time of the tasks in this lookahead entry can be an intricate function
      * of the finishing times of the prerequisites.
      *
      * @param function The function that describes the prerequisites of this entry. Should return
      * `None` if the prerequisites are not met, and `Some(value)` if the prerequistes are met, where the
      * value is the starting time of the tasks in this entry.
      * @param generators A list of [[TaskGenerator]]s that describes the tasks that should start if the
      * prerequistes are met.
      * @return A LookaheadStructure with the specified entry added to it.
      */
    def +(function: Map[UUID,Long]=>Option[Long], generators: List[TaskGenerator]): LookaheadStructure
    /**
      * Removes an ID from the list of completed IDs
      *
      * @param id the ID to be removed
      * @return A LookaheadStructure with this ID removed from the list of completed IDs
      */
    def uncomplete(id: UUID): LookaheadStructure //todo remove and make LookaheadObject wrapper
    /**
      * Adds an Task described by an (ID,time) pair to the list of completed IDs
      *
      * @param id The ID to be added
      * @param time The time at which the task completed
      * @return A LookaheadStructure with this (ID,time) pair added to the list of completed tasks
      */
    def complete(id: UUID, time: Long): LookaheadStructure
    /**
      * Retrieves all tasks that can start given the list of scheduled/completed tasks
      *
      * @param scheduled The list of tasks that have been scheduled/completed, described with an (ID,time) tuple
      * @return The list of tasks that can start, described by (TaskGenerator,starting_time) tuples
      */
    def getTaskData(scheduled: Seq[(UUID,Long)]): Seq[(TaskGenerator,Long)]
    
    /**
      * TODO
      *
      * @param function
      * @param generator
      * @return
      */
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
    /**
      * Combines two LookaheadStructures
      *
      * @param that The other LookaheadStructure to merge with this one.
      * @return The merged LookaheadStructure
      */
    def and(that: LookaheadStructure): LookaheadStructure = {
        LookaheadStructures(this,that)
    }
}

/**
  * A Structure used for combining multiple [[LookaheadStructure]]s into one.
  *
  * @param handlers A queue of the [[LookaheadStructure]]s which are included in this strucutre.
  */
case class LookaheadStructures(handlers: Queue[LookaheadStructure]) extends LookaheadStructure {
    /**
      * @inheritdoc
      */
    override def -(id: UUID): LookaheadStructure = LookaheadStructures(handlers map (_.-(id)))
    /**
      * @inheritdoc
      */
    override def +(function: Map[UUID,Long]=>Option[Long], generators: List[TaskGenerator]): LookaheadStructure = LookaheadStructures(handlers map (_.+(function, generators)))
    /**
      * @inheritdoc
      */
    override def uncomplete(id: UUID): LookaheadStructure = LookaheadStructures(handlers map (_.uncomplete(id)))
    /**
      * @inheritdoc
      */
    override def complete(id: UUID, time: Long): LookaheadStructure = LookaheadStructures(handlers map (_.complete(id,time)))
    /**
      * @inheritdoc
      */
    override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator, Long)] = handlers flatMap (_.getTaskData(scheduled))
    /**
      * @inheritdoc
      */
    override def and(that: LookaheadStructure): LookaheadStructure = copy( handlers = handlers :+ that )
}

object LookaheadStructures {
    /** Shorthand constructor for a [[LookaheadStructures]] from a list of [[LookaheadStructure]]s. */
    def apply(handlers: LookaheadStructure*): LookaheadStructures = LookaheadStructures(
        Queue[LookaheadStructure]() ++ handlers
    )
}

/**
  * A [[LookaheadStructure]] that uses a set in the implementation
  * 
  * @see [[LookaheadStructure]]
  *
  * @param lookaheadSet The initial set used. Empty by default.
  * @param completed The initial completed tasks. Empty by default.
  */
case class LookaheadSet(
        lookaheadSet: Set[(Map[UUID, Long] => Option[Long], List[(TaskGenerator)])] = Set(),
        completed: Set[(UUID, Long)] = Set()
    ) extends LookaheadStructure {
    /**
      * @inheritdoc
      */
    override def -(id: UUID): LookaheadStructure = {      
        LookaheadSet(
        lookaheadSet.filter { 
            //remove all entries that spawn this task
            entry => entry._2 forall ( data => data.id != id) 
        }.filter {
            //remove all entries that are spawned by this task (among others)
            entry => ! entry._1( completed.foldLeft(Map.empty[UUID,Long]){(a,b)=>a + ((b._1,b._2))} ).isDefined
        }, completed)
    }
    /**
      * @inheritdoc
      */
    override def +(function: Map[UUID,Long]=>Option[Long], generators: List[TaskGenerator]): LookaheadStructure = {
        copy(lookaheadSet=lookaheadSet+((function,generators)))
    }
    /**
      * @inheritdoc
      */
    override def uncomplete(id: UUID): LookaheadStructure = LookaheadSet(lookaheadSet, completed filter {x=> x._1 != id})
    /**
      * @inheritdoc
      */
    override def complete(id: UUID, time: Long): LookaheadStructure = LookaheadSet(lookaheadSet, completed + ((id, time)))
    /**
      * @inheritdoc
      */
    override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator,Long)] = {
        val y = lookaheadSet flatMap { x: (Map[UUID, Long] => Option[Long], Seq[TaskGenerator]) => 
            x match {
                case(function, data) => 
                val l = function( (scheduled ++ completed).foldLeft(Map.empty[UUID,Long]){(a,b)=>a + ((b._1,b._2))} )
                l match {
                    case None => Seq()
                    case Some(v) => (data map((_,v))).toSeq
                }
            }
        }
        y.to[collection.immutable.Seq]
    }
}

/**
  * An empty [[LookaheadStructure]] that does nothing.
  */
case object EmptyStructure extends LookaheadStructure {
    /**
      * @inheritdoc
      */
    override def -(id: UUID): LookaheadStructure = EmptyStructure
    /**
      * @inheritdoc
      */
    override def +(function: Map[UUID,Long]=>Option[Long], generators: List[TaskGenerator]): LookaheadStructure = LookaheadSet() + (function,generators)
    /**
      * @inheritdoc
      */
    override def uncomplete(id: UUID): LookaheadStructure = EmptyStructure
    /**
      * @inheritdoc
      */
    override def complete(id: UUID, time: Long): LookaheadStructure = EmptyStructure
    /**
      * @inheritdoc
      */
    override def getTaskData(scheduled: Seq[(UUID, Long)]): Seq[(TaskGenerator, Long)] = Seq()
    }