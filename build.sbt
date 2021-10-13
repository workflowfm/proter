import com.workflowfm.proter.Dependencies

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

lazy val commonSettings = Seq(
  scalaVersion := Dependencies.scalaVer,

  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,

  scalacOptions += "-Ywarn-unused", // required by `RemoveUnused` rule
  autoAPIMappings := true,
  Compile / doc / scalacOptions ++= Seq("-groups", "-implicits", "-diagrams", "-diagrams-debug"),

)

lazy val root = Project(id = "proter-online", base = file("."))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Dependencies.common)
  .settings(libraryDependencies ++= Dependencies.testAll)
