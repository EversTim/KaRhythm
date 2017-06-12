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
    fromPatternID(1)
  }

  def postPattern: Action[AnyContent] = Action.async { implicit request =>
    patternForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(formWithErrors.toString))
      },
      pForm => {
        val pattern: Future[Pattern] = {
          val tracks: Future[Seq[Track]] = Future.sequence(for {
            ((data, name), soundName) <- pForm.data.zip(pForm.trackNames).zip(pForm.trackSounds)
          } yield sc.get(soundName).map(soundOption => Track(name, soundOption.get, (data ++ Seq.fill(pForm.length - data.length)(false)).take(pForm.length): _*)))
          val paddedTracks = tracks.map(ts => (ts ++ Seq.fill(pForm.tracks - ts.length)(Track.empty(pForm.length))).take(pForm.tracks))
          paddedTracks.map(ts => Pattern(pForm.name, ts: _*))
        }
        val fid = pattern.flatMap(pc.post)
        fid.map(id => Redirect(routes.HomeController.fromPatternID(id)))
      }
    )
  }
}