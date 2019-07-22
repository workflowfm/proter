package com.worklflowfm.simulator.events

import akka.NotUsed
import akka.actor.{ Actor, ActorRef, ActorSystem }
import akka.event.LoggingReceive
import akka.stream.{ ActorMaterializer, OverflowStrategy }
import akka.stream.scaladsl.{ BroadcastHub, Keep, Sink, Source, SourceQueueWithComplete }

trait Publisher {
  implicit val system: ActorSystem
  implicit val materializer = ActorMaterializer()

  private val sourceQueue = Source.queue[Event](Publisher.bufferSize, Publisher.overflowStrategy)

  private val (
    queue: SourceQueueWithComplete[Event],
    source: Source[Event, NotUsed]
  ) = {
    val (q,s) = sourceQueue.toMat(BroadcastHub.sink(bufferSize = 256))(Keep.both).run()
    s.runWith(Sink.ignore)
    (q,s)
  }

  def publish(evt: Event) = {
    queue.offer(evt)
  }

  def subscribe(actor: ActorRef): Unit = source.runWith(Sink.actorRef(actor, Publisher.Done))
}


object Publisher {
  final val bufferSize = 5
  final val overflowStrategy = OverflowStrategy.backpressure

  case object Subscribe
  case object Ack
  case object Done
}


trait PublisherActor extends Publisher with Actor {

  def publisherBehaviour: Receive = {
    case Publisher.Subscribe => {
      subscribe(sender)
      sender ! Publisher.Ack
    }
  }

  def receiveBehaviour: Receive = ???

  override def receive = { publisherBehaviour orElse receiveBehaviour }
}
