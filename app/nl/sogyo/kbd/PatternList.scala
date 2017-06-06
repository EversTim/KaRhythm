package nl.sogyo.kbd

import scala.collection.mutable.Map

object PatternList {
  val numberOfTestPatterns = 3
  private val patterns: Map[Int, Pattern] = Map()
  private var curID = 0

  // Default patterns
  IndexedSeq(
    Pattern(Vector(Vector(true, false, true, false), Vector(false, true, false, true))),
    Pattern(Vector(Vector(true, false, true, false), Vector(false, true, false, true), Vector(true, true, true, true))),
    Pattern(Vector(Vector(true, false, true, false, true), Vector(false, true, false, true, false), Vector(true, true, true, true, false)))
  ).foreach(this.apply)

  def apply(id: Int): Option[Pattern] = get(id)

  def get(id: Int): Option[Pattern] = patterns.get(id)

  def apply(p: Pattern): Int = post(p)

  def post(p: Pattern): Int = {
    val idx = patterns.find { case (_, pa) => pa == p }
    idx match {
      case Some((i, _)) => i
      case None =>
        val toSaveAt = curID
        patterns += toSaveAt -> p
        curID = curID + 1
        toSaveAt
    }
  }
}
