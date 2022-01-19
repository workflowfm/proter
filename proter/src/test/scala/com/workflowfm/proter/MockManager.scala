package com.workflowfm.proter

import scala.annotation.tailrec
import scala.util.{ Failure, Success }

import org.scalatest.matchers.MatchResult
import org.scalatest.matchers.Matcher

class MockManager extends Manager {
  import MockManager._
  import collection.mutable.{ Queue, Map }

  val responses: Queue[SimResponse] = Queue()
  val reactions: Map[Task, Reaction] = Map()
  val expected: Queue[SimResponse] = Queue()

  override def waitFor(simulation: String): Unit = ()

  override def simResponse(response: SimResponse): Unit = {
    responses.enqueue(response)
    response match {
      case SimDone(_, _) => ()
      case SimReady(sim, tasks, _, _) => {
        tasks.map(react)
        ()
      }
    }
  }

  private def react(task: Task): Unit = reactions.get(task) match {
    case None => ()
    case Some(Stop(sim)) => sim.stop()
    case Some(Complete(sim, time, tasks)) => sim.completed(time, tasks)
  }

  def expectedResponses: Seq[SimResponse] = expected.toSeq
}

object MockManager {
  sealed trait Reaction
  case class Stop(sim: SimulationRef) extends Reaction
  case class Complete(sim: SimulationRef, time: Long, tasks: Seq[TaskInstance]) extends Reaction
}

trait MockManagerResponseMatcher {

  def comply: Matcher[MockManager] = MockManagerResponseComplyMatcher

  object MockManagerResponseComplyMatcher extends Matcher[MockManager] {

    def apply(sim: MockManager): MatchResult =
      allResponsesMatch(sim.responses.toList, sim.expectedResponses.toList)
  }

  @tailrec
  private def allResponsesMatch(
      responses: List[SimResponse],
      expectedResponses: List[SimResponse]
  ): MatchResult = (responses, expectedResponses) match {
    case (Nil, Nil) => MatchResult(true, "", "Responses matched")
    case (_, Nil) =>
      MatchResult(
        false,
        s"""Unexpected responses: ${responses.mkString(", ")}""",
        "Responses matched"
      )
    case (Nil, _) =>
      MatchResult(
        false,
        s"""Did not receive expected responses: ${expectedResponses.mkString(", ")}""",
        "Responses matched"
      )
    case (response :: rrest, expected :: erest) =>
      if (responsesMatch(response, expected)) allResponsesMatch(rrest, erest)
      else MatchResult(false, s"Expected: $expected - but got: $response", "Response matched")
  }

  def responsesMatch(response: SimResponse, expected: SimResponse): Boolean =
    (response, expected) match {

      case (SimDone(sim1, Success(result1)), SimDone(sim2, Success(result2))) =>
        sim1.equals(sim2) && result1 == result2
      case (SimDone(sim1, Failure(ex1)), SimDone(sim2, Failure(_))) =>
        sim1.equals(sim2) && ex1.isInstanceOf[Simulation.SimulationStoppingException]
      case (SimReady(sim1, tasks1, _, _), SimReady(sim2, tasks2, _, _)) =>
        sim1.equals(sim2) && tasksMatch(tasks1, tasks2)
      case _ => false
    }

  def tasksMatch(l: Seq[Task], r: Seq[Task]): Boolean =
    l.toSet.equals(r.toSet)
}
