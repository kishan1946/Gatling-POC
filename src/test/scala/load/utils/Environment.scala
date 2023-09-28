package load.utils

class Environment {
  val testServerUrl = System.getProperty("url")
  //val kgName = System.getProperty("kgName")
  val isThinkEnabled: Boolean = System.getProperty("isThinkEnabled").toBoolean
  //  val user1Volume: Int = System.getProperty("user1Volume").toInt
  //  val user2Volume: Int = System.getProperty("user2Volume").toInt
  //  val user3Volume: Int = System.getProperty("user3Volume").toInt
  //  val user4Volume: Int = System.getProperty("user4Volume").toInt
  //  val user5Volume: Int = System.getProperty("user5Volume").toInt
  //  val total1Duration: Int = System.getProperty("total1Duration").toInt
  //  val total2Duration: Int = System.getProperty("total2Duration").toInt
  //  val total3Duration: Int = System.getProperty("total3Duration").toInt
  //  val total4Duration: Int = System.getProperty("total4Duration").toInt
  //  val total5Duration: Int = System.getProperty("total5Duration").toInt
  //  val dropDown1Volume: Int = System.getProperty("dropDown1Volume").toInt
  //  val dropDown2Volume: Int = System.getProperty("dropDown2Volume").toInt
  val runType = System.getProperty("runType")
  //  val monitoringUrl = System.getProperty("monitoringUrl", "/apps/monitoring")
  //  val loginAndLogoutUrl = System.getProperty("monitoringUrl", "/apps/login")
  //  val monitoringSearchUrl = System.getProperty("searchUrl", "/apps/monitoring/#search")
  //  val userTestDataPath = System.getProperty("userTestDataPath", "./src/test/scala/digitalreasoning/testdata/")
}
