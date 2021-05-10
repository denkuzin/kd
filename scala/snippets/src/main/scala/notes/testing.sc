// RALF coockies analysis
ssh $AM6
spark-shell \
--deploy-mode client \
--num-executors 10 \
--executor-cores 32 \
--driver-cores 32 \
--executor-memory 38g \
--driver-memory 38g \
--driver-java-options "-XX:+UseParallelGC -DCRITEO_ENV=dev" \
--jars /home/d.kuzin/knn/retrieval-1.0-SNAPSHOT-uber.jar

import com.criteo.enterprise.common.{DataReaders, ExtraData}
import com.criteo.enterprise.common.Guid
import com.criteo.enterprise.deepr.jobs.{ConfigurableSparkJob, JobConfig, SchedulingConfig}
import com.criteo.enterprise.timelines.processing._
import com.criteo.hadoop.commons.formats.SequenceFileCombineInputFormat
import com.criteo.hadoop.recocomputer.jobs.user_profile.{MiniRalf, UserFeatures}
import com.criteo.hadoop.recocomputer.utils.SparkUtils
import com.criteo.hadoop.recocomputer.utils.SparkUtils.getIdealNbPartitions
import com.criteo.hadoop.spark.metrics.MetricContainer
import com.criteo.sdk.Platform
import com.criteo.xdrichtimeline.messages.XdRichTimeline
import org.apache.hadoop.io.{BytesWritable, Text}
import org.apache.spark.SparkContext
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{Dataset, SaveMode, SparkSession}
import org.apache.spark.storage.StorageLevel
import org.apache.spark.util.LongAccumulator
import org.apache.thrift.TDeserializer
import org.apache.thrift.protocol.TCompactProtocol
import scala.collection.JavaConverters._
import scala.util.Random

object PartnerOriginType extends Enumeration {
  type PartnerOriginType = Value
  val MiniRalf, Timeline, MiniRalfAndTimeline = Value
}
object EligiblePartnersWithCountry {
  def apply(row: (String, Guid, Array[Int])): EligiblePartnersWithCountry = {
    EligiblePartnersWithCountry(row._1, row._2, row._3)
  }
}
case class EligiblePartnersWithCountry(country: String, uid: Guid, partnerIds: Array[Int])

val miniRalfPartitionSizeMb = 128
val partitionSizeMb = miniRalfPartitionSizeMb
val config = DataReaders.getMapReduceReadingConfig(partitionSizeMb, sc)
val path = miniRalfPath
val miniRalfPath = "/user/audience/mini_ralf_cookie/US/20191106000000"

val logFile = sc.newAPIHadoopFile(
      path,
      classOf[SequenceFileCombineInputFormat[Text, BytesWritable]],
      classOf[Text],
      classOf[BytesWritable],
      config)

val miniRalfs = logFile.map {
      case (uid, cookieBytes) =>
        uid.toString -> cookieBytes.copyBytes()
    }

def getCampaignIds(miniRalfFeatures: UserFeatures): Seq[Int] = {
    if (miniRalfFeatures != null && miniRalfFeatures.isSetProspecting && miniRalfFeatures.prospecting.isSetCampaignIds) {
      miniRalfFeatures.prospecting.campaignIds.asScala.map(_.toInt)
    } else Seq.empty[Int]
  }

val extraDataPath: String = ExtraData.prodExtraDataPath
val partnersByCampaignId = ExtraData.loadCampaignPartnerMapping(Platform.US, extraDataPath)


val parsed = miniRalfs.mapPartitions { partition =>
      partition.flatMap {
        case (uid, miniRalfBytes) =>

          val miniRalf = new MiniRalf()
          val thriftDeserializer = new TDeserializer(new TCompactProtocol.Factory())
          thriftDeserializer.deserialize(miniRalf, miniRalfBytes)

          val campaignIds = (
            getCampaignIds(miniRalf.userFeaturesPopA) ++
              getCampaignIds(miniRalf.userFeaturesPopB)
          ).distinct

          val partnerIds = campaignIds.flatMap { campaignId =>
            partnersByCampaignId.get(campaignId)
          }.distinct

          if (partnerIds.isEmpty) {
            None
          } else {
            Some(uid -> partnerIds.toArray)
          }
      }
    }




