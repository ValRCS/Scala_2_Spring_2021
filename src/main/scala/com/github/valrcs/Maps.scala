package com.github.valrcs

import com.github.valrcs.Maps.testSize

import scala.collection.immutable
import scala.collection.mutable.HashMap
import scala.util.Random

object Maps extends App {
  println("Testing Map type performance")
  val testSize = 15
  val numTests = 100
  val myMap = Map("A"-> 1, "B" -> 2,"C"->1, "A"-> 2) //so during construction the last key - value pair wins
  println(myMap("A"))
  for ((k,v) <- myMap) println(s"KEY: $k -> VALUE: $v") //unpacking key value tuple in each iteration
  //so keys are generally strings, but they could be integers they could be even tuples - one requirement is that keys are immutable
  //so Maps you have very fast access(almost as fast as in Sequences by numeric index) to value by key

//  val baseSeq = for (n <- 0 until testSize) yield n.toString -> n
//  val baseMap = baseSeq.toMap //this works but lets do a one liner
  val baseMap = (0 until testSize).map(n => n.toString -> n).toMap
//  baseMap.foreach(println)

  def genMap(testSize:Int = testSize):Map[String,Int] = {
    (0 until testSize).map(n => n.toString -> Random.nextInt(testSize)).toMap
  }

  //TODO see if we can generate a Java HashMap on the fly
//  def genImmutableHashMap(testSize:Int = testSize):immutable.HashMap[String,Int] = {
//    val myMap = (0 until testSize).map(n => n.toString -> Random.nextInt(testSize)).toMap
//    val hashMap = new immutable.HashMap[String,Int](myMap)
//    hashMap
//  }

  def genHashMap(testSize:Int = testSize):HashMap[String,Int] = {
//    (0 until testSize).map(n => n.toString -> Random.nextInt(testSize)).toMap.asInstanceOf[HashMap]
    val myHashMap = HashMap[String,Int]()
    for (n <- 0 until testSize) {
      myHashMap += (n.toString -> Random.nextInt(testSize)) //this should be slower than our genMap map creation
    }
    myHashMap
  }

  def measureHashMap(genMap: HashMap[String,Int]):Long = {
    val t0 = System.nanoTime()
    //    val sum = genMap.map(it => it._2).sum
    val sum = genMap.values.sum //we gain about 30% speed increase by going to values instead of using map
    val t1 = System.nanoTime()
    val delta = t1 - t0
    //    println(s"Nanoseconds expired: $delta")
    delta
  }

  def measureMap(genMap: Map[String,Int]):Long = {
    val t0 = System.nanoTime()
//    val sum = genMap.map(it => it._2).sum
    val sum = genMap.values.sum //we gain about 30% speed increase by going to values instead of using map
    val t1 = System.nanoTime()
    val delta = t1 - t0
//    println(s"Nanoseconds expired: $delta")
    delta
  }
  val genMapTime = (0 until numTests).map(_ => measureMap(genMap())).sum
  println(s"$numTests tests took $genMapTime nanoseconds")
  val genHashMapTime = (0 until numTests).map(_ => measureHashMap(genHashMap())).sum
  println(s"$numTests tests took $genHashMapTime nanoseconds")

}
