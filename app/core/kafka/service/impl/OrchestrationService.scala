package core.kafka.service.impl

import core.kafka.models.request.ValueEvent
import core.kafka.models.response.{ServiceResponse, ValidValue}
import core.kafka.api.producer.Publisher
import core.kafka.service.DoSomethingService
import play.api.Logging

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

@Singleton
class OrchestrationService @Inject() (
    doSometingService: DoSomethingService,
    publisher: Publisher[ValueEvent]
)(implicit ec: ExecutionContext)
    extends Logging {

  def doSomethingAndPublishEvent(valueEvent: ValueEvent): Future[ServiceResponse] = {
    doSometingService
      .doSomething(valueEvent) andThen publish()
  }

  def publish(): PartialFunction[Try[ServiceResponse], Unit] = {
    case Success(ValidValue(a)) =>
      publisher.send(a)
    case Failure(exception) =>
      logger.info(s"Exception: $exception")
  }
}
