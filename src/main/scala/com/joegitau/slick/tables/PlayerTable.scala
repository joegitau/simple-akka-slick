package com.joegitau.slick.tables

import com.joegitau.model.Player
import com.joegitau.slick.CustomPostgresProfile.api._

import java.time.Instant

class PlayerTable(tag: Tag) extends Table[Player](tag, "players") {
  def id          = column[Option[Int]]("id", O.PrimaryKey, O.AutoInc)
  def firstName   = column[String]("first_name")
  def lastName    = column[String]("last_name")
  def nationality = column[String]("nationality")
  def team        = column[String]("team")
  def created     = column[Option[Instant]]("created")
  def modified    = column[Option[Instant]]("modified")

  override def * = (id, firstName, lastName, nationality, team, created, modified) <> (Player.tupled, Player.unapply)
}

object PlayerTable {
  lazy val Players = TableQuery[PlayerTable]
}
