package fractalutils.interpolation.mixers

import fractalutils.interpolation.ValueMixer

/**
 * Simple value mixer for mixing doubles.
 */
class DoubleValueMixer: ValueMixer<Double> {
    override fun mix(t: Double, a: Double, b: Double, out: Double?): Double {
        // Can't use mix from mathUtils.kt here as it has the same name as this method..
        return a + t * (b - a)
    }
}
