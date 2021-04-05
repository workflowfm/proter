package com.workflowfm.proter

/**
  * Represents a function that can generate a value for some simulation parameters
  * (typically duration and cost). It can be a [[ConstantGenerator]] which always
  * provides the same value or any probability distribution.
  *
  * ValueGenerators must also implement an estimate method
  * (such as the median of the distribution) that provides an estimate of the generated
  * values.
  * This can help create an environment of imperfect knowledge. For example, the
  * [[Scheduler]] does not know the actual durations of tasks, which can vary from
  * the expected estimate for various reasons.
  */
trait ValueGenerator[T] {

  /**
    * Provides a sample value.
    *
    * @return A sample value.
    */
  def get: T

  /**
    * Provides an estimate of the values that can be generated.
    * This could be the median of the distribution for instance.
    * It can help create an environment of imperfect knowledge. For example, the
    * [[Scheduler]] does not know the actual durations of tasks, which can vary from
    * the expected estimate for various reasons.
    *
    * @return An estimate of the values that can be generated.
    */
  def estimate: T
}

/**
  * A constant value generator.
  * Always generates the same value.
  *
  * @param value The value to generate.
  */
case class ConstantGenerator[T](value: T) extends ValueGenerator[T] {
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
  * A uniform distribution generator. Uses Integers
  * Samples a random variable uniformly between [[min]] and [[max]].
  *
  * @param min The minimum possible value.
  * @param max The maximum possible value.
  */
case class IntUniformGenerator(min: Int, max: Int) extends ValueGenerator[Int] {
  /**
    * Provides a random value uniformly sampled between [[min]] and [[max]].
    *
    * @return The random value.
    */
  override def get: Int = new util.Random().nextInt(max - min) + min

  /**
    * Provides an estimate of the values that can be generated.
    * Uses the median of the uniform distribution.
    *
    * @return The median as an estimate of the values that can be generated.
    */
  override def estimate: Int = (max + min) / 2
}

/**
  * A uniform distribution generator. Uses Longs
  * Samples a random variable uniformly between [[min]] and [[max]].
  *
  * @param min The minimum possible value.
  * @param max The maximum possible value.
  */
case class UniformGenerator(min: Long, max: Long) extends ValueGenerator[Long] {
  /**
    * Provides a random value uniformly sampled between [[min]] and [[max]].
    *
    * @return The random value.
    */
  override def get: Long = (new util.Random().nextDouble * (max - min) + min).toLong
  /**
    * Provides an estimate of the values that can be generated.
    * Uses the median of the uniform distribution.
    *
    * @return The median as an estimate of the values that can be generated.
    */
  override def estimate: Long = (max + min) / 2
}


case class ExponentialGenerator(mean: Double) extends ValueGenerator[Double] {
  import scala.math.log

  /**
    * Provides a random value exponentially sampled with a [[mean]] value (lambda).
    *
    * @return The random value.
    */
  override def get: Double = {
    val rand = new util.Random().nextDouble
    /*
     * Density function:
     *  fx(t) = le^(-lt) , where l is lambda, t is time
     *  
     * Cumulative distribution:
     * Fx(t) = 1-e(-lt)
     * 
     * given a random number r, let
     * r = Fx(t)
     * => t = -ln(1-r)/l
     * => t = -ln(r)/l , since r is random between 0 and 1 we can use r and 1-r interchangably.
     */
    -(log(rand)/mean)
  }

  /**
    * Provides an estimate of the values that can be generated.
    * Uses the mean of the exponential distribution.
    *
    * @return The mean as an estimate of the values that can be generated.
    */
  override def estimate: Double = mean
}
