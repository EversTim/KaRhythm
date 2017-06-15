package nl.sogyo.kbd.actionsrequests

import nl.sogyo.kbd.domain.Pattern
import play.api.mvc._

class PatternRequest[A](val pattern: Pattern, request: UserRequest[A])
  extends WrappedRequest[A](request) {
  def username: Option[String] = request.username
}
