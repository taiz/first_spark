import org.apache.spark.deploy.SparkHadoopUtil
import org.apache.spark.{SparkEnv, SparkContext, SparkConf}
import org.apache.spark.sql.SQLContext
import org.apache.spark.SparkContext._

object SimpleApp {
  def main(args: Array[String]) {
    val logFile = "README.md"
    val conf = new SparkConf().setAppName("Simple Application")
    val sc = new SparkContext("local", "Simple App", conf)

    val logData = sc.textFile(logFile, 2).cache()
    val numAs = logData.filter(_.contains("a")).count()
    val numBs = logData.filter(_.contains("b")).count()

    println(s"Lines with a: $numAs, Lines with b: $numBs")

    val sqlContext = new SQLContext(sc)
    val rdd = sqlContext.read.json("s3n://jp.co.nssys.sanso.spark/test.json")
    println(rdd)
    println(rdd.toJSON.collect.toList.mkString(",\n"))
  }
}
