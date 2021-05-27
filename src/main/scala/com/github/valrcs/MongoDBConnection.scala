package com.github.valrcs
import org.mongodb.scala.MongoClient
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._

import java.lang.Thread.sleep

object MongoDBConnection extends App {
  println("Testing MongoDB database")

  val user = "badscalaVal"
  val pw = "needreal" //FIXME do not submit real pw to GIT!!!!
  val dbname = "sample_restaurants"
  val collectionName = "restaurants"

  //uri - universal resource identificator
  //make sure when connectiong that cluster0......net is correct for your databas
  val uri: String = s"mongodb+srv://$user:$pw@cluster0.whr5v.mongodb.net"
  System.setProperty("org.mongodb.async.type", "netty")
  val client : MongoClient  = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase(dbname)
  val collection = db.getCollection(collectionName)

  //https://mongodb.github.io/mongo-scala-driver/1.0/reference/observables/
  collection.find(equal("name", "456 Cookies Shop")).subscribe((doc: Document) => println(doc.toJson()),
    (e: Throwable) => println(s"There was an error: $e"),
    () => println("Completed!"))


  println(collection.find().first().toString)

  val brooklyn_chinese = collection.find(and(equal("borough", "Brooklyn"),
    equal("cuisine", "Chinese")))//so this example of


//  val chinese = collection.find(and(equal("cuisine", "Chinese"),equal("borough","Brooklyn")))//so this example of



//  brooklyn_chinese.foreach(println)
    sleep(5000) //so sleep for 5 seconds
  client.close()
}
