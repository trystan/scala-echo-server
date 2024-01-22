package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import java.util.Date
import play.api.http.ContentTypes

@Singleton
class EchoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index(userId: String) = Action { implicit request: Request[AnyContent] =>
    val json = Json.obj(
      "user" -> userId,
      "date" -> new Date().toInstant().toString(),
      "method" -> request.method,
      "uri" -> request.uri,
      "query" -> Json.toJsObject(request.queryString),
      "headers" -> Json.toJsObject(request.headers.toMap), 
      "body" -> request.body.asText.getOrElse(null))

    Ok(json)
  }
}
