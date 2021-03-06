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
  "org.pac4j" % "play-pac4j" % "2.5.1-SNAPSHOT",
  "org.pac4j" % "pac4j-jwt" % "1.9.2" exclude("commons-io" , "commons-io"),
  "org.pac4j" % "pac4j-http" % "1.9.2",
  "ma.glasnost.orika" % "orika-core" % "1.4.6",
  "org.powermock" % "powermock-module-junit4" % "1.6.5",
  "commons-collections" % "commons-collections" % "3.2.2",
  javaJdbc,
  filters,
  cache,
  javaWs
)

javaOptions in Test += "-Dconfig.file=conf/application.test.conf"
routesGenerator := InjectedRoutesGenerator

PlayKeys.externalizeResources := false
