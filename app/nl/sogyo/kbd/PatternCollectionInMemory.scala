package nl.sogyo.kbd

import javax.inject._

import scala.collection._

@Singleton
class PatternCollectionInMemory extends PatternCollection {
  private val patterns: mutable.Map[Int, Pattern] = mutable.Map()
  private var curID = -1

  // Default patterns
  IndexedSeq(
    Pattern(Vector(Vector(true, false, true, false), Vector(false, true, false, true))),
    Pattern(Vector(Vector(true, false, true, false), Vector(false, true, false, true), Vector(true, true, true, true))),
    Pattern(Vector(Vector(true, false, true, false, true), Vector(false, true, false, true, false), Vector(true, true, true, true, false)))
  ).foreach(this.post)

  def get(id: Int): Option[Pattern] = patterns.get(id)

  def post(p: Pattern): Int =
    patterns.find { case (_, pa) => pa == p } match {
      case Some((i, _)) => i
      case None =>
        curID = curID + 1
        patterns += curID -> p
        curID
    }
}
