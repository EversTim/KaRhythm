package nl.sogyo.karhythm.domain.exceptions

case class ZeroLengthTrackException(msg: String = "") extends Exception(msg) {

}
