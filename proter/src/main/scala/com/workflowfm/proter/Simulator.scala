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

/**
  * Runs simulations.
  * 
  * @param scheduler The [[Scheduler]] to use during simulation.
  * @param subscribers The [[Subscriber]]s that should receive events.
  */
final case class Simulator[F[_] : Concurrent](
    scheduler: Scheduler,
    subscribers: Seq[Subscriber[F]]
) extends ScenarioState {

  /** 
    * Updates the [[Scheduler]] to be used during simulation. 
    * 
    * @param s The new [[Scheduler]].
    * @return The updated simulator.
    */
  def withScheduler(s: Scheduler): Simulator[F] = copy(scheduler = s)

  /** 
    * Updates to using a single [[Subscriber]].
    * 
    * @param sub The new [[Subscriber]].
    * @return The updated simulator.
    */
  def withSub(sub: Subscriber[F]): Simulator[F] = copy(subscribers = Seq(sub))

  /** 
    * Updates to using new [[Subscriber]]s.
    * 
    * @param subs The new [[Subscriber]]s.
    * @return The updated simulator.
    */
  def withSubs(subs: Subscriber[F]*): Simulator[F] = copy(subscribers = subs)

  /** 
    * Adds additional [[Subscriber]]s.
    * 
    * @param subs The new [[Subscriber]]s to add.
    * @return The updated simulator.
    */
  def and(subs: Subscriber[F]*): Simulator[F] = copy(subscribers = subscribers ++ subs)

  /** 
    * Simulates a [[Scenario]].
    * 
    * @param scenario The [[Scenario]] to simulate.
    * @return An effect producing `Unit` when the simulation finishes.
    */
  def simulate(scenario: Scenario[F]): F[Unit] =
    simulateState(scenario.name, scenario.state)

  /** 
    * Simulates a [[Scenario]] and streams the [[Event]]s.
    * 
    * @param scenario The [[Scenario]] to simulate.
    * @return A stream of [[Event]]s or `Throwable` errors.
    */
  def stream(scenario: Scenario[F]): Stream[F, Either[Throwable, Event]] =
    streamState(scenario.name, scenario.state)

  /** 
    * Runs a simulation from a given [[Simulation.SimState]].
    * 
    * @param name A name to use for the simulation.
    * @param state The starting state.
    * @return An effect producing `Unit` when the simulation finishes.
    */
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

  /** 
    * Runs and streams a simulation from a given [[Simulation.SimState]].
    * 
    * @param name A name to use for the simulation.
    * @param state The starting state.
    * @return A stream of [[Event]]s or `Throwable` errors.
    */
  def streamState(
      name: String,
      state: StateT[F, Simulation[F], Seq[Event]]
  ): Stream[F, Either[Throwable, Event]] = Stream
    .eval(for {
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
    } yield (stream))
    .flatten

  /** 
    * Runs a [[Simulation.SimState]] with a given [[Publisher]].
    * 
    * Subscribers should be handled externally. We are only publishing here.
    * 
    * @param name A name to use for the simulation.
    * @param state The starting state.
    * @param publisher The [[Publisher]] to use.
    * @return An effect producing `Unit` when the simulation finishes..
    */
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

