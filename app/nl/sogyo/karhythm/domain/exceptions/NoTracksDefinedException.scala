package nl.sogyo.karhythm.domain.exceptions

case class NoTracksDefinedException(msg: String = "") extends Exception(msg)
