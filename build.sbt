ThisBuild / autoAPIMappings := true
//fork in run := true

// Fixes some sbt import problems.
// https://github.com/sbt/sbt-native-packager/issues/1063
resolvers += Resolver.sbtPluginRepo("releases")

/* Only invoked when you do `doc` in SBT */
scalacOptions in (Compile, doc) += "-groups"
scalacOptions in (Compile, doc) += "-diagrams"
scalacOptions in (Compile, doc) += "-diagrams-debug"

lazy val commonSettings = Seq(
  organization := "com.workflowfm",
  scalaVersion := "2.12.12"
)

libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.0-SNAP10" % Test
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.14.0" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.4.12"
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.16" % "test"
libraryDependencies += "com.typesafe.akka" %% "akka-stream" % "2.5.13"
libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "0.1-SNAPSHOT"
libraryDependencies += "uk.ac.ed.inf" %% "subakka" % "0.1-SNAPSHOT" % Test classifier "tests"
libraryDependencies += "org.apache.commons" % "commons-lang3" % "3.3.2"
libraryDependencies += "junit" % "junit" % "4.8.2"

// https://mvnrepository.com/artifact/org.camunda.bpm.model/camunda-bpmn-model
libraryDependencies += "org.camunda.bpm.model" % "camunda-bpmn-model" % "7.14.0"

// https://mvnrepository.com/artifact/org.camunda.bpm/camunda-engine
libraryDependencies += "org.camunda.bpm" % "camunda-engine" % "7.14.0" % "provided"

// https://mvnrepository.com/artifact/de.odysseus.juel/juel-api
libraryDependencies += "de.odysseus.juel" % "juel-api" % "2.2.7"
// https://mvnrepository.com/artifact/de.odysseus.juel/juel-impl
libraryDependencies += "de.odysseus.juel" % "juel-impl" % "2.2.7"

libraryDependencies += "io.spray" %%  "spray-json" % "1.3.6"



lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "wfm-simulator"
  )

lazy val rootRef = LocalProject("root")

lazy val camundaTest= (project in file("camundaTest"))
  .settings(
    commonSettings,
    name := "camundaTest",
    scalaSource in Compile := baseDirectory.value / "src",
    scalaSource in Test := baseDirectory.value / "test"
  ).dependsOn(rootRef)





