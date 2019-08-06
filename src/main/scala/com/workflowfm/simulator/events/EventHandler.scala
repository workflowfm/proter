package com.workflowfm.simulator.events

import akka.actor.{ ActorRef, ActorSystem }
import java.text.SimpleDateFormat
import scala.collection.mutable.HashSet
import scala.concurrent.Promise
import uk.ac.ed.inf.ppapapan.subakka.Subscriber

trait EventHandler extends Subscriber[Event]

trait PoolEventHandler extends EventHandler {
  val coordinators: HashSet[ActorRef] = HashSet[ActorRef]()

  override def onInit(a: ActorRef) = {
    println(s"Pool handler adding coordinator: $a")
    coordinators += a
  }
  override def onDone(a: ActorRef, s: ActorRef) = {
    println(s"Pool handler done with coordinator: $a")
    coordinators -= a
  }
}


class PrintEventHandler extends EventHandler {   
  val formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS")
  override def onEvent(e: Event) = {
    val time = formatter.format(System.currentTimeMillis())
    System.err.println(s"[$time] ${Event.asString(e)}")
  }
}

trait ResultHandler[R] extends EventHandler {   
  def result: R
}

class CounterHandler extends ResultHandler[Int] {
  var count = 0

  override def onInit(a: ActorRef) = count = 0
  override def onEvent(e: Event) = count = count + 1
  override def result = count
}

class PromiseHandler[R](handler: ResultHandler[R]) extends ResultHandler[R] {
  protected val promise = Promise[R]()
  def future = promise.future

  override def onInit(a: ActorRef): Unit = handler.onInit(a)
  override def onEvent(e: Event): Unit = handler.onEvent(e)

  override def onDone(a: ActorRef, s: ActorRef): Unit = {
    handler.onDone(a,s)
    if (!promise.isCompleted) promise.success(result)
  }

  override def onFail(ex: Throwable, a: ActorRef, s: ActorRef) = {
    handler.onFail(ex, a, s)
    if (!promise.isCompleted) promise.failure(ex)
  }

  override def result = handler.result
}
object PromiseHandler {
  def of[R](handler: ResultHandler[R]): PromiseHandler[R] = new PromiseHandler[R](handler)
}


class ShutdownHandler(implicit system: ActorSystem) extends PoolEventHandler {
  override def onDone(a: ActorRef, s: ActorRef): Unit = {
    super.onDone(a,s)
    if (coordinators.isEmpty) {
      println("********************************************* SHUTTING DOWN!!! ***********************************************")
      Thread.sleep(1000)
      system.terminate()
    }
  }
}