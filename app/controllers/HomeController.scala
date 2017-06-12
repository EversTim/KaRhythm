package controllers

import javax.inject._

import play.api.mvc._
import nl.sogyo.kbd.db.{PatternCollection, SoundCollection}
import nl.sogyo.kbd.domain.{Pattern, Track}

import scala.concurrent._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class HomeController @Inject()(pc: PatternCollection, sc: SoundCollection) extends PatternController(pc, sc) {

  def index: Action[AnyContent] = {
    from(1)
  }

  def postPattern: Action[AnyContent] = Action.async { implicit request =>
    patternForm.bindFromRequest.fold(
      formWithErrors => {
        Future(BadRequest(formWithErrors.toString))
      },
      pForm => {
        val pattern: Future[Pattern] = {
          val tracks: Future[Seq[Track]] = Future.sequence(for {
            ((data, name), soundName) <- pForm.data.zip(pForm.trackNames).zip(pForm.trackSounds)
          } yield sc.get(soundName).map(soundOption => Track(name, soundOption.get, data: _*)))
          tracks.map(ts => Pattern(pForm.name, ts: _*))
        }
        val fid = pattern.flatMap(pc.post)
        fid.map(id => Redirect(routes.HomeController.from(id)))
      }
    )
  }
}