val users_1 = miniRalfs.map{case (uid, miniRalfBytes) => uid }



// this works fine

val rrr = miniRalfs.map { 
        case (uid, miniRalfBytes) =>
          val thriftDeserializer = new TDeserializer(new TCompactProtocol.Factory())
          val miniRalf = new MiniRalf()
          //try {
          thriftDeserializer.deserialize(miniRalf, miniRalfBytes)
          uid -> miniRalf
          //}
          //catch
          //{
          //  case unknown => {println("Got this unknown exception: " + unknown)
         //   uid -> miniRalfBytes}
         // }
}





          // val campaignIds = (
          //   getCampaignIds(miniRalf.userFeaturesPopA) ++
          //     getCampaignIds(miniRalf.userFeaturesPopB)
          // ).distinct

          // val partnerIds = campaignIds.flatMap { campaignId =>
          //   partnersByCampaignId.get(campaignId)
          // }.distinct

          // if (partnerIds.isEmpty) {
          //   None
          // } else {
          //   Some(uid -> partnerIds.toArray)
          // }
      }
























////////////////////////////////////////////////////////////////////////////////////////////////////////////////

ssh $AM6
spark-shell \
--deploy-mode client \
--num-executors 10 \
--executor-cores 32 \
--driver-cores 32 \
--executor-memory 38g \
--driver-memory 38g \
--driver-java-options "-XX:+UseParallelGC -DCRITEO_ENV=dev" \
--jars /home/d.kuzin/knn/retrieval-1.0-SNAPSHOT-uber.jar


import com.criteo.enterprise.common.{DataReaders, Guid, IOHelpers}
import com.criteo.enterprise.deepr.jobs.{ConfigurableSparkJob, JobConfig, SchedulingConfig}
import com.criteo.enterprise.user.embeddings.UserEmbeddingReader
import com.criteo.hadoop.recocomputer.common
import com.criteo.hadoop.recocomputer.common.hdfs.PathConverters._
import com.criteo.hadoop.recocomputer.utils.SparkUtils
import com.criteo.hadoop.spark.metrics.MetricContainer
import com.criteo.sdk.Platform
import org.apache.spark.SparkConf
import org.apache.spark.rdd.RDD
import org.apache.spark.serializer.{KryoSerializer, SerializerInstance}
import com.criteo.enterprise.common.ProductWithScore
import org.apache.spark.sql.SparkSession
import org.apache.spark.util.LongAccumulator



import com.criteo.enterprise.knn.HnswKnnIndex


val date="20191028000000"

val suspicious_user = "5c29d778-c0d4-46cb-97a0-1dd1b8bd0017"
val suspicious_partner: Int = 30216

val knnIndicesPath = s"/user/deepr/dev/d.kuzin/temp_copy/knn-indices-alt/US/$date/country=US"
val indices = HnswKnnIndex.getIndices(knnIndicesPath, 50)

val userEmbeddingPath = s"/user/deepr/dev/d.kuzin/temp_copy/user-embeddings-nn/US/$date/country=US/"

val userEmbeddings = UserEmbeddingReader.loadUserEmbeddings(spark, userEmbeddingPath).filter(
  {case (uid, embeddings) => uid.toString == suspicious_user}).collect()

val eligiblePartnersPath: String = s"/user/deepr/dev/d.kuzin/temp_copy/eligible-partners/US/$date/country=US/"
val usersEligiblePartners = spark.read.parquet(eligiblePartnersPath).as[(Guid, List[Int])].rdd.filter(
  {case (uid, _) => uid.toString == suspicious_user}).collect()

val partners = usersEligiblePartners(0)._2.toSet
val vector = userEmbeddings(0)._2.map(_.toFloat)
val numRecommendations: Int = 20
val partnerId: Int = suspicious_partner

val recos = indices.get(partnerId).flatMap { partnerIndices =>
  val indexRecos =
    partnerIndices.flatMap { index =>
      index.getClosestVectors(vector, numRecommendations).map {
        case (p, s) => ProductWithScore(p, s)
      }
    }.sortBy(_.score).take(numRecommendations)
  Some(indexRecos)
}
recos









