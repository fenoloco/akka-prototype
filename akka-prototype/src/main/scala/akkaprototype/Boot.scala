package akkaprototype


import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akkaprototype.actors.bindigs.in.RestServiceActor
import org.slf4j.{Logger, LoggerFactory}
import spray.can.Http

/**
 * Created by Mauri on 08/08/2014.
 */
object Main extends App {

  val logger: Logger = LoggerFactory.getLogger("Boot.scala");

  implicit val system = ActorSystem()

  val handler = system.actorOf(Props[RestServiceActor], name = "rest-service")
  logger.info("RestServiceActor Created...")

  IO(Http) ! Http.Bind(handler, interface = "0.0.0.0", port = 8280)
}