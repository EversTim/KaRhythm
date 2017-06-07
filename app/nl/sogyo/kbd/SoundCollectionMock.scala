package nl.sogyo.kbd

import javax.inject._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

import scala.collection._

@Singleton
class SoundCollectionMock @Inject() extends SoundCollection {

  private val map: mutable.Map[Int, Sound] = mutable.Map()
  private var curID = -1

  addDefaults()

  def get(id: Int): Future[Option[Sound]] = Future{
    map.get(id)
  }

  def get(name: String): Future[Option[Sound]] = Future {
    map.values.find(_.name == name)
  }

  def post(s: Sound): Future[Int] = Future {
    curID = curID + 1
    map += curID -> s
    curID
  }
}
