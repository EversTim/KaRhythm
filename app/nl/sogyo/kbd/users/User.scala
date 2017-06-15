package nl.sogyo.kbd.users

import javax.inject._

import nl.sogyo.kbd.db.patterns.PatternCollection
import nl.sogyo.kbd.domain.Pattern

import scala.collection.mutable

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class User @Inject()(val username: String) {
  private val patterns = mutable.Set.empty[Pattern]

  def addPattern(p: Pattern): Unit =
    patterns += p

  def hasPattern(p: Pattern): Boolean = patterns.contains(p)
}

object User {
  def apply(username: String): User = new User(username)
  def public = User("public")
}
