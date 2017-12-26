package data.dao.impl


import javax.inject.Inject

import data.dao.TransactionsDao
import data.database.slick.h2.DbSchemaDefinition
import data.model.Transaction
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import slick.jdbc.H2Profile.api._

import scala.concurrent.{ExecutionContext, Future}

class TransactionsDaoImpl @Inject()(dbSchemaDefinition: DbSchemaDefinition,
                                    @NamedDatabase("slick") dbConfigProvider: DatabaseConfigProvider)
                                   (implicit ec: ExecutionContext) extends TransactionsDao {

  private val db = dbConfigProvider.get[JdbcProfile].db

  override def getTransactionsForUser(userId: Long, userLogin: String): Future[Seq[Transaction]] = {
    db.run(dbSchemaDefinition.transactions.filter(_.userId === userId).result).map(_.map {
      case (id, usrId, isDebit, amount, timestamp) => Transaction(id, usrId, isDebit, amount, timestamp)
    })
  }

  override def getAllTransactions: Future[Seq[Transaction]] = {
    db.run(dbSchemaDefinition.transactions.result).map(_.map {
      case (id, usrId, isDebit, amount, timestamp) => Transaction(id, usrId, isDebit, amount, timestamp)
    })
  }

  override def addTransaction(userId: Long, isDebit: Boolean, amount: Int): Future[Int] = {
    db.run((dbSchemaDefinition.transactions.map {
      trn => (trn.userId, trn.isDebit, trn.amount, trn.timestamp)
    } += (userId, isDebit, amount, System.currentTimeMillis())).transactionally)
  }
}
