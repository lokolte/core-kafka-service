package core.kafka.models.request

import core.kafka.utils.Constants.Fields._
import core.kafka.utils.Validators._
import play.api.libs.functional.syntax._
import play.api.libs.json.Reads._
import play.api.libs.json.{Json, Reads, __}

case class ValueEvent(
    key: String,
    value: String
)

object ValueEvent {

  implicit val reads: Reads[ValueEvent] = (
    string(__, KEY) and
    string(__, VALUE)
  )(ValueEvent.apply _)

  implicit val writes = Json.writes[ValueEvent]

}
