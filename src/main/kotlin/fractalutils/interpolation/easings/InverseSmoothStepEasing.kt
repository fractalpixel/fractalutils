package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import kotlin.math.asin
import kotlin.math.sin

/**
 * Like [SmoothStepEasing], but instead of an S curve, it's an N-like smooth curve
 * (starts straight upwards, planes out, then finishes straight upwards).
 *
 * NOTE: Perhaps crossFade of mirrored smoothStart and smoothEnd could be used instead?  To avoid two trig functions?
 *
 * See https://iquilezles.org/www/articles/ismoothstep/ismoothstep.htm for the derivation.
 */
object InverseSmoothStepEasing: Easing {
    override fun interpolate(value: Double): Double {
        return 0.5 - sin(asin(1.0 - 2.0 * value) / 3.0)
    }
}