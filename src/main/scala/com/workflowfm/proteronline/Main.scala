package com.workflowfm.proteronline

import cats.effect.{ExitCode, IOApp}

object Main extends IOApp {
  def run(args: List[String]) =
    StreamingServer.serverBuilder.serve.compile.drain.as(ExitCode.Success)
}
