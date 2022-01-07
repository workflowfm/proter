package com.workflowfm.proter

import java.util.UUID

import scala.collection.immutable.{ Map, Queue }

/**
  * A Lookahead contains information about the order in which tasks run.
  *
  * The structure is immutable. It is built by the simulation and passed to the scheduler where it
  * is used for scheduling by revealing information about future tasks so that look-ahead can be
  * achieved.
  */
trait Lookahead {
  /**
    * Removes a task with the corresponding ID from the structure
    *
    * Once removed, the task with this ID will no longer be returned.
    *
    * @param id
    *   The id of the task
    * @return
    *   A Lookahead with the specified task removed.
    */
  def -(id: UUID): Lookahead

  /**
    * Adds an entry to the structure
    *
    * An entry is comprised of a function that describes the prerequisites and resultant starting
    * time of a set of tasks, and a list of [[Task]]s that should start if this function returns
    * `Some(value)`.
    *
    * The function has type `Map[UUID,Long]=>Option[Long]`. It should take a map that contains the
    * IDs of completed/scheduled tasks and their respective finishing times, and it should return
    * `None` if the prerequisites of this entry are not met, and `Some(value)` if the prerequisites
    * are met, where `value` should be the starting time of the tasks contained in this entry. This
    * allows us to express a complex relationship whereby the starting time of the tasks in this
    * lookahead entry can be an intricate function of the finishing times of the prerequisites.
    *
    * @param function
    *   The function that describes the prerequisites of this entry. Should return `None` if the
    *   prerequisites are not met, or `Some(value)` if the prerequistes are met, where the value is
    *   the starting time of the tasks in this entry.
    * @param generators
    *   A list of [[Task]]s that describes the tasks that should start if the prerequistes are met.
    * @return
    *   A Lookahead with the specified entry added to it.
    */
  def ++(function: Map[UUID, Long] => Option[Long], generators: List[Task]): Lookahead

  /**
    * Retrieves all tasks that can start given the list of scheduled/completed tasks
    *
    * @param scheduled
    *   The list of tasks that have been scheduled/completed, described with an (ID,time) tuple
    * @return
    *   The list of tasks that can start, described by (Task,starting_time) tuples
    */
  def getTaskData(scheduled: Iterable[(UUID, Long)]): Seq[(Task, Long)]

  /**
    * Provides a nicer interface for adding elements to the lookeahead structure.
    *
    * Allows adding a single generator instead of a list of generators.
    *
    * @see
    *   [[Lookahead.++]]
    *
    * @param function
    *   The function describing the conditions of starting the correspoinding task.
    * @param generator
    *   The task that will start when the conditions are met.
    * @return
    */
  /* todo e.g. // e.g. [[abortSimulation(name:String,actor:akka\.actor\.ActorRef)*
   * abortSimulation]]. */
  def +(function: Map[UUID, Long] => Option[Long], generator: Task): Lookahead =
    this.++(function, List(generator))

  /**
    * Provides a nicer interface for adding elements to the lookeahead structure.
    *
    * Allows adding a simple one-to-one relationship, where the generator task starts right after
    * the sourceID task finishes.
    *
    * @see
    *   [[Lookahead.++]]
    *
    * @param sourceID
    *   The id of the task that will finish.
    * @param generator
    *   The generator fo the task that will start when the prior task finishes.
    * @return
    */
  def +>(sourceID: UUID, generator: Task): Lookahead = {
    val function: Map[UUID, Long] => Option[Long] = { s =>
      s.get(sourceID)
    }
    this.+(function, generator)
  }

  def +>>(source: Set[UUID], generator: Task): Lookahead = {
    val function: Map[UUID, Long] => Option[Long] = { s =>
      val times = source map (s.get(_))
      if (times.contains(None)) None
      else Some((times map (_.get)).max)
    }
    this.+(function, generator)
  }

