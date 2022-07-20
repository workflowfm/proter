package com.workflowfm.proter.metrics

import cats.Applicative
import cats.implicits.*
import cats.effect.{ Sync, Resource }
import cats.effect.std.Console
import fs2.Pipe
import java.text.SimpleDateFormat

import scala.collection.immutable.Queue

import org.apache.commons.lang3.time.DurationFormatUtils

/**
  * Helper to write stuff to a file.
  * @todo
  *   Move somewhere else as a utility?
  */

trait FileOutput[F[_] : Sync] {
  import java.io.*

  def outputStream(f: File): Resource[F, BufferedWriter] =
    Resource.fromAutoCloseable(Sync[F].blocking(new BufferedWriter(new FileWriter(f))))

  def writeToFile(filePath: String, output: String): F[Unit] = {
    val file = new File(filePath)
    outputStream(file).use { out => 
      Sync[F].blocking(out.write(output)) 
    }
  }
}

/**
  * Manipulates a [[SimMetricsAggregator]] to produce some output via side-effects.
  *
  * As a function, takes 2 arguments:
  *   - a [[scala.Long]] representing the total virtual time elapsed
  *   - the [[SimMetricsAggregator]] to act upon
  */
trait MetricsOutput[F[_]] extends (Metrics => F[Unit]) {
  def pipe: Pipe[F, Metrics, Unit] = _.evalMap(this)
}

/** Contains helpful formatting shortcut functions. */
object MetricsOutput {

  def formatOption[T](
      v: Option[T],
      nullValue: String,
      format: T => String = { (x: T) => x.toString }
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

  def empty[F[_] : Applicative]: MetricsOutput[F] = new MetricsOutput[F] {
    override def apply(metrics: Metrics): F[Unit] = Applicative[F].pure(()) 
  }
}


/** Generates a string representation of the metrics using a generalized CSV format. */
trait MetricsStringOutput[F[_]] extends MetricsOutput[F] {
  /** A string representing null values. */
  val nullValue = "NULL"

  /**
    * The field names for [[TaskMetrics]].
    * @param separator
    *   a string (such as a space or comma) to separate the names
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

  /**
    * String representation of a [[TaskMetrics]] instance.
    *
    * @param separator
    *   a string (such as a space or comma) to separate the values
    * @param resSeparator
    *   a string (such as a space or comma) to separate the list of names of [[TaskResource]]s in
    *   [[TaskMetrics]]
    * @param m
    *   the [[TaskMetrics]] instance to be handled
    */
  def taskCSV(separator: String, resSeparator: String)(m: TaskMetrics): String = m match {
    case TaskMetrics(id, task, sim, priority, ct, st, dur, cost, res, aborted) =>
      Seq(
        id,
        task,
        sim,
        priority,
        ct,
        MetricsOutput.formatOption(st, nullValue),
        m.delay,
        dur,
        cost,
        aborted,
        res.mkString(resSeparator)
      ).mkString(separator)
  }

  /**
    * The field names for [[SimulationMetrics]].
    * @param separator
    *   a string (such as a space or comma) to separate the names
    */
  def caseHeader(separator: String): String =
    Seq("Name", "Start", "Duration", "Delay", "Tasks", "Cost", "Result").mkString(separator)

  /**
    * String representation of a [[SimulationMetrics]] instance.
    *
    * @param separator
    *   a string (such as a space or comma) to separate the values
    * @param m
    *   the [[SimulationMetrics]] instance to be handled
    */
  def caseCSV(separator: String)(m: CaseMetrics): String = m match {
    case CaseMetrics(name, st, dur, delay, ts, c, res) =>
      Seq(name, st, dur, delay, ts, c, res).mkString(separator)
  }

  /**
    * The field names for [[ResourceMetrics]].
    * @param separator
    *   a string (such as a space or comma) to separate the names
    */
  def resHeader(separator: String): String =
    Seq("Name", "Busy", "Idle", "Tasks", "Cost").mkString(separator)

  /**
    * String representation of a [[ResourceMetrics]] instance.
    *
    * @param separator
    *   a string (such as a space or comma) to separate the values
    * @param m
    *   the [[ResourceMetrics]] instance to be handled
    */
  def resCSV(separator: String)(m: ResourceMetrics): String = m match {
    case ResourceMetrics(name, _, _, b, i, ts, c) =>
      Seq(name, b, i, ts, c).mkString(separator)
  }

  /**
    * Formats all [[TaskMetrics]] in a [[SimMetricsAggregator]] in a single string.
    *
    * @param aggregator
    *   the [[SimMetricsAggregator]] to retrieve the metrics to be formatted
    * @param separator
    *   a string (such as a space or comma) to separate values
    * @param lineSep
    *   a string (such as a new line) to separate tasks
    * @param resSeparator
    *   a string (such as a space or comma) to separate the list of names of [[TaskResource]]s in
    *   [[TaskMetrics]]
    */
  def tasks(
      metrics: Metrics,
      separator: String,
      lineSep: String = "\n",
      resSeparator: String = ";"
  ): String =
    metrics.taskMetrics.map(taskCSV(separator, resSeparator)).mkString(lineSep)

  /**
    * Formats all [[SimulationMetrics]] in a [[SimMetricsAggregator]] in a single string.
    *
    * @param aggregator
    *   the [[SimMetricsAggregator]] to retrieve the metrics to be formatted
    * @param separator
    *   a string (such as a space or comma) to separate values
    * @param lineSep
    *   a string (such as a new line) to separate simulations
    */
  def cases(
      metrics: Metrics,
      separator: String,
      lineSep: String = "\n"
  ): String =
    metrics.caseMetrics.map(caseCSV(separator)).mkString(lineSep)

