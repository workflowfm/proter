package com.workflowfm.proter
package server

import java.util.UUID

import cats.{ MonadError }
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
import metrics.*
import schedule.ProterScheduler

class SimulationRunner[F[_] : Random : Async : UUIDGen](using monad: MonadError[F, Throwable]) {

  import IntermediateObjects.given


  /**
    * This top level function should take an IRequest and then return a Results object
    *
    * @param request The input IRequest
    * @return A Results object
    */
  def handle(request: IRequest): F[Metrics] = {

    println(s"Handling IRequest: $request")

    if (!this.matchingResources(request)) {
      monad.raiseError(new IllegalArgumentException("Resources do not match"))
    }

    for {
      result <- Deferred[F, Metrics]
      simulator = Simulator[F](ProterScheduler).withSubs (
        MetricsSubscriber[F](
          MetricsResult(result)
        ),
//        PrintEvents()
      )

      scenario = getScenario(request)
      _ <- simulator.simulate(scenario)
      metrics <- result.get
    } yield (metrics)

  }

/*
  /**
    * This method is the streaming equivalent of the process() method. Takes in an IRequest,
    * builds the proter objects, runs the simulation and passes back a stream of the simulation events
    *
    * @param request An IRequest Object
    * @return
    */
  def streamHandler(request: IRequest): Stream[IO, Event] = {

    if (!this.matchingResources(request)) {
      throw new IllegalArgumentException("Resources do not match")
    }
    if (!this.matchingTasks(request)) {
      throw new IllegalArgumentException("Tasks do not match")
    }
    
    val coordinator : Coordinator = new Coordinator(new ProterScheduler)

    programmaticTransform(coordinator, request)

    val s = for {
      (sub, str) <- streamSetup()
      stream <- str.concurrently(Stream.eval(streamStart(coordinator, sub)))
    } yield (stream)
    s //Returns the stream
  }

  /**
    * Method that starts a stream prepared by the streamSetup() method
    *
    * @param p The Coordinator (AKA producer)
    * @param s The Event Handler (AKA subscriber)
    * @return
    */
  def streamStart(p: Coordinator, s: EventHandler): IO[Unit] = for {
    _ <- IO(p.subscribe(s))
    _ <- IO.fromFuture(IO.pure(p.start()))
  } yield ()

  /**
    * Sets up a stream that will catch the events of the proter coordinator and allows them to be sent back over a connection
    *
    * @return Some kinda Stream
    */
  def streamSetup(): Stream[IO, (EventHandler, Stream[IO, Event])] = { 
    for {
      dispatcher <- Stream.resource(Dispatcher[IO])
      queue <- Stream.eval( Queue.unbounded[IO, Option[Event]] )
      impureInterface <- Stream.eval( IO.delay {
        new EventHandler { //Creates a custom EventHandler subclass that can interact with the stream
          override val id: UUID = UUID.randomUUID
          override def onEvent(event: Event): Unit = {
            //println("[Subscriber] Received: " + event.toString())
            dispatcher.unsafeRunSync(queue.offer(Some(event)))
          }
          override def onDone(publisher: Publisher): Unit = {
            //println("[Subscriber] Done!")
            dispatcher.unsafeRunSync(queue.offer(None))
          }
        }
      })
    } yield (impureInterface, Stream.fromQueueNoneTerminated(queue))
  }
*/
  /**
    * Method takes a decoded request and adds to the given coordinator the details of the request
    *
    * @param coord
    * @param requestObj
    */
  def getScenario(requestObj: IRequest): Scenario[F] = {

    //Resources
    val resources: List[Resource] = requestObj.resources.map(_.toProterResource()) //Build the task resources

    val scenario = Scenario[F]("Server Scenario")
      .withResources(resources)

    //For each arrival in the request
    val updated = requestObj.arrivals.foldLeft(scenario) { (scenario, arrival) =>
      arrival.limit match {
        case None => scenario.withInfiniteArrival(
          arrival.name,
          arrival.flow.flow,
          arrival.rate.toProterDistribution()
        )
        case Some(l) => scenario.withArrival(
          arrival.name,
          arrival.flow.flow,
          arrival.rate.toProterDistribution(),
          l
        )
      }
    }

    requestObj.timeLimit.map(l => updated.withLimit(l)).getOrElse(updated)
  }


  /**
    * This checks to ensure that the request has matching resources, as in ensuring the resources referenced in the Tasks are
    * defined in the resource list
    * 
    * @param request An IRequest object to check
    * @return a boolean
    */
  def matchingResources(request: IRequest): Boolean = {
    val definedResources: Set[String] = request.resources.map(_.name).toSet
    val referencedResources: Set[String] = request.arrivals.flatMap(arrival => arrival.flow.resourceNames).toSet
    if (referencedResources.subsetOf(definedResources)) {
      true
    } else {
      false
    }
  }

}