object Testing {
  val METADATA_PATH = "_metadata"
  val COUNTERS_PATH = "_counters"
  def getConfigPath(outputRootPath: String): String =
    outputRootPath + "/_config"
}

trait ConfigTemplate {
  val outputRootPath: String
  val resultsPath: String = outputRootPath
  val configPath: String = Testing.getConfigPath(outputRootPath)
}

case class ComputeViewsJobConfig(
    outputRootPath: String = "",
    inputPath: String = "",
    endTimestamp: Int = 1555329600 //04-15 12:00
) extends ConfigTemplate









//def getRetentionConfigPath(outputRootPath: String): Option[String] = {
//  val patternWithDate = """.*\/[0-9]{4}[0-9]{2}[0-9]{2}000000"""
//  patternWithDate.r.findFirstIn(outputRootPath) match {
//    case Some(path) =>
//      val configPath = path.split('/').dropRight(1).mkString("/", "/", "/.retention.yaml")
//      Some(configPath)
//    case None => None
//  }
//}
//
//getRetentionConfigPath("/a/b/c/20120323000000/dsa/ds")
//def getRetentionConfigPath(outputPath: String): Option[String] = {
//  val patternWithDate = """.*\/[0-9]{4}[0-9]{2}[0-9]{2}000000"""
//
//  patternWithDate.r.findFirstIn(outputPath) match {
//    case Some(path) =>
//      val configPath = path.split('/').dropRight(1).mkString("/", "/", "/.retention.yaml")
//      Some(configPath)
//    case None => None
//  }
//}
//
//
//val stringString = "a/b/c/20191212000000/d/versd"
//getRetentionConfigPath(stringString)
////val stringString = "a/b/c/201912120000/d/versd"
//
////val patternWithDate = """((.*[0-9]{4}[0-9]{2}[0-9]{2}000000))"""
//val patternWithDate = """.*\/[0-9]{4}[0-9]{2}[0-9]{2}000000"""
//
////val subPath = patternWithDate.r.findFirstIn(stringString)
//patternWithDate.r.findFirstIn(stringString) match {
//  case Some(path) =>
//    path.split('/').dropRight(1).mkString("/", "/", "/.retention.yaml")
//  case _ => None
//}

//.get.group(0)
//patternWithDate.r.findFirstMatchIn(stringString).get.group(1)
//patternWithDate.r.findFirstMatchIn(stringString).get.group(2)
//subPath match {
//  case Some(path) => {
//    // 1. remove the last name
//    // 2. add '/.retention.yaml'
//    // 3.
//    true
//
//  }
//  case _ => false
//}
//
//
//
//def parsePath(path: String): String = {
//  ""
//}
//
//val dateRegex = """([0-9]{4}[0-9]{2}[0-9]{2}.*)""".r
//
//def isMatch(string: String): Boolean = {
//  string match {
//    case dateRegex(_) => true
//    case _ => false
//  }
//}
//
//isMatch("20121032000000")
//isMatch("abc")
//
//val string = "a/b/c/2019121200000/d"
//val split = string.split('/')
//
////check is there any date in the name
//split.map(x => isMatch(x)).foldLeft(false)(_ || _)
//
//val filtered = split.takeWhile(p => !isMatch(p))
//
//if (filtered sameElements split)
//  // we didn't find the date in any element of array
//
//
////mkString(start="/", sep="/", end = "/.retention.yaml")
//
//
//val part = split.takeWhile {
//  case x@dateRegex(path) => false
//  case x => true
//}
//
////val s = "sdfdsfsdfafsdfasd/rqwerqer/201910120000000/wqerewqrweqrqwe"
////s.split('/')
////extract(s.split('/').toList)
//
//
////val splittedString = s2.split("""([0-9]{4}[0-9]{2}[0-9]{2}.*)""")
////splittedString.foreach( println )
////println(s2.split("([0-9]{4}[0-9]{2}[0-9]{2}.*)").head)
////println(s2.split("qwertyqwerty").head)
////val exampleSplittedString = "asdsadsa/dsadasdsada/dasdas/ads".split("/a")
////exampleSplittedString.foreach( println )
//
//
//
//
//
//
