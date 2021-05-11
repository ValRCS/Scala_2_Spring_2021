
import com.github.valrcs.Utilities

object SparseArrays extends App {
  // https://www.hackerrank.com/challenges/sparse-arrays/problem
  //TODO read and parse ../resources/arr1.txt
  //TODO read and parse ../resources/arr2.txt
//  val filePath1 = "./src/resources/arr1.txt" //these we could read from argparse as well
//  val filePath2 = "./src/resources/arr2.txt"
  val folderName = "./src/resources"

  //this is a way to generate some strings with some number in middle
  def getFilePaths(beg: Int, end:Int, prefix: String="./src/resources/arr", suffix: String=".txt"):Array[String] = {
    (beg to end).map(num => s"$prefix$num$suffix").toArray //remember to INCLUDES the end value, until DOES NOT
  }
//  val filePaths = getFilePaths(1,3) //this will get us file names which are similar just differ by some number in middle
//FIXME  val filePaths = Utilities.getFileNames(folderName, prefix = "arr", suffix = "txt") //it is already default .txt which is fine
//  val filePaths = Utilities.getFileNames(folderName, suffix = "xt") //it is already default .txt which is fine
  val filePaths = Utilities.getFileNames(folderName, prefix="ar", suffix = "xt")
  filePaths.foreach(println)
  //in fact i could create the above file paths programmatically with a loop, if I had many similar files to process
  //return (println) the results
//  val lines = Utilities.getLinesFromFile("./src/resources/arr1.txt")

  def getAnswers(lines: Array[String]): Array[Int] = {
    val numbOfString = lines(0).toInt
    val stringEnd = numbOfString+1
    val strings = lines.slice(1, stringEnd)
    val queries = lines.slice(stringEnd+1, lines.length)
    //we do not even need the number of queries we just go until end

    //for debugging better to use syslog on large projects
//    strings.foreach(println)
//    println("*"*20)
//    queries.foreach(println)
    //now we simply count occurrence for each query in our queries

    //so we make two anonymous functions one for map and one for count
    val answers = queries.map(qry => strings.count(entry => entry == qry))
    //TODO think about how complex the above operation is?
    //That how many operations will be needed to perform the above calculation
    //Answer: the above is like two nested loops - loop within loop
    //Meaning O(queries*strings) so if we had 1000 strings and 500 queries we'd need 500k operations
    //for truly large date we could start thinking if there is a possibility of some optimization

//    answers.foreach(println)
    answers
  }

  def getAnswersFromFileName(filePath:String):Array[Int] ={
    val lines = Utilities.getLinesFromFile(filePath)
    getAnswers(lines)
  }

  def printAnswers(answers: Array[Int]):Unit = {
    answers.foreach(println) //I could adjust formatting without breaking code otherwise
  }

//  printAnswers(getAnswersFromFileName(filePath1))
//  println("*"*20)
//  printAnswers(getAnswersFromFileName(filePath2))

  //so we loop through each entry in our filePaths
  filePaths.foreach(filePath => printAnswers(getAnswersFromFileName(filePath)))

}
