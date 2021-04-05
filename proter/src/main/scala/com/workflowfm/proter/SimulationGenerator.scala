package com.workflowfm.proter

trait SimulationGenerator {
  protected def manager: Manager

  def get(count: Int): SimulationRef
}

class SingleTaskSimulationGenerator(
    baseName: String,
    override protected val manager: Manager,
    resources: Seq[String],
    duration: ValueGenerator[Long],
    cost: ValueGenerator[Long] = new ConstantGenerator(0L),
    interrupt: Int = (-1),
    priority: Task.Priority = Task.Medium
) extends SimulationGenerator {

  override def get(count: Int): SimulationRef = {
    val name = baseName + count.toString()
    new SingleTaskSimulation(name, manager, resources, duration, cost, interrupt, priority)
  }
}

