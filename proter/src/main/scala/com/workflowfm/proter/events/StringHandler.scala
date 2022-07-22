package com.workflowfm.proter
package events

import java.text.SimpleDateFormat

import cats.Applicative
import cats.implicits.*
import cats.effect.{ Clock, Sync }
import cats.effect.implicits.*

import fs2.{ Stream, Pipe }

trait StringHandler[F[_] : Applicative : Clock] extends TimedHandler[F] {
  def strPipe(newlines: Boolean): Pipe[F, Either[Throwable, Event], String] = 
    _.through(timedEventPipe).map(timedEventToString(newlines))
  /**
    * A simple date formatter for printing the current (system) time.
    */
  val formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS")

  def timedEventToString(newlines: Boolean)(e: TimedEvent): String = {
    val time = formatter.format(e.time)
    val str = s"[$time] ${Event.asString(e.event)}"
    if newlines then str + "\n" else str
  }

}


/**
  * An [[EventHandler]] that prints events to standard error.
  */
class PrintEvents[F[_] : Clock : Sync] 
    extends Subscriber[F]
    with StringHandler[F]
{

  override def apply(s: Stream[F, Either[Throwable, Event]]): Stream[F, Unit] = 
    s.through(strPipe(true))
      .through(fs2.io.stdoutLines[F, String]())
      .map(_ => ())

  override def init(): F[Unit] = Applicative[F].pure(())

}

