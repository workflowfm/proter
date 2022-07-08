package com.workflowfm.proter
package cases

import cases.given
import events.*
import schedule.GreedyScheduler
import state.Simulationx
import state.Simulationx.SimState

import cats.{ Monad, MonadError }
import cats.implicits.*
import cats.effect.IO
import cats.effect.implicits.*
import cats.effect.kernel.Ref
import cats.effect.std.{ Random, UUIDGen }
import cats.effect.testing.scalatest.AsyncIOSpec

import java.util.UUID

import scala.concurrent.*
import scala.util.{ Failure, Success, Try }

import org.scalatest.Inside
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AsyncWordSpec
import org.scalatest.{ Assertions, LoneElement }


class CaseTests extends CaseTester {

  "Cases" should {

    "interact correctly having no tasks" in {
      val c1 = new NoTasks[IO]("c1")
      val init = Simulationx[IO]("Test", GreedyScheduler(true))
      val sim = init.copy(
          waiting = init.waiting + ("c1" -> Seq()),
          cases = init.cases + ("c1" -> c1)
        )
      val correct = init

      for {
        state <- c1.run()
        result <- state.run(sim)
        (ret, events) = result
        _ = ret `should` be (correct)
        _ = events `should` be (Seq(ECaseEnd("Test", 0, "c1", "()")))
      } yield ()
    }

    "interact correctly having one task" in {
      val task = Task("OneTask", 2L)
        .withPriority(10)
        .withCost(6)
        .withResources(Seq("R"))
      
      val init = Simulationx[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random 

        t1id <- UUIDGen[IO].randomUUID
        t1 = task.withID(t1id)
        ti1 <- t1.create[IO]("Case", 0L)

        ref <- summon[Case[IO, Task]].init("Case", t1)
       
        sim1 = init.copy(
          waiting = init.waiting + ("Case" -> Seq()),
          cases = init.cases + ("Case" -> ref)
        )
        s1 <- ref.run()
        r1 <- s1.run(sim1)
        (ret1, e1) = r1
        _ = ret1 `should` be (sim1.copy(
          tasks = sim1.tasks + ti1
        ))
        _ = e1 `should` be (Seq(ETaskAdd("Test", 0, ti1)))

        sim2 = ret1.copy(time = ti1.duration)
        s2 <- ref.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2
        _ = ret2 `should` be (sim2.copy(
          waiting = Map(),
          cases = Map()
        ))
        _ = e2 `should` be (Seq(ECaseEnd("Test", ti1.duration, "Case", (ti1, ti1.duration).toString())))
      } yield ()
    }

    "interact correctly having 2 tasks in sequence" in {
      val init = Simulationx[IO]("Test", GreedyScheduler(true))

      for {
        random <- Random.scalaUtilRandom[IO]
        given Random[IO] = random 

        ref <- twoTasks("Case")
            
        ti1 <- ref.t1.create[IO]("Case", 0L)(using random, Monad[IO])
        ti2 <- ref.t2.create[IO]("Case", 2L)(using random, Monad[IO])

        sim1 = init.copy(
          waiting = init.waiting + ("Case" -> Seq()),
          cases = init.cases + ("Case" -> ref)
        )
        s1 <- ref.run()
        r1 <- s1.run(sim1)
        (ret1, e1) = r1
        ref1 = ret1.cases.get("Case").getOrElse(ref)
        correct1 = sim1.copy(
          tasks = sim1.tasks + ti1,
          cases = sim1.cases + ("Case" -> ref1)
        )
        _ = ret1 `should` be (correct1)
        _ = e1 `should` be (Seq(ETaskAdd("Test", 0, ti1)))

        sim2 = correct1.copy(time = ti1.duration)
        s2 <- ref1.completed(ti1.duration, Seq(ti1))
        r2 <- s2.run(sim2)
        (ret2, e2) = r2
        ref2 = ret2.cases.get("Case").getOrElse(ref1)
        correct2 = sim2.copy(
          tasks = sim2.tasks + ti2,
          cases = sim2.cases + ("Case" -> ref2)
        )
        _ = ret2 `should` be (correct2)
        _ = e2 `should` be (Seq(ETaskAdd("Test", ti1.duration, ti2)))

        sim3 = correct2.copy(time = ti1.duration + ti2.duration)
        s3 <- ref2.completed(ti2.duration, Seq(ti2))
        r3 <- s3.run(sim3)
        (ret3, e3) = r3
        correct3 = sim3.copy(
          waiting = Map(),
          cases = Map()
        )
        _ = ret3 `should` be (correct3)
        _ = e3 `should` be (Seq(ECaseEnd("Test", ti1.duration + ti2.duration, "Case", ().toString())))
/*
 Simulationx(Test,GreedyScheduler(true),4,EventQueue(TreeMap()),Set(Task(ba63c236-3277-460c-b9c8-44f8f8a0a31d,task1,Case,0,[r1],d2(2),c0.0,i-1,0), Task(908c2e9a-70f4-47d4-a5b3-9e1ee921ba6e,task2,Case,2,[r1],d2(2),c0.0,i-1,0)),Map(Case -> TwoTasks(Case,Task(task1,Some(ba63c236-3277-460c-b9c8-44f8f8a0a31d),ConstantLong(2),Constant(0.0),0,List(r1),-1,0,-1),Task(task2,Some(908c2e9a-70f4-47d4-a5b3-9e1ee921ba6e),ConstantLong(2),Constant(0.0),0,List(r1),-1,0,-1),CallbackMap(Map()))),ResourceMap(Map()),Map(Case -> List()),HashSet()) was not equal to 
 
 Simulationx(Test,GreedyScheduler(true),4,EventQueue(TreeMap()),Set(Task(ba63c236-3277-460c-b9c8-44f8f8a0a31d,task1,Case,0,[r1],d2(2),c0.0,i-1,0), Task(908c2e9a-70f4-47d4-a5b3-9e1ee921ba6e,task2,Case,2,[r1],d2(2),c0.0,i-1,0)),Map(),ResourceMap(Map()),Map(),HashSet())



*/
      } yield ()
    }
/*
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
          r.caseName `should` be (c1.caseName)
          tasks.loneElement `should` be (t1)
          abort `shouldBe` empty
        } } )

        r2 <- c1.completed(2L, Seq(ti1))
        _ <- IO (inside(r2) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName `should` be (c1.caseName)
          tasks.loneElement `should` be (t2)
          abort `shouldBe` empty
        } } )

        r3 <- c1.completed(4L, Seq(ti2))
        _ <- IO (inside(r3) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName `should` be (c1.caseName)
          tasks.loneElement `should` be (t3)
          abort `shouldBe` empty
        } } )

        r4 <- c1.completed(6L, Seq(ti3))
        _ <- IO ( inside(r4) { case CaseDone(r, result) => {
          r.caseName `should` be (c1.caseName)
          result `should` be (Success((ti3, 6L)))
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
          r.caseName `should` be (c1.caseName)
          tasks.loneElement `should` be (t1)
          abort `shouldBe` empty
        } } )

        r2 <- c1.completed(2L, Seq(ti1))
        _ <- IO (inside(r2) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName `should` be (c1.caseName)
          tasks.loneElement `should` be (t2)
          abort `shouldBe` empty
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
          r.caseName `should` be (c1.caseName)
          tasks.loneElement `should` be (t1)
          abort `shouldBe` empty
        } } )

        r2 <- c1.completed(2L, Seq(ti1))
        _ <- IO (inside(r2) { case CaseReady(r, tasks, abort, _ ) => {
          r.caseName `should` be (c1.caseName)
          tasks.size `should` be (2)
          tasks `should` contain (t2)
          tasks `should` contain (t3)
          abort `shouldBe` empty
        } } )

        u <- c1.stop().assertThrows[CallbackCalledException.type]
      } yield (u)
    }


*/
  }
}

