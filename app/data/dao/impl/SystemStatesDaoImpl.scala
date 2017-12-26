package data.dao.impl

import javax.inject.{Inject, Singleton}

import data.dao.SystemStatesDao
import data.database.slick.h2.DbSchemaDefinition
import data.model.SystemState
import play.api.db.slick.DatabaseConfigProvider
import play.db.NamedDatabase
import slick.jdbc.H2Profile.api._
import slick.jdbc.JdbcProfile

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class SystemStatesDaoImpl @Inject()(dbSchemaDefinition: DbSchemaDefinition,
                                    @NamedDatabase("slick") dbConfigProvider: DatabaseConfigProvider)
                                   (implicit ec: ExecutionContext) extends SystemStatesDao {

  private val db = dbConfigProvider.get[JdbcProfile].db

  override def getAllSystemStates: Future[Seq[SystemState]] = db.run(dbSchemaDefinition.systemStates.result).map(_.map {
    case (id, amount, timestamp) => SystemState(id, amount, timestamp)
  })

  override def getCurrentSystemState: Future[SystemState] = db.run(dbSchemaDefinition.systemStates.sortBy(_.id.desc).result.head).map {
    case (id, amount, timestamp) => SystemState(id, amount, timestamp)
  }

  override def addNewSystemState(amount: Int): Future[Int] = {
    db.run((dbSchemaDefinition.systemStates.map {
      ss => (ss.amount, ss.timestamp)
    } += (amount, System.currentTimeMillis())).transactionally)
  }
}
