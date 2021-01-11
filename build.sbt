import com.workflowfm.proter.Dependencies

lazy val commonSettings = Seq(
  organization := "com.workflowfm",
  scalaVersion := Dependencies.scalaVer,
  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,
  scalacOptions += "-Ywarn-unused" // required by `RemoveUnused` rule
)

autoAPIMappings := true

// Fixes some sbt import problems.
// https://github.com/sbt/sbt-native-packager/issues/1063
resolvers += Resolver.sbtPluginRepo("releases")

/* Only invoked when you do `doc` in SBT */
scalacOptions in (Compile, doc) += "-groups"
scalacOptions in (Compile, doc) += "-diagrams"
scalacOptions in (Compile, doc) += "-diagrams-debug"

ThisBuild / scalafixDependencies += Dependencies.sortImports

lazy val root = Project(id = "proter", base = file("."))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Dependencies.common)
  .settings(libraryDependencies ++= Dependencies.testAll)
