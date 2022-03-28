package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing


/**
 * Linear interpolation.
 */
object LinearEasing : Easing {

    override fun interpolate(value: Double): Double {
        return value
    }

}
