package com.workflowfm.simulator

import akka.actor.{ActorSystem, ActorRef,Props}
import akka.testkit.{ ImplicitSender, TestActors, TestKit, TestProbe }
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import org.scalatest.{ BeforeAndAfterAll, Matchers, WordSpecLike }
import scala.concurrent._
import scala.concurrent.Await
import scala.concurrent.duration._
import scala.util.{ Failure, Success, Try }
import java.util.UUID
import com.workflowfm.simulator.metrics._
import uk.ac.ed.inf.ppapapan.subakka.MockPublisher

class SimulationTests
    extends TestKit(
      ActorSystem("SimulaionTests", ConfigFactory.parseString(MockPublisher.config))
    )
    with WordSpecLike
    with Matchers
    with BeforeAndAfterAll
    with ImplicitSender {
  implicit val executionContext = ExecutionContext.global
  implicit val timeout: FiniteDuration = 10.seconds

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Simulations" must {

        "interact correctly with a coordinator with no tasks" in {
            class DummySim(name: String, coordinator: ActorRef)
            (implicit executionContext: ExecutionContext) 
            extends Simulation(name,coordinator) {
                override def run():Future[Any] = { Future.unit } //finish instantly
                override def complete(task: Task, time: Long): Unit = { Unit } //does nothing
            }

            val sim = system.actorOf(Props(new DummySim("sim",self)))
            
            sim ! Simulation.Start
            expectMsg( Coordinator.SimStarted("sim"))
            expectMsg( Coordinator.SimDone("sim",Success()))
            expectNoMsg()
        }

        "interact correctly with a cooridnator with one task" in {
            val sim = system.actorOf(SingleTaskSimulation.props("sim",self,Seq("r1"),ConstantGenerator(2L)))

            sim ! Simulation.Start
            expectMsg( Coordinator.SimStarted("sim"))
            val Coordinator.AddTask(id, generator, resources) = expectMsgType[ Coordinator.AddTask ]
            expectMsg( Coordinator.SimReady )

            val task = generator.create(id,0L,sim,"r1")
            sim ! Simulation.TaskCompleted(task,2L)
            val Coordinator.SimDone(name, future) = expectMsgType[ Coordinator.SimDone ]
            name should be ("sim")
            expectNoMsg()
        }

        "interact correctly with a cooridnator with a sequence of task, acking tasks as they complete" in {
            class DummySim(name: String, coordinator: ActorRef)
            (implicit executionContext: ExecutionContext) 
            extends AsyncSimulation(name,coordinator) with FutureTasks {
                override def run():Future[Any] = { 
                    val id1 = java.util.UUID.randomUUID
                    val id2 = java.util.UUID.randomUUID
                    val id3 = java.util.UUID.randomUUID
                    val task1 = futureTask(id1, TaskGenerator("task1","sim",ConstantGenerator(2L),ConstantGenerator(0L)),Seq("r1"))
                    ready()
                    val task2 = task1 flatMap { _=> 
                        val t = futureTask(id2,TaskGenerator("task2","sim",ConstantGenerator(2L),ConstantGenerator(0L)),Seq("r1"))
                        ack(Seq(id1))
                        t
                    }
                    val task3 = task2 flatMap { _=> 
                        val t = futureTask(id3,TaskGenerator("task3","sim",ConstantGenerator(2L),ConstantGenerator(0L)),Seq("r1"))
                        ack(Seq(id2))
                        t
                    }
                    task3
                } 
            }

            val sim = system.actorOf(Props(new DummySim("sim",self)))
            
            sim ! Simulation.Start
            expectMsg( Coordinator.SimStarted("sim"))
            val Coordinator.AddTask(id1, generator1, resources1) = expectMsgType[ Coordinator.AddTask ]
            expectMsg( Coordinator.SimReady )
            expectNoMsg()

            val task1 = generator1.create(id1,0L,sim,resources1:_*)
            sim ! Simulation.TaskCompleted(task1,2L)

            val Coordinator.AddTask(id2, generator2, resources2) = expectMsgType[ Coordinator.AddTask ]
            expectMsg( Coordinator.AckTasks(Seq(id1)))
            expectNoMsg()

            val task2 = generator2.create(id2,2L,sim,resources2:_*)
            sim ! Simulation.TaskCompleted(task2,4L)

            val Coordinator.AddTask(id3, generator3, resources3) = expectMsgType[ Coordinator.AddTask ]
            expectMsg( Coordinator.AckTasks(Seq(id2)))
            expectNoMsg()

            val task3 = generator3.create(id3,2L,sim,resources3:_*)
            sim ! Simulation.TaskCompleted(task3,6L)

            //expectMsg( Coordinator.AckTasks(Seq(id3)))
            val Coordinator.SimDone(name, future) = expectMsgType[ Coordinator.SimDone ]
            name should be ("sim")
            expectNoMsg()

        }
        
        "add tasks in the future" in {
            class DummySim(name: String, coordinator: ActorRef)
            (implicit executionContext: ExecutionContext) 
            extends Simulation(name,coordinator) {
                val promise = Promise[Any]()
                override def run():Future[Any] = { 
                    val id1 = java.util.UUID.randomUUID
                    val id2 = java.util.UUID.randomUUID
                    val id3 = java.util.UUID.randomUUID
                    val task1 = task(id1, TaskGenerator("task1","sim",ConstantGenerator(2L),ConstantGenerator(0L)),0L,Seq("r1"))
                    val task2 = task(id2,TaskGenerator("task2","sim",ConstantGenerator(2L),ConstantGenerator(0L)),2L,Seq("r1")) 
                    val task3 = task(id3,TaskGenerator("task3","sim",ConstantGenerator(2L),ConstantGenerator(0L)),4L,Seq("r1"))
                    ready()
                    promise.future
                } 

                override def complete(task: Task, time: Long): Unit = {
                    ack(Seq(task.id))
                    if (task.name=="task3") promise.success(Unit)
                }
            }

            val sim = system.actorOf(Props(new DummySim("sim",self)))

            sim ! Simulation.Start
            expectMsg( Coordinator.SimStarted("sim"))
            val Coordinator.AddTaskAtTime(id1, generator1, time1, resources1) = expectMsgType[ Coordinator.AddTaskAtTime ]
            val Coordinator.AddTaskAtTime(id2, generator2, time2, resources2) = expectMsgType[ Coordinator.AddTaskAtTime ]
            val Coordinator.AddTaskAtTime(id3, generator3, time3, resources3) = expectMsgType[ Coordinator.AddTaskAtTime ]
            expectMsg( Coordinator.SimReady )

            val task1 = generator1.create(id1,0L,sim,"r1")
            val task2 = generator2.create(id2,0L,sim,"r1")
            val task3 = generator3.create(id3,0L,sim,"r1")

            sim ! Simulation.TaskCompleted(task1,2L)
            expectMsg( Coordinator.AckTasks(Seq(id1)))

            sim ! Simulation.TaskCompleted(task2,4L)
            expectMsg( Coordinator.AckTasks(Seq(id2)))

            sim ! Simulation.TaskCompleted(task3,6L)
            expectMsg( Coordinator.AckTasks(Seq(id3)))
            val Coordinator.SimDone(name, future) = expectMsgType[ Coordinator.SimDone ]
            name should be ("sim")
            //expectNoMsg()

        }
    }

}