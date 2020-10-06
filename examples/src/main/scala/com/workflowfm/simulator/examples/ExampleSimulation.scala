package com.workflowfm.simulator.examples

import akka.actor.{ActorSystem, ActorRef}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent._
import scala.concurrent.duration._
import com.workflowfm.simulator._
import com.workflowfm.simulator.metrics._
import com.workflowfm.simulator.events.{ ShutdownHandler }
import com.workflowfm.simulator.flows._
import uk.ac.ed.inf.ppapapan.subakka.Subscriber
import java.io.File
import scala.collection.mutable

class ExampleSimulation(
  outputPath:String,
  outputName:String,
  resources: Seq[TaskResource]
) (implicit val system: ActorSystem, implicit val context: ExecutionContext) {

  val coordinator = system.actorOf(Coordinator.props(new LookaheadScheduler()))

  val handler = SimMetricsOutputs(
    new SimMetricsPrinter(),
    new SimCSVFileOutput(outputPath + File.separator,outputName),
    new SimD3Timeline(outputPath + File.separator,outputName) //60*1000
  )

  val metrics = new SimOutputHandler(handler)

  /**
    * Loads the specified simulations
    * */
  def load(shutdownActor: ActorRef, simulations:Seq[(Long,ActorRef)]) {
    implicit val timeout = Timeout(2.seconds)

    Await.result(metrics.subAndForgetTo(coordinator, Some(s"$outputName-Metrics")), 3.seconds)
    Await.result(shutdownActor ? Subscriber.SubAndForgetTo(coordinator), 3.seconds)
    coordinator ! Coordinator.AddResources(resources)
    simulations map { case (start,sim) =>
      coordinator ! Coordinator.AddSim(start, sim)
    }
  }

  def start() = {
    coordinator ! Coordinator.Start
  }
}