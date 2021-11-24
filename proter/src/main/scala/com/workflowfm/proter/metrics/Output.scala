package com.workflowfm.proter.metrics

import java.text.SimpleDateFormat

import scala.collection.immutable.Queue

import org.apache.commons.lang3.time.DurationFormatUtils

/** Helper to write stuff to a file.
  * @todo Move somewhere else as a utility?
  */
trait FileOutput {
  import java.io._

  def writeToFile(filePath: String, output: String): Unit = try {
    val file = new File(filePath)
    val bw = new BufferedWriter(new FileWriter(file))
    bw.write(output)
    bw.close()
  } catch {
    case e: Exception => e.printStackTrace()
  }
}

/** Manipulates a [[SimMetricsAggregator]] to produce some output via side-effects.
  *
  * As a function, takes 2 arguments:
  * - a [[scala.Long]] representing the total virtual time elapsed
  * - the [[SimMetricsAggregator]] to act upon
  */
trait SimMetricsOutput extends ((Long, SimMetricsAggregator) => Unit) {
  /** Compose with another [[SimMetricsOutput]] in sequence. */
  def and(h: SimMetricsOutput): SimMetricsOutputs = SimMetricsOutputs(this, h)
}

/** Contains helpful formatting shortcut functions. */
object SimMetricsOutput {

  def formatOption[T](
      v: Option[T],
      nullValue: String,
      format: T => String = { x: T => x.toString }
  ): String = v.map(format).getOrElse(nullValue)
  def formatTime(format: String)(time: Long): String = new SimpleDateFormat(format).format(time)

  def formatTimeOption(time: Option[Long], format: String, nullValue: String): String =
    formatOption(time, nullValue, formatTime(format))

  def formatDuration(from: Long, to: Long, format: String): String =
    DurationFormatUtils.formatDuration(to - from, format)

  def formatDuration(from: Option[Long], to: Long, format: String, nullValue: String): String =
    from.map { f =>
      DurationFormatUtils.formatDuration(to - f, format).toString
    } getOrElse (nullValue)

  def formatDuration(
      from: Option[Long],
      to: Option[Long],
      format: String,
      nullValue: String
  ): String =
    from.map { f =>
      to.map { t =>
        DurationFormatUtils.formatDuration(t - f, format).toString
      } getOrElse (nullValue)
    } getOrElse (nullValue)
}

/** A [[SimMetricsOutput]] consisting of a [[scala.collection.immutable.Queue Queue]] of [[SimMetricsOutput]]s
  * to be run sequentially.
  */
case class SimMetricsOutputs(handlers: Queue[SimMetricsOutput]) extends SimMetricsOutput {

  /** Call all included [[SimMetricsOutput]]s. */
  override def apply(time: Long, aggregator: SimMetricsAggregator): Unit =
    handlers map (_.apply(time, aggregator))
  /** Add another [[SimMetricsOutput]] in sequence. */
  override def and(h: SimMetricsOutput): SimMetricsOutputs = SimMetricsOutputs(handlers :+ h)
}

object SimMetricsOutputs {

  /** Shorthand constructor for a [[SimMetricsOutputs]] from a list of [[SimMetricsOutput]]s. */
  def apply(handlers: SimMetricsOutput*): SimMetricsOutputs = SimMetricsOutputs(
    Queue[SimMetricsOutput]() ++ handlers
  )
}

/**
  * A [[SimMetricsOutput]] that does nothing.
  */
object SimNoOutput extends SimMetricsOutput {
  def apply(totalTicks: Long, aggregator: SimMetricsAggregator): Unit = Unit
}

/** Generates a string representation of the metrics using a generalized CSV format. */
trait SimMetricsStringOutput extends SimMetricsOutput {
  /** A string representing null values. */
  val nullValue = "NULL"

  /** The field names for [[TaskMetrics]].
    * @param separator a string (such as a space or comma) to separate the names
    */
  def taskHeader(separator: String): String =
    Seq(
      "ID",
      "Task",
      "Simulation",
      "Priority",
      "Created",
      "Start",
      "Delay",
      "Duration",
      "Cost",
      "Aborted",
      "Resources"
    )
      .mkString(separator)

