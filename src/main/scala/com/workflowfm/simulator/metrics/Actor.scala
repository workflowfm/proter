package com.workflowfm.simulator.metrics

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import com.workflowfm.simulator.Coordinator
import com.workflowfm.simulator.events.{ EDone, Event, EventHandler }
import uk.ac.ed.inf.ppapapan.subakka.SubscriberActor


class SimOutputHandler(output: SimMetricsOutput) extends EventHandler {
  val metricsHandler = new SimMetricsHandler()

  override def onEvent(evt: Event) = {
    metricsHandler.onEvent(evt)
    evt match {
      case EDone(_,t) => output(t, metricsHandler.result)
      case _ => Unit
    }
  }
}
