package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing


/**
 * Starts out very smoothly and accelerates towards end (x^4 function) (zero first, second and third derivative at 0).
 */
object SmoothStart4Easing: Easing {
    override fun interpolate(value: Double): Double {
        return value * value * value * value
    }
}
