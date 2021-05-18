import Crush.thisCrush
import com.github.valrcs.Utilities.getLinesFromFile

import scala.collection.mutable.ArrayBuffer

object CrushOptimized extends App {
  val crush1 = getLinesFromFile("src/resources/crush1.txt")
  val crush2 = getLinesFromFile("src/resources/crush2.txt")
  val crush3 = getLinesFromFile("src/resources/crush3.txt")
  val crush4 = getLinesFromFile("src/resources/crush4.txt")
//  crush1.foreach(println)
  val thisCrush = crush3

  //get n and m out
  val n = thisCrush.slice(0,1).mkString.split(" ")(0).toInt //how many zeros to begin with
  val m = thisCrush.slice(0,1).mkString.split(" ")(1).toInt //how many operations

  //val seq = (1 to n).map(_ => 0)
  val myArray = ArrayBuffer.fill(n+1)(0L) //we need an extra value for operations to avoid checking out of bounds index

  for (i <- 1 to m) {
    val currentLine = thisCrush.slice(i, i + 1).mkString.split(" ")
    val startFrom = currentLine(0).toInt
    val goTo = currentLine(1).toInt
    val addValue = currentLine(2).toLong

    //so instead of a loop we will simply store two values
//    println(s"BEFORE $myArray")
    myArray(startFrom-1) += addValue
    myArray(goTo) -= addValue
//    println(s"AFTER $myArray")
  }

  //TODO reconstruct the final values from our change values
  //we need to create a cumulative sum
  val realArray = ArrayBuffer.fill(n+1)(0L)
  realArray(0) = myArray(0)
  for (i <- 1 until myArray.size) {
    realArray(i) = myArray(i) + realArray(i-1)
  }
//  println(s"$realArray")
  println(realArray.max)
}
