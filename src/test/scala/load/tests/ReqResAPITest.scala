package load.tests

import io.gatling.core.Predef.{feed, _}
import io.gatling.core.structure.ScenarioBuilder
import load.suits.SimulationBaseClass

class ReqResAPITest extends SimulationBaseClass {
  def builder_reqress(): ScenarioBuilder = {
    val scenario_reqress = scenario("@ " + globalFunctions.Global.getSystemCurrentTimeInMillis() + " Run >> " + environment.runType + " -> ReqRes API Test")

//      .repeat(1) {
        .feed(monitoringConstant.userData.random)
        .group("ReqRess Test") {
          exec(
            reqResAPI.Action.getListUsers(),
            reqResAPI.Action.getSingleUsers(),
            reqResAPI.Action.createUsers()
          )
            .feed(monitoringConstant.userData.circular)
            .exec(
              reqResAPI.Action.updateUsers()
            )
        }
//      }

    scenario_reqress
  }

  setUp( // 11 - Where one sets up the scenarios that will be launched in this Simulation
    builder_reqress().inject(onSetUp.customCheck_DryRun)) // 12 - Declaring to inject into scenario named scn one single/multiple users
    //builder_WishlistCreate().inject(onSetUp.customCheck_2) // 12 - Declaring to inject into scenario named scn one single/multiple users
    .protocols(createHttpProtocolBuilder()) // 13 - Attaching the HTTP configuration declared above
    .assertions(
      forAll.failedRequests.percent.is(0),
      global.responseTime.max.lt(5000),
      global.responseTime.mean.lt(2000),
      global.responseTime.percentile3.lt(2000),
      global.successfulRequests.percent.gt(90)
    )
    .maxDuration(60)

}
