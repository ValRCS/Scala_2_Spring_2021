name := "Scala_2_Spring_2021"

version := "0.1"

scalaVersion := "2.13.5"

libraryDependencies ++= Seq(
  "net.debasishg" %% "redisclient" % "3.30"
)

libraryDependencies += "org.mongodb.scala" %% "mongo-scala-driver" % "4.2.3"

// https://mvnrepository.com/artifact/org.neo4j.driver/neo4j-java-driver
libraryDependencies += "org.neo4j.driver" % "neo4j-java-driver" % "4.2.5"

// https://mvnrepository.com/artifact/com.dimafeng/neotypes
libraryDependencies += "com.dimafeng" %% "neotypes" % "0.17.0"

