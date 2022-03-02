package com.workflowfm.proteronline

import io.circe._
import io.circe.generic.semiauto._
import com.workflowfm.proter._
import com.workflowfm.proter.schedule.ProterScheduler
import scala.concurrent.duration._
import scala.concurrent.Await
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.workflowfm.proter.events.PromiseHandler
import cats.effect.IO
import fs2.Stream
import cats.effect.std.Dispatcher
import cats.effect.std.Queue
import com.workflowfm.proter.events.Event
import java.util.UUID
import com.workflowfm.proter.events.EventHandler
import com.workflowfm.proter.events.Publisher

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

    val coordinator : Coordinator = new Coordinator(new ProterScheduler)

    val promiseHandler = new PromiseHandler(new SimMetricsHandler) //(new SimMetricsPrinter)
    val agg = promiseHandler.future

    coordinator.subscribe(promiseHandler)

    programmaticTransform(coordinator, request)

    coordinator.start()

    val result: Future[Results] = agg.map(x => processAggregator(x))


    Await.result(result, 10.second) //Current approach involves getting the data out of the future here, in later versions the future will be passed out of the function
    //println("Simulations Complete")
    result.value.get.get //Grabs the result out of the future
  }

  def streamHandler(request: IRequest): IO[Unit] = {

    if (!this.matchingResources(request)) {
      throw new IllegalArgumentException("Resources do not match")
    }
    
    val coordinator : Coordinator = new Coordinator(new ProterScheduler)

    //val streamHandler: StreamEventHandler = new StreamEventHandler()
    //coordinator.subscribe(streamHandler)
    //coordinator.subscribe(new SimMetricsHandler(new SimMetricsPrinter())) Uncomment for debug

    programmaticTransform(coordinator, request)
    //coordinator.start()
    println("Made it to the start")

    val s = for {
      (sub, str) <- streamSetup()
      _ <- Stream.eval(streamStart(coordinator, sub)).concurrently(Stream.eval(consume(str)))
    } yield ()
    s.compile.drain
  }

  def streamStart(p: Coordinator, s: EventHandler): IO[Unit] = for {
    _ <- IO(p.subscribe(s))
    _ <- IO(p.start())
  } yield ()

  def streamSetup(): Stream[IO, (EventHandler, Stream[IO, Event])] = { 
    for {
      dispatcher <- Stream.resource(Dispatcher[IO])
      queue <- Stream.eval( Queue.unbounded[IO, Option[Event]] ) // Queue.unbounded[IO, Option[Event]] )
      impureInterface <- Stream.eval( IO.delay {
        new EventHandler {
          override val id: UUID = UUID.randomUUID
          override def onEvent(event: Event): Unit = {
            println("[Subscriber] Received: " + event.toString())
            dispatcher.unsafeRunSync(queue.offer(Some(event)))
          }
          override def onDone(publisher: Publisher): Unit = {
            println("[Subscriber] Done!")
            dispatcher.unsafeRunSync(queue.offer(None))
          }
        }
      })
    } yield (impureInterface, Stream.fromQueueNoneTerminated(queue))
  }

  def consume(str: Stream[IO, Event]): IO[Unit] = for {
    _ <- IO.println("Setting up stream...")
    _ <- str.evalMap(event => IO.println(Thread.currentThread().getName + " consumed an Event: " + event.toString())).compile.drain
    _ <- IO.println("Consuming finished!")
  } yield ()

  /**
    * Method takes a decoded request and adds to the given coordinator the details of the request
    *
    * @param coord
    * @param requestObj
    */
  def programmaticTransform(coord: Coordinator, requestObj: IRequest) = {

    //Resources
    val resources: List[TaskResource] = requestObj.resources.map(_.toProterResource()) //Build the task resources
    coord.addResources(resources) //Task Resources added

    //If/when multiple simulations are implimented loop should include all lines down to, but excluding, coord.limit...

    val order: Array[String] = requestObj.arrival.simulation.flow.ordering.split("->")

    val iTasks: List[ITask] = requestObj.arrival.simulation.flow.tasks

    val orderedTasks: List[ITask] = order.map(x => iTasks.filter(y => y.name == x).head).toList

    val tasks: List[FlowTask] = orderedTasks.map(_.toProterTask()).map(new FlowTask(_))
    
    val taskFlow: Flow = Flow.seq(tasks)

    val simGen = new FlowSimulationGenerator(requestObj.arrival.simulation.name, taskFlow)

    //println(requestObj.arrival.simulationLimit.isEmpty)

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
}

object Test {
  def main(args: Array[String]) {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
    )
    val flow: IFlow = new IFlow(taskList, "A->B")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(3), None)
    val request: IRequest = new IRequest(arrival, externalResourceList)

    println("Stuff has finished now?")
    println(simRun.streamHandler(request))
  }
}