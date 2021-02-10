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

case class BpmnData(
    id: String,
    duration: Int,
    cost: Int,
    resources: List[String]
)

case class BpmnDataset(data: List[BpmnData])
object BpmnDataJsonProtocol extends DefaultJsonProtocol {
    implicit val bpmnFormat = jsonFormat4(BpmnData) }
import BpmnDataJsonProtocol._
object BpmnDatasetJsonProtocol extends DefaultJsonProtocol {
    implicit val bpmnDatasetFormat = jsonFormat1(BpmnDataset) }
import BpmnDatasetJsonProtocol._


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
    def fromPath(path: String): BPMN = new BPMN(path, "modelData.json")

    def getNextEvents(i: ModelElementInstance): List[FlowNode] = {
        i.asInstanceOf[FlowNode].getSucceedingNodes().list().asScala.toList
    }
}


class BPMNSimulation(
    override val name: String,
    override protected val manager: Manager,
) extends Simulation {

    val bpmn: BPMN = BPMN.fromPath("process.bpmn")
    //Map from WFM Task ID to BPMN FlowNode ID
    var pendingTasks: Map[UUID,String] = Map()
    //val promise = Promise[Any]()

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
        else nextEvents map (handleNode(_))
    }

    protected def handleNode(node: FlowNode): Unit = {
        node.getElementType.getTypeName match {
            case y if List("userTask","scriptTask","serviceTask","undefinedTask") contains (y) =>
                {
                    val theTask = makeTask(node)
                    val newID = UUID.randomUUID()
                    pendingTasks += (newID -> node.getId())
                    task(theTask.withID(newID))
                }

            case "exclusiveGateway" =>
                {   
                    val rnd = new util.Random().nextInt(node.getOutgoing().size())
                    handleNode(BPMN.getNextEvents(node)(rnd))
                }

            case "parallelGateway" =>
                {
                    handleTasksAfter(node.getId())
                }

            case y => println("ERROR: unhandled node type: " + y ) 

        }
    }

    def makeTask(node: FlowNode): Task = {
        println(node.getElementType().getTypeName())
        val nodeId = node.getId()
        println(nodeId)
        val bpmnData: BpmnData = bpmn.bpmnDataset.data.find(b => b.id == nodeId).getOrElse(BpmnData("notFound",5,0,List())) //Throw an error properly
        Task(node.getName(),None,ConstantGenerator(bpmnData.duration),ConstantGenerator(bpmnData.cost)).withResources(bpmnData.resources)
    }

}