import com.github.valrcs.Utilities.getLinesFromFile

import scala.collection.mutable.ArrayBuffer

object Crush extends App {
  //TODO Solve the following problem
  //https://www.hackerrank.com/challenges/crush/problem
  //TODO read and parse ../resources/crush1.txt
  //TODO read and parse ../resources/crush2.txt
  //TODO read and parse ../resources/crush3.txt
  //TODO read and parse ../resources/crush4.txt
  //you need to perform 3 operations
  //and find the maximum in the new array

  //think how well this would work on large values of operations like
  //1000 1000
//  val seq = (1 to 5).map(_ => 0)
//  val myArray = ArrayBuffer.fill(5)(0)
//  println(seq)
////  seq(3) = 555
////  myArray(3) = 777
//  //1 2 100
//  myArray(1-1) += 100
//  myArray(2-1) += 100
//  //2nd instruction 2 5 100
//  myArray(2-1) += 100
//  myArray(3-1) += 100
//  myArray(4-1) += 100
//  myArray(5-1) += 100
//  //3rd instruction for crush
//  myArray(3-1) += 100
//  myArray(4-1) += 100
//
//  println(myArray)
//  println(myArray.max)
  //TODO do this brute force solution by loops
  val crush1 = getLinesFromFile("src/resources/crush1.txt")
  val crush2 = getLinesFromFile("src/resources/crush2.txt")
  val crush3 = getLinesFromFile("src/resources/crush3.txt")
  val crush4 = getLinesFromFile("src/resources/crush4.txt")

  //here choose which input file to work with
  val thisCrush = crush3

  //get n and m out
  val n = thisCrush.slice(0,1).mkString.split(" ")(0).toInt //how many zeros to begin with
  val m = thisCrush.slice(0,1).mkString.split(" ")(1).toInt //how many operations


  //TODO do this brute force solution by loops

  //val seq = (1 to n).map(_ => 0)
  val myArray = ArrayBuffer.fill(n)(0)
  //println(seq)
  for (i <- 1 to m) {
    val currentLine = thisCrush.slice(i,i+1).mkString.split(" ")
    val startFrom = currentLine(0).toInt
    val goTo = currentLine(1).toInt
    val addValue = currentLine(2).toInt
    //extracts data from each subsequent input line
    //println(s"line $i -> $startFrom, $goTo, $addValue")

    for (j <- startFrom-1 to goTo-1) {
      myArray(j) += addValue
    }

  }

  println(myArray)
  println(myArray.max)


  //TODO think if you really need that many loops ...

  //TODO think if you really need that many loops ...

}
