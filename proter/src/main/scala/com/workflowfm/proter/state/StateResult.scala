package com.workflowfm.proter
package state

import cases.CaseRef

import scala.collection.immutable.Map

sealed trait StateResult[F[_], C[T] <: Iterable[T]]

case class StateDone[F[_], C[T] <: Iterable[T]](state: State[F, C]) extends StateResult[F, C]

case class StateWaiting[F[_], C[T] <: Iterable[T]](state: State[F, C], tasks: Map[CaseRef[F], TaskInstance]) extends StateResult[F, C]
