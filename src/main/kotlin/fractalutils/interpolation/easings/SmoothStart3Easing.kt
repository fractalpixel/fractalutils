package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing


/**
 * Starts out quite smoothly and accelerates towards end (x^3 function) (zero first and second derivative at 0).
 */
object SmoothStart3Easing: Easing {
    override fun interpolate(value: Double): Double {
        return value * value * value
    }
}
