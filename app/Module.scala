import com.google.inject._
import nl.sogyo.kbd.db.patterns._
import nl.sogyo.kbd.db.sounds._

class Module extends AbstractModule {
  def configure(): Unit = {
    bind(classOf[PatternCollection]).to(classOf[PatternDB])
    bind(classOf[SoundCollection]).to(classOf[SoundDB])
  }
}
