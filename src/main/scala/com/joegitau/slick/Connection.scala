package com.joegitau.slick

import com.joegitau.slick.CustomPostgresProfile.api._

object Connection {
  val Db = Database.forConfig("postgres")
}
