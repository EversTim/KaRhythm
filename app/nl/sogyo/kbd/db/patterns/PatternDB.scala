package nl.sogyo.kbd.db.patterns

import java.sql.{PreparedStatement, ResultSet, Statement}
import javax.inject._

import nl.sogyo.kbd.domain._
import nl.sogyo.kbd.users.User
import play.api.db.Database

import scala.collection._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class PatternDB @Inject()(db: Database) extends PatternCollection {

  import PatternDB._

  def select(id: Int): Future[Option[Pattern]] = Future {
    db.withConnection { conn =>

      val preparedString =
        """
          |SELECT p.name AS pname, t.name AS tname, t.data, s.name AS sname, s.file_location
          |FROM patterns AS p JOIN tracks AS t ON p.pattern_id = t.pattern_id JOIN sounds AS s ON t.sound_id = s.sound_id
          |WHERE p.pattern_id = ?;
        """.stripMargin
      val statement = conn.prepareStatement(preparedString)
      statement.setInt(1, id)
      val resultSet = statement.executeQuery
      val hasResult = resultSet.next()
      if (hasResult) singlePatternFromResultSet(resultSet)
      else None
    }
  }

  // Expects input of the form "pattern name, track name, sound name, sound file location".
  // with result set cursor set on the first row of the pattern.
  // It leaves the cursor after the last row of the pattern.
  private def singlePatternFromResultSet(resultSet: ResultSet): Option[Pattern] = {
    val tracks = mutable.Buffer[Track]()

    val patternName: String = resultSet.getString(1)
    var curPName: String = null
    do {
      curPName = resultSet.getString(1)
      if (curPName == patternName) {
        val trackName = resultSet.getString(2)
        val data = stringToBoolSeq(resultSet.getString(3))
        val soundName = resultSet.getString(4)
        val soundLocation = resultSet.getString(5)
        tracks += Track(trackName, Sound(soundName, soundLocation), data: _*)
      }
    } while (resultSet.next() && (patternName == curPName))

    Some(Pattern(patternName, tracks: _*))
  }

  def insert(p: Pattern, u: User): Future[Int] = Future {
    db.withTransaction { conn =>
      val patternString =
        """
          |INSERT INTO patterns (name, username)
          |VALUES (?, ?);
        """.stripMargin
      val patternStatement = conn.prepareStatement(patternString, Statement.RETURN_GENERATED_KEYS)
      patternStatement.setString(1, p.name)
      patternStatement.setString(2, u.username)
      patternStatement.executeUpdate
      val patternResult = patternStatement.getGeneratedKeys
      patternResult.next()
      val patternID = patternResult.getInt(1)
      val trackString =
        """
          |INSERT INTO tracks (name, data, pattern_id, sound_id)
          |VALUES (?, ?, ?, ?);
        """.stripMargin
      val trackStatement = conn.prepareStatement(trackString)
      val soundString =
        """
          |SELECT sound_id
          |FROM sounds
          |WHERE name = ?;
        """.
          stripMargin
      val soundStatement = conn.prepareStatement(soundString)
      for (track <- p.data) {
        insertTrack(trackStatement, soundStatement, patternID, track)
      }

      patternID
    }
  }

  private def insertTrack(trackStatement: PreparedStatement, soundStatement: PreparedStatement, patternID: Int, track: Track) = {
    trackStatement.clearParameters()
    val soundID = findSoundID(soundStatement, track.sound)
    trackStatement.setString(1, track.name)
    trackStatement.setString(2, boolSeqToString(track.data))
    trackStatement.setLong(3, patternID)
    trackStatement.setLong(4, soundID)
    trackStatement.executeUpdate
  }

  private def findSoundID(soundStatement: PreparedStatement, sound: Sound) = {
    soundStatement.clearParameters()
    soundStatement.setString(1, sound.name)
    val resultSet = soundStatement.executeQuery
    resultSet.next()
    resultSet.getInt(1)
  }

  def findByUser(user: User): Future[Seq[Pattern]] = Future {
    db.withConnection { conn =>
      val patternString =
        """
          |SELECT pattern_id
          |FROM patterns
          |WHERE username = ?;
        """.stripMargin
      val patternStatement = conn.prepareStatement(patternString)
      patternStatement.setString(1, user.username)
      val resultSet = patternStatement.executeQuery
      val hasResults = resultSet.next()
      if(!hasResults) Seq.empty[Pattern]
      else {
        val outputOpt= new mutable.ArrayBuffer[Option[Pattern]]()
        do {
          outputOpt += singlePatternFromResultSet(resultSet)
        } while (outputOpt.last.isDefined)
        outputOpt.map{
          case Some(p) => p
          case None => Nil
        }
      }
    }
  }
}

object PatternDB {
  def boolSeqToString(bs: Seq[Boolean]): String = bs.foldLeft("")((acc, bl) => acc + (if (bl) "1" else "0"))

  def stringToBoolSeq(s: String): Seq[Boolean] = s.foldLeft(Seq.empty[Boolean])((acc, c) => acc :+ (c == '1'))
}
