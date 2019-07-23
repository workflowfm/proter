package com.workflowfm.simulator.events

import akka.NotUsed
import akka.actor.{ Actor, ActorLogging, ActorRef, ActorSystem }
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

  def publish(evt: Event) = queue.offer(evt)

  def subscribe(actor: ActorRef): Unit = source.runWith(Sink.actorRef(actor, Publisher.Done))
  def subHandler(handler: EventHandler): Unit = source.runWith(Sink.foreach(handler))

}


object Publisher {
  final val bufferSize = 5
  final val overflowStrategy = OverflowStrategy.backpressure

  case class Subscribe(ack: Option[ActorRef])
  case class SubHandler(handler: EventHandler, ack: Option[ActorRef])
  case object Ack
  case object Done
}


trait PublisherActor extends Publisher with Actor with ActorLogging {

  def publisherBehaviour: Receive = {
    case Publisher.Subscribe(ack) => {
      subscribe(sender)
      ack.getOrElse(sender()) ! Publisher.Ack
    }
    case Publisher.SubHandler(handler,ack) => {
      subHandler(handler)
      ack.getOrElse(sender()) ! Publisher.Ack
    }

  }

  def receiveBehaviour: Receive = ???

  override def receive = { publisherBehaviour orElse receiveBehaviour }

  override def publish(evt: Event) = {
    log.debug("Publishing Event: {}", Event.asString(evt))
    super.publish(evt)
  }
}
