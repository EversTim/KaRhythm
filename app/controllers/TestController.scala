package controllers

import javax.inject._
import play.api.mvc._

@Singleton
class TestController @Inject() extends PatternController {

  def test(id: Int): Action[AnyContent] =
    if(id < nl.sogyo.kbd.PatternList.numberOfTestPatterns) fromID(id)
    else Action(NotFound)

}