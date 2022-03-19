package com.workflowfm.proter
package cases

import cats.{ Monad, MonadError }
import cats.implicits._
import cats.effect.IO
import cats.effect.implicits._
import cats.effect.kernel.Ref
import cats.effect.std.{ Random, UUIDGen }
import cats.effect.testing.scalatest.AsyncIOSpec

import java.util.UUID

import scala.concurrent._
import scala.util.{ Failure, Success, Try }

import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatest.{ Assertions, LoneElement }


class CaseTests extends CaseTester {

  "Cases" should {

    "interact correctly having no tasks" in {
      val c1 = new NoTasks[IO]("c1")
      for {
        result <- c1.run()
      } yield (result should be (CaseDone(c1, Success(()))))
    }

    "interact correctly having one task" in {
      val task = Task("OneTask", 2L)
        .withPriority(10)
        .withCost(6)
        .withResources(Seq("R"))

      for {
        random <- Random.scalaUtilRandom[IO]
        t1id <- UUIDGen[IO].randomUUID
        t1 = task.withID(t1id)

        ref <- summon[Case[IO, Task]].init("Case", t1)

        r1 <- ref.run()
        _ <- IO (inside(r1) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (ref.caseName)
          tasks.loneElement should be (t1)
          abort shouldBe empty
        } } )

        ti1 <- t1.create[IO]("Case", 0L)(using random, Monad[IO])
        r2 <- ref.completed(ti1.duration, Seq(ti1))
        _ <- IO ( inside(r2) { case CaseDone(r, result) => {
          r.caseName should be (ref.caseName)
          result should be (Success((ti1, ti1.duration)))
        } } )
      } yield ()
    }

    "interact correctly having 2 tasks in sequence" in {
      for {
        x <- twoTasks("Case")
        (t1, t2, c1) = x
        random <- Random.scalaUtilRandom[IO]

        ti1 <- t1.create[IO]("Case", 0L)(using random, Monad[IO])
        ti2 <- t2.create[IO]("Case", 2L)(using random, Monad[IO])

        r1 <- c1.run()
        _ <- IO (inside(r1) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (c1.caseName)
          tasks.loneElement should be (t1)
          abort shouldBe empty
        } } )

        r2 <- c1.completed(2L, Seq(ti1))
        _ <- IO (inside(r2) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (c1.caseName)
          tasks.loneElement should be (t2)
          abort shouldBe empty
        } } )

        r3 <- c1.completed(4L, Seq(ti2))
        _ <- IO ( inside(r3) { case CaseDone(r, result) => {
          r.caseName should be (c1.caseName)
          result should be (Success((ti2, 4L)))
        } } )

      } yield ()
    }

    "interact correctly having 3 tasks in sequence" in {
      for {
        x <- threeTasks("Case")
        (t1, t2, t3, c1) = x
        random <- Random.scalaUtilRandom[IO]

        ti1 <- t1.create[IO]("Case", 0L)(using random, Monad[IO])
        ti2 <- t2.create[IO]("Case", 2L)(using random, Monad[IO])
        ti3 <- t3.create[IO]("Case", 4L)(using random, Monad[IO])

        r1 <- c1.run()
        _ <- IO (inside(r1) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (c1.caseName)
          tasks.loneElement should be (t1)
          abort shouldBe empty
        } } )

        r2 <- c1.completed(2L, Seq(ti1))
        _ <- IO (inside(r2) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (c1.caseName)
          tasks.loneElement should be (t2)
          abort shouldBe empty
        } } )

        r3 <- c1.completed(4L, Seq(ti2))
        _ <- IO (inside(r3) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (c1.caseName)
          tasks.loneElement should be (t3)
          abort shouldBe empty
        } } )

        r4 <- c1.completed(6L, Seq(ti3))
        _ <- IO ( inside(r4) { case CaseDone(r, result) => {
          r.caseName should be (c1.caseName)
          result should be (Success((ti3, 6L)))
        } } )

      } yield ()
    }

    "stop between 2 tasks in sequence" in {
      for {
        x <- twoTasksStopping("Case")
        (t1, t2, c1) = x
        random <- Random.scalaUtilRandom[IO]

        ti1 <- t1.create[IO]("Case", 0L)(using random, Monad[IO])
        ti2 <- t2.create[IO]("Case", 2L)(using random, Monad[IO])

        r1 <- c1.run()
        _ <- IO (inside(r1) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (c1.caseName)
          tasks.loneElement should be (t1)
          abort shouldBe empty
        } } )

        r2 <- c1.completed(2L, Seq(ti1))
        _ <- IO (inside(r2) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (c1.caseName)
          tasks.loneElement should be (t2)
          abort shouldBe empty
        } } )

        u <- c1.stop().assertThrows[CallbackCalledException.type]
      } yield (u)
    }

    "stop between 1+2 tasks in an AsyncCase" in {
      for {
        x <- threeTasksStoppingCase(2L, 2L, 2L)
        (t1, t2, t3, c) = x
        c1 <- c.init("Case", ())
        random <- Random.scalaUtilRandom[IO]

        ti1 <- t1.create[IO]("Case", 0L)(using random, Monad[IO])
        ti2 <- t2.create[IO]("Case", 2L)(using random, Monad[IO])

        r1 <- c1.run()
        _ <- IO (inside(r1) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (c1.caseName)
          tasks.loneElement should be (t1)
          abort shouldBe empty
        } } )

        r2 <- c1.completed(2L, Seq(ti1))
        _ <- IO (inside(r2) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName should be (c1.caseName)
          tasks.size should be (2)
          tasks should contain (t2)
          tasks should contain (t3)
          abort shouldBe empty
        } } )

        u <- c1.stop().assertThrows[CallbackCalledException.type]
      } yield (u)
    }



  }
}

