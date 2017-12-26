package data.database.slick.h2

import javax.inject.Singleton

import slick.jdbc.H2Profile.api._
import slick.sql.SqlProfile.ColumnOption.NotNull

@Singleton
class DbSchemaDefinition {

  class Users(tag: Tag) extends Table[(Long, String)](tag, "USERS") {
    def id = column[Long]("USER_ID", O.PrimaryKey, O.AutoInc)
    def login = column[String]("LOGIN", NotNull, O.Unique)
    def * = (id, login)
  }
  val users = TableQuery[Users]

  class Transactions(tag: Tag) extends Table[(Long, Long, Boolean, Int, Long)](tag, "TRANSACTIONS") {
    def id = column[Long]("TRANSACTION_ID", O.PrimaryKey, O.AutoInc)
    def isDebit = column[Boolean]("IS_DEBIT", NotNull)
    def amount = column[Int]("AMOUNT", NotNull)
    def timestamp = column[Long]("TIMESTAMP", NotNull)
    def userId = column[Long]("USER_ID")
    def * = (id, userId, isDebit, amount, timestamp)
    def user = foreignKey("USER_FK", userId, users)(_.id, onDelete=ForeignKeyAction.Cascade)
  }
  val transactions = TableQuery[Transactions]

  class SystemStates(tag: Tag) extends Table[(Long, Int, Long)](tag, "SYSTEM_STATES") {
    def id = column[Long]("SYSTEM_STATE_ID", O.PrimaryKey, O.AutoInc)
    def amount = column[Int]("AMOUNT", NotNull)
    def timestamp = column[Long]("TIMESTAMP", NotNull)
    def * = (id, amount, timestamp)
  }
  val systemStates = TableQuery[SystemStates]
}
