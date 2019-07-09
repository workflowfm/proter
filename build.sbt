ThisBuild / version          := "0.0.1-SNAPSHOT"
ThisBuild / organization     := "com.workflowfm"
ThisBuild / scalaVersion     := "2.12.6"

ThisBuild / autoAPIMappings  := true
// Fixes some sbt import problems.
// https://github.com/sbt/sbt-native-packager/issues/1063
resolvers += Resolver.sbtPluginRepo("releases")

lazy val root = (project in file("."))
  .settings(
    name := "WorkflowFM-Simulator",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test,

    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.12",
    libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.16" % "test",

    libraryDependencies += "com.typesafe.akka" %% "akka-stream-kafka" % "0.21.1",
    libraryDependencies += "com.typesafe.akka" %% "akka-stream"       % "2.5.13",

    libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2",

    libraryDependencies += "junit" % "junit" % "4.8.2"
)
