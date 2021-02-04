package core.kafka.api.consumer

import com.google.inject.ImplementedBy
import org.apache.kafka.clients.consumer.ConsumerRecord
import core.kafka.api.consumer.impl.ProcessRecordImpl
import core.kafka.models.response.ServiceResponse

import scala.concurrent.Future

@ImplementedBy(classOf[ProcessRecordImpl])
trait ProcessRecord {

  def processRecord(record: ConsumerRecord[String, String]): Future[ServiceResponse]

}
