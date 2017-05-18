package nl.sogyo.kbd

case class Pattern(data: Seq[Seq[Boolean]]) {
  if (data.isEmpty)
    throw NoTracksDefinedException(data.toString)

  if (data.exists(_.isEmpty))
    throw ZeroLengthTrackException(data.toString)

  val length: Int = data.foldLeft(Int.MinValue)((acc, s) => acc.max(s.length))

  if (data.exists(_.size != length))
    throw UnevenLengthException(data.toString)

  val tracks: Int = data.size
}

object Pattern {
  def testPatterns: IndexedSeq[Pattern] = IndexedSeq(
    Pattern(Vector(Vector(true, false, true, false), Vector(false, true, false, true))),
    Pattern(Vector(Vector(true, false, true, false), Vector(false, true, false, true), Vector(true, true, true, true))),
    Pattern(Vector(Vector(true, false, true, false, true), Vector(false, true, false, true, false), Vector(true, true, true, true, false)))
  )

  def defaultPattern: Pattern = Pattern(Vector(Vector(true, false, true, false), Vector(false, true, false, true)))
}