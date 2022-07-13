package com.workflowfm.proter
package cases

import state.StateOps
import state.Simulation.SimState

import java.util.UUID

import scala.annotation.tailrec
import scala.util.Success

import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher

import cats.effect.IO
import cats.effect.implicits.*
import cats.effect.std.{ Random, UUIDGen }
import cats.implicits.*

abstract class MockCaseRef(override val caseName: String) extends CaseRef[IO] {
  import MockCaseRef._
  import collection.mutable.Queue

  val calls: Queue[Call] = Queue()

  override def run(): IO[SimState[IO]] = {
    calls.enqueue(Run)
    IO.pure(react(Run))
  }

  override def stop(): IO[Unit] = {
    calls.enqueue(Stop)
    IO.pure(react(Stop)).void
  }

  override def completed(time: Long, tasks: Seq[TaskInstance]): IO[SimState[IO]] = {
    calls.enqueue(Complete(time, tasks))
    IO.pure(react(Complete(time, tasks)))
  }

  def react(call: Call): SimState[IO]

  def expectedCalls: Seq[Call]
}

trait MockCaseCallMatcher {
  import MockCaseRef._

  def comply: Matcher[MockCaseRef] = MockCaseRefCallComplyMatcher

  object MockCaseRefCallComplyMatcher extends Matcher[MockCaseRef] {

    def apply(sim: MockCaseRef): MatchResult =
      allCallsMatch(sim.calls.toList, sim.expectedCalls.toList)
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
    case (Run, Run) => true
    case (Stop, Stop) => true
    case (Complete(ctime, ctasks), Complete(etime, etasks)) => {
      ctime == etime && tasksMatch(ctasks, etasks)
    }
    case _ => false
  }
}

object MockCaseRef extends StateOps {
  sealed trait Call
  case object NoCall extends Call
  case object Run extends Call
  case object Stop extends Call
  case class Complete(time: Long, tasks: Seq[TaskInstance]) extends Call

  val gen = summon[UUIDGen[IO]]

  def tasksMatch(l: Seq[TaskInstance], r: Seq[TaskInstance]): Boolean =
    l.sorted.corresponds(r.sorted)(_.compare(_) == 0)

  def mockSingleTask(
      name: String,
      expectedCreate: Long,
      duration: Long,
      expectedEnd: Long
  ): IO[MockCaseRef] = for {
    random <- Random.scalaUtilRandom[IO]
    given Random[IO] = random
    id <- gen.randomUUID
    tg = Task("T", duration).withID(id)
    expected <- tg.create(name, expectedCreate)
  } yield (new MockCaseRef(name) {

    override def react(call: Call): SimState[IO] = call match {
      case Run => addTask(caseName)(tg)
      case Complete(time, Seq(task)) if time == expectedEnd && task.compare(expected) == 0 =>
        succeed(())
      case _ => idState
    }

    override def expectedCalls: Seq[Call] = Seq(Run, Complete(expectedEnd, Seq(expected)))
  })

  def mockTwoTasks(
      name: String,
      expectedCreate1: Long,
      duration1: Long,
      expectedEnd1: Long,
      duration2: Long,
      expectedEnd2: Long
  ): IO[MockCaseRef] = for {
    random <- Random.scalaUtilRandom[IO]
    given Random[IO] = random
    id1 <- gen.randomUUID
    tg1 = Task("T1", duration1).withID(id1)
    expected1 <- tg1.create(name, expectedCreate1)
    id2 <- gen.randomUUID
    tg2 = Task("T2", duration2).withID(id2)
    expected2 <- tg2.create(name, expectedEnd1)
  } yield (new MockCaseRef(name) {

    override def react(call: Call): SimState[IO] = call match {
      case Run => addTask(caseName)(tg1)
      case Complete(time, Seq(task)) if time == expectedEnd1 && task.compare(expected1) == 0 =>
        addTask(caseName)(tg2)
      case Complete(time, Seq(task)) if time == expectedEnd2 && task.compare(expected2) == 0 =>
        succeed(())
      case _ => idState
    }

    override def expectedCalls: Seq[Call] =
      Seq(Run, Complete(expectedEnd1, Seq(expected1)), Complete(expectedEnd2, Seq(expected2)))

  })

  def mockRepeater(
      name: String,
      expectedCreate: Long,
      duration: Long,
      repeat: Int
  ): IO[MockCaseRef] = for {
    random <- Random.scalaUtilRandom[IO]
    given Random[IO] = random
    ids <- (for i <- 0 to repeat yield gen.randomUUID).toList.sequence
    tasks = ids.zipWithIndex.map((id, i) => Task(s"T$i", duration).withID(id))
    expected <- tasks.zipWithIndex
      .map((t, i) =>
        t.create(name, expectedCreate + i * duration)
          .map(ti => Complete(expectedCreate + (i + 1) * duration, Seq(ti)))
      )
      .sequence
  } yield (new MockCaseRef(name) {

    var i: Int = 0

    override def react(call: Call): SimState[IO] = call match {
      case Run => addTask(caseName)(tasks(0))
      case Complete(time, Seq(task))
          if time == expected(i).time && task.compare(expected(i).tasks.head) == 0 => {
        if i < repeat then {
          i = i + 1
          addTask(caseName)(tasks(i))
        } else {
          succeed(())
        }
      }
      case _ => idState
    }

    override def expectedCalls: Seq[Call] = Seq(Run) ++ expected
  })

