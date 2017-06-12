package controllers

import javax.inject._

import play.api.mvc._
import nl.sogyo.kbd.db.{PatternCollection, SoundCollection}

@Singleton
class TestController @Inject()(pc: PatternCollection, sc: SoundCollection) extends PatternController(pc, sc) {

  def test(id: Int): Action[AnyContent] =
    if(id <= pc.numberOfTestPatterns) from(id)
    else Action(NotFound)

}