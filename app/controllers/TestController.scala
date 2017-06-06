package controllers

import javax.inject._
import play.api.mvc._

import nl.sogyo.kbd._

@Singleton
class TestController @Inject()(pc: PatternCollection) extends PatternController(pc) {

  def test(id: Int): Action[AnyContent] =
    if(id < pc.numberOfTestPatterns) fromID(id)
    else Action(NotFound)

}