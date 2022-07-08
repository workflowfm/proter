package com.workflowfm.proter
package state

import cats.Monad
import cats.data.{ State, StateT }

import events.Event

trait StateOps {
  type SimState[F[_]] = StateT[F, Simulationx[F], Seq[Event]]

  def liftSingleState[F[_] : Monad](
      state: State[Simulationx[F], Event]
  ): StateT[F, Simulationx[F], Seq[Event]] = StateT.fromState(state.map(e => Monad[F].pure(Seq(e))))

  def liftSeqState[F[_] : Monad](
      state: State[Simulationx[F], Seq[Event]]
  ): StateT[F, Simulationx[F], Seq[Event]] = StateT.fromState(state.map(Monad[F].pure))

  def liftSingleStateT[F[_] : Monad](
      state: StateT[F, Simulationx[F], Event]
  ): StateT[F, Simulationx[F], Seq[Event]] = state.map(Seq(_))

  def compose[F[_] : Monad](l: SimState[F]*): SimState[F] =
    l.foldLeft(idState)(compose2)

  def compose2[F[_] : Monad](a: SimState[F], b: SimState[F]): SimState[F] =
    a.flatMap(e1 => b.map(e2 => e1 ++ e2))

  def idState[F[_] : Monad]: SimState[F] = StateT.pure(Seq())

  def idStateM[F[_] : Monad]: F[SimState[F]] = Monad[F].pure(StateT.pure(Seq()))
}
