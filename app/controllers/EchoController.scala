package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json._
import java.util.Date
import play.api.http.ContentTypes
import scala.concurrent.Future
import scala.concurrent.Await
import scala.util.Using
import scala.concurrent.duration.Duration
import scala.util.Failure
import scala.util.Success
import services.HistoryRepository
import services.StatsRepository

@Singleton
class EchoController @Inject()(
    val controllerComponents: ControllerComponents, 
    val historyService: HistoryRepository,
    val statsService: StatsRepository) extends BaseController {
  private implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  def index(userId: String) = Action.async { implicit request: Request[AnyContent] =>
    val json = Json.obj(
      "user" -> userId,
      "date" -> new Date().toInstant().toString(),
      "method" -> request.method,
      "uri" -> request.uri,
      "query" -> Json.toJsObject(request.queryString),
      "headers" -> Json.toJsObject(request.headers.toMap), 
      "body" -> request.body.asText.getOrElse(null))

    val future = Future.sequence(Seq(
      historyService.saveRequestDetails(userId, json),
      statsService.saveRequestDetails(userId, json)))
    
    future.map(_ => Ok(json))
  }

  def history(userId: String) = Action.async { implicit request: Request[AnyContent] =>
    historyService.getRequestHistory(userId)
      .map(docs => Ok(Json.obj("history" -> docs)))
  }

  def stats(userId: String) = Action.async { implicit request: Request[AnyContent] =>
    statsService.getStats(userId)
      .map {
        case None => NotFound("")
        case Some(json) => Ok(json)
      }
  }
}
