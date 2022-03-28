package fractalutils.interpolation.mixers

import fractalutils.interpolation.ValueMixer

/**
 * A value mixer that uses the provided mixing function.
 *
 * @param mutableMixer a mixer that mixes a and b using the mixing ratio t (0 = a, 1 = b, in between = linear mix), and
 *                     stores the result in out if provided.
 *                     a and b should not be changed.
 */
class CustomValueMixer<T>(val mutableMixer: (t: Double, a: T, b: T, out: T?) -> T): ValueMixer<T> {

    /**
     * @param immutableMixer a mixer that mixes a and b using the mixing ratio t (0 = a, 1 = b, in between = linear mix),
     *                       and returns a new instance of T with the result.
     *                       a and b should not be changed.
     */
    constructor(immutableMixer: (t: Double, a: T, b: T) -> T): this({ t, a, b, _ -> immutableMixer(t, a, b)})

    override fun mix(t: Double, a: T, b: T, out: T?): T = mutableMixer(t, a, b, out)
}