trait CaseTester extends AsyncWordSpec with AsyncIOSpec with Matchers with Inside with LoneElement {

  class NoTasks[F[_]](override val caseName: String)(using F: MonadError[F, Throwable]) extends CaseRef[F] {
    override def run(): F[CaseResponse] = F.pure(succeed(())) // finish instantly
    override def completed(time: Long, tasks: Seq[TaskInstance]): F[CaseResponse] = 
      F.raiseError(new Exception(s"Test Case $caseName with no tasks received call to `completed`: $time - $tasks"))
    override def stop(): F[Unit] = F.pure(())
  }

  def twoTasks(
    name: String,
    d1: Long = 2L,
    d2: Long = 2L
  ): IO[(Task, Task, AsyncCaseRef[IO])] = for {

    id1 <- UUIDGen[IO].randomUUID
    id2 <- UUIDGen[IO].randomUUID

    t1: Task = Task("task1", d1) withID id1 withResources Seq("r1")
    t2: Task = Task("task2", d2) withID id2 withResources Seq("r1")

    state <- Ref[IO].of[Map[UUID, AsyncCaseRef.Callback[IO]]](Map()) 

    myCase = new AsyncCaseRef[IO](state) {
      val caseName: String = name

      val t1callback: Callback = {
        case Success((t, _)) => {
          task(t2, t2callback).map(r => Seq(r))
        }
        case Failure(ex) => Assertions.fail("Failure on t1 callback")
      }

      val t2callback: Callback = {
        case Success((task, time)) => {
          IO.pure(Seq(done(Success(task, time))))
        }
        case Failure(ex) => Assertions.fail("Failure on t2 callback")
      }

      override def run(): IO[CaseResponse] = task(t1, t1callback)
    }
  } yield ((t1, t2, myCase))

