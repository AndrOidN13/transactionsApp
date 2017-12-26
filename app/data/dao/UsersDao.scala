package data.dao

import com.google.inject.ImplementedBy
import data.dao.impl.UsersDaoImpl
import data.model.User

import scala.concurrent.Future

@ImplementedBy(classOf[UsersDaoImpl])
trait UsersDao {
  def getUserByLogin(login: String): Future[User]
  def getAllUsers: Future[Seq[User]]
  def addNewUser(login: String): Future[Int]
  def deleteUser(login: String): Future[Int]
}
