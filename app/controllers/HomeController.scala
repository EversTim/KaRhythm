package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.Logger

import nl.sogyo.kbd._
import nl.sogyo.kbd.forms.PatternForm

import scala.util.{Try, Success, Failure}

/**
  * This controller creates an `Action` to handle HTTP requests to the
  * application's home page.
  */
@Singleton
class HomeController @Inject() extends Controller {

  /**
    * Create an Action to render an HTML page.
    *
    * The configuration in the `routes` file means that this method
    * will be called when the application receives a `GET` request with
    * a path of `/`.
    */
  def index: Action[AnyContent] = {
    createAction(Pattern.defaultPattern)
  }

  def test(id: Int): Action[AnyContent] = {
    if (id < Pattern.testPatterns.length) createAction(Pattern.testPatterns(id))
    else Action(NotFound("404: ID not found: " + id))
  }

  def createAction(p: Pattern): Action[AnyContent] = {
    val form = Form(
      mapping(
        "boxes" -> seq(seq(boolean)),
        "length" -> number,
        "tracks" -> number
      )(PatternForm.apply)(PatternForm.unapply)
    )
    val filledForm = form.fill(PatternForm(p.data, p.length, p.tracks))

    val testMap = SoundMap(p.tracks)
    Action { implicit request =>
      testMap match {
        case Success(m) => Ok(views.html.index(m)(filledForm))
        case Failure(e) => BadRequest("Bad request: " + e)
      }
    }

  }
}
