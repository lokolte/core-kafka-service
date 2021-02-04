package core.kafka.utils

import play.api.libs.json.{JsPath, JsonValidationError, Reads}
import core.kafka.utils.Constants.ErrorMessages.{INVALID_VALUE, REQUIRED_FIELD}

object Validators {
  private def error(msg: String) = JsonValidationError(msg)

  val DEFAULT_VALIDATOR: Any => Boolean = _ => true

  def string(path: JsPath,
             key: String,
             errMsg: String = INVALID_VALUE,
             validator: String => Boolean = DEFAULT_VALIDATOR,
             transformer: String => String = _.trim): Reads[String] =
    (path \ key)
      .read[String]
      .map(transformer)
      .filter(error(REQUIRED_FIELD))(_.length > 0)
      .filter(error(errMsg))(validator)

}
