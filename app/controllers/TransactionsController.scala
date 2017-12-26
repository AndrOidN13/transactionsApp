package controllers

import java.util.NoSuchElementException
import javax.inject._

import data.dao.TransactionsDao
import data.model.{Transaction, TransactionPostResource}
import play.api.libs.json._
import play.api.mvc._
import services.TransactionsService

import scala.concurrent.ExecutionContext

@Singleton
class TransactionsController @Inject()(cc: ControllerComponents, transactionsService: TransactionsService, transactionsDao: TransactionsDao)
                               (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getTransactions = Action.async {
    transactionsDao.getAllTransactions
      .map(_.map(Json.toJson[Transaction]))
      .map(trnSeq => Ok(Json.toJson(trnSeq)))
  }

  def addTransaction = Action.async {
    request =>
      val json = request.body.asJson.get
      val tpr = json.as[TransactionPostResource]
      transactionsService.addTransaction(tpr.userLogin, tpr.isDebit, tpr.amount) map {
        _ => Created
      } recover {
        case ns: NoSuchElementException => NotFound("There is no such user in the system, try another login")
        case is: IllegalStateException => BadRequest("You were trying to take too big credit. There are only " + is.getMessage + "$ in the system now.")
        case ex: Exception => InternalServerError(ex.getMessage)
      }
    }
}
