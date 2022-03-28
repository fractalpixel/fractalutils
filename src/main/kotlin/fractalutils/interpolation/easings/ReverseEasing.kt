package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing


/**
 * Reverses the interpolator given as parameter using the formula value = [a].interpolate(1.0 - value)
 */
class ReverseEasing (val a: Easing): Easing {
    override fun interpolate(value: Double): Double = a.interpolate(1.0 - value)
}