package akkaprototype.internal.actors

import akka.actor.ActorLogging
import akka.event.LoggingReceive
import akkaprototype.internal.api.{FindDataOneProxy, FindDataThreeProxy, FindDataTwoProxy}
import akkaprototype.internal.messages.{ActionOneData, ActionThreeData, ActionTwoData, ProcessAction}

/**
 * Created by Mauri on 24/08/2014.
 */
class FindDataOneActor extends FindDataOneProxy with ActorLogging with FindDataGenericActor {

  def receive = LoggingReceive {
    case ProcessAction(id: Long) =>
      val sleep = getSleepTime
      log.info(s"TODO FindDataOneActor process action for ID: $id with delay $sleep")
      Thread.sleep(sleep)
      sender ! ActionOneData(Some(List((1l, 150000d), (2l, 29000d))))
  }
}

class FindDataTwoActor extends FindDataTwoProxy with ActorLogging with FindDataGenericActor {
  def receive = LoggingReceive {
    case ProcessAction(id: Long) =>
      val sleep = getSleepTime
      log.info(s"TODO FindDataTwoActor process action for ID: $id with delay $sleep")
      Thread.sleep(sleep)
      sender ! ActionTwoData(Some(List((1l, 150000d), (2l, 29000d))))
  }
}

class FindDataThreeActor extends FindDataThreeProxy with ActorLogging with FindDataGenericActor {
  def receive = LoggingReceive {
    case ProcessAction(id: Long) =>
      val sleep = getSleepTime
      log.info(s"TODO FindDataThreeActor process action for ID: $id with delay $sleep")
      Thread.sleep(sleep)
      sender ! ActionThreeData(Some(List((1l, 150000d), (2l, 29000d))))
  }
}

trait FindDataGenericActor {
  val r = new scala.util.Random

  def getSleepTime() = {
    val value = r.nextInt() % 10
    if(value > 0) value else -value
  }
}