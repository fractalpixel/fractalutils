package fractalutils.interpolation.mixers

import fractalutils.interpolation.Mixable
import fractalutils.interpolation.ValueMixer

/**
 * A ValueMixer that works with a class that implements Mixable.
 */
class MixableValueMixer<T: Mixable<Any>>: ValueMixer<T> {

    @Suppress("UNCHECKED_CAST")
    override fun mix(t: Double, a: T, b: T, out: T?): T = a.mix(b, t, out) as T

}