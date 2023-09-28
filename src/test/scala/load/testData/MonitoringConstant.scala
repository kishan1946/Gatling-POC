package load.testData

import io.gatling.core.Predef._

class MonitoringConstant {
  val password: String = System.getProperty("password", "1234")
  val userTestDataPath = System.getProperty("userTestDataPath", "./src/test/scala/load/testData/")
  val userData = csv(userTestDataPath + "userData.csv")

  val logLevel = System.getProperty("logLevel", "WARN")
  val receivedStartDate: String = "1899-12-31T18:45:00.000Z"
  val successStatus: Int = 200
  val successStatusCreate: Int = 201
  val successStatusAccepted: Int = 202
  val statusNoContent: Int = 204
  val successRedirectsStatus: Int = 302
  val pauseMin: Int = 1
  val pauseMax: Int = 10
  val paceMin: Int = 1
  val paceMax: Int = 4
  val tryMin: Int = 1
  val tryMax: Int = 1
  val thinkMin: Int = 5
  val thinkMax: Int = 20
  val repeat6 = 6
  val startPageNumber: Int = 1

  //#feeder-from-array-with-random
  val collectionOfKeywordTextPercentage = Array(
    Map("keywordText" -> "bookout", "percentValue" -> "90"),
    Map("keywordText" -> "remedies", "percentValue" -> "75"),
    Map("keywordText" -> "offsite", "percentValue" -> "50"),
    Map("keywordText" -> "examples", "percentValue" -> "25"),
    Map("keywordText" -> "test", "percentValue" -> "10"),
    Map("keywordText" -> "log", "percentValue" -> "5")
  )

  //#feeder-from-array-with-random
  val scaleKeyIndicatorNames = Array(
    Map("ki" -> "AWfoREFxQbvhRU4vftc_", "order" -> "asc"),
    Map("ki" -> "AWfoSApwz339lKCcujvP", "order" -> "desc"),
    Map("ki" -> "AWfoRUZHee3eMlxOUjNU", "order" -> "asc"),
    Map("ki" -> "AWfoRuGbjr_dbad4347H", "order" -> "desc")
  )

  //#feeder-from-array-with-random
  val orderWay = Array(
    Map("order" -> "asc"),
    Map("order" -> "desc"),
    Map("order" -> "asc"),
    Map("order" -> "desc")
  )

  //#feeder-from-array-with-random
  val harmanKeyIndicatorNames = Array(
    Map("ki" -> "AWfOkMyPQee4NVSPXAr6", "order" -> "asc"),
    Map("ki" -> "AWfOkO53RcmU5GU4Rabb", "order" -> "desc"),
    Map("ki" -> "AWfOkQTr957byjWyXY1k", "order" -> "asc"),
    Map("ki" -> "AWfOkPmhqPuVH3wtrXwt", "order" -> "desc")
  )

  val kiId = Set("AWjjOV2eg9ZXE2DBqZMh", "AWjjOwx5dKZcWJi9yf6j", "AWjjOdPZCBe_kDcgPbSk", "AWjjOoIR4Ox6Gi-XgsDR").toVector
  //val kiId = Set("AWj-I1IY1YArkcVP7JlT", "AWj-I2aF6QY5w6NSTglK", "AWj-I3DdCFxspqmt6gzj", "AWj-IzMFCFxspqmt6gzd", "AWj-I0exCFxspqmt6gzf", "AWj-JpKb6QY5w6NSTgl0", "AWj-Iz1l1YArkcVP7JlR", "AWj-I1xfudn6XNUiz3-k", "AWj_pBXwCFxspqmt6iVJ", "AWj_pMwhCFxspqmt6iVU").toVector

  val pageNumbers = Array(
    Map("pageNumber" -> "1"),
    Map("pageNumber" -> "2"),
    Map("pageNumber" -> "3"),
    Map("pageNumber" -> "4"),
    Map("pageNumber" -> "5"),
    Map("pageNumber" -> "6"),
    Map("pageNumber" -> "7"),
    Map("pageNumber" -> "8"),
    Map("pageNumber" -> "9")
  )

  val actions = Array(
    Map("action" -> "reviewed"),
    Map("action" -> "spam"),
    Map("action" -> "news"),
    Map("action" -> "follow-up"),
    Map("action" -> "escalate"),
    Map("action" -> "breach")
  )
}
