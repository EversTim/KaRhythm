package nl.sogyo.kbd.domain

case class Sound(name: String, location: String)

object Sound {
  val nullSound = Sound("", "")
}