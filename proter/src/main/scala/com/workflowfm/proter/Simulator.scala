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


trait Simulator extends CaseState {

  def simulateState[F[_] : Concurrent](name: String, state: StateT[F, Simulationx[F], Seq[Event]], scheduler: Scheduler) (eventHandlers: EventHandler[F]*): F[Unit] = for {
    publisher <- Publisher.build[F]()
    subresource = eventHandlers.map(publisher.subscribe(_)).sequence
    _ <- subresource.use { streams => {
      val subio = streams.map(_.compile.drain).sequence
      val pubio = simulateStateWithPublisher(name, state, scheduler, publisher)
      (pubio, subio).parTupled
    } }
  } yield (())

  def simulateStateWithPublisher[F[_] : Monad](name: String, state: StateT[F, Simulationx[F], Seq[Event]], scheduler: Scheduler, publisher: Publisher[F]): F[Unit] = {
    val sim = Simulationx[F](name, scheduler)
    for {
      sResult <- state.run(sim)
      (updated, events) = sResult
      _ <- events.map(publisher.publish(_)).sequence
      x <- updated.tailRecM(simRec(publisher))
    } yield (x)
  }

  protected def simRec[F[_] : Monad](publisher: Publisher[F]): Simulationx[F] => F[Either[Simulationx[F], Unit]] = sim => for {
    tResult <- sim.tick
    (ticked, tEvents) = tResult
    _ <- tEvents.map(publisher.publish(_)).sequence
  } yield (ticked)
  
}

import cats.effect.{ IO, IOApp, ExitCode }


object TestSim extends IOApp with Simulator {
  def run(args: List[String]): IO[ExitCode] =
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val cases = addCasesNow[IO, Task](Seq(
        ("foo1", Task("t", 1)),
        ("foo2", Task("t", 2)),
        ("foo3", Task("t", 3)),
        ("foo4", Task("t", 4)),
        ("foo4.2", Task("t", 4)),
      ))
      simulateState("X", cases, ProterScheduler)(PrintEventHandler()).as(ExitCode(1))
  }
  
}

