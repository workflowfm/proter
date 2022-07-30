package com.workflowfm.proter

import sbt._

object Dependencies {

  val scalaVer = "3.1.0"
  
  val sortImports = "com.nequissimus" %% "sort-imports" % "0.6.1"


  val scalaTest = "org.scalatest" %% "scalatest" % "3.2.12" % "test"
  val scalaTestFun = "org.scalatest"   %% "scalatest-funsuite"  % "3.2.11" % "test"

  val cats = "org.typelevel" %% "cats-effect" % "3.3.12"
  val catsTest = "org.typelevel" %% "cats-effect-testing-scalatest" % "1.4.0" % Test

  val fs2Version = "3.2.10"
  val fs2 = Seq(
    "co.fs2" %% "fs2-core",
    "co.fs2" %% "fs2-io"
  ).map(_ % fs2Version) 

  val circeVersion = "0.14.2"
  val circe = Seq(
    "io.circe" %% "circe-core",
    "io.circe" %% "circe-generic",
  ).map(_ % circeVersion)
  val circefs2 = "io.circe" %% "circe-fs2" % "0.14.0"
  val circeparser = "io.circe" %% "circe-parser" % circeVersion

  val apache = "org.apache.commons" % "commons-lang3" % "3.3.2"

  val http4sVersion = "0.23.13"
  val http4s = Seq(
      "org.http4s"      %% "http4s-ember-server" % http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % http4sVersion,
      "org.http4s"      %% "http4s-circe"        % http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % http4sVersion,
  )

  val logback = "ch.qos.logback"  %  "logback-classic"     % "1.2.6"
  //val svmSubs = "org.scalameta"   %% "svm-subs"            % "20.2.0"

  //"org.scalactic"   %% "scalactic"           % "3.2.11",
    



  val common: Seq[ModuleID] = Seq(
    apache,
    cats,
    circefs2
  ) ++ 
  Seq(
    fs2, 
    circe
  ).flatten

  val testAll: Seq[ModuleID] = Seq(
    scalaTest,
    catsTest,
  )

  val server: Seq[ModuleID] = Seq(
    apache,
    cats,
    circeparser,
    logback,
  ) ++
  Seq(
    circe,
    http4s
  ).flatten

  val testServer: Seq[ModuleID] = Seq(
    scalaTest,
    scalaTestFun
  )

}

