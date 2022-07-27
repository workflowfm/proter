package com.workflowfm.proter.server

import org.scalatest.funsuite.AnyFunSuite

import cats.implicits.*
import cats.effect.IO
import cats.effect.unsafe.IORuntime
import cats.effect.implicits.*
import cats.effect.std.Random

/**
  * Contains the partial tests for SimulationRunner primarily testing the validation and utility functions of that class
  *
  */
class SimulationRunnerTests extends AnyFunSuite with EntityTestHelper {

  given IORuntime = IORuntime.global

  test("matchingResources, should identify simple matching resource lists") {
    Random.scalaUtilRandom[IO].map { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        new IResource("R1", 1, 0.4),
        new IResource("R2", 1, 8.3)
      )
      val taskList: List[ITask] = List(
        new ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        new ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        new ITask("C", IConstant(3.4), IConstant(3.4), require("R1"), 0)
      )
      val flow: IFlow = IThen(taskList)
      val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), Some(10))
      val request: IRequest = IRequest(None, List(arrival), externalResourceList, None)

      assert(simRun.matchingResources(request))
    }.unsafeRunSync()
  }


  test("matchingResources, should identify matching resource list with multiple arrivals") {
    Random.scalaUtilRandom[IO].map { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()
      val externalResourceList: List[IResource] = List(
        new IResource("R1", 1, 0.4),
        new IResource("R2", 1, 8.3),
        new IResource("R3", 1, 11),
        new IResource("R4", 1, 0.8)
      )

      val taskList1: List[ITask] = List(
        new ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        new ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        new ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow1: IFlow = IThen(taskList1)
      val arrival1: IArrival = IArrival("Sim1", flow1, None, Some(IConstant(4.3)), Some(10))

      val taskList2: List[ITask] = List(
        new ITask("D", IConstant(3.4), IConstant(3.4), require("R3"), 0),
        new ITask("E", IConstant(3.4), IConstant(3.4), require("R4"), 0),
        new ITask("F", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow2: IFlow = IThen(taskList2)
      val arrival2: IArrival = IArrival("Sim2", flow2, None, Some(IConstant(4.3)), Some(10))

      val request: IRequest = IRequest(None, List(arrival1, arrival2), externalResourceList, None)

      assert(simRun.matchingResources(request))
    }.unsafeRunSync()
  }

  test("matchingResources, multiple resources per task") {
    Random.scalaUtilRandom[IO].map { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()

      val externalResourceList: List[IResource] = List(
        new IResource("R1", 1, 0.4),
        new IResource("R2", 1, 8.3)
      )
      val taskList: List[ITask] = List(
        new ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        new ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        new ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow: IFlow = IThen(taskList)
      val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), Some(10))
      val request: IRequest = IRequest(None, List(arrival), externalResourceList, None)

      assert(simRun.matchingResources(request))
    }.unsafeRunSync()
  }

  test("matchingResources, missing task in defined resources") {
    Random.scalaUtilRandom[IO].map { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()

      val externalResourceList: List[IResource] = List(
        new IResource("R1", 1, 0.4),
      )
      val taskList: List[ITask] = List(
        new ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        new ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        new ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow: IFlow = IThen(taskList)
      val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), Some(10))
      val request: IRequest = IRequest(None, List(arrival), externalResourceList, None)

      assert(simRun.matchingResources(request) == false)
    }.unsafeRunSync()
  }

  test("matchingResources, missing task in defined resources with multiple arrivals") {
    Random.scalaUtilRandom[IO].map { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()

      val externalResourceList: List[IResource] = List(
        new IResource("R1", 1, 0.4),
        new IResource("R2", 1, 8.3),
        new IResource("R4", 1, 0.8)
      )

      val taskList1: List[ITask] = List(
        new ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        new ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        new ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow1: IFlow = IThen(taskList1)
      val arrival1: IArrival = IArrival("Sim1", flow1, None, Some(IConstant(4.3)), Some(10))

      val taskList2: List[ITask] = List(
        new ITask("D", IConstant(3.4), IConstant(3.4), require("R3"), 0),
        new ITask("E", IConstant(3.4), IConstant(3.4), require("R4"), 0),
        new ITask("F", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow2: IFlow = IThen(taskList2)
      val arrival2: IArrival = IArrival("Sim2", flow2, None, Some(IConstant(4.3)), Some(10))

      val request: IRequest = IRequest(None, List(arrival1, arrival2), externalResourceList, None)

      assert(!simRun.matchingResources(request))
    }.unsafeRunSync()
  }

  test("matchingResources, unused tasks in specified resources") {
    Random.scalaUtilRandom[IO].map { r =>
      given Random[IO] = r
      val simRun = new SimulationRunner[IO]()

      val externalResourceList: List[IResource] = List(
        new IResource("R1", 1, 0.4),
        new IResource("R2", 1, 8.3),
        new IResource("R3", 1, 6.2)
      )
      val taskList: List[ITask] = List(
        new ITask("A", IConstant(3.4), IConstant(3.4), require("R1"), 0),
        new ITask("B", IConstant(3.4), IConstant(3.4), require("R2"), 0),
        new ITask("C", IConstant(3.4), IConstant(3.4), require("R1","R2"), 0)
      )
      val flow: IFlow = IThen(taskList)
      val arrival: IArrival = IArrival("Sim Name", flow, None, Some(IConstant(4.3)), Some(10))
      val request: IRequest = IRequest(None, List(arrival), externalResourceList, None)

      assert(simRun.matchingResources(request))
    }.unsafeRunSync()
  }

}
