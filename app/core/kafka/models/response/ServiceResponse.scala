package core.kafka.models.response

import core.kafka.models.request.ValueEvent

trait ServiceResponse

final case class ServiceError(errMsg: String) extends ServiceResponse

final case class ValidValue(value: ValueEvent) extends ServiceResponse
