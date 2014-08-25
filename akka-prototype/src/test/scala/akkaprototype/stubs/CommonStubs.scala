package akkaprototype.stubs

import akka.actor.ActorLogging
import akka.event.LoggingReceive
import akkaprototype.internal.api.{FindDataOneProxy, FindDataThreeProxy, FindDataTwoProxy}
import akkaprototype.internal.messages.{ProcessAction, ActionOneData, ActionThreeData, ActionTwoData}

class FindDataOneStub extends FindDataOneProxy with ActorLogging {
  val data = Map[Long, List[(Long, Double)]](
    1L -> List((3l, 15000d)),
    2L -> List((6l, 640000d), (7l, 1125000d), (8l, 40000d))
  )

  def receive = LoggingReceive {
    case ProcessAction(id: Long) =>
      log.debug(s"Received process action for ID: $id")
      data.get(id) match {
        case Some(data) => sender ! ActionOneData(Some(data))
        case None => sender ! ActionOneData(Some(List()))
      }
  }
}

class FindDataTwoStub extends FindDataTwoProxy with ActorLogging {
  val data = Map[Long, List[(Long, Double)]](
    1L -> (List((1l, 150000d), (2l, 29000d))),
    2L -> (List((5l, 80000d))))

  def receive = LoggingReceive {
    case ProcessAction(id: Long) =>
      log.debug(s"Received process action for ID: $id")
      data.get(id) match {
        case Some(data) => sender ! ActionTwoData(Some(data))
        case None => sender ! ActionTwoData(Some(List()))
      }
  }
}

class FindDataThreeStub extends FindDataThreeProxy with ActorLogging {
  val data = Map[Long, List[(Long, Double)]](
    2L -> List((9l, 640000d), (10l, 1125000d), (11l, 40000d)))

  def receive = LoggingReceive {
    case ProcessAction(id: Long) =>
      log.debug(s"Received process action for ID: $id")
      data.get(id) match {
        case Some(data) => sender ! ActionThreeData(Some(data))
        case None => sender ! ActionThreeData(Some(List()))
      }
  }
}

class FindDataTwoStubTimeOut extends FindDataTwoProxy with ActorLogging {
  def receive = LoggingReceive {
    case ProcessAction(id: Long) =>
      log.debug(s"Forcing timeout by not responding!")
  }
}