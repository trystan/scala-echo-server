package services

import scala.util.Using
import scala.concurrent.Future
import play.api.libs.json._
import com.mongodb.{ServerApi, ServerApiVersion}
import org.mongodb.scala.{ConnectionString, MongoClient, MongoClientSettings}
import org.mongodb.scala.bson.Document
import org.mongodb.scala.model.Filters._
import com.mongodb.client.result.InsertOneResult
import scala.concurrent.ExecutionContext
import org.mongodb.scala.model.Indexes
import org.mongodb.scala.model.IndexOptions
import java.util.concurrent.TimeUnit

class HistoryRepository {
  private val mongo = MongoClient("mongodb://localhost:27017")
  
  private def ensureIndexesExist()(implicit ec: ExecutionContext) = {
    mongo.getDatabase("echo-server")
        .getCollection("echos")
        .createIndex(Indexes.ascending("userId"), IndexOptions().expireAfter(1, TimeUnit.DAYS))
        .toFuture()
  }

  def start()(implicit ec: ExecutionContext) = ???
    
  //   {
  //   ensureIndexesExist()
  // }

  def stop()(implicit ec: ExecutionContext) = {
    mongo.close()
  }

  def getRequestHistory(userId: String)(implicit ec: ExecutionContext): Future[Seq[JsValue]] = {
    def toJson(doc: Document): JsObject = { Json.parse(doc.toJson()).as[JsObject] - "_id" }

    mongo.getDatabase("echo-server")
      .getCollection("echos")
      .find(equal("userId", userId))
      .toFuture()
      .map(docs => docs.map(toJson))
  }
  
  // TODO: return something other than InsertOneResult; no need to expose which db we're using
  def saveRequestDetails(userId: String, json: JsObject)(implicit ec: ExecutionContext): Future[InsertOneResult] = {
    val toInsert = json + ("userId" -> JsString(userId))

    mongo.getDatabase("echo-server")
        .getCollection("echos")
        .insertOne(Document.apply(toInsert.toString()))
        .toFuture()
  }
}