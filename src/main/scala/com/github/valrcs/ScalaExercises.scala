package com.github.valrcs

object ScalaExercises extends App {
  val myMap = Map("MI" -> "Michigan", "OH" -> "Ohio", "WI" -> "Wisconsin", "MI" -> "Michigan")
  val aNewMap = myMap + ("IL" -> "Illinois") //memory wise for big maps you would want to use mutable map
  println(aNewMap.contains("IL"))
  val newAnyMap = scala.collection.mutable.Map[Any, Any]() //for those cases when we do not know types in advance
  newAnyMap += ("Name" -> "Valdis")
  newAnyMap += ("Height" -> 180)
  newAnyMap += (900 -> "Alarm")
  newAnyMap += (9000 -> "Level")
  newAnyMap.foreach(println)
  newAnyMap -= "Name" //removing key value pair from our Map same
//  newAnyMap --= List(900, 9000)
  newAnyMap --= Seq(900, 9000) //List is just a specific type of Seq
  newAnyMap --= Seq("some key", 50000) //Graceful removal of non existing keys
  println("*"*20)
  newAnyMap.foreach(println)

  //prefer specific types but it is possible to create sets of Any types
  val mySet = Set("Michigan", "Ohio", 12)
  println(mySet.size)


}