  def twoTasksStopping(
    name: String,
    d1: Long = 2L,
    d2: Long = 2L
  ): IO[(Task, Task, AsyncCaseRef[IO])] = for {

    id1 <- UUIDGen[IO].randomUUID
    id2 <- UUIDGen[IO].randomUUID

    t1: Task = Task("task1", d1) withID id1 withResources Seq("r1")
    t2: Task = Task("task2", d2) withID id2 withResources Seq("r1")

    state <- Ref[IO].of[Map[UUID, AsyncCaseRef.Callback[IO]]](Map()) 

    myCase = new AsyncCaseRef[IO](state) {
      val caseName: String = name

      val t1callback: Callback = {
        case Success((t, _)) => {
          task(t2, t2callback).map(r => Seq(r))
        }
        case Failure(ex) => Assertions.fail("Failure on t1 callback")
      }

      val t2callback: Callback = {
        case Success((task, time)) => Assertions.fail("Unexpected success on stopped t2 callback")
        case Failure(ex) => {
          ex shouldBe a [Simulation.SimulationStoppingException]
          IO.raiseError(CallbackCalledException)
        }
      }

      override def run(): IO[CaseResponse] = task(t1, t1callback)
    }
  } yield ((t1, t2, myCase))

  def threeTasks(
    name: String,
    d1: Long = 2L,
    d2: Long = 2L,
    d3: Long = 3L
  ): IO[(Task, Task, Task, AsyncCaseRef[IO])] = for {

    id1 <- UUIDGen[IO].randomUUID
    id2 <- UUIDGen[IO].randomUUID
    id3 <- UUIDGen[IO].randomUUID

    t1: Task = Task("task1", d1) withID id1 withResources Seq("r1")
    t2: Task = Task("task2", d2) withID id2 withResources Seq("r1")
    t3: Task = Task("task3", d3) withID id3 withResources Seq("r1")

    state <- Ref[IO].of[Map[UUID, AsyncCaseRef.Callback[IO]]](Map())

    myCase = new AsyncCaseRef[IO](state) {
      val caseName: String = name

      val t1callback: Callback = {
        case Success((t, _)) => {
          task(t2, t2callback).map(r => Seq(r))
        }
        case Failure(ex) => Assertions.fail("Failure on t1 callback")
      }

      val t2callback: Callback = {
        case Success((t, _)) => {
          task(t3, t3callback).map(r => Seq(r))
        }
        case Failure(ex) => Assertions.fail("Failure on t2 callback")
      }

      val t3callback: Callback = {
        case Success((task, time)) => {
          IO.pure(Seq(done(Success(task, time))))
        }
        case Failure(ex) => Assertions.fail("Failure on t3 callback")
      }

      override def run(): IO[CaseResponse] = task(t1, t1callback)
    }
  } yield ((t1, t2, t3, myCase))


  def threeTasksStoppingCase(
    d1: Long = 2L,
    d2: Long = 2L,
    d3: Long = 3L
  ): IO[(Task, Task, Task, AsyncCase[IO, Unit])] = for {

    id1 <- UUIDGen[IO].randomUUID
    id2 <- UUIDGen[IO].randomUUID
    id3 <- UUIDGen[IO].randomUUID

    t1: Task = Task("task1", d1) withID id1 withResources Seq("r1")
    t2: Task = Task("task2", d2) withID id2 withResources Seq("r1")
    t3: Task = Task("task3", d3) withID id3 withResources Seq("r1")

    myCase = new AsyncCase[IO, Unit] {

      def t1callback(cs: AsyncCaseRef[IO]): Callback = {
        case Success((t, _)) => Seq(
          cs.task(t2, t2callback),
          cs.task(t3, t2callback)
        ).sequence
        case Failure(ex) => Assertions.fail("Failure on t1 callback")
      }

      val t2callback: Callback = {
        case Success((task, time)) => Assertions.fail("Unexpected success on stopped t2 callback")
        case Failure(ex) => {
          ex shouldBe a [Simulation.SimulationStoppingException]
          IO.raiseError(CallbackCalledException)
        }
      }

      override def run(cs: AsyncCaseRef[IO], t: Unit): IO[CaseResponse] = cs.task(t1, t1callback(cs))
    }
  } yield ((t1, t2, t3, myCase))

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

  case object CallbackCalledException extends Exception("Callback called!")
}




