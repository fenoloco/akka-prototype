package akkaprototype.actors.bindigs.in

import akka.actor.Actor
import spray.http.MediaTypes.`application/json`
import spray.routing.HttpService

import scala.language.postfixOps


class RestServiceActor extends Actor with RestService {
  def actorRefFactory = context
  def receive = runRoute(router)

}

trait RestService extends HttpService {
  val jsonRoute = {
    get {
      path("api") {
        respondWithMediaType(`application/json`) {
          complete {
            "[]"
          }
        }
      }
    }
  }
  val router = jsonRoute
}

