package com.workflowfm.proter.events

import cats.implicits.*
import cats.effect.{ IO, Ref, Resource }
import cats.effect.implicits.*
import cats.effect.testing.scalatest.AsyncIOSpec

import fs2.concurrent.Topic

import scala.annotation.tailrec
import scala.collection.immutable.Queue

import org.scalatest.Assertion
import org.scalatest.matchers.{ MatchResult, Matcher }
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class PublisherTests extends PublisherTester {
  import MockHandler.*

  "The Publisher" should {

    "Init and Done one subscriber" in {
      for {
        topic <- Topic[IO, Either[Throwable, Event]]
        publisher = Publisher[IO](topic, 10)
        handler <- MockHandler.of(EDone("Test", 0L))
        subresource = publisher.subscribe(handler)
        _ <- subresource.use { stream =>
          {
            val subio = stream.compile.drain
            val pubio = publisher.publish(EDone("Test", 0L))
            (pubio, subio).parTupled
          }
        }
        assertion <- handler.complies()
      } yield (assertion)
    }

    "Init and Done multiple subscribers" in {
      val subs = 4
      val handlersIO =
        (for (i <- 1 to subs) yield MockHandler.of(EDone("Test", 0L))).toList.sequence

      for {
        topic <- Topic[IO, Either[Throwable, Event]]
        publisher = Publisher[IO](topic, 10)
        handlers <- handlersIO
        subresource = handlers.map(publisher.subscribe(_)).sequence
        _ <- subresource.use { streams =>
          {
            val subio = streams.map(_.compile.drain).sequence
            val pubio = publisher.publish(EDone("Test", 0L))
            (pubio, subio).parTupled
          }
        }
        assertions <- handlers.map(_.complies()).sequence
      } yield (assertions.last) // Just calculating all assertions is enough.
      // They throw exceptions if they fail.
    }

    "Stream events to one subscriber" in {
      val events = Seq(
        EStart("Start0", 0L),
        EStart("Start1", 1L),
        EStart("Start2", 2L),
        EStart("Start3", 3L),
        EDone("Test", 0L)
      )

      for {
        topic <- Topic[IO, Either[Throwable, Event]]
        publisher = Publisher[IO](topic, 10)
        handler <- MockHandler.of(events*)
        subresource = publisher.subscribe(handler)
        _ <- subresource.use { stream =>
          {
            val subio = stream.compile.drain
            val pubio = events.map(publisher.publish(_)).sequence
            (pubio, subio).parTupled
          }
        }
        assertion <- handler.complies()
      } yield (assertion)
    }

    "Stream events to multiple subscribers" in {
      val events = Seq(
        EStart("Start0", 0L),
        EStart("Start1", 1L),
        EStart("Start2", 2L),
        EStart("Start3", 3L),
        EDone("Test", 0L)
      )
      val subs = 4
      val handlersIO = (for (i <- 1 to subs) yield MockHandler.of(events*)).toList.sequence

      for {
        topic <- Topic[IO, Either[Throwable, Event]]
        publisher = Publisher[IO](topic, 10)
        handlers <- handlersIO
        subresource = handlers.map(publisher.subscribe(_)).sequence
        _ <- subresource.use { streams =>
          {
            val subio = streams.map(_.compile.drain).sequence
            val pubio = events.map(publisher.publish(_)).sequence
            (pubio, subio).parTupled
          }
        }
        assertions <- handlers.map(_.complies()).sequence
      } yield (assertions.last)
    }

    "Fail to one subscriber" in {
      val events = Seq(
        EStart("Start0", 0L),
        EStart("Start1", 1L),
        EStart("Start2", 2L)
      )

      for {
        topic <- Topic[IO, Either[Throwable, Event]]
        publisher = Publisher[IO](topic, 10)
        handler <- MockHandler.failing(PublisherTestException, events*)
        subresource = publisher.subscribe(handler)
        _ <- subresource.use { stream =>
          {
            val subio = stream.compile.drain
            val pubio =
              (events.map(publisher.publish(_)) :+ publisher.fail(PublisherTestException)).sequence
            (pubio, subio).parTupled
          }
        }
        assertion <- handler.complies()
      } yield (assertion)
    }

    "Fail to multiple subscribers" in {
      val events = Seq(
        EStart("Start0", 0L),
        EStart("Start1", 1L),
        EStart("Start2", 2L)
      )
      val subs = 4
      val handlersIO = (for (i <- 1 to subs)
        yield MockHandler.failing(PublisherTestException, events*)).toList.sequence

      for {
        topic <- Topic[IO, Either[Throwable, Event]]
        publisher = Publisher[IO](topic, 10)
        handlers <- handlersIO
        subresource = handlers.map(publisher.subscribe(_)).sequence
        _ <- subresource.use { streams =>
          {
            val subio = streams.map(_.compile.drain).sequence
            val pubio =
              (events.map(publisher.publish(_)) :+ publisher.fail(PublisherTestException)).sequence
            (pubio, subio).parTupled
          }
        }
        assertions <- handlers.map(_.complies()).sequence
      } yield (assertions.last)
    }

  }
}

trait PublisherTester extends AsyncWordSpec with AsyncIOSpec with Matchers {
  import MockHandler.*

  extension (handler: MockHandler) {

    def complies(): IO[Assertion] = handler.calls.get.asserting { q =>
      q `should` complyWith(handler.expected)
    }
  }

  private def complyWith(expected: Seq[Call]): Matcher[Queue[Call]] = new Matcher[Queue[Call]] {
    def apply(q: Queue[Call]): MatchResult = allCallsMatch(q.toList, expected.toList)
  }

  @tailrec
  private def allCallsMatch(calls: List[Call], expectedCalls: List[Call]): MatchResult =
    (calls, expectedCalls) match {
      case (Nil, Nil) => MatchResult(true, "", "Calls matched")
      case (_, Nil) =>
        MatchResult(false, s"""Unexpected calls: ${calls.mkString(", ")}""", "Calls matched")
      case (Nil, _) =>
        MatchResult(
          false,
          s"""Did not receive expected calls: ${expectedCalls.mkString(", ")}""",
          "Calls matched"
        )
      case (call :: crest, expected :: erest) =>
        if callsMatch(call, expected) then allCallsMatch(crest, erest)
        else MatchResult(false, s"Expected: $expected - but got: $call", "Call matched")
    }

  def callsMatch(call: Call, expected: Call): Boolean = (call, expected) match {
    case (Init, Init) => true
    case (Done, Done) => true
    case (OnEvent(e1), OnEvent(e2)) => e1 `equals` (e2)
    case (OnFail(f1), OnFail(f2)) => f1 `equals` (f2)
    case _ => false
  }

  object PublisherTestException extends Exception("Publisher Test Exception")
}
