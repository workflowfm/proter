import com.workflowfm.proter.Dependencies

lazy val commonSettings = Seq(
  organization := "com.workflowfm",
  scalaVersion := Dependencies.scalaVer,

  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,

  scalacOptions += "-Ywarn-unused", // required by `RemoveUnused` rule
  autoAPIMappings := true,
  Compile / doc / scalacOptions ++= Seq("-groups", "-implicits", "-diagrams", "-diagrams-debug"),

)

ThisBuild / scalafixDependencies += Dependencies.sortImports

lazy val aggregatedProjects: Seq[ProjectReference] = List[ProjectReference](
  proter,
  proterAkka
)

def proterModule(name: String): Project = 
  Project(id = name, base = file(name))
    .settings(commonSettings)
    .settings(libraryDependencies ++= Dependencies.common)
    .settings(libraryDependencies ++= Dependencies.testAll)
    .dependsOn(proter % "compile->compile;test->test")

lazy val root = Project(id = "proter-root", base = file("."))
  .settings(commonSettings)
  .aggregate(aggregatedProjects: _*)
  .enablePlugins(ScalaUnidocPlugin)

lazy val proter = Project(id = "proter", base = file("proter"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Dependencies.common)
  .settings(libraryDependencies ++= Dependencies.testAll)

lazy val proterAkka = proterModule("proter-akka")
  .settings(libraryDependencies ++= Dependencies.akka)
