package core.kafka.utils

//defined in application.conf
object Constants {
  object Fields {
    val KEY   = "key"
    val VALUE = "value"
  }

  object ErrorMessages {
    private val invalid = "error.invalid"
    val INVALID_VALUE   = s"$invalid.value"
    val REQUIRED_FIELD  = "error.required.field"
  }

  object KafkaConfigKeys {
    val LOCALHOST = "localhost"

    val KAFKA_ROOT     = "kafka"
    val KAFKA_HOST     = s"$KAFKA_ROOT.hosts"
    val KAFKA_USERNAME = s"$KAFKA_ROOT.username"
    val KAFKA_PASSWORD = s"$KAFKA_ROOT.password"
    val SASL_ENABLED   = s"$KAFKA_ROOT.sasl.enabled"

    val KAFKA_PRODUCER_TOPIC = s"$KAFKA_ROOT.producer.topic"

    val KAFKA_CONSUMER_TOPIC = s"$KAFKA_ROOT.consumer.topic"
    val CONSUMER_GROUP       = s"$KAFKA_ROOT.consumer.group"
    val CONSUMER_TIMEOUT     = s"$KAFKA_ROOT.consumer.timeout"
    val CONSUMER_AUTOCOMMIT  = s"$KAFKA_ROOT.consumer.autocommit"
    val CONSUMER_DELAY       = s"$KAFKA_ROOT.consumer.scheduler.delay"
    val CONSUMER_MAX_RETRIES = s"$KAFKA_ROOT.consumer.scheduler.maxRetries"
  }

  object ControllerResults {
    val MESSAGE = "message"
    val ERRORS  = "errors"
    val DATA    = "data"
  }
}
