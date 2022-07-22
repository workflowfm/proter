package com.workflowfm.proter
package events

import java.text.SimpleDateFormat

import cats.Monad
import cats.implicits.*
import cats.effect.{ Ref, Deferred, Sync, Concurrent }
import cats.effect.implicits.*

import fs2.{ Stream, Pipe }


import scala.collection.immutable.HashSet

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

class CountEvents[F[_] : Monad](counter: Ref[F, Int]) extends EventHandler[F] {
  /**
    * @inheritdoc
    *
    * Increases the counter by one.
    */
  override def onEvent(e: Event): F[Unit] =
    counter.update(_ + 1)

  def get(): F[Int] = counter.get

  def reset(): F[Unit] = counter.set(0)
 
}

object CountEvents {
  def apply[F[_] : Monad : Concurrent]() : F[CountEvents[F]] = for {
    r <- Ref[F].of(0)
  } yield (new CountEvents(r))
}

/**
  * Listens for the end of a named simulation and handles its result.
  *
  * @param name
  *   The name of the [[SimulationRef]] to listen for.
  * @param callback
  *   A function to handle the results of the simulation when it completes.
  */
class GetCaseResult[F[_] : Monad](caseName: String, result: Deferred[F, String]) extends EventHandler[F]  {

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
