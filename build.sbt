//import sbtrelease.Version

parallelExecution in ThisBuild := false

val kafkaVersion = "0.10.0.1"

val slf4jLog4jOrg = "org.slf4j"
val slf4jLog4jArtifact = "slf4j-log4j12"

lazy val commonSettings = Seq(
  organization := "me.jeffshaw.kafka",
  scalaVersion := "2.11.12",
  crossScalaVersions := Seq("2.10.7", "2.12.4"),
  homepage := Some(url("https://github.com/shawjef3/scalatest-embedded-kafka")),
  parallelExecution in Test := false,
  logBuffered in Test := false,
  fork in Test := true,
  javaOptions += "-Xmx1G"
)

lazy val commonLibrarySettings = libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.0",
  "org.apache.kafka" %% "kafka" % kafkaVersion exclude(slf4jLog4jOrg, slf4jLog4jArtifact),
  "org.apache.zookeeper" % "zookeeper" % "3.4.8" exclude(slf4jLog4jOrg, slf4jLog4jArtifact),
  "org.apache.avro" % "avro" % "1.7.7" exclude(slf4jLog4jOrg, slf4jLog4jArtifact),
  "com.typesafe.akka" %% "akka-actor" % "2.5.11" % Test,
  "com.typesafe.akka" %% "akka-testkit" % "2.5.11" % Test
)

lazy val publishSettings = Seq(
  licenses += ("MIT", url("http://opensource.org/licenses/MIT")),
  publishMavenStyle := true,
  publishArtifact in Test := false,
  pomIncludeRepository := { _ =>
    false
  },
  publishTo := sonatypePublishTo.value,
  pomExtra :=
    <scm>
      <url>https://github.com/manub/scalatest-embedded-kafka</url>
      <connection>scm:git:git@github.com:manub/scalatest-embedded-kafka.git</connection>
    </scm>
      <developers>
        <developer>
          <id>manub</id>
          <name>Emanuele Blanco</name>
          <url>http://twitter.com/manub</url>
        </developer>
      </developers>
)

//lazy val releaseSettings = Seq(
//  releaseVersionBump := Version.Bump.Minor,
//  releaseCrossBuild := true
//)

lazy val root = (project in file("."))
  .settings(name := "scalatest-embedded-kafka-root")
  .settings(commonSettings: _*)
  .settings(publishArtifact := false)
  .settings(publish := {})
//  .settings(releaseSettings: _*)
//  .disablePlugins(BintrayPlugin)
  .settings(publishTo := Some(Resolver.defaultLocal))
  .aggregate(embeddedKafka, kafkaStreams)

lazy val embeddedKafka = (project in file("embedded-kafka"))
  .settings(name := "scalatest-embedded-kafka_" + kafkaVersion)
  .settings(publishSettings: _*)
  .settings(commonSettings: _*)
  .settings(commonLibrarySettings)
//  .settings(releaseSettings: _*)

lazy val kafkaStreams = (project in file("kafka-streams"))
  .settings(name := "scalatest-embedded-kafka-streams" + kafkaVersion)
  .settings(publishSettings: _*)
  .settings(commonSettings: _*)
  .settings(commonLibrarySettings)
//  .settings(releaseSettings: _*)
  .settings(libraryDependencies ++= Seq(
    "org.apache.kafka" % "kafka-streams" % kafkaVersion exclude(slf4jLog4jOrg, slf4jLog4jArtifact)
  ))
  .dependsOn(embeddedKafka)
