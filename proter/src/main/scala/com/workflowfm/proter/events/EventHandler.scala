package com.workflowfm.proter.events

import java.text.SimpleDateFormat

import cats.Monad
import cats.implicits.*
import cats.effect.{ Ref, Deferred }
import cats.effect.std.Console

import fs2.Stream

import scala.collection.immutable.HashSet
import scala.concurrent.Promise

/**
  * A handler of [[Event]]s.
  *
  * Handles a stream of events from a [[Publisher]].
  */
trait EventHandler[F[_] : Monad] extends Subscriber[F] {

  override def apply(s: Stream[F, Either[Throwable, Event]]): Stream[F, Unit] =
    s.evalMap(evt =>
      evt match {
        case Left(e) => onFail(e)
        case Right(e) => {
          e match {
            case EDone(_, _) => onEvent(e) >> onDone()
            case _ => onEvent(e)
          }
        }
      }
    )
      .handleErrorWith(ex => Stream.eval(onFail(ex)))
 

  /**
    * Handles the initialisation of a new event stream.
    *
    * @param publisher
    *   The [[Publisher]] that started the stream.
    */
  override def init(): F[Unit] = Monad[F].pure(())

  /**
    * Handles an [[Event]] in the stream.
    *
    * @param event
    *   The [[Event]] to handle.
    */
  def onEvent(event: Event): F[Unit] = Monad[F].pure(())

  /**
    * Handles the end of a stream.
    *
    * @param publisher
    *   The [[Publisher]] that ended the stream.
    */
  def onDone(): F[Unit] = Monad[F].pure(())

  /**
    * Handles an error in the stream.
    *
    * @param e
    *   The throwable error that occurred.
    * @param publisher
    *   The [[Publisher]] that threw the error.
    */
  def onFail(e: Throwable): F[Unit] = Monad[F].pure(())
}

/*
/**
  * An [[EventHandler]] for a pool of [[Coordinator]]s.
  */
class PoolEventHandler[F[_] : Monad](publishers: Ref[F, HashSet[Publisher[?]]])
    extends EventHandler[F] {

  /**
    * @inheritdoc
    *
    * Adds the publisher to [[publishers]].
    */
  override def onInit(publisher: Publisher[?]): F[Unit] =
    publishers.update(_ + publisher)

  /**
    * @inheritdoc
    *
    * Closes the event stream by removing the publisher from [[publishers]].
    */
  override def onDone(publisher: Publisher[?]): F[Unit] =
    publishers.update(_ - publisher)

  /**
    * @inheritdoc
    *
    * Closes the event stream by removing the publisher from [[publishers]].
    */
  override def onFail(e: Throwable, publisher: Publisher[?]): F[Unit] =
    publishers.update(_ - publisher)
}
 */

/**
  * An [[EventHandler]] that prints events to standard error.
  */
class PrintEventHandler[F[_] : Monad : Console] extends EventHandler[F] {
  /**
    * A simple date formatter for printing the current (system) time.
    */
  val formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS")

  override def init(): F[Unit] = {
    val time = formatter.format(System.currentTimeMillis())
    Console[F].println(s"[$time] === Event stream started. ===")
  }

  override def onEvent(e: Event): F[Unit] = {
    val time = formatter.format(System.currentTimeMillis())
    Console[F].println(s"[$time] ${Event.asString(e)}")
  }

  override def onDone(): F[Unit] = {
    val time = formatter.format(System.currentTimeMillis())
    Console[F].println(s"[$time] === Event stream ended. ===")
  }

  override def onFail(e: Throwable): F[Unit] = {
    val time = formatter.format(System.currentTimeMillis())
    Console[F].println(s"[$time] ??? Failure: ${e.getLocalizedMessage}")
  }

}

/**
  * An [[EventHandler]] with a measured result.
  * @tparam R
  *   The type of the result.
  */
trait ResultHandler[F[_] : Monad, R](val result: Deferred[F, R]) extends EventHandler[F]

/**
  * A [[ResultHandler]] that counts the events seen.
  */
class CounterHandler[F[_] : Monad](r: Deferred[F, Int], counter: Ref[F, Int])
    extends ResultHandler[F, Int](r) {

  /**
    * @inheritdoc
    *
    * Resets the counter to 0.
    */
  override def init(): F[Unit] =
    counter.set(0)

  /**
    * @inheritdoc
    *
    * Increases the counter by one.
    */
  override def onEvent(e: Event): F[Unit] =
    counter.update(_ + 1)

  override def onDone(): F[Unit] =
    counter.get.flatMap(result.complete).void

  override def onFail(e: Throwable): F[Unit] =
    counter.get.flatMap(result.complete).void

}

/**
  * Listens for the end of a named simulation and handles its result.
  *
  * @param name
  *   The name of the [[SimulationRef]] to listen for.
  * @param callback
  *   A function to handle the results of the simulation when it completes.
  */
class CaseResultHandler[F[_] : Monad](caseName: String, r: Deferred[F, String])
    extends ResultHandler[F, String](r) {

  /**
    * @inheritdoc
    *
    * If the event is [[ESimEnd]] and the simulation name matches then we record the simulation
    * result in [[simResult]], unsubscribe, and call the callback function.
    */
  override def onEvent(evt: Event): F[Unit] = evt match {
    case ECaseEnd(_, _, n, r) if n == caseName => {
//      publisher.map(_.unsubscribe(this))
      result.complete(r.toString).void
    }
    case _ => super.onEvent(evt)
  }
}
