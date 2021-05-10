object HelloScala2 extends App {
  println("Welcome all to our Spring 2021 Scala course")
  val myNumbers = (1 to 100).toArray //if we had truly big range we might not want to convert to Array
  //range would be more efficient for memory and also a bit for speed
  val myBigNumbers = myNumbers.map(n => n*200)
  myBigNumbers.slice(0,10).foreach(println) //remember last index in slice is not included

}
