package com.workflowfm.proter

/**
  * A generator of simulation instances.
  * 
  * This is used to model repeating processes, for instance with a given arrival rate.
  * 
  * It generates copies of a [[SimulationRef]]. It must ensure that each copy has a unique name and is independent
  * from all other copies. An integer counter is provided as an argument for that purpose, though it is not necessary
  * to use that.
  */
trait SimulationGenerator {
  /**
    * The common [[Manager]] of all generated [[SimulationRef]]s.
    *
    * @return the common [[Manager]] of all generated [[SimulationRef]]s.
    */
  protected def manager: Manager 

  /**
    * Generates a new [[SimulationRef]].
    *
    * @param count An integer counter to help construct unique names.
    * @return A new [[SimulationRef]] instance.
    */
  def get(count: Int): SimulationRef
}


/**
  * A [[SimulationGenerator]] for [[SingleTaskSimulation]] instances.
  *
  * @param baseName The base string (prefix) to use in the name.
  * @param manager The [[Manager]] of the simulations.
  * @param resources The resources required by the task.
  * @param duration The duration of the task.
  * @param cost The cost of the task.
  * @param interrupt 
  * @param priority The priority to be given to the task.
  */
class SingleTaskSimulationGenerator(
    baseName: String,
    override protected val manager: Manager,
    resources: Seq[String],
    duration: ValueGenerator[Long],
    cost: ValueGenerator[Long] = new ConstantGenerator(0L),
    interrupt: Int = (-1),
    priority: Task.Priority = Task.Medium
) extends SimulationGenerator {

  /**
    * @inheritdoc
    */
  override def get(count: Int): SimulationRef = {
    val name = baseName + count.toString()
    new SingleTaskSimulation(name, manager, resources, duration, cost, interrupt, priority)
  }
}

