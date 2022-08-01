package com.workflowfm.proter.events

import cats.Applicative
import cats.effect.{ Resource, MonadCancel, Concurrent }
import cats.implicits.*
import fs2.Pipe
import fs2.Stream
import fs2.concurrent.Topic
import java.util.UUID

/**
  * A publisher of a stream of simulation [[Event]]s.
  *
  * @param topic
  *   The `fs2.Topic` used to broadcast events to all subscribers.
  * @param maxQueued
  *   The maximum number of elements we can enqueue.
  */
case class Publisher[F[_]](topic: Topic[F, Either[Throwable, Event]], maxQueued: Int)(
    using MonadCancel[F, Throwable]
) {

  /**
    * Publishes an event into the stream.
    *
    * @param evt
    *   The [[Event]] to publish.
    */
  def publish(evt: Event): F[Either[Topic.Closed, Unit]] = {
    val pub = topic.publish1(Right(evt))
    evt match {
      // case EError(_, _, _) => pub >> stop()
      case EDone(_, _) => pub >> stop()
      case _ => pub
    }
  }

  /**
    * Propagates a `Throwable` failure through the stream and then stops it.
    */
  def fail(e: Throwable): F[Either[Topic.Closed, Unit]] = topic.publish1(Left(e)) >> stop()

  /**
    * Performs any cleaning up required when the stream is finished.
    */
  def stop(): F[Either[Topic.Closed, Unit]] = topic.close

  /**
    * Subscribes a [[Subscriber]] to the stream so they can receive events.
    *
    * @param subscriber
    *   The [[Subscriber]].
    * @return
    *   The resulting stream resource.
    */
  def subscribe(subscriber: Subscriber[F]): Resource[F, Stream[F, Unit]] =
    topic.subscribeAwait(maxQueued).evalMap { sub =>
      for {
        _ <- subscriber.init()
      } yield sub.through(subscriber)
    }

  /**
    * Yields a subscriber stream of [[Event]]s directly.
    */
  def stream: Resource[F, Stream[F, Either[Throwable, Event]]] =
    topic.subscribeAwait(maxQueued)
}

object Publisher {

  def build[F[_] : Concurrent](maxQueued: Int = 10): F[Publisher[F]] = for {
    topic <- Topic[F, Either[Throwable, Event]]
  } yield (Publisher[F](topic, 10))
}

/**
  * A subscriber is essentially a pipe of the [[Publisher]] stream to `Unit`.
  */
trait Subscriber[F[_]] extends Pipe[F, Either[Throwable, Event], Unit] {
  /**
    * Method to be called when the stream is initialized.
    */
  def init(): F[Unit]
}

object Subscriber {

  given pipeConv[F[_] : Applicative]
      : Conversion[Pipe[F, Either[Throwable, Event], Unit], Subscriber[F]] with {

    def apply(pipe: Pipe[F, Either[Throwable, Event], Unit]): Subscriber[F] =
      new Subscriber[F] {
        override def apply(s: Stream[F, Either[Throwable, Event]]): Stream[F, Unit] = pipe(s)

        override def init(): F[Unit] = Applicative[F].pure(())
      }
  }
}
