package nl.sogyo.kbd.db

import nl.sogyo.kbd.domain.Sound

import scala.concurrent.Future

trait SoundCollection {
  def get(id: Int): Future[Option[Sound]]
  def get(name: String): Future[Option[Sound]]
  def getAllNames: Future[Seq[String]]
}
