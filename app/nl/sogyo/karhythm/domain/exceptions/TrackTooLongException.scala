package nl.sogyo.karhythm.domain.exceptions

case class TrackTooLongException(msg: String = "") extends Exception(msg)
