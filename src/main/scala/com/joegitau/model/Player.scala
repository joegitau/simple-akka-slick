package com.joegitau.model

import java.sql.Timestamp


case class Player(
  id:          Option[Int],
  firstName:   String,
  lastName:    String,
  nationality: String,
  team:        String,
  created:     Timestamp,
  modified:    Option[Timestamp]
)
