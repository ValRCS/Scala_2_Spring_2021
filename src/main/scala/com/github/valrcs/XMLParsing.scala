package com.github.valrcs

import upickle.legacy.macroRW

import kantan.csv._
import kantan.csv.ops._
import kantan.csv.generic._

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
//  val files = bookXML \\ "file" //so we just take all file tags no matter how deep works but a bit loose
  val files = bookXML \ "files" \  "file" //this is more specific than the above
  println(s"We have information about ${files.length} files in our XML data")

  //File is a very common name but it is under our own package so no collision
  case class MyFile(id:Int,
                    versionId: Int,
                    uri: String,
                    publicName: String,
                    internalName: String,
                    fileAccessType: String, //type is already used by Scala
                    kind: String,
                    lastSaved: String,
                    date: String,
                    time: String,
                    sizeMB: Double)

  //REQUIRED in order to write File case class directly to JSON
  //https://www.lihaoyi.com/post/HowtoworkwithJSONinScala.html
  //unclear if start of fileRW has to match case class name
implicit val fileRW = upickle.default.macroRW[MyFile]
  //if you want to write other custom classes you have to do the same thing

  def parseFileSize(txt: String):Double = {
    val fileSizeRegex = raw"(\d+,?\d*)".r //we want some digits with optional comma and optional more digits
    val res = fileSizeRegex.findFirstIn(txt)
      .getOrElse("0") //so on mismatches we will report file size as zero
      //        .split(" ")(0)
      .replace(',', '.')
      .toDouble
    //for more complex matches we would probably want pattern matching here we have KB or MB(default)
    if (txt.contains("KB")) res/1000 else res
  }

  def parseToDate(txt: String):String = {
    val fileDateRegex = raw"(\d{2}\.\d{2}\.\d{4})".r //. represent any single character so we want to escape them if we want . specifically
    val date = fileDateRegex.findFirstIn(txt)
      .getOrElse("0")
    date
  }


  def parseToTime(txt: String):String = {
    val fileTimeRegex = raw"(\d{2}:\d{2}:\d{2})".r
    val time = fileTimeRegex.findFirstIn(txt)
      .getOrElse("0")
    time
  }

  def fromXMLtoFile(node: scala.xml.Node):MyFile = {
    val fields = node \ "fileMetadata" \ "field"
    val lastSavedText = fields.filter( _ \ "@name" exists (_.text == "Date last saved")).text //if we call it 3 times it might be more efficient
//    println(s"We have ${fields.length} fields")
//    fields.foreach(f => println(s"${f.attribute("name")} : ${f.text}"))
    MyFile(
      id = node.attribute("id").getOrElse("0").toString.toInt,
      versionId = node.attribute("digitalObjectVersionId").getOrElse("0").toString.toInt,
      uri = (node \ "uri").text,
      publicName = node.attribute("name").getOrElse("").toString,
      internalName = fields.filter(_ \ "@name" exists (_.text == "Name")).text,
      //https://stackoverflow.com/questions/7574862/scala-xml-get-nodes-where-parent-has-attribute-value-match/7577990
      fileAccessType = node.attribute("type").getOrElse("").toString,
      kind = fields.filter(n => n \ "@name" exists (_.text == "Type")).text,
      lastSaved = lastSavedText,
      date = parseToDate(lastSavedText),
      time = parseToTime(lastSavedText),
      //TODO get date and time separately
      sizeMB = parseFileSize(fields.filter(_ \ "@name" exists (_.text == "Size")).text)
    )
  }

  val fileSeq = files.map(f => fromXMLtoFile(f))
  println(s"We have gotten information on ${fileSeq.length} files")
  fileSeq.slice(0,5).foreach(println)

  //print only those files which have uri field of something
  fileSeq.filter(_.uri.nonEmpty).foreach(println)

  //TODO get me files over 5.5 MB in size
  fileSeq.filter(_.sizeMB > 5.5).foreach(println)

  //https://stackoverflow.com/questions/15259250/in-scala-how-to-get-a-slice-of-a-list-from-nth-element-to-the-end-of-the-list-w/15259268
  fileSeq.takeRight(4).foreach(println)

//  val topString = ujson.write(Seq("doh",1,3424,3.1466),  indent = 4)
//  val jsonString = ujson.write((List("Hello", "World")), indent = 4)
//  println(jsonString)
  val singleJson = upickle.default.write(fileSeq(0), indent = 4)
  println(singleJson)
  val filesJson = upickle.default.write(fileSeq, indent = 4)

  val saveJsonPath = "./src/resources/json/aija.json"
  Utilities.saveString(filesJson, saveJsonPath)

  // File in which we'll be writing the CSV data.
//  val out = java.io.File.createTempFile("./src/resources/csv/files.csv", "csv")
//  val fileEncoder: RowEncoder[MyFile] = RowEncoder.caseEncoder(0, 1, 2,3,4,5,6,7,8,9,10)(MyFile.unapply)
//  val writer = out.asCsvWriter[File](rfc.withHeader("id",
//    "versionId",
//    "uri",
//    "publicName",
//    "internalName",
//    "fileAccessType",
//    "kind",
//    "lastSaved",
//    "date",
//    "time",
//    "sizeMB"
//
//  ))
//  out.writeCsv(fileSeq).close() //FIXME see what is missing to write a collection of MyFile

}
