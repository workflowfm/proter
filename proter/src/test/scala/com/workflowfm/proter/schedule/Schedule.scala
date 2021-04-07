package com.workflowfm.proter.schedule

import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner

import com.workflowfm.proter.TaskTester

@RunWith(classOf[JUnitRunner])
class ScheduleTests extends TaskTester with ScheduleTester {

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
}

trait ScheduleTester {
  def s(l: (Long, Long)*): Schedule = Schedule(l.toList)
}
