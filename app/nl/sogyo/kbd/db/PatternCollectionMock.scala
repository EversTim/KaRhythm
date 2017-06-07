package nl.sogyo.kbd.db

import javax.inject._

import nl.sogyo.kbd.domain.{Pattern, Sound, Track}

import scala.collection._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

@Singleton
class PatternCollectionMock @Inject() extends PatternCollection {
  private val patterns: mutable.Map[Int, Pattern] = mutable.Map()
  private var curID = -1

  // Default patterns
  IndexedSeq(
    Pattern("default",
      Track("d1", Sound("Windows Ding", "Windows Ding.wav"), true, false, true, false),
      Track("d2", Sound("Windows Error", "Windows Error.wav"), false, true, false, true)
    ),
    Pattern("test1",
      Track("t11", Sound("Windows Ding", "Windows Ding.wav"), true, false, true, false),
      Track("t12", Sound("Windows Error", "Windows Error.wav"), false, true, false, true),
      Track("t13", Sound("Windows Default", "Windows Default.wav"), true, true, true, true)
    ),
    Pattern("test2",
      Track("T21", Sound("Windows Ding", "Windows Ding.wav"), true, false, true, false, true),
      Track("T22", Sound("Windows Error", "Windows Error.wav"), false, true, false, true, false),
      Track("T23", Sound("Windows Default", "Windows Default.wav"), true, true, true, true, false))
  ).foreach(post)

  def get(id: Int): Future[Option[Pattern]] = Future {
    patterns.get(id - 1)
  }

  def post(p: Pattern): Future[Int] = Future {
    patterns.find { case (_, pa) => pa == p } match {
      case Some((i, _)) => i
      case None =>
        curID = curID + 1
        patterns += curID -> p
        curID + 1
    }
  }

  def get(name: String): Future[Option[Pattern]] = Future {
    patterns.values.find(p => p.name == name)
  }
}
