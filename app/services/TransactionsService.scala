package services

import java.util.concurrent.atomic.AtomicInteger
import javax.inject.{Inject, Singleton}

import data.dao.{SystemStatesDao, TransactionsDao, UsersDao}
import play.api.inject.ApplicationLifecycle

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class TransactionsService @Inject()(usersDao: UsersDao, transactionsDao: TransactionsDao,
                                    systemStatesDao: SystemStatesDao, appLifecycle: ApplicationLifecycle)
                                   (implicit ec: ExecutionContext) {

  private val currentAmount: AtomicInteger = new AtomicInteger(Await.result(systemStatesDao.getCurrentSystemState.map(_.amount), 3 seconds))

  def addTransaction(userLogin: String, isDebit: Boolean, amount: Int): Future[Unit] = Future {
    val userId = Await.result(usersDao.getUserByLogin(userLogin).map(_.id), 5 seconds)
    validateAndAddTransaction(userId, isDebit, amount)
  }

  private def validateAndAddTransaction(userId: Long, isDebit: Boolean, amount: Int): Unit = {
    currentAmount.synchronized {
      if(isDebit || amount <= currentAmount.get()){
        transactionsDao.addTransaction(userId, isDebit, amount)
        currentAmount.getAndAdd(if(isDebit) amount else -amount)
        systemStatesDao.addNewSystemState(currentAmount.get())
      }
      else throw new IllegalStateException(currentAmount.get().toString)
    }
  }
}
