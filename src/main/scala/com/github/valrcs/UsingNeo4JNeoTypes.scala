package com.github.valrcs

object UsingNeo4JNeoTypes extends App {
  println("Tesing Neo4J Neotypes library")

  import neotypes.GraphDatabase
  import neotypes.generic.auto._
  import neotypes.implicits.syntax.all._
  import org.neo4j.driver.AuthTokens
  import scala.concurrent.{Await, Future}
  import scala.concurrent.ExecutionContext.Implicits.global
  import scala.concurrent.duration._

  val pw = "changeme" //FIXME read me from enviromen
  val driver = GraphDatabase.driver[Future]("bolt://3.83.112.130:7687", AuthTokens.basic("neo4j", pw))

  val people = "MATCH (p: Person) RETURN p.name, p.born LIMIT 10".query[(String, Int)].list(driver)
  Await.result(people, 2.second)
  // res: Seq[(String, Int)] = ArrayBuffer(
  //   (Charlize Theron, 1975),
  //   (Keanu Reeves, 1964),
  //   (Carrie-Anne Moss, 1967),
  //   (Laurence Fishburne, 1961),
  //   (Hugo Weaving, 1960),
  //   (Lilly Wachowski, 1967),
  //   (Lana Wachowski, 1965),
  //   (Joel Silver,1952),
  //   (Emil Eifrem,1978),
  //   (Charlize Theron,1975)
  // )

  final case class Person(id: Long, born: Int, name: Option[String], notExists: Option[Int])
  final case class Movie(id: Long, released: Int, tagline: Option[String], title: Option[String])

//  val movies = "MATCH (m: Movie) WHERE m.release > 2000 RETURN m LIMIT 10".query[Movie].list(driver)
  val movies = "MATCH (m: Movie) RETURN m LIMIT 10".query[Movie].list(driver)
  Await.result(movies, 3.second)


  val peopleCC = "MATCH (p: Person) RETURN p LIMIT 10".query[Person].list(driver)
  Await.result(peopleCC, 2.second)
  // res: Seq[Person] = ArrayBuffer(
  //   Person(0, 1975, Some(Charlize Theron), None),
  //   Person(1, 1964, Some(Keanu Reeves), None),
  //   Person(2, 1967, Some(Carrie-Anne Moss), None),
  //   Person(3, 1961, Some(Laurence Fishburne), None),
  //   Person(4, 1960, Some(Hugo Weaving), None),
  //   Person(5, 1967, Some(Lilly Wachowski), None),
  //   Person(6, 1965, Some(Lana Wachowski), None),
  //   Person(7, 1952, Some(Joel Silver), None),
  //   Person(8, 1978, Some(Emil Eifrem), None),
  //   Person(9, 1975, Some(Charlize Theron), None)
  // )

  Await.ready(driver.close, 2.second)
  println("Should be done with the connection to Neo4J")
  movies.foreach(movieList => movieList.foreach(println))
  peopleCC.foreach(myList => myList.foreach(println))
}
