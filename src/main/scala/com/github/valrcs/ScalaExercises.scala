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
  val mySet = scala.collection.mutable.Set("Michigan", "Ohio", 12)
  println(mySet.size)
  mySet += ("Valdis") //can add single new item
  mySet ++= Seq("Apples", "Oranges") //can add multiples
  println(mySet)
  mySet -= "Ohio" //can remove singles from a set
  mySet --= Seq("Michigan", 12, 9000) //so no errors on removing nonexistent members of the set
  println(mySet)

  val mySet1 = Set("Michigan", "Ohio", "Wisconsin", "Iowa")
  val mySet2 = Set("Wisconsin", "Michigan", "Minnesota")
  val aNewSet = mySet1 intersect mySet2 // NOTE: You can use the "&" operator
  println(aNewSet)
  val aNewSet2 = mySet1 union mySet2 // NOTE: You can also use the "|" operator
  println(aNewSet2)
  val mySet3 = Set("Wisconsin", "Michigan")
  println(mySet2 subsetOf mySet1)
  println(mySet2.subsetOf(mySet1)) //full syntax, same as above line
  println(mySet3 subsetOf mySet1)

  val aNewDiffSet = mySet1 diff mySet2 // Note: you can use the "&~" operator if you *really* want to.
  //this is non symmetrical difference
  println(aNewDiffSet.equals(Set("Ohio", "Iowa")) )

  //tuples
  //https://www.scala-exercises.org/std_lib/tuples
  val student = ("Sean Rogers", 21, 3.5)
  val (name, age, gpa) = student //this is so called tuple unpacking
  println(student)
  val tuple2Swap = ("Apple", 3).swap //only for Tuple2 swap is available
  println(tuple2Swap)

  //https://www.scala-exercises.org/std_lib/lists
  val a = List(1, 2, 3)
  val b = List(1, 2, 3)
  println(a eq b) //tests if they are same object
  println(a == b) //tests if they have same contents

  val aList = List(1, 3, 5, 7, 9)
  val bList = aList.filterNot(v => v == 5)
  println(bList)
  //TODO write a filter which is same as filterNot above
  val cList = aList.filter(n => true)
  println(bList == cList) //should be true
}
