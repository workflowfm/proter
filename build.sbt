import com.workflowfm.proter.Dependencies

inThisBuild(List(
  organization := "com.workflowfm",
  organizationName := "WorkflowFM",
  organizationHomepage := Some(url("http://www.workflowfm.com/")),
  homepage := Some(url("http://www.workflowfm.com/proter/")),
  description := "A discrete event simulator for asynchronous prioritized processes",
  licenses := Seq("APL2" -> url("http://www.apache.org/licenses/LICENSE-2.0.txt")),

  developers := List(
    Developer(
      id = "PetrosPapapa",
      name = "Petros Papapanagiotou",
      email = "petros@workflowfm.com",
      url = url("https://homepages.inf.ed.ac.uk/ppapapan/")
    )
  ),
  scmInfo := Some(
    ScmInfo(
      url("https://github.com/workflowfm/Proter"),
      "scm:git@github.com:workflowfm/Proter.git"
    )
  ),
  dynverSonatypeSnapshots := true,

  scalafixDependencies += Dependencies.sortImports,
))

// Publish to Sonatype / Maven Central

publishTo in ThisBuild := sonatypePublishToBundle.value
pomIncludeRepository := { _ => false }
publishMavenStyle := true
sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"


// Website generation with sbt-site

enablePlugins(HugoPlugin)
enablePlugins(SiteScaladocPlugin)
Hugo / sourceDirectory := file("docs")
//baseURL in Hugo := uri("http://docs.workflowfm.com/proter")
baseURL in Hugo := uri("./")
includeFilter in Hugo := ("*")


lazy val commonSettings = Seq(
  scalaVersion := Dependencies.scalaVer,

  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,

  scalacOptions += "-Ywarn-unused", // required by `RemoveUnused` rule
  autoAPIMappings := true,
  Compile / doc / scalacOptions ++= Seq("-groups", "-implicits", "-diagrams", "-diagrams-debug"),

)



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
  .settings( 
    publishArtifact := false,
    siteSubdirName in ScalaUnidoc := "api",
    addMappingsToSiteDir(mappings in (ScalaUnidoc, packageDoc), siteSubdirName in ScalaUnidoc)
  )
  .aggregate(aggregatedProjects: _*)
  .enablePlugins(ScalaUnidocPlugin)

lazy val proter = Project(id = "proter", base = file("proter"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Dependencies.common)
  .settings(libraryDependencies ++= Dependencies.testAll)

lazy val proterAkka = proterModule("proter-akka")
  .settings(libraryDependencies ++= Dependencies.akka)



