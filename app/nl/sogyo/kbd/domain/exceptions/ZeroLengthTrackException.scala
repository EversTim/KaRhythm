package nl.sogyo.kbd.domain.exceptions

case class ZeroLengthTrackException(msg: String = "") extends Exception(msg) {

}
