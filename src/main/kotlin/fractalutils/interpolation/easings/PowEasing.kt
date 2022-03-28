package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import kotlin.math.pow


/**
 * Uses the x^exponent function to fade in and out.
 *
 * Note that this can be a bit slow if used in inner loops, as it uses the pow() function.
 *
 * @param exponent exponent to use for the interpolation curve.  2.0 by default.
 */
class PowEasing(var exponent: Double = 2.0): Easing {

    override fun interpolate(value: Double): Double {
        var t = value
        if (t < 0.5) {
            // Move t to 0..1 range.
            t *= 2.0

            // Return power
            return t.pow(exponent) * 0.5
        } else {
            // Move t to 0..1 range
            t -= 0.5
            t *= 2.0

            // Flip t to 1..0 range
            t = 1.0 - t

            // Return flipped over power
            return 1.0 - t.pow(exponent) * 0.5
        }
    }

    companion object {
        val DEFAULT = PowEasing(2.0)
    }

}
