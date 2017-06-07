package nl.sogyo.kbd.db

import slick.lifted.TableQuery

package object tables {
  val patterns: TableQuery[Patterns] = TableQuery[Patterns]
  val tracks: TableQuery[Tracks] = TableQuery[Tracks]
  val sounds: TableQuery[Sounds] = TableQuery[Sounds]
}
