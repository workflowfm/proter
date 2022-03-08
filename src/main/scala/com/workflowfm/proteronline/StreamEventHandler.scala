package com.workflowfm.proteronline

import com.workflowfm.proter.events.Event
import com.workflowfm.proter.events.EventHandler
import com.workflowfm.proter.events.Publisher


class StreamEventHandler extends EventHandler {

    override def onEvent(event: Event): Unit = {
        println("StreamEventHandler Sees: " + event.toString())
        super.onEvent(event)
    }

    override def onDone(publisher: Publisher): Unit = {
        println("Stream Handler Sees End of Simulation")
        super.onDone(publisher)
    }
}