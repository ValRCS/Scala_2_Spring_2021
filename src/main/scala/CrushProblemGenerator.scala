import com.github.valrcs.Utilities

import scala.util.Random

object CrushProblemGenerator extends App {
  val n = args(0).toInt
  val m = args(1).toInt
  val k = args(2).toInt
  val fileName = args(3)
  println(s"Will generate $m rows of instructions for sequence of $n zeros, largest value to add is $k, save File to $fileName")

  val myBuffer = scala.collection.mutable.ArrayBuffer[String]()
  myBuffer.addOne(s"$n $m")

  val r = Random
  val maxA = n / 2
  val maxB = n
  val myNumbers = (1 to m).map(n => s"${r.nextInt(maxA)+1} ${r.between(maxA, maxB)} ${r.nextInt(k)}") //potentially we could have entery with a and b matching

  myBuffer.addAll(myNumbers) //might not be the most efficient

  Utilities.saveLines(myBuffer.toSeq, fileName)
}
