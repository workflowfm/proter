package com.workflowfm.proter

import java.util.UUID

import scala.annotation.tailrec
import scala.util.Success

import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher

abstract class MockSimulation(override val name: String) extends SimulationRef {
  import MockSimulation._
  import collection.mutable.Queue

  val calls: Queue[Call] = Queue()

  override def run(): Unit = {
    calls.enqueue(Run)
    react(Run)
  }

  override def stop(): Unit = {
    calls.enqueue(Stop)
    react(Stop)
  }

  override def completed(time: Long, tasks: Seq[TaskInstance]): Unit = {
    calls.enqueue(Complete(time, tasks))
    react(Complete(time, tasks))
  }

  def react(call: Call): Unit

  def expectedCalls: Seq[Call]
}

trait MockSimulationGenerator extends SimulationRefGenerator {
  def sims: Iterable[MockSimulation]
}

trait MockSimulationCallMatcher {
  import MockSimulation._

  def comply: Matcher[MockSimulation] = MockSimulationCallComplyMatcher

  object MockSimulationCallComplyMatcher extends Matcher[MockSimulation] {

    def apply(sim: MockSimulation): MatchResult =
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
        if (callsMatch(call, expected)) allCallsMatch(crest, erest)
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

object MockSimulation {
  sealed trait Call
  case object NoCall extends Call
  case object Run extends Call
  case object Stop extends Call
  case class Complete(time: Long, tasks: Seq[TaskInstance]) extends Call

  def tasksMatch(l: Seq[TaskInstance], r: Seq[TaskInstance]): Boolean =
    l.sorted.corresponds(r.sorted)(_.compare(_) == 0)

  def mockSingleTask(
      name: String,
      coordinator: Manager,
      expectedCreate: Long,
      duration: Long,
      expectedEnd: Long
  ): MockSimulation = new MockSimulation(name) {
    val id = UUID.randomUUID()
    val tg = Task("T", duration).withID(id)
    val expected = tg.create(name, expectedCreate)

    override def react(call: Call): Unit = call match {
      case Run => coordinator.simResponse(SimReady(name, Seq(tg)))
      case Complete(time, Seq(task)) if time == expectedEnd && task.compare(expected) == 0 =>
        coordinator.simResponse(SimDone(name, Success(())))
      case _ => ()
    }

    override def expectedCalls: Seq[Call] = Seq(Run, Complete(expectedEnd, Seq(expected)))
  }

  def mockTwoTasks(
      name: String,
      coordinator: Manager,
      expectedCreate1: Long,
      duration1: Long,
      expectedEnd1: Long,
      duration2: Long,
      expectedEnd2: Long
  ): MockSimulation = new MockSimulation(name) {
    val id1 = UUID.randomUUID()
    val tg1 = Task("T1", duration1).withID(id1)
    val expected1 = tg1.create(name, expectedCreate1)

    val id2 = UUID.randomUUID()
    val tg2 = Task("T2", duration2).withID(id2)
    val expected2 = tg2.create(name, expectedEnd1)

    override def react(call: Call): Unit = call match {
      case Run => coordinator.simResponse(SimReady(name, Seq(tg1)))
      case Complete(time, Seq(task)) if time == expectedEnd1 && task.compare(expected1) == 0 =>
        coordinator.simResponse(SimReady(name, Seq(tg2)))
      case Complete(time, Seq(task)) if time == expectedEnd2 && task.compare(expected2) == 0 =>
        coordinator.simResponse(SimDone(name, Success(())))
      case _ => ()
    }

    override def expectedCalls: Seq[Call] =
      Seq(Run, Complete(expectedEnd1, Seq(expected1)), Complete(expectedEnd2, Seq(expected2)))

  }

  def mockRepeater(
      name: String,
      coordinator: Manager,
      expectedCreate: Long,
      duration: Long,
      repeat: Int
  ): MockSimulation = new MockSimulation(name) {

    var i: Int = 0

    val tasks: Seq[Task] =
      for (i <- 0 to repeat) yield Task("T" + i, duration).withID(UUID.randomUUID)

    val expected = tasks.zipWithIndex.map { case (t, i) =>
      Complete(
        expectedCreate + (i + 1) * duration,
        Seq(t.create(name, expectedCreate + i * duration))
      )
    }

    override def react(call: Call): Unit = call match {
      case Run => coordinator.simResponse(SimReady(name, Seq(tasks(0))))
      case Complete(time, Seq(task))
          if time == expected(i).time && task.compare(expected(i).tasks.head) == 0 => {
        if (i < repeat) {
          i = i + 1
          coordinator.simResponse(SimReady(name, Seq(tasks(i))))
        } else {
          coordinator.simResponse(SimDone(name, Success(())))
        }
      }
      case _ => ()
    }

    override def expectedCalls: Seq[Call] = Seq(Run) ++ expected
  }

