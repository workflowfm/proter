package com.workflowfm.proter
import scala.collection.mutable.SortedSet
import scala.concurrent.{ Await, ExecutionContext, Future }
import scala.concurrent.duration._

import akka.actor._
import akka.testkit.TestProbe
import akka.util.Timeout
import org.junit.runner.RunWith
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import org.scalatest.junit.JUnitRunner

@RunWith(classOf[JUnitRunner])
class SchedulerTests extends TaskTester with ScheduleTester {

  "The Schedule" must {

    "fit a task at the edge of another" in {
      s((1, 2)) + (2, 3) should be(Some(s((1, 3))))
      s((3, 4)) + (2, 3) should be(Some(s((2, 4))))
    }

    "fit a task at the edge of two others" in {
      s((1, 2), (3, 4)) + (2, 3) should be(Some(s((1, 4))))
      s((1, 2), (4, 5)) + (2, 3) should be(Some(s((1, 3), (4, 5))))
      s((1, 2), (4, 5)) + (3, 4) should be(Some(s((1, 2), (3, 5))))
    }

    "fit a task in gaps" in {
      s((1, 2)) + (3, 4) should be(Some(s((1, 2), (3, 4))))
      s((3, 4)) + (1, 2) should be(Some(s((1, 2), (3, 4))))
      s((1, 2), (3, 4)) + (5, 6) should be(Some(s((1, 2), (3, 4), (5, 6))))
      s((1, 2), (5, 6)) + (3, 4) should be(Some(s((1, 2), (3, 4), (5, 6))))
      s((3, 4), (5, 6)) + (1, 2) should be(Some(s((1, 2), (3, 4), (5, 6))))
    }

    "not fit tasks that clash with the start of another task" in {
      s((1, 2)) + (1, 3) should be(None)
      s((1, 4)) + (1, 3) should be(None)
      s((2, 3)) + (1, 3) should be(None)
      s((2, 4)) + (1, 3) should be(None)
    }

    "not fit tasks that clash with the end of another task" in {
      s((2, 4)) + (3, 4) should be(None)
      s((3, 4)) + (2, 4) should be(None)
      s((1, 4)) + (1, 5) should be(None)
      s((1, 5)) + (1, 4) should be(None)
      s((2, 3)) + (1, 3) should be(None)
    }

    "not fit tasks that overlap with another task" in {
      s((1, 3)) + (1, 3) should be(None)
      s((1, 4)) + (2, 3) should be(None)
      s((2, 3)) + (1, 4) should be(None)
    }

    "not fit tasks that clash with two other tasks" in {
      s((1, 2), (4, 6)) + (2, 5) should be(None)
      s((1, 2), (4, 6)) + (3, 5) should be(None)
      s((1, 2), (4, 6)) + (2, 6) should be(None)
      s((1, 2), (4, 6)) + (3, 6) should be(None)
      s((1, 2), (4, 6)) + (2, 7) should be(None)
      s((1, 2), (4, 6)) + (3, 7) should be(None)
      s((1, 3), (4, 6)) + (2, 4) should be(None)
    }

    "merge single tasks with common start" in {
      s((1, 2)) ++ s((1, 2)) should be(s((1, 2)))
      s((1, 2)) ++ s((1, 3)) should be(s((1, 3)))
      s((1, 3)) ++ s((1, 2)) should be(s((1, 3)))
    }

    "merge single tasks with common finish" in {
      s((1, 3)) ++ s((2, 3)) should be(s((1, 3)))
      s((2, 3)) ++ s((1, 3)) should be(s((1, 3)))
    }

    "merge single tasks that don't overlap" in {
      s((1, 2)) ++ s((2, 3)) should be(s((1, 3)))
      s((1, 2)) ++ s((3, 4)) should be(s((1, 2), (3, 4)))
      s((1, 2), (5, 6)) ++ s((3, 4)) should be(s((1, 2), (3, 4), (5, 6)))
      s((2, 3)) ++ s((1, 2)) should be(s((1, 3)))
      s((3, 4)) ++ s((1, 2)) should be(s((1, 2), (3, 4)))
      s((3, 4)) ++ s((1, 2), (5, 6)) should be(s((1, 2), (3, 4), (5, 6)))
    }

    "merge multiple tasks with one overlapping task" in {
      s((1, 2), (3, 4), (5, 6)) ++ s((1, 6)) should be(s((1, 6)))
      s((1, 2), (3, 4), (5, 6)) ++ s((0, 6)) should be(s((0, 6)))
      s((1, 2), (3, 4), (5, 6)) ++ s((1, 7)) should be(s((1, 7)))
      s((1, 2), (3, 4), (5, 6)) ++ s((0, 7)) should be(s((0, 7)))
      s((1, 6)) ++ s((1, 2), (3, 4), (5, 6)) should be(s((1, 6)))
      s((0, 6)) ++ s((1, 2), (3, 4), (5, 6)) should be(s((0, 6)))
      s((1, 7)) ++ s((1, 2), (3, 4), (5, 6)) should be(s((1, 7)))
      s((0, 7)) ++ s((1, 2), (3, 4), (5, 6)) should be(s((0, 7)))
    }

    "merge multiple overlapping tasks" in {
      s((1, 2), (3, 4), (5, 6)) ++ s((2, 3), (4, 5), (6, 7)) should be(s((1, 7)))
      s((1, 2), (3, 4), (5, 6)) ++ s((2, 3), (4, 5)) should be(s((1, 6)))
    }
  }

