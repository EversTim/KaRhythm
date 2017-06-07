package nl.sogyo.kbd.db

import nl.sogyo.kbd.domain.Pattern

import scala.concurrent._

trait PatternCollection {
  def numberOfTestPatterns = 3

  protected def sc: SoundCollection

  def get(id: Int): Future[Option[Pattern]]

  def get(name: String): Future[Option[Pattern]]

  def post(p: Pattern): Future[Int]
}
