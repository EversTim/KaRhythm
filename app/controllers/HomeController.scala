package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.Logger

import nl.sogyo.kbd._
import nl.sogyo.kbd.forms._

import scala.util.{Try, Success, Failure}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() extends Controller {

  val patternForm = Form(
    mapping(
      "boxes" -> seq(seq(boolean)),
      "length" -> number.verifying(_ > 0),
      "tracks" -> number.verifying(_ > 0)
    )(PatternForm.apply)(PatternForm.unapply)
  )

  def index: Action[AnyContent] = {
    createAction(Pattern.defaultPattern)
  }

  def test(id: Int): Action[AnyContent] = {
    if (id < Pattern.testPatterns.length) createAction(Pattern.testPatterns(id))
    else Action(NotFound("404: ID not found: " + id))
  }

  def createAction(p: Pattern): Action[AnyContent] = {
    val filledForm = patternForm.fill(PatternForm(p.data, p.length, p.tracks))

    val testMap = SoundMap(p.tracks)
    Action { implicit request =>
      testMap match {
        case Success(m) => Ok(views.html.index(m)(filledForm))
        case Failure(e) => BadRequest("Bad request: " + e)
      }
    }
  }

  def withPattern(p: Pattern): Action[AnyContent] = createAction(p)

  def postPattern: Action[AnyContent] = Action { implicit request =>
    patternForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(formWithErrors.toString)
      },
      pf => {
        val p = Pattern(pf.data, pf.length, pf.tracks)
        Ok("Form received: " + p.toString)
      }
    )
  }
}
