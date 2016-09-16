name := """spongeblog"""

version := "0.1.0"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

resolvers += "sonatype snapshots" at "https://oss.sonatype.org/content/repositories/snapshots/"

libraryDependencies ++= Seq(
 "org.mongodb" % "mongo-java-driver" % "3.2.2",
  "org.mongodb.morphia" % "morphia" % "1.2.1",
  "org.mongodb.morphia" % "morphia-logging-slf4j" % "1.2.1",
  "org.mongodb.morphia" % "morphia-validation" % "1.2.1",
  "org.pac4j" % "play-pac4j" % "2.5.0-SNAPSHOT",
  "org.pac4j" % "pac4j-jwt" % "1.9.2" exclude("commons-io" , "commons-io"),
  javaJdbc,
  cache,
  javaWs
)

PlayKeys.externalizeResources := false
