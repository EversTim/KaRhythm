package nl.sogyo.karhythm.db.exceptions

case class MultipleUpdateException(msg: String = "") extends Exception(msg)
