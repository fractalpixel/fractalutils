package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import fractalutils.math.Tau
import fractalutils.math.mixTo
import kotlin.math.pow
import kotlin.math.sin

/**
 * Creates an elastic stop.
 * NOTE: Could perhaps tune to make the start longer, making it easier to cross-fade with other interpolators.
 */
class ElasticEasing(val bounces: Int = 3,
                    val dampening: Double = 10.0): Easing {
    override fun interpolate(value: Double): Double {
        val t = 1.0 - value
        return 1.0 - sin(Tau * ((bounces + 0.25) * t)) * 2.0.pow(t.mixTo(-dampening, 0.0))
    }
}