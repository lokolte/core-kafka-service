package core.kafka.api.consumer

import java.util.Properties

import org.apache.kafka.clients.consumer.{ConsumerConfig, OffsetResetStrategy}
import org.apache.kafka.common.serialization.StringDeserializer
import core.kafka.api.config.KafkaConfig
import core.kafka.utils.Constants.KafkaConfigKeys.{CONSUMER_GROUP, CONSUMER_AUTOCOMMIT}

trait ConsumerKafkaConfig extends KafkaConfig {

  val consumerGroup: String = config.get[String](CONSUMER_GROUP)
  val autoCommit: String    = config.get[String](CONSUMER_AUTOCOMMIT)

  override def properties: Properties = {
    //Create consumer config
    val props: Properties = super.properties

    props.setProperty(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG,
                      classOf[StringDeserializer].getName)
    props.setProperty(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG,
                      classOf[StringDeserializer].getName)
    props.setProperty(ConsumerConfig.GROUP_ID_CONFIG, consumerGroup)
    props.setProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG,
                      OffsetResetStrategy.EARLIEST.toString.toLowerCase)
    props.setProperty(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit)

    props
  }

}
