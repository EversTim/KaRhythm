package nl.sogyo.kbd.db.tables

import slick.driver.PostgresDriver.api._

class Sounds(tag: Tag) extends Table[(Int, String, String)](tag, "sounds") {
  def * = (soundID, name, fileLocation)

  def soundID: Rep[Int] = column[Int]("sound_id", O.PrimaryKey, O.AutoInc)

  def name: Rep[String] = column[String]("name")

  def fileLocation: Rep[String] = column[String]("file_location")
}
