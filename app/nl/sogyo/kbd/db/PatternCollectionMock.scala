package nl.sogyo.kbd.db

import javax.inject._

import nl.sogyo.kbd.domain.{Pattern, Track}

import scala.collection._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

@Singleton
class PatternCollectionMock @Inject()(protected val sc: SoundCollection) extends PatternCollection {
  private val patterns: mutable.Map[Int, Pattern] = mutable.Map()
  private var curID = -1

  // Default patterns
  IndexedSeq(
    Pattern("Default",
      Track("D1", Await.result(sc.get(0), 100.millis).get, true, false, true, false),
      Track("D2", Await.result(sc.get(1), 100.millis).get, false, true, false, true)
    ),
    Pattern("Test 1",
      Track("T11", Await.result(sc.get(0), 100.millis).get, true, false, true, false),
      Track("T12", Await.result(sc.get(1), 100.millis).get, false, true, false, true),
      Track("T13", Await.result(sc.get(2), 100.millis).get, true, true, true, true)
    ),
    Pattern("Test 2",
      Track("T21", Await.result(sc.get(0), 100.millis).get, true, false, true, false, true),
      Track("T22", Await.result(sc.get(1), 100.millis).get, false, true, false, true, false),
      Track("T23", Await.result(sc.get(2), 100.millis).get, true, true, true, true, false))
  ).foreach(post)

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
