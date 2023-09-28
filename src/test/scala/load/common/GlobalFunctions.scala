package load.common

import java.time.LocalDateTime
import io.gatling.core.Predef._
import io.gatling.http.Predef._
import load.testData.MonitoringConstant
import load.utils.Environment
import org.yaml.snakeyaml.Yaml

import java.io.{File, FileInputStream}
import java.time.format.DateTimeFormatter
import scala.collection._
import java.util.concurrent.ConcurrentHashMap
import scala.collection.JavaConverters._
import scala.collection.immutable.Map
import scala.language.postfixOps
import scala.util.Random


class GlobalFunctions extends Simulation {
  // parse load profile
  val monitoringConstant = new MonitoringConstant()
  val environment = new Environment()
  val randomNumber = new scala.util.Random

  val cache  : concurrent.Map[String, String] = new ConcurrentHashMap[String, String] asScala
  val locked : concurrent.Map[String, String]   = new ConcurrentHashMap[String, String] asScala

  // first thread to call lock(key) returns true, all subsequent ones will return false
  def lock ( key: String, who: String ) : Boolean = locked.putIfAbsent(key, who ) == None

  // only the thread that first called lock(key) can call put, and then only once
  def put( key: String, value: String, who: String ) =
    if ( locked.get( key ).get == who )
      if ( cache.get( key ) == None )
        cache.put( key, value )
      else
        throw new Exception( "You can't put more than one value into the cache! " + key )
    else
      throw new Exception( "You have not locked '" + key + "'" )

  // any thread can call get - will block until a non-null value is stored in the cache
  // WARNING: if the thread that is holding the lock never puts a value, this thread will block forever
  def get( key: String ) = {
    if ( locked.get( key ) == None )
      throw new Exception( "Must lock '" + key + "' before you can get() it" )
    var result : Option[String] = None
    do {
      result = cache.get( key )
      if ( result == None ) Thread.sleep( 100 )
    } while ( result == None )
    result.get
  }

  object Auth {
    val checkSessionIsOn = exec(
      http("check_session_is_on")
        .get(environment.testServerUrl)
        .check(status.is(monitoringConstant.successStatus))
    )

    val checkSessionIsOff = exec(
      http("check_session_is_off")
        .get(environment.testServerUrl)
        .check(status.not(monitoringConstant.successStatus))
    )
  }

  object Global {

    def getRandomAlphabets(maxRange: Int): String = {
      return "perf_" + scala.util.Random.alphanumeric.take(maxRange).mkString
    }

    def getRandomString(maxRange: Int): String = {
      return "perf_" + Iterator.continually(Map("emailIDs" -> (Random.alphanumeric.take(maxRange).mkString)))
    }

    def getRandomNumber(maxRange: Int): Int = {
      return randomNumber.nextInt(maxRange)
    }

    def getRandomNumberInBetween(minRange: Int, maxRange: Int): Int = {
      return minRange + randomNumber.nextInt( (maxRange - minRange) + 1 )
    }

    def customThinkTime(isThinkEnabled: Boolean, thinkTimeMax: Int): Int= {
      val thinkMin: Int = monitoringConstant.thinkMin
      if(isThinkEnabled) {
        return thinkMin + randomNumber.nextInt((thinkTimeMax - thinkMin) + 1 )
      } else {
        return thinkTimeMax
      }
    }

    def getSystemCurrentTimeInMillis(): String = {
      return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS").format(LocalDateTime.now())
    }

    def getSystemTimeForMessageReceivedEnd(): String = {
      return DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").format(LocalDateTime.now())
    }

    /*    def getRandomString(kiId:Vector[String]): String ={
          return kiId(randomNumber.nextInt(kiId.size)).toString
        }*/

    // Define an infinite feeder which calculates random numbers
    /*val randomNumbers = Iterator.continually(
      // Random number will be accessible in session under variable "randomNum"
      Map("randomNum" -> util.Random.nextInt(Integer.MAX_VALUE))
    )*/

    def readYaml(env: String): java.util.Map[String, Any] = {
      var ymlData: java.util.Map[String, Any] = null
      try {
        // Read the YAML file
        val yamlFile = new File("runtimeConfigLoad/api.key.yml")
        if (yamlFile.exists()) {
          val inputStream = new FileInputStream(yamlFile)
          val yaml = new Yaml()
          val data = yaml.load(inputStream).asInstanceOf[java.util.Map[String, Any]]

          // Check if the "preprod" field exists
          if (data.containsKey(env)) {
            ymlData = data.get(env).asInstanceOf[java.util.Map[String, Any]]
          } else {
            println(s"$env not found in YAML")
          }

          inputStream.close()
        } else {
          println("YAML file not found")
        }
      } catch {
        case e: Exception =>
          println(s"Error reading YAML file: ${e.getMessage}")
      }

      return ymlData
    }

  }
}