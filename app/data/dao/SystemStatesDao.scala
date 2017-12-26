package data.dao

import com.google.inject.ImplementedBy
import data.dao.impl.SystemStatesDaoImpl
import data.model.SystemState

import scala.concurrent.Future

@ImplementedBy(classOf[SystemStatesDaoImpl])
trait SystemStatesDao {
  def getAllSystemStates: Future[Seq[SystemState]]
  def getCurrentSystemState: Future[SystemState]
  def addNewSystemState(amount: Int): Future[Int]
}
