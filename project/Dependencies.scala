package com.workflowfm.proter

import sbt._

object Dependencies {

  val scalaVer = "3.1.0"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.12" % "test"

  val cats = "org.typelevel" %% "cats-effect" % "3.3.12"
  val catsTest = "org.typelevel" %% "cats-effect-testing-scalatest" % "1.4.0" % Test

  val fs2 = "co.fs2" %% "fs2-core" % "3.2.10"
  val fs2io = "co.fs2" %% "fs2-io" % "3.2.10"

  val apache = "org.apache.commons" % "commons-lang3" % "3.3.2"

  val sortImports = "com.nequissimus" %% "sort-imports" % "0.6.1"

  val common: Seq[ModuleID] = Seq(
    apache,
    cats,
    fs2,
    fs2io
  )

  val testAll: Seq[ModuleID] = Seq(
    scalaTest,
    catsTest,
  )

}

