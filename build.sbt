
name := "holomorphic-maps"

version := "1.0"

scalaVersion := "2.12.1"

scalacOptions ++= Seq("-deprecation", "-feature", "-encoding", "utf-8")

lazy val `renderer` = project.in(file(".")).
  enablePlugins(ScalaJSPlugin).
  settings(
    libraryDependencies ++= Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.1",
      "me.chrons" %%% "boopickle" % "1.2.5",
      "be.adoeraene" %%% "scala-canvas-gui" % "0.1.0-SNAPSHOT"

    ),
    scalaJSModuleKind := ModuleKind.CommonJSModule
  )
