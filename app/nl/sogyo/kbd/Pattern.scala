package nl.sogyo.kbd

class Pattern private (val data: Seq[Seq[Boolean]]) {
  if(data.isEmpty)
    throw new NoTracksDefinedException(data.toString)

  if(data.exists(_.isEmpty))
    throw new ZeroLengthTrackException(data.toString)

  val length = data.foldLeft(Int.MinValue)((acc, s) => acc.max(s.length))

  if(data.exists(_.size != length))
    throw new UnevenLengthException(data.toString)

  val tracks = data.size
}

object Pattern {
  def apply(data: Seq[Seq[Boolean]]): Pattern = new Pattern(data)

  def unapply(p: Pattern): Option[Seq[Seq[Boolean]]] = Some(p.data)
}
