package core.kafka.api.consumer.impl

import core.kafka.ec.CustomConsumerExecutionContext

import javax.inject.Inject
import play.api.Logging
import org.apache.kafka.clients.consumer.ConsumerRecord
import play.api.libs.json._
import core.kafka.api.consumer.ProcessRecord
import core.kafka.models.request.ValueEvent
import core.kafka.models.response.{ServiceError, ServiceResponse, ValidValue}
import core.kafka.service.DoSomethingService

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class ProcessRecordImpl @Inject() (doSomethingService: DoSomethingService)(
    implicit ec: CustomConsumerExecutionContext
) extends ProcessRecord
    with Logging {

  override def processRecord(record: ConsumerRecord[String, String]): Future[ServiceResponse] = {
    Try(Json.parse(record.value().toString).validate[ValueEvent]) match {
      case Success(jsvalue) =>
        jsvalue match {
          case JsSuccess(valueEvent, _) =>
            logger.info(s"Valid message: $valueEvent")

            persist(valueEvent)

          case JsError(errors) =>
            logger.error(s"Invalid message: ${record.value().toString} $errors")
            Future.successful(ServiceError(s"Invalid message: ${record.value().toString} $errors"))
        }
      case Failure(exception) =>
        logger.error(s"Exception when parsing message ${record.value().toString}", exception)
        Future.successful(
          ServiceError(
            s"Exception when parsing message:${record.value().toString}, Exception: $exception"
          )
        )

    }
  }

  private def persist(valueEvent: ValueEvent): Future[ServiceResponse] = {
    logger.info(s"Data has persisted: $valueEvent")
    doSomethingService.doSomethingAndPersist(valueEvent)
  }

}
