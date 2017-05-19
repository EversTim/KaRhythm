package nl.sogyo.kbd

import scala.util.{Try, Success, Failure}

object SoundMap {
  private val tracks = Map(0 -> "Windows Ding.wav", 1 -> "Windows Error.wav", 2 -> "Windows Default.wav")

  def apply(numberOfTracks: Int): Try[Map[Int, String]] =
    if (tracks.size >= numberOfTracks) Success(tracks.filterKeys(_ < numberOfTracks))
    else Failure(new NotEnoughSoundsException)
}
