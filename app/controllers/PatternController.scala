package controllers

import javax.inject._

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import nl.sogyo.kbd.db._
import nl.sogyo.kbd.domain.{Pattern, Track}
import nl.sogyo.kbd.forms._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PatternController @Inject()(pc: PatternCollection, sc: SoundCollection) extends Controller {

  val patternForm = Form(
    mapping(
      "name" -> text,
      "boxes" -> seq(seq(boolean)),
      "tracknames" -> seq(text),
      "soundnames" -> seq(text),
      "length" -> number.verifying(_ > 0),
      "tracks" -> number.verifying(_ > 0)
    )(PatternForm.apply)(PatternForm.unapply)
  )

  def index: Action[AnyContent] = {
    fromPatternID(1)
  }

  def fromPatternID(patternID: Int): Action[AnyContent] = Action.async { implicit request =>
    pc.get(patternID).flatMap {
      case Some(p) => createResult(p)
      case None => Future.successful(NotFound("404 error: ID " + patternID + " not found."))
    }
  }

  def createResult(p: Pattern)(implicit rc: RequestHeader): Future[Result] = {
    val filledForm = patternForm.fill(PatternForm(p.name, p.data.map(_.data), p.data.map(_.name), p.data.map(_.sound.name), p.length, p.tracks))
    val soundMap = p.generateSoundMap
    sc.getAllNames.map(_.sorted).map(_.map(Some(_))).map(names => Ok(views.html.index(Some(soundMap), Some(names), filledForm)))
  }

  def postPattern: Action[AnyContent] = Action.async { implicit request =>
    patternForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.index(None, None, formWithErrors)))
      },
      pForm => {
        val pattern: Future[Pattern] = makePatternFromForm(pForm)
        val patternID = pattern.flatMap(pc.post)
        patternID.map(id => Redirect(routes.PatternController.fromPatternID(id)))
      }
    )
  }

  def makePatternFromForm(pForm: PatternForm): Future[Pattern] = {
    val tracks: Future[Seq[Track]] = Future.sequence(for {
      ((data, name), soundName) <- pForm.data.zip(pForm.trackNames).zip(pForm.trackSounds)
    } yield sc.get(soundName).map(soundOption => Track(name, soundOption.get, (data ++ Seq.fill(pForm.length - data.length)(false)).take(pForm.length): _*)))
    val paddedTracks = tracks.map(ts => (ts ++ Seq.fill(pForm.tracks - ts.length)(Track.empty(pForm.length))).take(pForm.tracks))
    paddedTracks.map(ts => Pattern(pForm.name, ts: _*))
  }
}
