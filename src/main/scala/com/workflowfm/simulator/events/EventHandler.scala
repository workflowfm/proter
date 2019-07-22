package com.worklflowfm.simulator.events

import akka.actor.ActorSystem
import java.text.SimpleDateFormat
import scala.concurrent.Promise

trait EventHandler extends (Event => Unit) {
  def onDone(e: Event)(f: Long=>Unit) = {
    e match {
      case EDone(t) => f(t)
      case _ => Unit
    }
  }
}

class PrintEventHandler extends EventHandler {   
  val formatter = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss.SSS")
  override def apply(e: Event) = {
    val time = formatter.format(System.currentTimeMillis())
    System.err.println(s"[$time] ${Event.asString(e)}")
  }
}

class ShutdownHandler(implicit system: ActorSystem) extends EventHandler {
  override def apply(e: Event): Unit = e match {
    case EDone(_) => system.terminate()
    case _ => Unit
  }
}

trait ResultHandler[R] extends EventHandler {   
  def result: R
}

class CounterHandler extends ResultHandler[Int] {
  var count = 0

  override def apply(e: Event) = count = count + 1
  override def result = count
}

class PromiseHandler[R](handler: ResultHandler[R]) extends ResultHandler[R] {
  protected val promise = Promise[R]()
  def future = promise.future
  
  override def apply(e: Event): Unit = try {
    handler(e)
    onDone(e) { _ => if (!promise.isCompleted) promise.success(result) }
  } catch {
    case x: Exception => if (!promise.isCompleted) promise.failure(x)
  }

  override def result = handler.result
}
