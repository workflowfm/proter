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
case class Publisher[F[_]](topic: Topic[F, Event], maxQueued: Int)(using MonadCancel[F, Throwable]) {

  /**
    * Publishes an event into the stream.
    *
    * @param evt
    *   The event to publish.
    */
  def publish(evt: Event): F[Either[Topic.Closed, Unit]] = topic.publish1(evt)

  def error(e: Throwable): F[Unit] = {
    val x =topic.publish(Stream.raiseError(e))
    }

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
      evt => subscriber.onEvent(evt)
    ).handleErrorWith (
      ex => Stream.eval(subscriber.onFail(ex, this))
    )
  }

  /**
    * Wrapper method for [[doPublish]] that also stops the stream upon final events.
    *
    * @param evt
    */
 /* def publish(evt: Event): Unit = {
    doPublish(evt)
    if isFinalEvent(evt) then stopStream()
  }*/
}

