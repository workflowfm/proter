package com.workflowfm.proter
package events

import java.text.SimpleDateFormat

import cats.Applicative
import cats.implicits.*
import cats.effect.{ Clock, Sync }
import cats.effect.implicits.*

import fs2.{ Stream, Pipe }

import io.circe.{ Json, Encoder }
import io.circe.generic.semiauto._
import io.circe.syntax.*

/**
  * A [[TimedHandler]] that produces a JSON stream.
  */
trait JsonHandler[F[_] : Applicative : Clock] extends TimedHandler[F] {
  import JsonHandler.given

  /**
    * A pipe to process the [[Publisher]] stream and produce JSON objects.
    */
  val jsonPipe: Pipe[F, Either[Throwable, Event], Json] =
    _.through(timedEventPipe).map(_.asJson)
}

object JsonHandler {
  given Encoder[TaskInstance] = deriveEncoder[TaskInstance]
  // These are redundant, but help the deriveEncoder macro avoid
  // a "Maximal number of successive inlines exceeded" error
  // without arbitrarily changing the -Xmax-inlines flag.
  given Encoder[ETaskAttach] = deriveEncoder[ETaskAttach]
  given Encoder[ETaskDetach] = deriveEncoder[ETaskDetach]
  given Encoder[Event] = deriveEncoder[Event]
  given Encoder[TimedEvent] = deriveEncoder[TimedEvent]
}

/**
  * An [[Event]] handler that converts them to JSON and prints them to console.
  */
class PrintJsonEvents[F[_] : Clock : Sync] extends Subscriber[F] with JsonHandler[F] {

  override def apply(s: Stream[F, Either[Throwable, Event]]): Stream[F, Unit] =
    s.through(jsonPipe)
      .map(_.noSpaces.toString() + "\n")
      .through(fs2.io.stdoutLines[F, String]())
      .map(_ => ())

  override def init(): F[Unit] = Applicative[F].pure(())

}
