package com.workflowfm.proter
package events

import cats.Applicative
import cats.implicits.*
import cats.effect.Clock
import cats.effect.implicits.*

import fs2.{ Pipe, Stream }

final case class TimedEvent(time: Long, event: Event)

trait TimedHandler[F[_] : Applicative : Clock] {

  val timedEventPipe: Pipe[F, Either[Throwable, Event], TimedEvent] = s =>
    s.evalMap(evt =>
      evt match {
        case Left(e) => fail(e)
        case Right(e) => event(e)
      }
    ).handleErrorWith(ex => Stream.eval(fail(ex)))

  def event(e: Event): F[TimedEvent] = for {
    t <- Clock[F].realTime
  } yield TimedEvent(t.toMillis, e)

  def fail(e: Throwable): F[TimedEvent] = for {
    t <- Clock[F].realTime
  } yield TimedEvent(t.toMillis, EError("*FATAL ERROR*", Long.MaxValue, e.getLocalizedMessage))

}
