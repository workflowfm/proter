package com.workflowfm.simulator.events

import akka.actor.{ ActorRef, ActorSystem, Props }
import akka.event.{ Logging, LoggingReceive }
import akka.testkit.{ ImplicitSender, TestActor, TestActors, TestKit, TestProbe }
import com.typesafe.config.ConfigFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import org.junit.runner.RunWith
import org.scalatest.junit.JUnitRunner
import scala.concurrent.{ Await, Future }
import scala.concurrent.duration._
import akka.pattern.{ask, pipe}
import scala.concurrent.ExecutionContext
import akka.util.Timeout

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

      val probe = MockObserver.probe(p)

      p ! MockPublisher.Publish

      probe.expectMsg(EStart)
      probe.reply(Publisher.StreamAck)

      probe.expectNoMessage
    }

    "publish 10 events" in {
      val es = Seq(EStart,EStart,EStart,EStart,EStart,EStart,EStart,EStart,EStart,EStart)
      val p = MockPublisher.actor(system,es:_*)

      val probe = MockObserver.probe(p)

      p ! MockPublisher.Publish

      es map { x =>
        probe.expectMsg(x)
        probe.reply(Publisher.StreamAck)
      }

      probe.expectNoMessage
    }

    "publish 1 event to twice subscriber" in {
      val p = MockPublisher.actor(system,EStart)

      val probe = MockObserver.probe(p)

      probe.send(p, Publisher.Subscribe(None))
      probe.expectMsgType[Publisher.StreamInit]
      probe.reply(Publisher.StreamAck)

      p ! MockPublisher.Publish

      probe.expectMsg(EStart)
      probe.reply(Publisher.StreamAck)
      probe.expectMsg(EStart)
      probe.reply(Publisher.StreamAck)
      probe.expectNoMessage
    }

    "publish events to 2 probes" in {
      val p = MockPublisher.actor(system,EStart,EDone(0L))

      val probe1 = MockObserver.probe(p)
      val probe2 = MockObserver.probe(p)

      p ! MockPublisher.Publish

      probe1.expectMsg(EStart)
      probe1.reply(Publisher.StreamAck)
      probe1.expectMsg(EDone(0L))
      probe1.reply(Publisher.StreamAck)
      probe1.expectMsg(Publisher.StreamDone)
      probe1.expectNoMessage

      probe2.expectMsg(EStart)
      probe2.reply(Publisher.StreamAck)
      probe2.expectMsg(EDone(0L))
      probe2.reply(Publisher.StreamAck)
      probe2.expectMsg(Publisher.StreamDone)
      probe2.expectNoMessage
    }

    "publish events to 3 observers" in {
      val p = MockPublisher.actor(system,EStart,EStart,EDone(0L))

      val f1 = MockObserver.observer(new CounterHandler())(system,p)
      val f2 = MockObserver.observer(new CounterHandler())(system,p)
      val f3 = MockObserver.observer(new CounterHandler())(system,p)

      //Thread.sleep(1000)
      p ! MockPublisher.Publish

      Await.result(f1, 1.seconds) should be (3)
      Await.result(f2, 1.seconds) should be (3)
      Await.result(f3, 1.seconds) should be (3)
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

object MockObserver {
  def observer[R](handler: ResultHandler[R])(system: ActorSystem, publisher: ActorRef): Future[R] = {
    implicit val timeout = Timeout(10.seconds)

    val h = new PromiseHandler(handler)
    val fut = h.future
    val actor = system.actorOf(Observer.props(h))
    println(Await.result(actor ? Observer.SubscribeTo(publisher), 3.seconds))
    fut
  }

  def probe(publisher: ActorRef)(implicit system: ActorSystem) = {
    val probe = TestProbe()

    probe.send(publisher, Publisher.Subscribe(None))
    probe.expectMsgType[Publisher.StreamInit]
    probe.reply(Publisher.StreamAck)
    println("Probe ready!")
    probe
  }
}
