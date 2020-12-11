package com.workflowfm.simulator

import java.{ util => ju }
import java.util.UUID

import scala.concurrent._

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.testkit._
import org.junit.runner.RunWith
import org.scalatest._
import org.scalatest.junit.JUnitRunner

import com.workflowfm.simulator._
import com.workflowfm.simulator.flows._

@RunWith(classOf[JUnitRunner])
class Flows extends FlowsTester {
  "Lookahead parseFlow" must {
    "Do nothing if given no tasks" in {
      val f = parseFlow(NoTask())
      f._2 should be(NoLookahead)
    }
    "Do nothing if given a single task" in {
      val t1 = FlowTask(TaskGenerator("t", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      //val t1id = t1.generator.id
      val f = parseFlow(t1)
      f._2 should be(NoLookahead)
    }
    "Do nothing if given an AND of two tasks" in {
      val t1 = FlowTask(TaskGenerator("t1", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val t2 = FlowTask(TaskGenerator("t2", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val f = parseFlow(t1 + t2)
      f._2 should be(NoLookahead)
    }
    "Do nothing if given an OR of two tasks" in {
      val t1 = FlowTask(TaskGenerator("t1", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val t2 = FlowTask(TaskGenerator("t2", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val f = parseFlow(t1 | t2)
      f._2 should be(NoLookahead)
    }
    "Register a task for lookahead if given a Then of two tasks" in {
      val t1 = FlowTask(TaskGenerator("t1", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val t2 = FlowTask(TaskGenerator("t2", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val f = parseFlow(t1 > t2)
      f._2 should not be (NoLookahead)
      f._2.getTaskData(Seq()).size should be(0)
      val data = f._2.getTaskData(Seq((t1.generator.id, 2L)))
      data.size should be(1)
      data.head._1.id should be(t2.generator.id)
    }
    "Consider the requirements of parent flow objects when registering a single task" in {
      val t1 = FlowTask(TaskGenerator("t", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val t1id = t1.generator.id
      val dummyID = java.util.UUID.randomUUID()
      val function = (m: Map[UUID, Long]) => if (m.keySet.contains(dummyID)) Some(0L) else None
      val f = parseFlow(t1, Some(function))
      f._2 should not be (NoLookahead)
      f._2.getTaskData(Seq()).size should be(0)
      f._2.getTaskData(Seq((t1id, 2L))).size should be(0)
      val data = f._2.getTaskData(Seq((dummyID, 2L)))
      data.size should be(1)
      data.head._1.id should be(t1id)
    }
    "Correctly encode an AND function from an AND of two tasks" in {
      val t1 = FlowTask(TaskGenerator("t1", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val t2 = FlowTask(TaskGenerator("t2", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val f = parseFlow(t1 + t2)
      f._1(Map()) should be(None)
      f._1(Map((t1.id, 2L))) should be(None)
      f._1(Map((t2.id, 3L))) should be(None)
      f._1(Map((t1.id, 2L), (t2.id, 3L))) should be(Some(3L))
    }
    "Correctly encode an OR function from an OR of two tasks" in {
      val t1 = FlowTask(TaskGenerator("t1", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val t2 = FlowTask(TaskGenerator("t2", "sim", ConstantGenerator(2), ConstantGenerator(2)))
      val f = parseFlow(t1 | t2)
      f._1(Map()) should be(None)
      f._1(Map((t1.id, 2L))) should be(Some(2L))
      f._1(Map((t2.id, 3L))) should be(Some(3L))
      f._1(Map((t1.id, 2L), (t2.id, 3L))) should be(Some(2L))
    }
  }
}

class FlowsTester
    extends TestKit(ActorSystem("FlowsTest"))
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ImplicitSender {
  implicit val executionContext: ExecutionContext = ExecutionContext.global

  val flowsLookaheadTest: Props = FlowsLookaheadTest.props(self)
  val test: TestActorRef[FlowLookaheadActor] = TestActorRef(new FlowLookaheadActor("testFlow", self, NoTask()))

  type IDFunction = Map[UUID, Long] => Option[Long]

  def parseFlow(
      flow: Flow,
      extraFunction: Option[IDFunction] = None,
      structure: Lookahead = NoLookahead
  ): (IDFunction, Lookahead) =
    TestActorRef(new FlowsLookaheadTest(self)).underlyingActor
      .parseFlow(flow, extraFunction, structure)

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }
}

class FlowsLookaheadTest(actor: ActorRef)(implicit executionContext: ExecutionContext)
    extends FlowLookaheadActor("testFlow", actor, NoTask()) {

  override def parseFlow(
      flow: Flow,
      extraFunction: Option[Map[ju.UUID, Long] => Option[Long]],
      structure: Lookahead
  ): (Map[ju.UUID, Long] => Option[Long], Lookahead) = {
    super.parseFlow(flow, extraFunction, structure)
  }
}

object FlowsLookaheadTest {

  def props(actor: ActorRef)(implicit executionContext: ExecutionContext): Props =
    Props(new FlowsLookaheadTest(actor))
}
