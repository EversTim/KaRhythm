package controllers

import javax.inject._

import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.Play.current
import play.api.i18n.Messages.Implicits._
import nl.sogyo.kbd.db.patterns.PatternCollection
import nl.sogyo.kbd.db.sounds.SoundCollection
import nl.sogyo.kbd.domain.{Pattern, Track}
import nl.sogyo.kbd.forms._
import nl.sogyo.kbd.actionsrequests._
import nl.sogyo.kbd.users.User

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PatternController @Inject()(pc: PatternCollection, sc: SoundCollection) extends Controller {

  private val patternForm = Form(
    mapping(
      "name" -> text,
      "boxes" -> seq(seq(boolean)),
      "tracknames" -> seq(text),
      "soundnames" -> seq(text),
      "length" -> number.verifying(_ > 0),
      "tracks" -> number.verifying(_ > 0)
    )(PatternForm.apply)(PatternForm.unapply)
  )

  def index: Action[AnyContent] = fromPatternID(1)

  def fromPatternID(patternID: Int): Action[AnyContent] = (UserAction andThen PatternIDAction(patternID)).async { implicit request =>
    createResult(request.pattern)
  }

  def PatternIDAction(patternID: Int) = new ActionRefiner[UserRequest, PatternRequest] {
    def refine[A](request: UserRequest[A]): Future[Either[Result, PatternRequest[A]]] =
      pc.select(patternID).map {
        case Some(p) => Right(new PatternRequest(p, request))
        case None => Left(NotFound("404 error: ID " + patternID + " not found."))
      }
  }

  def createResult(p: Pattern)(implicit request: PatternRequest[AnyContent]): Future[Result] = {
    val filledForm = patternForm.fill(PatternForm(p.name, p.data.map(_.data), p.data.map(_.name), p.data.map(_.sound.name), p.length, p.tracks))
    val soundMap = p.generateSoundMap
    sc.getAllNames.map(_.sorted).map(_.map(Some(_))).map(names => Ok(views.html.pattern.index(Some(soundMap), Some(names), filledForm, request.username)))
  }

  def postPattern: Action[AnyContent] = UserAction.async { implicit request =>
    patternForm.bindFromRequest.fold(
      formWithErrors => {
        Future.successful(BadRequest(views.html.pattern.index(None, None, formWithErrors, request.username)))
      },
      pForm => {
        val pattern: Future[Pattern] = makePatternFromForm(pForm)
        val patternID = pattern.flatMap(pc.insert(_, User("public")))
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
