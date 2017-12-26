package data.dao.impl

import javax.inject.{Inject, Singleton}

import data.dao.{TransactionsDao, UsersDao}
import data.database.slick.h2.DbSchemaDefinition
import data.model.User
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.JdbcProfile
import slick.jdbc.H2Profile.api._

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContext, Future}

@Singleton
class UsersDaoImpl @Inject()(dbSchemaDefinition: DbSchemaDefinition,
                             transactionsDao: TransactionsDao,
                             @NamedDatabase("slick") dbConfigProvider: DatabaseConfigProvider)
                            (implicit ec: ExecutionContext) extends UsersDao {

  private val db = dbConfigProvider.get[JdbcProfile].db

  override def getUserByLogin(userLogin: String): Future[User] = db.run(dbSchemaDefinition.users.filter(_.login === userLogin).result.head).map {
    case (id, login) => User(id, login, Await.result(transactionsDao.getTransactionsForUser(id, login), 5 seconds))
  }

  override def getAllUsers: Future[Seq[User]] = db.run(dbSchemaDefinition.users.result).map(_.map {
    case (id, login) => User(id, login, Await.result(transactionsDao.getTransactionsForUser(id, login), 5 seconds))
  })

  override def addNewUser(login: String): Future[Int] = {
    db.run((dbSchemaDefinition.users.map {
      user => user.login
    } += login).transactionally)
  }

  override def deleteUser(login: String): Future[Int] = db.run(dbSchemaDefinition.users.filter(_.login === login).delete.transactionally)
}
