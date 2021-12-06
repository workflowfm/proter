package com.workflowfm.proteronline

import io.circe._, io.circe.parser._
import io.circe.generic.semiauto._
import com.workflowfm.proter._
import com.workflowfm.proter.schedule.ProterScheduler
import com.workflowfm.proter.flows.FlowSimulation
import scala.concurrent.duration._
import scala.concurrent.Await
import com.workflowfm.proter
import com.workflowfm.proter.flows._
import com.workflowfm.proter.metrics._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import com.workflowfm.proter.events.PromiseHandler

//Below case classes define the structure of the JSON for decoding
case class IRequest(arrival: IArrival, resources: List[IResource])
case class IArrival(simulation: ISimulation, numberOfRuns: Int)
case class ISimulation(name: String, flow: IFlow)
case class IFlow(tasks: List[ITask], ordering: String)

case class ITask(name: String, duration: Int, cost: Int, resources: String) {
  def toProterTask(): Task = {
    proter.Task(this.name, Constant(this.duration.toDouble)).withCost(this.cost.toDouble).withResources(this.resources.split(","))
  }
}

case class IResource(name: String, costPerTick: Int) {
  def toProterResource(): TaskResource = {
    new TaskResource(this.name, this.costPerTick.toDouble)
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

  /**
    * This top level function should take an IRequest and then return a Results object
    *
    * @param request The input IRequest
    * @return 
    */
  def process(request: IRequest) : Results = {

    //println(request)

    val coordinator : Coordinator = new Coordinator(new ProterScheduler) //Consider multiple schedulers

    val promiseHandler = new PromiseHandler(new SimMetricsHandler(new proter.metrics.SimMetricsPrinter))
    val agg = promiseHandler.future

    coordinator.subscribe(promiseHandler)
    coordinator.subscribe(new proter.events.PrintEventHandler)

    //println("Starting Simulations")

    programmaticTransform(coordinator, request)

    coordinator.start()

    val result: Future[Results] = agg.map(x => processAggregator(x))


    Await.result(result, 10.second) //Current approach involves getting the data out of the future here, in later versions the future will be passed out of the function
    //println("Simulations Complete")
    result.value.get.get //Grabs the result out of the future
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

    val order: Array[String] = requestObj.arrival.simulation.flow.ordering.split("->")

    val iTasks: List[ITask] = requestObj.arrival.simulation.flow.tasks

    val orderedTasks: List[ITask] = order.map(x => iTasks.filter(y => y.name == x).head).toList

    val tasks: List[FlowTask] = orderedTasks.map(_.toProterTask()).map(new FlowTask(_))
    
    val taskFlow: Flow = Flow.seq(tasks)

    val i: List[Int] = (1 to 2).toList

    //Make sure to .copy() that tasks
    val sims: List[FlowSimulation] = i.map(x => new FlowSimulation(requestObj.arrival.simulation.name + x.toString(), coord, taskFlow.copy()))

    //val sim: FlowSimulation = new FlowSimulation(requestObj.arrival.simulation.name, coord, taskFlow)
    coord.addSimulationsNow(sims)
    //coord.addSimulationNow(sim)
  }



  def processAggregator(aggregator: SimMetricsAggregator): Results = {
    new Results(
      aggregator.start.get,
      aggregator.end.get,
      aggregator.resourceMetrics.toList,
      aggregator.simulationMetrics.toList,
      aggregator.taskMetrics.toList
    )
  }

}

object Test {

  val sampleInputData = "{ \"Arrival\": {\"Simulation\": {\"Name\": \"Example Name\", \"Flow\":{ \"Tasks\": [{\"Name\": \"Get Order\",\"Duration\": 1,\"Cost\": 0,\"Resources\": \"2\"},{\"Name\": \"Cook\",\"Duration\": 10,\"Cost\": 4,\"Resources\": \"2\"},{\"Name\": \"Serve\",\"Duration\": 1,\"Cost\": 0,\"Resources\": \"2\"}], \"Ordering\": \"A->B->C\"} }, \"Number of Runs\": 10},\"Resources\": [{\"Name\": \"Waiter1\",\"CostPerTick\": 2},{\"Name\": \"Waiter2\",\"CostPerTick\": 2},{\"Name\": \"Chef\",\"CostPerTick\": 5}]}"

  val betterSampleData: String = "{ \"arrival\": {\"simulation\": {\"name\": \"Example Name\", \"flow\":{ \"tasks\": [{\"name\": \"A\",\"duration\": 1,\"cost\": 2,\"resources\": \"R1\"},{\"name\": \"B\",\"duration\": 3,\"cost\": 5,\"resources\": \"R2\"},{\"name\": \"C\",\"duration\": 1,\"cost\": 6,\"resources\": \"R1,R2\"}], \"ordering\": \"A->B->C\"} }, \"numberOfRuns\": 17},\"resources\": [{\"name\": \"R1\",\"costPerTick\": 1},{\"name\": \"R2\",\"costPerTick\": 2}]}"
  
  implicit val requestDecoder1: Decoder[IRequest] = deriveDecoder[IRequest]
  implicit val requestDecoder2: Decoder[IArrival] = deriveDecoder[IArrival]
  implicit val requestDecoder3: Decoder[ISimulation] = deriveDecoder[ISimulation]
  implicit val requestDecoder4: Decoder[IFlow] = deriveDecoder[IFlow]
  implicit val requestDecoder5: Decoder[ITask] = deriveDecoder[ITask]
  implicit val requestDecoder6: Decoder[IResource] = deriveDecoder[IResource]

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