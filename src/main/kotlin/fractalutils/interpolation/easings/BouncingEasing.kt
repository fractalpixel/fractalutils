package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import fractalutils.math.Tau
import kotlin.math.abs
import kotlin.math.pow
import kotlin.math.sin

/**
 * Creates a bouncing stop.
 * NOTE: Some approximation, could do with some fine-tuning for better physics feel.
 */
class BouncingEasing(val bounces: Int = 3,
                     val dampening: Double = 2.0): Easing {
    override fun interpolate(value: Double): Double {
        val t = value
        val d = (1 - t).pow(dampening)
        return 1 - abs(sin(bounces * Tau * (t * t + 0.25)) * d)
    }
}