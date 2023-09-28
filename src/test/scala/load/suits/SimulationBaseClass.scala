package load.suits

import io.gatling.core.Predef._
import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.http.protocol.HttpProtocolBuilder
import load.common.GlobalFunctions
import load.modules.datalayer.ReqResAPI
import load.testData.MonitoringConstant
import load.utils.Environment

import java.io.{File, FileInputStream}
import java.util.Properties
import scala.concurrent.duration._
import scala.language.postfixOps

/**
 * Abstract Gatling simulation class that specifies properties common to multiple Gatling simulations.
 * Certain simulation parameters have been extracted to class variables which allows for subclasses
 * to modify certain behaviour of the basic simulation by setting new values of these variables.
 *
 * @author Prasad
 */

abstract class SimulationBaseClass extends Simulation {
  // Object Creation
  val monitoringConstant = new MonitoringConstant()
  val environment = new Environment()
  val globalFunctions = new GlobalFunctions()

  val reqResAPI = new ReqResAPI()

  val inputStream = new FileInputStream(monitoringConstant.userTestDataPath + "testData.properties")
  val file = new File(monitoringConstant.userTestDataPath+"testData.properties")
  val props = new Properties()

  try {
    // properties file load
    props.load(inputStream)
  }
  catch {
    case e: Exception => println("Exception caught: " + e)
  }

  /*
    * Performs initialization of the simulation before it is executed.
    * Initialization must be performed this way in order to allow for subclasses to
    * modify instance variables and for those modifications to affect the resulting
    * simulation configuration.
    */
  before {
    //create stuff and put the refUrls from it in a map
    println("On SetUp Simulation is about to start!")
    if(!file.exists()){
      exitHereIfFailed
    }
  }

  /**
   * Performs setup of the simulation.
   * Load testing, Stress testing, Endurance testing, Spike testing, Volume testing and Scalability testing
   */
  object onSetUp {
    // Scenario wise ingestion type and Load break downs
    // Scenario 1 >> Inject TakeAction On Message Level
    // Pause for a given duration
    val inject1 = nothingFor(props.getProperty("tAM_nothingFor").toInt)
    // Injects a given number of users with a linear ramp over a given duration
    val inject2 = rampUsers(props.getProperty("bR_atOnce").toInt)
    val inject9 = rampUsers(props.getProperty("rM_atOnce").toInt)
    // Injects users at a constant rate, defined in users per second
    val inject3 = constantUsersPerSec(props.getProperty("rM_atOnce").toInt)
    // Injects a given number of users at once
    val inject4 = atOnceUsers(props.getProperty("tAM_atOnce").toInt)
    // Injects users from starting rate to target rate, defined in users per second, during a given duration. Users will be injected at regular intervals
    val inject5 = rampUsersPerSec(props.getProperty("tAM_minRampUsersPerSec").toInt) to props.getProperty("tAM_maxRampUsersPerSec").toInt during (props.getProperty("tAM_rampUsersPerSecDuringInMin").toInt minutes)
    // Injects a given number of users following a smooth approximation of the heaviside step function stretched to a given duration
    val inject6 = heavisideUsers(props.getProperty("tAM_heavisideUsers").toInt) during (props.getProperty("tAM_heavisideUsersDuringInSeconds").toInt minutes)
    val inject7 = atOnceUsers(props.getProperty("bR_atOnce").toInt)
    val inject8 = atOnceUsers(props.getProperty("rM_atOnce").toInt)


    // Injects a given number of users with a linear ramp over a given duration
    val injectionSeq = Vector(1, 2, 4, 8).map(x => rampUsers(x * 100) during (props.getProperty("tAM_seqDuringInSec").toInt seconds))
    // constant Concurrent Users
    //val closedInject1 = constantConcurrentUsers(props.getProperty("tAM_constantConcurrentUsers").toInt).during(props.getProperty("tAM_constantConcurrentUsersDuringInSec").toInt seconds)
    val closedInject1 = constantConcurrentUsers(props.getProperty("tAM_constantConcurrentUsers").toInt).during(props.getProperty("tAM_constantConcurrentUsersDuringInSec").toInt hours)
    // ramp Concurrent Users
    val closedInject2 = rampConcurrentUsers(props.getProperty("tAM_minRampConcurrentUsers").toInt).to(props.getProperty("tAM_maxRampConcurrentUsers").toInt).during(props.getProperty("tAM_rampConcurrentUsersDuringInSec").toInt minutes)
    val closedInject3 = rampConcurrentUsers(props.getProperty("tAM_minRampConcurrentUsers").toInt).to(props.getProperty("tAM_maxRampConcurrentUsers1").toInt).during(props.getProperty("tAM_rampConcurrentUsersDuringInSec").toInt minutes)
    // Combination - Load Balancer
    val _tamInject_SequenceLoad = Seq(inject1, inject2, inject3)
    val _tamInject_BetweenSeqLoad = Seq(inject3, inject5)
    val _tamInject_ConcurrentLoad = Seq(closedInject1, closedInject2)
    val _tamInject_BaseLineLoad = Seq(inject4, inject6)
    // Scenario 1 >> End....!

