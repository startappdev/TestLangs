import AssemblyKeys._

name := "ScalaTesting"

version := "1.0"

scalaVersion := "2.11.8"

mainClass in Global := Some("com.startapp.etl.detectlang.Main")


resolvers ++= Seq(
  "Typesafe Repository" at "http://repo.typesafe.com/typesafe/releases/",
  "maven" at "http://repo1.maven.org/maven2",
  "scala tools" at "http://scala-tools.org/repo-releases",
  "Spray Repository" at "http://repo.spray.io"
)

libraryDependencies ++= {
  val akkaVersion       = "2.4.14"
  val sprayVersion      = "1.3.2"
  Seq(
    "com.typesafe.akka"         %% "akka-actor"        % akkaVersion,
    "commons-codec"             % "commons-codec"      % "1.9",
    "com.typesafe.akka"         %% "akka-stream"       % akkaVersion,
    //"org.reactivemongo"         %% "reactivemongo"     % "0.12",
    "com.optimaize.languagedetector" % "language-detector" % "0.6"
    //langdetect is added as unmanaged jar

  )
}