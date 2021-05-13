package com.github.valrcs

import scala.util.Random

object Sequences extends App {
  val testSize = 1000
  val numTests = 100

  val arrayNumbers = (1 to testSize).map(n => Random.nextInt(testSize)).toArray
  val seqNumbers = (1 to testSize).map(n => Random.nextInt(testSize)).toArray.toSeq //range already is a Sequence so we converted to Java array and then to Seq
  val indexedNumbers = (1 to testSize).map(n => Random.nextInt(testSize))//range already is a Sequence so we converted to Java array and then to Seq
  val listNumbers = (1 to testSize).map(n => Random.nextInt(testSize)).toList //we can type cast directly since range is not List
  val vectorNumbers = (1 to testSize).map(n => Random.nextInt(testSize)).toVector

  def testSeq(data:Seq[Int]):Long = {
    val middle = testSize / 2
    val t0 = System.nanoTime()
//    val sum = data.sum //loop is hidden here
    val sum = data.head + data(middle) + data.last //we would epect for the list to win here since it should be double linked list so head and tail is always available
    val t1 = System.nanoTime()
    val delta = t1 - t0
//    println(s"Test Sequence executed in ${delta/1000} microseconds")
    delta
  }
  val arrayRes = (1 to numTests).map(n => testSeq(arrayNumbers)).sum
  val seqRes = (1 to numTests).map(n => testSeq(seqNumbers)).sum
  val indexedRes = (1 to numTests).map(n => testSeq(indexedNumbers)).sum
  val listRes = (1 to numTests).map(n => testSeq(listNumbers)).sum
  val vectorRes = (1 to numTests).map(n => testSeq(vectorNumbers)).sum

  println(s"array ${arrayRes/numTests}")
  println(s"seq ${seqRes/numTests}")
  println(s"indexed ${indexedRes/numTests}")
  println(s"list ${listRes/numTests}")
  println(s"vector ${vectorRes/numTests}")

}
