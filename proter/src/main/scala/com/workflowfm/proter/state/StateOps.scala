package com.workflowfm.proter
package state

import cats.Monad
import cats.data.{ State, StateT }

trait StateOps {
  type SimState[F[_]] = StateT[F, Simulationx[F], Seq[events.Event]]

  def seqM[F[_] : Monad, S[_[_]], E](
      state: State[S[F], E]
  ): StateT[F, S[F], Seq[E]] = StateT.fromState(state.map(e => Monad[F].pure(Seq(e))))

  def lift[F[_] : Monad, S[_[_]], E](
      state: State[S[F], Seq[E]]
  ): StateT[F, S[F], Seq[E]] = StateT.fromState(state.map(Monad[F].pure))

  def seq[F[_] : Monad, S[_[_]], E](
      state: StateT[F, S[F], E]
  ): StateT[F, S[F], Seq[E]] = state.map(Seq(_))

  def compose[F[_] : Monad, S[_[_]], E](l: StateT[F, S[F], Seq[E]]*): StateT[F, S[F], Seq[E]] =
    l.foldLeft(idState)(compose2)

  def compose2[F[_] : Monad, S[_[_]], E](a: StateT[F, S[F], Seq[E]], b: StateT[F, S[F], Seq[E]]): StateT[F, S[F], Seq[E]] =
    a.flatMap(e1 => b.map(e2 => e1 ++ e2))

  def idState[F[_] : Monad, S[_[_]], E]: StateT[F, S[F], Seq[E]] = StateT.pure(Seq())

  def idStateM[F[_] : Monad, S[_[_]], E]: F[StateT[F, S[F], Seq[E]]] = Monad[F].pure(StateT.pure(Seq()))

}