    // Base Line test
    /*val _Builder1_ResolveAMessage = Seq(heavisideUsers(environment.user1Volume) during(environment.total1Duration seconds))
    val _Builder2_ResolveSingleAlert = Seq(nothingFor(10), heavisideUsers(environment.user2Volume) during(environment.total2Duration seconds))
    val _Builder3_RandomSampling = Seq(nothingFor(10), heavisideUsers(environment.user3Volume) during(environment.total3Duration seconds))
    val _Builder4_BulkAddManualAlert = Seq(nothingFor(90), heavisideUsers(environment.user4Volume) during(environment.total4Duration seconds))
    val _Builder5_BulkResolution = Seq(nothingFor(90), heavisideUsers(environment.user4Volume) during(environment.total4Duration seconds))
    val _Builder6_ExportMessages = Seq(nothingFor(90), heavisideUsers(environment.user5Volume) during(environment.total5Duration seconds))*/


    val customCheck_1 = Seq(heavisideUsers(50) during(60 seconds), nothingFor(90 seconds), atOnceUsers(5))
    //val customCheck_3 = Seq(rampUsers(100) during(60), nothingFor(300.seconds), rampUsers(200) during(60),nothingFor(300.seconds),rampUsers(75) during(60),nothingFor(300.seconds),rampUsers(200) during(60),nothingFor(300.seconds))
    //val customCheck_3 = Seq(rampUsers(100) during(60), nothingFor(120.seconds), rampUsers(200) during(60),nothingFor(120.seconds),rampUsers(300) during(60),nothingFor(120.seconds),rampUsers(500) during(60),nothingFor(300.seconds))
    ////val customCheck_3 = Seq(rampUsers(100) during(60), nothingFor(120.seconds), rampUsers(200) during(60),nothingFor(120.seconds),rampUsers(300) during(60),nothingFor(150.seconds),rampUsers(500) during(60),nothingFor(300.seconds))
    val customCheck_Incremental = Seq(rampUsers(50) during(60), nothingFor(300.seconds),
      rampUsers(50) during(60),nothingFor(300.seconds),
      rampUsers(50) during(60),nothingFor(300.seconds),
      rampUsers(50) during(60),nothingFor(300.seconds),
      rampUsers(50) during(60),nothingFor(300.seconds),
      rampUsers(50) during(60),nothingFor(300.seconds),
      rampUsers(50) during(60),nothingFor(300.seconds),
      rampUsers(50) during(60),nothingFor(300.seconds),
      rampUsers(50) during(60),nothingFor(300.seconds),
      rampUsers(50) during(60),nothingFor(300.seconds))
    val customCheck_Constant = Seq(constantUsersPerSec(25) during(60), nothingFor(300.seconds), rampUsers(35) during(60),nothingFor(300.seconds),rampUsers(200) during(60),nothingFor(300.seconds),rampUsers(300) during(60),nothingFor(300.seconds),rampUsers(400) during(60),nothingFor(300.seconds),rampUsers(500) during(60),nothingFor(300.seconds))
    val containerLoadTest = Seq(rampUsers(100) during(60),nothingFor(60.seconds),rampUsers(200) during(60),nothingFor(60.seconds),rampUsers(400) during(60),nothingFor(60.seconds),rampUsers(500) during(60),nothingFor(60.seconds),rampUsers(600) during(60))
    val customCheck_DryRun = atOnceUsers(1)
    val amiWishlistLoadTest_1 = Seq(rampUsers(25) during(60),nothingFor(600.seconds),rampUsers(10) during(60),nothingFor(600.seconds),rampUsers(10) during(60),nothingFor(600.seconds),rampUsers(10) during(10),nothingFor(600.seconds),rampUsers(10) during(60),nothingFor(600.seconds))
    val amiWishlistLoadTest_2 = Seq(nothingFor(3500.seconds), rampUsers(25) during(60),nothingFor(600.seconds),rampUsers(10) during(60),nothingFor(600.seconds),rampUsers(10) during(60),nothingFor(600.seconds),rampUsers(10) during(10),nothingFor(600.seconds),rampUsers(10) during(60),nothingFor(600.seconds))
    val amiWishlistLoadTest_3 = Seq(nothingFor(7000.seconds), rampUsers(25) during(60),nothingFor(600.seconds),rampUsers(10) during(60),nothingFor(600.seconds),rampUsers(10) during(60),nothingFor(600.seconds),rampUsers(10) during(10),nothingFor(600.seconds),rampUsers(10) during(60),nothingFor(600.seconds))
    val customCheck_3 = Seq(constantUsersPerSec(0.5) during(120))
    val customWishlistCheck_1 = Seq(atOnceUsers(25))
    val customWishlistCheck_0 = Seq(atOnceUsers(15))
    val customWishlistCheck_2 = Seq(atOnceUsers(35))
    val customWishlistCheck_3 = Seq(atOnceUsers(45))
    val customWishlistCheck_4 = Seq(atOnceUsers(55))
    val customWishlistCheck_5 = Seq(atOnceUsers(65))
    val customWishlistCheck_6 = Seq(rampUsers(100) during(60), nothingFor(3600.seconds))
    val customCartCheckoutCheck_6 = Seq(rampUsers(60) during(60), nothingFor(600.seconds))
    val customAddressExecution = Seq(rampUsers(100) during(60), nothingFor(600.seconds))
    //val customCheck_3 = Seq(rampUsers(25) during(60), nothingFor(300.seconds))
    val customCheck_2 = Seq(nothingFor(90 seconds), atOnceUsers(10))

