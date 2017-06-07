package nl.sogyo.kbd

import scala.concurrent.Future

trait SoundCollection {
  final def addDefaults(): Unit = IndexedSeq(
    Sound("Windows Ding", "Windows Ding.wav"),
    Sound("Windows Error", "Windows Error.wav"),
    Sound("Windows Default", "Windows Default.wav")
  ).foreach(post)

  def get(id: Int): Future[Option[Sound]]

  def get(name: String): Future[Option[Sound]]

  def post(s: Sound): Future[Int]
}
