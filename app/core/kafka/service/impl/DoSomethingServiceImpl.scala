package core.kafka.service.impl;

import core.kafka.models.request.ValueEvent
import core.kafka.models.response.{ServiceResponse, ValidValue}
import core.kafka.service.DoSomethingService
import play.api.i18n.MessagesApi

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

@Singleton
class DoSomethingServiceImpl @Inject() (implicit ec: ExecutionContext) extends DoSomethingService {
  override def doSomething(valueEvent: ValueEvent): Future[ServiceResponse] = {
    Future(ValidValue(valueEvent))
  }

  def doSomethingAndPersist(valueEvent: ValueEvent): Future[ServiceResponse] = {
    Future.successful(ValidValue(valueEvent))
  }
}
