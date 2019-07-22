package com.worklflowfm.simulator.events

import akka.actor.ActorSystem
import java.text.SimpleDateFormat

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
    case EDone(t) => system.terminate()
    case _ => Unit
  }
}

