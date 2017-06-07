package nl.sogyo.kbd.db

import java.sql.PreparedStatement
import javax.inject._

import nl.sogyo.kbd.domain._
import play.api.db.Database

import scala.annotation.tailrec
import scala.concurrent.Future
import scala.collection._
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PatternDB @Inject()(db: Database) extends PatternCollection {

  import PatternDB._

  def get(id: Int): Future[Option[Pattern]] = Future {
    db.withConnection { conn =>

      val stmt = conn.prepareStatement("SELECT p.name AS pname, t.name AS tname, t.data, s.name AS sname, s.file_location FROM patterns AS p JOIN tracks AS t ON p.pattern_id = t.pattern_id JOIN sounds AS s ON t.sound_id = s.sound_id WHERE p.pattern_id = ?;")
      stmt.setInt(1, id)
      val rs = stmt.executeQuery

      val tracks = mutable.Buffer[Track]()
      var patternName: String = null
      var hasAnswer = false
      while (rs.next()) {
        hasAnswer = true
        patternName = rs.getString(1)
        val trackName = rs.getString(2)
        val data = stringToBoolSeq(rs.getString(3))
        val soundName = rs.getString(4)
        val soundLocation = rs.getString(5)
        tracks += Track(trackName, Sound(soundName, soundLocation), data: _*)
      }
      if (hasAnswer) Some(Pattern(patternName, tracks: _*))
      else None
    }
  }

  def post(p: Pattern): Future[Int] = Future {
    db.withConnection{ conn =>
      val stmt = conn.createStatement
      stmt.executeUpdate(s"INSERT INTO ")
      ???
    }
  }
}

object PatternDB {
  def boolSeqToString(bs: Seq[Boolean]): String = bs.foldLeft("")((acc, bl) => acc + (if (bl) "1" else "0"))

  def stringToBoolSeq(s: String): Seq[Boolean] = {
    @tailrec
    def helper(s: String, acc: Seq[Boolean]): Seq[Boolean] = {
      if (s.isEmpty) acc
      else helper(s.tail, acc :+ (s.head == '1'))
    }

    helper(s, Nil)
  }

  def longToHex(l: Long): String = l.toHexString

  def hexToLong(input: String): Long =
    makeEven(input).grouped(2).toVector.map(Integer.parseInt(_, 16).toByte).foldLeft(0L)((acc, b) => (acc << 8) | b)

  private def makeEven(input: String): String =
    if (input.length % 2 == 0) input
    else "0" + input
}
