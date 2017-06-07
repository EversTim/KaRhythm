package nl.sogyo.kbd.db

import javax.inject._

import nl.sogyo.kbd.domain.Pattern
import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

@Singleton
class PatternDB @Inject()(dbConfigProvider: DatabaseConfigProvider, protected val sc: SoundCollection) extends PatternCollection {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  def get(id: Int): Future[Option[Pattern]] = ???

  def post(p: Pattern): Future[Int] = ???

  def get(name: String): Future[Option[Pattern]] = ???
}
