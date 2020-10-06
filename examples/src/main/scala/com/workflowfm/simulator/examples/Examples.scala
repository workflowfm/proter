package com.workflowfm.simulator.examples

import akka.actor.ActorSystem
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

object Example {
    //toggle for debug
    val DEBUG = false

    def main(args: Array[String]): Unit = {
	    implicit val context: ExecutionContext = ExecutionContext.global
        implicit val system: ActorSystem = ActorSystem("ExampleTests")

        val shutdownActor = Subscriber.actor(new ShutdownHandler())

        def r(name: String, cost: Int = 0): TaskResource = new TaskResource(name,cost)
        def t(name: String, simulation: String, resources: Seq[String], duration: ValueGenerator[Long] = ConstantGenerator(3L), cost: ValueGenerator[Long] = ConstantGenerator(0L), priority: Task.Priority = Task.Medium): Flow = 
            FlowTask(TaskGenerator(name,simulation,duration,cost).withResources(resources).withPriority(priority))


        { 
            val flow = t("t1","S01",Seq("r1")) + t("t2","S01",Seq("r2"))
            val resources = Seq(
                r("r1"),
                r("r2"),
                r("r3")
            )
            val simulator = new ExampleSimulation("output", "Test01", resources)

            val sims = Seq (
                (0L, system.actorOf(FlowSimulationActor.props("S01",simulator.coordinator,flow))),
            )
            
            simulator.load(shutdownActor,sims)
            simulator.start()
        }

        { 
            val flow = t("t1","S01",Seq("r1")) > t("t2","S01",Seq("r2"))
            val resources = Seq(
                r("r1"),
                r("r2"),
                r("r3")
            )
            val simulator = new ExampleSimulation("output", "Test02", resources)

            val sims = Seq (
                (0L, system.actorOf(FlowSimulationActor.props("S01",simulator.coordinator,flow))),
            )
            
            simulator.load(shutdownActor,sims)
            simulator.start()
        }

        { 
            val flow = ( t("t1","S01",Seq("r1"),priority = Task.High) > t("t2","S01",Seq("r2"),priority=Task.High) ) + t("t3","S01",Seq("r2"),duration = ConstantGenerator(5L))
            val resources = Seq(
                r("r1"),
                r("r2"),
                r("r3")
            )
            val simulator = new ExampleSimulation("output", "Test03", resources)

            val sims = Seq (
                (0L, system.actorOf(FlowSimulationActor.props("S01",simulator.coordinator,flow))),
            )
            
            simulator.load(shutdownActor,sims)
            simulator.start()
        }

        { 
            val flow = ( t("t1","S01",Seq("r1"),priority = Task.High) > t("t2","S01",Seq("r2"),priority=Task.High) ) + t("t3","S01",Seq("r2"),duration = ConstantGenerator(5L))
            val resources = Seq(
                r("r1"),
                r("r2"),
                r("r3")
            )
            val simulator = new ExampleSimulation("output", "Test04", resources)

            val sims = Seq (
                (0L, system.actorOf(FlowLookaheadActor.props("S01",simulator.coordinator,flow))),
            )
            
            simulator.load(shutdownActor,sims)
            simulator.start()
        }
    }
}