package nl.sogyo.kbd.db.users

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.collection.mutable

object UserCollectionMockNoHashes extends UserCollection {
  private val db: mutable.Map[String, String] = mutable.Map[String, String]()

  def checkIfEmailExists(email: String): Future[Boolean] = Future.successful(db.contains(email))

  def addUser(email: String, plainPassword: String): Future[Boolean] = checkIfEmailExists(email).map{ e =>
    if(e) false else {
      db += (email -> plainPassword)
      true
    }
  }

  def updatePassword(email: String, oldPlainPassword: String, newPlainPassword: String): Future[Boolean] =
    login(email, oldPlainPassword)

  def login(email: String, plainPassword: String): Future[Boolean] =
    checkIfEmailExists(email).map(e => if(e) {
      plainPassword == db(email)
    } else false)
}
