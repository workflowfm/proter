package com.worklflowfm.simulator.events

import akka.actor.{ Actor, ActorRef, Props }

class Observer(handler: EventHandler) extends Actor {
  def receive = {
    case Observer.SubscribeTo(publisher) => publisher ! Publisher.Subscribe
    case e: Event => handler(e)
    case Publisher.Done => context.stop(self)
    case Publisher.Ack => Unit
  }
}

object Observer {
  case class SubscribeTo(publisher: ActorRef)

  def props(f: EventHandler): Props = { Props(new Observer(f)) }
}

