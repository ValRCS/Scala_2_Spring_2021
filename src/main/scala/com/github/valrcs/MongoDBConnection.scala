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

//  val user = "reader" //even user name should not be submitted to git
  val user = "readWriter" //even user name should not be submitted to git
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
  //this is a command it does return an observable if you want to monitor creation
 // db.createCollection("good_restaurants") //TODO why creation is not working, we have to do it manually in MongoDB atlas cloud

  //https://mongodb.github.io/mongo-scala-driver/1.0/reference/observables/
//  collection.find(equal("name", "456 Cookies Shop")).subscribe((doc: Document) => println(doc.toJson()),
//    (e: Throwable) => println(s"There was an error: $e"),
//    () => println("Completed!"))
//  collection.find(equal("borough", "Brooklyn")).subscribe((doc: Document) => println(doc.toJson()),
//    (e: Throwable) => println(s"There was an error: $e"),
//    () => println("Completed!"))
  val arrBuffer = ArrayBuffer[Document]() //Document comes from our MongoDB library

//  collection.find(
//    and(
//    equal("borough", "Brooklyn"),
//    equal("cuisine","Polish")))
//    .subscribe((doc: Document) => {
//      println(doc.toJson())
//      arrBuffer += doc
//    },
//    (e: Throwable) => println(s"There was an error: $e"),
//    () => println("Completed!"))

//dirty restaurants {"grades.0.grade":"C","grades.0.score":{$gt:37} }
  //good restaurants {"grades.0.grade":"A","grades.0.score":{$gt:0, $lt:3} }
  collection.find(
    and(
      equal("grades.0.grade", "A"),
      lt("grades.0.score",3),
      gt("grades.0.score",0)
    ))
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
  //FIXME finish retrieving all values for this method
    def docToRestaurant(doc:Document) :Restaurant = {
      //TODO add more values
      val name = doc.getOrElse("name","NoNameRestaurant").asString().getValue()
      val cuisine = doc.getOrElse("cuisine","noCuisine").asString().getValue()
      val borough = doc.getOrElse("borough","noBorough").asString().getValue()
      //default for street should be blank document, but since string is valid json this still works
      val street = doc.getOrElse(key = "address", "nothing")
        .asDocument().getOrElse(key="street", "noStreet")
        .asString().getValue()
      val building = ""//TODO
      val zipcode = ""//TODO
      //of course there is a possibility of index not existing in get(0)
      val longitude = doc.getOrElse(key = "address", "nothing")
        .asDocument()
        .getOrElse("coord",Seq(0.0,0.0))
        .asArray().get(0)
        .asDouble() //mongoDb library methods
        .doubleValue() //this is the Scala approach the last 2
      val latitude = 0L //TODO
      val curGrade = "FF" //TODO
      val curScore = doc.getOrElse("grades",Seq(0,0))
        .asArray().get(0).asDocument()
        .getOrElse("score", -2)
        .asInt32()
        .intValue()
      Restaurant(name, cuisine, borough, street, building, zipcode, longitude, latitude, curGrade, curScore)
    }

  //notice again how there is no enforcement of strict schema, I can put whatever I need inside (so plus and minus)
  def restaurantToDoc(restaurant: Restaurant): Document = {
    Document("name" -> restaurant.name ,
      "borough" -> restaurant.borough,
      "address" -> Document("phone" -> "228-555-0149",
        "email" -> "cafeconleche@example.com",
        "location" -> Seq(restaurant.latitude, restaurant.longitude)),
      "latestGrade" -> restaurant.curGrade,
      "latestScore" -> restaurant.curScore,
      "cuisine" -> restaurant.cuisine)
  }

//  brooklyn_chinese.foreach(println)
    sleep(2000) //so sleep for 5 seconds


  println(arrBuffer.size)

    val goodRestaurantCollection = db.getCollection("good_restaurants")
//    goodRestaurantCollection.insertOne(arrBuffer(0)).subscribe(res => println(res),
//      (e: Throwable) => println(s"There was an error when inserting data: $e"),
//      () => println("Completed insertion or not!"))
  //what happens when we try to insert some document with identical keys?
  //we can't insert anything
//    goodRestaurantCollection.insertMany(arrBuffer.slice(1,10).toSeq).subscribe(res => println(res),
//      (e: Throwable) => println(s"There was an error when inserting tons of data: $e"),
//      () => println("Completed insertion!"))



    val restaurants = arrBuffer.map(doc => docToRestaurant(doc)).toSeq
    restaurants.slice(0,5).foreach(println)

    val restaurantDocuments = restaurants.map(restaurant => restaurantToDoc(restaurant))
  goodRestaurantCollection.insertMany(restaurantDocuments.slice(0,5)).subscribe(res => println(res),
        (e: Throwable) => println(s"There was an error when inserting tons of data: $e"),
        () => println("Completed insertion!"))


    val jsonLines = arrBuffer.map(doc => doc.toJson()).toSeq
    val jsonString = "[\n" + jsonLines.mkString(",\n") + "\n]" //quick and dirty solution to our json formattting proble
//    Utilities.saveString(jsonString, "./src/resources/good_restaurants.json") //this works fine
//    Utilities.saveLines(jsonLines, "./src/resources/restaurants.json", sep =",\n")
  client.close()
}
