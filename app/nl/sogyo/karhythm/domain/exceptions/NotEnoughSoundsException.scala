package nl.sogyo.karhythm.domain.exceptions

case class NotEnoughSoundsException(msg: String = "") extends Exception(msg)