    val customCheckout_1 = Seq(rampUsers(100)during(60), nothingFor(1800.seconds))
    val customCheckout_2 = Seq(rampUsers(75)during(60), nothingFor(1800.seconds))
    val customCheckout_3 = Seq(rampUsers(40)during(60), nothingFor(1800.seconds))

    val customCheckout_4 = Seq(rampUsers(30) during (60), nothingFor(600 seconds),rampUsers(20) during (60), nothingFor(600 seconds),rampUsers(10) during (60), nothingFor(600 seconds),rampUsers(10) during (60), nothingFor(600 seconds),rampUsers(10) during (60), nothingFor(600 seconds),rampUsers(10) during (60), nothingFor(600 seconds))
    val customCheckout_44 = Seq(nothingFor(300.seconds), atOnceUsers(1))
    val customCheckout_7 = Seq(rampUsers(50) during (60),nothingFor(300 seconds),rampUsers(20) during (60),nothingFor(300 seconds),rampUsers(10) during (60),nothingFor(300 seconds),rampUsers(10) during (60),nothingFor(300 seconds),rampUsers(10) during (60),nothingFor(300 seconds),rampUsers(10) during (60),nothingFor(300 seconds))
    val customCheckout_8 = Seq(rampUsers(60) during (60),nothingFor(300 seconds),rampUsers(15) during (60),nothingFor(300 seconds),rampUsers(15) during (60),nothingFor(300 seconds),rampUsers(15) during (60),nothingFor(300 seconds),rampUsers(15) during (60),nothingFor(300 seconds),rampUsers(15) during (60),nothingFor(300 seconds))
    val customCheckout_5 = Seq(rampUsers(25) during (60), nothingFor(1800.seconds))
    val customCheckout_6 = Seq(rampUsers(15) during (60), nothingFor(1800.seconds))

    // Loads for nightly run
    //val _BaseLineTestUsers = Seq(heavisideUsers(environment.user1Volume) during(environment.total1Duration seconds))
    val _BaseLineTestUsers20 = Seq(heavisideUsers(2) during(200 seconds)) // Dropdown Value 100/1000
    val _BaseLineTestUsers100 = Seq(heavisideUsers(100) during(120 seconds)) // Dropdown Value 200/1000
    val _BaseLineTestUsers300 = Seq(heavisideUsers(300) during(900 seconds)) // Dropdown Value 500/1000
    val _StressTestResolveAMessage = Seq(heavisideUsers(5000) during(6 hours))

  }

  /**
   * Creates the HTTP protocol builder used in the simulation.
   */
  // Https Protocol Section
  def createHttpProtocolBuilder(): HttpProtocolBuilder = {
    val httpProtocolBuilder = http // 4 - The common configuration to all HTTP requests
      .baseUrls(environment.testServerUrl) // 5 - The baseURL that will be prepended to all relative urls
      .inferHtmlResources()
      .disableCaching
    httpProtocolBuilder
  }

  after {
    // delete whatever I created in the before method
    // THIS METHOD DOES NOT EXECUTE if the feeder is emptied
    // I need it to execute regardless of errors during the scenario
    inputStream.close()
    println("Simulation is finished!")
  }
}