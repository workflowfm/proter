package com.workflowfm.simulator

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.collection.mutable.SortedSet
import akka.actor._
import akka.testkit.TestProbe
import scala.concurrent.{Await, Future, ExecutionContext}
import scala.concurrent.duration._
import akka.util.Timeout
import java.{util => ju}
import scala.collection.mutable

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
/*
  "The LookaheadScheduler" must {
    LookaheadScheduler.setLookaheadObject(mock, new LookaheadObj(mock) with dummyLookahead)
    "notify of a new itteration of lookahead" in {
      val m = new TestResourceMap("A")   
      val answer = Future( m.l(t(1L, Seq("A"), actor=probe.ref)) )

      probe.expectMsg(TestCalls.LookaheadNextIter)
      //probe.reply(Unit)
    }

    "ask for future tasks after a task is sheduled" in {
      val m = new TestResourceMap("A")   
      val answer = Future( m.l(t(1L, Seq("A"), actor=probe.ref)) )

      probe.expectMsg(TestCalls.LookaheadNextIter)
      probe.reply(Unit)
      val TestCalls.TasksAfterThis(id,time,official) = probe.expectMsgType[TestCalls.TasksAfterThis]
      id.getMostSignificantBits should be (1L)
      time should be (1L)
      official should be (true)
      //probe.reply(Seq())
    }

    "ask for future tasks of a task that is already running" in {
      val m = new TestResourceMap("A") + ("A",1L)  
      val answer = Future( m.l(t(1L, Seq("A"))))

      val TestCalls.TasksAfterThis(id,time,official) = probe.expectMsgType[TestCalls.TasksAfterThis]
      official should be (false)
      time should be (1L)
      id.getMostSignificantBits should be (0L)

      probe.reply(Seq()) // reply with no future tasks
      probe.expectMsg(TestCalls.LookaheadNextIter) // the scheduler continues
      //probe.reply(Unit)

    }

    "select a single task" in {
      val m = new TestResourceMap("A")   
      val answer = Future( m.l(t(1L, Seq("A"), actor=probe.ref)) )

      probe.expectMsg(TestCalls.LookaheadNextIter)
      probe.reply(Unit)
      probe.expectMsgType[TestCalls.TasksAfterThis]
      probe.reply(Seq())
      
      val r = Await.result(answer, 5.seconds)
      r should be(Seq(1L))
    }

    "select multiple tasks" in {
      val m = new TestResourceMap("A", "B")
      val answer = Future( m.l(
        t(1L, Seq("A"), actor=probe.ref),
        t(2L, Seq("B"), actor=probe.ref)
      ) )

      probe.expectMsg(TestCalls.LookaheadNextIter)
      probe.reply(Unit)
      probe.expectMsgType[TestCalls.TasksAfterThis]
      probe.reply(Seq())
      probe.expectMsgType[TestCalls.TasksAfterThis]
      probe.reply(Seq())
      
      val r = Await.result(answer,5.seconds)
      r should be(Seq(1L, 2L))
  }

    "not select a blocked task" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      val answer = Future(m.l(t(1L, Seq("A", "B"), Task.Highest, actor=probe.ref), t(2L, Seq("A"), Task.VeryLow, 0L, 2L, actor=probe.ref)))

      probe.expectMsgType[TestCalls.TasksAfterThis] //unofficial
      probe.reply(Seq())
      probe.expectMsg(TestCalls.LookaheadNextIter)
      probe.reply(Unit)
      probe.expectMsgType[TestCalls.TasksAfterThis] //official
      probe.reply(Seq())
      probe.expectMsgType[TestCalls.TasksAfterThis]
      probe.reply(Seq())
      
      Await.result(answer,5.seconds) should be(Nil)
    }

    "select a lower priority task if it will finish on time" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      val answer = Future(m.l(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow)))

      probe.expectMsgType[TestCalls.TasksAfterThis] //unofficial
      probe.reply(Seq())
      probe.expectMsg(TestCalls.LookaheadNextIter)
      probe.reply(Unit)
      probe.expectMsgType[TestCalls.TasksAfterThis] //official
      probe.reply(Seq())
      probe.expectMsgType[TestCalls.TasksAfterThis]
      probe.reply(Seq())
      
      Await.result(answer, 5.seconds) should be(List(2L))
    }

    "not block higher priority tasks" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      val answer = Future(m.l(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow, 0L, 100L))) 

      probe.expectMsgType[TestCalls.TasksAfterThis] //unofficial
      probe.reply(Seq())
      probe.expectMsg(TestCalls.LookaheadNextIter)
      probe.reply(Unit)
      probe.expectMsgType[TestCalls.TasksAfterThis] //official
      probe.reply(Seq())
      probe.expectMsgType[TestCalls.TasksAfterThis]
      probe.reply(Seq())
      
      Await.result(answer, 5.seconds) should be(Nil)
    }

    "not block a higher priority task that will start in the future" in {
      val m = new TestResourceMap("A") + ("A", 1L)
      val answer = Future(m.l(t(1L, Seq("A"), Task.Low))) 

      probe.expectMsgType[TestCalls.TasksAfterThis] //unofficial
      probe.reply(Seq(t(2L, Seq("A"), Task.High, 1L))) // reply with a future task
      probe.expectMsg(TestCalls.LookaheadNextIter)
      probe.reply(Unit)
      probe.expectMsgType[TestCalls.TasksAfterThis] //official
      probe.reply(Seq())
      probe.expectMsgType[TestCalls.TasksAfterThis]
      probe.reply(Seq())
      
      Await.result(answer, 5.seconds) should be(Nil)
    }

    "start if a task in the future has lower priority" in {
      val m = new TestResourceMap("A","B") + ("A", 1L)
      val answer = Future(m.l(t(1L, Seq("B"), Task.High))) 

      probe.expectMsgType[TestCalls.TasksAfterThis] //unofficial
      probe.reply(Seq(t(2L, Seq("A","B"), Task.Low, 1L))) // reply with a future task
      probe.expectMsg(TestCalls.LookaheadNextIter)
      probe.reply(Unit)
      probe.expectMsgType[TestCalls.TasksAfterThis] //official
      probe.reply(Seq())
      probe.expectMsgType[TestCalls.TasksAfterThis]
      probe.reply(Seq())
      
      Await.result(answer, 5.seconds) should be(List(1L))
    }

    "start a task despite subsequent tasks being blocked" in {
      val m = new TestResourceMap("A","B")
      val answer = Future(m.l(t(1L, Seq("A"), Task.Low),t(2L, Seq("B"), Task.High)))

      probe.expectMsg(TestCalls.LookaheadNextIter)
      probe.reply(Unit)
      probe.expectMsgType[TestCalls.TasksAfterThis] //asks about task 2 first because of priority
      probe.reply(Seq(t(3L, Seq("A","B"), Task.High, 1L))) // reply with a task that uses A and B
      probe.expectMsgType[TestCalls.TasksAfterThis] // asks about the above task (task 3)
      probe.reply(Seq()) //reply with nothing
      probe.expectMsgType[TestCalls.TasksAfterThis] //now asks about low priority task
      probe.reply(Seq(t(4L, Seq("A"), Task.Low, 1L))) // reply with task that uses A - will be blocked because of the high-priority task above
      probe.expectMsgType[TestCalls.TasksAfterThis] // asks about the above (task 4)
      probe.reply(Seq()) //reply with nothing
      
      Await.result(answer, 5.seconds) should be(List(2L,1L))
    }

    "ask multiple simulations about their respective tasks" in {
      val probe2: TestProbe = TestProbe.apply("TaskTestsProbe")(system);
      LookaheadScheduler.setLookaheadObject(probe2.ref, new LookaheadObj(probe2.ref) with dummyLookahead)
      val m = new TestResourceMap("A", "B")
      val answer = Future( m.l(t(1L, Seq("A"), Task.Low, actor=probe.ref), t(2L, Seq("B"), Task.High, actor=probe2.ref)))

      //This test is a bit tricky because probes are synchronous, so the order in which I write the expectMsg is important
      probe.expectMsg(TestCalls.LookaheadNextIter) //tells probe1 first because of the order they were passed
      probe.reply(Unit)
      probe2.expectMsg(TestCalls.LookaheadNextIter) // all the probes are notified before it starts waiting for replies 
      probe2.reply(Unit)

      probe2.expectMsgType[TestCalls.TasksAfterThis] // asks probe2 first because its task was high priority
      probe2.reply(Seq()) // waits for a reply before it moves on

      probe.expectMsgType[TestCalls.TasksAfterThis]
      probe.reply(Seq())
      
      Await.result(answer,5.seconds) should be(Seq(2L, 1L)) //response is ordered by priority
    }
  } */

  class TestResourceMap(names: String*) {
    // create a resource map
    val m: Map[String, TaskResource] = Map[String, TaskResource]() ++ (names map { n => (n, r(n)) })

    // create a resource
    def r(name: String) = new TaskResource(name, 0)

    // pre-attach Tasks to resources
    def +(r: String, duration: Long, actor: ActorRef=mock): TestResourceMap = {
      m.get(r).map { _.startTask(t(0L, Seq(r), Task.Medium, 0L, duration, actor=actor), 0L) }
      this
    }

    // test DefaultScheduler
    def s(tasks: Task*): Seq[Long] =
      new DefaultScheduler(tasks: _*).getNextTasks(0L, m) map (_.id
            .getMostSignificantBits())

    // test LookaheadScheduler
    def l(tasks: Task*): Seq[Long] =
      LookaheadScheduler.getNextTasks(SortedSet[Task]() ++ tasks, 0L, m) map (_.id
            .getMostSignificantBits())
  } 
}

trait ScheduleTester {
  def s(l: (Long, Long)*) = Schedule(l.toList)
}
