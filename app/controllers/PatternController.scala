package controllers

import javax.inject.Inject

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import nl.sogyo.kbd.db._
import nl.sogyo.kbd.domain.Pattern
import nl.sogyo.kbd.forms._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

abstract class PatternController @Inject()(pc: PatternCollection, sc: SoundCollection) extends Controller {

  val patternForm = Form(
    mapping(
      "name" -> text,
      "boxes" -> seq(seq(boolean)),
      "tracknames" -> seq(text),
      "soundnames" -> seq(text),
      "length" -> number.verifying(_ > 0),
      "tracks" -> number.verifying(_ > 0)
    )(PatternForm.apply)(PatternForm.unapply)
  )

  def fromPatternID(patternID: Int): Action[AnyContent] = Action.async { implicit request =>
    pc.get(patternID).flatMap {
      case Some(p) => createResult(p)
      case None => Future.successful(NotFound("404 error: ID " + patternID + " not found."))
    }
  }

  def createResult(p: Pattern)(implicit rc: RequestHeader): Future[Result] = {
    val filledForm = patternForm.fill(PatternForm(p.name, p.data.map(_.data), p.data.map(_.name), p.data.map(_.sound.name), p.length, p.tracks))
    val soundMap = p.generateSoundMap
    sc.getAllNames.map(_.sorted).map(_.map(Some(_))).map(names => Ok(views.html.index(soundMap, names)(filledForm)))
  }
}
