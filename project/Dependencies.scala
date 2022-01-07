package com.workflowfm.proter

import sbt._

object Dependencies {

  val scalaVer = "2.12.15"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.10" % "test"
  val scalaMock = "org.scalamock" %% "scalamock" % "5.2.0" % "test"

  val akkaActor = "com.typesafe.akka" %% "akka-actor" % "2.6.1"
  val akkaTestkit = "com.typesafe.akka" %% "akka-testkit" % "2.6.1" % "test"

  val apache = "org.apache.commons" % "commons-lang3" % "3.3.2"

  val sortImports = "com.nequissimus" %% "sort-imports" % "0.5.4"

  val common: Seq[ModuleID] = Seq(
    apache
  )

  val testAll: Seq[ModuleID] = Seq(
    scalaTest,
    scalaMock,
  )

  val akka: Seq[ModuleID] = Seq (
    akkaActor,
    akkaTestkit,
  )
}


