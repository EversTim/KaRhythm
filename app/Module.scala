import com.google.inject._

import nl.sogyo.kbd._

class Module extends AbstractModule {
  def configure(): Unit = {
    bind(classOf[PatternCollection]).to(classOf[PatternCollectionMock])
    bind(classOf[SoundCollection]).to(classOf[SoundCollectionMock])
  }
}
