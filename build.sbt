ThisBuild / version          := "0.3-SNAPSHOT"
ThisBuild / autoAPIMappings  := true

// Fixes some sbt import problems.
// https://github.com/sbt/sbt-native-packager/issues/1063
resolvers += Resolver.sbtPluginRepo("releases")

/* Only invoked when you do `doc` in SBT */
scalacOptions in (Compile, doc) += "-groups"
scalacOptions in (Compile, doc) += "-diagrams"
scalacOptions in (Compile, doc) += "-diagrams-debug"

lazy val commonSettings = Seq (
  organization     := "com.workflowfm",
  scalaVersion     := "2.12.10"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test
libraryDependencies +=  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.12"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.16" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-stream"       % "2.5.13"

libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "0.1-SNAPSHOT"
libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "0.1-SNAPSHOT" % Test  classifier "tests"

libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2"

libraryDependencies += "junit" % "junit" % "4.8.2"


lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "wfm-simulator"
)

lazy val flowsExample = project
  .in(file("flowsExample"))
  .settings(
    commonSettings,
    libraryDependencies += "com.workflowfm" %% "wfm-simulator" % "0.3-SNAPSHOT",

    scalaSource in Compile := baseDirectory.value / "src",
    scalaSource in Test := baseDirectory.value / "test"
)
