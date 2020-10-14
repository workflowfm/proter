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
            /* Example 1
                A basic AND of two tasks (t1 and t2 happen in parallel)
            */
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
            /* Example 2
                A basic THEN of two tasks (t1 and t2 happen in sequence)
            */
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
            /* Example 3
                A basic flow ( (t1 > t2) + t3)
                t2 must happen after t1, but t3 can run in parallel to t1.
                Illustrates a shortcoming of non-lookahead simulation; t2 is higher prioirty than t3, but t3 starts first anyway.
                Compare this to example 4, which uses the same flow but with lookahead.
            */
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
            /* Example 4
                A basic flow ( (t1 > t2) + t3)
                Same flow as example 3
                Shows how, by using lookahead, higher priority tasks are executed sooner. Here t2 will run before t3 thanks to lookahead,
                as opposed to what happens in example 3, where t2 runs first causing t3 to be delayed.
            */
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

        { 
            /* Example 5
                The flow ( t1 > (t2>t3) + (t4>t5) )
                The two "sub assemblies" run in parallel. Since we are not using lookahead, t5 prevents
                t3 from starting, which is bad since t3 has higher priority. This happens because (at the time)
                t5 is not competing for resources, since t3 cannot start yet.
                This should be compared with example 6.
            */
            val flow = (t("t1","S01",Seq("r2"),priority = Task.High) > (
                        t("t2","S01",Seq("r1"),priority = Task.High) >
                        t("t3","S01",Seq("r2"),priority = Task.High) ) + (
                        t("t4","S01",Seq("r3"),duration = ConstantGenerator(2L)) >
                        t("t5","S01",Seq("r2")) ) )
            val resources = Seq(
                r("r1"),
                r("r2"),
                r("r3")
            )
            val simulator = new ExampleSimulation("output", "Test05", resources)

            val sims = Seq (
                (0L, system.actorOf(FlowSimulationActor.props("S01",simulator.coordinator,flow))),
            )
            
            simulator.load(shutdownActor,sims)
            simulator.start()
        }

        { 
            /* Example 6
                The flow ( t1 > (t2>t3) + (t4>t5) )
                The same flow as example 5
                Unlike example 5, task 3 starts before task 5. This illustrates that our lookahead method
                also considers currently-running tasks and the future tasks of those currently-running tasks
                in scheduling, and the result is that task 5 is intentionally delayed.
            */
            val flow = (t("t1","S01",Seq("r2"),priority = Task.High) > (
                        t("t2","S01",Seq("r1"),priority = Task.High) >
                        t("t3","S01",Seq("r2"),priority = Task.High) ) + (
                        t("t4","S01",Seq("r3"),duration = ConstantGenerator(2L)) >
                        t("t5","S01",Seq("r2")) ) )
            val resources = Seq(
                r("r1"),
                r("r2"),
                r("r3")
            )
            val simulator = new ExampleSimulation("output", "Test06", resources)

            val sims = Seq (
                (0L, system.actorOf(FlowLookaheadActor.props("S01",simulator.coordinator,flow))),
            )
            
            simulator.load(shutdownActor,sims)
            simulator.start()
        }

        { 
            /* Example 7
                The flow ( t1 > ( ( (t2+t3+t4) > t5 ) + t6 ) )
                After t1, tasks 2, 3, 4 and 6 can all start. Once tasks 2, 3, and 4 finish, t5 can begin.
                Tasks 5 and 6 are competing for the same resource, task 5 has higher priority but it needs
                to wait for tasks 2,3,4 to finish, so task 6 can start sooner.
                Without lookahead, t6 starts as soon as t4 finishes, and this causes t5 to be delayed.
                Compare this example with example 8
            */
            val flow = (t("t1","S01",Seq("r2"),priority = Task.High) > ( ( (
                        t("t2","S01",Seq("r1"),priority = Task.High, duration = ConstantGenerator(4L)) +
                        t("t3","S01",Seq("r2"),priority = Task.High, duration = ConstantGenerator(3L)) +
                        t("t4","S01",Seq("r3"),priority = Task.High, duration = ConstantGenerator(2L)) ) >
                        t("t5","S01",Seq("r3"),priority = Task.High) ) +
                        t("t6","S01",Seq("r3"),priority = Task.Low, duration = ConstantGenerator(5L)) ) ) 
            val resources = Seq(
                r("r1"),
                r("r2"),
                r("r3")
            )
            val simulator = new ExampleSimulation("output", "Test07", resources)

            val sims = Seq (
                (0L, system.actorOf(FlowSimulationActor.props("S01",simulator.coordinator,flow))),
            )
            
            simulator.load(shutdownActor,sims)
            simulator.start()
        }

        { 
            /* Example 8
                The flow ( t1 > ( ( (t2+t3+t4) > t5 ) + t6 ) )
                The same flow as in example 7
                With lookahead, the scheduler is made aware of t5 and its dependency on t2,3,4.
                t5 is scheduled before t6 due to its priority (and considering the prerequisites),
                and thus t6 is intentionally started later so as to not delay t5.
            */
            val flow = (t("t1","S01",Seq("r2"),priority = Task.High) > ( ( (
                        t("t2","S01",Seq("r1"),priority = Task.High, duration = ConstantGenerator(4L)) +
                        t("t3","S01",Seq("r2"),priority = Task.High, duration = ConstantGenerator(3L)) +
                        t("t4","S01",Seq("r3"),priority = Task.High, duration = ConstantGenerator(2L)) ) >
                        t("t5","S01",Seq("r3"),priority = Task.High) ) +
                        t("t6","S01",Seq("r3"),priority = Task.Low, duration = ConstantGenerator(5L)) ) ) 
            val resources = Seq(
                r("r1"),
                r("r2"),
                r("r3")
            )
            val simulator = new ExampleSimulation("output", "Test08", resources)

            val sims = Seq (
                (0L, system.actorOf(FlowLookaheadActor.props("S01",simulator.coordinator,flow))),
            )
            
            simulator.load(shutdownActor,sims)
            simulator.start()
        }
    }
}