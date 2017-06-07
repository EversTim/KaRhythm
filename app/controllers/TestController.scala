package controllers

import javax.inject._

import play.api.mvc._
import nl.sogyo.kbd._
import nl.sogyo.kbd.db.PatternCollection

@Singleton
class TestController @Inject()(pc: PatternCollection) extends PatternController(pc) {

  def test(id: Int): Action[AnyContent] =
    if(id < pc.numberOfTestPatterns) from(id)
    else Action(NotFound)

}