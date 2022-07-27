package com.workflowfm.proter.server

import org.scalatest.funsuite.AnyFunSuite

import cats.implicits.*
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import cats.effect.implicits.*
import cats.effect.std.Random


/**
  * Contains the full end to end tests for Simulation Runner
  *
  */
class SimulationRunnerFullTests extends AnyFunSuite with EntityTestHelper {

  given IORuntime = IORuntime.global

  test("Example, finite, Simulation should run producing results") {
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        IResource("R1", 1, 0.4),
        IResource("R2", 1, 8.3),
      )
      val taskList: List[ITask] = List(
        ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
      )
      val flow: IFlow = IThen(taskList)
      val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), Some(10))
      val request: IRequest = IRequest(None, List(arrival), externalResourceList, None)

      simRun.handle(request)
    }.map { results =>
      assert( //Checks the results are of the correct shape and size (and that they are there)
        (results.cases.size == 10) &&
          (results.tasks.size == 20) &&
          (results.resources.size == 2)
      )
    }.unsafeRunSync()
  }

  test("Example, finite, multiple arrival simulation should run producing results") {
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        IResource("R1", 1, 0.4),
        IResource("R2", 1, 8.3),
        IResource("R3", 1, 8.3),
        IResource("R4", 1, 0.8)
      )

      val taskList1: List[ITask] = List(
        ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow1: IFlow = IThen(taskList1)
      val arrival1: IArrival = IArrival("Sim1", flow1, None, Some(IConstant(4.3)), Some(10))

      val taskList2: List[ITask] = List(
        ITask("D", IConstant(3.4), IConstant(3.4), require("R3"), 0),
        ITask("E", IConstant(3.4), IConstant(3.4), require("R4"), 0),
        ITask("F", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow2: IFlow = IThen(taskList2)
      val arrival2: IArrival = IArrival("Sim2", flow2, None, Some(IConstant(4.3)), Some(10))


      val request: IRequest = IRequest(None, List(arrival1, arrival2), externalResourceList, None)

      simRun.handle(request)
    }.map { results =>
      assert( //Checks the results are of the correct shape and size (and that they are there)
        (results.cases.size == 20) &&
          (results.tasks.size == 60) &&
          (results.resources.size == 4)
      )
    }.unsafeRunSync()
  }

  test("Simulation with Infinite Arrival") {
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        IResource("R1", 1, 0.4),
        IResource("R2", 1, 8.3),
      )
      val taskList: List[ITask] = List(
        ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
      )
      val flow: IFlow = IThen(taskList)
      val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), None)
      val request: IRequest = IRequest(None, List(arrival), externalResourceList, Some(100))


      simRun.handle(request)
    }.map { results =>
      assert( //Checks the results are of the correct shape and size (and that they are there)
        (results.resources.size == 2)
      )
    }.unsafeRunSync()
  }

  /*
  test("Stream Test") {
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        IResource("R1", 1, 0.4),
        IResource("R2", 1, 8.3),
      )
      val taskList: List[ITask] = List(
        ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
      )
      val flow: IFlow = IThen(taskList, "A->B")
      val sim: ISimulation = new ISimulation("Sim Name", flow)
      val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(3), None)
      val request: IRequest = new IRequest(List(arrival), externalResourceList)

      simRun.streamHandler(request)
    }.unsafeRunSync()
  }

  test("Stream Test, multiple arrivals") {
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        IResource("R1", 1, 0.4),
        IResource("R2", 1, 8.3),
        IResource("R3", 1, 8.3),
        IResource("R4", 1, 0.8)
      )

      val taskList1: List[ITask] = List(
        ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow1: IFlow = IThen(taskList1, "A->B->C")
      val sim1: ISimulation = new ISimulation("Sim NameA", flow1)
      val arrival1 = new IArrival(sim1, false, new IDistribution("C", 4.3, None), Some(10), None)

      val taskList2: List[ITask] = List(
        ITask("D", IConstant(3.4), IConstant(3.4), require("R3"), 0),
        ITask("E", IConstant(3.4), IConstant(3.4), require("R4"), 0),
        ITask("F", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow2: IFlow = IThen(taskList2, "D->E->F")
      val sim2: ISimulation = new ISimulation("Sim NameB", flow2)
      val arrival2 = new IArrival(sim2, false, new IDistribution("C", 4.3, None), Some(10), None)


      val request: IRequest = new IRequest(List(arrival1, arrival2), externalResourceList)

      simRun.streamHandler(request)
    }.unsafeRunSync()
  }*/

  test("Simulation that fails resource checks") {
    val io = Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        IResource("R1", 1, 0.4),
        IResource("R2", 1, 8.3),
      )
      val taskList: List[ITask] = List(
        ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        ITask("C", IConstant(3.4), IConstant(3.4), require("R3"), 0)

      )
      val flow: IFlow = IThen(taskList)
      val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), Some(10))
      val request: IRequest = IRequest(None, List(arrival), externalResourceList, None)


      simRun.handle(request).attempt
    }.map { result =>
      assert( result match {
        case Left(e) => e.isInstanceOf[IllegalArgumentException]
        case _ => false
      })
    }
  }

  /*
  test("Streaming Simulation that fails resource checks") {
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        IResource("R1", 1, 0.4),
        IResource("R2", 1, 8.3),
      )
      val taskList: List[ITask] = List(
        ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        ITask("C", IConstant(3.4), IConstant(3.4), require("R3"), 0)

      )
      val flow: IFlow = IThen(taskList, "A->B->C")
      val sim: ISimulation = new ISimulation("Sim Name", flow)
      val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
      val request: IRequest = new IRequest(List(arrival), externalResourceList)

      assertThrows[IllegalArgumentException] {
        simRun.streamHandler(request)
      }
    }.unsafeRunSync()
  }

  test("Streaming Simulation that fails task checks") {
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        IResource("R1", 1, 0.4),
        IResource("R2", 1, 8.3),
      )
      val taskList: List[ITask] = List(
        ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        ITask("B", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)

      )
      val flow: IFlow = IThen(taskList, "A->B->C->GGG")
      val sim: ISimulation = new ISimulation("Sim Name", flow)
      val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
      val request: IRequest = new IRequest(List(arrival), externalResourceList)

      assertThrows[IllegalArgumentException] {
        simRun.streamHandler(request)
      }
    }.unsafeRunSync()
  }*/

  test("Single Task Simulation") {
    Random.scalaUtilRandom[IO].flatMap { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        IResource("R1", 1, 0.4)
      )
      val flow: IFlow = ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0)
      val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), Some(10))
      val request: IRequest = IRequest(None, List(arrival), externalResourceList, None)


      simRun.handle(request)
    }.map { results =>
      assert( //Checks the results are of the correct shape and size (and that they are there)
        (results.cases.size == 10) &&
          (results.tasks.size == 10) &&
          (results.resources.size == 1)
      )
    }.unsafeRunSync()
  }
}

