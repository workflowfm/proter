package com.workflowfm.proter.events

import cats.Monad
import cats.effect.kernel.{ Resource, MonadCancel }
import cats.implicits._
import fs2.Stream
import fs2.concurrent.Topic
import java.util.UUID

/**
  * A publisher of a stream of simulation [[Event]]s.
  */
case class Publisher[F[_]](topic: Topic[F, Either[Throwable, Event]], maxQueued: Int)(using MonadCancel[F, Throwable]) {

  /**
    * Publishes an event into the stream.
    *
    * @param evt
    *   The event to publish.
    */
  def publish(evt: Event): F[Either[Topic.Closed, Unit]] = {
    val pub = topic.publish1(Right(evt))
    evt match {
      case EError(_, _, _) => pub >> stop()
      case EDone(_, _) => pub >> stop()
      case _ => pub
    }
  }

  def error(e: Throwable): F[Either[Topic.Closed, Unit]] = topic.publish1(Left(e)) >> stop()

  /**
    * Performs any cleaning up required when the stream is finished.
    */
  def stop(): F[Either[Topic.Closed, Unit]] = topic.close

  /**
    * Subscribes an [[EventHandler]] to the stream so they can receive events.
    *
    * @param subscriber
    *   The [[EventHandler]] to subscribe.
    */
  def subscribe(subscriber: EventHandler): F[Unit] = topic.subscribeAwait(maxQueued).use { sub => 
    for {
      _ <- subscriber.onInit(this)
    } yield sub.evalMap ( 
      evt => evt match {
        case Left(e) => subscriber.onFail(e, this)
        case Right(e) => subscriber.onEvent(e)
      }
    ).handleErrorWith (
      ex => Stream.eval(subscriber.onFail(ex, this))
    )
  }
}

