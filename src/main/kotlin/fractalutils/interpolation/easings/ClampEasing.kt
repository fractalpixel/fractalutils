package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import fractalutils.math.clamp

/**
 * Delegates to the given interpolator, but clamps the input parameters to the start..end range (0 to 1 by default).
 */
class ClampEasing(val baseEasing: Easing,
                  val start: Double = 0.0,
                  val end: Double = 1.0): Easing {
    override fun interpolate(value: Double): Double {
        return baseEasing(clamp(value, start, end))
    }
}