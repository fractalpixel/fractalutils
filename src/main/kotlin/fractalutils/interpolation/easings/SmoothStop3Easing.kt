package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing


/**
 * Starts out quickly and de-accelerates quite smoothly towards end (mirrored x^3 function) (zero first and second derivative at 1).
 */
object SmoothStop3Easing: Easing {
    override fun interpolate(value: Double): Double {
        val invValue = 1.0 - value
        return 1.0 - invValue * invValue * invValue
    }
}
