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
  println(note502)
  println(note502.head \ "@id") //so we can get the id value
  val n502id = (note502.head \ "@id" ).text //this would be "" if no id attribute nodes are found

  case class Message(id:Int, to:String,from: String, heading:String, body:String, footer: String, main: String)

  def fromXML(node: scala.xml.Node):Message = {
    Message (
      node.attribute("id").getOrElse("0").toString.toInt, //if we have no id, well then we give "0"
//      (node \ "@id").text.toInt, //this attribute selector would work if we have this attribute
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

  val url = "https://dom.lndb.lv/data/obj/656227.xml"
  val savePath = "./src/resources/xml/aija.xml"
//  Utilities.saveUrlToFile(url, savePath) //we did it once no need for it again

//  val bookXML = XML.load(url) //this hits LNB server which is fine but no need to do it constantly
  val bookXML = XML.load(savePath) //no need to hit library servers unless we need a fresh copy
  val files = bookXML \\ "file" //so we just take file tag
  println(s"We have information about ${files.length} files in our XML data")

  //File is a very common name but it is under our own package so no collision
  case class File(id:Int,
                  versionId: Int,
                  uri: String,
                  publicName: String,
                  internalName: String,
                  fileAccessType: String, //type is already used by Scala
                  kind: String,
                  lastSaved: String,
                  sizeMB: Double)

  def fromXMLtoFile(node: scala.xml.Node):File = {
    val fields = node \ "fileMetadata" \ "field"
//    println(s"We have ${fields.length} fields")
//    fields.foreach(f => println(s"${f.attribute("name")} : ${f.text}"))
    val fileSizeRegex = raw"(\d+,?\d*)".r //we want some digits with optional comma and optional more digits

    File(
      id = node.attribute("id").getOrElse("0").toString.toInt,
      versionId = node.attribute("digitalObjectVersionId").getOrElse("0").toString.toInt,
      uri = (node \ "uri").text,
      publicName = node.attribute("name").getOrElse("").toString,
      internalName = fields.filter(_ \ "@name" exists (_.text == "Name")).text,
      //https://stackoverflow.com/questions/7574862/scala-xml-get-nodes-where-parent-has-attribute-value-match/7577990
//      kind = fields.filter(n => n.attribute("name").contains("Kind")).text,
      fileAccessType = node.attribute("type").getOrElse("").toString,
      kind = fields.filter(n => n \ "@name" exists (_.text == "Type")).text,
      lastSaved = fields.filter( _ \ "@name" exists (_.text == "Date last saved")).text,
//      sizeMB = fields.filter(_ \ "@name" exists (_.text == "Size")).text
      sizeMB = fileSizeRegex.findFirstIn(fields.filter(_ \ "@name" exists (_.text == "Size")).text)
        .getOrElse("0") //so on mismatches we will report file size as zero
//        .split(" ")(0)
        .replace(',', '.')
        .toDouble
    )
  }

  val fileSeq = files.map(f => fromXMLtoFile(f))
  println(s"We have gotten information on ${fileSeq.length} files")
  fileSeq.slice(0,5).foreach(println)

  //print only those files which have uri field of something
  fileSeq.filter(_.uri.nonEmpty).foreach(println)

  //TODO get me files over 10 MB in size

}
