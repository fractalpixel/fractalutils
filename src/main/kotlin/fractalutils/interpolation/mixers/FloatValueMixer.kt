package fractalutils.interpolation.mixers

import fractalutils.interpolation.ValueMixer

/**
 * Simple value mixer for mixing floats.
 */
class FloatValueMixer: ValueMixer<Float> {
    override fun mix(t: Double, a: Float, b: Float, out: Float?): Float {
        // Can't use mix from mathUtils.kt here as it has the same name as this method..
        return a + t.toFloat() * (b - a)
    }
}