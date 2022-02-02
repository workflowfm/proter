package com.workflowfm.proteronline

import com.workflowfm.proter.events.EventHandler
import com.workflowfm.proter.events.Event
import cats.effect.IO


class StreamEventHandler extends EventHandler {

    var stream: fs2.Stream[IO, String] = fs2.Stream.empty

    override def onEvent(event: Event): Unit = {
        println("StreamEventHandler Sees: " + event.toString())
        super.onEvent(event)
        this.stream = this.stream ++ fs2.Stream.emit(event.toString())
    }
}