package com.github.valrcs


import com.datastax.driver.core._
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import java.io.FileInputStream
import java.security.KeyStore
import java.security.cert.CertificateFactory
import java.security.cert.X509Certificate


object CassandraExample {
  @throws[Exception]
  def cassandraExample(host: String, port: Int, username: String, password: String, caPath: String): Unit = {
    println(s"Going to connect to $host")
    val sslOptions = loadCaCert(caPath)
    //    var cluster = null
    val cluster = Cluster.builder.addContactPoint(host).withPort(port).withSSL(sslOptions).withAuthProvider(new PlainTextAuthProvider(username, password)).build
    try {
      //      cluster: Cluster = Cluster.builder.addContactPoint(host).withPort(port).withSSL(sslOptions).withAuthProvider(new PlainTextAuthProvider(username, password)).build
      val session = cluster.connect
      session.execute("CREATE KEYSPACE IF NOT EXISTS example_keyspace WITH REPLICATION = {'class': 'NetworkTopologyStrategy', 'aiven': 3}")
      session.execute("USE example_keyspace")
      session.execute("CREATE TABLE IF NOT EXISTS example_java (id int PRIMARY KEY, message text)")
      session.execute("INSERT INTO example_java (id, message) VALUES (?, ?)", 123, "Hello from Java-Scala!")
      session.execute("INSERT INTO example_java (id, message) VALUES (?, ?)", 90, "Hello from Valdis")
      val rs = session.execute("SELECT id, message FROM example_java")
      import scala.jdk.CollectionConverters._
      //      val row = rs.one()
      rs.forEach(row => println(String.format("Row: id = %d, message = %s", row.getInt("id"), row.getString("message"))))

      //      while ( rs.next() ) {
      //
      //      }
      //      for (row <- rs) {
      //        System.out.println(String.format("Row: id = %d, message = %s", row.getInt("id"), row.getString("message")))
      //      }
    } finally if (cluster != null) cluster.close()
  }

  @throws[Exception]
  private def loadCaCert(caCertPath: String) = {
    val cf = CertificateFactory.getInstance("X.509")
    var fis: FileInputStream = null
    var caCert: X509Certificate = null
    try {
      fis = new FileInputStream(caCertPath)
      caCert = cf.generateCertificate(fis).asInstanceOf[X509Certificate]
    } finally if (fis != null) fis.close()
    val tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm)
    val ks = KeyStore.getInstance(KeyStore.getDefaultType)
    ks.load(null)
    ks.setCertificateEntry("caCert", caCert)
    tmf.init(ks)
    val sslContext = SSLContext.getInstance("TLS")
    sslContext.init(null, tmf.getTrustManagers, null)
    RemoteEndpointAwareJdkSSLOptions.builder.withSSLContext(sslContext).build
  }
}