package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing


/**
 * Starts out quickly and de-accelerates very smoothly towards end (mirrored x^4 function) (zero first, second and third derivative at 1).
 */
object SmoothStop4Easing: Easing {
    override fun interpolate(value: Double): Double {
        val invValue = 1.0 - value
        return 1.0 - invValue * invValue * invValue * invValue
    }
}
