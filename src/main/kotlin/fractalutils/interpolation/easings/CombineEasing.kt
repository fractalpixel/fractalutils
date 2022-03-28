package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import fractalutils.math.map

/**
 * Uses the [start] easing for the part between 0 and [splitPoint],
 * and the [end] easing for the part between [splitPoint] and 1.
 * Does not scale the value in any way before passing it to the easings.
 * @param compressStart if true, the whole of the start interpolator will be used before the split point
 * @param compressEnd if true, the whole of the  end interpolator will be used after the split point
 */
class CombineEasing(val start: Easing,
                    val end: Easing,
                    val splitPoint: Double = 0.5,
                    val compressStart: Boolean = false,
                    val compressEnd: Boolean = false): Easing {
    override fun interpolate(value: Double): Double {
        return if (value < splitPoint) start.interpolate(if (compressStart) map(value, 0.0, splitPoint, 0.0, 1.0) else value)
               else end.interpolate(if (compressEnd) map(value, splitPoint, 1.0, 0.0, 1.0) else value)
    }
}