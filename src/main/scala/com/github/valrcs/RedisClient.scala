package com.github.valrcs
import com.redis._

object RedisClient extends App {
  println("Testing Connection to Redis")
  //we just a need a database first :)
  val port = 10283
  val db = "FunStoreNov20" //this also could be read from enviroment variables
  val secret = Some(scala.util.Properties.envOrElse("REDISPW", "nosuchsecret"))

  val host = scala.util.Properties.envOrElse("REDISHOST", "no such host") //of course care must be taken for name colission
  println(s"My host is $host")

  //TODO move to enviroment
  val r = new RedisClient(host, port, 0, secret)

  r.set("name", "Valdis")
  r.incr(key = "visits")

  println(r.get("name"))
  val visits = r.get(key="visits")
  println(s"Number of visits: $visits")

  val keys = r.keys().getOrElse(List[String]()) //on empty database we get empty List of Strings
//  keys.foreach(println)
  keys.foreach(key => println(s"Key $key type is ${key.getClass})")) // each key is wrapped in Some
//  keys.foreach(key => println(s"Key $key type is ${key.getOrElse("empty key")})"))

  //so we would use this pattern when using it for some nonexistant key
  val badKey = r.get(key = "shouldNotExist").getOrElse("didn't find this key")
  println(badKey)

//  r.lpush("friends", "Alice", List("Dave", "Bob", "Carol")) //this will add a list inside the list
//  r.lpush("friends", "Alice", "Bob", "Carol", "Dave", "Eve") //this is the more usual correct way of adding to list
//  val friends = r.lrange("friends", 8, 12).getOrElse(List[String]())
  val friends = r.lrange("friends", 0, 5).getOrElse(List[String]())
  println("All my friends:")
  friends.foreach(println)

  //Set example
  val addCount = r.sadd("superpowers", "flight", "x-ray vision", "freezing")
  print("Added ")
  println(addCount.getOrElse(0L)) //since we are adding the same values we should get 0 the next time

  val randomPower = r.srandmember("superpowers").getOrElse("No such power")
  println(s"Random superpower: $randomPower")

  //Storing Hash data
  val addHashCount = r.hset("person:100", "name", "Valdis")
  println(s"Added to hash $addHashCount key-values") //so true will indicate creating a new value
  val myName = r.hget("person:100", "name").getOrElse("no such value found")
  println(s"My name is $myName")





}
