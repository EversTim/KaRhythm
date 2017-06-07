package nl.sogyo.kbd.db

import nl.sogyo.kbd.domain.Sound

import javax.inject._
import play.api.db.Database
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class SoundDB @Inject()(db: Database) extends SoundCollection {
  def get(id: Int): Future[Option[Sound]] = Future {
    db.withConnection { conn =>

      val stmt = conn.createStatement
      val rs = stmt.executeQuery(s"SELECT name, file_location FROM sounds WHERE sound_id=$id;")

      var soundName: String = null
      var soundLocation: String = null
      var hasAnswer = false
      while (rs.next()) {
        hasAnswer = true
        soundName = rs.getString(1)
        soundLocation = rs.getString(2)
      }
      if (hasAnswer) Some(Sound(soundName, soundLocation))
      else None
    }
  }

  def get(name: String): Future[Option[Sound]] = Future {
    db.withConnection { conn =>

      val stmt = conn.createStatement
      val rs = stmt.executeQuery(s"SELECT name, file_location FROM sounds WHERE name='$name;'")

      var soundName: String = null
      var soundLocation: String = null
      var hasAnswer = false
      while (rs.next()) {
        hasAnswer = true
        soundName = rs.getString(1)
        soundLocation = rs.getString(2)
      }
      if (hasAnswer) Some(Sound(soundName, soundLocation))
      else None
    }
  }
}
