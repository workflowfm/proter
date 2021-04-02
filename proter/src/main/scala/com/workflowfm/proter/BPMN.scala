package com.workflowfm.proter

import java.util.UUID

import scala.collection.mutable.Map;
import collection.JavaConverters._

//import java.io._

import org.camunda.bpm.model.bpmn.Bpmn
import org.camunda.bpm.model.bpmn.instance._
import org.camunda.bpm.model.xml.instance.ModelElementInstance
//import org.camunda.bpm.model.xml.Model

//import org.camunda.bpm.engine.impl.javax.el._
//import javax.el._

import spray.json._


case class BpmnDataset(data: List[BpmnData])

/**
  * So this chunk of code is particularly nasty but it's necessary to
  * make an object and import it like so so that the json protocol can be 
  * used throughout.
  * 
  * BpmnDataJsonProtocol e
  */
object BpmnDataJsonProtocol extends DefaultJsonProtocol {
    implicit val ValueGeneratorProtocol = ValueGeneratorFormat
    implicit val PriorityProtocol = PriorityFormat
    implicit val bpmnFormat = jsonFormat5(BpmnData)
    implicit val bpmnDatasetFormat = jsonFormat1(BpmnDataset)
    
}
import BpmnDataJsonProtocol._


class BPMN(path: String, data: String) {
    val dataSource = scala.io.Source.fromFile(data)
    val source = try dataSource.mkString finally dataSource.close()
    val bpmnDataset: BpmnDataset = source.parseJson.convertTo[BpmnDataset]

    var modelInstance = Bpmn.readModelFromStream(
            getClass().getClassLoader().getResourceAsStream(path));

    def getStartEvent: StartEvent = modelInstance.getModelElementsByType(classOf[StartEvent]).toArray()(0).asInstanceOf[StartEvent]

    def isEndEvent(i: ModelElementInstance): Boolean = {
        modelInstance.getModelElementsByType(classOf[EndEvent]).toArray()(0).asInstanceOf[EndEvent] == i
    }

    def isEndEvent(i: String): Boolean = {
        modelInstance.getModelElementsByType(classOf[EndEvent]).toArray()(0).asInstanceOf[EndEvent] == modelInstance.getModelElementById(i)
    }

    def getNode(id: String): ModelElementInstance = {
        modelInstance.getModelElementById(id)
    }

}


object BPMN {
    def fromPath(path: String, json: String): BPMN = new BPMN(path, json)

    def getNextEvents(i: ModelElementInstance): List[FlowNode] = {
        i.asInstanceOf[FlowNode].getSucceedingNodes().list().asScala.toList
    }
}


class BPMNSimulation(
    override val name: String,
    override protected val manager: Manager,
    val src: String,
    val json: String
) extends Simulation {

    val bpmn: BPMN = BPMN.fromPath(src, json)
    //Map from WFM Task ID to BPMN FlowNode ID
    var pendingTasks: Map[UUID,String] = Map()
    //val promise = Promise[Any]()
    var parallelGateways = Map[String, ParallelGateway]()

    override def stop(): Unit = Unit

    override def run(): Unit = {
        val startEvent: StartEvent = bpmn.getStartEvent
        handleTasksAfter(startEvent)
        ready()
    }

    override def complete(task: TaskInstance, time: Long): Unit = {
        val nodeId = pendingTasks.get(task.id).get
        pendingTasks.remove(task.id)
        handleTasksAfter(nodeId)
        ack(Seq(task.id))
    }

    protected def handleTasksAfter(node: String): Unit = handleTasksAfter(bpmn.getNode(node))

    protected def handleTasksAfter(node: ModelElementInstance): Unit = {
        val nextEvents = BPMN.getNextEvents(node) 
        if (nextEvents.filter(x => bpmn.isEndEvent(x)).length >= 1) { succeed(Unit) }
        else {
            val t = node.asInstanceOf[FlowNode].getOutgoing()
            t.asScala map {x=> handleNode(x.getTarget(),x.getId())}
            //nextEvents map (handleNode(_))
        }
    }

    protected def handleNode(node: FlowNode, incomingID: String): Unit = {
        node.getElementType.getTypeName match {
            case y if List("userTask","scriptTask","serviceTask","undefinedTask","task") contains (y) =>
                {
                    val theTask = makeTask(node)
                    val newID = UUID.randomUUID()
                    pendingTasks += (newID -> node.getId())
                    task(theTask.withID(newID))
                }

            case "exclusiveGateway" =>
                {   
                    val rnd = new util.Random().nextInt(node.getOutgoing().size())
                    val t = node.asInstanceOf[FlowNode].getOutgoing().asScala
                    val connector = t.toSeq(rnd)
                    handleNode(connector.getTarget(),connector.getId)
                }

            case "parallelGateway" =>
                {   
                    val incomingSize = node.getIncoming.size()
                    if (incomingSize==1) handleTasksAfter(node.getId())
                    else {
                        var gateway: ParallelGateway = null
                        if (parallelGateways.contains(node.getId())) gateway = parallelGateways.get(node.getId()).get
                        else {
                            gateway = new ParallelGateway(node.getId(), incomingSize)
                            parallelGateways += (node.getId() -> gateway)
                        }
                        gateway.addToken(incomingID)
                        if (gateway.tryToProgress()) handleTasksAfter(node)
                    }
                }

            case y => println("ERROR: unhandled node type: " + y ) 

        }
    }

    def makeTask(node: FlowNode): Task = {
        println(node.getElementType().getTypeName())
        val nodeId = node.getId()
        println(nodeId)
        val bpmnData: BpmnData = bpmn.bpmnDataset.data.find(b => b.id == nodeId).getOrElse(BpmnData("notFound",ConstantGenerator(5),0,Task.Low,List())) //Throw an error properly
        Task(node.getName(),None,bpmnData.duration,ConstantGenerator(bpmnData.cost)).withResources(bpmnData.resources)
    }

}

class ParallelGateway(id: String, inputs: Int) {
    var inputTokens = Map[String, Int]()
    def addToken(input: String): Unit = {
        if (inputTokens.contains(input)) {
            val old = inputTokens(input)
            inputTokens.update(input, old+1)
        }
        else {
            inputTokens += (input -> 1)
        }
    }
    def tryToProgress(): Boolean = {
        if ( inputTokens.values.count(_>=1)==inputs ) {
            inputTokens map { x=>
                inputTokens.update(x._1, x._2-1)
            }
            true
        }
        else false
    } 
}