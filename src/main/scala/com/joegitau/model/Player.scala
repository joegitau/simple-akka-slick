package com.joegitau.model

import java.time.Instant

case class Player(
  id:          Option[Int],
  firstName:   String,
  lastName:    String,
  nationality: String,
  team:        String,
  created:     Option[Instant],
  modified:    Option[Instant]
)

case class PatchPlayer(
  firstName:   Option[String],
  lastName:    Option[String],
  nationality: Option[String],
  team:        Option[String],
)
