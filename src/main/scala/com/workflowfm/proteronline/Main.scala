package com.workflowfm.proteronline

import cats.effect.{Async, Resource}
import cats.effect.{ExitCode, IOApp}
import cats.effect.IO
import cats.syntax.all._
import com.comcast.ip4s._
import fs2.Stream
import org.http4s.blaze.server._
import org.http4s.blaze.server.BlazeServerBuilder
import org.http4s.ember.client.EmberClientBuilder
import org.http4s.ember.server.EmberServerBuilder
import org.http4s.implicits._
import org.http4s.implicits._
import org.http4s.server.Router
import org.http4s.server.Router
import org.http4s.server.middleware.CORS
import org.http4s.server.middleware.CORS
import org.http4s.server.middleware.Logger
import cats.data.Kleisli
import org.http4s.{Request, Response }


object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    if (args.length > 0) {
      return StreamingServer.serverBuilder.serve.compile.drain.as(ExitCode.Success)
    } else {
      return StandardServer.stream[IO].compile.drain.as(ExitCode.Success)
    }    
}

object StreamingServer {

    val services = CORS.policy
          .withAllowOriginAll
          .withAllowCredentials(false)
          .apply(ProteronlineRoutes.streamApiRoutes[IO]())
    val httpApp: Kleisli[IO,Request[IO],Response[IO]] = Router("/" -> services).orNotFound
    val serverBuilder: BlazeServerBuilder[IO] = BlazeServerBuilder[IO].bindHttp(8080, "localhost").withHttpApp(httpApp)  
}

object StandardServer {

  def stream[F[_]: Async]: Stream[F, Nothing] = {
    
    for {
      _ <- Stream.resource(EmberClientBuilder.default[F].build) //Cant remove this line or the whole thing freaks out
      helloWorldAlg = HelloWorld.impl[F]

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


