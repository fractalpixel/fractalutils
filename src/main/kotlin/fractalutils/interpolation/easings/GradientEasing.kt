package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing
import fractalutils.interpolation.gradient.Gradient

/**
 * Uses the gradient to look up interpolation results.
 *
 * A simple linear interpolation can be created by adding a point at 0 in the gradient with a value of 0,
 * and a point at 1 with a value of 1.  Custom interpolation shapes can be created by adding more points and
 * using various interpolators between them.
 */
class GradientEasing(var gradient: Gradient<Double>): Easing {

    override fun interpolate(value: Double): Double {
        return gradient[value]
    }

}