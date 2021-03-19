package com.workflowfm.proter

import spray.json._
import DefaultJsonProtocol._

case class BpmnData(
    id: String,
    duration: ValueGenerator[Long],
    cost: Int,
    priority: Task.Priority,
    resources: List[String]
)


object PriorityFormat extends RootJsonFormat[Task.Priority] {
    // deserialization code
    override def read(json: JsValue): Task.Priority = {
        val priority = json.asInstanceOf[JsString].value
        priority match {
            case "VeryLow" => return Task.VeryLow
            case "Low" => return Task.Low
            case "Medium" => return Task.Medium
            case "High" => return Task.High
            case "Highest" => return Task.Highest
        }
    }

    // serialization code
    override def write(priority: Task.Priority): JsValue = JsString((priority.toString()))
}

object ValueGeneratorFormat extends RootJsonFormat[ValueGenerator[Long]] {
    // serialization code
    override def write(generator: ValueGenerator[Long]): JsValue = {
        if (generator.isInstanceOf[ConstantGenerator[Long]]) {
            return JsObject(
                "type" -> "ConstantGenerator".toJson,
                "value" -> generator.asInstanceOf[ConstantGenerator[Long]].value.toJson
            )
        }
        else {
            return JsObject(
                "type" -> "UniformGenerator".toJson,
                "min" -> generator.asInstanceOf[UniformGenerator].min.toJson,
                "max" -> generator.asInstanceOf[UniformGenerator].max.toJson
            )
        }
        
    }

    // deserialization code
    override def read(json: JsValue): ValueGenerator[Long] = {
        val fields = json.asJsObject("ValueGenerator object expected").fields
        fields("type").convertTo[String] match {
            case "ConstantGenerator" => {
                return ConstantGenerator(fields("value").convertTo[Long])
            } 
            case "UniformGenerator" => {
                return UniformGenerator(fields("min").convertTo[Long], fields("max").convertTo[Long])
            }  
        }
    }
}