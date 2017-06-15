package nl.sogyo.kbd.actionsrequests

import play.api.mvc._

import scala.concurrent.Future

class UserRequest[A](val username: Option[String], request: Request[A])
  extends WrappedRequest[A](request)

object UserAction
  extends ActionBuilder[UserRequest] with ActionTransformer[Request, UserRequest] {
  protected def transform[A](request: Request[A]): Future[UserRequest[A]] = Future.successful {
    new UserRequest(request.session.get("username"), request)
  }
}
