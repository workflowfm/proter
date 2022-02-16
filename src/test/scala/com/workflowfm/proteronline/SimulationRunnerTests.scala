package com.workflowfm.proteronline

import org.scalatest.funsuite.AnyFunSuite

class SimulationRunnerTests extends AnyFunSuite {

  test("matchingResources, should identify simple matching resource lists") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3)
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
    assert(simRun.matchingResources(request))
  }

  test("matchingResources, multiple resources per task") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3)
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
    assert(simRun.matchingResources(request))
  }

  test("matchingResources, missing task in defined resources") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
    assert(simRun.matchingResources(request) == false)
  }

  test("matchingResources, unused tasks in specified resources") {
    val simRun = new SimulationRunner()
    val externalResourceList: List[IResource] = List(
      new IResource("R1", 0.4),
      new IResource("R2", 8.3),
      new IResource("R3", 6.2)
    )
    val taskList: List[ITask] = List(
      new ITask("A", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1", 0),
      new ITask("B", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R2", 0),
      new ITask("C", new IDistribution("C", 3.4, None), new IDistribution("C", 3.4, None), "R1,R2", 0)
    )
    val flow: IFlow = new IFlow(taskList, "A->B->C")
    val sim: ISimulation = new ISimulation("Sim Name", flow)
    val arrival = new IArrival(sim, false, new IDistribution("C", 4.3, None), Some(10), None)
    val request: IRequest = new IRequest(arrival, externalResourceList)
    
    assert(simRun.matchingResources(request))
  }

}

/* Old Testing Data, may prove to be useful
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
    val a = obj.streamHandler(req)
    print(a)
  }
}
*/