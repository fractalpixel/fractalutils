package fractalutils.interpolation.gradient

import fractalutils.checking.Check
import fractalutils.interpolation.Easing
import fractalutils.interpolation.easings.LinearEasing

/**
 * Mutable implementation of GradientPoint.
 */
class MutableGradientPoint<T>(override var pos: Double,
                              override var value: T,
                              override var easing: Easing = LinearEasing
) : GradientPoint<T> {

    init {
        Check.normalNumber(pos, "pos")
    }

    /**
     * @return true if the position changed.
     */
    fun setPos(pos: Double): Boolean {
        Check.normalNumber(pos, "pos")

        return if (pos != this.pos) {
            this.pos = pos
            true
        } else {
            false
        }
    }
}
