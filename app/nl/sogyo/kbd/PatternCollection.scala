package nl.sogyo.kbd

trait PatternCollection {
  def numberOfTestPatterns = 3

  def get(id: Int): Option[Pattern]

  def post(p: Pattern): Int
}
