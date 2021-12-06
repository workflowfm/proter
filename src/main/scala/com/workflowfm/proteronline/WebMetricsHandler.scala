package com.workflowfm.proteronline

import com.workflowfm.proter.metrics.SimMetricsHandler
import com.workflowfm.proter.events._
import scala.concurrent.Promise

//REPLACED WITH OTHER CODE, IGNROE THIS


class WebMetricsHandler(placeHolderSocket: String, prom: Promise[String]) extends SimMetricsHandler {
    override def onEvent(evt: Event): Unit = {
        super.onEvent(evt)
        //Below line placeholder for sending data across the socket or calling a function to send data to the socket
        println("Event Sent to Socket: " + placeHolderSocket)

        if (evt.isInstanceOf[EDone]) {
            prom.success(gatherFinalResults())
        }

    }

    //Method simply forms the metrics into the format I want
    def gatherFinalResults(): String = {
        val finalMetrics = super.metrics
        //Build the return string from the metrics
        "Example Results"
    }

}



object exampleUsage {
    val thePromise = Promise[String]()
    val metricsHandler = new WebMetricsHandler("A Socket", thePromise)
    //coordinator.subscribe(metricsHandler)
    //coordinator.run()
    //return thePromise.future
}