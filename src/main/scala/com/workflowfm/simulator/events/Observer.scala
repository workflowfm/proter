package com.workflowfm.simulator.events

import akka.actor.{ Actor, ActorLogging, ActorRef, Props }
import akka.event.LoggingReceive

class Observer(handler: EventHandler) extends Actor with ActorLogging {
  def receive =  {
    case Observer.SubscribeTo(publisher) => {
      publisher ! Publisher.Subscribe(Some(sender))
    }
    case e: Event => {
      log.debug("Observing Event: {}",Event.asString(e))
      handler(e)
    }
    case Publisher.Done => context.stop(self)
    //case Publisher.Ack => Unit
  }
}

object Observer {
  case class SubscribeTo(publisher: ActorRef)

  def props(f: EventHandler): Props = { Props(new Observer(f)) }
}

