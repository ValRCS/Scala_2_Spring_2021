package com.github.valrcs

import org.neo4j.driver.{AuthTokens, Config, GraphDatabase}
import org.neo4j.driver.Values.parameters

import scala.collection.mutable.ArrayBuffer

object UsingNeo4J extends App {
  println("Will connect to Neo4J database")


  val noSSL = Config.builder().build()

  final case class Movie(id: Int, title: String, tagline: String,  released: Int)

  val pw = "changeme" //FIXME read from enviroment ASAP!
  val driver = GraphDatabase.driver("bolt://3.83.112.130:7687", AuthTokens.basic("neo4j", pw), noSSL) // <password>
  val arrayBuffer1: ArrayBuffer[Movie] = ArrayBuffer()
  try {
    val session = driver.session

    try {
      //        val cypherQuery = "MATCH (n) " + "RETURN id(n) AS id"
      //        val cypherQuery = "MATCH (m:Movie) " + "RETURN m.title AS title, id(m) as id"
      val cypherQuery = "MATCH (m:Movie) " + "RETURN m as movie, id(m) as id"
      val result = session.run(cypherQuery, parameters()) //TODO check if parameters need values

      while ( {
        result.hasNext
      }) {
        val record = result.next // so somewhat similar to row in SQL one entry
        //          System.out.println(result.next.get("id"))
        //          println(record.get("id"), record.get("title"))
        val movie = record.get("movie")
        println(record.get("id"),movie.get("title"),movie.get("tagline"),movie.get("released"))
        arrayBuffer1 += Movie(record.get("id").asInt(),movie.get("title").asString(),movie.get("tagline").asString(),movie.get("released").asInt())
        //this is where you would store it in a case class type callin
      }
    } finally if (session != null) session.close()
  }
  driver.close()
  val movies = arrayBuffer1.toArray
  movies.filter(_.released > 2005).foreach(println)
}
