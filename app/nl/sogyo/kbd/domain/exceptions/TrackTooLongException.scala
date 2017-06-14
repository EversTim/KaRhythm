package nl.sogyo.kbd.domain.exceptions

case class TrackTooLongException(msg: String = "") extends Exception(msg)