  def mockTwoPlusOneTasks(
      name: String,
      coordinator: Manager,
      expectedCreate1: Long,
      duration1: Long,
      expectedEnd1: Long,
      duration2: Long,
      expectedEnd2: Long,
      duration3: Long,
      expectedEnd3: Long
  ): MockSimulation = new MockSimulation(name) {
    val id1 = UUID.randomUUID()
    val tg1 = Task("T1", duration1).withID(id1)
    val expected1 = tg1.create(name, expectedCreate1)

    val id2 = UUID.randomUUID()
    val tg2 = Task("T2", duration2).withID(id2)
    val expected2 = tg2.create(name, expectedCreate1)

    val id3 = UUID.randomUUID()
    val tg3 = Task("T3", duration3).withID(id3)
    val expected3 = tg3.create(name, Math.max(expectedEnd1, expectedEnd2))

    override def react(call: Call): Unit = call match {
      case Run => coordinator.simResponse(SimReady(name, Seq(tg1, tg2)))
      case Complete(time, Seq(task)) if time == expectedEnd1 && task.compare(expected1) == 0 => {
        if (expectedEnd1 > expectedEnd2)
          coordinator.simResponse(SimReady(name, Seq(tg3)))
        else
          coordinator.simResponse(SimReady(name, Seq()))
      }
      case Complete(time, Seq(task)) if time == expectedEnd2 && task.compare(expected2) == 0 => {
        if (expectedEnd1 < expectedEnd2)
          coordinator.simResponse(SimReady(name, Seq(tg3)))
        else
          coordinator.simResponse(SimReady(name, Seq()))
      }
      case Complete(time, Seq(task1, task2))
          if time == expectedEnd1 && expectedEnd1 == expectedEnd2 && tasksMatch(
            Seq(task1, task2),
            Seq(expected1, expected2)
          ) =>
        coordinator.simResponse(SimReady(name, Seq(tg3)))
      case Complete(time, Seq(task)) if time == expectedEnd3 && task.compare(expected3) == 0 =>
        coordinator.simResponse(SimDone(name, Success(())))
      case _ => ()
    }

    override def expectedCalls: Seq[Call] =
      if (expectedEnd1 < expectedEnd2)
        Seq(
          Run,
          Complete(expectedEnd1, Seq(expected1)),
          Complete(expectedEnd2, Seq(expected2)),
          Complete(expectedEnd3, Seq(expected3))
        )
      else if (expectedEnd1 > expectedEnd2)
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
  }

  def mockAbort(
      name: String,
      coordinator: Manager,
      resource: Option[TaskResource]
  ): MockSimulation = new MockSimulation(name) {
    // T1 0..3 - to be aborted at 2
    val id1 = UUID.randomUUID()

    val tg1 = Task(
      "T1",
      3L
    ).withID(id1).withResources(resource.map(_.name).toSeq).withPriority(Task.Medium)
    // val expected1 = tg1.create(name, 0)

    // T2 0..2
    val id2 = UUID.randomUUID()
    val tg2 = Task("T2", 2L).withID(id2).withPriority(Task.Medium)
    val expected2 = tg2.create(name, 0)

    // T3 0..5
    val id3 = UUID.randomUUID()

    val tg3 =
      Task("T3", 5L).withID(id3).withResources(resource.map(_.name).toSeq).withPriority(Task.Low)
    val expected3 = tg3.create(name, 0)

    val expectedTime = if (resource.isEmpty) 5L else 7L

    override def react(call: Call): Unit = call match {
      case Run => coordinator.simResponse(SimReady(name, Seq(tg1, tg2, tg3)))
      case Complete(time, Seq(task)) if time == 2L && task.compare(expected2) == 0 =>
        coordinator.simResponse(SimReady(name, Seq(), Seq(id1)))
      case Complete(time, Seq(task)) if time == expectedTime && task.compare(expected3) == 0 =>
        coordinator.simResponse(SimDone(name, Success(())))
      case _ => ()
    }

    override def expectedCalls: Seq[Call] =
      Seq(Run, Complete(2L, Seq(expected2)), Complete(expectedTime, Seq(expected3)))
  }

  def mockAborted(
      name: String,
      coordinator: Manager,
      duration: Long
  ): MockSimulation = new MockSimulation(name) {
    val id = UUID.randomUUID()
    val tg = Task("T", duration).withID(id)

    override def react(call: Call): Unit = call match {
      case Run => coordinator.simResponse(SimReady(name, Seq(tg)))
      case _ => ()
    }

    override def expectedCalls: Seq[Call] = Seq(Run, Stop)
  }

  def mockSingleTaskGenerator(
      name: String,
      start: Long,
      interval: Long,
      duration: Long,
      limit: Int
  ): MockSimulationGenerator = new MockSimulationGenerator {
    import collection.mutable.Queue

    val sims: Queue[MockSimulation] = Queue()

    override def build(manager: Manager, count: Int): SimulationRef = {
      val sim = mockSingleTask(
        name + ":" + count,
        manager,
        start + interval * count,
        duration,
        start + interval * count + duration
      )
      sims.enqueue(sim)
      sim
    }
  }
}
