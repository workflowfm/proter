package com.workflowfm.proter

import java.util.UUID

import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

import com.workflowfm.proter.flows._
import com.workflowfm.proter.schedule.ProterScheduler
import scala.concurrent.ExecutionContext

class FlowLookaheadTests extends FlowLookaheadTester {
  "Lookahead parseFlow" should {
    "Do nothing if given no tasks" in {
      val f = parseFlow(NoTask)
      f._2 should be(NoLookahead)
    }
    "Do nothing if given a single task" in {
      val t1 = FlowTask(Task("t", 2))
      // val t1id = t1.generator.id
      val f = parseFlow(t1)
      f._2 should be(NoLookahead)
    }
    "Do nothing if given an AND of two tasks" in {
      val t1 = FlowTask(Task("t1", 2))
      val t2 = FlowTask(Task("t2", 2))
      val f = parseFlow(t1 + t2)
      f._2 should be(NoLookahead)
    }
    "Register a task for lookahead if given a Then of two tasks" in {
      val t1 = FlowTask(Task("t1", 2).withID(UUID.randomUUID))
      val t2 = FlowTask(Task("t2", 2).withID(UUID.randomUUID))
      val f = parseFlow(t1 > t2)
      f._2 should not be (NoLookahead)
      f._2.getTaskData(Seq()).size should be(0)
      val data = f._2.getTaskData(Seq((t1.task.id.value, 2L)))
      data.size should be(1)
      data.head._1.id.value should be(t2.task.id.value)
    }
    "Consider the requirements of parent flow objects when registering a single task" in {
      val t1 = FlowTask(Task("t", 2).withID(UUID.randomUUID))
      val t1id = t1.task.id.value
      val dummyID = java.util.UUID.randomUUID()
      val function = (m: Map[UUID, Long]) => if m.keySet.contains(dummyID) then Some(0L) else None
      val f = parseFlow(t1, Some(function))
      f._2 should not be (NoLookahead)
      f._2.getTaskData(Seq()).size should be(0)
      f._2.getTaskData(Seq((t1id, 2L))).size should be(0)
      val data = f._2.getTaskData(Seq((dummyID, 2L)))
      data.size should be(1)
      data.head._1.id.value should be(t1id)
    }
    "Correctly encode an AND function from an AND of two tasks" in {
      val t1 = FlowTask(Task("t1", 2).withID(UUID.randomUUID))
      val t2 = FlowTask(Task("t2", 2).withID(UUID.randomUUID))
      val f = parseFlow(t1 + t2)
      f._1(Map()) should be(None)
      f._1(Map((t1.task.id.value, 2L))) should be(None)
      f._1(Map((t2.task.id.value, 3L))) should be(None)
      f._1(Map((t1.task.id.value, 2L), (t2.task.id.value, 3L))) should be(Some(3L))
    }
  }
}

class FlowLookaheadTester extends AnyWordSpecLike with Matchers with OptionValues {

  type IDFunction = Map[UUID, Long] => Option[Long]

  def parseFlow(
      flow: Flow,
      extraFunction: Option[IDFunction] = None,
      structure: Lookahead = NoLookahead
  ): (IDFunction, Lookahead) =
    new FlowLookaheadTest().parseFlow(flow, extraFunction, structure)
}

class FlowLookaheadTest
    extends FlowLookahead(
      "testFlow",
      new Coordinator(new ProterScheduler)(using ExecutionContext.global),
      NoTask
    ) {

  override def parseFlow(
      flow: Flow,
      extraFunction: Option[Map[UUID, Long] => Option[Long]],
      structure: Lookahead
  ): (Map[UUID, Long] => Option[Long], Lookahead) = {
    super.parseFlow(flow, extraFunction, structure)
  }
}
