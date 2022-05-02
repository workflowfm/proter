package com.workflowfm.proter.flows

import com.workflowfm.proter._

/**
  * Factory for making random flows given some parameters
  *
  * @param andProbability
  *   Probability that a flow node is an [[And]]. Must be in range [0,1]. The inverse probability
  *   yeilds [[Then]] nodes.
  * @param resources
  *   The list of available resources
  * @param numTasks
  *   Distribution determining the number of tasks in the flow. Distribution values must not exceed
  *   `Int.MaxValue` due to truncation during rounding from Long to Int
  * @param durations
  *   Distribution determining task duration.
  * @param numResources
  *   Distribution determining number of resources required by each task. Distribution values must
  *   not exceed `Int.MaxValue` due to truncation during rounding from Long to Int
  * @param cost
  *   Distribution determining cost of a task
  * @param priority
  *   Distribution determining priority of a task
  */
case class RandomFlowFactory(
    andProbability: Float, // balance between and and then nodes
    resources: Seq[TaskResource],
    numTasks: Distribution = Constant(1),
    durations: LongDistribution = ConstantLong(1),
    numResources: Distribution = Constant(1),
    cost: Distribution = Constant(0),
    priority: Distribution = Uniform(-2, 2)
) {

  def withResources(r: Seq[TaskResource]): RandomFlowFactory = copy(resources = r)
  def withTasks(d: Distribution): RandomFlowFactory = copy(numTasks = d)
  def withDurations(d: LongDistribution): RandomFlowFactory = copy(durations = d)
  def withNumResources(d: Distribution): RandomFlowFactory = copy(numResources = d)
  def withRandomPriority(d: Distribution): RandomFlowFactory = copy(priority = d)

  def build(): Flow = {
    val goal: Int = numTasks.getLong.toInt
    randomNode(goal, 1)
  }

  protected def randomNode(num: Int, name: Int): Flow = {
    num match {
      case 1 => randomTask(name)
      case _ => {
        val leftNum = Uniform(1, num - 1).getLong.toInt
        if new util.Random().nextDouble() <= andProbability then {
          new And(randomNode(leftNum, name), randomNode(num - leftNum, name + leftNum))
        } else {
          new Then(randomNode(leftNum, name), randomNode(num - leftNum, name + leftNum))
        }
      }
    }
  }

  protected def randomTask(name: Int): FlowTask = {
    val numberOfResources = Math.round(numResources.get).toInt
    val possibleResources = resources.combinations(numberOfResources).toSeq
    val selectedResources = possibleResources(new util.Random().nextInt(possibleResources.length))
    new FlowTask(
      Task("task" + name.toString, durations.getLong)
        .withResources(selectedResources.map(_.name))
        .withCost(cost.get)
        .withPriority(priority.getLong.toInt)
    )
  }
}
