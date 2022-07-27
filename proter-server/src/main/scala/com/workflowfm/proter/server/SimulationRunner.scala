package com.workflowfm.proter
package server

import java.util.UUID

import cats.MonadError
import cats.implicits.*
import cats.effect.{ Concurrent, Deferred, Clock, Async }
import cats.effect.std.{ Random, UUIDGen }
import cats.effect.implicits.*

import fs2.Stream

import io.circe.*
import io.circe.generic.semiauto.*

import cases.Case
import events.*
import flows.*
import flows.given
import metrics.{ Metrics, MetricsResult, MetricsSubscriber }
import schedule.ProterScheduler

class SimulationRunner[F[_] : Random : Async : UUIDGen](using monad: MonadError[F, Throwable]) {

  import Entities.given

  /**
    * This top level function should take an IRequest and then return a Results object
    *
    * @param request
    *   The input IRequest
    * @return
    *   A Results object
    */
  def handle(request: IRequest): F[Metrics] = {

    if (!this.matchingResources(request)) {
      monad.raiseError(new IllegalArgumentException("Resources do not match"))
    }

    for {
      result <- Deferred[F, Metrics]
      simulator = Simulator[F](ProterScheduler).withSubs(
        MetricsSubscriber[F](
          MetricsResult(result)
        )
//        PrintEvents()
      )

      scenario = getScenario(request)
      _ <- simulator.simulate(scenario)
      metrics <- result.get
    } yield (metrics)

  }

  def stream(request: IRequest): Stream[F, Event] = {

    if (!this.matchingResources(request)) {
      monad.raiseError(new IllegalArgumentException("Resources do not match"))
    }

    val simulator = Simulator[F](ProterScheduler)
    val scenario = getScenario(request)

    simulator
      .stream(scenario)
      .map(evt =>
        evt match {
          case Left(e) => EError("*FATAL ERROR*", Long.MaxValue, e.getLocalizedMessage)
          case Right(e) => e
        }
      )
  }

  /**
    * Method takes a decoded request and adds to the given coordinator the details of the request
    *
    * @param coord
    * @param requestObj
    */
  def getScenario(requestObj: IRequest): Scenario[F] = {

    // Resources
    val resources: List[Resource] = requestObj.resources.map(_.toProterResource()) // Build the task resources

    val scenario = Scenario[F]("Server Scenario")
      .withResources(resources)

    val starting = requestObj.start.map(s => scenario.withStartingTime(s)).getOrElse(scenario)

    // For each arrival in the request
    val updated = requestObj.arrivals.foldLeft(starting) { (scenario, arrival) =>
      arrival.rate match {
        case None =>
          arrival.start match {
            case None =>
              scenario.withCase(
                arrival.name,
                arrival.flow.flow
              )
            case Some(s) =>
              scenario.withTimedCase(
                arrival.name,
                s,
                arrival.flow.flow
              )
          }
        case Some(rate) =>
          (arrival.start, arrival.limit) match {
            case (None, None) =>
              scenario.withInfiniteArrival(
                arrival.name,
                arrival.flow.flow,
                rate.toProterDistribution()
              )
            case (Some(s), None) =>
              scenario.withTimedInfiniteArrival(
                arrival.name,
                s,
                arrival.flow.flow,
                rate.toProterDistribution()
              )
            case (None, Some(l)) =>
              scenario.withArrival(
                arrival.name,
                arrival.flow.flow,
                rate.toProterDistribution(),
                l
              )
            case (Some(s), Some(l)) =>
              scenario.withTimedArrival(
                arrival.name,
                s,
                arrival.flow.flow,
                rate.toProterDistribution(),
                l
              )
          }
      }
    }

    requestObj.timeLimit.map(l => updated.withLimit(l)).getOrElse(updated)
  }

  /**
    * This checks to ensure that the request has matching resources, as in ensuring the resources
    * referenced in the Tasks are defined in the resource list
    *
    * @param request
    *   An IRequest object to check
    * @return
    *   a boolean
    */
  def matchingResources(request: IRequest): Boolean = {
    val definedResources: Set[String] = request.resources.map(_.name).toSet
    val referencedResources: Set[String] =
      request.arrivals.flatMap(arrival => arrival.flow.resourceNames).toSet
    if (referencedResources.subsetOf(definedResources)) {
      true
    } else {
      false
    }
  }

}
