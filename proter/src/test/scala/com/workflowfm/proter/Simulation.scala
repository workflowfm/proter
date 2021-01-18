package com.workflowfm.proter

import java.util.UUID

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{ Failure, Success }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import org.scalamock.scalatest.MockFactory


class SimulationTests extends SimulationTester with MockFactory {

  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global
  implicit val timeout: FiniteDuration = 10.seconds

  "Simulations" must {

    "interact correctly having  no tasks" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new NoTasks("sim", mockinator)

      (mockinator.simDone _).expects("sim", Success(Unit)).once()

      sim.run()
    }

    "interact correctly having one task" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new SingleTaskSimulation("sim", mockinator, Seq("r1"), ConstantGenerator(2L))

      var t1: Option[Task] = None

      inSequence {
        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head.duration == ConstantGenerator(2L)
        } ) onCall {
          (_: String, ts: Seq[Task]) => t1 = Some(ts.head)
        } once

        mockinator.simReady _ expects "sim" onCall { s: String => t1 match {
          case None => fail("No task was captured!")
          case Some(t) => sim.complete(t.create(s, 0L), 2L)
        } } once

        mockinator.simDone _ expects ("sim", Success(*)) once
      }

      sim.run()
    }

    "interact correctly having 2 tasks in sequence with callback" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new TwoTasks("sim", mockinator)

      var t1: Option[Task] = None
      var t2: Option[Task] = None

      inSequence {
        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head.name.equals("task1")
        } ) onCall {
          (_: String, ts: Seq[Task]) => t1 = Some(ts.head)
        } once

        mockinator.simReady _ expects "sim" onCall { s: String => t1 match {
          case None => fail("No task was captured!")
          case Some(t) => sim.complete(t.create(s, 0L), 2L)
        } } once

       mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head.name.equals("task2")
        } ) onCall {
          (_: String, ts: Seq[Task]) => t2 = Some(ts.head)
        } once

        mockinator.ackTask _ expects ("sim", *) onCall { (s: String, _: Seq[UUID]) => t2 match {
          case None => fail("No task was captured!")
          case Some(t) => sim.complete(t.create(s, 2L), 4L)
        } } once

        mockinator.simDone _ expects ("sim", Success(*)) once
      }

      sim.run()
    }

    "interact correctly having 3 tasks in sequence with Futures" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new ThreeFutureTasks("sim", mockinator)

      var t1: Option[Task] = None
      var t2: Option[Task] = None
      var t3: Option[Task] = None

      inSequence {
        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head.name.equals("task1")
        } ) onCall {
          (_: String, ts: Seq[Task]) => t1 = Some(ts.head)
        } once

        mockinator.simReady _ expects "sim" onCall { s: String => t1 match {
          case None => fail("No task was captured!")
          case Some(t) => sim.complete(t.create(s, 0L), 2L)
        } } once

       mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head.name.equals("task2")
        } ) onCall {
          (_: String, ts: Seq[Task]) => t2 = Some(ts.head)
        } once

        mockinator.ackTask _ expects ("sim", *) onCall { (s: String, _: Seq[UUID]) => t2 match {
          case None => fail("No task was captured!")
          case Some(t) => sim.complete(t.create(s, 2L), 4L)
        } } once

       mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head.name.equals("task3")
        } ) onCall {
          (_: String, ts: Seq[Task]) => t3 = Some(ts.head)
        } once

        mockinator.ackTask _ expects ("sim", *) onCall { (s: String, _: Seq[UUID]) => t3 match {
          case None => fail("No task was captured!")
          case Some(t) => sim.complete(t.create(s, 4L), 6L)
        } } once

        mockinator.simDone _ expects ("sim", Success(*)) once
      }

      sim.run()
    }

