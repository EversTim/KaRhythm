package nl.sogyo.kbd

case class Pattern(data: Track*) {

  val length: Int = data.foldLeft(Int.MinValue)((acc, s) => acc.max(s.length))

  val tracks: Int = data.size

  if (data.isEmpty)
    throw NoTracksDefinedException(data.toString)

  if (data.exists(_.length != data.head.length))
    throw UnevenLengthException(data.toString)
}