package com.workflowfm.proter.events

import cats.Monad
import cats.effect.{ Resource, MonadCancel, Concurrent }
import cats.implicits.*
import fs2.Stream
import fs2.concurrent.Topic
import java.util.UUID

/**
  * A publisher of a stream of simulation [[Event]]s.
  */
case class Publisher[F[_]](topic: Topic[F, Either[Throwable, Event]], maxQueued: Int)(
    using MonadCancel[F, Throwable]
) {

  /**
    * Publishes an event into the stream.
    *
    * @param evt
    *   The event to publish.
    */
  def publish(evt: Event): F[Either[Topic.Closed, Unit]] = {
    val pub = topic.publish1(Right(evt))
    evt match {
      // case EError(_, _, _) => pub >> stop()
      case EDone(_, _) => pub >> stop()
      case _ => pub
    }
  }

  def fail(e: Throwable): F[Either[Topic.Closed, Unit]] = topic.publish1(Left(e)) >> stop()

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
  def subscribe(subscriber: EventHandler[F]): Resource[F, Stream[F, Unit]] =
    topic.subscribeAwait(maxQueued).evalMap { sub =>
      for {
        _ <- subscriber.onInit(this)
      } yield sub
        .evalMap(evt =>
          evt match {
            case Left(e) => subscriber.onFail(e, this)
            case Right(e) => {
              e match {
                case EDone(_, _) => subscriber.onEvent(e) >> subscriber.onDone(this)
                case _ => subscriber.onEvent(e)
              }
            }
          }
        )
        .handleErrorWith(ex => Stream.eval(subscriber.onFail(ex, this)))
    }
}

object Publisher {

  def build[F[_] : Concurrent](maxQueued: Int = 10): F[Publisher[F]] = for {
    topic <- Topic[F, Either[Throwable, Event]]
  } yield (Publisher[F](topic, 10))
}
