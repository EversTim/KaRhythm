package nl.sogyo.kbd.domain

case class Pattern(name: String, data: Track*) {

  val length: Int = data.foldLeft(Int.MinValue)((acc, s) => acc.max(s.length))

  val tracks: Int = data.size

  if (data.isEmpty)
    throw NoTracksDefinedException(data.toString)

  if (data.exists(_.length != data.head.length))
    throw UnevenLengthException(data.toString)

  def generateSoundMap: Map[Int, String] = data.zipWithIndex.map {
    case (d, i) => i -> d.sound.location
  }.toMap
}