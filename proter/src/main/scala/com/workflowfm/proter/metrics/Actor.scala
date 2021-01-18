package com.workflowfm.proter.metrics

import com.workflowfm.proter.events.{ EDone, Event, EventHandler }

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
