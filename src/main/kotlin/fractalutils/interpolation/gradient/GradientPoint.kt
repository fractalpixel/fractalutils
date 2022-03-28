package fractalutils.interpolation.gradient

import fractalutils.interpolation.Easing


/**
 * A point in a Gradient.
 */
interface GradientPoint<T> {

    /**
     * Position of this point in the gradient.
     */
    val pos: Double

    /**
     * Value at this point.
     */
    val value: T

    /**
     * Easing function to use from the previous point to this point.
     */
    val easing: Easing

}
