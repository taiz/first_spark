import org.apache.spark.deploy.SparkHadoopUtil
import org.apache.spark.{SparkEnv, SparkContext, SparkConf}
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkContext._

object SimpleApp {
  def main(args: Array[String]) {
    /*
    val hadoopConfiguration = {
      val env = SparkEnv.get
      val conf = SparkHadoopUtil.get.newConfiguration()
      // Explicitly check for S3 environment variables
      if (System.getenv("AWS_ACCESS_KEY_ID") != null &&
        System.getenv("AWS_SECRET_ACCESS_KEY") != null) {
        conf.set("fs.s3.awsAccessKeyId", System.getenv("AWS_ACCESS_KEY_ID"))
        conf.set("fs.s3n.awsAccessKeyId", System.getenv("AWS_ACCESS_KEY_ID"))
        conf.set("fs.s3.awsSecretAccessKey", System.getenv("AWS_SECRET_ACCESS_KEY"))
        conf.set("fs.s3n.awsSecretAccessKey", System.getenv("AWS_SECRET_ACCESS_KEY"))
      }
      // Copy any "spark.hadoop.foo=bar" system properties into conf as "foo=bar"
      /*
      Utils.getSystemProperties.foreach { case (key, value) =>
        if (key.startsWith("spark.hadoop.")) {
          conf.set(key.substring("spark.hadoop.".length), value)
        }
      }
      */
      val bufferSize = System.getProperty("spark.buffer.size", "65536")
      conf.set("io.file.buffer.size", bufferSize)
    }
    */

    val logFile = "README.md"
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext("local", "Simple App", conf)

    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(_.contains("a")).count()
    val numBs = logData.filter(_.contains("b")).count()

    println(s"Lines with a: $numAs, Lines with b: $numBs")

    //sc.hadoopConfiguration.set("fs.s3.awsAccessKeyId", "AKIAJ3IAFTDHJEVTBQ5Q")
    //sc.hadoopConfiguration.set("fs.s3.awsSecretAccessKey", "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")

    val sqlContext = new SQLContext(sc)
    val rdd = sqlContext.read.json("s3n://jp.co.nssys.sanso.spark/test.json")
    println(rdd)
    println(rdd.toJSON.collect.toList.mkString(",\n"))
  }
}
