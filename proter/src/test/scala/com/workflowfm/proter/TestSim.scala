package com.workflowfm.proter

import cases.*
import cases.given
import events.*
import flows.*
import flows.given
import metrics.*
import schedule.*

import cats.effect.{ IO, IOApp, ExitCode }
import cats.effect.std.Random

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
        // PrintEvents(),
        PrintJsonEvents(),
//        PrintEvents(),
        MetricsSubscriber[IO](
          MetricsPrinter()
          // CSVFile("output/", "HAHA"),
          // D3Timeline("output/", "HAHA")
        )
      )
      val scenario = Scenario[IO]("MYSCENARIO")
        .withStartingTime(14)
        .withResources(
          Seq(
            Resource("A", 2, 1)
          )
        )
        // .withCases(
        //  ("foo1", Task("t", 1)),
        //  ("foo2", Task("t", 2))
        //  ("foo3", Task("t", 3)),
        //  ("foo4", Task("t", 4)),
        //  ("foo4.2", Task("t", 4)),
        // )
        // .withCases(
        //  ("foo1", Task("t", 1).withCost(10).withResources(Seq("A"))),
        //  ("foo2", Task("t", 2).withCost(10).withResources(Seq("A"))),
        // ("foo3", Task("t", 3).withResources(Seq("A"))),
        // ("foo4", Task("t", 4).withResourceQuantities(Seq(("A", 2)).toMap)),
        // ("foo4.2", Task("t", 4).withResourceQuantities(Seq(("A", 2)).toMap)),
        // )
        .withTimedCases(
          (14, "foo1", Task("t", 10).withCost(10).withResourceQuantities(Seq(("A", 1)).toMap)),
          (16, "foo2", Task("t", 2).withCost(10).withResourceQuantities(Seq(("A", 1)).toMap))
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
        PrintEvents()
      )

      val scenario = Scenario[IO]("Scenario 1")
        .withStartingTime(1)
        .withCase("foo1", Task("t1", 1))

      simulator
        .stream(scenario)
        .evalTap(x => IO.println(s">>>>> $x"))
        .compile
        .drain
        .as(ExitCode.Success)
    }

}


