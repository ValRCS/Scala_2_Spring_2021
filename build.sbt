name := "Scala_2_Spring_2021"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "net.debasishg" %% "redisclient" % "3.30"
)

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.2.3"
