package com.github.valrcs
import org.mongodb.scala.MongoClient
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._

object MongoDBConnection extends App {
  println("Testing MongoDB database")

  val user = "needsuser" //FIXME do not submit real pw to GIT!!!!
  val pw = "needspw"
  val dbname = "sample_restaurants"

  //uri - universal resource identificator
  val uri: String = s"mongodb+srv://$user:$pw@cluster0.qabwl.mongodb.net/$dbname?retryWrites=true&w=majority"
  System.setProperty("org.mongodb.async.type", "netty")
  val client : MongoClient  = MongoClient(uri)
  val db: MongoDatabase = client.getDatabase(dbname)
}
