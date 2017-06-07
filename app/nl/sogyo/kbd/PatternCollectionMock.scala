package nl.sogyo.kbd

import javax.inject._

import scala.collection._
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

@Singleton
class PatternCollectionMock @Inject()(val sc: SoundCollection) extends PatternCollection {
  private val patterns: mutable.Map[Int, Pattern] = mutable.Map()
  private var curID = -1

  addDefaults()

  def get(id: Int): Future[Option[Pattern]] = Future {
    patterns.get(id)
  }

  def post(p: Pattern): Future[Int] = Future {
    patterns.find { case (_, pa) => pa == p } match {
      case Some((i, _)) => i
      case None =>
        curID = curID + 1
        patterns += curID -> p
        curID
    }
  }

  def get(name: String): Future[Option[Pattern]] = Future {
    patterns.values.find(p => p.name == name)
  }
}
