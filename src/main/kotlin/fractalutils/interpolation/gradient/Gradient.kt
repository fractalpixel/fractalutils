package fractalutils.interpolation.gradient

import fractalutils.interpolation.Easing
import fractalutils.interpolation.ValueMixer
import fractalutils.interpolation.easings.LinearEasing
import fractalutils.math.DoubleFun


/**
 * Interpolates between objects placed at specified points, using specified easings between each point.
 * Uses a ValueInterpolator to do the actual mixing between two values.
 */
interface Gradient<T> : DoubleFun<T> {

    /**
     * Used for interpolating between the values in the gradient.
     */
    var valueMixer: ValueMixer<T>

    /**
     * @param pos position to add the point at.
     * @param value value at the point.
     * @param easing easing function to use between the previous point and this point.  If not specified, uses the default.
     */
    fun addPoint(pos: Double, value: T, easing: Easing? = null)

    /**
     * Number of points in this gradient.
     */
    val size: Int

    /**
     * @return information for the point closest to the specified position, or null if no points in the gradient.
     */
    fun getClosestPoint(pos: Double): GradientPoint<T>?

    /**
     * @return the index of the point closest to the specified pos, or -1 if there are no points in the gradient.
     */
    fun getClosestIndex(pos: Double): Int

    /**
     * @return information for the point at the specified index, or null if no point at the specified index.
     */
    fun getPointAtIndex(index: Int): GradientPoint<T>?

    /**
     * @return iterator over the points in this gradient, ordered from smallest to largest position.
     */
    val points: List<GradientPoint<T>>

    /**
     * @param pos position at which the closest point should be removed.
     * @return true if a point was removed.
     */
    fun removeClosestPoint(pos: Double): Boolean

    /**
     * Removes the n:th point from this gradient, if there is one.
     * @return true if a point was removed.
     */
    fun removePointAtIndex(index: Int): Boolean

    /**
     * Removes all points.
     */
    fun clear()

    /**
     * Updates the properties of the point at the specified index.
     * Note that the index may change as the result of this operation!
     * @param pointIndex index of the point to change.
     * @param position new position for the point.
     * @param value new value at the point.
     * @param easing new easing function to use for values between the point and the previous one.
     */
    fun updatePoint(pointIndex: Int, position: Double, value: T, easing: Easing = LinearEasing)

    /**
     * Update the position of the point at the specified index.
     * Note that the index may change as the result of this operation!
     */
    fun setPointPosition(pointIndex: Int, position: Double)

    /**
     * Update the value at the specified index.
     */
    fun setPointValue(pointIndex: Int, newValue: T)

    /**
     * Update the interpolator at the specified index.
     */
    fun setPointInterpolation(pointIndex: Int, newInterpolation: Easing)

    /**
     * @param out if non-null (and T is mutable), this is used to write the result to and is returned.
     * @return value at the specified position.
     */
    fun get(pos: Double, out: T?): T = get(pos, out, valueMixer)

    /**
     * @param out if non-null (and T is mutable), this is used to write the result to and is returned.
     * @return value at the specified position, using the specified mixer to interpolate between values of type T.
     */
    fun get(pos: Double, out: T?, valueMixer: ValueMixer<T>): T

}
