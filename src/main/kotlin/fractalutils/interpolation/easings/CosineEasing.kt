package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import kotlin.math.cos


/**
 * Simple cosine based interpolation, with a smooth start and stop.
 */
object CosineEasing : Easing {

    override fun interpolate(value: Double): Double {
        return 0.5 - 0.5 * cos(value * Math.PI)
    }
}