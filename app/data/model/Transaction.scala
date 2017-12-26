package data.model

import play.api.libs.json.{Json, OFormat}

object Transaction {
  implicit val transactionFormat: OFormat[Transaction] = Json.format[Transaction]
}

case class Transaction(id: Long, userId: Long, isDebit: Boolean, amount: Int, timestamp: Long)

object TransactionPostResource {
  implicit val transactionPostResourceFormat: OFormat[TransactionPostResource] = Json.format[TransactionPostResource]
}

case class TransactionPostResource(userLogin: String, isDebit: Boolean, amount: Int)
