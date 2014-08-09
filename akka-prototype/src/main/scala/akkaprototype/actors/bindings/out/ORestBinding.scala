package akkaprototype.actors.bindings.out

import akka.actor.Actor

/**
 * Created by Mauri on 08/08/2014.
 */
class ORestBinding extends Actor {

  def receive = {
    case _ => println("OHttpBinding send to the external service")
  }
}


