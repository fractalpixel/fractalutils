package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing

/**
 * Returns the product of the result of two interpolators (a(value) * b(value)).
 */
class MultiplyEasing(val a: Easing,
                     val b: Easing
): Easing {
    override fun interpolate(value: Double): Double {
        return a(value) * b(value)
    }
}