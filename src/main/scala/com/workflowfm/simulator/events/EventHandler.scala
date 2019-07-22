package com.worklflowfm.simulator.events

import akka.actor.ActorSystem
import java.text.SimpleDateFormat
import scala.concurrent.Promise
import scala.util.{ Success, Try }

trait EventHandler extends (Event => Unit)

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

trait PromiseHandler[R] extends EventHandler {   

  protected val promise = Promise[R]()
  def future = promise.future
  
  override def apply(e: Event) = try {
    handle(e) match {
      case None => Unit
      case Some(r) => if (!promise.isCompleted) promise.success(r)
    }
  } catch {
    case x: Exception => if (!promise.isCompleted) promise.failure(x)
  }

  def handle(event: Event) : Option[R] = ???
}

class CounterHandler extends PromiseHandler[Int] {
  var count = 0

  override def handle(e: Event) = e match {
    case EDone(_) => Some(count+1)
    case _ => count = count + 1; None
  }
}
