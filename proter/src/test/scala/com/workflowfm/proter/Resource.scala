package com.workflowfm.proter

import org.scalatest.{ EitherValues, OptionValues, LoneElement }

import java.util.UUID

class ResourceTests
    extends TaskTester
    with ResourceTester
    with EitherValues
    with OptionValues
    with LoneElement {

  "ResourceState" when {

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
        val res = state.detach(id(2L)).value
        res.resource.currentTasks.keys `should` not contain (id(2L))
        res.resource.remainingCapacity `should` be(1801)
        res.task.id `should` be(id(2L))
      }

      "not remove a task that is not there" in {
        val state = rs("Test", 1821)((1L, 0L, 20), (2L, 0L, 1800))
        val res = state.detach(id(3L))
        res `should` be(None)
      }
    }
  }

  "ResourceMap" when {
    val rmap = ResourceMap(
      Map() ++ Seq(
        "Empty" -> rsc("Empty", 2)(),
        "Full" -> rsc("Full", 2)(
          rsTask(1L, 0L)(("Full", 1), ("Half1", 1))
        ),
        "Half1" -> rsc("Half1", 4)(
          rsTask(1L, 0L)(("Full", 1), ("Half1", 1)),
          rsTask(2L, 0L)(("Half1", 1), ("Half2", 2))
        ),
        "Half2" -> rsc("Half2", 4)(
          rsTask(2L, 0L)(("Half1", 1), ("Half2", 2))
        )
      )
    )

    "calling `startTask`" should {
      "start a task with a free resource" in {
        val rt = rsTask(3L, 1L)(("Empty", 2))
        val res = rmap.startTask(rt._2._2, 1L)
        res.value.resources.get("Empty").value.currentTasks.keys.loneElement `should` be(id(3L))
        res.value.resources.get("Full").value.currentTasks.keys.loneElement `should` be(id(1L))
        res.value.resources.get("Half1").value.currentTasks.size `should` be(2)
        res.value.resources.get("Half2").value.currentTasks.keys.loneElement `should` be(id(2L))
      }

      "start a task with a half-full resource" in {
        val rt = rsTask(3L, 1L)(("Half1", 2))
        val res = rmap.startTask(rt._2._2, 1L)
        res.value.resources.get("Empty").value.currentTasks `shouldBe` empty
        res.value.resources.get("Full").value.currentTasks.keys.loneElement `should` be(id(1L))
        res.value.resources.get("Half1").value.currentTasks.keys `should` contain(id(1L))
        res.value.resources.get("Half1").value.currentTasks.keys `should` contain(id(2L))
        res.value.resources.get("Half1").value.currentTasks.keys `should` contain(id(3L))
        res.value.resources.get("Half2").value.currentTasks.keys.loneElement `should` be(id(2L))
      }

      "start a task with 2 resources" in {
        val rt = rsTask(3L, 1L)(("Empty", 2), ("Half1", 2))
        val res = rmap.startTask(rt._2._2, 1L)
        res.value.resources.get("Empty").value.currentTasks.keys.loneElement `should` be(id(3L))
        res.value.resources.get("Full").value.currentTasks.keys.loneElement `should` be(id(1L))
        res.value.resources.get("Half1").value.currentTasks.keys `should` contain(id(1L))
        res.value.resources.get("Half1").value.currentTasks.keys `should` contain(id(2L))
        res.value.resources.get("Half1").value.currentTasks.keys `should` contain(id(3L))
        res.value.resources.get("Half2").value.currentTasks.keys.loneElement `should` be(id(2L))
      }

      "not start a task with a full resource" in {
        val rt = rsTask(3L, 1L)(("Full", 2))
        val res = rmap.startTask(rt._2._2, 1L)
        res `should` be(Left(ResourceState.Full(rmap.resources.get("Full").value)))
      }

      "not start a task with an empty resource with not enough capacity" in {
        val rt = rsTask(3L, 1L)(("Empty", 12))
        val res = rmap.startTask(rt._2._2, 1L)
        res `should` be(Left(ResourceState.Full(rmap.resources.get("Empty").value)))
      }

      "not start a task with an half-full resource with not enough capacity" in {
        val rt = rsTask(3L, 1L)(("Half1", 12))
        val res = rmap.startTask(rt._2._2, 1L)
        res `should` be(Left(ResourceState.Full(rmap.resources.get("Half1").value)))
      }

      "not start a task with the first of 2 resources full" in {
        val rt = rsTask(3L, 1L)(("Half2", 3), ("Empty", 2))
        val res = rmap.startTask(rt._2._2, 1L)
        res `should` be(Left(ResourceState.Full(rmap.resources.get("Half2").value)))
      }

      "not start a task with the second of 2 resources full" in {
        val rt = rsTask(3L, 1L)(("Empty", 2), ("Half1", 12))
        val res = rmap.startTask(rt._2._2, 1L)
        res `should` be(Left(ResourceState.Full(rmap.resources.get("Half1").value)))
      }
    }

    "calling `stopTasks`" should {
      "stop 1 task correctly" in {
        val (res, detached) = rmap.stopTasks(Seq(id(1L)))
        res.resources.get("Empty").value.currentTasks `shouldBe` empty
        res.resources.get("Full").value.currentTasks `shouldBe` empty
        res.resources.get("Half1").value.currentTasks.keys.loneElement `should` be(id(2L))
        res.resources.get("Half2").value.currentTasks.keys.loneElement `should` be(id(2L))

        detached.size `should` be(2)
        val states = detached.map(_.resource.resource.name)
        states `should` contain("Full")
        states `should` contain("Half1")
      }

      "stop 2 tasks correctly" in {
        val (res, detached) = rmap.stopTasks(Seq(id(1L), id(2L)))
        res.resources.get("Empty").value.currentTasks `shouldBe` empty
        res.resources.get("Full").value.currentTasks `shouldBe` empty
        res.resources.get("Half1").value.currentTasks `shouldBe` empty
        res.resources.get("Half2").value.currentTasks `shouldBe` empty

        detached.size `should` be(4)
        val states = detached.map(_.resource.resource.name)
        states `should` contain("Full")
        states `should` contain("Half1")
        states `should` contain("Half2")
      }
    }

    "calling `get`" should {
      "get the right resources for a task" in {
        val rt = rsTask(1L, 0L)(("Full", 1), ("Half1", 1))
        val res = rmap.get(rt._2._2)
        res.size `should` be(2)
        res `should` contain(rmap.resources.get("Full").value)
        res `should` contain(rmap.resources.get("Half1").value)
      }
    }

  }

}

trait ResourceTester extends TaskTester {

  def rsTask(tid: Long, start: Long)(resources: (String, Int)*): (UUID, (Long, TaskInstance)) =
    id(tid) -> (start, tc(tid, resources))

  def rsc(name: String, capacity: Int)(tasks: (UUID, (Long, TaskInstance))*): ResourceState =
    ResourceState(
      Resource(name, capacity, 0),
      Map() ++ tasks
    )

  def rs(name: String, capacity: Int)(tasks: (Long, Long, Int)*): ResourceState =
    rsc(name, capacity)(tasks.map((i, s, c) => rsTask(i, s)((name, c))): _*)

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
