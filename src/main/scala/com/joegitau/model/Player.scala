package com.joegitau.model

import play.api.libs.json.{Json, OFormat}

import java.sql.Timestamp

case class Player(
  id:          Option[Int],
  firstName:   String,
  lastName:    String,
  nationality: String,
  team:        String,
  created:     Timestamp,
  modified:    Option[Timestamp]
) {
  implicit val playerFormat: OFormat[Player] = Json.format
}

case class PatchPlayer(
  firstName:   Option[String],
  lastName:    Option[String],
  nationality: Option[String],
  team:        Option[String],
) {
  implicit val patchPlayerFormat: OFormat[PatchPlayer] = Json.format
}