  /** String representation of a [[TaskMetrics]] instance.
    *
    * @param separator a string (such as a space or comma) to separate the values
    * @param resSeparator a string (such as a space or comma) to separate the list of names of [[TaskResource]]s in [[TaskMetrics]]
    * @param m the [[TaskMetrics]] instance to be handled
    */
  def taskCSV(separator: String, resSeparator: String)(m: TaskMetrics): String = m match {
    case TaskMetrics(id, task, sim, priority, ct, st, dur, cost, res, aborted) =>
      Seq(
        id,
        task,
        sim,
        priority,
        ct,
        SimMetricsOutput.formatOption(st, nullValue),
        m.delay,
        dur,
        cost,
        aborted,
        res.mkString(resSeparator)
      ).mkString(separator)
  }

  /** The field names for [[SimulationMetrics]].
    * @param separator a string (such as a space or comma) to separate the names
    */
  def simHeader(separator: String): String =
    Seq("Name", "Start", "Duration", "Delay", "Tasks", "Cost", "Result").mkString(separator)

  /** String representation of a [[SimulationMetrics]] instance.
    *
    * @param separator a string (such as a space or comma) to separate the values
    * @param m the [[SimulationMetrics]] instance to be handled
    */
  def simCSV(separator: String)(m: SimulationMetrics): String = m match {
    case SimulationMetrics(name, st, dur, delay, ts, c, res) =>
      Seq(name, st, dur, delay, ts, c, res).mkString(separator)
  }

  /** The field names for [[ResourceMetrics]].
    * @param separator a string (such as a space or comma) to separate the names
    */
  def resHeader(separator: String): String =
    Seq("Name", "Busy", "Idle", "Tasks", "Cost").mkString(separator)

  /** String representation of a [[ResourceMetrics]] instance.
    *
    * @param separator a string (such as a space or comma) to separate the values
    * @param m the [[ResourceMetrics]] instance to be handled
    */
  def resCSV(separator: String)(m: ResourceMetrics): String = m match {
    case ResourceMetrics(name, _, _, b, i, ts, c) =>
      Seq(name, b, i, ts, c).mkString(separator)
  }

  /** Formats all [[TaskMetrics]] in a [[SimMetricsAggregator]] in a single string.
    *
    * @param aggregator the [[SimMetricsAggregator]] to retrieve the metrics to be formatted
    * @param separator a string (such as a space or comma) to separate values
    * @param lineSep a string (such as a new line) to separate tasks
    * @param resSeparator a string (such as a space or comma) to separate the list of names of [[TaskResource]]s in [[TaskMetrics]]
    */
  def tasks(
      aggregator: SimMetricsAggregator,
      separator: String,
      lineSep: String = "\n",
      resSeparator: String = ";"
  ): String =
    aggregator.taskMetrics.map(taskCSV(separator, resSeparator)).mkString(lineSep)

  /** Formats all [[SimulationMetrics]] in a [[SimMetricsAggregator]] in a single string.
    *
    * @param aggregator the [[SimMetricsAggregator]] to retrieve the metrics to be formatted
    * @param separator a string (such as a space or comma) to separate values
    * @param lineSep a string (such as a new line) to separate simulations
    */
  def simulations(
      aggregator: SimMetricsAggregator,
      separator: String,
      lineSep: String = "\n"
  ): String =
    aggregator.simulationMetrics.map(simCSV(separator)).mkString(lineSep)

  /** Formats all [[ResourceMetrics]] in a [[SimMetricsAggregator]] in a single string.
    *
    * @param aggregator the [[SimMetricsAggregator]] to retrieve the metrics to be formatted
    * @param separator a string (such as a space or comma) to separate values
    * @param lineSep a string (such as a new line) to separate resources
    */
  def resources(
      aggregator: SimMetricsAggregator,
      separator: String,
      lineSep: String = "\n"
  ): String =
    aggregator.resourceMetrics.map(resCSV(separator)).mkString(lineSep)
}

/** Prints all simulation metrics to standard output. */
class SimMetricsPrinter extends SimMetricsStringOutput {

  def apply(totalTicks: Long, aggregator: SimMetricsAggregator): Unit = {
    /** Separates the values. */
    val sep = "\t| "
    /** Separates metrics instances. */
    val lineSep = "\n"
    /** Default time format using `java.text.SimpleDateFormat`. */
    val timeFormat = "YYYY-MM-dd HH:mm:ss.SSS"
    /** Default duration format using [[org.apache.commons.lang3.time.DurationFormatUtils.formatDuration]]. */
    val durFormat = "HH:mm:ss.SSS"
    /** A string representing null time values. */
    val nullTime = "NONE"
    println(
      s"""
Tasks
-----
${taskHeader(sep)}
${tasks(aggregator, sep, lineSep)}

Simulations
-----------
${simHeader(sep)}
${simulations(aggregator, sep, lineSep)}

Resources
---------
${resHeader(sep)}
${resources(aggregator, sep, lineSep)}
---------

Started: ${SimMetricsOutput.formatTimeOption(aggregator.start, timeFormat, nullTime)}
Ended: ${SimMetricsOutput.formatTimeOption(aggregator.end, timeFormat, nullTime)}
Duration: ${SimMetricsOutput.formatDuration(aggregator.start, aggregator.end, durFormat, nullTime)}
"""
    )
  }
}

