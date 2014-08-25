package akkaprototype.test

import akka.actor.{ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit, TestProbe}
import akkaprototype.internal.actors.AggregateMessageDataActor
import akkaprototype.internal.messages.{ActionData, ProcessAction, ProcessActionTimeOut}
import akkaprototype.stubs.{FindDataOneStub, FindDataThreeStub, FindDataTwoStub, FindDataTwoStubTimeOut}
import org.scalatest.WordSpecLike
import org.scalatest.matchers.MustMatchers

import scala.concurrent.duration._

class TestService extends TestKit(ActorSystem("TestAS")) with ImplicitSender with WordSpecLike with MustMatchers {

  "An Agregegate message" should {
    "return a list of diferente action data" in {
      val probe1 = TestProbe()
      val probe2 = TestProbe()
      val actionOneActor = system.actorOf(Props[FindDataOneStub], "find-data-one-stub")
      val actionTwoActor = system.actorOf(Props[FindDataTwoStub], "find-data-two-stub")
      val actionThreeActor = system.actorOf(Props[FindDataThreeStub], "find-data-three-stub")
      val agregateDataActor = system.actorOf(
        Props(new AggregateMessageDataActor()), "agregate-data-actor")

      within(300 milliseconds) {
        probe1.send(agregateDataActor, ProcessAction(1L))
        val result = probe1.expectMsgType[ActionData]
        result must equal(ActionData(Some(List((3l, 15000d))), Some(List((1l, 150000d), (2l, 29000d))),
          Some(List())))
      }
      within(300 milliseconds) {
        probe2.send(agregateDataActor, ProcessAction(2L))
        val result = probe2.expectMsgType[ActionData]
        result must equal(
          ActionData(Some(List((6l, 640000d), (7l, 1125000d), (8l, 40000d))), Some(List((5l, 80000d))),
            Some(List((9l, 640000d), (10l, 1125000d), (11l, 40000d)))))
      }
    }

    "return a TimeoutException when timeout is exceeded" in {
      val actionOneActor = system.actorOf(Props[FindDataOneStub], "find-data-one-stub-timeout")
      val actionTwoActorTimeOut = system.actorOf(Props[FindDataTwoStubTimeOut], "find-data-two-stub-timeout")
      val actionThreeActor = system.actorOf(Props[FindDataThreeStub], "find-data-three-stub-timeout")
      val agregateDataActor = system.actorOf(
        Props(new AggregateMessageDataActor(actionOneActor, actionTwoActorTimeOut, actionThreeActor)),
        "agregate-data-actor-timeout")
      val probe = TestProbe()
      within(250 milliseconds, 500 milliseconds) {
        probe.send(agregateDataActor, ProcessAction(1L))
        probe.expectMsg(ProcessActionTimeOut)
      }
    }
  }
}