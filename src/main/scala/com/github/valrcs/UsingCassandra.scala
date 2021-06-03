package com.github.valrcs

import com.github.valrcs.CassandraExample.cassandraExample

object UsingCassandra extends App {
  println("Testing Cassandra connection")

  val host = "cassandra-3fec232b-mihtal-a81e.aivencloud.com" //your url will be slightly different
  val port = 13249 //port might also be a different
  val username = "avnadmin" //most likely the same
  val password = "yourown" //FIXME get from enviroment, DO NOT commit real credentials to git!!
  //  val caPath = "C:\\certs\\"
  val caPath = "./src/resources/certs/ca.pem" //you need to download your own cert

  cassandraExample(host=host,port=port,username=username,password=password, caPath=caPath)
  println("Lets hold our fingers crossed")

}
