package nl.sogyo.karhythm.forms

case class PatternForm(name: String, data: Seq[Seq[Boolean]], trackNames:Seq[String], trackSounds: Seq[String], length: Int, tracks: Int)