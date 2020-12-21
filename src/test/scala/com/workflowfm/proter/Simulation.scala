package com.workflowfm.proter

import java.util.UUID

import scala.concurrent._
import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.testkit.{ ImplicitSender, TestActors, TestKit, TestProbe }
import com.typesafe.config.ConfigFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }

import uk.ac.ed.inf.ppapapan.subakka.MockPublisher

class SimulationTests extends SimulationTester {
  implicit val executionContext: ExecutionContextExecutor = ExecutionContext.global
  implicit val timeout: FiniteDuration = 10.seconds

  "Simulations" must {

    "interact correctly having  no tasks" in {
      val sim = system.actorOf(Props(new NoTasks("sim", self)))

      sim ! Simulation.Start
      expectMsg(Coordinator.SimStarted("sim", sim))
      expectMsg(Coordinator.SimDone("sim", Success(Unit)))
      expectNoMsg()
    }

    "interact correctly having one task" in {
      val sim =
        system.actorOf(SingleTaskSimulation.props("sim", self, Seq("r1"), ConstantGenerator(2L)))

      sim ! Simulation.Start
      expectMsg(Coordinator.SimStarted("sim", sim))
      val Coordinator.AddTask(generator) = expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.SimReady(None))

      val task = generator.create(0L, sim)
      sim ! Simulation.TaskCompleted(task, 2L)
      val Coordinator.SimDone(name, _) = expectMsgType[Coordinator.SimDone]
      name should be("sim")
      expectNoMsg()
    }


    "interact correctly having 2 tasks in sequence with callback" in {
      val sim = system.actorOf(Props(new TwoTasks("sim", self)))

      sim ! Simulation.Start
      expectMsg(Coordinator.SimStarted("sim", sim))

      //task1
      val Coordinator.AddTask(generator1) = expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.SimReady(None))
      val task1 = generator1.create(0L, sim)
      sim ! Simulation.TaskCompleted(task1, 2L)

      //task2
      val Coordinator.AddTask(generator2) = expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.AckTasks(Seq(generator1.id)))
      val task2 = generator2.create(2L, sim)
      sim ! Simulation.TaskCompleted(task2, 4L)

      //expectMsg( Coordinator.AckTasks(Seq(id3)))
      val Coordinator.SimDone(name, _) = expectMsgType[Coordinator.SimDone]
      name should be("sim")
      expectNoMsg()

    }


    "interact correctly having 3 tasks in sequence with Futures" in {
      val sim = system.actorOf(Props(new ThreeFutureTasks("sim", self)))

      sim ! Simulation.Start
      expectMsg(Coordinator.SimStarted("sim", sim))

      //task1
      val Coordinator.AddTask(generator1) = expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.SimReady(None))
      val task1 = generator1.create(0L, sim)
      sim ! Simulation.TaskCompleted(task1, 2L)

      //task2
      val Coordinator.AddTask(generator2) = expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.AckTasks(Seq(generator1.id)))
      val task2 = generator2.create(2L, sim)
      sim ! Simulation.TaskCompleted(task2, 4L)

      //task3
      val Coordinator.AddTask(generator3) = expectMsgType[Coordinator.AddTask]
      expectMsg(Coordinator.AckTasks(Seq(generator2.id)))
      val task3 = generator3.create(2L, sim)
      sim ! Simulation.TaskCompleted(task3, 6L)

      //expectMsg( Coordinator.AckTasks(Seq(id3)))
      val Coordinator.SimDone(name, _) = expectMsgType[Coordinator.SimDone]
      name should be("sim")
      expectNoMsg()

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
    extends TestKit(
      ActorSystem("SimulationTests", ConfigFactory.parseString(MockPublisher.config))
    )
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ImplicitSender {

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  class NoTasks(name: String, coordinator: ActorRef)
      extends Simulation(name, coordinator) {
    override def run(): Unit = succeed(Unit) //finish instantly
    override def complete(task: Task, time: Long): Unit =  Unit //does nothing
    override def stop(): Unit = Unit
  }

  class ThreeFutureTasks(name: String, coordinator: ActorRef, d1: Long = 2L, d2: Long = 2L, d3: Long = 3L)(
      implicit executionContext: ExecutionContext
  ) extends AsyncSimulation(name, coordinator)
      with FutureTasks {

    override def run(): Unit = {
      val id1 = UUID.randomUUID
      val id2 = UUID.randomUUID
      val id3 = UUID.randomUUID

      val task1 = futureTask(
        TaskGenerator(
          "task1",
          id1,
          "sim",
          ConstantGenerator(d1),
          ConstantGenerator(0L),
          0L,
          Seq("r1")
        )
      )
      ready()
      val task2 = task1 flatMap { _ =>
        val t = futureTask(
          TaskGenerator(
            "task2",
            id2,
            "sim",
            ConstantGenerator(d2),
            ConstantGenerator(0L),
            0L,
            Seq("r1")
          )
        )
        ack(Seq(id1))
        t
      }
      val task3 = task2 flatMap { _ =>
        val t = futureTask(
          TaskGenerator(
            "task3",
            id3,
            "sim",
            ConstantGenerator(d3),
            ConstantGenerator(0L),
            0L,
            Seq("r1")
          )
        )
        ack(Seq(id2))
        t
      }
      task3.onComplete(done)
    }
  }

 class TwoTasks(name: String, coordinator: ActorRef, d1: Long = 2L, d2: Long = 2L) extends AsyncSimulation(name, coordinator)
      with FutureTasks {

   val id1 = UUID.randomUUID
   val id2 = UUID.randomUUID

   val generator1 = TaskGenerator(
     "task1",
     id1,
     "sim",
     ConstantGenerator(d1),
     ConstantGenerator(0L),
     0L,
     Seq("r1")
   )

   val generator2 = TaskGenerator(
     "task2",
     id2,
     "sim",
     ConstantGenerator(d2),
     ConstantGenerator(0L),
     0L,
     Seq("r1")
   )

   val callback: Callback = callback { case (t, _) => {
     task(generator2, r => done(r))
     ack(Seq(t.id))
   }}


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
