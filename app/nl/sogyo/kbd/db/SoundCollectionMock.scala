package nl.sogyo.kbd.db

import javax.inject._

import nl.sogyo.kbd.domain.Sound

import scala.collection._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class SoundCollectionMock @Inject() extends SoundCollection {

  private val map: mutable.Map[Int, Sound] = mutable.Map(
    0 -> Sound("Windows Ding", "Windows Ding.wav"),
    1 -> Sound("Windows Error", "Windows Error.wav"),
    2 -> Sound("Windows Default", "Windows Default.wav")
  )

  def get(id: Int): Future[Option[Sound]] = Future {
    map.get(id)
  }

  def get(name: String): Future[Option[Sound]] = Future {
    map.values.find(_.name == name)
  }
}