  def mockTwoPlusOneTasks(
      name: String,
      expectedCreate1: Long,
      duration1: Long,
      expectedEnd1: Long,
      duration2: Long,
      expectedEnd2: Long,
      duration3: Long,
      expectedEnd3: Long
  ): IO[MockCaseRef] = for {
    random <- Random.scalaUtilRandom[IO]
    given Random[IO] = random
    id1 <- gen.randomUUID
    tg1 = Task("T1", duration1).withID(id1)
    expected1 <- tg1.create(name, expectedCreate1)
    id2 <- gen.randomUUID
    tg2 = Task("T2", duration2).withID(id2)
    expected2 <- tg2.create(name, expectedCreate1)
    id3 <- gen.randomUUID
    tg3 = Task("T3", duration3).withID(id3)
    expected3 <- tg3.create(name, Math.max(expectedEnd1, expectedEnd2))
  } yield (new MockCaseRef(name) {

    override def react(call: Call): SimState[IO] = call match {
      case Run => addTasks(caseName, Seq(tg1, tg2))
      case Complete(time, Seq(task)) if time == expectedEnd1 && task.compare(expected1) == 0 => {
        if expectedEnd1 > expectedEnd2 then addTask(caseName)(tg3)
        else idState
      }
      case Complete(time, Seq(task)) if time == expectedEnd2 && task.compare(expected2) == 0 => {
        if expectedEnd1 < expectedEnd2 then addTask(caseName)(tg3)
        else idState
      }
      case Complete(time, Seq(task1, task2))
          if time == expectedEnd1 && expectedEnd1 == expectedEnd2 && tasksMatch(
            Seq(task1, task2),
            Seq(expected1, expected2)
          ) =>
        addTask(caseName)(tg3)
      case Complete(time, Seq(task)) if time == expectedEnd3 && task.compare(expected3) == 0 =>
        succeed(())
      case _ => idState
    }

    override def expectedCalls: Seq[Call] =
      if expectedEnd1 < expectedEnd2 then
        Seq(
          Run,
          Complete(expectedEnd1, Seq(expected1)),
          Complete(expectedEnd2, Seq(expected2)),
          Complete(expectedEnd3, Seq(expected3))
        )
      else if expectedEnd1 > expectedEnd2 then
        Seq(
          Run,
          Complete(expectedEnd2, Seq(expected2)),
          Complete(expectedEnd1, Seq(expected1)),
          Complete(expectedEnd3, Seq(expected3))
        )
      else
        Seq(
          Run,
          Complete(expectedEnd1, Seq(expected1, expected2)),
          Complete(expectedEnd3, Seq(expected3))
        )
  })

  def mockAbort(
      name: String,
      resource: Option[Resource]
  ): IO[MockCaseRef] = for {
    random <- Random.scalaUtilRandom[IO]
    given Random[IO] = random
    // T1 0..3 - to be aborted at 2
    id1 <- gen.randomUUID
    tg1 = Task("T1", 3L)
      .withID(id1)
      .withResources(resource.map(_.name).toSeq)
      .withPriority(Task.Medium)
    // expected1 <- tg1.create(name, expectedCreate1)

    // T2 0..2
    id2 <- gen.randomUUID
    tg2 = Task("T2", 2L).withID(id2).withPriority(Task.Medium)
    expected2 <- tg2.create(name, 0)

    // T3 0..5
    id3 <- gen.randomUUID
    tg3 = Task("T3", 5L)
      .withID(id3)
      .withResources(resource.map(_.name).toSeq)
      .withPriority(Task.Low)
    expected3 <- tg3.create(name, 0)

    expectedTime = if resource.isEmpty then 5L else 7L
  } yield (new MockCaseRef(name) {

    override def react(call: Call): SimState[IO] = call match {
      case Run => addTasks(caseName, Seq(tg1, tg2, tg3))
      case Complete(time, Seq(task)) if time == 2L && task.compare(expected2) == 0 =>
        lift(abortTasks(Seq(id1)))
      case Complete(time, Seq(task)) if time == expectedTime && task.compare(expected3) == 0 =>
        succeed(())
      case _ => idState
    }

    override def expectedCalls: Seq[Call] =
      Seq(Run, Complete(2L, Seq(expected2)), Complete(expectedTime, Seq(expected3)))
  })

  def mockAborted(
      name: String,
      duration: Long
  ): IO[MockCaseRef] = for {
    random <- Random.scalaUtilRandom[IO]
    given Random[IO] = random
    // T1 0..3 - to be aborted at 2
    id1 <- gen.randomUUID
    tg1 = Task("T", duration).withID(id1)
  } yield (new MockCaseRef(name) {

    override def react(call: Call): SimState[IO] = call match {
      case Run => addTask(caseName)(tg1)
      case _ => idState
    }

    override def expectedCalls: Seq[Call] = Seq(Run, Stop)
  })

  case class MockSingleTaskArrival(start: Long, interval: Long, duration: Long)
      extends Case[IO, Unit] {
    import collection.mutable.Queue
    val cases: Queue[MockCaseRef] = Queue()

    override def init(name: String, count: Int, time: Long, t: Unit): IO[CaseRef[IO]] = for {
      c <- mockSingleTask(
        name,
        start + interval * count,
        duration,
        start + interval * count + duration
      )
      _ = cases.enqueue(c)
    } yield (c)
  }

}
