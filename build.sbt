ThisBuild / autoAPIMappings := true

// Fixes some sbt import problems.
// https://github.com/sbt/sbt-native-packager/issues/1063
resolvers += Resolver.sbtPluginRepo("releases")

/* Only invoked when you do `doc` in SBT */
scalacOptions in (Compile, doc) += "-groups"
scalacOptions in (Compile, doc) += "-diagrams"
scalacOptions in (Compile, doc) += "-diagrams-debug"

ThisBuild / scalafixDependencies +=  "com.nequissimus" %% "sort-imports" % "0.5.4"

lazy val commonSettings = Seq(
  organization := "com.workflowfm",
  scalaVersion := "2.12.12",
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,
  scalacOptions += "-Ywarn-unused" // required by `RemoveUnused` rule
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.12"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.16" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.13"

libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "1.0.0"
libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "1.0.0" % Test classifier "tests"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2"

libraryDependencies += "junit" % "junit" % "4.8.2"

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "proter"
  )
