package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing

/**
 * Re-maps the input and output range of the interpolator function.
 */
class MappedEasing(val a: Easing,
                   val inputStart: Double = 0.0,
                   val inputEnd: Double = 1.0,
                   val outputStart: Double = 0.0,
                   val outputEnd: Double = 1.0,
                   val clampInput: Boolean = false): Easing {
    override fun interpolate(value: Double): Double {
        return a.interpolate(value, inputStart, inputEnd, outputStart, outputEnd, clampInput)
    }
}