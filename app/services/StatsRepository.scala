package services

import scala.util.Using
import scala.concurrent.Future
import play.api.libs.json._
import com.mongodb.{ServerApi, ServerApiVersion}
import org.mongodb.scala.{ConnectionString, MongoClient, MongoClientSettings}
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._
import com.mongodb.client.result.InsertOneResult
import scala.concurrent.ExecutionContext
import org.mongodb.scala.model.Indexes
import org.mongodb.scala.model.IndexOptions
import java.util.concurrent.TimeUnit
import org.mongodb.scala.model.FindOneAndUpdateOptions

class StatsRepository {
  private val mongo = MongoClient("mongodb://localhost:27017")
  
  private def toJson(doc: Document): JsObject = { Json.parse(doc.toJson()).as[JsObject] - "_id" }

  private def ensureIndexesExist()(implicit ec: ExecutionContext) = {
    mongo.getDatabase("echo-server")
        .getCollection("stats")
        .createIndex(Indexes.ascending("userId"))
        .toFuture()
  }

  def start()(implicit ec: ExecutionContext) = {
    ensureIndexesExist()
  }

  def stop()(implicit ec: ExecutionContext) = {
    mongo.close()
  }

  def getStats(userId: String)(implicit ec: ExecutionContext): Future[Option[JsValue]] = {
    mongo.getDatabase("echo-server")
      .getCollection("stats")
      .find(equal("userId", userId))
      .first()
      .toFuture()
      .map(doc => Option(doc).map(toJson))
  }
  
  def saveRequestDetails(userId: String, json: JsObject)(implicit ec: ExecutionContext): Future[Option[JsObject]] = {
    val toInsert = json + ("userId" -> JsString(userId))

    mongo.getDatabase("echo-server")
        .getCollection("stats")
        .findOneAndUpdate(
          equal("userId", userId), 
          inc("requestCount", 1),
          FindOneAndUpdateOptions().upsert(true))
        .toFuture()
        .map(doc => Option(doc).map(toJson))
  }
}