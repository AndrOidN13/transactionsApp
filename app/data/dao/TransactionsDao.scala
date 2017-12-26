package data.dao

import com.google.inject.ImplementedBy
import data.dao.impl.TransactionsDaoImpl
import data.model.Transaction

import scala.concurrent.Future

@ImplementedBy(classOf[TransactionsDaoImpl])
trait TransactionsDao {
  def getTransactionsForUser(userId: Long, userLogin: String): Future[Seq[Transaction]]

  def getAllTransactions: Future[Seq[Transaction]]

  def addTransaction(userId: Long, isDebit: Boolean, amount: Int): Future[Int]
}
