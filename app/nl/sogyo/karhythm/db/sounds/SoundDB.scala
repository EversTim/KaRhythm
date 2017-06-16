package nl.sogyo.karhythm.db.sounds

import javax.inject._

import nl.sogyo.karhythm.domain.Sound
import play.api.db.Database

import scala.collection._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class SoundDB @Inject()(db: Database) extends SoundCollection {
  def get(id: Int): Future[Option[Sound]] = Future {
    db.withConnection { conn =>

      val stmt = conn.prepareStatement("SELECT name, file_location FROM sounds WHERE sound_id = ?;")
      stmt.setInt(1, id)
      val rs = stmt.executeQuery

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

      val stmt = conn.prepareStatement("SELECT name, file_location FROM sounds WHERE name = ?;")
      stmt.setString(1, name)
      val rs = stmt.executeQuery

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

  def getAllNames: Future[Seq[String]] = Future {
    db.withConnection { conn =>
      val stmt = conn.createStatement
      val rs = stmt.executeQuery("SELECT name FROM sounds")
      var buf = mutable.Buffer[String]()
      while(rs.next()) {
        buf += rs.getString(1)
      }
      buf
    }
  }
}
