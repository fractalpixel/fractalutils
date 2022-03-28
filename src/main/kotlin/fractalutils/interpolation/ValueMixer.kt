package fractalutils.interpolation

import fractalutils.interpolation.easings.LinearEasing
import fractalutils.math.map

/**
 * Something that can mix two values of the specified type.
 */
interface ValueMixer<T> {

    /**
     * @param t 0 = return value of a, 1 = return value of b,
     *          0..1 = linearly interpolate between a and b and return the result in out and as return value.
     * @param a value for t value of 0.  This value should not be changed by this function if it is mutable.
     * @param b value for t value of 1.  This value should not be changed by this function if it is mutable.
     * @param out if T is a mutable type, the result of the interpolation should be written to this object, and this object returned as result.
     *            If T is not mutable (e.g. primitive numbers), this should be null (the default value).
     * @return the interpolated value (as the out object, if it is mutable).
     */
    fun mix(t: Double, a: T, b: T, out: T? = null): T

    /**
     * @param t 0 = return value of a, 1 = return value of b,
     *          0..1 = linearly interpolate between a and b and return the result in out and as return value.
     * @param easing an easing to use to determine the final mixing ratio (applied to t before it is used).
     * @param a value for t value of 0.  This value should not be changed by this function if it is mutable.
     * @param b value for t value of 1.  This value should not be changed by this function if it is mutable.
     * @param out if T is a mutable type, the result of the interpolation should be written to this object, and this object returned as result.
     *            If T is not mutable (e.g. primitive numbers), this should be null (the default value).
     * @return the interpolated value (as the out object, if it is mutable).
     */
    fun mix(t: Double, easing: Easing, a: T, b: T, out: T? = null): T {
        val pos = easing.interpolate(t)
        return mix(pos, a, b, out)
    }

    /**
     * @param t input value to use for mixing the values.
     * @param sourceStart the value for t where targetStart is returned
     * @param sourceEnd the value for t where targetEnd is returned
     * @param targetStart value for t value of sourceStart.  This value should not be changed by this function if it is mutable.
     * @param targetEnd value for t value of sourceEnd.  This value should not be changed by this function if it is mutable.
     * @param clamp if true, limits the input value t to be between sourceStart and sourceEnd.
     * @param easing an easing to use to determine the final mixing ratio.  Defaults to a linear interpolation.
     * @param out if T is a mutable type, the result of the interpolation should be written to this object, and this object returned as result.
     *            If T is not mutable (e.g. primitive numbers), this should be null (the default value).
     * @return the interpolated value (as the out object, if it is mutable).
     */
    fun mapAndMix(t: Double,
                  sourceStart: Double,
                  sourceEnd: Double,
                  targetStart: T,
                  targetEnd: T,
                  clamp: Boolean = false,
                  easing: Easing = LinearEasing,
                  out: T? = null): T {
        val pos = map(t, sourceStart, sourceEnd, 0.0, 1.0, clamp, easing)
        return mix(pos, targetStart, targetEnd, out)
    }

    /**
     * Shorthand for assigning a value to the out variable, and returning it.
     */
    fun assign(value: T, out: T?): T {
        return mix(0.0, value, value, out)
    }

}
