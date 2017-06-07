package nl.sogyo.kbd

import javax.inject._

import play.api.db.slick.DatabaseConfigProvider
import slick.driver.JdbcProfile

import scala.concurrent.Future

@Singleton
class PatternDB @Inject()(dbConfigProvider: DatabaseConfigProvider, val sc: SoundCollection) extends PatternCollection {

  private val dbConfig = dbConfigProvider.get[JdbcProfile]

  addDefaults()

  def get(id: Int): Future[Option[Pattern]] = ???

  def post(p: Pattern): Future[Int] = ???

  def get(name: String): Future[Option[Pattern]] = ???
}
