import com.workflowfm.proter.Dependencies

val Http4sVersion = "0.23.6"
val CirceVersion = "0.14.1"
val MunitVersion = "0.7.29"
val LogbackVersion = "1.2.6"
val MunitCatsEffectVersion = "1.0.6"

lazy val root = (project in file("."))
  .settings(
    organization := "com.workflowfm",
    name := "proteronline",
    version := "0.0.1-SNAPSHOT",
    scalaVersion := "2.12.12",
    libraryDependencies ++= Seq(
      "org.http4s"      %% "http4s-ember-server" % Http4sVersion,
      "org.http4s"      %% "http4s-ember-client" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-server" % Http4sVersion,
      "org.http4s"      %% "http4s-blaze-client" % Http4sVersion,
      "org.http4s"      %% "http4s-circe"        % Http4sVersion,
      "org.http4s"      %% "http4s-dsl"          % Http4sVersion,
      "io.circe"        %% "circe-generic"       % CirceVersion,
      "io.circe"        %% "circe-parser"        % CirceVersion,
      "org.scalameta"   %% "munit"               % MunitVersion           % Test,
      "org.typelevel"   %% "munit-cats-effect-3" % MunitCatsEffectVersion % Test,
      "ch.qos.logback"  %  "logback-classic"     % LogbackVersion,
      "org.scalameta"   %% "svm-subs"            % "20.2.0",
      "com.workflowfm"  %% "proter"              % "0.7.4",
      "org.scalactic"   %% "scalactic"           % "3.2.11",
      "org.scalatest"   %% "scalatest"           % "3.2.11" % "test",
      "org.scalatest"   %% "scalatest-funsuite"  % "3.2.11" % "test"
    ),
    addCompilerPlugin("org.typelevel" %% "kind-projector"     % "0.13.0" cross CrossVersion.full),
    addCompilerPlugin("com.olegpy"    %% "better-monadic-for" % "0.3.1"),
    testFrameworks += new TestFramework("munit.Framework")
  )

lazy val commonSettings = Seq(
  scalacOptions += "-Ypartial-unification",
  scalaVersion := Dependencies.scalaVer,

  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,

  scalacOptions += "-Ywarn-unused", // required by `RemoveUnused` rule
  autoAPIMappings := true,
  Compile / doc / scalacOptions ++= Seq("-groups", "-implicits", "-diagrams", "-diagrams-debug"),
)

inThisBuild(List(
  organization := "com.workflowfm",
  organizationName := "WorkflowFM",
  organizationHomepage := Some(url("http://www.workflowfm.com/")),
  homepage := Some(url("http://www.workflowfm.com/proter/")),
  description := "Proter frontend and API",
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),
  
  dynverSonatypeSnapshots := true,

  scalafixDependencies += Dependencies.sortImports,
))
