package com.workflowfm.proter
package server

import metrics.Metrics
import events.{ Event, JsonHandler }

import cats.effect.Async
import cats.implicits.*
import cats.effect.std.{ Random, UUIDGen }

import io.circe.syntax.*

import org.http4s.{ EntityDecoder, EntityEncoder, HttpRoutes }
import org.http4s.circe.*
import org.http4s.dsl.Http4sDsl

object Routes {

  def apiRoutes[F[_] : Async : Random : UUIDGen](): HttpRoutes[F] = {
    val dsl = new Http4sDsl[F] {}
    import dsl._
    import Metrics.given
    import Entities.given
    import JsonHandler.given

    given EntityDecoder[F, IRequest] = jsonOf[F, IRequest]

    val runner: SimulationRunner[F] = new SimulationRunner[F]()
    HttpRoutes.of[F] {
      case req @ POST -> Root / "simulate" =>
        req
          .as[IRequest]
          .flatMap { decReq =>
            runner.handle(decReq).flatMap { (m: Metrics) => Ok(m.asJson) }
          }
          .handleErrorWith { case e: Throwable =>
            UnprocessableEntity(e.getMessage().asJson)
          }
      case req @ POST -> Root / "stream" =>
        req
          .as[IRequest]
          .flatMap { decReq =>
            Ok.apply(runner.stream(decReq).map(_.asJson))
          }
          .handleErrorWith { case e: Throwable =>
            UnprocessableEntity(e.getMessage().asJson)
          }
    }
  }

}
