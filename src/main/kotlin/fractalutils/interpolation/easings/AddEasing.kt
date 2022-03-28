package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing

/**
 * Returns the sum of the result of two easings (a(value) + b(value)).
 */
class AddEasing(val a: Easing,
                val b: Easing
): Easing {
    override fun interpolate(value: Double): Double {
        return a(value) + b(value)
    }
}