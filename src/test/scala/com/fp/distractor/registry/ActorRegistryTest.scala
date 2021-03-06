package com.fp.distractor.registry

import akka.actor.{Actor, Props}
import akka.testkit.{TestActorRef, TestProbe}
import com.fp.common.AkkaActorTest
import com.fp.distractor.registry.ActorRegistry.{RegisteredMsg, GetRegisteredMsg, RegisterMsg, UnregisterMsg}

class ActorRegistryTest extends AkkaActorTest {

  val ACTOR_ID: String = "some_id"

  "an actor with ActorRegistry trait mixed in" should {

    // given
    val actorToRegister = TestProbe()
    val actor = TestActorRef(Props[WithActorRegistry])

    "have the possibility to register other actors" in {

      // when
      actor ! RegisterMsg(ACTOR_ID, actorToRegister.ref)
      actor ! GetRegisteredMsg

      // then
      expectMsg(new RegisteredMsg(List(ACTOR_ID)))
    }

    "have the possibility to unregister other actors" in {

      // when
      actor ! RegisterMsg(ACTOR_ID, actorToRegister.ref)
      actor ! UnregisterMsg(ACTOR_ID)
      actor ! GetRegisteredMsg

      // then
      expectMsg(new RegisteredMsg(List.empty[String]))
    }
  }

}

class WithActorRegistry extends Actor with ActorRegistry {
  override def receive = handleRegistry
}
