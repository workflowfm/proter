package com.workflowfm.proter.server

import cats.implicits.*
import cats.effect.{ Async, Resource }
import cats.effect.{ ExitCode, IOApp, IO }
import cats.effect.std.{ Random, UUIDGen }
import cats.effect.implicits.*

import com.comcast.ip4s.*
import fs2.Stream

import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits.*
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import org.http4s.server.middleware.Logger
import org.http4s.{ Request, Response }

object ProterServer {

  def stream[F[_] : Async : Random : UUIDGen]: Stream[F, Nothing] = {
    val httpApp = (
      CORS.policy.withAllowOriginAll
        .withAllowCredentials(false)
        .apply(Routes.apiRoutes[F]())
      )
      .orNotFound

    // With Middlewares in place
    val finalHttpApp = Logger.httpApp(true, true)(httpApp)

    Stream.resource(
      EmberServerBuilder
        .default[F]
        .withHost(ipv4"0.0.0.0")
        .withPort(port"8080")
        .withHttpApp(finalHttpApp)
        .build >>
        Resource.eval(Async[F].never)
    )
  }
}

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] =
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      ProterServer.stream[IO].compile.drain.as(ExitCode.Success)
    }
}
