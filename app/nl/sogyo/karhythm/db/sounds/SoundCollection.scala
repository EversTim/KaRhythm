package nl.sogyo.karhythm.db.sounds

import nl.sogyo.karhythm.domain.Sound

import scala.concurrent.Future

trait SoundCollection {
  def get(id: Int): Future[Option[Sound]]
  def get(name: String): Future[Option[Sound]]
  def getAllNames: Future[Seq[String]]
}
