package nl.sogyo.kbd.forms
import nl.sogyo.kbd.Pattern

case class PatternForm(pattern: Seq[Seq[Boolean]], length: Int, tracks: Int)