package com.workflowfm.proter

import events.*
import schedule.*
import state.*

import fs2.Stream

import cats.{ Monad, Applicative }
import cats.data.{ State, StateT }
import cats.effect.Concurrent
import cats.effect.std.Random
import cats.implicits.*
import cats.effect.implicits.*

final case class Simulator[F[_] : Concurrent](
    scheduler: Scheduler,
    subscribers: Seq[Subscriber[F]]
) extends ScenarioState {

  def withScheduler(s: Scheduler): Simulator[F] = copy(scheduler = s)

  def withSub(sub: Subscriber[F]): Simulator[F] = copy(subscribers = Seq(sub))
  def withSubs(subs: Subscriber[F]*): Simulator[F] = copy(subscribers = subscribers ++ subs)
  def and(sub: Subscriber[F]): Simulator[F] = copy(subscribers = subscribers :+ sub)

  def simulate(scenario: Scenario[F]): F[Unit] =
    simulateState(scenario.name, scenario.state)

  def stream(scenario: Scenario[F]): Stream[F, Either[Throwable, Event]] =
    streamState(scenario.name, scenario.state)


  def simulateState(name: String, state: StateT[F, Simulation[F], Seq[Event]]): F[Unit] = for {
    publisher <- Publisher.build[F]()
    subresource = subscribers.map(publisher.subscribe(_)).parSequence
    _ <- subresource.use { subs =>
      {
        val subio = subs.map(_.compile.drain).parSequence
        val pubio = simulateStateWithPublisher(name, state, publisher)
        (pubio, subio).parTupled
      }
    }
  } yield (())

  def streamState(name: String, state: StateT[F, Simulation[F], Seq[Event]]): Stream[F, Either[Throwable, Event]] = Stream.eval(for {
    publisher <- Publisher.build[F]()
    subresource = subscribers.map(publisher.subscribe(_)).parSequence
    result = Stream.eval(subresource.use { subs =>
      {
        val subio = subs.map(_.compile.drain).parSequence
        val pubio = simulateStateWithPublisher(name, state, publisher)
        (pubio, subio).parTupled
      }
    })
    stream = publisher.stream.concurrently(result)
  } yield (stream)).flatten

  def simulateStateWithPublisher(
      name: String,
      state: StateT[F, Simulation[F], Seq[Event]],
      publisher: Publisher[F]
  ): F[Unit] = {
    val sim = Simulation[F](name, scheduler)
    for {
      sResult <- sim.start(state)
      (updated, events) = sResult
      _ = println("hi")
      _ <- events.map(publisher.publish(_)).sequence
      x <- updated.tailRecM(simRec(publisher))
    } yield (x)
  }

  protected def simRec(publisher: Publisher[F]): Simulation[F] => F[Either[Simulation[F], Unit]] =
    sim =>
      for {
        tResult <- sim.tick
        (ticked, tEvents) = tResult
        _ <- tEvents.map(publisher.publish(_)).sequence
      } yield (ticked)

}

object Simulator {
  def apply[F[_] : Concurrent](): Simulator[F] = Simulator(ProterScheduler, Seq())
  def apply[F[_] : Concurrent](scheduler: Scheduler): Simulator[F] = Simulator(scheduler, Seq())

}

import cases.*
import cases.given
import flows.*
import flows.given
import metrics.*

import cats.effect.{ IO, IOApp, ExitCode }

object TestSim extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    runScenarioStream()

  def runState(): IO[ExitCode] =
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simulator = Simulator[IO](ProterScheduler) withSub (PrintEvents())
      val cases = simulator.addCasesNow[IO, Task](
        Seq(
          ("foo1", Task("t", 1)),
          ("foo2", Task("t", 2)),
          ("foo3", Task("t", 3)),
          ("foo4", Task("t", 4)),
          ("foo4.2", Task("t", 4))
        )
      )
      simulator.simulateState("X", cases).as(ExitCode(1))
    }

  def runScenario(): IO[ExitCode] =
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simulator = Simulator[IO](ProterScheduler) withSubs (
        //PrintEvents(),
        PrintJsonEvents(),
//        PrintEvents(),
        MetricsSubscriber[IO](
          MetricsPrinter(),
          //CSVFile("output/", "HAHA"),
          //D3Timeline("output/", "HAHA")
          )
      )
      val scenario = Scenario[IO]("MYSCENARIO")
        .withStartingTime(14)
        .withResources(Seq(
          Resource("A", 2, 1)
        ))
        //.withCases(
        //  ("foo1", Task("t", 1)),
        //  ("foo2", Task("t", 2))
        //  ("foo3", Task("t", 3)),
        //  ("foo4", Task("t", 4)),
        //  ("foo4.2", Task("t", 4)),
        //)
        //.withCases(
        //  ("foo1", Task("t", 1).withCost(10).withResources(Seq("A"))),
        //  ("foo2", Task("t", 2).withCost(10).withResources(Seq("A"))),
          //("foo3", Task("t", 3).withResources(Seq("A"))),
          //("foo4", Task("t", 4).withResourceQuantities(Seq(("A", 2)).toMap)),
          //("foo4.2", Task("t", 4).withResourceQuantities(Seq(("A", 2)).toMap)),
        //)
        .withTimedCases(
          (14, "foo1", Task("t", 10).withCost(10).withResourceQuantities(Seq(("A", 1)).toMap)),
          (16, "foo2", Task("t", 2).withCost(10).withResourceQuantities(Seq(("A", 1)).toMap)),
        )

         //      .withCases(
         //        ("flowsingle", FlowTask(Task("f1", 1)): Flow),
        //         ("flow1", Flow(Task("f1", 1), Task("f2", 1))),
        // ("flow2", Flow.par(Seq(Task("f2a", 1), Task("f2b", 2), Task("f2c", 3)).map(FlowTask(_)))) 
        //           ("flowAnd", And(FlowTask(Task("f1", 1)), FlowTask(Task("f2", 2))): Flow),
        //       )
        //       .withArrival("A1", Task("t", 1), ConstantLong(2), 10)
        //       .withTimedArrival("A2", 5, Task("t", 1), ConstantLong(2), 1)
        //       .withInfiniteArrival("A3", Task("t", 2), ConstantLong(5))
        .withLimit(25)

      simulator.simulate(scenario).as(ExitCode(1))
    }

  def run2Scenarios(): IO[ExitCode] =  
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      for {
        counter <- CountEvents[IO]()
        simulator = Simulator[IO](ProterScheduler) withSubs (
          PrintEvents(),
          counter
        )
        
        scenario1 = Scenario[IO]("Scenario 1")
          .withStartingTime(1)
          .withCase("foo1", Task("t1", 1))
        scenario2 = Scenario[IO]("Scenario 2")
          .withStartingTime(20)
          .withCase("foo2", Task("2", 1))

        _ <- simulator.simulate(scenario1)
        _ <- simulator.simulate(scenario2)
        count <- counter.get()
        _ <- IO.println(s"*** Final count: $count")
      } yield (ExitCode.Success)
    }

  def runScenarioStream(): IO[ExitCode] =  
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simulator = Simulator[IO](ProterScheduler) withSubs (
        PrintEvents(),
      )

      val scenario = Scenario[IO]("Scenario 1")
        .withStartingTime(1)
        .withCase("foo1", Task("t1", 1))

      simulator.stream(scenario).evalTap(x => IO.println(s">>>>> $x")).compile.drain.as(ExitCode.Success)
    }

}
