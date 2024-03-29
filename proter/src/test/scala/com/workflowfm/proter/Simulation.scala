package com.workflowfm.proter

import java.util.UUID

import scala.concurrent._
import scala.util.{ Failure, Success }

import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike

class SimulationTests extends SimulationTester with MockManagerResponseMatcher {

  import MockManager._
  given ExecutionContextExecutor = ExecutionContext.global

  "Simulations" should {

    "interact correctly having no tasks" in {
      val manager: MockManager = new MockManager
      val sim = new NoTasks("sim", manager)

      manager.expected += SimDone("sim", Success(()))

      sim.run()
      manager should comply
    }

    "interact correctly having one task" in {
      val manager: MockManager = new MockManager
      val sim = new SingleTaskSimulation("sim", manager, Seq("r1"), Constant(2L))

      val ti1 = sim.theTask.create("sim", 0L)

      manager.reactions += sim.theTask -> Complete(sim, 2L, Seq(ti1))
      manager.expected ++= Seq(
        SimReady("sim", Seq(sim.theTask)),
        SimDone("sim", Success((ti1, 2L)))
      )

      sim.run()
      manager should comply
    }

    "interact correctly having 2 tasks in sequence with callback" in {
      val manager: MockManager = new MockManager
      val sim = new TwoTasks("sim", manager)

      val ti1 = sim.t1.create("sim", 0L)
      val ti2 = sim.t2.create("sim", 2L)

      manager.reactions += sim.t1 -> Complete(sim, 2L, Seq(ti1))
      manager.reactions += sim.t2 -> Complete(sim, 4L, Seq(ti2))

      manager.expected += SimReady("sim", Seq(sim.t1))
      manager.expected += SimReady("sim", Seq(sim.t2))
      manager.expected += SimDone("sim", Success((ti2, 4L)))

      sim.run()
      manager should comply
    }

    "interact correctly having 3 tasks in sequence with Futures" in {
      val manager: MockManager = new MockManager
      val sim = new ThreeFutureTasks("sim", manager)

      val ti1 = sim.t1.create("sim", 0L)
      val ti2 = sim.t2.create("sim", 2L)
      val ti3 = sim.t3.create("sim", 4L)

      manager.reactions += sim.t1 -> Complete(sim, 2L, Seq(ti1))
      manager.reactions += sim.t2 -> Complete(sim, 4L, Seq(ti2))
      manager.reactions += sim.t3 -> Complete(sim, 6L, Seq(ti3))

      manager.expected += SimReady("sim", Seq(sim.t1))
      manager.expected += SimReady("sim", Seq(sim.t2))
      manager.expected += SimReady("sim", Seq(sim.t3))
      manager.expected += SimDone("sim", Success((ti3, 6L)))

      sim.run()
      Thread.sleep(200)
      manager should comply
    }

    "stop between 2 tasks in sequence" in {
      val manager: MockManager = new MockManager
      val sim = new TwoTasks("sim", manager)

      val ti1 = sim.t1.create("sim", 0L)

      manager.reactions += sim.t1 -> Complete(sim, 2L, Seq(ti1))
      manager.reactions += sim.t2 -> Stop(sim)

      manager.expected += SimReady("sim", Seq(sim.t1))
      manager.expected += SimReady("sim", Seq(sim.t2))
      manager.expected += SimDone("sim", Failure(new Exception))

      sim.run()
      manager should comply
    }

    "stop after the 1st task in a sequence of 3 tasks with futures" in {
      val manager: MockManager = new MockManager
      val sim = new ThreeFutureTasks("sim", manager)

      manager.reactions += sim.t1 -> Stop(sim)

      manager.expected += SimReady("sim", Seq(sim.t1))
      manager.expected += SimDone("sim", Failure(new Exception))

      sim.run()
      Thread.sleep(200) // allow futures to complete
      manager should comply
    }

    "stop after the 2nd task in a sequence of 3 tasks with futures" in {
      val manager: MockManager = new MockManager
      val sim = new ThreeFutureTasks("sim", manager)

      val ti1 = sim.t1.create("sim", 0L)
      val ti2 = sim.t2.create("sim", 2L)

      manager.reactions += sim.t1 -> Complete(sim, 2L, Seq(ti1))
      manager.reactions += sim.t2 -> Stop(sim)

      manager.expected += SimReady("sim", Seq(sim.t1))
      manager.expected += SimReady("sim", Seq(sim.t2))
      manager.expected += SimDone("sim", Failure(new Exception))

      sim.run()
      Thread.sleep(200) // allow futures to complete
      manager should comply
    }

    // "reply to LookaheadNextItter messages" in {
    //     val sim = system.actorOf(Props(new SimLookaheadSeq("sim",self)))
    //     sim ! Simulation.Start
    //     expectMsg( Coordinator.SimStarted("sim"))
    /* val Coordinator.AddTask(id1, generator1, resources1) = expectMsgType[ Coordinator.AddTask ] */
    //     expectMsg( Coordinator.SimReady(None) )
    //     val response = Await.result((sim ? Simulation.LookaheadNextItter)(3.seconds),3.seconds)
    //     response should be (Unit)
    // }

    // "reply to TasksAfterThis messages with future tasks" in {
    //     val sim = system.actorOf(Props(new SimLookaheadSeq("sim",self)))

    //     sim ! Simulation.Start
    //     expectMsg( Coordinator.SimStarted("sim"))
    //     //task1
    /* val Coordinator.AddTask(id1, generator1, resources1) = expectMsgType[ Coordinator.AddTask ] */
    //     expectMsg( Coordinator.SimReady(None) )
    //     val task1 = generator1.create(id1,0L,sim,resources1:_*)

    //     Await.result((sim ? Simulation.LookaheadNextItter)(3.seconds),3.seconds)
    /* val response1 = Await.result((sim ?
     * Simulation.TasksAfterThis(id1,2L))(3.seconds),3.seconds).asInstanceOf[Seq[Task]] */
    //     response1.size should be (1)
    //     response1.head.name should be ("task2")

    //     sim ! Simulation.TaskCompleted(task1,2L)
    //     //task2
    /* val Coordinator.AddTask(id2, generator2, resources2) = expectMsgType[ Coordinator.AddTask ] */
    //     expectMsg( Coordinator.AckTasks(Seq(id1)))
    //     val task2 = generator2.create(id2,2L,sim,resources2:_*)

    //     Await.result((sim ? Simulation.LookaheadNextItter)(3.seconds),3.seconds)
    /* val response2 = Await.result((sim ?
     * Simulation.TasksAfterThis(id2,4L))(3.seconds),3.seconds).asInstanceOf[Seq[Task]] */
    //     response2.size should be (1)
    //     response2.head.name should be ("task3")

    //     sim ! Simulation.TaskCompleted(task2,4L)
    //     //task3
    /* val Coordinator.AddTask(id3, generator3, resources3) = expectMsgType[ Coordinator.AddTask ] */
    //     expectMsg( Coordinator.AckTasks(Seq(id2)))
    //     val task3 = generator1.create(id3,4L,sim,resources3:_*)

    //     Await.result((sim ? Simulation.LookaheadNextItter)(3.seconds),3.seconds)
    /* val response3 = Await.result((sim ?
     * Simulation.TasksAfterThis(id3,6L))(3.seconds),3.seconds).asInstanceOf[Seq[Task]] */
    //     response3.size should be (0)

    //     sim ! Simulation.TaskCompleted(task3,6L)

    // }
  }
}

