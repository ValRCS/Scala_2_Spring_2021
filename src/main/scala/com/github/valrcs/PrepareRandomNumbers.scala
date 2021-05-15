package com.github.valrcs

import scala.util.Random

object PrepareRandomNumbers extends App {

  val r = new Random
  r.setSeed(42) //any number will do, basically this will set specific pseudo-random sequence
  val numCount = 950
  val maxNum = 1000
  val savePath = "./src/resources/numbers.txt"
  println(s"Will generate $numCount numbers up to $maxNum and save in $savePath")
//  val numbers = (1 to numCount).map(_ => r.nextInt(maxNum).toString)
  val numbers = (1 to numCount).map(_ => r.nextInt(maxNum))
  Utilities.saveIntSeq(numbers, destPath = savePath)
}
