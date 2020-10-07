package com.workflowfm.simulator

import scala.collection.immutable.{Map, Queue}
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
      * Once removed, the task with this ID will no longer be returned.
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
      * whereby the starting time of the tasks in this lookahead entry can be an intricate
      * function of the finishing times of the prerequisites.
      *
      * @param function The function that describes the prerequisites of this entry. Should return
      * `None` if the prerequisites are not met, and `Some(value)` if the prerequistes are met, where
      * the value is the starting time of the tasks in this entry.
      * @param generators A list of [[TaskGenerator]]s that describes the tasks that should start if
      * the prerequistes are met.
      * @return A LookaheadStructure with the specified entry added to it.
      */
    def +(function: Map[UUID,Long]=>Option[Long], generators: List[TaskGenerator]): LookaheadStructure
    /**
      * Retrieves all tasks that can start given the list of scheduled/completed tasks
      *
      * @param scheduled The list of tasks that have been scheduled/completed, described with an (ID,time) tuple
      * @return The list of tasks that can start, described by (TaskGenerator,starting_time) tuples
      */
    def getTaskData(scheduled: Iterable[(UUID,Long)]): Seq[(TaskGenerator,Long)]
    
    /**
      * Provides a nicer interface for adding elements to the lookeahead structure.
      * 
      * Allows adding a single generator instead of a list of generators.
      * 
      * @see [[LookaheadStrucutre.+]]
      * 
      * @param function The function describing the conditions of starting the correspoinding task.
      * @param generator The task that will start when the conditions are met.
      * @return
      */
      //todo e.g. // e.g. [[abortSimulation(name:String,actor:akka\.actor\.ActorRef)* abortSimulation]]. 
    def +(function: Map[UUID,Long]=>Option[Long], generator: TaskGenerator): LookaheadStructure = this.+(function, List(generator))

    /**
      * Provides a nicer interface for adding elements to the lookeahead structure.
      * 
      * Allows adding a simple one-to-one relationship, where the generator task starts right after
      * the sourceID task finishes.
      * 
      * @see [[LookaheadStrucutre.+]]
      * 
      * @param sourceID The id of the task that will finish.
      * @param generator The generator fo the task that will start when the prior task finishes.
      * @return
      */
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
      that match {
        case EmptyStructure => this
        case LookaheadStructures(handlers) => that and this
        case _ => LookaheadStructures(this,that)
      }
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
    override def getTaskData(scheduled: Iterable[(UUID, Long)]): Seq[(TaskGenerator, Long)] = handlers flatMap (_.getTaskData(scheduled))
    /**
      * @inheritdoc
      */
    override def and(that: LookaheadStructure): LookaheadStructure = {
      that match {
        case EmptyStructure => this
        case LookaheadStructures(h) => copy(handlers = handlers ++ h)
        case _ => copy(handlers = handlers :+ that)
      }
    }
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
    ) extends LookaheadStructure {
    /**
      * @inheritdoc
      */
    override def -(id: UUID): LookaheadStructure = {      
        LookaheadSet(
        lookaheadSet.filter { 
            //remove all entries that spawn this task
            entry => entry._2 forall ( data => data.id != id) 
        }
        )
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
    override def getTaskData(scheduled: Iterable[(UUID, Long)]): Seq[(TaskGenerator,Long)] = {
        val y = lookaheadSet flatMap { x: (Map[UUID, Long] => Option[Long], Seq[TaskGenerator]) => 
            x match {
                case(function, data) => 
                val l = function( (scheduled).foldLeft(Map.empty[UUID,Long]){(a,b)=>a + ((b._1,b._2))} )
                l match {
                    case None => Seq()
                    case Some(v) => (data map((_,v))).toSeq
                }
            }
        }
        y.toSeq
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
    override def getTaskData(scheduled: Iterable[(UUID, Long)]): Seq[(TaskGenerator, Long)] = Seq()
  }