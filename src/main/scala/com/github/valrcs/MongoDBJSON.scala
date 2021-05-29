package com.github.valrcs

import org.bson.BSONEncoder
import org.mongodb.scala.{Document, MongoClient, MongoDatabase}

import java.lang.Thread.sleep

object MongoDBJSON extends App {
  val jsonFile = "./src/resources/restaurants.json"

  val user = "readWriter" //even user name should not be submitted to git
  val pw = Some(scala.util.Properties.envOrElse("MONGOPW", "nosuchsecret")).getOrElse("nothing") //TODO remember do not submit real pw to GIT!!!!
  val dbname = "sample_restaurants"
  val collectionName = "from_json_texts"

  println(s"Will read $jsonFile and add it to $collectionName")
  val jsonStrings = Utilities.getLinesFromFile(jsonFile).tail.init //so everything but head and last //we could have used slice
//  jsonStrings.foreach(println)
  val jsonStringsTrim = jsonStrings.map(txt => txt.replaceAll(",$", "")) //$ means end of the line
  jsonStringsTrim.foreach(println)


  //uri - universal resource identificator
  //make sure when connectiong that cluster0......net is correct for your databas
  val uri: String = s"mongodb+srv://$user:$pw@cluster0.whr5v.mongodb.net/sample_restaurants?retryWrites=true&w=majority"
  System.setProperty("org.mongodb.async.type", "netty")
  val client : MongoClient  = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase(dbname)
  val collection = db.getCollection(collectionName)

  val doc: Document = org.bson.Document.parse(jsonStringsTrim.head).toBsonDocument
  println(doc)
  def txtToDoc(txt: String):Document = org.bson.Document.parse(txt).toBsonDocument

//  collection.insertOne(doc).subscribe(res => println(res),
//          (e: Throwable) => println(s"There was an error when inserting data: $e"),
//          () => println("Completed insertion or not!"))
  //tail because we already inserted first element above
  val docs = jsonStringsTrim.tail.map(txtToDoc) //same as .map(it => txtToDoc(it))

      collection.insertMany(docs).subscribe(res => println(res),
        (e: Throwable) => println(s"There was an error when inserting tons of data: $e"),
        () => println("Completed insertion!"))

  sleep(1000)
  client.close()
}