trait SimulationTester extends AnyWordSpecLike with Matchers {

  class NoTasks(override val name: String, override protected val manager: Manager)
      extends Simulation {
    override def run(): Unit = succeed(()) // finish instantly
    override def complete(task: TaskInstance, time: Long): Unit = () // does nothing
    override def stop(): Unit = ()
  }

  class ThreeFutureTasks(
      override val name: String,
      override protected val manager: Manager,
      onFail: Throwable => Unit = _ => (),
      d1: Long = 2L,
      d2: Long = 2L,
      d3: Long = 3L
  )(
      using ExecutionContext
  ) extends AsyncSimulation
      with FutureTasks {

    val id1 = UUID.randomUUID
    val id2 = UUID.randomUUID
    val id3 = UUID.randomUUID

    val t1: Task = Task("task1", d1) withID id1 withResources Seq("r1")
    val t2: Task = Task("task2", d2) withID id2 withResources Seq("r1")
    val t3: Task = Task("task3", d3) withID id3 withResources Seq("r1")

    override def run(): Unit = {
      val task1 = futureTask(t1)
      ready()
      val task2 = task1 flatMap { _ =>
        val t = futureTask(t2)
        ack(Seq(id1))
        t
      }
      val task3 = task2 flatMap { _ =>
        val t = futureTask(t3)
        ack(Seq(id2))
        t
      }
      task3.onComplete {
        case Failure(ex) => onFail(ex)
        case _ => ()
      }
      task3.onComplete(done)
    }
  }

  class TwoTasks(
      override val name: String,
      override protected val manager: Manager,
      onFail: Throwable => Unit = _ => (),
      d1: Long = 2L,
      d2: Long = 2L
  ) extends AsyncSimulation {

    val id1 = UUID.randomUUID
    val id2 = UUID.randomUUID

    val t1: Task = Task("task1", d1) withID id1 withResources Seq("r1")
    val t2: Task = Task("task2", d2) withID id2 withResources Seq("r1")

    val callback: Callback = {
      case Success((t, _)) => {
        task(t2, r => done(r))
        ack(Seq(t.id))
      }
      case Failure(ex) => onFail(ex)
    }

    override def run(): Unit = {
      task(t1, callback)
      ready()
    }
  }

  // class SimLookaheadSeq(name: String, coordinator: ActorRef)
  // (implicit executionContext: ExecutionContext)
  // extends AsyncSimulation(name,coordinator) with Lookahead {
  //     val promise = Promise[Any]()
  //     override def run():Future[Any] = {
  //         val id1 = java.util.UUID.randomUUID
  //         val id2 = java.util.UUID.randomUUID
  //         val id3 = java.util.UUID.randomUUID
  /* val generator1 = TaskGenerator("task1","sim",Constant(2L),Constant(0L)) */
  /* val generator2 = TaskGenerator("task2","sim",Constant(2L),Constant(0L)) */
  /* val generator3 = TaskGenerator("task3","sim",Constant(2L),Constant(0L)) */
  //         val resources = Seq("r1")

  //         add1To1Lookahead(id1,id2,generator2,resources)
  //         add1To1Lookahead(id2,id3,generator3,resources)

  //         val task1 = task(id1,generator1,
  //             {(_,_)=> task(id2,generator2,
  //                 {(_,_)=> task(id3,generator3,
  //                     {(_,_)=> promise.success(Unit)},
  //                 resources); ack(Seq(id2)) },
  //             resources); ack(Seq(id1)) },
  //         resources)

  //         ready()
  //         promise.future
  //     }
  // }
}
