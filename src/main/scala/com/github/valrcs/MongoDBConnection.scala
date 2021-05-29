package com.github.valrcs
import org.mongodb.scala.MongoClient
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._

import java.lang.Thread.sleep
import scala.collection.mutable.ArrayBuffer

case class Restaurant(name: String,
                      cuisine: String,
                      borough: String, street:String, building:String, zipcode:String,
                      longitude:Double, latitude:Double,
                      curGrade:String,
                      curScore:Int)

object MongoDBConnection extends App {
  println("Testing MongoDB database")

  val user = "reader" //even user name should not be submitted to git
  val pw = Some(scala.util.Properties.envOrElse("MONGOPW", "nosuchsecret")).getOrElse("nothing") //TODO remember do not submit real pw to GIT!!!!
  val dbname = "sample_restaurants"
  val collectionName = "restaurants"

  //uri - universal resource identificator
  //make sure when connectiong that cluster0......net is correct for your databas
  val uri: String = s"mongodb+srv://$user:$pw@cluster0.whr5v.mongodb.net/sample_restaurants?retryWrites=true&w=majority"
  System.setProperty("org.mongodb.async.type", "netty")
  val client : MongoClient  = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase(dbname)
  val collection = db.getCollection(collectionName)

  //https://mongodb.github.io/mongo-scala-driver/1.0/reference/observables/
//  collection.find(equal("name", "456 Cookies Shop")).subscribe((doc: Document) => println(doc.toJson()),
//    (e: Throwable) => println(s"There was an error: $e"),
//    () => println("Completed!"))
//  collection.find(equal("borough", "Brooklyn")).subscribe((doc: Document) => println(doc.toJson()),
//    (e: Throwable) => println(s"There was an error: $e"),
//    () => println("Completed!"))
  val arrBuffer = ArrayBuffer[Document]() //Document comes from our MongoDB library

  collection.find(
    and(
    equal("borough", "Brooklyn"),
    equal("cuisine","Polish")))
    .subscribe((doc: Document) => {
      println(doc.toJson())
      arrBuffer += doc
    },
    (e: Throwable) => println(s"There was an error: $e"),
    () => println("Completed!"))




//  val brooklyn_chinese = collection.find(and(equal("borough", "Brooklyn"),
//    equal("cuisine", "Chinese")))//so this example of


//  val chinese = collection.find(and(equal("cuisine", "Chinese"),equal("borough","Brooklyn")))//so this example of

  //TODO convert single document into Restaurant case class
    def docToRestaurant(doc:Document) :Restaurant = {
      //TODO add more values
      val name = doc.getOrElse("name","NoNameRestaurant").asString().getValue()
      val cuisine = doc.getOrElse("cuisine","noCuisine").asString().getValue()
      val borough = doc.getOrElse("borough","noBorough").asString().getValue()
      //default for street should be blank document, but since string is valid json this still works
      val street = doc.getOrElse(key = "address", "nothing")
        .asDocument().getOrElse(key="street", "noStreet")
        .asString().getValue()
      val building = ""
      val zipcode = ""
      //of course there is a possibility of index not existing in get(0)
      val longitude = doc.getOrElse(key = "address", "nothing")
        .asDocument()
        .getOrElse("coord",Seq(0.0,0.0))
        .asArray().get(0)
        .asDouble() //mongoDb library methods
        .doubleValue() //this is the Scala approach the last 2
      val latitude = 0L
      val curGrade = "FF"
      val curScore = 0
      Restaurant(name, cuisine, borough, street, building, zipcode, longitude, latitude, curGrade, curScore)
    }

//  brooklyn_chinese.foreach(println)
    sleep(2000) //so sleep for 5 seconds
    println(arrBuffer.size)

    val restaurants = arrBuffer.map(doc => docToRestaurant(doc)).toSeq
    restaurants.slice(0,5).foreach(println)

    val jsonLines = arrBuffer.map(doc => doc.toJson()).toSeq
    val jsonString = "[\n" + jsonLines.mkString(",\n") + "\n]" //quick and dirty solution to our json formattting proble
//    Utilities.saveString(jsonString, "./src/resources/restaurants.json") //this works fine
//    Utilities.saveLines(jsonLines, "./src/resources/restaurants.json", sep =",\n")
  client.close()
}
