package nl.sogyo.kbd.domain

case class TrackTooLongException(msg: String = "") extends Exception(msg)
