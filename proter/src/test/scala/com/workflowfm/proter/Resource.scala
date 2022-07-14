package com.workflowfm.proter

import org.scalatest.EitherValues


class ResourceTests extends TaskTester with ResourceTester with EitherValues {

  "ResourceState" when {

    "calling `hasCapacity`" should {

      "check capacity of an empty state" in {
        rs("Test", 1821)().hasCapacity `should` be(true)
      }

      "check capacity with a single task" in {
        rs("Test", 1821)((1L, 0L, 1820)).hasCapacity `should` be(true)
      }

      "check capacity with 2 tasks" in {
        rs("Test", 1821)((1L, 0L, 20), (2L, 0L, 1800)).hasCapacity `should` be(true)
      }

      "check full capacity with a single task" in {
        rs("Test", 1821)((1L, 0L, 1821)).hasCapacity `should` be(false)
      }

      "check full capacity with 2 tasks" in {
        rs("Test", 1821)((1L, 0L, 21), (2L, 0L, 1800)).hasCapacity `should` be(false)
      }

    }

    "calling `remainingCapacity`" should {

      "calculate capacity of an empty state" in {
        rs("Test", 1821)().remainingCapacity `should` be(1821)
      }

      "calculate capacity with a single task" in {
        rs("Test", 1821)((1L, 0L, 1801)).remainingCapacity `should` be(20)
      }

      "calculate capacity with 2 tasks" in {
        rs("Test", 1821)((1L, 0L, 20), (2L, 0L, 1800)).remainingCapacity `should` be(1)
      }

      "calculate full capacity with a single task" in {
        rs("Test", 1821)((1L, 0L, 1821)).remainingCapacity `should` be(0)
      }

      "calculate full capacity with 2 tasks" in {
        rs("Test", 1821)((1L, 0L, 21), (2L, 0L, 1800)).remainingCapacity `should` be(0)
      }

    }

    "calling `startTask`" should {

      "start a task in an empty state" in {
        val state = rs("Test", 1821)()
        val task = tc(1L, Seq(("Test", 1821)))
        val res = state.startTask(task, 5L) 
        res.value.currentTasks.keys `should` contain(task.id)
        res.value.remainingCapacity `should` be(0)
      }

      "not start a task in an empty state with not enough capacity" in {
        val state = rs("Test", 1821)()
        val task = tc(1L, Seq(("Test", 1822)))
        val res = state.startTask(task, 5L) 
        res.left.value.state `should` be(state)
      }

      "start a task in a state with 1 task" in {
        val state = rs("Test", 1821)((2L, 0L, 1801))
        val task = tc(1L, Seq(("Test", 19)))
        val res = state.startTask(task, 5L) 
        res.value.currentTasks.keys `should` contain(task.id)
        res.value.remainingCapacity `should` be(1)
      }

      "not start a task in a state with 1 task and not enough capacity" in {
        val state = rs("Test", 1821)((2L, 0L, 1801))
        val task = tc(1L, Seq(("Test", 21)))
        val res = state.startTask(task, 5L) 
        res.left.value.state `should` be(state)
      }

      "start a task in a state with 2 tasks" in {
        val state = rs("Test", 1821)((1L, 0L, 20), (2L, 0L, 1800))
        val task = tc(3L, Seq(("Test", 1)))
        val res = state.startTask(task, 5L) 
        res.value.currentTasks.keys `should` contain(task.id)
        res.value.remainingCapacity `should` be(0)
      }

      "not start a task in a state with 2 tasks and not enough capacity" in {
        val state = rs("Test", 1821)((1L, 0L, 20), (2L, 0L, 1800))
        val task = tc(3L, Seq(("Test", 12)))
        val res = state.startTask(task, 5L) 
        res.left.value.state `should` be(state)
      }

    }

    "calling `detach`" should {

      "remove a task and free its capacity" in {
        val state = rs("Test", 1821)((1L, 0L, 20), (2L, 0L, 1800))
        val res = state.detach(id(2L))
        res.currentTasks.keys `should` not contain(id(2L))
        res.remainingCapacity `should` be(1801)
      }

      "remove 2 tasks and free their capacity" in {
        val state = rs("Test", 1821)((1L, 0L, 20), (2L, 0L, 1800), (3L, 0L, 1))
        val res = state.detach(Seq(id(2L), id(1L)))
        res.currentTasks.keys `should` not contain(id(2L))
        res.currentTasks.keys `should` not contain(id(1L))
        res.remainingCapacity `should` be(1820)
      }

    }

  }
}

trait ResourceTester extends TaskTester {

  def rs(name: String, capacity: Int)(tasks: (Long, Long, Int)*): ResourceState = 
    ResourceState(
      Resource(name, capacity, 0), 
      Map() ++ tasks.map( (i, s, c) => 
        id(i) -> (s, tc(i, Seq((name, c))))
      )
    )

  final case class TestResourceMap(m: ResourceMap) {
    // pre-attach Tasks to resources
    def +(r: String, duration: Long): TestResourceMap = TestResourceMap(
      m.startTask(t(0L, Seq(r), Task.Medium, 0L, duration), 0L).getOrElse(m)
    )
  }

  object TestResourceMap {
    def apply(names: String*): TestResourceMap = TestResourceMap(
      ResourceMap(names.map(Resource(_, 1, 0)))
    )
  }
}
