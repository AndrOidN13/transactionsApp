package data.model

import play.api.libs.json.{Json, OFormat}

object User {
  implicit val userFormat: OFormat[User] = Json.format[User]
}

case class User(id: Long, login: String, transactions: Seq[Transaction])

object UserPostResource {
  implicit val userFormat: OFormat[UserPostResource] = Json.format[UserPostResource]
}

case class UserPostResource(login: String)

