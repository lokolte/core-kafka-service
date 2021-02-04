package core.kafka.service

import com.google.inject.ImplementedBy
import core.kafka.models.request.ValueEvent
import core.kafka.models.response.ServiceResponse
import core.kafka.service.impl.DoSomethingServiceImpl

import scala.concurrent.Future

@ImplementedBy(classOf[DoSomethingServiceImpl])
trait DoSomethingService {
  def doSomething(valueEvent: ValueEvent): Future[ServiceResponse]

  def doSomethingAndPersist(valueEvent: ValueEvent): Future[ServiceResponse]
}
