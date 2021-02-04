package core.kafka.api.config

import java.util.Properties

import org.apache.kafka.clients.CommonClientConfigs
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.config.SaslConfigs
import org.apache.kafka.common.security.auth.SecurityProtocol
import play.api.Configuration
import core.kafka.utils.Constants.KafkaConfigKeys.{
  KAFKA_HOST,
  KAFKA_PASSWORD,
  KAFKA_USERNAME,
  SASL_ENABLED
}

/**
  * Common KafkaConfiguration used by Producer & Consumer
  */
trait KafkaConfig {
  val config: Configuration

  val kafkaHost: String    = config.get[String](KAFKA_HOST)
  val username: String     = config.get[String](KAFKA_USERNAME)
  val password: String     = config.get[String](KAFKA_PASSWORD)
  val saslEnabled: Boolean = config.get[Boolean](SASL_ENABLED)

  def properties: Properties = {
    val props: Properties = new Properties()

    props.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaHost)

    if (saslEnabled) {
      props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, SecurityProtocol.SASL_SSL.toString)
      props.put(SaslConfigs.SASL_MECHANISM, "PLAIN")
      props.put(SaslConfigs.SASL_JAAS_CONFIG, jass)
    }
    props
  }

  val jass: String = {
    val jaasTemplate: String =
      "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"%s\" password=\"%s\";"
    String.format(jaasTemplate, username, password)
  }
}
