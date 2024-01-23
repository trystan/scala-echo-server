name := """scala-echo-server"""
organization := "com.trystan"

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayScala)

scalaVersion := "2.13.12"

libraryDependencies += guice
libraryDependencies += "org.scalatestplus.play" %% "scalatestplus-play" % "7.0.0" % Test
libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.11.0"

// Adds additional packages into Twirl
//TwirlKeys.templateImports += "com.trystan.controllers._"

// Adds additional packages into conf/routes
// play.sbt.routes.RoutesKeys.routesImport += "com.trystan.binders._"
