import com.github.valrcs.Utilities.getLinesFromFile

object FindFrequency extends App {
  val srcPath = "./src/resources/numbers.txt"
  val uniquePath = "./src/resources/uniques.txt"
  val frequencyPath = "./src/resources/frequencies.txt"
  println(s"Find unique numbers in $srcPath")
  //TODO find uniques -> save to $uniques
  //TODO save number usage frequencies in the following format
  //0 2
  //1 3
  //4 1
  //9 6 times etc

  //HINTS we can use Map
  //Also groupBy might help

  val numbers = getLinesFromFile(srcPath)
  val uniqueNumbers = numbers.toSet.toSeq //we can find unique numbers in our set
  com.github.valrcs.Utilities.saveLines(uniqueNumbers, uniquePath)

  //1, 10, 2 in string sort instead of 1,2,10 in int sort

  val groupedNumbers = numbers.groupBy(identity).view.mapValues(_.size) //view is added for optimization so we do not
//  groupedNumbers.toSeq.slice(0,10).foreach(println)
  val groupedStrings = groupedNumbers.map(item => s"${item._1} ${item._2}").toSeq
  com.github.valrcs.Utilities.saveLines(groupedStrings, frequencyPath)

  println(s"Unique numbers (Set) is ${uniqueNumbers.size}")
  println(s"Grouped numbers (groupBy) is ${groupedNumbers.size}")

  //how about sorting?
  //we cant sort a generic map, so i'd suggest a sequence
  val groupedNumberSeq = groupedNumbers.toSeq
  groupedNumberSeq.slice(0,5).foreach(println)
  val sortedFrequencies = groupedNumberSeq.sortBy(it => it._2).reverse
  val sortedFrequencies2 = groupedNumberSeq.sortBy(it => it._2)(Ordering[Int].reverse) //this should be faster than line above
  //why? because we are not doing the reversal we sort by the reverse condition
  sortedFrequencies.slice(0,5).foreach(println)
  sortedFrequencies2.slice(0,5).foreach(println)

  //now let's see how we could sort by multiple conditions this will be both ascending
  val groupedNumbersInts = groupedNumberSeq.map(it => (it._1.toInt, it._2))
  val sortedFrequenciesAndNumbers = groupedNumbersInts.sortBy(r => (r._2, r._1))
  sortedFrequenciesAndNumbers.slice(0,5).foreach(println)
  //rows.sortBy(r => (r.lastName, r.firstName))( Ordering.Tuple2(Ordering.String.reverse, Ordering.String) )
  val sortedFrequenciesDescAsc = groupedNumbersInts.sortBy(r => (r._2, r._1))( Ordering.Tuple2(Ordering.Int.reverse, Ordering.Int) )
  sortedFrequenciesDescAsc.slice(0,5).foreach(println)

  //we could have made our own counter (if did not know about groupBy) , this naive counter might be faster
  def countNumbers(seq: Seq[Int], maxInt: Int): Seq[Int] = {
//    val counterSeq = (0 until maxInt).map(_ => 0) //there is also fill
    val counterSeq = Array.fill(maxInt)(0) //remember maxInt should be max Number in our sequence
    seq.foreach(n => counterSeq(n)+=1)
    counterSeq
  }
  def intNumbers = numbers.map(_.toInt)
  val counter = countNumbers(intNumbers, 1000)
  counter.slice(0, 20).foreach(println)
//  val counterMap = (counter zipWithIndex).toMap
  val counterMapZip = counter.zipWithIndex.toMap //same as above
  counterMapZip.toSeq.slice(0,10).foreach(println)

  //general counter would be create a Map and just count things there


  def countAnything(seq:Seq[Any]):Map[Any,Int] = {
    val counterMap = scala.collection.mutable.Map[Any, Int]()
    for (it <- seq) {
      if (counterMap contains it) counterMap(it) += 1
      else counterMap(it) = 1
    }
    counterMap.toMap //we return immutable 99% of time
  }
  val genericCount = countAnything(Seq("Dog",1,3,6,"Valdis", "dog","cat","dog",3))
  println(genericCount)
  val genericNumberCount = countAnything(numbers)
  genericNumberCount.toSeq.slice(0,10).foreach(println)
}
