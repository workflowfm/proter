package com.workflowfm.simulator.metrics
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import akka.actor.Props

import uk.ac.ed.inf.ppapapan.subakka.SubscriberActor

import com.workflowfm.simulator.Coordinator
import com.workflowfm.simulator.events.{ EDone, Event, EventHandler }

class SimOutputHandler(output: SimMetricsOutput) extends EventHandler {
  val metricsHandler = new SimMetricsHandler()

  override def onEvent(evt: Event): Unit = {
    metricsHandler.onEvent(evt)
    evt match {
      case EDone(_, t) => output(t, metricsHandler.result)
      case _ => Unit
    }
  }
}
