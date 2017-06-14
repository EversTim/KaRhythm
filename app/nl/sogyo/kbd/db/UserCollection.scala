package nl.sogyo.kbd.db

import nl.sogyo.kbd.users.User

trait UserCollection {
  def nameExists(username: String): Boolean

  def getHashedPassword(username: String): String

  def addUser(user: User): Boolean
}
