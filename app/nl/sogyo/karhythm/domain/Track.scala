package nl.sogyo.karhythm.domain

import nl.sogyo.karhythm.domain.exceptions.{TrackTooLongException, ZeroLengthTrackException}

case class Track(name: String, sound: Sound, data: Boolean*) {

  if (data.isEmpty)
    throw ZeroLengthTrackException(data.toString)

  val length: Int = data.length

  if (length > 64)
    throw TrackTooLongException(length.toString)
}

object Track {
  def empty(n: Int): Track = Track("Empty", Sound.nullSound, Seq.fill(n)(false): _*)
}