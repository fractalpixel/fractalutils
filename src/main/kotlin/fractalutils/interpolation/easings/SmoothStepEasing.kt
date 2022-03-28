package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing

/**
 * Uses 3x^2 - 2x^3 (or more optimally expressed x*x*(3-2*x)), to interpolate with a smooth S-shaped curve that has a
 * derivative of zero at 0 and 1.
 *
 * (Analogous to crossFade of SmoothStart2 and SmoothStop2)
 *
 * See https://en.wikipedia.org/wiki/Smoothstep
 */
object SmoothStepEasing: Easing {
    override fun interpolate(value: Double): Double {
        return value * value * (3.0 - 2.0 * value)
    }
}