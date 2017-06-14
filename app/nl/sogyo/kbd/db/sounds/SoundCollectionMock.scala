package nl.sogyo.kbd.db.sounds

import nl.sogyo.kbd.domain.Sound

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SoundCollectionMock extends SoundCollection {
  val map: Map[Int, Sound] = Map(
    1 -> Sound("Windows Ding", "Windows Ding.wav"),
    2 -> Sound("Windows Error", "Windows Error.wav"),
    3 -> Sound("Windows Default", "Windows Default.wav")
  )

  def get(id: Int): Future[Option[Sound]] = Future(map.get(id))

  def get(name: String): Future[Option[Sound]] = Future {
    map.values.find(_.name == name)
  }

  def getAllNames: Future[Seq[String]] = Future {
    (for {
      (_, Sound(name, _)) <- map
    } yield name).toVector
  }
}
