import com.google.inject._
import nl.sogyo.kbd.db._
import nl.sogyo.kbd.db.patterns.{PatternCollection, PatternDB}
import nl.sogyo.kbd.db.sounds.{SoundCollection, SoundDB}

class Module extends AbstractModule {
  def configure(): Unit = {
    bind(classOf[PatternCollection]).to(classOf[PatternDB])
    bind(classOf[SoundCollection]).to(classOf[SoundDB])
  }
}
