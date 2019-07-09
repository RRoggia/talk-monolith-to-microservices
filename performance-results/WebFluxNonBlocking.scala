package perf 

import io.gatling.core.Predef._ 
import io.gatling.http.Predef._
import scala.concurrent.duration._
import io.gatling.core.feeder._
import io.gatling.core.session._
import scala.io.Source

class WebFluxNonBlocking extends Simulation { 

  val DurationMinutes = 2
  val NumberOfUsers = 100
  val Endpoint = "/route?delay=0"
  val BaseUrl = "http://localhost:8083"

  println ("Parameters:\n------------------")
  printf ("Duration %d minutes:\n", DurationMinutes)
  printf ("Users: %d\n", NumberOfUsers)
  printf ("Endpoint: %s\n", Endpoint)
  printf ("URL: %s\n", BaseUrl)  


  val httpProtocol = http 
    .baseUrl(BaseUrl)
    .header("Content-Type", "application/json")
    .acceptHeader("application/json") 
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")


  val warm_up= scenario("Warm up") 
    .during(30 seconds) {
      exec(http("warm_up") 
      .get(Endpoint)) 
  }
  val scn0 = scenario("Route to HTTP Engine - 0") 
    .during(DurationMinutes minutes) {
      exec(http("exec_0") 
      .get(Endpoint)) 
  }
  val scn1 = scenario("Route to HTTP Engine - 1") 
    .during(DurationMinutes minutes) {
      exec(http("exec_1") 
      .get(Endpoint)) 
  }
  val scn2 = scenario("Route to HTTP Engine - 2") 
    .during(DurationMinutes minutes) {
      exec(http("exec_2") 
      .get(Endpoint)) 
  }

  val allScenarios = warm_up.pause(15 seconds).exec(scn0).pause(30 seconds).exec(scn1).pause(30 seconds).exec(scn2)

  setUp(
       allScenarios.inject(atOnceUsers(NumberOfUsers))
  ).protocols(httpProtocol) 

}