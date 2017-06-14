package nl.sogyo.kbd.db.users

import scala.concurrent.Future

trait UserCollection {
  def checkIfEmailExists(email: String): Future[Boolean]

  def addUser(email: String, plainPassword: String): Future[Boolean]

  def updatePassword(email: String, oldPlainPassword: String, newPlainPassword: String): Future[Boolean]

  def login(email: String, plainPassword: String): Future[Boolean]
}
