package nl.sogyo.kbd

import scala.collection.IndexedSeq
import scala.concurrent.Future

trait PatternCollection {
  def numberOfTestPatterns = 3

  // Default patterns
  final def addDefaults(): Unit = IndexedSeq(
    Pattern(Track(true, false, true, false), Track(false, true, false, true)),
    Pattern(Track(true, false, true, false), Track(false, true, false, true), Track(true, true, true, true)),
    Pattern(Track(true, false, true, false, true), Track(false, true, false, true, false), Track(true, true, true, true, false))
  ).foreach(post)

  def get(id: Int): Future[Option[Pattern]]

  def post(p: Pattern): Future[Int]
}
