package com.workflowfm.proter

import cats.effect.std.Random
import cats.Applicative
import cats.implicits.*

/**
  * A random function sampling some probability distribution to produce a `Long` value.
  *
  * It can be used to generate a value for simulation parameters (typically duration). It can be a
  * [[ConstantLong]] which always provides the same value or any probability distribution.
  *
  * Distributions must also provide an estimate of the generated values such that can be used in an
  * environment of imperfect knowledge. For example, the [[schedule.Scheduler Scheduler]] does not
  * know the actual durations of tasks, which can vary from the expected estimate for various
  * reasons.
  */
trait LongDistribution {

  /**
    * Provides a sample `Long` value.
    *
    * @return
    *   A sample `Long` value.
    */
  def getLong[F[_] : Applicative : Random]: F[Long]

  /**
    * Provides an estimate of the values that can be generated.
    *
    * This could be the mean of the distribution for instance. It can help create an environment of
    * imperfect knowledge. For example, the [[schedule.Scheduler Scheduler]] does not know the
    * actual durations of tasks, which can vary from the expected estimate for various reasons.
    *
    * @return
    *   An estimate of the values that can be generated.
    */
  def longEstimate: Long
}

/**
  * A random function sampling some probability distribution to produce a `Double` value.
  *
  * It can be used to generate a value for simulation parameters (typically duration and cost). It
  * can be a [[Constant]] which always provides the same value or any probability distribution.
  *
  * Distributions must also provide an estimate of the generated values such that can be used in an
  * environment of imperfect knowledge. For example, the [[schedule.Scheduler Scheduler]] does not
  * know the actual durations of tasks, which can vary from the expected estimate for various
  * reasons.
  */
trait Distribution extends LongDistribution {

  /**
    * Provides a sample `Double` value.
    *
    * @return
    *   A sample `Double` value.
    */
  def get[F[_] : Applicative : Random]: F[Double]

  /**
    * Provides an estimate of the values that can be generated. This could be the mean of the
    * distribution for instance. It can help create an environment of imperfect knowledge. For
    * example, the [[schedule.Scheduler Scheduler]] does not know the actual durations of tasks,
    * which can vary from the expected estimate for various reasons.
    *
    * @return
    *   An estimate of the values that can be generated.
    */
  def estimate: Double

  /**
    * Provides a sample `Long` value.
    *
    * Simply rounds a `Double` sample.
    *
    * @return
    *   A sample `Long` value.
    */
  override def getLong[F[_] : Applicative : Random]: F[Long] =
    Applicative[F].map(get[F])(_.floor.round)

  /**
    * Provides an estimate of the `Long` values that can be generated.
    *
    * This could be the mean of the distribution for instance. It can help create an environment of
    * imperfect knowledge. For example, the [[schedule.Scheduler Scheduler]] does not know the
    * actual durations of tasks, which can vary from the expected estimate for various reasons.
    *
    * @return
    *   An estimate of the values that can be generated.
    */
  override def longEstimate: Long = estimate.floor.round
}

/**
  * A constant Long value generator.
  *
  * Always generates the same value.
  *
  * @param value
  *   The value to generate.
  */
case class ConstantLong(value: Long) extends LongDistribution {
  import cats.implicits.catsSyntaxApplicativeId

  /**
    * Provides the constant value.
    *
    * @return
    *   The constant value.
    */
  override def getLong[F[_] : Applicative : Random]: F[Long] = Applicative[F].pure(value)

  /**
    * Provides an estimate of the constant value, i.e. the value itself.
    *
    * @return
    *   The constant value as an estimate of itself.
    */
  override def longEstimate = value
}

/**
  * A constant value generator.
  *
  * Always generates the same value.
  *
  * @param value
  *   The value to generate.
  */
case class Constant(value: Double) extends Distribution {

  /**
    * Provides the constant value.
    *
    * @return
    *   The constant value.
    */
  override def get[F[_] : Applicative : Random]: F[Double] = Applicative[F].pure(value)

  /**
    * Provides an estimate of the constant value, i.e. the value itself.
    *
    * @return
    *   The constant value as an estimate of itself.
    */
  override def estimate: Double = value
}

/**
  * A uniform Long distribution.
  *
  * Samples a random variable uniformly between [[min]] and [[max]].
  *
  * @param min
  *   The minimum possible value.
  * @param max
  *   The maximum possible value.
  */
case class UniformLong(min: Long, max: Long) extends LongDistribution {

  /**
    * Provides a random value uniformly sampled between [[min]] and [[max]].
    *
    * @return
    *   The random value.
    */
  override def getLong[F[_] : Applicative : Random]: F[Long] = Random[F].betweenLong(min, max)

  /**
    * Provides an estimate of the values that can be generated.
    *
    * Uses the median of the uniform distribution.
    *
    * @return
    *   The median as an estimate of the values that can be generated.
    */
  override def longEstimate: Long = (max + min) / 2
}

/**
  * A uniform distribution.
  *
  * Samples a random variable uniformly between [[min]] and [[max]].
  *
  * @param min
  *   The minimum possible value.
  * @param max
  *   The maximum possible value.
  */
case class Uniform(min: Double, max: Double) extends Distribution {

  /**
    * Provides a random value uniformly sampled between [[min]] and [[max]].
    *
    * @return
    *   The random value.
    */
  override def get[F[_] : Applicative : Random]: F[Double] = Random[F].betweenDouble(min, max)

  /**
    * Provides an estimate of the values that can be generated.
    *
    * Uses the median of the uniform distribution.
    *
    * @return
    *   The median as an estimate of the values that can be generated.
    */
  override def estimate: Double = (max + min) / 2
}

case class Exponential(mean: Double) extends Distribution {
  import scala.math.log

  /**
    * Provides a random value exponentially sampled with a [[mean]] value (lambda).
    *
    * @return
    *   The random value.
    */
  override def get[F[_] : Applicative : Random]: F[Double] = Random[F].nextDouble.map { rand =>
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
    * Provides an estimate of the values that can be generated. Uses the mean of the exponential
    * distribution.
    *
    * @return
    *   The mean as an estimate of the values that can be generated.
    */
  override def estimate: Double = mean
}
