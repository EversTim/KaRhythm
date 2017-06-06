package nl.sogyo.kbd

case class Pattern(data: Seq[Seq[Boolean]], length: Int, tracks: Int) {
  def this(data: Seq[Seq[Boolean]]) =
    this(data, data.foldLeft(Int.MinValue)((acc, s) => acc.max(s.length)), data.size)

  if (data.isEmpty)
    throw NoTracksDefinedException(data.toString)

  if (data.exists(_.isEmpty))
    throw ZeroLengthTrackException(data.toString)

  if (data.exists(_.size != length))
    throw UnevenLengthException(data.toString)
}

object Pattern {
  def apply(data: Seq[Seq[Boolean]]) = new Pattern(data)
}