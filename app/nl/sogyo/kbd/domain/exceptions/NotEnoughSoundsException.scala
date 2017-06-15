package nl.sogyo.kbd.domain.exceptions

case class NotEnoughSoundsException(msg: String = "") extends Exception(msg)
