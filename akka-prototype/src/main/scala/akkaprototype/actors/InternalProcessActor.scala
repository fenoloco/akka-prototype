package akkaprototype.actors

import akka.actor.Actor
import akkaprototype.messages.IMessage

/**
 * Created by Mauri on 08/08/2014.
 */
class InternalProcessActor extends Actor {

  def receive = {
    case IMessage(data) => println(s"Data arrived:$data")
  }
}