  "The DefaultScheduler" must {

    "select a single task" in {
      val m = new TestResourceMap("A")
      m.s(t(1L, Seq("A"))) should be(Seq(1L))
    }

    "select multiple tasks" in {
      val m = new TestResourceMap("A", "B")
      m.s(
        t(1L, Seq("A")),
        t(2L, Seq("B"))
      ) should be(Seq(1L, 2L))
    }

    "select an earlier task" in {
      val m = new TestResourceMap("A")
      m.s(t(1L, Seq("A"), Task.Medium, 2L), t(2L, Seq("A"), Task.Medium, 1L)) should be(Seq(2L))
    }

    "not select a blocked task" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.s(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow, 0L, 2L)) should be(Nil)
    }

    "select a lower priority task if it will finish on time" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.s(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow)) should be(List(2L))
    }

    "not block higher priority tasks" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      /* DefaultScheduler.nextEstimatedTaskStart(t(1L,Seq("A","B"),Task.Highest), 0L, m.m,Seq(
       * t(1L,Seq("A","B"),Task.Highest),t(2L,Seq("A"),Task.VeryLow,0L,100L))) should be (1L) */
      m.s(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow, 0L, 100L)) should be(
        Nil
      )
    }

    "not block higher priority tasks based on ordering" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.s(t(1L, Seq("A", "B"), Task.Medium, 0L), t(2L, Seq("A"), Task.Medium, 2L, 100L)) should be(
        Nil
      )
    }

    "not block higher priority tasks of other resources" in {
      val m = new TestResourceMap("A", "B") //+ ("B",1L)
      m.s(t(1L, Seq("B"), Task.Highest), t(2L, Seq("A", "B"), Task.VeryLow, 0L, 100L)) should be(
        Seq(1L)
      )
    }

    "consider all higher priority tasks for availability" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.s(
        t(1L, Seq("B"), Task.Highest),
        t(2L, Seq("A", "B"), Task.Medium),
        t(3L, Seq("A"), Task.VeryLow, 0L, 2L)
      ) should be(List(3L))
    }

    "select higher priority tasks" in {
      val m = new TestResourceMap("A", "B")
      m.s(
        t(2L, Seq("A", "B"), Task.Medium),
        t(1L, Seq("A"), Task.Highest)
      ) should be(List(1L))
    }
  }

  "The LookaheadScheduler" must {
    "select a single task without  alookahead structure" in {
      val m = new TestResourceMap("A")
      m.l(t(1L, Seq("A"))) should be(Seq(1L))
    }
    "select multiple tasks without a lookahead structure" in {
      val m = new TestResourceMap("A", "B")
      m.l(
        t(1L, Seq("A")),
        t(2L, Seq("B"))
      ) should be(Seq(1L, 2L))
    }
    "not select a blocked task without a lookahead structure" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.l(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow, 0L, 2L)) should be(Nil)
    }
    "select a single task with a lookahead structure" in {
      val m = new TestResourceMap("A")
      m.lookahead = NoLookahead +> (id(1L), TaskGenerator(
              "t2",
              "sim",
              ConstantGenerator(2L),
              ConstantGenerator(2L)
            ))
      m.l(t(1L, Seq("A"))) should be(Seq(1L))
    }
    "not select a task which would offset a higher priority future task" in {
      val m = new TestResourceMap("A", "B")
      m.lookahead = NoLookahead +> (id(1L), TaskGenerator(
              "t2",
              "sim",
              ConstantGenerator(5L),
              ConstantGenerator(5L)
            ).withPriority(Task.High).withResources(Seq("B")))
      m.l(
        t(1L, Seq("A"), Task.High),
        t(2L, Seq("B"), duration = 5L)
      ) should be(Seq(1L))
    }
    "select a task which offsets a lower priority future task" in {
      val m = new TestResourceMap("A", "B")
      m.lookahead = NoLookahead +> (id(1L), TaskGenerator(
              "t2",
              "sim",
              ConstantGenerator(5L),
              ConstantGenerator(5L)
            ).withPriority(Task.Low).withResources(Seq("B")))
      m.l(
        t(1L, Seq("A"), Task.High),
        t(2L, Seq("B"), duration = 5L)
      ) should be(Seq(1L, 2L))
    }
    "consider a distant future task" in {
      val m = new TestResourceMap("A", "B")
      m.lookahead = NoLookahead +> (id(1L), TaskGenerator(
              "t2",
              id(2L),
              "sim",
              ConstantGenerator(2L),
              ConstantGenerator(2L)
            ).withPriority(Task.High).withResources(Seq("A")))
      m.lookahead = m.lookahead +> (id(2L), TaskGenerator(
              "t3",
              "sim",
              ConstantGenerator(5L),
              ConstantGenerator(5L)
            ).withPriority(Task.High).withResources(Seq("B")))
      m.l(
        t(1L, Seq("A"), Task.High),
        t(3L, Seq("B"), duration = 5L)
      ) should be(Seq(1L))
    }
    "consider currently-running tasks" in {
      val m = new TestResourceMap("A")
      m.lookahead = NoLookahead +> (id(1L), TaskGenerator(
              "t2",
              "sim",
              ConstantGenerator(5L),
              ConstantGenerator(5L)
            ).withPriority(Task.Low).withResources(Seq("B")))
      m.m.get("A").map { _.startTask(t(1L, Seq("A"), Task.High, 0L, 5L, actor = mock), 0L) }
      m.l(t(1L, Seq("A"))) should be(Seq())
    }

  }

  def id(l: Long) = new java.util.UUID(l, l)

  class TestResourceMap(names: String*) {
    // create a resource map
    val m: Map[String, TaskResource] = Map[String, TaskResource]() ++ (names map { n => (n, r(n)) })

    var lookahead: Lookahead = NoLookahead

    // create a resource
    def r(name: String) = new TaskResource(name, 0)

    // pre-attach Tasks to resources
    def +(r: String, duration: Long, actor: ActorRef = mock): TestResourceMap = {
      m.get(r).map { _.startTask(t(0L, Seq(r), Task.Medium, 0L, duration, actor = actor), 0L) }
      this
    }

    // test DefaultScheduler
    def s(tasks: Task*): Seq[Long] =
      new DefaultScheduler(tasks: _*).getNextTasks(0L, m) map (_.id
            .getMostSignificantBits())

    // test LookaheadScheduler
    def l(tasks: Task*): Seq[Long] = {
      val ls = new LookaheadScheduler(tasks: _*)
      ls.setLookahead(self, lookahead)
      ls.getNextTasks(0L, m) map (_.id
        .getMostSignificantBits())
    }
  }
}

trait ScheduleTester {
  def s(l: (Long, Long)*): Schedule = Schedule(l.toList)
}
