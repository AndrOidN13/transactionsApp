package controllers

import javax.inject._

import data.dao.UsersDao
import data.model.{User, UserPostResource}
import play.api.libs.json._
import play.api.mvc._

import scala.concurrent.ExecutionContext

@Singleton
class UsersController @Inject()(cc: ControllerComponents, usersDao: UsersDao)
                               (implicit ec: ExecutionContext) extends AbstractController(cc) {

  def getUsers = Action.async {
    usersDao.getAllUsers.map(_.map(Json.toJson[User])) map {
      users => Ok(Json.toJson(users))
    }
  }

  def getUserByLogin(login: String) = Action.async {
    usersDao.getUserByLogin(login) map {
      user => Ok(Json.toJson(user))
    } recover {
      case ns: NoSuchElementException => NotFound("User with given login was not found")
    }
  }

  def addUser = Action.async {
    request =>
      val json = request.body.asJson.get
      val usr = json.as[UserPostResource]
      usersDao.addNewUser(usr.login) map {
        _ => Created
      } recover {
        case ex: Exception => BadRequest(
          if(ex.getMessage.contains("Unique index or primary key violation")) "User with given login already exists"
          else "Sorry, something went wrong"
        )
      }
  }

  def deleteUser(login: String) = Action.async {
    usersDao.deleteUser(login) map {
      _ => Ok
    }
  }

}
