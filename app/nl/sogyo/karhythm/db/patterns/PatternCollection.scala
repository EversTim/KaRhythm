package nl.sogyo.karhythm.db.patterns

import nl.sogyo.karhythm.domain.Pattern
import nl.sogyo.karhythm.users.User

import scala.concurrent._

trait PatternCollection {
  def numberOfTestPatterns = 3

  def select(id: Int): Future[Option[Pattern]]

  def insert(p: Pattern, u: User): Future[Int]

  def findByUser(user: User): Future[Seq[Pattern]]
}
