package nl.sogyo.karhythm.db

import org.scalatest._
import PatternDB._

import scala.collection._

class PatternDBObjectSpec extends FlatSpec with Matchers {
  "boolSeqToString and stringToBoolSeq" should "be inverses" in {
    val bs = mutable.Set(Seq(true, false, true, false))
    bs += Seq(true, false, false, true)
    bs += Seq(false, false, false, false)
    bs += Seq(true, true, true, true)
    bs += Seq(true, false, true)
    bs.foreach(sq => stringToBoolSeq(boolSeqToString(sq)) should be(sq))
  }
}
