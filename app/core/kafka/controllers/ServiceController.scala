package core.kafka.controllers

import core.kafka.models.request.ValueEvent
import core.kafka.models.response.{ServiceError, ServiceResponse, ValidValue}
import core.kafka.service.impl.OrchestrationService
import core.kafka.utils.HttpJsonResponseHelper.{error, success}
import play.api.i18n.{Langs, MessagesApi}
import play.api.libs.json.{JsValue, Json}
import play.api.mvc.{Action, ControllerComponents, Result}

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

@Singleton
class ServiceController @Inject() (
    cc: ControllerComponents,
    orchestrationService: OrchestrationService,
    langs: Langs
)(implicit ec: ExecutionContext, messagesApi: MessagesApi)
    extends WWBaseController(cc, langs) {

  def publishEvent: Action[JsValue] =
    Action.async(parse.json) { implicit request =>
      handle[ValueEvent](orchestrationService.doSomethingAndPublishEvent(_).map(responseHandler))
    }

  def responseHandler(res: ServiceResponse): Result = res match {
    case err: ServiceError      => InternalServerError(error(err.errMsg))
    case validValue: ValidValue => Ok(success(Json.toJson(validValue.value)))
  }
}
