package com.workflowfm.proter

import java.util.UUID

import scala.annotation.tailrec
import scala.collection.mutable.{ HashSet, Map, PriorityQueue }
import scala.concurrent.{ ExecutionContext, Future, Promise }
import scala.util.{ Try, Success, Failure }

import com.workflowfm.proter.events.*
//import com.workflowfm.proter.schedule.Scheduler

/*


  /**
    * Sets or updates the [[Lookahead]] structure for a given simulation.
    *
    * @group simulations
    * @param simulation
    *   The name of the [[SimulationRef]].
    * @param lookahead
    *   The new [[Lookahead]] structure.
    */
  protected def setLookahead(simulation: String, lookahead: Lookahead): Unit = {
    scheduler.setLookahead(simulation, lookahead)
  }


  /**
    * Adds a new infinite arrival process to the coordinator at the next arrival time.
    *
    * @note
    *   This will generate new simulations infinitely. The [[Coordinator]] will not terminate unless
    *   you add a [[TimeLimit]] event via [[limit]].
    *
    * @param rate
    *   The arrival rate of the simulation instances.
    * @param simulationGenerator
    *   The generator used to create new instances.
    */
  def addInfiniteArrivalNext(
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    events += Arrival(time + rate.get.round, rate, simulationGenerator)
  }

  /**
    * Adds a new arrival process to the coordinator at the next arrival time.
    *
    * @param limit
    *   The number of simulation instances to spawn.
    * @param rate
    *   The arrival rate of the simulation instances.
    * @param simulationGenerator
    *   The generator used to create new instances.
    */
  def addArrivalNext(
      limit: Int,
      rate: Distribution,
      simulationGenerator: SimulationRefGenerator
  ): Unit = {
    events += Arrival(time + rate.get.round, rate, simulationGenerator, Some(limit))
  }


 */
