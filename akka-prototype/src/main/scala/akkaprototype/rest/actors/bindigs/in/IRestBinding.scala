package akkaprototype.rest.actors

import akka.actor._
import akka.pattern.ask
import akkaprototype.internal.actors.AggregateMessageDataActor
import akkaprototype.internal.messages.ProcessAction
import spray.can.Http
import spray.can.server.Stats
import spray.http._
import spray.httpx.marshalling.Marshaller
import spray.routing.HttpService
import spray.util._

import scala.concurrent.duration._


// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class DemoServiceActor extends Actor with DemoService with ActorLogging {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing,
  // timeout handling or alternative handler registration
  def receive = runRoute(demoRoute)

  log.info("Stopping context capturing actor")
}


// this trait defines our service behavior independently from the service actor
trait DemoService extends HttpService {

  // we use the enclosing ActorContext's or ActorSystem's dispatcher for our Futures and Scheduler
  implicit def executionContext = actorRefFactory.dispatcher

  val demoRoute = {
    get {
      pathSingleSlash {
        complete(index)
      } ~
        path("ping") {
          complete("PONG!")
        } ~
        path("processAction") {
          complete {
            actorRefFactory.actorOf(Props[AggregateMessageDataActor]) ! ProcessAction(12);
            "processAction"
          }
        } ~
        path("stats") {
          complete {
            actorRefFactory.actorSelection("/user/IO-HTTP/listener-0")
              .ask(Http.GetStats)(1.second)
              .mapTo[Stats]
          }
        } ~
        path("timeout") { ctx =>
          // we simply let the request drop to provoke a timeout
        } ~
        path("crash") { ctx =>
          sys.error("crash boom bang")
        } ~
        path("fail") {
          failWith(new RuntimeException("aaaahhh"))
        }
    } ~
      (post | parameter('method ! "post")) {
        path("stop") {
          complete {
            in(1.second) {
              actorSystem.shutdown()
            }
            "Shutting down in 1 second..."
          }
        }
      }
  }

  lazy val index =
    <html>
      <body>
        <h1>Say hello to
          <i>spray-routing</i>
          on
          <i>spray-can</i>
          !</h1>
        <p>Defined resources:</p>
        <ul>
          <li>
            <a href="/ping">/ping</a>
          </li>
          <li>
            <a href="/stats">/stats</a>
          </li>
          <li>
            <a href="/timeout">/timeout</a>
          </li>
          <li>
            <a href="/crash">/crash</a>
          </li>
          <li>
            <a href="/fail">/fail</a>
          </li>
          <li>
            <a href="/stop?method=post">/stop</a>
          </li>
        </ul>
      </body>
    </html>

  implicit val statsMarshaller: Marshaller[Stats] =
    Marshaller.delegate[Stats, String](ContentTypes.`text/plain`) { stats =>
      "Uptime                : " + stats.uptime.formatHMS + '\n' +
        "Total requests        : " + stats.totalRequests + '\n' +
        "Open requests         : " + stats.openRequests + '\n' +
        "Max open requests     : " + stats.maxOpenRequests + '\n' +
        "Total connections     : " + stats.totalConnections + '\n' +
        "Open connections      : " + stats.openConnections + '\n' +
        "Max open connections  : " + stats.maxOpenConnections + '\n' +
        "Requests timed out    : " + stats.requestTimeouts + '\n'
    }

  def in[U](duration: FiniteDuration)(body: => U): Unit =
    actorSystem.scheduler.scheduleOnce(duration)(body)
}