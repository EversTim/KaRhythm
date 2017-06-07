package nl.sogyo.kbd.db.tables

import slick.driver.PostgresDriver.api._
import slick.model.ForeignKeyAction

class Tracks(tag: Tag) extends Table[(Int, String, Int, Int)](tag, "tracks") {
  def * = (trackID, name, patternID, soundID)

  def trackID: Rep[Int] = column[Int]("track_id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def patternID: Rep[Int] = column[Int]("pattern_id")

  def soundID: Rep[Int] = column[Int]("sound_id")

  def pattern =
    foreignKey("pattern_id", patternID, patterns)(_.patternID)

  def sound =
    foreignKey("sound_id", soundID, sounds)(_.soundID)
}
