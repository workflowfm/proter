package com.workflowfm.proter

import sbt._

object Dependencies {

  val scalaVer = "2.12.12"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % "test"
  val scalaCheck = "org.scalacheck" %% "scalacheck" % "1.14.0" % "test"
  val scalaMock = "org.scalamock" %% "scalamock" % "4.4.0" % "test"
  val junit = "junit" % "junit" % "4.8.2" % "test"

  val proter = "com.workflowfm" %% "proter" % "0.7.3"

  val sortImports = "com.nequissimus" %% "sort-imports" % "0.5.4"

  val common: Seq[ModuleID] = Seq(
    proter
  )

  val testAll: Seq[ModuleID] = Seq(
    scalaTest,
    scalaCheck,
    scalaMock,
    junit,
  )

}


