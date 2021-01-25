package com.workflowfm.proter.events

import java.util.UUID

trait Publisher {

  def isFinalEvent(e: Event): Boolean = false

  def doPublish(evt: Event): Unit

  def stopStream(): Unit

  def subscribe(subscriber: EventHandler): Unit

  def unsubscribe(subscriber: EventHandler): Unit

  def publish(evt: Event): Unit = {
    doPublish(evt)
    if (isFinalEvent(evt)) stopStream()
  }
}

trait SubscriptionSwitch {
  def stop(): Unit
}

trait HashMapPublisher extends Publisher {

  import scala.collection.mutable.HashMap

  val subscribers: HashMap[UUID, EventHandler] = HashMap[UUID, EventHandler]()

  override def doPublish(evt: Event): Unit = subscribers foreach (_._2.onEvent(evt))

  override def subscribe(subscriber: EventHandler): Unit = {
    subscribers += subscriber.id -> subscriber
    subscriber.onInit(this)
  }

  override def stopStream(): Unit = {
    subscribers foreach (_._2.onDone(this))
    subscribers.clear()
  }

  override def unsubscribe(subscriber: EventHandler): Unit = subscribers -= subscriber.id

}
