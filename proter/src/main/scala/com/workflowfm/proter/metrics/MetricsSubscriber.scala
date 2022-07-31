package com.workflowfm.proter
package metrics

import cats.Applicative
import cats.implicits.*
import cats.effect.Concurrent
import fs2.Stream
import events.{ Event, Subscriber }

/**
  * A [[events.Subscriber Subscriber]] that compiles and outputs [[Metrics]].
  *
  * Runs all outputs concurrently.
  *
  * @param outputs
  *   The [[MetricsOutput]]s to run at the end of the stream.
  */
final case class MetricsParSubscriber[F[_] : Concurrent](outputs: MetricsOutput[F]*)
    extends Subscriber[F] {
  override def init(): F[Unit] = Concurrent[F].pure(())

  override def apply(s: Stream[F, Either[Throwable, Event]]): Stream[F, Unit] =
    s.fold(Metrics())((m, e) =>
      e match {
        case Left(t) => m
        case Right(evt) => m.handle(evt)
      }
    ).broadcastThrough(outputs.map(_.pipe): _*)
}

/**
  * A [[events.Subscriber Subscriber]] that compiles and outputs [[Metrics]].
  *
  * Runs all outputs in sequence.
  *
  * @param outputs
  *   The [[MetricsOutput]]s to run at the end of the stream.
  */
final case class MetricsSubscriber[F[_] : Applicative](outputs: MetricsOutput[F]*)
    extends Subscriber[F] {
  override def init(): F[Unit] = Applicative[F].pure(())

  override def apply(s: Stream[F, Either[Throwable, Event]]): Stream[F, Unit] =
    s.fold(Metrics())((m, e) =>
      e match {
        case Left(t) => m
        case Right(evt) => m.handle(evt)
      }
    ).evalMap(m => outputs.map(_(m)).sequence.void)
}