  /**
    * Formats all [[ResourceMetrics]] in a [[SimMetricsAggregator]] in a single string.
    *
    * @param aggregator
    *   the [[SimMetricsAggregator]] to retrieve the metrics to be formatted
    * @param separator
    *   a string (such as a space or comma) to separate values
    * @param lineSep
    *   a string (such as a new line) to separate resources
    */
  def resources(
      metrics: Metrics,
      separator: String,
      lineSep: String = "\n"
  ): String =
    metrics.resourceMetrics.map(resCSV(separator)).mkString(lineSep)
}

/** Prints all simulation metrics to standard output. */
class MetricsPrinter[F[_] : Console] extends MetricsStringOutput[F] {

  def apply(metrics: Metrics): F[Unit] = {
    /** Separates the values. */
    val sep = "\t| "
    /** Separates metrics instances. */
    val lineSep = "\n"
    /** Default time format using `java.text.SimpleDateFormat`. */
    val timeFormat = "YYYY-MM-dd HH:mm:ss.SSS"
    /**
      * Default duration format using
      * [[org.apache.commons.lang3.time.DurationFormatUtils.formatDuration]].
      */
    val durFormat = "HH:mm:ss.SSS"
    /** A string representing null time values. */
    val nullTime = "NONE"
    Console[F].println(
      s"""
Tasks
-----
${taskHeader(sep)}
${tasks(metrics, sep, lineSep)}

Simulations
-----------
${caseHeader(sep)}
${cases(metrics, sep, lineSep)}

Resources
---------
${resHeader(sep)}
${resources(metrics, sep, lineSep)}
---------

Started: ${MetricsOutput.formatTimeOption(metrics.sStart, timeFormat, nullTime)}
Ended: ${MetricsOutput.formatTimeOption(metrics.sEnd, timeFormat, nullTime)}
Duration: ${MetricsOutput.formatDuration(metrics.sStart, metrics.sEnd, durFormat, nullTime)}
"""
    )
  }
}

/**
  * Outputs simulation metrics to files using a standard CSV format. Generates 3 CSV files,
  *   1. One for tasks with a "-tasks.csv" suffix,
  *   1. One for simulations with a "-simulations.csv" suffix.
  *   1. One for resources with a "-resources.csv" suffix.
  *
  * @param path
  *   path to directory where the files will be placed
  * @param name
  *   file name prefix
  */
class CSVFile[F[_] : Sync](path: String, name: String) 
  extends MetricsStringOutput[F] 
  with FileOutput[F] {
  
  val separator = ","
  val lineSep = "\n"

  def apply(metrics: Metrics): F[Unit] = {
    val taskFile = s"$path$name-tasks.csv"
    val caseFile = s"$path$name-simulations.csv"
    val resourceFile = s"$path$name-resources.csv"
    
    val t = writeToFile(taskFile, taskHeader(separator) + "\n" + tasks(metrics, separator, lineSep))
    val c = writeToFile(
      caseFile,
      caseHeader(separator) + "\n" + cases(metrics, separator, lineSep)
    )
    val r = writeToFile(
      resourceFile,
      resHeader(separator) + "\n" + resources(metrics, separator, lineSep)
    )
    Seq(t, c, r).sequence.void
  }
}

/**
  * Outputs simulation metrics to a file using the d3-timeline format. Generates 1 file with a
  * "-simdata.js" suffix. This can then be combined with the resources at
  * [[https://github.com/PetrosPapapa/WorkflowFM-PEW/tree/master/resources/d3-timeline]] to render
  * the timeline in a browser.
  *
  * The timeline can display either virtual units of time or millisecond durations. If we choose the
  * latter, we can provide the size of the virtual unit of time in milliseconds and all the
  * durations will be scaled to that. For example, if our virtual unit of time is minutes, we need
  * to provide a `tick` value of 60000.
  *
  * @param path
  *   path to directory where the files will be placed
  * @param file
  *   file name prefix
  * @param tick
  *   the size of 1 unit of virtual time
  */
class D3Timeline[F[_] : Sync](path: String, file: String, tick: Int = 1)
  extends MetricsOutput[F]
  with FileOutput[F] {

  override def apply(metrics: Metrics): F[Unit] = {
    val result = build(metrics, System.currentTimeMillis())
    // println(result)
    val dataFile = s"$path$file-simdata.js"
    writeToFile(dataFile, result)
  }

  /** Helps build the output with a static system time. */
  def build(metrics: Metrics, now: Long): String = {
    val buf: StringBuilder = new StringBuilder()
    buf.append("var tasks = [\n")
    for p <- (collection.immutable.SortedSet[String]() ++ metrics.taskSet) do
      buf.append(s"""\t"$p",\n""")
    buf.append("];\n\n")
    buf.append("var resourceData = [\n")
    for m <- metrics.resourceMetrics do buf.append(s"""${resourceEntry(m, metrics)}\n""")
    buf.append("];\n\n")
    buf.append("var simulationData = [\n")
    for m <- metrics.caseMetrics do buf.append(s"""${simulationEntry(m, metrics)}\n""")
    buf.append("];\n")
    buf.toString
  }

  def simulationEntry(s: CaseMetrics, agg: Metrics): String = {
    val times = agg.taskMetricsOf(s).flatMap(taskEntry).mkString(",\n")
    s"""{label: "${s.name}", times: [
$times
]},"""
  }

  def resourceEntry(res: ResourceMetrics, agg: Metrics): String = {
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
