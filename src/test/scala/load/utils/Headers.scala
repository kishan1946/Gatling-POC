package load.utils

import load.common.GlobalFunctions

class Headers {
  val globalFunctions = new GlobalFunctions()
  val env = System.getProperty("environment")
  val readYaml: java.util.Map[String, Any] = globalFunctions.Global.readYaml(env)
  val testServerUrl = readYaml.get("URL").toString
//  System.setProperty("url",testServerUrl)

}
