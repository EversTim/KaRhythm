name := """KBD"""
organization := "nl.sogyo"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.11.11"

libraryDependencies += filters
libraryDependencies += jdbc
libraryDependencies += "org.postgresql" % "postgresql" % "42.1.1.jre7"
//libraryDependencies += "com.typesafe.play" %% "play-slick" % "2.0.0"
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "2.0.0" % Test

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "nl.sogyo.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "nl.sogyo.binders._"
