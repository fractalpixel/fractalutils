package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing

/**
 * Inverts the easing given as parameter using the formula value = 1.0 - [a].interpolate(value)
 */
class FlipEasing(val a: Easing): Easing {
    override fun interpolate(value: Double): Double = 1.0 - a.interpolate(value)
}