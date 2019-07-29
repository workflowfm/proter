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

  def publish(evt: Event) = {
    queue.offer(evt)
    evt match {
      case EDone(_,_) => queue.complete()
      case _ => Unit
    }
  }

  def subscribe(actor: ActorRef, ack: ActorRef): Unit = source.runWith(
    Sink.actorRefWithAck(
      actor,
      onInitMessage = Publisher.StreamInit(ack),
      ackMessage = Publisher.StreamAck,
      onCompleteMessage = Publisher.StreamDone,
      onFailureMessage = onErrorMessage))

  // Subscribing handlers like this will not pass on init, done, and fail messages
  //def subHandler(handler: EventHandler): Unit = source.runWith(Sink.foreach(handler.onEvent))

  def onErrorMessage(ex: Throwable) = Publisher.StreamFail(ex)
}


object Publisher {
  final val bufferSize = 5
  final val overflowStrategy = OverflowStrategy.backpressure

  case class Subscribe(ack: Option[ActorRef])
  case class SubHandler(handler: EventHandler, ack: Option[ActorRef])

  case object StreamAck
  case class StreamInit(ack: ActorRef)
  case object StreamDone
  case class StreamFail(ex: Throwable)
}


trait PublisherActor extends Publisher with Actor with ActorLogging {

  def publisherBehaviour: Receive = {
    case Publisher.Subscribe(ack) => {
      subscribe(sender,ack.getOrElse(sender()))
    }
/*    case Publisher.SubHandler(handler,ack) => {
      subHandler(handler)
      val a = ack.getOrElse(sender())
      a ! Publisher.StreamInit(a)
    }*/
    case Publisher.StreamAck => Unit

  }

  def receiveBehaviour: Receive = ???

  override def receive = LoggingReceive { publisherBehaviour orElse receiveBehaviour }

  override def publish(evt: Event) = {
    log.debug("Publishing Event: {}", Event.asString(evt))
    super.publish(evt)
  }
}
