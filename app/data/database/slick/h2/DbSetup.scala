package data.database.slick.h2

import javax.inject.{Inject, Singleton}

import play.api.db.slick.DatabaseConfigProvider
import play.api.inject.ApplicationLifecycle
import play.db.NamedDatabase
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcProfile

import scala.concurrent.ExecutionContext

@Singleton
class DbSetup @Inject()(dbSchemaDefinition: DbSchemaDefinition,
                        @NamedDatabase("slick") dbConfigProvider: DatabaseConfigProvider,
                        appLifecycle: ApplicationLifecycle)(implicit ec: ExecutionContext) {

  private val users = dbSchemaDefinition.users
  private val transactions = dbSchemaDefinition.transactions
  private val systemStates = dbSchemaDefinition.systemStates

  def initialize(): Unit = {
    val setup = DBIO.seq(
      (users.schema ++ transactions.schema ++ systemStates.schema).create,

      users += (1, "admin"),
      users += (2, "John Doe"),

      transactions += (1, 1, true, 1488, System.currentTimeMillis()),
      transactions += (2, 2, true, 1488, System.currentTimeMillis()),

      systemStates += (1, 1488, System.currentTimeMillis()),
      systemStates += (2, 1488 * 2, System.currentTimeMillis())
    )
    dbConfigProvider.get[JdbcProfile].db.run(setup)
  }
  initialize()
}
