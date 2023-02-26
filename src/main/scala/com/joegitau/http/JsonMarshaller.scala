package com.joegitau.http

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.joegitau.model.Player
import spray.json.{DefaultJsonProtocol, JsNumber, JsValue, JsonFormat, RootJsonFormat, deserializationError}

import java.sql.Timestamp

trait JsonMarshaller extends DefaultJsonProtocol with SprayJsonSupport {
  // implicit timestamp format
  implicit object timestampFormat extends JsonFormat[Timestamp] {
    override def write(tms: Timestamp): JsValue = JsNumber(tms.getTime)

    override def read(json: JsValue): Timestamp = json match {
      case JsNumber(jsNum) => new Timestamp(jsNum.bigDecimal.longValue())
      case _               => deserializationError("Was expecting some timestamp!")
    }
  }

  implicit val playerJsSprayFormat: RootJsonFormat[Player] = jsonFormat7(Player)
}
