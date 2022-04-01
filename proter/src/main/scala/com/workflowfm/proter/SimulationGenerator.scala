/*
package com.workflowfm.proter

/**
  * A generator of simulation instances.
  *
  * This is used to model repeating processes, for instance with a given arrival rate.
  *
  * It generates copies of a [[SimulationRef]]. It must ensure that each copy has a unique name and
  * is independent from all other copies. An integer counter is provided as an argument for that
  * purpose, though it is not necessary to use that.
  */
trait SimulationRefGenerator {
  /**
    * Generates a new [[SimulationRef]].
    *
    * @param manager
    *   The [[Manager]] of generated [[SimulationRef]].
    * @param count
    *   An integer counter to help construct unique names.
    * @return
    *   A new [[SimulationRef]] instance.
    */
  def build(manager: Manager, count: Int): SimulationRef
}

/**
  * A generator of simulation instances.
  *
  * This is used to model repeating processes, for instance with a given arrival rate.
  *
  * It generates copies of a [[SimulationRef]]. It must ensure that each copy has a unique name and
  * is independent from all other copies. An integer counter is provided as an argument for that
  * purpose, though it is not necessary to use that.
  */
trait SimulationGenerator extends SimulationRefGenerator {
  /**
    * Generates a new [[SimulationRef]].
    *
    * @param manager
    *   The [[Manager]] of generated [[SimulationRef]].
    * @param count
    *   An integer counter to help construct unique names.
    * @return
    *   A new [[SimulationRef]] instance.
    */
  override def build(manager: Manager, count: Int): Simulation
}

/**
  * A [[SimulationGenerator]] for [[SingleTaskSimulation]] instances.
  *
  * @param baseName
  *   The base string (prefix) to use in the name.
  * @param resources
  *   The resources required by the task.
  * @param duration
  *   The duration distribution of the task.
  * @param cost
  *   The cost of the task.
  * @param interrupt
  * @param priority
  *   The priority to be given to the task.
  */
class SingleTaskSimulationGenerator(
    baseName: String,
    resources: Seq[String],
    duration: Distribution,
    cost: Distribution = Constant(0L),
    interrupt: Int = (-1),
    priority: Int = 0
) extends SimulationGenerator {

  /**
    * @inheritdoc
    */
  override def build(manager: Manager, count: Int): Simulation = {
    val name = baseName + count.toString()
    new SingleTaskSimulation(name, manager, resources, duration, cost, interrupt, priority)
  }
}
 */
