package core.kafka.api.consumer

import com.google.inject.ImplementedBy
import core.kafka.api.consumer.impl.KafkaConsumerServiceImpl

@ImplementedBy(classOf[KafkaConsumerServiceImpl])
trait KafkaConsumerService {
  def start(delay: Int, retries: Int): Unit
  def close(): Unit

}
