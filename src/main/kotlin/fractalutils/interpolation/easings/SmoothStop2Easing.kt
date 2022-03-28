package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing


/**
 * Starts out quickly and de-accelerates fairly smoothly towards end (mirrored x^2 function) (zero first derivative at 1).
 */
object SmoothStop2Easing: Easing {
    override fun interpolate(value: Double): Double {
        val invValue = 1.0 - value
        return 1.0 - invValue * invValue
    }
}
