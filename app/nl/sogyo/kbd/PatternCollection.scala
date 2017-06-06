package nl.sogyo.kbd

trait PatternCollection {
  def numberOfTestPatterns = 3

  def post(p: Pattern): Int
  def get(id: Int): Option[Pattern]
}
