package nl.sogyo.kbd.db.tables

import slick.driver.PostgresDriver.api._

class Patterns(tag: Tag) extends Table[(Int, String)](tag, "patterns") {
  def * = (patternID, name)

  def patternID: Rep[Int] = column[Int]("pattern_id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")
}
