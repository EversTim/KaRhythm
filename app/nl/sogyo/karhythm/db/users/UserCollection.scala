package nl.sogyo.karhythm.db.users

import nl.sogyo.karhythm.users._
import scala.concurrent.Future

trait UserCollection {
  def checkIfUsernameExists(user: User): Future[Boolean]

  def addUser(user: User, plainPassword: String): Future[Boolean]

  def updatePassword(user: User, oldPlainPassword: String, newPlainPassword: String): Future[Boolean]

  def login(user: User, plainPassword: String): Future[Boolean]
}
