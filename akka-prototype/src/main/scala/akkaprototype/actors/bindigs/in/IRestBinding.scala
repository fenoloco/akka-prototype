package akkaprototype.actors.bindigs.in

import akka.actor.Actor
import spray.http.MediaTypes.`application/json`
import spray.httpx.marshalling.ToResponseMarshallable
import spray.routing.HttpService

import scala.language.postfixOps


class RestServiceActor extends Actor with RestService {
  def actorRefFactory = context
  def receive = runRoute(router)
}

trait RestService extends HttpService {

  val jsonRoute = {
    get {
      path("") {
        respondWithMediaType(`application/json`) {
          complete {
            ToResponseMarshallable.isMarshallable(<html>
              <body>
                <h1>Testing</h1>
              </body>
            </html>)
          }
        }
      }
    }
  }
  val router = jsonRoute
}

