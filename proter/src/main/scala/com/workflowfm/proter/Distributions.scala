package com.workflowfm.proter

/**
  * A random function sampling some probability distribution.
  *
  * It can be used to generate a value for some simulation parameters
  * (typically duration and cost). It can be a [[Constant]] which always
  * provides the same value or any probability distribution.
  *
  * Distributions must also provide an estimate of the generated values
  * such that can be used in an environment of imperfect knowledge.
  * For example, the
  * [[schedule.Scheduler Scheduler]] does not know the actual durations of tasks, which can vary from
  * the expected estimate for various reasons.
  */
trait Distribution {

  /**
    * Provides a sample value.
    *
    * @return A sample value.
    */
  def get: Double

  /**
    * Provides an estimate of the values that can be generated.
    * This could be the mean of the distribution for instance.
    * It can help create an environment of imperfect knowledge. For example, the
    * [[schedule.Scheduler Scheduler]] does not know the actual durations of tasks, which can vary from
    * the expected estimate for various reasons.
    *
    * @return An estimate of the values that can be generated.
    */
  def estimate: Double

  /**
    * Provides a sample `Long` value.
    *
    * Simply rounds a `Double` sample.
    *
    * @return A sample `Long` value.
    */
  def getLong: Long = get.round
}

/**
  * A constant value generator.
  *
  * Always generates the same value.
  *
  * @param value The value to generate.
  */
case class Constant(value: Double) extends Distribution {
  /**
    * Provides the constant value.
    *
    * @return The constant value.
    */
  override def get = value

  /**
    * Provides an estimate of the constant value, i.e. the value itself.
    *
    * @return The constant value as an estimate of itself.
    */
  override def estimate = value
}

/**
  * A uniform distribution.
  *
  * Samples a random variable uniformly between [[min]] and [[max]].
  *
  * @param min The minimum possible value.
  * @param max The maximum possible value.
  */
case class Uniform(min: Double, max: Double) extends Distribution {
  /**
    * Provides a random value uniformly sampled between [[min]] and [[max]].
    *
    * @return The random value.
    */
  override def get: Double = new util.Random().nextDouble * (max - min) + min

  /**
    * Provides an estimate of the values that can be generated.
    *
    * Uses the median of the uniform distribution.
    *
    * @return The median as an estimate of the values that can be generated.
    */
  override def estimate: Double = (max + min) / 2
}

case class Exponential(mean: Double) extends Distribution {
  import scala.math.log

  /**
    * Provides a random value exponentially sampled with a [[mean]] value (lambda).
    *
    * @return The random value.
    */
  override def get: Double = {
    val rand = new util.Random().nextDouble
    /* Density function:
     * fx(t) = le^(-lt) , where l is lambda, t is time
     *
     * Cumulative distribution:
     * Fx(t) = 1-e(-lt)
     *
     * given a random number r, let r = Fx(t)
     * => t = -ln(1-r)/l
     * => t = -ln(r)/l , since r is random between 0 and 1 we can use r and 1-r interchangably. */
    -(log(rand) * mean)
  }

  /**
    * Provides an estimate of the values that can be generated.
    * Uses the mean of the exponential distribution.
    *
    * @return The mean as an estimate of the values that can be generated.
    */
  override def estimate: Double = mean
}
