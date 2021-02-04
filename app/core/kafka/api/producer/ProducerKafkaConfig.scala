package core.kafka.api.producer

import java.util.Properties

import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import core.kafka.api.config.KafkaConfig

trait ProducerKafkaConfig extends KafkaConfig {

  override def properties: Properties = {
    val props: Properties = super.properties

    props.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, classOf[StringSerializer].getName)
    props.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                      classOf[StringSerializer].getName)
    props
  }

}
