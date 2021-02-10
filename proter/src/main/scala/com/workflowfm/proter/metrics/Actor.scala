package com.workflowfm.proter.metrics

import com.workflowfm.proter.events._

class SimOutputHandler(output: SimMetricsOutput) extends EventHandler {
  val metricsHandler: ResultHandler[SimMetricsAggregator] = new SimMetricsHandler()

  override def onEvent(evt: Event): Unit = {
    metricsHandler.onEvent(evt)
    evt match {
      case EDone(_, t) => output(t, metricsHandler.result)
      case _ => Unit
    }
  }
}

// class SimMultiOutputHandler(output: SimMetricsOutput, number: Int, totalTime: Long) extends SimOutputHandler(output) {
//   override val metricsHandler = new MultiAggregateMetricsHandler(number, totalTime, output)
// }

class SimSubrunHandler(output: SimMetricsOutput, number: Int, totalTime: Long) extends EventHandler {
  val metrics: List[SimMetricsHandler] = List.fill(number)(new SimMetricsHandler())

  val master = new SimMetricsHandler()

  override def onEvent(evt: Event): Unit = {
    master.onEvent(evt)
    evt match {
      case EStart(s) => all(evt)
      case EDone(s, t) => {
        all(evt)
        calcAverages()
        output(t, master.result)
      }
      case EResourceAdd(s, t, n, c) => all(evt)
      case ESimEnd(s, t, n, r) => all(evt)
      case _ => metrics(selectAggregator(evt.time).toInt).onEvent(evt)

    }
  }

  protected def all(evt: Event) = metrics map (_.onEvent(evt))

  protected def selectAggregator(currentTime: Long): Long = {
    val timePerAggregator: Double = totalTime.toDouble / number.toDouble
    val out = (currentTime.toDouble / timePerAggregator).toLong // floor, e.g. 12.3 is rounded to 12.
    if (out < number) out else (number - 1)
  }

  protected def calcAverages() = {
    var averages: Seq[(MetaTaskMetrics, MetaSimMetrics, MetaResourceMetrics)] = Seq()
    val i = 0
    for (i <- 0 to number-1) {
      println("calcing")
      val result = metrics(i).result
      averages = averages :+ ( ( MetaTaskMetrics(result.taskMetrics), MetaSimMetrics(result.simulationMetrics), MetaResourceMetrics(result.resourceMetrics) ) )
    }
  }
  
}