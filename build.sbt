ThisBuild / version          := "0.3-SNAPSHOT"
ThisBuild / organization     := "com.workflowfm"
ThisBuild / scalaVersion     := "2.12.10"

ThisBuild / autoAPIMappings  := true
// Fixes some sbt import problems.
// https://github.com/sbt/sbt-native-packager/issues/1063
resolvers += Resolver.sbtPluginRepo("releases")

lazy val pew = "com.workflowfm" %% "pew" % "1.4.0-SNAPSHOT"
lazy val sim = "com.workflowfm" %% "wfm-simulator" % "0.2.1"
lazy val pewsim = "com.workflowfm" %% "pew-simulator" % "1.4.0-SNAPSHOT"

lazy val root = (project in file("."))
  .settings(
    name := "wfm-simulator",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test,
    libraryDependencies +=  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,

    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.12",
    libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.16" % "test",

    libraryDependencies += "com.typesafe.akka" %% "akka-stream"       % "2.5.13",

    libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "0.1-SNAPSHOT",
    libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "0.1-SNAPSHOT" % Test  classifier "tests",

    libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2",

    libraryDependencies += "junit" % "junit" % "4.8.2"
)

lazy val flowsExample = project
  .in(file("flowsExample"))
  .settings(
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test,
    libraryDependencies +=  "org.scalacheck" %% "scalacheck" % "1.14.0" % Test,

    libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.12",
    libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.16" % "test",

    libraryDependencies += "com.typesafe.akka" %% "akka-stream"       % "2.5.13",

    libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "0.1-SNAPSHOT",
    libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "0.1-SNAPSHOT" % Test  classifier "tests",

    libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2",

    libraryDependencies += "junit" % "junit" % "4.8.2",
    
    //libraryDependencies += "com.workflowfm" %% "flows-simulator" % "0.2.1",
    libraryDependencies += pew,
    libraryDependencies += sim,
    libraryDependencies += pewsim,

    scalaSource in Compile := baseDirectory.value / "src",
    scalaSource in Test := baseDirectory.value / "test"
)
