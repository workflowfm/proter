package com.workflowfm.simulator

import com.workflowfm.simulator._
import scala.concurrent.{ ExecutionContext, Future, Promise }
import akka.actor.{ActorRef, Props}

import org.kie.api.KieBase

import scala.collection.mutable.Map;
import collection.JavaConverters._

import java.io._

import org.kie.api.KieServices;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.manager.RuntimeEngine;
import org.kie.api.runtime.manager.RuntimeEnvironment;
import org.kie.api.runtime.manager.RuntimeEnvironmentBuilder;
import org.kie.api.runtime.manager.RuntimeManager;
import org.kie.api.runtime.manager.RuntimeManagerFactory;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.TaskSummary;
import org.kie.test.util.db.PersistenceUtil;
import scala.concurrent.Future

import scala.io.StdIn.readLine

object BPMN {

    def runScriptProcess(path: String, name: String): Unit = {
        //val manager: RuntimeManager = createManager(path)
        val manager: RuntimeManager = getRuntimeManager(path)
        val runtime: RuntimeEngine = manager.getRuntimeEngine(null)
        val ksession: KieSession = runtime.getKieSession()

        val params: Map[String, Object] = Map[String, Object]()
        params.put("employee", "krisv");
        params.put("reason", "Yearly performance evaluation");

        try { 
            ksession.startProcess(name, params.asJava);

            val taskService: TaskService = runtime.getTaskService();
            val tasks: List[TaskSummary] = taskService.getTasksAssignedAsPotentialOwner("krisv", "en-UK").asScala.toList
            
            val tasks2: List[TaskSummary] = taskService.getTasksAssignedAsBusinessAdministrator("wbadmin", "en-UK").asScala.toList
            //val task = tasks.head
            println("tasks: "+ tasks.length)
            println("tasks2: "+ tasks2.length)
            //System.out.println("'testName' completing task " + task.getName() + ": " + task.getDescription());

        } catch {
            case wfre: Throwable => wfre.printStackTrace(); //WorkflowRuntimeException
        }
        
        manager.close();
    }
  
    ////////// human actor version
    def getRuntimeManager(process: String): RuntimeManager = {
        PersistenceUtil.setupPoolingDataSource();
        val environment: RuntimeEnvironment = RuntimeEnvironmentBuilder.Factory.get().newDefaultBuilder().addAsset(KieServices.Factory.get().getResources().newClassPathResource(process), ResourceType.BPMN2).get();
        return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);
    } /////////

    def createManager(process: String): RuntimeManager = {
    	val environment: RuntimeEnvironment = RuntimeEnvironmentBuilder.Factory.get().newEmptyBuilder()
            .addAsset(KieServices.Factory.get().getResources()
        		.newClassPathResource(process), ResourceType.BPMN2)
            .get();
        return RuntimeManagerFactory.Factory.get().newSingletonRuntimeManager(environment);
    }

    def sayHello(): Unit = {
        System.out.println("hello!")
        var t = readLine()
        Thread.sleep(2000)
        println("got: "+t)
    }
    
    def sayGoodbye(): Unit = {
        System.out.println("goodbye!");
    }

}

class BPMN() {


}

class BPMNSimulationActor(
    name: String,
    coordinator: ActorRef,
    process: String,
    processName: String
)(implicit executionContext: ExecutionContext)
    extends AsyncSimulation(name, coordinator)(executionContext) {

    override def run(): Future[Any] = {
        BPMN.runScriptProcess(process, processName)
        Future.unit
        
    }

}

object BPMNSimulationActor {
    def props(name: String, coordinator: ActorRef, process: String, processName: String)(
        implicit executionContext: ExecutionContext
    ): Props =
    Props(new BPMNSimulationActor(name, coordinator, process, processName))
}