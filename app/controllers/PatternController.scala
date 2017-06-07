package controllers

import javax.inject.Inject

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import nl.sogyo.kbd._
import nl.sogyo.kbd.forms._

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

abstract class PatternController @Inject()(pc: PatternCollection) extends Controller {

  val patternForm = Form(
    mapping(
      "boxes" -> seq(seq(boolean)),
      "length" -> number.verifying(_ > 0),
      "tracks" -> number.verifying(_ > 0)
    )(PatternForm.apply)(PatternForm.unapply)
  )

  def fromID(patternID: Int): Action[AnyContent] = Action.async {
    pc.get(patternID).map {
      case Some(p) => createResult(p)
      case None => NotFound("ID " + patternID + " not found.")
    }
  }

  def createResult(p: Pattern): Result = {
    val filledForm = patternForm.fill(PatternForm(p.data.map(_.data), p.length, p.tracks))

    val testMap = SoundMap(p.tracks)
    testMap match {
      case Success(m) => Ok(views.html.index(m)(filledForm))
      case Failure(e) => BadRequest("Bad request: " + e)
    }
  }
}
