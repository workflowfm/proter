package com.workflowfm.proteronline

import cats.effect._
import cats.effect.Sync
import cats.implicits._
import cats.implicits._
import io.circe.generic.auto._
import io.circe.syntax._
import org.http4s.EntityDecoder
import org.http4s.HttpRoutes
import org.http4s.circe._
import org.http4s.dsl.Http4sDsl

object ProteronlineRoutes {

  def helloWorldRoutes[F[_]: Sync](H: HelloWorld[F]): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    HttpRoutes.of[F] {
      case GET -> Root / "hello" / name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
      case POST -> Root / "hello" /name =>
        for {
          greeting <- H.hello(HelloWorld.Name(name))
          resp <- Ok(greeting)
        } yield resp
    }
  }

  def apiRoutes[F[_]: Concurrent](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    implicit val reqDec: EntityDecoder[F, IRequest] = jsonOf[F, IRequest]
    val parse: SimulationRunner = new SimulationRunner()
    HttpRoutes.of[F] {
      case req @ POST -> Root / "API" =>
        for {
          request <- req.as[IRequest]
          resp <- Ok(parse.process(request).asJson)
        } yield resp
      }
  }

  def streamApiRoutes[F[_]: Concurrent](): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO]{}
    import dsl._
    implicit val reqDec: EntityDecoder[IO, IRequest] = jsonOf[IO, IRequest]
    //implicit val streamEnc = EntityEncoder.streamEncoder[IO, Stream[String]]
    val parse: SimulationRunner = new SimulationRunner()
    HttpRoutes.of[IO] {
      case req @ POST -> Root / "stream" =>
        for {
          request <- req.as[IRequest]
          resp <- Ok(parse.streamHandler(request).map(_.toString()))
        } yield resp
      }
  }
}
