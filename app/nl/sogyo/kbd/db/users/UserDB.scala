package nl.sogyo.kbd.db.users

import java.sql.Connection
import javax.inject._

import nl.sogyo.kbd.db.exceptions.MultipleUpdateException
import nl.sogyo.kbd.users.User
import play.api.db.Database

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserDB @Inject()(db: Database) extends UserCollection {

  private val encryptionType = "bf"
  private val rounds = "8"
  private val gen_salt: String = s"gen_salt('$encryptionType', $rounds)"

  def addUser(user: User, plainPassword: String): Future[Boolean] = Future {
    db.withTransaction { conn =>
      val usernameExists = doCheckIfUsernameExists(user, conn)
      if (!usernameExists) doAddUser(user, plainPassword, conn)
      else false
    }
  }

  private def doAddUser(user: User, plainPassword: String, conn: Connection): Boolean = {
    val preparedString =
      s"""
         |INSERT INTO users
         |VALUES (?, crypt(?, $gen_salt));
          """.stripMargin
    val statement = conn.prepareStatement(preparedString)
    statement.setString(1, user.username)
    statement.setString(2, plainPassword)
    val rowsAdded = statement.executeUpdate
    rowsAdded == 1
  }

  def checkIfUsernameExists(user: User): Future[Boolean] = Future {
    db.withConnection { conn =>
      doCheckIfUsernameExists(user, conn)
    }
  }

  private def doCheckIfUsernameExists(user: User, conn: Connection): Boolean = {
    val preparedString =
      """
        |SELECT user_id
        |FROM users
        |WHERE username = ?;
      """.
        stripMargin
    val statement = conn.prepareStatement(preparedString)
    statement.setString(1, user.username)
    val resultSet = statement.executeQuery
    resultSet.first()
  }

  def updatePassword(user: User, oldPlainPassword: String, newPlainPassword: String): Future[Boolean] = Future {
    db.withTransaction { conn =>
      val login = doLogin(user, oldPlainPassword, conn)
      if (login) doUpdatePassword(user, newPlainPassword, conn)
      else false
    }
  }

  private def doUpdatePassword(user: User, newPlainPassword: String, conn: Connection): Boolean = {
    val preparedString =
      s"""
         |UPDATE users
         |SET password = crypt(?, $gen_salt)
         |WHERE username = ?;
         """.stripMargin
    val statement = conn.prepareStatement(preparedString)
    statement.setString(1, newPlainPassword)
    statement.setString(2, user.username)
    val rowsChanged = statement.executeUpdate
    if (rowsChanged == 1) true
    else if (rowsChanged == 0) false
    else throw
      MultipleUpdateException("Multiple passwords were about to be updated!")
  }

  private def doLogin(user: User, plainPassword: String, conn: Connection): Boolean = {
    val preparedString =
      """
        |SELECT (password = crypt(?, password)) AS pwmatch
        |FROM users
        |WHERE username = ?;
      """.
        stripMargin
    val statement = conn.prepareStatement(preparedString)
    statement.setString(1, user.username)
    statement.setString(2, plainPassword)
    val resultSet = statement.executeQuery
    if(resultSet.next()) {
      resultSet.getBoolean("pwmatch")
    } else false
  }

  def login(user: User, plainPassword: String): Future[Boolean] = Future {
    db.withConnection { conn =>
      doLogin(user, plainPassword, conn)
    }
  }
}
