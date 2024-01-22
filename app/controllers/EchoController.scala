package controllers

import javax.inject._
import play.api._
import play.api.mvc._
import play.api.libs.json.Json
import java.util.Date

@Singleton
class EchoController @Inject()(val controllerComponents: ControllerComponents) extends BaseController {

  def index() = Action { implicit request: Request[AnyContent] =>
    val headers = request.headers.toMap
    val body = request.body.asText
    val now = new Date()
    Ok(Json.obj(
      "method" -> request.method,
      "uri" -> request.uri,
      "headers" -> Json.toJsObject(headers), 
      "body" -> body.getOrElse(null),
      "date" -> now.toInstant().toString()))
  }
}
