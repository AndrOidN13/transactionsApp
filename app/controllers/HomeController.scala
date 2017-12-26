package controllers

import javax.inject._

import data.dao.TransactionsDao
import play.api.mvc._
import scala.concurrent.duration._

import scala.concurrent.{Await, ExecutionContext}

@Singleton
class HomeController @Inject()(cc: ControllerComponents, transactionsDao: TransactionsDao)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action {
    val transactions = Await.result(transactionsDao.getAllTransactions, 5 seconds)
    Ok(views.html.index("", transactions))
  }

}
