package controllers

import javax.inject.{Inject, Singleton}

import data.dao.SystemStatesDao
import data.model.SystemState
import play.api.libs.json.Json
import play.api.mvc.{AbstractController, ControllerComponents}

import scala.concurrent.ExecutionContext

@Singleton
class SystemStatesController @Inject()(cc: ControllerComponents, systemStatesDao: SystemStatesDao)
                                      (implicit ec: ExecutionContext) extends AbstractController(cc){

  def getSystemStates = Action.async {
    systemStatesDao.getAllSystemStates.map(_.map(Json.toJson[SystemState])) map {
      systemStates => Ok(Json.toJson(systemStates))
    }
  }

  def getCurrentSystemState = Action.async {
    systemStatesDao.getCurrentSystemState map {
      currentSystemState => Ok(Json.toJson(currentSystemState))
    }
  }
}
