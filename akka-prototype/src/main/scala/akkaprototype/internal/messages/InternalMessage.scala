package akkaprototype.internal.messages

/**
 * Created by Mauri on 08/08/2014.
 */

case class ProcessAction(id: Long)

case class ActionData(val actionOneResponse: Option[List[(Long, Double)]],
                      val actionTwoResponse: Option[List[(Long, Double)]],
                      val actionThreeResponse: Option[List[(Long, Double)]])

case class ActionOneData(val data: Option[List[(Long, Double)]])

case class ActionTwoData(val data: Option[List[(Long, Double)]])

case class ActionThreeData(val data: Option[List[(Long, Double)]])

case object ProcessActionTimeOut
