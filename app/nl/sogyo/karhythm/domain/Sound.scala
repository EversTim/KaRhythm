package nl.sogyo.karhythm.domain

case class Sound(name: String, location: String)

object Sound {
  val nullSound = Sound("Empty", "Empty.wav")
}