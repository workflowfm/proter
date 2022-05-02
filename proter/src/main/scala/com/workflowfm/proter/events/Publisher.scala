package com.workflowfm.proter.events

import java.util.UUID

/**
  * A publisher of a stream of simulation [[Event]]s.
  */
trait Publisher {

  /**
    * Checks if an event signifies the end of the stream.
    *
    * @param e
    *   The event to check.
    * @return
    *   `true` if it is a final event.
    */
  def isFinalEvent(e: Event): Boolean = false

  /**
    * Publishes an event into the stream.
    *
    * @param evt
    *   The event to publish.
    */
  def doPublish(evt: Event): Unit

  /**
    * Performs any cleaning up required when the stream is finished.
    */
  def stopStream(): Unit

  /**
    * Subscribes an [[EventHandler]] to the stream so they can receive events.
    *
    * @param subscriber
    *   The [[EventHandler]] to subscribe.
    */
  def subscribe(subscriber: EventHandler): Unit

  /**
    * Unsubscribes an [[EventHandler]] to the stream so they can stop receiving events.
    *
    * @param subscriber
    *   The [[EventHandler]] to unsubscribe.
    */
  def unsubscribe(subscriber: EventHandler): Unit

  /**
    * Wrapper method for [[doPublish]] that also stops the stream upon final events.
    *
    * @param evt
    */
  def publish(evt: Event): Unit = {
    doPublish(evt)
    if isFinalEvent(evt) then stopStream()
  }
}

/**
  * [[Publisher]] implementation using a `HashMap` of subscribers.
  */
trait HashMapPublisher extends Publisher {

  import scala.collection.mutable.HashMap

  /**
    * The `HashMap` of subscribers that receive events.
    */
  val subscribers: HashMap[UUID, EventHandler] = HashMap[UUID, EventHandler]()

  /**
    * @inheritdoc
    *
    * Sends the event to all subscribers in the `HashMap`.
    */
  override def doPublish(evt: Event): Unit = subscribers foreach (_._2.onEvent(evt))

  /**
    * @inheritdoc
    *
    * Adds the subscriber to the `HashMap` and calls its `onInit` method.
    */
  override def subscribe(subscriber: EventHandler): Unit = {
    subscribers += subscriber.id -> subscriber
    subscriber.onInit(this)
  }

  /**
    * @inheritdoc
    *
    * Notifies all subscribers that the stream is done and clears the `HashMap` to prevent any
    * further events being delivered.
    */
  override def stopStream(): Unit = {
    subscribers foreach (_._2.onDone(this))
    subscribers.clear()
  }

  /**
    * @inheritdoc
    *
    * Removes the subscriber from the `HashMap`.
    */
  override def unsubscribe(subscriber: EventHandler): Unit = subscribers -= subscriber.id

}
