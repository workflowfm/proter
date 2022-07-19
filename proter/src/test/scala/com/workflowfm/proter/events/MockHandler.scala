package com.workflowfm.proter.events

import cats.effect.{ IO, Ref }
import cats.effect.implicits.*

import scala.collection.immutable.Queue

case class MockHandler(calls: Ref[IO, Queue[MockHandler.Call]], expected: Seq[MockHandler.Call])
    extends EventHandler[IO] {
  import MockHandler.*

  override def init(): IO[Unit] = calls.update(_ :+ Init)
  override def onEvent(event: Event): IO[Unit] = calls.update(_ :+ OnEvent(event))
  override def onDone(): IO[Unit] = calls.update(_ :+ Done)

  override def onFail(e: Throwable): IO[Unit] =
    calls.update(_ :+ OnFail(e))
}

object MockHandler {
  sealed trait Call

  case object Init extends Call
  case class OnEvent(e: Event) extends Call
  case class OnFail(f: Throwable) extends Call
  case object Done extends Call

  def withCalls(expected: Call*): IO[MockHandler] = for {
    ref <- Ref[IO].of(Queue[Call]())
  } yield MockHandler(ref, expected)

  def of(expected: Event*): IO[MockHandler] = withCalls(Init +: (expected.map(OnEvent(_))) :+ Done*)

  def failing(ex: Throwable, expected: Event*): IO[MockHandler] = withCalls(
    Init +: (expected.map(OnEvent(_))) :+ OnFail(ex)*
  )
}
