package com.workflowfm.proter

import java.util.UUID

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }

import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import org.scalamock.scalatest.MockFactory


class SimulationTests extends SimulationTester with MockFactory {

  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global
  implicit val timeout: FiniteDuration = 10.seconds

  "Simulations" must {

    "interact correctly having no tasks" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new NoTasks("sim", mockinator)

      (mockinator.simDone _).expects("sim", Success(Unit)).once()

      sim.run()
    }

    "interact correctly having one task" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new SingleTaskSimulation("sim", mockinator, Seq("r1"), ConstantGenerator(2L))

      val ti1 = sim.theTask.create("sim", 0L)

      inSequence {
        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.theTask
        } ) once

        mockinator.simReady _ expects "sim" onCall { _: String => sim.complete(ti1, 2L) } once

        mockinator.simDone _ expects ("sim", Success((ti1, 2L))) once
      }

      sim.run()
    }

    "interact correctly having 2 tasks in sequence with callback" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new TwoTasks("sim", mockinator)

      val ti1 = sim.t1.create("sim", 0L)
      val ti2 = sim.t2.create("sim", 2L)

      inSequence {
        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t1
        } ) once

        mockinator.simReady _ expects "sim" onCall { _: String => sim.complete(ti1, 2L) } once

        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t2
        } ) once

        mockinator.ackTask _ expects ("sim", Seq(ti1.id)) onCall { (_: String, _: Seq[UUID]) => sim.complete(ti2, 4L) } once

        mockinator.simDone _ expects ("sim", Success((ti2, 4L))) once
      }

      sim.run()
    }

    "interact correctly having 3 tasks in sequence with Futures" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new ThreeFutureTasks("sim", mockinator)

      val ti1 = sim.t1.create("sim", 0L)
      val ti2 = sim.t2.create("sim", 2L)
      val ti3 = sim.t3.create("sim", 4L)

      inSequence {
        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t1
        } ) once
 
       mockinator.simReady _ expects "sim" onCall { _: String => sim.complete(ti1, 2L) } once

        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t2
        } ) once

        mockinator.ackTask _ expects ("sim", Seq(ti1.id)) onCall { (_: String, _: Seq[UUID]) => sim.complete(ti2, 4L) } once

        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t3
        } ) once

        mockinator.ackTask _ expects ("sim", Seq(ti2.id)) onCall { (_: String, _: Seq[UUID]) => sim.complete(ti3, 6L) } once

        mockinator.simDone _ expects ("sim", Success((ti3, 6L))) once
      }

      sim.run()
      Thread.sleep(500) //allow futures to complete
    }

    "stop between 2 tasks in sequence" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new TwoTasks("sim", mockinator)

      val ti1 = sim.t1.create("sim", 0L)
      val ti2 = sim.t2.create("sim", 2L)

      inSequence {
        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t1
        } ) once

        mockinator.simReady _ expects "sim" onCall { _: String => sim.complete(ti1, 2L) } once

        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t2
        } ) once

        mockinator.ackTask _ expects ("sim", Seq(ti1.id)) onCall { (_: String, _: Seq[UUID]) => {
          sim.stop()
          sim.complete(ti2, 4L)
        } } once

        mockinator.simDone _ expects (where { (s: String, t: Try[Any]) => s == "sim" && (t match {
          case Failure(_: Simulation.SimulationStoppingException) => true
          case _ => false 
        })}) once

        mockinator.simDone _ expects ("sim", *) never
      }

      sim.run()
    }

    "stop after the 1st task in a sequence of 3 tasks with futures" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new ThreeFutureTasks("sim", mockinator)

      val ti1 = sim.t1.create("sim", 0L)

      inSequence {
        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t1
        } ) once
 
        mockinator.simReady _ expects "sim" onCall { _: String => {
          sim.stop()
          sim.complete(ti1, 4L)
        } } once
        
        mockinator.addTask _ expects (*, *) never

        mockinator.ackTask _ expects (*, *) never

        mockinator.simDone _ expects (where { (s: String, t: Try[Any]) => s == "sim" && (t match {
          case Failure(_: Simulation.SimulationStoppingException) => true
          case _ => false 
        })}) once
      }

      sim.run()
      Thread.sleep(500)
    }
  
    "stop after the 2nd task in a sequence of 3 tasks with futures" in {
      val mockinator: Coordinator = mock[Coordinator]
      val sim = new ThreeFutureTasks("sim", mockinator)

      val ti1 = sim.t1.create("sim", 0L)
      val ti2 = sim.t2.create("sim", 2L)

      inSequence {
        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t1
        } ) once
 
        mockinator.simReady _ expects "sim" onCall { _: String => sim.complete(ti1, 2L) } once

        mockinator.addTask _ expects (where {
          (s: String, ts: Seq[Task]) => s == "sim" && ts.length == 1 && ts.head == sim.t2
        } ) once

        mockinator.ackTask _ expects ("sim", Seq(ti1.id)) onCall { (_: String, _: Seq[UUID]) => {
          sim.stop()
          sim.complete(ti2, 4L)
        } } once
        
        mockinator.addTask _ expects (*, *) never

        mockinator.ackTask _ expects (*, *) never

        mockinator.simDone _ expects (where { (s: String, t: Try[Any]) => s == "sim" && (t match {
          case Failure(_: Simulation.SimulationStoppingException) => true
          case _ => false 
        })}) once
      }

      sim.run()
      Thread.sleep(500)
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

    val id1 = UUID.randomUUID
    val id2 = UUID.randomUUID
    val id3 = UUID.randomUUID

    val t1 = Task("task1", d1) withID id1 withResources Seq("r1")
    val t2 = Task("task2", d2) withID id2 withResources Seq("r1")
    val t3 = Task("task3", d3) withID id3 withResources Seq("r1")

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
 
