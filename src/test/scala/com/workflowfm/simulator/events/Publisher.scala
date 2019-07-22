package com.worklflowfm.simulator.events

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.event.{ Logging, LoggingReceive }
import akka.testkit.{ ImplicitSender, TestActors, TestKit }
import com.typesafe.config.ConfigFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.concurrent.duration._

@RunWith(classOf[JUnitRunner])
class PublisherTests extends TestKit(ActorSystem("PublisherTests", ConfigFactory.parseString(MockPublisher.config))) with
    WordSpecLike with Matchers with BeforeAndAfterAll with ImplicitSender {

  //implicit val timeout:FiniteDuration = 10.seconds
  override def beforeAll:Unit = {
    system.eventStream.setLogLevel(Logging.DebugLevel)
  }
  override def afterAll:Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "The Publisher" must {
    "publish a single event" in {
      val p = MockPublisher.actor(system,EStart)

      p ! Publisher.Subscribe
      expectMsg(Publisher.Ack)

      p ! MockPublisher.Publish
      expectMsg(EStart)
      expectNoMessage
    }

    /*
    "publish 2 events" in {
      val p = MockPublisher.actor(system,EStart,EStart)

      p ! Publisher.Subscribe
      expectMsg(Publisher.Ack)

      p ! MockPublisher.Publish
      expectMsgAllOf(EStart,EStart)
      expectNoMessage
    }
     */

    "publish 10 events" in {
      val es = Seq(EStart,EStart,EStart,EStart,EStart,EStart,EStart,EStart,EStart,EStart)
      val p = MockPublisher.actor(system,es:_*)

      p ! Publisher.Subscribe
      expectMsg(Publisher.Ack)

      p ! MockPublisher.Publish
      expectMsgAllOf(es:_*)
      expectNoMessage
    }

    "publish 1 event to twice subscriber" in {
      val p = MockPublisher.actor(system,EStart)

      p ! Publisher.Subscribe
      expectMsg(Publisher.Ack)
      p ! Publisher.Subscribe
      expectMsg(Publisher.Ack)

      p ! MockPublisher.Publish
      expectMsgAllOf(EStart,EStart)
      expectNoMessage
    }
  }
}

class MockPublisher(override val system: ActorSystem, events: Event*) extends PublisherActor {
  override def receiveBehaviour = {
    case MockPublisher.Publish => events map mpublish
  }

  def mpublish(e: Event) = {
    println(s">>> ${Event.asString(e)}")
    publish(e)
  }
}
object MockPublisher {
  case object Publish

  def actor(system: ActorSystem, events: Event*): ActorRef = system.actorOf(Props(
    new MockPublisher(system, events :_*)
  ))

  val config = """
akka {
    stdout-loglevel = "DEBUG"
    loglevel = "DEBUG"    
    actor {
      debug {
        receive = on
        unhandled = on
      }
    }
}
    """

}

/*
 event-stream = on
 autoreceive = on
 lifecycle = on
 */
