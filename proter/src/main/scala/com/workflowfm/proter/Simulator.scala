package com.workflowfm.proter

import cases.*
import cases.given_Case_F_Task
import events.*
import schedule.*
import state.*

import cats.{ Monad, Applicative }
import cats.data.{ State, StateT }
import cats.effect.Concurrent
import cats.effect.std.Random
import cats.implicits.*
import cats.effect.implicits.*


final case class Simulator[F[_] : Concurrent](scheduler: Scheduler, eventHandlers: Seq[EventHandler[F]]) extends CaseState {

  def withScheduler(s: Scheduler): Simulator[F] = copy(scheduler = s)

  def withHandler(h: EventHandler[F]): Simulator[F] = copy(eventHandlers = Seq(h))
  def withHandlers(hs: EventHandler[F]*): Simulator[F] = copy(eventHandlers = eventHandlers ++ hs)
  def and(h: EventHandler[F]): Simulator[F] = copy(eventHandlers = eventHandlers :+ h)


  def simulate(scenario: Scenario[F]): F[Unit] =
      simulateState(scenario.name, scenario.state)

  def simulateState(name: String, state: StateT[F, Simulationx[F], Seq[Event]]): F[Unit] = for {
    publisher <- Publisher.build[F]()
    subresource = eventHandlers.map(publisher.subscribe(_)).sequence
    _ <- subresource.use { streams => {
      val subio = streams.map(_.compile.drain).sequence
      val pubio = simulateStateWithPublisher(name, state, publisher)
      (pubio, subio).parTupled
    } }
  } yield (())

  def simulateStateWithPublisher(name: String, state: StateT[F, Simulationx[F], Seq[Event]], publisher: Publisher[F]): F[Unit] = {
    val sim = Simulationx[F](name, scheduler)
    for {
      sResult <- state.run(sim)
      (updated, events) = sResult
      _ <- events.map(publisher.publish(_)).sequence
      x <- updated.tailRecM(simRec(publisher))
    } yield (x)
  }

  protected def simRec(publisher: Publisher[F]): Simulationx[F] => F[Either[Simulationx[F], Unit]] = sim => for {
    tResult <- sim.tick
    (ticked, tEvents) = tResult
    _ <- tEvents.map(publisher.publish(_)).sequence
  } yield (ticked)
  
}

object Simulator {
  def apply[F[_] : Concurrent](): Simulator[F] = Simulator(ProterScheduler, Seq())
  def apply[F[_] : Concurrent](scheduler: Scheduler): Simulator[F] = Simulator(scheduler, Seq())

}

import cats.effect.{ IO, IOApp, ExitCode }


object TestSim extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    runScenario()
  
  def runScenario(): IO[ExitCode] =
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simulator = Simulator[IO](ProterScheduler) withHandler (PrintEventHandler())
      val scenario = Scenario[IO]("MYSCENARIO").withCases (
        ("foo1", Task("t", 1)),
        ("foo2", Task("t", 2)),
        ("foo3", Task("t", 3)),
        ("foo4", Task("t", 4)),
        ("foo4.2", Task("t", 4)),
      )
      simulator.simulate(scenario).as(ExitCode(1))
  }
  
  def runState(): IO[ExitCode] =
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simulator = Simulator[IO](ProterScheduler) withHandler (PrintEventHandler())
      val cases = simulator.addCasesNow[IO, Task](Seq(
        ("foo1", Task("t", 1)),
        ("foo2", Task("t", 2)),
        ("foo3", Task("t", 3)),
        ("foo4", Task("t", 4)),
        ("foo4.2", Task("t", 4)),
      ))
      simulator.simulateState("X", cases).as(ExitCode(1))
  }
  

}