  /**
    * Combines two Lookaheads
    *
    * @param that
    *   The other Lookahead to merge with this one.
    * @return
    *   The merged Lookahead
    */
  def and(that: Lookahead): Lookahead = {
    that match {
      case NoLookahead => this
      case Lookaheads(_) => that and this
      case _ => Lookaheads(this, that)
    }
  }
}

/**
  * A Structure used for combining multiple [[Lookahead]]s into one.
  *
  * @param lookaheads
  *   A queue of the [[Lookahead]]s which are included in this strucutre.
  */
case class Lookaheads(lookaheads: Queue[Lookahead]) extends Lookahead {
  /**
    * @inheritdoc
    */
  override def -(id: UUID): Lookahead = Lookaheads(lookaheads map (_.-(id)))

  /**
    * @inheritdoc
    */
  override def ++(
      function: Map[UUID, Long] => Option[Long],
      generators: List[Task]
  ): Lookahead = Lookaheads(lookaheads map (_.++(function, generators)))

  /**
    * @inheritdoc
    */
  override def getTaskData(scheduled: Iterable[(UUID, Long)]): Seq[(Task, Long)] =
    lookaheads flatMap (_.getTaskData(scheduled))

  /**
    * @inheritdoc
    */
  override def and(that: Lookahead): Lookahead = {
    that match {
      case NoLookahead => this
      case Lookaheads(h) => copy(lookaheads = lookaheads ++ h)
      case _ => copy(lookaheads = lookaheads :+ that)
    }
  }
}

object Lookaheads {

  /** Shorthand constructor for a [[Lookaheads]] from a list of [[Lookahead]]s. */
  def apply(lookaheads: Lookahead*): Lookaheads = Lookaheads(
    Queue[Lookahead]() ++ lookaheads
  )
}

/**
  * A [[Lookahead]] that uses a set in the implementation
  *
  * @see
  *   [[Lookahead]]
  *
  * @param lookaheadSet
  *   The initial set used. Empty by default.
  * @param completed
  *   The initial completed tasks. Empty by default.
  */
case class LookaheadSet(
    lookaheadSet: Set[(Map[UUID, Long] => Option[Long], List[(Task)])] = Set()
) extends Lookahead {

  /**
    * @inheritdoc
    */
  override def -(id: UUID): Lookahead = {
    LookaheadSet(
      lookaheadSet.filter {
        // remove all entries that spawn this task
        entry => entry._2 forall (data => data.id != Some(id))
      }
    )
  }

  /**
    * @inheritdoc
    */
  override def ++(
      function: Map[UUID, Long] => Option[Long],
      generators: List[Task]
  ): Lookahead = {
    copy(lookaheadSet = lookaheadSet + ((function, generators)))
  }

  /**
    * @inheritdoc
    */
  override def getTaskData(scheduled: Iterable[(UUID, Long)]): Seq[(Task, Long)] = {
    val y = lookaheadSet flatMap { x: (Map[UUID, Long] => Option[Long], Seq[Task]) =>
      x match {
        case (function, data) =>
          val l = function((scheduled).foldLeft(Map.empty[UUID, Long]) { (a, b) =>
            a + ((b._1, b._2))
          })
          l match {
            case None => Seq()
            case Some(v) => (data map ((_, v))).toSeq
          }
      }
    }
    y.toSeq
  }
}

/**
  * An empty [[Lookahead]] that does nothing.
  */
case object NoLookahead extends Lookahead {
  /**
    * @inheritdoc
    */
  override def -(id: UUID): Lookahead = NoLookahead

  /**
    * @inheritdoc
    */
  override def ++(
      function: Map[UUID, Long] => Option[Long],
      generators: List[Task]
  ): Lookahead = LookaheadSet() ++ (function, generators)
  /**
    * @inheritdoc
    */
  override def getTaskData(scheduled: Iterable[(UUID, Long)]): Seq[(Task, Long)] = Seq()
}
