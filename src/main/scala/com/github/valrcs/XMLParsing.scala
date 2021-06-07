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
  val strongElems = xmlTop \ "P" \ "Strong" //of course it would be better to have same case for all tags
  strongElems.foreach(println)

  // so \\ will work on any depth, you have to be a bit more careful but very nice for looser XML
  val strongElemsAnyDepth = xmlTop \\ "Strong" //so \\ should pick up top level and 3 levels deep as well
  strongElemsAnyDepth.foreach(println)

  val messagesPath = "./src/resources/xml/messages.xml"
  val messagesXML = XML.loadFile(messagesPath)
  val notes = messagesXML \ "note"
  println(s"We have ${notes.length} notes with contents ${notes.text}")
  println(notes(1)) //not recommended because  you might not have 2 notes!
  notes.foreach(el => println(el.attribute("id"))) //we might not have attribute so we get Some()
  val note502 = notes.filter(el => el.attribute("id").getOrElse("").toString == "502") //TODO how to avoid ToString
  print(note502)

  case class Message(id:Int, to:String,from: String, heading:String, body:String, footer: String, main: String)

  def fromXML(node: scala.xml.Node):Message = {
    Message (
      node.attribute("id").getOrElse("0").toString.toInt, //if we have no id, well then we give "0"
      (node \ "to").text, //if these child elements do not exist, we will just get nothing ""
      (node \ "from").text,
      (node \ "heading").text,
      (node \ "body").text, //if we have multiple children we will get text for all
      (node \ "footer").text,
      (node \ "body" \ "main").text
    )
  }
  println("\nMy Messages:")
  val messages = notes.map(el => fromXML(el)) //now we have our own internal data structure where we can do whatever we want
  messages.foreach(println)

  //filter those messages with id over 502
  val over502 = messages.filter(msg => msg.id > 502)
  println(over502)
}
