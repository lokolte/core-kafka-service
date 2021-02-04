package core.kafka.api.producer.impl

import com.google.inject.{Inject, Singleton}
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}
import play.api.libs.json.Json
import play.api.{Configuration, Logging}
import core.kafka.models.request.ValueEvent
import core.kafka.api.producer.{ProducerKafkaConfig, Publisher}
import core.kafka.utils.Constants.KafkaConfigKeys._

@Singleton
class KafkaProducerImpl @Inject() (val config: Configuration, callback: KafkaCallback)
    extends Publisher[ValueEvent]
    with ProducerKafkaConfig
    with Logging {

  val topic: String = config.get[String](KAFKA_PRODUCER_TOPIC)
  val producer      = new KafkaProducer[String, String](properties)

  override def send(valueEvent: ValueEvent): Unit = {
    //create the producer record
    val record: ProducerRecord[String, String] =
      new ProducerRecord(topic, Json.toJson(valueEvent).toString())

    producer.send(record, callback)

    producer.flush()
  }
}
