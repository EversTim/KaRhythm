package nl.sogyo.kbd.domain

case class Track(name: String, sound: Sound, data: Boolean*) {

  if(data.isEmpty)
    throw ZeroLengthTrackException(data.toString)

  val length: Int = data.length

  if(length > 64)
    throw TrackTooLongException(length.toString)
}
