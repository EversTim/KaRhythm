package controllers

import javax.inject._

import play.api.mvc._
import nl.sogyo.karhythm.db.patterns.PatternCollection
import nl.sogyo.karhythm.db.sounds.SoundCollection

@Singleton
class TestController @Inject()(pc: PatternCollection, sc: SoundCollection) extends PatternController(pc, sc) {

  def test(id: Int): Action[AnyContent] =
    if(id <= pc.numberOfTestPatterns) fromPatternID(id)
    else Action(NotFound)

}