package fractalutils.interpolation.easings

import fractalutils.interpolation.Easing

/**
 * Mixes two easings, using one for the start and another for the end, with a third easing
 * specifying which easing is used (by default a linear easing).
 */
class CrossFadeEasing(val start: Easing,
                      val end: Easing,
                      val mixer: Easing = LinearEasing,
                      val clampMix: Boolean = false): Easing {
    override fun interpolate(value: Double): Double {
        return mixer.interpolate(
            value,
            start.interpolate(value),
            end.interpolate(value),
            clampMix)
    }
}