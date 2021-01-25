package com.workflowfm.proter.events

import java.text.SimpleDateFormat
import java.util.UUID

import scala.collection.mutable.HashSet
import scala.concurrent.Promise

/**
  * A [[uk.ac.ed.inf.ppapapan.subakka.Subscriber]] for [[Coordinator]] [[Event]]s.
  */
trait EventHandler {
  val id: UUID = UUID.randomUUID

  def onInit(publisher: Publisher): Unit = ()
  def onEvent(event: Event): Unit = ()
  def onDone(publisher: Publisher): Unit = ()
  def onFail(e: Throwable, publisher: Publisher): Unit = ()
}

/**
  * An [[EventHandler]] for a pool of [[Coordinator]]s.
  */
trait PoolEventHandler extends EventHandler {

  /**
    * The set of [[Coordinator]]s we have subscribed to.
    */
  val coordinators: HashSet[Publisher] = HashSet[Publisher]()

  /**
    * Initializes the event stream by adding the coordinator to [[coordinators]].
    *
    * @param a The reference to the [[Coordinator]] actor that initialized the stream.
    */
  override def onInit(c: Publisher): Unit = {
    //log.debug(s"Pool handler adding coordinator: $a")
    coordinators += c
  }

  /**
    * Closes the event stream by removing the coordinator from [[coordinators]].
    *
    * @param a The reference to the [[Coordinator]] that closed the stream.
    * @param s
    */
  override def onDone(c: Publisher): Unit = {
    //log.debug(s"Pool handler done with coordinator: $a")
    coordinators -= c
  }

  override def onFail(e: Throwable, c: Publisher): Unit = {
    //log.debug(s"Pool handler done with coordinator: $a")
    coordinators -= c
  }
}

/**
  * An [[EventHandler]] that prints events to standard error.
  */
class PrintEventHandler extends EventHandler {
  val formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS")

  override def onEvent(e: Event): Unit = {
    val time = formatter.format(System.currentTimeMillis())
    System.err.println(s"[$time] ${Event.asString(e)}")
  }
}

/**
  * An [[EventHandler]] with a measured result.
  * @tparam R The type of the result.
  */
trait ResultHandler[R] extends EventHandler {
  def result: R
}

/**
  * A [[ResultHandler]] that counts the events seen.
  */
class CounterHandler extends ResultHandler[Int] {
  var count = 0

  override def onInit(c: Publisher): Unit = count = 0
  override def onEvent(e: Event): Unit = count = count + 1
  override def result = count
}

/**
  * A wrapper for a [[ResultHandler]] that fulfills a [[scala.concurrent.Promise]] with the result.
  * This is also itself a [[ResultHandler]].
  *
  * @param handler The [[ResultHandler]] that calculates the result.
  */
class PromiseHandler[R](handler: ResultHandler[R]) extends ResultHandler[R] {
  protected val promise: Promise[R] = Promise[R]()
  def future = promise.future

  override def onInit(c: Publisher): Unit = handler.onInit(c)
  override def onEvent(e: Event): Unit = handler.onEvent(e)

  override def onDone(c: Publisher): Unit = {
    handler.onDone(c)
    if (!promise.isCompleted) promise.success(result)
  }

  override def onFail(ex: Throwable, c: Publisher): Unit = {
    handler.onFail(ex, c)
    if (!promise.isCompleted) promise.failure(ex)
  }

  override def result = handler.result
}

object PromiseHandler {
  def of[R](handler: ResultHandler[R]): PromiseHandler[R] = new PromiseHandler[R](handler)
}

/**
  * Listens for the end of a named simulation and handles its result.
  *
  * @param name The name of the [[Simulation]] to listen for.
  * @param callback A function to handle the results of the simulation when it completes.
  */
class SimulationResultHandler(name: String, callback: String => Unit = { _ => Unit })
    extends ResultHandler[Option[String]] {
  var coordinator: Option[Publisher] = None
  var simResult: Option[String] = None

  override def onInit(publisher: Publisher): Unit = coordinator = Some(publisher)

  override def onEvent(evt: Event): Unit = evt match {
    case ESimEnd(_, _, n, r) if n == name => {
      coordinator.map(_.unsubscribe(this))
      simResult = Some(r)
      callback(r)
    }
    case _ => Unit
  }

  override def result = simResult
}
