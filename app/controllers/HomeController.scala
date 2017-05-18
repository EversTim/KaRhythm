package controllers

import javax.inject._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import play.api.Logger

import nl.sogyo.kbd._

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
    createAction(Pattern.testPatterns(id))
  }

  def createAction(p: Pattern): Action[AnyContent] = {
    val form = Form(
      mapping(
        "boxes" -> seq(seq(boolean))
      )(Pattern.apply)(Pattern.unapply)
    )
    val filledForm = form.fill(p)

    val testMap = Map(0 -> "Windows Ding.wav", 1 -> "Windows Error.wav")
    Action { implicit request =>
      Ok(views.html.index(testMap)(filledForm))
    }
  }
}
