package core.kafka.filters

import akka.stream.Materializer
import javax.inject.Inject
import net.logstash.logback.marker.Markers._
import org.slf4j._
import play.api.http.HttpEntity
import play.api.mvc._

import scala.jdk.CollectionConverters._
import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

class LoggingFilter @Inject() (implicit val mat: Materializer, ec: ExecutionContext)
    extends Filter {

  val logger: Logger = LoggerFactory.getLogger(this.getClass)

  def apply(
      nextFilter: RequestHeader => Future[Result]
  )(requestHeader: RequestHeader): Future[Result] = {

    val startTime = System.currentTimeMillis

    nextFilter(requestHeader).map { result =>
      val endTime           = System.currentTimeMillis
      val requestTime: Long = getRequestTimeDiff(startTime, endTime)

      // Logging
      if (result.header.status >= 500) {
        logger.error(logMarker(requestHeader, result, requestTime), logMsg(requestHeader, result))
      } else if (result.header.status >= 400) {
        logger.error(logMarker(requestHeader, result, requestTime), logMsg(requestHeader, result))
      }

      // decorate response with request time
      result.withHeaders("Request-Time" -> requestTime.toString)

    }
  }

  def getRequestTimeDiff(startTime: Long, endTime: Long): Long = endTime - startTime

  private def logMsg(requestHeader: RequestHeader, result: Result): String = {

    //GET and DELETE do not have a body
    val messageOpt = Try(result.body.asInstanceOf[HttpEntity.Strict].data.utf8String)

    val message = messageOpt match {
      case Success(value)     => value
      case Failure(exception) => exception.toString
    }

    s"http result ${result.header.status} on $requestHeader with headers: ${result.header.headers}. Message: $message"
  }

  private def logMarker(requestHeader: RequestHeader,
                        result: Result,
                        requestTimeMillis: Long): Marker = {

    appendEntries(
      Map[String, Any](
        "@fields.elapsed-time"         -> requestTimeMillis,
        "@fields.method"               -> requestHeader.method,
        "@fields.statusCode"           -> result.header.status,
        "@fields.requested_uri"        -> requestHeader.uri,
        "@fields.authorization_header" -> requestHeader.headers.keys.contains("Authorization")
      ).asJava
    )
  }
}
