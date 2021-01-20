package com.workflowfm.simulator

import com.workflowfm.simulator._
import scala.concurrent.{ ExecutionContext, Future, Promise }
import akka.actor.{ActorRef, Props}
import java.util.UUID

import scala.collection.mutable.Map;
import scala.util.Random
import collection.JavaConverters._

//import java.io._

import org.camunda.bpm.model.bpmn.Bpmn
import org.camunda.bpm.model.bpmn.instance._
import org.camunda.bpm.model.xml.instance.ModelElementInstance
import org.camunda.bpm.model.xml.Model

//import org.camunda.bpm.engine.impl.javax.el._
import javax.el._

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


class BPMNSimulationActor(
    name: String,
    coordinator: ActorRef,
)(implicit executionContext: ExecutionContext)
    extends Simulation(name, coordinator)(executionContext) {

    val bpmn: BPMN = BPMN.fromPath("process.bpmn")
    //Map from WFM Task ID to BPMN FlowNode ID
    var pendingTasks: Map[UUID,String] = Map()
    val promise = Promise[Any]()

    override def run(): Future[Any] = {
        val startEvent: StartEvent = bpmn.getStartEvent
        handleTasksAfter(startEvent)
        //val taskGenerator: TaskGenerator = makeTaskGenerator(startEvent)
        //pendingTasks += (taskGenerator.id -> startEvent.getId())
        //task(taskGenerator)
        ready()
        promise.future
    }

    override def complete(completedTask: Task, time: Long): Unit = {
        val nodeId = pendingTasks.get(completedTask.id).get
        pendingTasks.remove(completedTask.id)
        handleTasksAfter(nodeId)

        ack(Seq(completedTask.id))
    
    }

    def handleTasksAfter(node: String): Unit = handleTasksAfter(bpmn.getNode(node))

    def handleTasksAfter(node: ModelElementInstance): Unit = {
        val nextEvents = BPMN.getNextEvents(node) 
        if (nextEvents.filter(x => bpmn.isEndEvent(x)).length >= 1) { if (!promise.isCompleted) promise.success(Unit) }
        else nextEvents map (handleNode(_))
    }

    def handleNode(node: FlowNode): Unit = {
        node.getElementType.getTypeName match {
            case y if List("userTask","scriptTask","serviceTask","undefinedTask") contains (y) =>
                {
                    val generator = makeTaskGenerator(node)
                    pendingTasks += (generator.id -> node.getId())
                    task(generator)
                }

            case "exclusiveGateway" =>
                {   
                    val outgoing = node.getOutgoing().asScala
                    if (outgoing.size>1) {
                        // println(node.getOutgoing().asScala.head.getTextContent())
                        // println(node.getOutgoing().asScala.head.getConditionExpression().getTextContent())
                        // //val em = ExpressionManager()
                        // //val ve = em.createValueExpression(node.getOutgoing().asScala.head.getTextContent())


                        // the ExpressionFactory implementation is de.odysseus.el.ExpressionFactoryImpl
                        val factory: ExpressionFactory = new de.odysseus.el.ExpressionFactoryImpl();
                        // package de.odysseus.el.util provides a ready-to-use subclass of ELContext
                        val context: de.odysseus.el.util.SimpleContext = new de.odysseus.el.util.SimpleContext();
                        // // map function math:max(int, int) to java.lang.Math.max(int, int)
                        // context.setFunction("math", "max", classOf[Math].getMethod("max", classOf[Int], classOf[Int]));
                        // // map variable foo to 0
                        // context.setVariable("foo", factory.createValueExpression(0, classOf[Int]));
                        // // parse our expression
                        // val e: ValueExpression = factory.createValueExpression(context, "${math:max(foo,bar)}", classOf[Int]);
                        // // set value for top-level property "bar" to 1
                        // factory.createValueExpression(context, "${bar}", classOf[Int]).setValue(context, 1);
                        // // get value for our expression
                        // System.out.println(e.getValue(context)); // --> 1


                        context.setVariable("foo", factory.createValueExpression("barr", classOf[String]))

                        val theOne = outgoing.find{ x=>
                            val e: ValueExpression = factory.createValueExpression(context, x.getConditionExpression().getTextContent(), classOf[Boolean])
                            val result: Boolean = e.getValue(context).asInstanceOf[Boolean]
                            result
                        }
                    
                        theOne match {
                            case None => println("ERROR: non-exhaustive or non-boolean conditions in gateway")
                            case Some(one) => handleNode(one.getTarget())
                        }
                    }
                    else {
                        handleNode(BPMN.getNextEvents(node).head)
                    }
                    

                    //ConditionExpression conditionExpression = (ConditionExpression) flow4.getUniqueChildElementByType(ConditionExpression.class);

                    //val rnd = new util.Random().nextInt(node.getOutgoing().size())
                    //handleNode(BPMN.getNextEvents(node)(rnd))
                }

            case "parallelGateway" =>
                {
                    handleTasksAfter(node.getId())
                }

            case y => println("ERROR: unhandled node type: " + y ) 

        }
    }

    def makeTaskGenerator(node: FlowNode): TaskGenerator = {
        println(node.getElementType().getTypeName())
        val nodeId = node.getId()
        println(nodeId)
        val bpmnData: BpmnData = bpmn.bpmnDataset.data.find(b => b.id == nodeId).getOrElse(BpmnData("notFound",5,0,List()))
        TaskGenerator(node.getName(),"sim1",ConstantGenerator(bpmnData.duration),ConstantGenerator(bpmnData.cost)).withResources(bpmnData.resources)
    }

}

object BPMNSimulationActor {
    def props(name: String, coordinator: ActorRef)(
        implicit executionContext: ExecutionContext
    ): Props =
    Props(new BPMNSimulationActor(name, coordinator))
}