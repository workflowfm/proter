package com.workflowfm.proteronline

import io.circe._, io.circe.parser._
import io.circe.generic.semiauto._
import com.workflowfm.proter._
import com.workflowfm.proter.schedule.ProterScheduler
import scala.concurrent.duration._
import scala.concurrent.Await
import com.workflowfm.proter
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.workflowfm.proter.events.PromiseHandler
import cats.effect.IO

//Below case classes define the structure of the JSON for decoding
case class IRequest(arrival: IArrival, resources: List[IResource])
case class IArrival(simulation: ISimulation, infinite: Boolean, rate: IDistribution, simulationLimit: Option[Int], timeLimit: Option[Int])
case class ISimulation(name: String, flow: IFlow)
case class IFlow(tasks: List[ITask], ordering: String)

case class ITask(name: String, duration: IDistribution, cost: IDistribution, resources: String, priority: Int) {
  def toProterTask(): Task = {
    proter.Task(this.name, this.duration.toProterDistribution()).withCostGenerator(this.cost.toProterDistribution()).withResources(this.resources.split(",")).withPriority(this.priority)
  }
}

case class IResource(name: String, costPerTick: Double) {
  def toProterResource(): TaskResource = {
    new TaskResource(this.name, this.costPerTick)
  }
}

case class IDistribution(distType: String, value1: Double, value2: Option[Double]) {
  def toProterDistribution(): Distribution = {
    if (this.distType == "C") {
      new Constant(this.value1);
    } else if (this.distType == "E") {
      new Exponential(this.value1);
    } else if (this.distType == "U") {
      new Uniform(this.value1, this.value2.get)
    } else {
      throw new IllegalArgumentException("Invalid Distribution Type: Was \'" + this.distType + "\' can only be C, E or U")
    }
  }
}

//Class defines an encoding for the results from a simulation, data should be pulled from a SimMetricsAggregator
case class Results(start: Long, end: Long, resources: List[ResourceMetrics], simulations: List[SimulationMetrics], tasks: List[TaskMetrics])

class JsonParser {

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

    val coordinator : Coordinator = new Coordinator(new ProterScheduler)

    val promiseHandler = new PromiseHandler(new SimMetricsHandler(new SimMetricsPrinter))
    val agg = promiseHandler.future

    coordinator.subscribe(promiseHandler)
    coordinator.subscribe(new proter.events.PrintEventHandler) //For debug use

    //println("Starting Simulations")

    programmaticTransform(coordinator, request)

    coordinator.start()

    val result: Future[Results] = agg.map(x => processAggregator(x))


    Await.result(result, 10.second) //Current approach involves getting the data out of the future here, in later versions the future will be passed out of the function
    //println("Simulations Complete")
    result.value.get.get //Grabs the result out of the future
  }

  def streamHandler(request: IRequest): fs2.Stream[IO, String] = {
    
    val coordinator : Coordinator = new Coordinator(new ProterScheduler)

    val streamHandler: StreamEventHandler = new StreamEventHandler()
    coordinator.subscribe(streamHandler)
    coordinator.subscribe(new SimMetricsHandler(new SimMetricsPrinter()))

    programmaticTransform(coordinator, request)
    coordinator.start()

    streamHandler.stream
  }

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

  def validateRequest(request: IRequest): Boolean = {
    val definedResources: Set[String] = request.resources.map(_.name).toSet
    val referencedResources: Set[String] = request.arrival.simulation.flow.tasks.flatMap(_.resources.split(",")).toSet
    if (definedResources.equals(referencedResources)) {
      false
    } else {
      true
    }
  }
}

object Test {


  val betterSampleData: String = "{ \"arrival\": {\"simulation\": {\"name\": \"Example Name\", \"flow\":{ \"tasks\": [{\"name\": \"A\",\"duration\": {\"distType\": \"C\",\"value1\": 2,\"value2\": null},\"cost\": {\"distType\": \"E\",\"value1\": 4,\"value2\": null},\"resources\": \"R1\",\"priority\": 0},{\"name\": \"B\",\"duration\": {\"distType\": \"U\",\"value1\": 3,\"value2\": 7},\"cost\": {\"distType\": \"C\",\"value1\": 5,\"value2\": null},\"resources\": \"R2\",\"priority\": 0}], \"ordering\": \"A->B\"} }, \"infinite\": false,\"rate\": {\"distType\": \"C\",\"value1\": 5,\"value2\": null},\"simulationLimit\": 10,\"timeLimit\": 25000},\"resources\": [{\"name\": \"R1\",\"costPerTick\": 3},{\"name\": \"R2\",\"costPerTick\": 2}]}"
  
  implicit val requestDecoder1: Decoder[IRequest] = deriveDecoder[IRequest]
  implicit val requestDecoder2: Decoder[IArrival] = deriveDecoder[IArrival]
  implicit val requestDecoder3: Decoder[ISimulation] = deriveDecoder[ISimulation]
  implicit val requestDecoder4: Decoder[IFlow] = deriveDecoder[IFlow]
  implicit val requestDecoder5: Decoder[ITask] = deriveDecoder[ITask]
  implicit val requestDecoder6: Decoder[IResource] = deriveDecoder[IResource]
  implicit val requestDecoder7: Decoder[IDistribution] = deriveDecoder[IDistribution]

  /**
    * Decodes the JSON provided to it based on the implicit decoders defined in the class' attributes using
    * circle's auto decode thing with the case classes at the top of this document
    *
    * @param json A JSON string of the correct format
    * @return Either a 
    */
  def decodeJson(json: String) : IRequest = {
    val result = decode[IRequest](json)

    result match {
      case Right(request) => return request
      case Left(error) => 
        println("Error: " + error)
        return null //Not good practice need to improve error handling here and in the parent function
    }
  }

  def main(args: Array[String]) {
    val obj = new JsonParser()
    val req: IRequest = decodeJson(betterSampleData)
    val a: Results = obj.process(req)
    println(a)
  }
}
