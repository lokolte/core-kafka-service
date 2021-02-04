package core.kafka.hooks

import com.google.inject.AbstractModule
import core.kafka.api.consumer.KafkaConsumerService
import core.kafka.api.consumer.impl.KafkaConsumerServiceImpl

class ConsumerStarter extends AbstractModule {
  override def configure(): Unit = {
    bind(classOf[KafkaConsumerService])
      .to(classOf[KafkaConsumerServiceImpl])
      .asEagerSingleton()
  }
}
