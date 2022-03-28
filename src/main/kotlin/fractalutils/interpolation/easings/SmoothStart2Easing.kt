package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing


/**
 * Starts out fairly smoothly and accelerates towards end (x^2 function) (zero first derivative at 0).
 */
object SmoothStart2Easing: Easing {
    override fun interpolate(value: Double): Double {
        return value * value
    }
}
