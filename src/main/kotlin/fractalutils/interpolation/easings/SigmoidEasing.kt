package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import fractalutils.math.sigmoidZeroToOne

/**
 * A tunable sigmoid type interpolator.
 *
 * The input value is automatically clamped to the 0..1 range.
 *
 * @param sharpness the sharpness of the curve, in the range -1..1.
 *                  When 0, the result is equal to the input,
 *                  when it goes towards 1 the S shape gets sharper (more output directed towards 0 or 1, less towards 0.5).
 *                  If negative, it will produce an inverse S curve that gets shallower towards 0 (more output directed towards 0.5, less towards 0 or 1).
 *                  Defaults to 0.5, for a traditional S shaped sigmoid curve.
 **/
class SigmoidEasing(val sharpness: Double = 0.5): Easing {
    override fun interpolate(value: Double): Double {
        return sigmoidZeroToOne(value, sharpness)
    }
}