package controllers

import javax.inject._

import play.api.mvc._
import nl.sogyo.kbd._
import play.api.db._

@Singleton
class HomeController @Inject()(pc: PatternCollection) extends PatternController(pc) {

  def index: Action[AnyContent] = {
    fromID(0)
  }

  def postPattern: Action[AnyContent] = Action { implicit request =>
    patternForm.bindFromRequest.fold(
      formWithErrors => {
        BadRequest(formWithErrors.toString)
      },
      pf => {
        val p = Pattern(pf.data, pf.length, pf.tracks)
        val id = pc.post(p)
        Redirect(routes.HomeController.fromID(id))
      }
    )
  }
}
