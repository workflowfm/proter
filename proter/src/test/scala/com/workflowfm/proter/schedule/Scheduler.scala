package com.workflowfm.proter.schedule

import com.workflowfm.proter.{
  Task,
  TaskTester,
  TaskResource,
  Lookahead,
  NoLookahead,
  Constant,
  TaskInstance
}

class SchedulerTests extends TaskTester with ScheduleTester {

  "GreedyFCFSScheduler" should {

    "select a single task" in {
      val m = new TestResourceMap("A")
      m.gf(t(1L, Seq("A"))) should be(Seq(1L))
    }

    "select multiple tasks" in {
      val m = new TestResourceMap("A", "B")
      m.gf(
        t(1L, Seq("A")),
        t(2L, Seq("B"))
      ) should be(Seq(1L, 2L))
    }

    "select an earlier task ignoring priority" in {
      val m = new TestResourceMap("A")
      m.gf(t(1L, Seq("A"), Task.Low), t(2L, Seq("A"), Task.High)) should be(Seq(1L))
    }

    "select an earlier task ignoring creation time" in {
      val m = new TestResourceMap("A")
      m.gf(t(1L, Seq("A"), Task.Medium, 2L), t(2L, Seq("A"), Task.Medium, 1L)) should be(Seq(1L))
    }

    "not select a blocked task" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.gf(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("B"), Task.VeryLow, 1L, 2L)) should be(Nil)
    }

    "select a later task if the earlier one is blocked" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.gf(t(1L, Seq("A", "B"), Task.Medium), t(2L, Seq("A"), Task.Medium, 1L)) should be(List(2L))
    }

    "greedily block earlier tasks" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.gf(t(1L, Seq("A", "B"), Task.Low), t(2L, Seq("A"), Task.Medium, 1L, 100L)) should be(
        Seq(2L)
      )
    }

    "consider all earlier tasks for availability" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.gf(
        t(1L, Seq("B"), Task.Medium),
        t(2L, Seq("A"), Task.Medium),
        t(3L, Seq("A"), Task.High)
      ) should be(List(2L))
    }
  }

  "StrictFCFSScheduler" must {

    "select a single task" in {
      val m = new TestResourceMap("A")
      m.sf(t(1L, Seq("A"))) should be(Seq(1L))
    }

    "select multiple tasks" in {
      val m = new TestResourceMap("A", "B")
      m.sf(
        t(1L, Seq("A")),
        t(2L, Seq("B"))
      ) should be(Seq(1L, 2L))
    }

    "select an earlier task ignoring priority" in {
      val m = new TestResourceMap("A")
      m.sf(t(1L, Seq("A"), Task.Low), t(2L, Seq("A"), Task.High)) should be(Seq(1L))
    }

    "select an earlier task ignoring creation time" in {
      val m = new TestResourceMap("A")
      m.sf(t(1L, Seq("A"), Task.Medium, 2L), t(2L, Seq("A"), Task.Medium, 1L)) should be(Seq(1L))
    }

    "not select a blocked task" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.sf(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("B"), Task.VeryLow, 1L, 2L)) should be(Nil)
    }

    "not select a later task if the earlier one is blocked" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.sf(t(1L, Seq("A", "B"), Task.Medium), t(2L, Seq("A"), Task.Medium, 1L)) should be(Nil)
    }

    "not block earlier tasks" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.sf(t(1L, Seq("A", "B"), Task.Low), t(2L, Seq("A"), Task.Medium, 1L, 100L)) should be(Nil)
    }

    "consider all earlier tasks for availability" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.sf(
        t(1L, Seq("B"), Task.Medium),
        t(2L, Seq("A"), Task.Medium),
        t(3L, Seq("A"), Task.High)
      ) should be(List(2L))
    }
  }

  "GreedyPriorityScheduler" must {

    "select a single task" in {
      val m = new TestResourceMap("A")
      m.g(t(1L, Seq("A"))) should be(Seq(1L))
    }

    "select multiple tasks" in {
      val m = new TestResourceMap("A", "B")
      m.g(
        t(1L, Seq("A")),
        t(2L, Seq("B"))
      ) should be(Seq(1L, 2L))
    }

    "select an earlier task" in {
      val m = new TestResourceMap("A")
      m.g(t(1L, Seq("A"), Task.Medium, 2L), t(2L, Seq("A"), Task.Medium, 1L)) should be(Seq(2L))
    }

    "not select a blocked task" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.g(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("B"), Task.VeryLow, 0L, 2L)) should be(Nil)
    }

    "select a lower priority task if the higher priority one is blocked" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.g(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow)) should be(List(2L))
    }

    "greedily block higher priority tasks" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.g(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow, 0L, 100L)) should be(
        Seq(2L)
      )
    }

    "consider all higher priority tasks for availability" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.g(
        t(1L, Seq("B"), Task.Highest),
        t(2L, Seq("A"), Task.Medium),
        t(3L, Seq("A"), Task.VeryLow, 0L, 2L)
      ) should be(List(2L))
    }

    "select higher priority tasks" in {
      val m = new TestResourceMap("A", "B")
      m.g(
        t(2L, Seq("A", "B"), Task.Medium),
        t(1L, Seq("A"), Task.Highest)
      ) should be(List(1L))
    }
  }

  "StrictPriorityScheduler" must {

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

    "select an earlier task by creation time" in {
      val m = new TestResourceMap("A")
      m.s(t(1L, Seq("A"), Task.Medium, 2L), t(2L, Seq("A"), Task.Medium, 1L)) should be(Seq(2L))
    }

    "not select a blocked task" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.s(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("B"), Task.VeryLow, 0L, 2L)) should be(Nil)
    }

    "not select a lower priority task if the higher priority one is blocked" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.s(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow)) should be(Nil)
    }

    "not block higher priority tasks" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.s(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow, 0L, 100L)) should be(
        Nil
      )
    }

    "consider all higher priority tasks for availability" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.s(
        t(1L, Seq("B"), Task.Highest),
        t(2L, Seq("A"), Task.Medium),
        t(3L, Seq("A"), Task.VeryLow, 0L, 2L)
      ) should be(List(2L))
    }

    "select higher priority tasks" in {
      val m = new TestResourceMap("A", "B")
      m.s(
        t(2L, Seq("A", "B"), Task.Medium),
        t(1L, Seq("A"), Task.Highest)
      ) should be(List(1L))
    }
  }

  "ProterScheduler" must {

    "select a single task" in {
      val m = new TestResourceMap("A")
      m.p(t(1L, Seq("A"))) should be(Seq(1L))
    }

    "select multiple tasks" in {
      val m = new TestResourceMap("A", "B")
      m.p(
        t(1L, Seq("A")),
        t(2L, Seq("B"))
      ) should be(Seq(1L, 2L))
    }

    "select an earlier task by creation time" in {
      val m = new TestResourceMap("A")
      m.p(t(1L, Seq("A"), Task.Medium, 2L), t(2L, Seq("A"), Task.Medium, 1L)) should be(Seq(2L))
    }

    "not select a blocked task" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.p(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow, 0L, 2L)) should be(Nil)
    }

    "select a lower priority task if it will finish on time" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.p(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow)) should be(List(2L))
    }

    "not block higher priority tasks" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      /* ProterScheduler.nextEstimatedTaskStart(t(1L,Seq("A","B"),Task.Highest), 0L, m.m,Seq(
       * t(1L,Seq("A","B"),Task.Highest),t(2L,Seq("A"),Task.VeryLow,0L,100L))) should be (1L) */
      m.p(t(1L, Seq("A", "B"), Task.Highest), t(2L, Seq("A"), Task.VeryLow, 0L, 100L)) should be(
        Nil
      )
    }

    "not block higher priority tasks based on ordering" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.p(t(1L, Seq("A", "B"), Task.Medium, 0L), t(2L, Seq("A"), Task.Medium, 2L, 100L)) should be(
        Nil
      )
    }

    "not block higher priority tasks of other resources" in {
      val m = new TestResourceMap("A", "B") //+ ("B",1L)
      m.p(t(1L, Seq("B"), Task.Highest), t(2L, Seq("A", "B"), Task.VeryLow, 0L, 100L)) should be(
        Seq(1L)
      )
    }

    "consider all higher priority tasks for availability" in {
      val m = new TestResourceMap("A", "B") + ("B", 1L)
      m.p(
        t(1L, Seq("B"), Task.Highest),
        t(2L, Seq("A", "B"), Task.Medium),
        t(3L, Seq("A"), Task.VeryLow, 0L, 2L)
      ) should be(List(3L))
    }

    "select higher priority tasks" in {
      val m = new TestResourceMap("A", "B")
      m.p(
        t(2L, Seq("A", "B"), Task.Medium),
        t(1L, Seq("A"), Task.Highest)
      ) should be(List(1L))
    }
  }

  "LookaheadScheduler" must {
    "select a single task without a lookahead structure" in {
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
      m.lookahead = NoLookahead +> (id(1L), Task(
              "t2",
              Some(id(2L)),
              Constant(2L),
              Constant(2L)
            ))
      m.l(t(1L, Seq("A"))) should be(Seq(1L))
    }
    "not select a task which would offset a higher priority future task" in {
      val m = new TestResourceMap("A", "B")
      m.lookahead = NoLookahead +> (id(1L), Task(
              "t2",
              Some(id(2L)),
              Constant(5L),
              Constant(5L)
            ).withPriority(Task.High).withResources(Seq("B")))
      m.l(
        t(1L, Seq("A"), Task.High),
        t(2L, Seq("B"), duration = 5L)
      ) should be(Seq(1L))
    }
    "select a task which offsets a lower priority future task" in {
      val m = new TestResourceMap("A", "B")
      m.lookahead = NoLookahead +> (id(1L), Task(
              "t2",
              Some(id(2L)),
              Constant(5L),
              Constant(5L)
            ).withPriority(Task.Low).withResources(Seq("B")))
      m.l(
        t(1L, Seq("A"), Task.High),
        t(2L, Seq("B"), duration = 5L)
      ) should be(Seq(1L, 2L))
    }
    "consider a distant future task" in {
      val m = new TestResourceMap("A", "B")
      m.lookahead = NoLookahead +> (id(1L), Task(
              "t2",
              Some(id(2L)),
              Constant(2L),
              Constant(2L)
            ).withPriority(Task.High).withResources(Seq("A")))
      m.lookahead = m.lookahead +> (id(2L), Task(
              "t3",
              Some(id(3L)),
              Constant(5L),
              Constant(5L)
            ).withPriority(Task.High).withResources(Seq("B")))
      m.l(
        t(1L, Seq("A"), Task.High),
        t(3L, Seq("B"), duration = 5L)
      ) should be(Seq(1L))
    }
    "consider currently-running tasks" in {
      val m = new TestResourceMap("A")
      m.lookahead = NoLookahead +> (id(1L), Task(
              "t2",
              Some(id(2L)),
              Constant(5L),
              Constant(5L)
            ).withPriority(Task.Low).withResources(Seq("B")))
      m.m.get("A").map { _.startTask(t(1L, Seq("A"), Task.High, 0L, 5L), 0L) }
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
    def +(r: String, duration: Long): TestResourceMap = {
      m.get(r).map { _.startTask(t(0L, Seq(r), Task.Medium, 0L, duration), 0L) }
      this
    }

    // test GreedyPriorityScheduler
    def g(tasks: TaskInstance*): Seq[Long] =
      new GreedyPriorityScheduler(tasks: _*).getNextTasks(0L, m) map (_.id
            .getMostSignificantBits())

    // test StrictPriorityScheduler
    def s(tasks: TaskInstance*): Seq[Long] =
      new StrictPriorityScheduler(tasks: _*).getNextTasks(0L, m) map (_.id
            .getMostSignificantBits())

    // test GreedyFCFSScheduler
    def gf(tasks: TaskInstance*): Seq[Long] =
      new GreedyFCFSScheduler(tasks: _*).getNextTasks(0L, m) map (_.id
            .getMostSignificantBits())

    // test StrictFCFSScheduler
    def sf(tasks: TaskInstance*): Seq[Long] =
      new StrictFCFSScheduler(tasks: _*).getNextTasks(0L, m) map (_.id
            .getMostSignificantBits())

    // test ProterScheduler
    def p(tasks: TaskInstance*): Seq[Long] =
      new ProterScheduler(tasks: _*).getNextTasks(0L, m) map (_.id
            .getMostSignificantBits())

    // test LookaheadScheduler
    def l(tasks: TaskInstance*): Seq[Long] = {
      val ls = new LookaheadScheduler(tasks: _*)
      ls.setLookahead("Test", lookahead)
      ls.getNextTasks(0L, m) map (_.id
        .getMostSignificantBits())
    }
  }
}
