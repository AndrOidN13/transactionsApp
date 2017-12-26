package controllers

import javax.inject._

import data.dao.TransactionsDao
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class HomeController @Inject()(cc: ControllerComponents, transactionsDao: TransactionsDao)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  def index = Action.async {
    transactionsDao.getAllTransactions map {
      transactions => Ok(views.html.index("", transactions))
    }
  }

}
