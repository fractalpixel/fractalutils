package fractalutils.interpolation.mixers

import fractalutils.interpolation.ValueMixer

/**
 * Mixes any two numerical values.
 * Returns a double result.
 */
class NumericValueMixer(): ValueMixer<Number> {

    override fun mix(t: Double, a: Number, b: Number, out: Number?): Number {
        val aValue = a.toDouble()
        val bvalue = b.toDouble()
        return aValue + t * (bvalue - aValue)
    }

}