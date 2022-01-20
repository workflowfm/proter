import com.workflowfm.proter.Dependencies

enablePlugins(HugoPlugin)
enablePlugins(SiteScaladocPlugin)
enablePlugins(GhpagesPlugin)


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
      "scm:git:git@github.com:workflowfm/Proter.git"
    )
  ),
  dynverSonatypeSnapshots := true,

  scalafixDependencies += Dependencies.sortImports,
  git.remoteRepo := scmInfo.value.get.connection.replace("scm:git:", "")
))

// Publish to Sonatype / Maven Central

ThisBuild / publishTo := sonatypePublishToBundle.value
pomIncludeRepository := { _ => false }
publishMavenStyle := true
sonatypeCredentialHost := "s01.oss.sonatype.org"
sonatypeRepository := "https://s01.oss.sonatype.org/service/local"


// Website generation with sbt-site

Hugo / sourceDirectory := file("docs")
Hugo / baseURL := uri("http://docs.workflowfm.com/proter")
//baseURL in Hugo := uri("./")
Hugo / includeFilter := ("*")

ghpagesNoJekyll := true
previewFixedPort := Some(9999)

//ThisBuild / scalafixScalaBinaryVersion := "3.1"

lazy val commonSettings = Seq(
  scalaVersion := Dependencies.scalaVer,

  semanticdbEnabled := true,
  semanticdbVersion := scalafixSemanticdb.revision,

 // scalacOptions += "-Wunused:imports", // required by `RemoveUnused` rule
 // scalacOptions += "-deprecation",
 // scalacOptions += "-feature",

  scalacOptions ++= {
    Seq(
      "-encoding",
      "UTF-8",
      "-feature",
      "-deprecation",
      //"-language:implicitConversions",
      // "-Xfatal-warnings",
      "-unchecked",
//      "-source:3.0-migration",
//      "-rewrite",
//      "-new-syntax"
      )
  },

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
    ScalaUnidoc / siteSubdirName := "api",
    addMappingsToSiteDir(ScalaUnidoc / packageDoc / mappings, ScalaUnidoc / siteSubdirName)
  )
  .aggregate(aggregatedProjects: _*)
  .enablePlugins(ScalaUnidocPlugin)

lazy val proter = Project(id = "proter", base = file("proter"))
  .settings(commonSettings)
  .settings(libraryDependencies ++= Dependencies.common)
  .settings(libraryDependencies ++= Dependencies.testAll)

lazy val proterAkka = proterModule("proter-akka")
  .settings(libraryDependencies ++= Dependencies.akka)



