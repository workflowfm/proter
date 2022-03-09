package com.workflowfm.proteronline

import java.util.UUID

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._

import cats.effect.IO
import cats.effect.std.Dispatcher
import cats.effect.std.Queue
import fs2.Stream
import io.circe._
import io.circe.generic.semiauto._

import com.workflowfm.proter._
import com.workflowfm.proter.events.Event
import com.workflowfm.proter.events.EventHandler
import com.workflowfm.proter.events.PromiseHandler
import com.workflowfm.proter.events.Publisher
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import com.workflowfm.proter.schedule.ProterScheduler

class SimulationRunner {

  //Forming the implicit decoders
  implicit val requestDecoder1: Decoder[IRequest] = deriveDecoder[IRequest]
  implicit val requestDecoder2: Decoder[IArrival] = deriveDecoder[IArrival]
  implicit val requestDecoder3: Decoder[ISimulation] = deriveDecoder[ISimulation]
  implicit val requestDecoder4: Decoder[IFlow] = deriveDecoder[IFlow]
  implicit val requestDecoder5: Decoder[ITask] = deriveDecoder[ITask]
  implicit val requestDecoder6: Decoder[IResource] = deriveDecoder[IResource]
  implicit val requestDecoder7: Decoder[IDistribution] = deriveDecoder[IDistribution]

  /**
    * This top level function should take an IRequest and then return a Results object
    *
    * @param request The input IRequest
    * @return A Results object
    */
  def process(request: IRequest) : Results = {

    if (!this.matchingResources(request)) {
      throw new IllegalArgumentException("Resources do not match")
    }
    if (!this.matchingTasks(request)) {
      throw new IllegalArgumentException("Tasks do not match")
    }

    val coordinator : Coordinator = new Coordinator(new ProterScheduler)

    val promiseHandler = new PromiseHandler(new SimMetricsHandler) //(new SimMetricsPrinter)
    val agg = promiseHandler.future

    coordinator.subscribe(promiseHandler)

    programmaticTransform(coordinator, request)

    coordinator.start()

    val result: Future[Results] = agg.map(x => processAggregator(x))


    Await.result(result, 10.second) //Current approach involves getting the data out of the future here, in later versions the future will be passed out of the function
    result.value.get.get //Grabs the result out of the future
  }

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

  /**
    * Method takes a decoded request and adds to the given coordinator the details of the request
    *
    * @param coord
    * @param requestObj
    */
  def programmaticTransform(coord: Coordinator, requestObj: IRequest): Unit = {

    //Resources
    val resources: List[TaskResource] = requestObj.resources.map(_.toProterResource()) //Build the task resources
    coord.addResources(resources) //Task Resources added

    //If multiple simulations are to be implemented a loop over simulations should include all lines down to, but excluding, the if statements

    val order: Array[String] = requestObj.arrival.simulation.flow.ordering.split("->")

    val iTasks: List[ITask] = requestObj.arrival.simulation.flow.tasks

    val orderedTasks: List[ITask] = order.map(x => iTasks.filter(y => y.name == x).head).toList

    val tasks: List[FlowTask] = orderedTasks.map(_.toProterTask()).map(new FlowTask(_))
    
    val taskFlow: Flow = Flow.seq(tasks)

    val simGen = new FlowSimulationGenerator(requestObj.arrival.simulation.name, taskFlow)

    if (requestObj.arrival.infinite) {
      coord.addInfiniteArrivalNow(
        requestObj.arrival.rate.toProterDistribution(),
        simGen
      )
      coord.limit(requestObj.arrival.timeLimit.get.toLong) //If its infinite then we add a timer to stop it running forever
    } else {
      coord.addArrivalNow(
        requestObj.arrival.simulationLimit.get, //If its finite then its this limit on the number of simulations that stops it
        requestObj.arrival.rate.toProterDistribution(),
        simGen
      )
    }
  }

  /**
    * Method takes the Proter SimMetricsHandler and turns it into a format that is more suitable for
    * turning into JSON and being sent back to the user
    *
    * @param aggregator The SimMetricsHandler from a completed set of simulations
    * @return A Results object
    */
  def processAggregator(aggregator: SimMetricsAggregator): Results = {
    new Results(
      aggregator.start.get,
      aggregator.end.get,
      aggregator.resourceMetrics.toList,
      aggregator.simulationMetrics.toList,
      aggregator.taskMetrics.toList
    )
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
    val referencedResources: Set[String] = request.arrival.simulation.flow.tasks.flatMap(_.resources.split(",")).toSet
    if (referencedResources.subsetOf(definedResources)) {
      true
    } else {
      false
    }
  }

  /**
    * This checks to ensure that the request has matching tasks, as in ensuring the tasks referenced in the flow ordering are
    * defined in the flows task list
    *
    * @param request An IRequest object
    * @return Boolean
    */
  def matchingTasks(request: IRequest): Boolean = {
    val definedTasks: Set[String] = request.arrival.simulation.flow.tasks.map(_.name).toSet
    val referencedTasks: Set[String] = request.arrival.simulation.flow.ordering.split("->").toSet
    if (referencedTasks.subsetOf(definedTasks)) {
      true
    } else {
      false
    }
  }
}