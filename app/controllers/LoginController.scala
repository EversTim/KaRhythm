package controllers

import javax.inject._

import nl.sogyo.kbd.db.users.UserCollection
import nl.sogyo.kbd.forms._
import nl.sogyo.kbd.users.User
import play.api.data.Form
import play.api.data.Forms._
import play.api.mvc._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class LoginController @Inject()(uc: UserCollection) extends Controller {
  private val loginForm = Form(
    mapping(
      "username" -> text.verifying(_.length > 0),
      "password" -> text.verifying(t => t.length <= 72 && t.length > 0) // 72 is pgcrypt blowfish maximum length
    )(LoginForm.apply)(LoginForm.unapply)
  )

  def postLogin: Action[AnyContent] = Action.async { implicit request =>
    loginForm.bindFromRequest.fold(
      formWithErrors => Future.successful(BadRequest(formWithErrors.toString)),
      lForm => {
        val valid = uc.login(User(lForm.username), lForm.password)
        valid.map {
          case true => Redirect(routes.PatternController.index()).withSession(request.session + ("username" -> lForm.username))
          case false => Unauthorized(views.html.login.login(loginForm, retry = true))
        }
      }
    )
  }

  def login(retry: Boolean = false): Action[AnyContent] = Action { implicit request =>
    if (request.session.get("username").isDefined) Redirect(routes.PatternController.index())
    else Ok(views.html.login.login(loginForm, retry))
  }

  def logout: Action[AnyContent] = Action { implicit request =>
    Redirect(routes.PatternController.index()).withNewSession
  }
}
