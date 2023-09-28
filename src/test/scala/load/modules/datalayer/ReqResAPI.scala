package load.modules.datalayer

import io.gatling.core.Predef.{jsonPath, _}
import io.gatling.http.Predef._
import load.common.GlobalFunctions
import load.testData.MonitoringConstant
import load.utils.{Environment, Headers}

import scala.util.matching.Regex

class ReqResAPI extends Simulation {
  val monitoringConstant = new MonitoringConstant()
  val environment = new Environment()
  val headers = new Headers()
  val globalFunctions = new GlobalFunctions()

  object Action {

    def getListUsers() = this.synchronized {
      pace(globalFunctions.Global.getRandomNumberInBetween(monitoringConstant.paceMin, monitoringConstant.paceMax))
        .tryMax(monitoringConstant.tryMax) {
          exec(http("Get List Users")
            .get(headers.testServerUrl+"/api/users?page=2")
//            .headers(headers.commonGetHeadersWithoutToken("CARRIER_X_API_KEY"))
            .check(jsonPath("$.page").ofType[Int].is(2))
            .check(jsonPath("$.support.url").ofType[String].is(s"${headers.testServerUrl}/#support-heading"))
            .check(jsonPath("$.data[0].id").ofType[Int].is(7))
            .check(status.is(monitoringConstant.successStatus)))
        }
    }

    def getSingleUsers() = this.synchronized {
      pace(globalFunctions.Global.getRandomNumberInBetween(monitoringConstant.paceMin, monitoringConstant.paceMax))
        .tryMax(monitoringConstant.tryMax) {
          exec(http("Get Single Users")
            .get(headers.testServerUrl+"/api/users/2")
//            .headers(headers.commonGetHeadersWithoutToken("CARRIER_X_API_KEY"))
            .check(jsonPath("$.support.url").ofType[String].is(s"${headers.testServerUrl}/#support-heading"))
            .check(jsonPath("$.data.id").ofType[Int].is(2))
            .check(status.is(monitoringConstant.successStatus)))
        }
    }

    def createUsers() = this.synchronized {
      pace(globalFunctions.Global.getRandomNumberInBetween(monitoringConstant.paceMin, monitoringConstant.paceMax))
        .tryMax(monitoringConstant.tryMax) {
          exec(http("Create Users")
            .post(headers.testServerUrl+"/api/users")
            .body(StringBody(
              """
                |{
                |    "name": "${name}",
                |    "job": "${job}"
                |}
                |""".stripMargin))
//            .headers(headers.commonGetHeadersWithoutToken("CARRIER_X_API_KEY"))
//            .check(jsonPath("$.name").ofType[String].is("${name}"))
//            .check(jsonPath("$.job").ofType[String].is("${job}"))
            .check(jsonPath("$.id").ofType[String].find.saveAs("id"))
            .check(status.is(monitoringConstant.successStatusCreate)))
        }
    }

    def updateUsers() = this.synchronized {
      pace(globalFunctions.Global.getRandomNumberInBetween(monitoringConstant.paceMin, monitoringConstant.paceMax))
        .tryMax(monitoringConstant.tryMax) {
          exec(http("Update Users")
            .put(headers.testServerUrl+"/api/users/${id}")
            .body(StringBody(
              """
                |{
                |    "name": "${name}",
                |    "job": "${job}"
                |}
                |""".stripMargin))
//            .headers(headers.commonGetHeadersWithoutToken("CARRIER_X_API_KEY"))
//            .check(jsonPath("$.name").ofType[String].is("${name}"))
//            .check(jsonPath("$.job").ofType[String].is("${job}"))
            .check(status.is(monitoringConstant.successStatus)))
        }
    }

  }

}
