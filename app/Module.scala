import com.google.inject._

import nl.sogyo.kbd.{PatternCollection, PatternCollectionInMemory}

class Module extends AbstractModule {
  def configure(): Unit = {
    bind(classOf[PatternCollection]).to(classOf[PatternCollectionInMemory])
  }
}
