package com.workflowfm.proter

import cases.CaseRef

/**
  * Discrete Events that need to be handled by the [[Coordinator]]..
  */
sealed trait DiscreteEvent extends Ordered[DiscreteEvent] {
  /** The virtual timestamp of the event */
  def time: Long

  /**
    * A value to allow ordering of [[DiscreteEvent]] subclasses.
    *
    * A lower class order means all events of that subclass will be handled before those of a
    * subclass with higher class order.
    */
  val classOrder: Short

  /**
    * Comparison method to order [[DiscreteEvent]]s
    *
    * Orders based on timestamp first (earlier first), class order second (lower first), and finally
    * using [[sameClassCompare]].
    *
    * @param that
    *   Another event to compare to.
    * @return
    *   The relative order of the events: lower means this event comes first
    */
  def compare(that: DiscreteEvent): Int = {
    lazy val tOrder = this.time.compare(that.time)
    lazy val cOrder = this.classOrder.compare(that.classOrder)
    lazy val vOrder = sameClassCompare(that)

    if tOrder != 0 then tOrder
    else if cOrder != 0 then cOrder
    else if vOrder != 0 then vOrder
    else this.hashCode().compare(that.hashCode())
  }

  /**
    * A method to compare events of the same subclass of [[DiscreteEvent]]s.
    *
    * @param that
    *   Another event to compare to, assumed to be of the same subclass.
    * @return
    *   The relative order of the events: lower means this event comes first
    */
  protected def sameClassCompare(that: DiscreteEvent): Int
}

/**
  * Event fired when a [[Task]] has finished.
  *
  * @param time
  *   The timestamp of the event
  * @param task
  *   The [[Task]] that was finished.
  */
case class FinishingTask(override val time: Long, task: TaskInstance) extends DiscreteEvent {
  override val classOrder: Short = 5

  override protected def sameClassCompare(that: DiscreteEvent): Int = that match {
    case FinishingTask(_, t) => task.compare(t)
    case _ => 0
  }
}

/**
  * Event fired when a simulation needs to start.
  *
  * @param time
  *   The timestamp of the event
  * @param simulation
  *   The [[Simulation]] that needs to start.
  */
case class StartingCase(override val time: Long, caseRef: CaseRef[?]) extends DiscreteEvent {
  override val classOrder: Short = 10

  override def sameClassCompare(that: DiscreteEvent): Int = that match {
    case StartingCase(_, c) => caseRef.caseName.compareTo(c.caseName)
    case _ => 0
  }
}

/**
  * Event fired when a global time limit has been reached.
  *
  * @param time
  *   The timestamp of the event
  */
case class TimeLimit(override val time: Long) extends DiscreteEvent {
  override val classOrder: Short = Short.MaxValue

  override def sameClassCompare(that: DiscreteEvent): Int = 0
}

/**
  * Event used to model a repeating process.
  *
  * An arrival rate is used to indicate when new instances of a simulation should be added to the
  * coordinator.
  *
  * @param time
  *   The timestamp of the event
  * @param rate
  *   The arrival rate of the simulation
  * @param simulationGenerator
  *   The simulation generator for getting new instances of a simulation
  * @param count
  *   A counter of the next simulation instance that will be generated
  */
case class Arrival(
    override val time: Long,
    rate: Distribution,
    simulationGenerator: SimulationRefGenerator,
    limit: Option[Int] = None,
    count: Int = 0
) extends DiscreteEvent {

  import cats.effect.std.Random
  import cats.Applicative
  import cats.implicits.*

  override val classOrder: Short = 11

  override def sameClassCompare(that: DiscreteEvent): Int = that match {
    // case Arrival(_, r) => rate.compareTo(r) TODO Need to figure out a way to sort arrivals!
    case _ => 0
  }

  /**
    * Generates the next arrival event to be queued.
    *
    * @return
    *   The next arrival event.
    */
  def next[F[_] : Applicative : Random](): F[Arrival] = 
    rate.get.map { r => copy(time = time + r.round, count = count + 1) }
}



import collection.immutable.{ SortedMap, SortedSet }

case class EventQueue(events: SortedMap[Long, SortedSet[DiscreteEvent]]) {
  def +(event: DiscreteEvent): EventQueue = {
    copy(events = events.updatedWith(event.time) {
      case None => Some(SortedSet(event))
      case Some(s) => Some(s + event)
    })
  }

  def next(): Option[(SortedSet[DiscreteEvent], EventQueue)] = events.headOption.map { (k,v) => 
    (v, copy(events = events - k))
  }

  def size: Int = events.size

  def isEmpty: Boolean = events.isEmpty
}
