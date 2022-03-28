package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import fractalutils.math.mix

/**
 * Blends the result two easings using some constant blending factor (by default 0.5).
 * @param t if 0, use a, if 1, use b, otherwise blend.
 */
class BlendEasing(val a: Easing,
                  val b: Easing,
                  val t: Double = 0.5): Easing {
    override fun interpolate(value: Double): Double {
        return mix(t, a(value), b(value))
    }
}