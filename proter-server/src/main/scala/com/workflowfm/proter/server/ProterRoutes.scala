package com.workflowfm.proter
package server

import metrics.Metrics

import cats.effect.{ Concurrent }
import cats.implicits.*
import cats.effect.std.{ Random, UUIDGen }

import io.circe.syntax.*

import org.http4s.{ EntityDecoder, HttpRoutes }
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl

object ProterRoutes {

  def apiRoutes[F[_]: Concurrent : Random : UUIDGen](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F]{}
    import dsl._
    import Metrics.given
    import IntermediateObjects.given

    given EntityDecoder[F, IRequest] = jsonOf[F, IRequest]

    val runner: SimulationRunner[F] = new SimulationRunner[F]()
    HttpRoutes.of[F] {
      case req @ POST -> Root / "API" =>
        req.as[IRequest].flatMap { decReq =>
          runner.handle(decReq).flatMap { (m: Metrics) => Ok(m.asJson) }
        }.handleErrorWith {
          case e: Throwable => UnprocessableEntity(e.getMessage().asJson)
        }
    }
  }
/*
  def streamApiRoutes[F[_]: Concurrent](): HttpRoutes[IO] = {
    val dsl = new Http4sDsl[IO]{}
    import dsl._
    implicit val reqDec: EntityDecoder[IO, IRequest] = jsonOf[IO, IRequest]
    //implicit val streamEnc = EntityEncoder.streamEncoder[IO, Stream[String]]
    val parse: SimulationRunner = new SimulationRunner()
    HttpRoutes.of[IO] {
      case req @ POST -> Root / "stream" =>
        req.as[IRequest].flatMap { decReq =>
          Ok(parse.streamHandler(decReq).map(_.toString()))
        }.handleErrorWith {
          case _: Throwable => BadRequest("Issue with your request".asJson)
        }
      }
  }*/
}
