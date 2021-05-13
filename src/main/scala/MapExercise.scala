

import scala.collection.mutable.ArrayBuffer
import scala.util.Random

object MapExercise extends App {


  def genMap(testSize:Int = 5):Map[String,Int] = {
    (0 until testSize).map(n => n.toString -> Random.nextInt(testSize)).toMap
  }

  val testMap = genMap()
  //TODO write Map reversal function for key,value to value,key
  def reverseMap(map:Map[String,Int]):Map[Int,String] = {
    val newMap = scala.collection.mutable.Map[Int, String]() //this is overhead

    for ((key, value) <- map) {newMap(value) = key}
    val reversedMap: Map[Int,String] = newMap.toMap
    reversedMap
  }

  def reverseMap2(map:Map[String,Int]):Map[Int,String] = {
    val newMap = for ((k,v) <- map) yield (v,k)
    newMap
  }

  def reverseMap3(map:Map[String,Int]):Map[Int,String] = {
    val reversedMap = map.map(_.swap)
    reversedMap
  }

  val reversedMap: Map[Int, String] = reverseMap(testMap)
  val reversedMap2: Map[Int, String] = reverseMap2(testMap)
  val reversedMap3: Map[Int, String] = reverseMap3(testMap)
  reversedMap.foreach(println)
  println("*"*10)
  reversedMap2.foreach(println)
  println("*"*10)
  reversedMap3.foreach(println)
  //So for now we do not worry about duplicates
  //Think about how to solve the duplicate problem

  def testFunSpeed(myFun: Map[String,Int] => Map[Int,String], mapSize:Int = 20):Long ={
    val tMap = genMap(mapSize)
    val t0 = System.nanoTime()
    val reversedMap = myFun(tMap) //so we can the function we passed in and use it
    val t1 = System.nanoTime()
    t1 - t0
  }



  //so challenge would be how to reverse and keep all the values
  //since keys have to be unique we will use the duplicate values when we convert them to keys

  def fullReverse(map:Map[String,Int]):Map[Int, Seq[String]] = {
    val newMap = scala.collection.mutable.Map[Int, ArrayBuffer[String]]()
    for ((k,v) <- map) {
      if (newMap.contains(v)) {
        newMap(v) += k
      } else {
        newMap(v) = ArrayBuffer(k)
      }
    }
    val resultMap:Map[Int, Seq[String]] = newMap.map(t => t._1 -> t._2.toSeq).toMap //maybe there is a prettier way
//    val resultMap:Map[Int, Seq[String]] = newMap.map((k,v) => k -> v.toSeq).toMap //maybe there is a prettier way
    //so in comes Map("foo" -> 42, "bar" -> 42)
//    Map(42 -> Seq("foo","bar"))
    resultMap
  }
  val tenMap = genMap(10)
  val reversedTenMap = fullReverse(tenMap)
  tenMap.foreach(println)
  println("*"*20)
  reversedTenMap.foreach(println)

  val bigMap = genMap(10000)
  val reversedBigMap = fullReverse(bigMap)
  bigMap.toSeq.slice(0,10).foreach(println)
  reversedBigMap.toSeq.slice(0,10).foreach(println)

  def testBigFunSpeed(myFun: Map[String,Int] => Map[Int,Seq[String]], mapSize:Int = 20):Long ={
    val tMap = genMap(mapSize)
    val t0 = System.nanoTime()
    val reversedMap = myFun(tMap) //so we can the function we passed in and use it
    val t1 = System.nanoTime()
    t1 - t0
  }

  val testSize = 100
  val mapSize = 100000
  val resultsBuf = ArrayBuffer[Long]()
  resultsBuf += (0 until testSize).map(_ => testFunSpeed(reverseMap, mapSize)).sum
  resultsBuf += (0 until testSize).map(_ => testFunSpeed(reverseMap2, mapSize)).sum
  resultsBuf += (0 until testSize).map(_ => testFunSpeed(reverseMap3, mapSize)).sum
  resultsBuf += (0 until testSize).map(_ => testBigFunSpeed(fullReverse, mapSize)).sum
  //we could finalize the buffer and convert to Sequence type or Vector here
  resultsBuf.zipWithIndex.foreach(t => println(s"Speed for result No. ${t._1} is ${t._2}"))
}
