package akkaprototype

import akka.actor.{ActorSystem, Props}
import akka.io.IO
import akka.routing.FromConfig
import akkaprototype.rest.actors.DemoServiceActor
import org.slf4j.{Logger, LoggerFactory}
import spray.can.Http


/**
 * Created by Mauri on 12/08/2014.
 */
object Main3 extends App {

  val logger: Logger = LoggerFactory.getLogger("Boot.scala");
  logger.info("Simple example with out a router and a dispatcher router")

  implicit val system = ActorSystem()
  logger.info("ActorSystem Created...")

  system.settings
  logger.info("ActorSystem settings readed...")

  val handler = system.actorOf(FromConfig.props(Props[DemoServiceActor]), "tester")
  logger.info("DemoService Created...")

  IO(Http) ! Http.Bind(handler, interface = "0.0.0.0", port = 8280)
  logger.info("Rest service Bound...")

  sys.addShutdownHook(system.shutdown())

}