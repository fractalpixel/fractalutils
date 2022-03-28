package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing


/**
 * Inverts the interpolator given as parameter by both flipping and reversing it,
 * using the formula value = 1.0 - [a].interpolate(1.0 - value)
 */
class InvertEasing (val a: Easing): Easing {
    override fun interpolate(value: Double): Double = 1.0 - a.interpolate(1.0 - value)
}