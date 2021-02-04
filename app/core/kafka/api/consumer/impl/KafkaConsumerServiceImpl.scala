package core.kafka.api.consumer.impl

import java.time.Duration
import java.util
import java.util.Properties
import java.util.concurrent.TimeUnit
import akka.actor.ActorSystem
import core.kafka.ec.CustomConsumerExecutionContext

import javax.inject.{Inject, Singleton}
import org.apache.kafka.clients.consumer._
import org.apache.kafka.common.TopicPartition
import play.api.inject.ApplicationLifecycle
import play.api.{Configuration, Logging}
import core.kafka.api.consumer.{ConsumerKafkaConfig, KafkaConsumerService, ProcessRecord}
import core.kafka.models.response.ServiceResponse
import core.kafka.models.response.ValidValue
import core.kafka.utils.Constants.KafkaConfigKeys._

import scala.concurrent.Future
import scala.concurrent.duration

@Singleton
class KafkaConsumerServiceImpl @Inject() (
    val config: Configuration,
    callback: KafkaConsumerCallBack,
    recordHandler: ProcessRecord
)(implicit val system: ActorSystem,
  ec: CustomConsumerExecutionContext,
  lifeCycle: ApplicationLifecycle)
    extends KafkaConsumerService
    with ConsumerKafkaConfig
    with Logging {

  logger.info("Initializing KafkaConsumer")
  val consumerTopic: String = config.get[String](KAFKA_CONSUMER_TOPIC)
  val pollTimeOut: Long     = config.get[Long](CONSUMER_TIMEOUT)
  val maxRetries: Int       = config.get[Int](CONSUMER_MAX_RETRIES)
  val delay: Int            = config.get[Int](CONSUMER_DELAY)
  val kafkaConsumer         = new KafkaConsumer[String, String](properties)

  override def properties: Properties = {
    val props: Properties = super.properties

    props.setProperty(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, "1")

    props
  }

  kafkaConsumer.subscribe(util.Arrays.asList(consumerTopic))

  schedule(delay)

  private def schedule(delay: Int, retries: Int = 0): Unit =
    system.scheduler.scheduleOnce(delay =
      duration.Duration(calculateDuration(delay, retries), TimeUnit.SECONDS)
    ) { start(delay, retries) }

  private def calculateDuration(delay: Int, retries: Int): Int =
    if (retries == 0) delay else delay * 5 * retries

  override def start(delay: Int, retries: Int = 0) = {
    logger.info("Polling for messages...")
    val records: ConsumerRecords[String, String] =
      kafkaConsumer.poll(Duration.ofMillis(pollTimeOut))

    if (records.iterator().hasNext()) {
      val record = records.iterator().next()
      logger.debug(s"ConsumerRecord: ${record.toString}")
      val result: Future[ServiceResponse] = recordHandler.processRecord(record)

      result
        .map {
          case ValidValue(_) =>
            logger.info("Event persisted")
            kafkaConsumer.commitAsync(callback)
            schedule(delay)
          case _ =>
            logger.info("Delay the consumer")
            if (retries < maxRetries) {
              kafkaConsumer.seek(new TopicPartition(consumerTopic, record.partition()),
                                 record.offset())
              logger.info(
                s"Scheduling retry for record [${retries + 1} of $maxRetries]: ${record.toString}"
              )
              schedule(delay, retries + 1)
            } else { // max retries was reached, retries reset to 0, this will be find out in other ticket
              logger.info(
                s"Scheduler reset delay due to maxRetries is reached for ${record.toString}"
              )
              schedule(delay)
            }
        }
    } else schedule(delay)
    logger.info("Exiting the poll")
  }

  override def close(): Unit = {
    logger.info("*** Shutting down Consumer ***")
    kafkaConsumer.close()
  }

  lifeCycle.addStopHook(() => Future(close()))
}
