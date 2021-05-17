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
  val seq = (1 to 5).map(_ => 0)
  val myArray = ArrayBuffer.fill(5)(0)
  println(seq)
//  seq(3) = 555
//  myArray(3) = 777
  //1 2 100
  myArray(1-1) += 100
  myArray(2-1) += 100
  //2nd instruction 2 5 100
  myArray(2-1) += 100
  myArray(3-1) += 100
  myArray(4-1) += 100
  myArray(5-1) += 100
  //3rd instruction for crush
  myArray(3-1) += 100
  myArray(4-1) += 100

  println(myArray)
  println(myArray.max)
  //TODO do this brute force solution by loops

  //TODO think if you really need that many loops ...

}
