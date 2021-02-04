package core.kafka.api.producer.impl

import com.google.inject.Singleton
import org.apache.kafka.clients.producer.{Callback, RecordMetadata}
import play.api.Logging

@Singleton
class KafkaCallback extends Callback with Logging {

  override def onCompletion(metadata: RecordMetadata, exception: Exception): Unit = {
    if (exception == null)
      logger.info(s"""
           |*********
           |Topic: ${metadata.topic()}

           |Partition: ${metadata.partition()}

           |Offset: ${metadata.offset()}
           |Timestamp: ${metadata.timestamp()}
              """.stripMargin)
    else
      logger.error(s"Error while publishing to Kafka $exception")
  }

}
