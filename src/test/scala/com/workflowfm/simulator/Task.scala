package com.workflowfm.simulator

import akka.actor.{ ActorRef, ActorSystem }
import akka.testkit.{ TestKit, TestProbe }
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import java.util.UUID
import akka.actor.Actor

import akka.testkit.{ ImplicitSender, TestActors, TestKit, TestProbe }
import com.typesafe.config.ConfigFactory
import uk.ac.ed.inf.ppapapan.subakka.MockPublisher
import scala.concurrent._
import scala.concurrent.Await
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class TaskTests extends TaskTester {

  "Task priority" must {

    "prioritize higher priority" in {
      t(2L, Seq("A"), Task.High, 2L, 1L, 0) < t(
        1L,
        Seq("A", "B"),
        Task.Medium,
        1L,
        2L,
        1
      ) should be(true)
    }

    "prioritize old age" in {
      t(2L, Seq("A", "B"), Task.Medium, 2L, 1L, 0) > t(
        1L,
        Seq("A"),
        Task.Medium,
        1L,
        2L,
        1
      ) should be(true)
    }

    "prioritize more resources" in {
      t(2L, Seq("A", "B"), Task.Medium, 0L, 1L, 0) < t(
        1L,
        Seq("A"),
        Task.Medium,
        0L,
        2L,
        1
      ) should be(true)
    }

    "prioritize longer duration" in {
      t(2L, Seq("A"), Task.Medium, 0L, 1L, 0) > t(1L, Seq("A"), Task.Medium, 0L, 2L, 1) should be(
        true
      )
    }

    "prioritize lower interrupt" in {
      t(2L, Seq("A"), Task.Medium, 0L, 1L, 0) < t(1L, Seq("A"), Task.Medium, 0L, 1L, 1) should be(
        true
      )
    }

    "prioritize lower ID if all else fails" in {
      t(2L, Seq("A"), Task.Medium, 0L, 1L, 0) > t(1L, Seq("A"), Task.Medium, 0L, 1L, 0) should be(
        true
      )
    }

  }
}

class TaskTester
    extends TestKit(ActorSystem("TaskTests", ConfigFactory.parseString(MockPublisher.config)))
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ImplicitSender {
    implicit val executionContext = ExecutionContext.global
    implicit val timeout: FiniteDuration = 10.seconds

  final val probe: TestProbe = TestProbe.apply("TaskTestsProbe")(system);
  final val mock: ActorRef = probe.ref;

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  def t(
      id: Long,
      resources: Seq[String],
      priority: Task.Priority = Task.Medium,
      created: Long = 0L,
      duration: Long = 1L,
      interrupt: Int = 0,
      name: String = "X",
      actor: ActorRef = mock
  ) =
    new Task(
      new UUID(id, id),
      name,
      "Test",
      actor,
      created,
      0L, //todo add tests for minStartTime
      resources,
      duration,
      duration,
      0L,
      interrupt,
      priority
    )
}