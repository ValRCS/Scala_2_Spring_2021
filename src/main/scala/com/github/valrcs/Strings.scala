package com.github.valrcs

//Strings is cutting it a bit too close to String type...
object Strings extends App {
  val n = 20000
  val tokens = Seq("dog", "cat", "eat", "bite")

  //lets test how fast/slow is building of new strings
  var text = ""
  val t0 = System.nanoTime()
  for (_ <- 1 to n) { //we are using counter for repeat so we do not need to name our iterable
    text += tokens(1) //this is going to get very slow for larger strings it https://www.joelonsoftware.com/2001/12/11/back-to-basics/
  }
  val t1 = System.nanoTime()

  //hypothesis is that this should be quite a bit faster
  val textBuffer = new StringBuilder //we have immutable buffer which can be modified
  //this means that val specifies that we are not changing that particular container, insides can be mutable thats what Buffer types do
  val t2 = System.nanoTime()
  for (_ <- 1 to n) {
    textBuffer.append(tokens(1))
  }
  val t3 = System.nanoTime()

  val textDelta = t1-t0
  val textBufferDelta = t3-t2
  println(s"textDelta:\t$textDelta")
  println(s"textDelta:\t$textBufferDelta")
  println(s"Ratio of text vs. textBuffer time spent: ${textDelta/textBufferDelta}")

}
