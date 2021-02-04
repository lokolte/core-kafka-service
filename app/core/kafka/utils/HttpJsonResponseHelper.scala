package core.kafka.utils

import play.api.libs.json.{JsValue, Json}
import core.kafka.models.response.{Error, Errors}
import core.kafka.utils.Constants.ControllerResults._

object HttpJsonResponseHelper {

  def success(jsValue: JsValue): JsValue = Json.obj(DATA -> jsValue)

  def success(message: String): JsValue = success(Json.obj(MESSAGE -> message))

  def error(error: Error): JsValue = Json.toJson(Errors(Seq(error)))

  def error(message: String): JsValue = error(Error(message))

  def error(jsVals: Seq[JsValue]): JsValue = Json.obj(ERRORS -> jsVals)

}
