package nl.sogyo.kbd.db

import nl.sogyo.kbd.domain.Pattern

import scala.concurrent._

trait PatternCollection {
  def numberOfTestPatterns = 3

  def get(id: Int): Future[Option[Pattern]]

  def post(p: Pattern): Future[Int]
}