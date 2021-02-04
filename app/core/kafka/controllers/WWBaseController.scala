package core.kafka.controllers

import play.api.i18n.{Lang, Langs, MessagesApi}
import play.api.libs.json._
import play.api.mvc.{AbstractController, ControllerComponents, Request, Result}

import javax.inject.{Inject, Singleton}
import scala.concurrent.{ExecutionContext, Future}

import core.kafka.utils.HttpJsonResponseHelper._

@Singleton
class WWBaseController @Inject() (
    cc: ControllerComponents,
    langs: Langs
)(implicit ec: ExecutionContext, messagesApi: MessagesApi)
    extends AbstractController(cc) {

  implicit val lang: Lang = langs.availables.head

  def formatValidationErrors(jsError: JsError)(implicit messagesApi: MessagesApi,
                                               lang: Lang): Seq[JsObject] =
    jsError.errors
      .flatMap(a => a._2.map(b => validationErrorToJson(a._1, b)))
      .toSeq

  def validationErrorToJson(jsPath: JsPath, validationError: JsonValidationError)(
      implicit messagesApi: MessagesApi,
      lang: Lang
  ): JsObject = {
    val key =
      if (validationError.args.nonEmpty)
        validationError.args.head.toString
      else
        jsPath.toString.substring(1)
    val value = messagesApi(validationError.message)
    Json.obj(key -> value)
  }

  def handle[R](f: R => Future[Result])(implicit request: Request[JsValue],
                                        reads: Reads[R]): Future[Result] =
    request.body
      .validate[R]
      .map(f)
      .recoverTotal { e => Future(BadRequest(error(formatValidationErrors(e)))) }
}
