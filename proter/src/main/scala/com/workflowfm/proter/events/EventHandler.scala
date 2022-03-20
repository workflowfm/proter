package com.workflowfm.proter.events

import java.text.SimpleDateFormat

import cats.Monad
import cats.implicits._

import scala.collection.mutable.HashSet
import scala.concurrent.Promise

/**
  * A handler of [[Event]]s.
  *
  * Handles a stream of events from a [[Publisher]].
  */
trait EventHandler {

  /**
    * Handles the initialisation of a new event stream.
    *
    * @param publisher
    *   The [[Publisher]] that started the stream.
    */
  def onInit[F[_]: Monad](publisher: Publisher[_]): F[Unit] = Monad[F].pure(())

  /**
    * Handles an [[Event]] in the stream.
    *
    * @param event
    *   The [[Event]] to handle.
    */
  def onEvent[F[_]: Monad](event: Event): F[Unit] = Monad[F].pure(())

  /**
    * Handles the end of a stream.
    *
    * @param publisher
    *   The [[Publisher]] that ended the stream.
    */
  def onDone[F[_]: Monad](publisher: Publisher[F]): F[Unit] = Monad[F].pure(())

  /**
    * Handles an error in the stream.
    *
    * @param e
    *   The throwable error that occurred.
    * @param publisher
    *   The [[Publisher]] that threw the error.
    */
  def onFail[F[_]: Monad](e: Throwable, publisher: Publisher[F]): F[Unit] = Monad[F].pure(())
}

/*
/**
  * An [[EventHandler]] for a pool of [[Coordinator]]s.
  */
trait PoolEventHandler extends EventHandler {

  /**
    * The set of [[Publisher]]s we have subscribed to.
    */
  val publishers: HashSet[Publisher] = HashSet[Publisher]()

  /**
    * @inheritdoc
    *
    * Adds the publisher to [[publishers]].
    */
  override def onInit(publisher: Publisher): Unit = {
    publishers += publisher
  }

  /**
    * @inheritdoc
    *
    * Closes the event stream by removing the publisher from [[publishers]].
    */
  override def onDone(publisher: Publisher): Unit = {
    publishers -= publisher
  }

  /**
    * @inheritdoc
    *
    * Closes the event stream by removing the publisher from [[publishers]].
    */
  override def onFail(e: Throwable, publisher: Publisher): Unit = {
    publishers -= publisher
  }
}

/**
  * An [[EventHandler]] that prints events to standard error.
  */
class PrintEventHandler extends EventHandler {
  /**
    * A simple date formatter for printing the current (system) time.
    */
  val formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS")

  override def onEvent(e: Event): Unit = {
    val time = formatter.format(System.currentTimeMillis())
    System.err.println(s"[$time] ${Event.asString(e)}")
  }
}

/**
  * An [[EventHandler]] with a measured result.
  * @tparam R
  *   The type of the result.
  */
trait ResultHandler[R] extends EventHandler {
  /**
    * The result of the handler.
    *
    * @return
    *   The result of the handler.
    */
  def result: R
}

/**
  * A [[ResultHandler]] that counts the events seen.
  */
class CounterHandler extends ResultHandler[Int] {
  /**
    * The counter of handled events.
    */
  var count = 0

  /**
    * @inheritdoc
    *
    * Resets the counter to 0.
    */
  override def onInit(publisher: Publisher): Unit = count = 0

  /**
    * @inheritdoc
    *
    * Increases the counter by one.
    */
  override def onEvent(e: Event): Unit = count = count + 1

  /**
    * @inheritdoc
    *
    * Simply returns the counter.
    */
  override def result = count
}

/**
  * A wrapper for a [[ResultHandler]] that fulfills a [[scala.concurrent.Promise Promise]] with the
  * result.
  *
  * This is also itself a [[ResultHandler]].
  *
  * @param handler
  *   The [[ResultHandler]] that calculates the result.
  */
class PromiseHandler[R](handler: ResultHandler[R]) extends ResultHandler[R] {
  /**
    * The promise to be fulfilled with the result.
    */
  protected val promise: Promise[R] = Promise[R]()

  /**
    * Returns a [[scala.concurrent.Future Future]] of the result.
    *
    * @return
    *   A future result.
    */
  def future = promise.future

  /**
    * @inheritdoc
    *
    * Delegates this to the result handler.
    */
  override def onInit(publisher: Publisher): Unit = handler.onInit(publisher)

  /**
    * @inheritdoc
    *
    * Delegates this to the result handler.
    */
  override def onEvent(e: Event): Unit = handler.onEvent(e)

  /**
    * @inheritdoc
    *
    * Delegates this to the result handler and then fulfills the promise.
    */
  override def onDone(publisher: Publisher): Unit = {
    handler.onDone(publisher)
    if !promise.isCompleted then promise.success(result)
  }

  /**
    * @inheritdoc
    *
    * Delegates this to the result handler and then fulfills the promise with a failure.
    */
  override def onFail(ex: Throwable, publisher: Publisher): Unit = {
    handler.onFail(ex, publisher)
    if !promise.isCompleted then promise.failure(ex)
  }

  /**
    * @inheritdoc
    *
    * Delegates this to the result handler.
    */
  override def result = handler.result
}

object PromiseHandler {
  /**
    * Declarative constructor for a [[PromiseHandler]] given a [[ResultHandler]].
    *
    * @param handler
    *   The [[ResultHandler]] to use.
    * @return
    *   The constructed [[PromiseHandler]].
    */
  def of[R](handler: ResultHandler[R]): PromiseHandler[R] = new PromiseHandler[R](handler)
}

/**
  * Listens for the end of a named simulation and handles its result.
  *
  * @param name
  *   The name of the [[SimulationRef]] to listen for.
  * @param callback
  *   A function to handle the results of the simulation when it completes.
  */
class SimulationResultHandler(name: String, callback: String => Unit = { _ => () })
    extends ResultHandler[Option[String]] {

  /**
    * The [[Publisher]] of the stream.
    *
    * We assume to be subscribed to a single publisher. We then keep track of it so we can
    * unsubscribe as soon as we get our result.
    */
  var publisher: Option[Publisher] = None

  /**
    * The result of the simulation when we get it.
    */
  var simResult: Option[String] = None

  /**
    * @inheritdoc
    *
    * Sets the [[publisher]].
    */
  override def onInit(publisher: Publisher): Unit = this.publisher = Some(publisher)

  /**
    * @inheritdoc
    *
    * If the event is [[ESimEnd]] and the simulation name matches then we record the simulation
    * result in [[simResult]], unsubscribe, and call the callback function.
    */
  override def onEvent(evt: Event): Unit = evt match {
    case ESimEnd(_, _, n, r) if n == name => {
      publisher.map(_.unsubscribe(this))
      simResult = Some(r)
      callback(r)
    }
    case _ => ()
  }

  /**
    * @inheritdoc
    *
    * @return
    *   The result of the simulation or `None` if we have not received one (yet).
    */
  override def result = simResult
}
 */
