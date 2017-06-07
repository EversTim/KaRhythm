import com.google.inject._
import nl.sogyo.kbd.db._

class Module extends AbstractModule {
  def configure(): Unit = {
    bind(classOf[PatternCollection]).to(classOf[PatternDB])
    bind(classOf[SoundCollection]).to(classOf[SoundDB])
  }
}
