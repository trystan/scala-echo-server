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

@Singleton
class EchoController @Inject()(val controllerComponents: ControllerComponents, val historyService: HistoryRepository) extends BaseController {
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

    historyService.saveRequestDetails(userId, json)
      .map(_ => Ok(json))
  }

  def history(userId: String) = Action.async { implicit request: Request[AnyContent] =>
    historyService.getRequestHistory(userId)
      .map(docs => Ok(Json.obj("history" -> docs)))
  }
}
