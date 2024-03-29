package com.workflowfm.proter

import java.util.UUID

import scala.concurrent._
import scala.concurrent.duration._

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class TaskTests extends TaskTester {

  "Task priority" should {

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

class TaskTester extends AnyWordSpecLike with Matchers {
  given ExecutionContextExecutor = ExecutionContext.global
  given timeout: FiniteDuration = 10.seconds

  def t(
      id: Long,
      resources: Seq[String],
      priority: Int = Task.Medium,
      created: Long = 0L,
      duration: Long = 1L,
      interrupt: Int = 0,
      name: String = "X"
  ): TaskInstance =
    new TaskInstance(
      new UUID(id, id),
      name,
      "Test",
      created,
      0L, // todo add tests for minStartTime
      resources,
      duration,
      duration,
      0L,
      interrupt,
      priority
    )
}
