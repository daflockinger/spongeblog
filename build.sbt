name := """spongeblog"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
 "org.mongodb" % "mongo-java-driver" % "3.2.2",
  "org.mongodb.morphia" % "morphia" % "1.2.1",
  "org.mongodb.morphia" % "morphia-logging-slf4j" % "1.2.1",
  "org.mongodb.morphia" % "morphia-validation" % "1.2.1",
  javaJdbc,
  cache,
  javaWs
)

PlayKeys.externalizeResources := false
