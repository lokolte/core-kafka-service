package core.kafka.api.producer

import com.google.inject.ImplementedBy
import core.kafka.api.producer.impl.KafkaProducerImpl

@ImplementedBy(classOf[KafkaProducerImpl])
trait Publisher[T] {
  def send(t: T): Unit
}
