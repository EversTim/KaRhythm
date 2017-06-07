package nl.sogyo.kbd

case class Track(name: String, sound: Sound, data: Boolean*) {

  if(data.isEmpty)
    throw ZeroLengthTrackException(data.toString)

  val length: Int = data.length
}
