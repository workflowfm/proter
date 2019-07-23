package com.workflowfm.simulator.events

import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import akka.event.LoggingReceive

class Observer(handler: EventHandler) extends Actor with ActorLogging {
  def receive = {
    case Observer.SubscribeTo(publisher) => {
      publisher ! Publisher.Subscribe(Some(sender))
    }
    case e: Event => {
      log.debug("Observing Event: {}",Event.asString(e))
      handler(e)
      sender() ! Publisher.StreamAck
    }

    case Publisher.StreamInit(ack) => {
      log.debug("Stream initialized.")
      sender() ! Publisher.StreamAck
      ack.forward(Publisher.StreamInit(ack))
    }
    case Publisher.StreamDone => {
      log.debug("Stream completed.")
      context.stop(self)
    }
    case Publisher.StreamFail(ex) => {
      log.error(ex, "Stream failed!")
//      context.stop(self)
    }
  }
}

object Observer {
  case class SubscribeTo(publisher: ActorRef)

  def props(f: EventHandler): Props = { Props(new Observer(f)) }
}