/*
    "stop between 2 tasks in sequence" in {
      val sim = system.actorOf(Props(new TwoTasks("sim", self)))

      sim ! Simulation.Start
      expectMsg(Coordinator.SimStarted("sim", sim))

      //task1
      val Coordinator.AddTask(generator1) = expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.SimReady(None))

      sim ! Simulation.Stop

      val task1 = generator1.create(0L, sim)
      sim ! Simulation.TaskCompleted(task1, 2L)

      expectNoMsg()

    }

    "fail the callback with a SimulationStoppingException when stopping" in {
      val sim = system.actorOf(Props(new TwoTasks("sim", self, ex => self ! ex)))

      sim ! Simulation.Start
      expectMsg(Coordinator.SimStarted("sim", sim))

      //task1
      expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.SimReady(None))

      sim ! Simulation.Stop

      expectMsgType[Simulation.SimulationStoppingException]

    }

    "stop after the 1st task in a sequence of 3 tasks with futures" in {
      val sim = system.actorOf(Props(new ThreeFutureTasks("sim", self)))

      ignoreMsg {
        case Coordinator.SimDone("sim", Failure(_: Simulation.SimulationStoppingException)) => true
      }

      sim ! Simulation.Start
      expectMsg(Coordinator.SimStarted("sim", sim))

      //task1
      val Coordinator.AddTask(generator1) = expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.SimReady(None))

      sim ! Simulation.Stop

      val task1 = generator1.create(0L, sim)
      sim ! Simulation.TaskCompleted(task1, 2L)

      expectNoMsg()
      ignoreNoMsg()
    }

    "fail the Futures with a SimulationStoppingException when stopping" in {
      val sim = system.actorOf(Props(new ThreeFutureTasks("sim", self, ex => self ! ex)))

      ignoreMsg {
        case Coordinator.SimDone("sim", Failure(_: Simulation.SimulationStoppingException)) => true
      }

      sim ! Simulation.Start
      expectMsg(Coordinator.SimStarted("sim", sim))

      //task1
      expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.SimReady(None))

      sim ! Simulation.Stop

      expectMsgType[Simulation.SimulationStoppingException]
      ignoreNoMsg()
    }

    "stop after the 2nd task in a sequence of 3 tasks with futures" in {
      val sim = system.actorOf(Props(new ThreeFutureTasks("sim", self)))

      ignoreMsg {
        case Coordinator.SimDone("sim", Failure(_: Simulation.SimulationStoppingException)) => true
      }

      sim ! Simulation.Start
      expectMsg(Coordinator.SimStarted("sim", sim))

      //task1
      val Coordinator.AddTask(generator1) = expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.SimReady(None))
      val task1 = generator1.create(0L, sim)
      sim ! Simulation.TaskCompleted(task1, 2L)

      //task2
      expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.AckTasks(Seq(generator1.id)))

      sim ! Simulation.Stop

      expectNoMsg()
      ignoreNoMsg()
    }
 */
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

class SimulationTester
    extends WordSpecLike
    with Matchers
    with BeforeAndAfterAll {

  class NoTasks(name: String, coordinator: Manager) extends Simulation(name, coordinator) {
    override def run(): Unit = succeed(Unit) //finish instantly
    override def complete(task: TaskInstance, time: Long): Unit = Unit //does nothing
    override def stop(): Unit = Unit
  }

  class ThreeFutureTasks(
      name: String,
      coordinator: Manager,
      onFail: Throwable => Unit = _ => Unit,
      d1: Long = 2L,
      d2: Long = 2L,
      d3: Long = 3L
  )(
      implicit executionContext: ExecutionContext
  ) extends AsyncSimulation(name, coordinator)
      with FutureTasks {

    override def run(): Unit = {
      val id1 = UUID.randomUUID
      val id2 = UUID.randomUUID
      val id3 = UUID.randomUUID

      val task1 = futureTask(
        Task("task1", d1) withID id1 withResources Seq("r1")
      )
      ready()
      val task2 = task1 flatMap { _ =>
        val t = futureTask(
          Task("task2", d2) withID id2 withResources Seq("r1")
        )
        ack(Seq(id1))
        t
      }
      val task3 = task2 flatMap { _ =>
        val t = futureTask(
          Task("task3", d3) withID id3 withResources Seq("r1")
        )
        ack(Seq(id2))
        t
      }
      task3.onFailure { case ex => onFail(ex) }
      task3.onComplete(done)
    }
  }

  class TwoTasks(
      name: String,
      coordinator: Manager,
      onFail: Throwable => Unit = _ => Unit,
      d1: Long = 2L,
      d2: Long = 2L
  ) extends AsyncSimulation(name, coordinator)
      with FutureTasks {

    val id1 = UUID.randomUUID
    val id2 = UUID.randomUUID

    val generator1: Task = Task("task1", d1) withID id1 withResources Seq("r1")

    val generator2: Task = Task("task2", d2) withID id2 withResources Seq("r1")

    val callback: Callback = {
      case Success((t, _)) => {
        task(generator2, r => done(r))
        ack(Seq(t.id))
      }
      case Failure(ex) => onFail(ex)
    }

    override def run(): Unit = {
      task(generator1, callback)
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
  /* val generator1 = TaskGenerator("task1","sim",ConstantGenerator(2L),ConstantGenerator(0L)) */
  /* val generator2 = TaskGenerator("task2","sim",ConstantGenerator(2L),ConstantGenerator(0L)) */
  /* val generator3 = TaskGenerator("task3","sim",ConstantGenerator(2L),ConstantGenerator(0L)) */
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
 
