package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing

/**
 * Uses 6x^5 - 15x^4 + 10x^3 to interpolate with a smoother S-shaped curve that has a
 * both first and second derivatives of zero at 0 and 1.
 *
 * (Analogous to crossFade of SmoothStart4 and SmoothStop4? (or maybe 5:s?))
 *
 * See variations for https://en.wikipedia.org/wiki/Smoothstep
 */
object SmootherStepEasing: Easing {
    override fun interpolate(value: Double): Double {
        return value * value * value * (value * (value * 6.0 - 15.0) + 10.0)
    }
}