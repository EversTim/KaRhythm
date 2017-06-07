package controllers

import javax.inject._

import play.api.mvc._
import nl.sogyo.kbd._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(pc: PatternCollection) extends PatternController(pc) {

  def index: Action[AnyContent] = {
    fromID(0)
  }

  def postPattern: Action[AnyContent] = Action.async { implicit request =>
    patternForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(formWithErrors.toString))
      },
      pf => {
        val p = Pattern(pf.data.map(ss => Track(ss:_*)):_*)
        val fid = pc.post(p)
        fid.map(id => Redirect(routes.HomeController.fromID(id)))
      }
    )
  }
}