trait CaseTester extends AsyncWordSpec with AsyncIOSpec with Matchers with Inside with LoneElement {

  class NoTasks[F[_]](override val caseName: String)(using F: MonadError[F, Throwable]) extends CaseRef[F] {
    override def run(): F[SimState[F]] = F.pure(succeed(())) // finish instantly
    override def completed(time: Long, tasks: Seq[TaskInstance]): F[SimState[F]] = 
      F.raiseError(new Exception(s"Test Case $caseName with no tasks received call to `completed`: $time - $tasks"))
    override def stop(): F[Unit] = F.pure(())
  }

  case class TwoTasks(
    override val caseName: String, 
    t1: Task, 
    t2: Task,
    callbackMap: CallbackMap[IO]
  )(using Random[IO]) extends AsyncCaseRef[IO](callbackMap) {

    override def updateState(callbackUpdate: CallbackMap[IO]): AsyncCaseRef[IO] =
      copy(callbackMap = callbackUpdate)

    override def stop(): IO[Unit] = IO.pure(())

    override def run(): IO[SimState[IO]] = compose(task(t1, t1callback))

    val t1callback: Callback = {
      case Success((t, _)) => {
        task(t2, t2callback)
      }
      case Failure(ex) => Assertions.fail("Failure on t1 callback")
    }

    val t2callback: Callback = {
      case Success((task, time)) => {
        succeedState(())
      }
      case Failure(ex) => Assertions.fail("Failure on t2 callback")
    }

  }

  def twoTasks(
    name: String,
    d1: Long = 2L,
    d2: Long = 2L
  )(using Random[IO]): IO[TwoTasks] = for {

    id1 <- UUIDGen[IO].randomUUID
    id2 <- UUIDGen[IO].randomUUID

    t1: Task = Task("task1", d1) `withID` id1 `withResources` Seq("r1")
    t2: Task = Task("task2", d2) `withID` id2 `withResources` Seq("r1")
/*
    myCase = new AsyncCaseRef[IO](CallbackMap(Map())) {
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
    }*/
  } yield (TwoTasks(name, t1, t2, CallbackMap(Map())))
/*
  def twoTasksStopping(
    name: String,
    d1: Long = 2L,
    d2: Long = 2L
  ): IO[(Task, Task, AsyncCaseRef[IO])] = for {

    id1 <- UUIDGen[IO].randomUUID
    id2 <- UUIDGen[IO].randomUUID

    t1: Task = Task("task1", d1) `withID` id1 `withResources` Seq("r1")
    t2: Task = Task("task2", d2) `withID` id2 `withResources` Seq("r1")

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
          ex `shouldBe` a [Simulation.SimulationStoppingException]
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

    t1: Task = Task("task1", d1) `withID` id1 `withResources` Seq("r1")
    t2: Task = Task("task2", d2) `withID` id2 `withResources` Seq("r1")
    t3: Task = Task("task3", d3) `withID` id3 `withResources` Seq("r1")

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

    t1: Task = Task("task1", d1) `withID` id1 `withResources` Seq("r1")
    t2: Task = Task("task2", d2) `withID` id2 `withResources` Seq("r1")
    t3: Task = Task("task3", d3) `withID` id3 `withResources` Seq("r1")

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
          ex `shouldBe` a [Simulation.SimulationStoppingException]
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

  case object CallbackCalledException extends Exception("Callback called!")*/
}




