import com.google.inject._
import nl.sogyo.karhythm.db.patterns._
import nl.sogyo.karhythm.db.sounds._
import nl.sogyo.karhythm.db.users._

class Module extends AbstractModule {
  def configure(): Unit = {
    bind(classOf[PatternCollection]).to(classOf[PatternDB])
    bind(classOf[SoundCollection]).to(classOf[SoundDB])
    bind(classOf[UserCollection]).to(classOf[UserDB])
  }
}
