package com.workflowfm.simulator.metrics

import akka.actor.Props
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.ActorSystem
import com.workflowfm.simulator.Coordinator
import com.workflowfm.simulator.events.{ EventHandler, Event, Observer }


class SimOutputHandler(output: SimMetricsOutput) extends EventHandler {
  val metricsHandler = new SimMetricsHandler()

  override def apply(evt: Event) = {
    metricsHandler(evt)
    onDone(evt) { t => output(t, metricsHandler.result) }
  }
}

class SimMetricsActor(m:SimMetricsOutput) extends Observer(new SimOutputHandler(m))

object SimMetricsActor {
  def props(m:SimMetricsOutput): Props = Props(new SimMetricsActor(m))
}