/** Outputs simulation metrics to files using a standard CSV format.
  * Generates 3 CSV files,
  *   1. One for tasks with a "-tasks.csv" suffix,
  *   1. One for simulations with a "-simulations.csv" suffix.
  *   1. One for resources with a "-resources.csv" suffix.
  *
  * @param path path to directory where the files will be placed
  * @param name file name prefix
  */
class SimCSVFileOutput(path: String, name: String) extends SimMetricsStringOutput with FileOutput {
  val separator = ","
  val lineSep = "\n"

  def apply(totalTicks: Long, aggregator: SimMetricsAggregator): Unit = {
    val taskFile = s"$path$name-tasks.csv"
    val simulationFile = s"$path$name-simulations.csv"
    val resourceFile = s"$path$name-resources.csv"
    writeToFile(taskFile, taskHeader(separator) + "\n" + tasks(aggregator, separator, lineSep))
    writeToFile(
      simulationFile,
      simHeader(separator) + "\n" + simulations(aggregator, separator, lineSep)
    )
    writeToFile(
      resourceFile,
      resHeader(separator) + "\n" + resources(aggregator, separator, lineSep)
    )
  }
}

/** Outputs simulation metrics to a file using the d3-timeline format.
  * Generates 1 file with a "-simdata.js" suffix.
  * This can then be combined with the resources at
  * [[https://github.com/PetrosPapapa/WorkflowFM-PEW/tree/master/resources/d3-timeline]]
  * to render the timeline in a browser.
  *
  * The timeline can display either virtual units of time or millisecond durations.
  * If we choose the latter, we can provide the size of the virtual unit of time in milliseconds
  * and all the durations will be scaled to that. For example, if our virtual unit of time is minutes,
  * we need to provide a `tick` value of 60000.
  *
  * @param path path to directory where the files will be placed
  * @param file file name prefix
  * @param tick the size of 1 unit of virtual time
  */
class SimD3Timeline(path: String, file: String, tick: Int = 1)
    extends SimMetricsOutput
    with FileOutput {

  override def apply(totalTicks: Long, aggregator: SimMetricsAggregator): Unit = {
    val result = build(aggregator, System.currentTimeMillis())
    //println(result)
    val dataFile = s"$path$file-simdata.js"
    writeToFile(dataFile, result)
  }

  /** Helps build the output with a static system time. */
  def build(aggregator: SimMetricsAggregator, now: Long): String = {
    val buf: StringBuilder = StringBuilder.newBuilder
    buf.append("var tasks = [\n")
    for (p <- (collection.immutable.SortedSet[String]() ++ aggregator.taskSet))
      buf.append(s"""\t"$p",\n""")
    buf.append("];\n\n")
    buf.append("var resourceData = [\n")
    for (m <- aggregator.resourceMetrics) buf.append(s"""${resourceEntry(m, aggregator)}\n""")
    buf.append("];\n\n")
    buf.append("var simulationData = [\n")
    for (m <- aggregator.simulationMetrics) buf.append(s"""${simulationEntry(m, aggregator)}\n""")
    buf.append("];\n")
    buf.toString
  }

  def simulationEntry(s: SimulationMetrics, agg: SimMetricsAggregator): String = {
    val times = agg.taskMetricsOf(s).flatMap(taskEntry).mkString(",\n")
    s"""{label: "${s.name}", times: [
$times
]},"""
  }

  def resourceEntry(res: ResourceMetrics, agg: SimMetricsAggregator): String = {
    val times = agg.taskMetricsOf(res).flatMap(taskEntry).mkString(",\n")
    s"""{label: "${res.name}", times: [
$times
]},"""
  }

  def taskEntry(m: TaskMetrics): Option[String] = m.started match {
    case None => None
    case Some(tstart) => {
      val start = tstart * tick
      val finish = (tstart + m.duration) * tick
      val delay = m.delay * tick
      Some(
        s"""\t{"label":"${m.fullName}", task: "${m.task}", "id":"${m.id}", "priority": ${m.priority}, "starting_time": $start, "ending_time": $finish, delay: $delay, cost: ${m.cost}}"""
      )
    }
  }
}
