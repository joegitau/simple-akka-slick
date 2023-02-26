package com.joegitau.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.joegitau.model.{PatchPlayer, Player}
import spray.json.{DefaultJsonProtocol, JsNumber, JsValue, JsonFormat, RootJsonFormat, deserializationError}

import java.sql.Timestamp
import java.time.Instant

trait JsonMarshaller extends DefaultJsonProtocol with SprayJsonSupport {
  // implicit timestamp format
  implicit object timestampFormat extends JsonFormat[Timestamp] {
    override def write(tms: Timestamp): JsValue = JsNumber(tms.getTime)

    override def read(json: JsValue): Timestamp = json match {
      case JsNumber(jsNum) => new Timestamp(jsNum.bigDecimal.longValue())
      case _               => deserializationError("Was expecting some timestamp, bro!")
    }
  }

  // implicit instant format
  implicit val instantFormat: RootJsonFormat[Instant] = new RootJsonFormat[Instant] {
    def write(instant: Instant): JsValue = JsNumber(instant.toEpochMilli)

    def read(json: JsValue): Instant = json match {
      case JsNumber(epochMillis) => Instant.ofEpochMilli(epochMillis.toLongExact)
      case _                     => deserializationError("Was expecting some JsNumber, bro!")
    }
  }

  implicit val playerJsSprayFormat: RootJsonFormat[Player]           = jsonFormat7(Player)
  implicit val patchPlayerJsSprayFormat: RootJsonFormat[PatchPlayer] = jsonFormat4(PatchPlayer)
}
