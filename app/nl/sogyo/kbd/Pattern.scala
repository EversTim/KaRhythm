package nl.sogyo.kbd

case class Pattern private (data: Seq[Seq[Boolean]]) {
  if(data.isEmpty)
    throw NoTracksDefinedException(data.toString)

  if(data.exists(_.isEmpty))
    throw ZeroLengthTrackException(data.toString)

  val length: Int = data.foldLeft(Int.MinValue)((acc, s) => acc.max(s.length))

  if(data.exists(_.size != length))
    throw UnevenLengthException(data.toString)

  val tracks: Int = data.size
}