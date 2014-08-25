package akkaprototype.internal.actors

import akka.actor._
import akka.event.LoggingReceive
import akkaprototype.internal.messages._

import scala.concurrent.duration._

object AggregateMessageDataHandler {
  def props(findDataOne: ActorRef, findDataTwo: ActorRef,
            findDataThree: ActorRef, originalSender: ActorRef): Props = {
    Props(new AggregateMessageDataHandler(findDataOne, findDataTwo, findDataThree, originalSender))
  }
}

class AggregateMessageDataHandler(findDataOne: ActorRef, findDataTwo: ActorRef,
                                  findDataThree: ActorRef, originalSender: ActorRef) extends Actor with ActorLogging {

  var actionOneResponse, actionTwoResponse, actionTreeResponse: Option[List[(Long, Double)]] = None

  def receive = LoggingReceive {
    case ActionOneData(data) =>
      log.debug(s"Received ActionOneData response: $data")
      actionOneResponse = data
      collectActionResponse
    case ActionTwoData(data) =>
      log.debug(s"Received ActionTwoData response: $data")
      actionTwoResponse = data
      collectActionResponse
    case ActionThreeData(data) =>
      log.debug(s"Received ActionThreeData response: $data")
      actionTreeResponse = data
      collectActionResponse
    case ProcessActionTimeOut =>
      log.debug("Timeout occurred")
      sendResponseAndShutdown(ProcessActionTimeOut)
  }

  def collectActionResponse = (actionOneResponse, actionTwoResponse, actionTreeResponse) match {
    case (Some(c), Some(s), Some(m)) =>
      log.debug(s"Values received for all three action")
      timeoutMessager.cancel
      sendResponseAndShutdown(ActionData(actionOneResponse, actionTwoResponse, actionTreeResponse))
    case _ =>
  }

  def sendResponseAndShutdown(response: Any) = {
    originalSender ! response
    log.debug("Stopping context capturing actor")
    context.stop(self)
  }

  import context.dispatcher

  val timeoutMessager = context.system.scheduler.scheduleOnce(
    20000 milliseconds, self, ProcessActionTimeOut)
}


class AggregateMessageDataActor() extends Actor with ActorLogging {

  var actionOneActor = context.actorOf(Props[FindDataOneActor], "find-data-one")
  var actionTwoActor = context.actorOf(Props[FindDataTwoActor], "find-data-two")
  var actionThreeActor = context.actorOf(Props[FindDataThreeActor], "find-data-three")

  def this(findDataOne: ActorRef, findDataTwo: ActorRef, findDataThree: ActorRef) {
    this()
    this.actionOneActor = findDataOne
    this.actionTwoActor = findDataTwo
    this.actionThreeActor = findDataThree
  }

  def receive = {
    case ProcessAction(id) =>
      log.info(s"Process message with id: $id")
      val originalSender = sender
      val handler = context.actorOf(
        AggregateMessageDataHandler.props(actionOneActor, actionTwoActor, actionThreeActor, originalSender),
        "agregate-action-message-handler")
      actionOneActor.tell(ProcessAction(id), handler)
      actionTwoActor.tell(ProcessAction(id), handler)
      actionThreeActor.tell(ProcessAction(id), handler)
  }
}