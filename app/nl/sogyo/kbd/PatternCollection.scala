package nl.sogyo.kbd

import scala.collection.IndexedSeq
import scala.concurrent._
import scala.concurrent.duration._

trait PatternCollection {
  def numberOfTestPatterns = 3

  def sc: SoundCollection

  // Default patterns
  final def addDefaults(): Unit = IndexedSeq(
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

  def get(id: Int): Future[Option[Pattern]]

  def get(name: String): Future[Option[Pattern]]

  def post(p: Pattern): Future[Int]
}
