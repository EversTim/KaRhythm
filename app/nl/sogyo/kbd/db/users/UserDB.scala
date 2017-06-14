package nl.sogyo.kbd.db.users

import javax.inject._

import nl.sogyo.kbd.db.exceptions.MultipleUpdateException
import play.api.db.Database

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class UserDB @Inject()(db: Database) extends UserCollection {
  def checkIfEmailExists(email: String): Future[Boolean] = Future {
    db.withConnection { conn =>
      val preparedString =
        """
          |SELECT user_id
          |FROM users
          |WHERE email = ?;
        """.
          stripMargin
      val statement = conn.prepareStatement(preparedString)
      statement.setString(1, email)
      val resultSet = statement.executeQuery
      resultSet.first()
    }
  }

  def addUser(email: String, plainPassword: String): Future[Boolean] = Future {
    db.withConnection { conn =>
      val preparedString =
        """
          |INSERT INTO users
          |VALUES (?, crypt(?, gen_salt('bf', 8)));
        """.stripMargin
      val statement = conn.prepareStatement(preparedString)
      statement.setString(1, email)
      statement.setString(2, plainPassword)
      val rowsAdded = statement.executeUpdate
      rowsAdded == 1
    }
  }

  def updatePassword(email: String, oldPlainPassword: String, newPlainPassword: String): Future[Boolean] =
    login(email, oldPlainPassword).flatMap { e =>
      if (e) {
        Future {
          db.withTransaction { conn =>
            val preparedString =
              """
                |UPDATE users
                |SET password = crypt(?, gen_salt('bf', 8))
                |WHERE email = ?;
              """.stripMargin
            val statement = conn.prepareStatement(preparedString)
            statement.setString(1, newPlainPassword)
            statement.setString(2, email)
            val rowsChanged = statement.executeUpdate
            if (rowsChanged == 1) true
            else if (rowsChanged == 0) false
            else throw MultipleUpdateException("Multiple passwords were about to be updated!")
          }
        }
      } else Future.successful(false)
    }

  def login(email: String, plainPassword: String): Future[Boolean] = Future {
    db.withConnection { conn =>
      val preparedString =
        """
          |SELECT *
          |FROM USERS
          |WHERE email = ?
          |AND password = crypt(?, password);
        """.stripMargin
      val statement = conn.prepareStatement(preparedString)
      statement.setString(1, email)
      statement.setString(2, plainPassword)
      val resultSet = statement.executeQuery
      resultSet.first()
    }
  }
}
