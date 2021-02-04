package core.kafka.api.consumer.impl

import java.util

import play.api.Logging

import javax.inject.Singleton
import org.apache.kafka.clients.consumer.{OffsetAndMetadata, OffsetCommitCallback}
import org.apache.kafka.common.TopicPartition

@Singleton
class KafkaConsumerCallBack extends OffsetCommitCallback with Logging {
  override def onComplete(offsets: util.Map[TopicPartition, OffsetAndMetadata],
                          exception: Exception): Unit = {
    if (exception != null)
      logger.error(s"Commit failed for offsets {$offsets, $exception}")
    else
      logger.debug(s"Offset commited: $offsets")

  }
}
