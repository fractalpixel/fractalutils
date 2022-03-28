package fractalutils.interpolation.mixers

import fractalutils.interpolation.ValueMixer

/**
 * A value mixer that just returns one or the other value depending on whether the mix ratio is under or over 0.5 (or some other threshold).
 * @param threshold the threshold for flipping over from one value to the other.  Defaults to 0.5.  Possible range is 0..1
 */
class BinaryValueMixer<T>(val threshold: Double = 0.5) : ValueMixer<T> {

    override fun mix(t: Double, a: T, b: T, out: T?): T = if (t < threshold) a else b

}