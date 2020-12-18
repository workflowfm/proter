ThisBuild / autoAPIMappings := true
fork in run := true

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

// jpbm stuff:
libraryDependencies += "org.jboss.narayana.jta" % "narayana-jta" % "5.9.0.Final"
libraryDependencies += "org.drools" % "drools-core" % "7.45.0.Final"
libraryDependencies += "org.jbpm" % "jbpm-bpmn2" % "7.45.0.Final"
libraryDependencies += "org.jbpm" % "jbpm-audit" % "7.45.0.Final"
libraryDependencies += "org.jbpm" % "jbpm-flow" % "7.45.0.Final"
libraryDependencies += "org.jbpm" % "jbpm-runtime-manager" % "7.45.0.Final"
libraryDependencies += "org.jbpm" % "jbpm-human-task-core" % "7.45.0.Final"
libraryDependencies += "org.jbpm" % "jbpm-test" % "7.45.0.Final"
libraryDependencies += "org.jbpm" % "jbpm-test-util" % "7.45.0.Final"
libraryDependencies += "org.kie" % "kie-test-util" % "7.45.0.Final"
//libraryDependencies += "org.subethamail" % "subethasmtp-wiser" % "1.2" % "provided"
libraryDependencies += "org.apache.logging.log4j" % "log4j-core" % "2.14.0"
libraryDependencies += "org.slf4j" % "jcl-over-slf4j" % "1.7.26"  //runtime??
libraryDependencies += "com.h2database" % "h2" % "1.3.173"
libraryDependencies += "org.hibernate" % "hibernate-entitymanager" % "5.3.17.Final"
libraryDependencies += "org.kie" % "kie-api" % "7.45.0.Final"
libraryDependencies += "org.kie" % "kie-internal" % "7.45.0.Final"
libraryDependencies += "com.thoughtworks.xstream" % "xstream" % "1.4.11.1"
libraryDependencies += "ch.qos.logback" % "logback-classic" % "1.2.3"


lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "wfm-simulator"
  )

lazy val rootRef = LocalProject("root")

lazy val bpmnTest = (project in file("bpmnTest"))
  .settings(
    commonSettings,
    name := "bpmnTest",
    scalaSource in Compile := baseDirectory.value / "src",
    scalaSource in Test := baseDirectory.value / "test"
  ).dependsOn(rootRef)





