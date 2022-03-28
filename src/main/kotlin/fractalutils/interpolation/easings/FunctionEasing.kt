package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing

/**
 * Shorthand for defining easings a bit more compactly.
 * Can be used as a base class, or for creating easings out of normal double functions.
 *
 * Also see [fractalutils.math.easing] for a function that creates Easing objects.
 */
open class FunctionEasing(private inline val f: (Double) -> Double): Easing {
    override fun interpolate(value: Double): Double {
        return f(value)
    }
}