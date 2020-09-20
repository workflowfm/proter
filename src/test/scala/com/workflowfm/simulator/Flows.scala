package com.workflowfm.simulator

import org.scalatest._
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import akka.testkit._

import akka.actor.{ActorRef, ActorSystem}
import scala.concurrent._
import java.util.UUID

import com.workflowfm.simulator.flows._
import com.workflowfm.simulator._
import java.{util => ju}


@RunWith(classOf[JUnitRunner])
class Flows extends FlowsTester {
    "Lookahead parseFlow" must {
        "execute a single flow" in {
            1 should be (1)
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

    val flowsLookaheadTest = new FlowsLookaheadTest(self)

    type IDFunction = Map[UUID,Long]=>Option[Long]
    def parseFlow(flow: Flow, extraFunction: Option[IDFunction] ): IDFunction = flowsLookaheadTest.parseFlow(flow,extraFunction)

    override def afterAll: Unit = {
        TestKit.shutdownActorSystem(system)
    }
}

class FlowsLookaheadTest(actor: ActorRef)(implicit executionContext: ExecutionContext)extends FlowLookaheadActor("testFlow",actor,NoTask()) {
    override def parseFlow(flow: Flow, extraFunction: Option[Map[ju.UUID,Long] => Option[Long]]): Map[ju.UUID,Long] => Option[Long] = {
        super.parseFlow(flow, extraFunction)
    }

}