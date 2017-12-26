package data.model

import play.api.libs.json.{Json, OFormat}


object SystemState {
  implicit val systemStateFormat: OFormat[SystemState] = Json.format[SystemState]
}

case class SystemState(id: Long, amount: Int, timestamp: Long)
