package controllers

import javax.inject._

import play.api.mvc._
import nl.sogyo.kbd._
import nl.sogyo.kbd.db.{PatternCollection, SoundCollection}
import nl.sogyo.kbd.domain.{Pattern, Track}

import scala.concurrent._
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(pc: PatternCollection, sc: SoundCollection) extends PatternController(pc) {

  def index: Action[AnyContent] = {
    from("Default")
  }

  def postPattern: Action[AnyContent] = Action.async { implicit request =>
    patternForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(formWithErrors.toString))
      },
      pf => {
        val p = Pattern(pf.name, pf.data.zip(pf.trackNames).zip(pf.trackSounds).map {
          case ((d, n), s) => Track(n, Await.result(sc.get(s), 100.millis).get, d: _*)
        }:_*)
        val fid = pc.post(p)
        fid.map(id => Redirect(routes.HomeController.from(id)))
      }
    )
  }
}
