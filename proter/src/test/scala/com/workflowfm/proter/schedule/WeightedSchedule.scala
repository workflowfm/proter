package com.workflowfm.proter
package schedule

class WeightedScheduleTests extends TaskTester with WeightedScheduleTester {

  "The Weighted Schedule" must {

    "fit a task at the edge of another" in {
      s((1, 2, 2)) + (2, 3, 2) should be(Some(s((1, 3, 2))))
      s((3, 4, 3)) + (2, 3, 3) should be(Some(s((2, 4, 3))))
      s((1, 2, 1)) + (2, 3, 2) should be(Some(s((1, 2, 1), (2, 3, 2))))
    }

    "fit a task at the edge of two others" in {
      s((1, 2, 1), (3, 4, 1)) + (2, 3, 1) should be(Some(s((1, 4, 1))))
      s((1, 2, 1), (4, 5, 1)) + (2, 3, 1) should be(Some(s((1, 3, 1), (4, 5, 1))))
      s((1, 2, 1), (4, 5, 1)) + (3, 4, 1) should be(Some(s((1, 2, 1), (3, 5, 1))))
      s((1, 2, 1), (3, 4, 2)) + (2, 3, 3) should be(Some(s((1, 2, 1), (2, 3, 3), (3, 4, 2))))
    }

    "fit a task in gaps" in {
      s((1, 2, 1)) + (3, 4, 1) should be(Some(s((1, 2, 1), (3, 4, 1))))
      s((3, 4, 1)) + (1, 2, 1) should be(Some(s((1, 2, 1), (3, 4, 1))))
      s((1, 2, 1), (3, 4, 1)) + (5, 6, 1) should be(Some(s((1, 2, 1), (3, 4, 1), (5, 6, 1))))
    }

    "merge tasks that clash with the start of another task" in {
      s((1, 2, 1)) + (1, 3, 1) should be(Some(s((1, 2, 2), (2, 3, 1))))
      s((1, 4, 1)) + (1, 3, 1) should be(Some(s((1, 3, 2), (3, 4, 1))))
      s((2, 3, 1)) + (1, 3, 1) should be(Some(s((1, 2, 1), (2, 3, 2))))
      s((2, 4, 1)) + (1, 3, 1) should be(Some(s((1, 2, 1), (2, 3, 2), (3, 4, 1))))
    }

    "not fit tasks that clash with the end of another task" in {
      s((2, 4, 1)) + (3, 4, 1) should be(Some(s((2, 3, 1), (3, 4, 2))))
      s((3, 4, 1)) + (2, 4, 1) should be(Some(s((2, 3, 1), (3, 4, 2))))
      s((1, 4, 1)) + (1, 5, 1) should be(Some(s((1, 4, 2), (4, 5, 1))))
      s((1, 5, 1)) + (1, 4, 1) should be(Some(s((1, 4, 2), (4, 5, 1))))
      s((2, 3, 1)) + (1, 3, 1) should be(Some(s((1, 2, 1), (2, 3, 2))))
    }

    "merge tasks that overlap with another task" in {
      s((1, 3, 1)) + (2, 4, 1) should be(Some(s((1, 2, 1), (2, 3, 2), (3, 4, 1))))
      s((1, 3, 1)) + (1, 3, 1) should be(Some(s((1, 3, 2))))
      s((1, 4, 1)) + (2, 3, 1) should be(Some(s((1, 2, 1), (2, 3, 2), (3, 4, 1))))
      s((2, 3, 1)) + (1, 4, 1) should be(Some(s((1, 2, 1), (2, 3, 2), (3, 4, 1))))
    }

    "not fit tasks that clash with two other tasks" in {
      s((1, 2, 1), (4, 6, 1)) + (2, 5, 1) should be(Some(s((1, 4, 1), (4, 5, 2), (5, 6, 1))))
      s((1, 2, 1), (4, 6, 1)) + (3, 5, 1) should be(
        Some(s((1, 2, 1), (3, 4, 1), (4, 5, 2), (5, 6, 1)))
      )
      s((1, 2, 1), (4, 6, 1)) + (2, 6, 1) should be(Some(s((1, 4, 1), (4, 6, 2))))
      s((1, 2, 1), (4, 6, 1)) + (3, 6, 1) should be(Some(s((1, 2, 1), (3, 4, 1), (4, 6, 2))))
      s((1, 2, 1), (4, 6, 1)) + (1, 6, 1) should be(Some(s((1, 2, 2), (2, 4, 1), (4, 6, 2))))
      s((2, 3, 1), (4, 6, 1)) + (1, 7, 1) should be(
        Some(s((1, 2, 1), (2, 3, 2), (3, 4, 1), (4, 6, 2), (6, 7, 1)))
      )
      s((1, 3, 1), (4, 6, 2)) + (2, 5, 3) should be(
        Some(s((1, 2, 1), (2, 3, 4), (3, 4, 3), (4, 5, 5), (5, 6, 2)))
      )
    }

    "convert to equivalent binary schedule" in {
      s((1, 2, 1)).binary(1, 1) should be(s_bin((1, 2)))
      s((1, 2, 1), (3, 4, 1)).binary(1, 1) should be(s_bin((1, 2), (3, 4)))
      s((1, 2, 1), (2, 3, 2)).binary(2, 2) should be(s_bin((1, 3)))
      s((1, 2, 2), (2, 3, 4)).binary(2, 4) should be(s_bin((2, 3)))
      s((1, 3, 3), (4, 6, 2)).binary(1, 3) should be(s_bin((1, 3)))
      s((1, 2, 3), (2, 3, 4), (3, 4, 5)).binary(1, 10) should be(s_bin())
      s().binary(0, 0) should be(s_bin())
      s().binary(1, 0) should be(Schedule.Full)
    }

  }
}

trait WeightedScheduleTester {
  def s(l: (Long, Long, Int)*): WeightedSchedule = WeightedSchedule(l.toList)
  def s_bin(l: (Long, Long)*): Schedule = Schedule(l.toList)
}
