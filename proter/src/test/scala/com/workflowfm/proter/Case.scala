package com.workflowfm.proter

import cats.MonadError
import cats.implicits._
import cats.effect.IO
import cats.effect.testing.scalatest.AsyncIOSpec

import java.util.UUID

import scala.concurrent._
import scala.util.{ Failure, Success }

import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec

class CaseTests extends CaseTester {

  import MockManager._
  given ExecutionContextExecutor = ExecutionContext.global

  "Cases" should {

    "interact correctly having no tasks" in {
      val c1 = new NoTasks[IO]("c1")
      for {
        result <- c1.run()
      } yield (result should be (CaseDone(c1, Success(()))))
    }

//    "interact correctly having one task" in {
//      val sim = new SingleTaskSimulation("sim", manager, Seq("r1"), Constant(2L))
//
//      val ti1 = sim.theTask.create("sim", 0L)
//
//      manager.reactions += sim.theTask -> Complete(sim, 2L, Seq(ti1))
//      manager.expected ++= Seq(
//        SimReady("sim", Seq(sim.theTask)),
//        SimDone("sim", Success((ti1, 2L)))
//      )
//
//      sim.run()
//      manager should comply
//    }

  }
}

trait CaseTester extends AsyncWordSpec with AsyncIOSpec with Matchers {

  class NoTasks[F[_]](override val caseName: String)(using F: MonadError[F, Throwable]) extends CaseRef[F,NoTasks[F]] {
    override def run(): F[CaseResponse] = F.pure(succeed(())) // finish instantly
    override def completed(time: Long, tasks: Seq[TaskInstance]): F[CaseResponse] = 
      F.raiseError(new Exception(s"Test Case $caseName with no tasks received call to `completed`: $time - $tasks"))
    override def stop(): F[Unit] = F.pure(())
  }
/*
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
 */
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

