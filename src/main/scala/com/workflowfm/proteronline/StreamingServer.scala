package com.workflowfm.proteronline

import org.http4s.blaze.server.BlazeServerBuilder
import cats.effect.IO
import org.http4s.server.middleware.CORS
import org.http4s.server.Router
import org.http4s.blaze.server._
import org.http4s.implicits._
import org.http4s.server.Router

object StreamingServer {

    val services = CORS.policy
          .withAllowOriginAll
          .withAllowCredentials(false)
          .apply(ProteronlineRoutes.streamApiRoutes[IO]())
    // services: cats.data.Kleisli[cats.data.OptionT[IO, β$0$], Request[IO], Response[IO]] = Kleisli(
    //   cats.data.KleisliSemigroupK$$Lambda$17101/636152927@30ca3ae0
    // )
    val httpApp = Router("/" -> services).orNotFound
    // httpApp: cats.data.Kleisli[IO, Request[IO], Response[IO]] = Kleisli(
    //   org.http4s.syntax.KleisliResponseOps$$Lambda$17314/2020906500@227d09cf
    // )
    val serverBuilder = BlazeServerBuilder[IO].bindHttp(8080, "localhost").withHttpApp(httpApp)
    // serverBuilder: BlazeServerBuilder[IO] = org.http4s.blaze.server.BlazeServerBuilder@12d04c86
  
}
