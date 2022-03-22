package com.workflowfm.proter
package cases

import cats.Monad
import cats.implicits.*
import cats.effect.kernel.{ Ref, Sync }
import cats.effect.std.UUIDGen

import java.util.UUID

trait Case[F[_], T] {
  def init(name: String, t: T): F[CaseRef[F]]
}

given [F[_]](using Monad[F], UUIDGen[F]): Case[F, Task] with {

  override def init(name: String, t: Task): F[CaseRef[F]] = for {
    uuid <- UUIDGen[F].randomUUID
  } yield new CaseRef[F] {

    val id: UUID = t.id.getOrElse(uuid)
    val theTask: Task = t.withID(id)

    override val caseName: String = name
    override def run(): F[CaseResponse] = Monad[F].pure(task(theTask))
    override def stop(): F[Unit] = Monad[F].pure(())
    override def completed(time: Long, tasks: Seq[TaskInstance]): F[CaseResponse] =
      tasks.find(_.id == id) match {
        case None => CaseResponse.empty(this)
        case Some(ti) => Monad[F].pure(succeed((ti, time)))
      }
  }
}


trait AsyncCase[F[_] : Sync : UUIDGen, T] extends Case[F, T] { self =>
  type Callback = AsyncCaseRef.Callback[F]

  def init(name: String, t: T): F[CaseRef[F]] = for {
    state <- Ref[F].of[Map[UUID, Callback]](Map())
  } yield new AsyncCaseRef[F](state) {
    override val caseName: String = name
    override def run(): F[CaseResponse] = self.run(this, t)
  }

  def run(cs: AsyncCaseRef[F], t: T): F[CaseResponse]
}

