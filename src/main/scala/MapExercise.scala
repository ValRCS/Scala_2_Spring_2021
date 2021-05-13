

import scala.util.Random

object MapExercise extends {

  def genMap(testSize:Int = 20):Map[String,Int] = {
    (0 until testSize).map(n => n.toString -> Random.nextInt(testSize)).toMap
  }

  val testMap = genMap()
  //TODO write Map reversal function for key,value to value,key
  def reverseMap(map:Map[String,Int]):Map[Int,String] = {
    Map(5 -> "Fix this")
  }

  val reversedMap: Map[Int, String] = reverseMap(testMap)

  //So for now we do not worry about duplicates
  //Think about how to solve the duplicate problem
}
