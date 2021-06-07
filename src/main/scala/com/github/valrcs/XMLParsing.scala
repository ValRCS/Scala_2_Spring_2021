package com.github.valrcs

import scala.xml.XML

object XMLParsing extends App {
  println("Let's parse some XML") //parsing meaning converting XML semi structure into our own data structures
  val relativePath = "./src/resources/xml/note.xml"
  val xmlTopElem = XML.loadFile(relativePath)
  println(xmlTopElem)
  println("Let's break down the XML a bit")
  val childXML = xmlTopElem \ "heading" //  \ refers to direct child
  childXML.foreach(println)

  val relativePath2 = "./src/resources/xml/kareivis.xml" //TODO better name for the value
  val xmlTop = XML.loadFile(relativePath2)
  val paragraphs = xmlTop \ "P" // our selector is case sensitive after all

  //notice that par.text acutally includes the children text as well
  paragraphs.foreach(par => println(s"paragraph is ${par.text.length} characters long and starts with ${par.text.slice(0,30)}"))

}
