package fractalutils.interpolation

import fractalutils.interpolation.easings.LinearEasing
import fractalutils.math.map

/**
 * An interface that indicates that a type knows how to mix itself with other instances of the same type.
 */
interface Mixable<T> {

    /**
     * Returns a linear mix of this value and the other.  Neither this or the other should be changed, instead a new value should be returned.
     * If this class is mutable and a resultOut value is provided, the mixed value should be stored in resultOut and resultOut returned,
     * instead of creating a new instance of this type.
     *
     * @param other value to mix with.  This value should not be changed by this function if it is mutable.
     * @param mixAmount 0 = return value of this, 1 = return value of other,
     *          0..1 = linearly interpolate between this and the other value and return the result in resultOut and as return value.
     * @param resultOut if T is a mutable type and resultOut is not null, the result of the interpolation should be written to this object, and this object returned as result.
     * @return the mixed value (using the resultOut instance, if it is provided and mutable).
     */
    fun mix(other: T, mixAmount: Double = 0.5, resultOut: T? = null): T

    /**
     * Returns an interpolation of this value and the other.  Neither this or the other should be changed, instead a new value should be returned.
     * If this class is mutable and a resultOut value is provided, the mixed value should be stored in resultOut and resultOut returned,
     * instead of creating a new instance of this type.
     *
     * @param other value to mix with.  This value should not be changed by this function if it is mutable.
     * @param mixAmount 0 = return value of this, 1 = return value of other,
     *          0..1 = linearly interpolate between this and the other value and return the result in resultOut and as return value.
     * @param easing an easing to use to determine the final mixing ratio.
     * @param resultOut if T is a mutable type and resultOut is not null, the result of the interpolation should be written to this object, and this object returned as result.
     * @return the mixed value (using the resultOut instance, if it is provided and mutable).
     */
    fun mix(other: T, mixAmount: Double, easing: Easing, resultOut: T? = null): T {
        val pos = easing.interpolate(mixAmount)
        return mix(other, pos, resultOut)
    }


    /**
     * @param mixPos input value to use for mixing the values.
     * @param thisPos the value for mixPos where this is returned
     * @param otherPos the value for mixPos where other is returned
     * @param clamp if true, limits the input value source to be between thisPos and otherPos.
     * @param easing an easing to use to determine the final mixing ratio.  Defaults to a linear interpolation.
     * @param out if T is a mutable type, the result of the interpolation should be written to this object, and this object returned as result.
     *            If T is not mutable (e.g. primitive numbers), this should be null (the default value).
     * @return the interpolated value (as the out object, if it is mutable).
     */
    fun mapAndMix(other: T,
                  mixPos: Double,
                  thisPos: Double,
                  otherPos: Double,
                  clamp: Boolean = false,
                  easing: Easing = LinearEasing,
                  out: T? = null): T {
        val t = map(mixPos, thisPos, otherPos, 0.0, 1.0, clamp, easing)
        return this.mix(other, t, out)
    }

}