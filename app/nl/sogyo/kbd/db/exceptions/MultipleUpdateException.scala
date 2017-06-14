package nl.sogyo.kbd.db.exceptions

case class MultipleUpdateException(msg: String = "") extends Exception(msg)
