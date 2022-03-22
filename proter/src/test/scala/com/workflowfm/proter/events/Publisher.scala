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
        _ <- subresource.use { stream => for {
          subio <- IO.delay(stream.compile.drain)
          pubio <- IO.delay(publisher.publish(EDone("Test", 0L)))
          _ <- (pubio, subio).parMapN { (_, _) => () }
        } yield () }
        assertion <- handler.complies()
      } yield (assertion)
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
}
