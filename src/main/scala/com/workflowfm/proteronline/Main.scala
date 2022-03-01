package com.workflowfm.proteronline

import cats.effect.{ExitCode, IOApp}
import org.http4s.blaze.server.BlazeServerBuilder
import cats.effect.IO
import org.http4s.server.middleware.CORS
import org.http4s.server.Router
import org.http4s.blaze.server._
import org.http4s.implicits._
import org.http4s.server.Router
import cats.effect.{Async, Resource}
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.server.middleware.Logger
import org.http4s.server.middleware.CORS


object Main extends IOApp {
  def run(args: List[String]) =
    StreamingServer.serverBuilder.serve.compile.drain.as(ExitCode.Success)
    //ProteronlineServer.stream[IO].compile.drain.as(ExitCode.Success)
}

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

object ProteronlineServer {

  def stream[F[_]: Async]: Stream[F, Nothing] = {
    
    for {
      _ <- Stream.resource(EmberClientBuilder.default[F].build) //Cant remove this line or the whole thing freaks out
      helloWorldAlg = HelloWorld.impl[F]

      // Combine Service Routes into an HttpApp.
      // Can also be done via a Router if you
      // want to extract a segments not checked
      // in the underlying routes.
      httpApp = (
        CORS.policy
          .withAllowOriginAll
          .withAllowCredentials(false)
          .apply(ProteronlineRoutes.helloWorldRoutes[F](helloWorldAlg))
        <+>
        CORS.policy
          .withAllowOriginAll
          .withAllowCredentials(false)
          .apply(ProteronlineRoutes.apiRoutes[F]())
      ).orNotFound

      // With Middlewares in place
      finalHttpApp = Logger.httpApp(true, true)(httpApp)

      exitCode <- Stream.resource(
        EmberServerBuilder.default[F]
          .withHost(ipv4"0.0.0.0")
          .withPort(port"8080")
          .withHttpApp(finalHttpApp)
          .build >>
        Resource.eval(Async[F].never)
      )
    } yield exitCode
  }.drain
}


