package com.github.valrcs
import com.redis._

object RedisClient extends App {
  println("Testing Connection to Redis")
  //we just a need a database first :)
  val port = 10283
  val db = "FunStoreNov20"
  val secret = Some("need to add it outside") //FIXME DO not show open secrets, do not commit ...


  val r = new RedisClient("redis-10283.c62.us-east-1-4.ec2.cloud.redislabs.com", port, 0, secret)

  r.set("name", "Valdis")
  r.incr(key = "visits")

  println(r.get("name"))
  val visits = r.get(key="visits")
  println(s"Number of visits: $visits")
}
