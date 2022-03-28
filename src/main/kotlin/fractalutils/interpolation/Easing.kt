package fractalutils.interpolation

import fractalutils.interpolation.easings.*

/**
 * Something defines how to interpolate between two values (fills in values in between according to a function).
 *
 * It is typically defined in the 0..1 range, with an output value of 0 for input value 0,
 * and an output value of 1 for input value 1, but these are not hard rules.
 */
// IDEA: Add overshooting and noise easings.
interface Easing: (Double) -> Double {

    override fun invoke(t: Double): Double = interpolate(t)

    operator fun invoke(t: Float): Float = interpolate(t.toDouble()).toFloat()

    /**
     * Interpolates between 0 and 1, using this easing function and an interpolation position t.
     * @param value relative value indicating the easing position, goes from 0 to 1.
     *          May go beyond 0 or 1 as well, but the result may vary depending on the interpolator used.
     * @return a value between 0 and 1 (may exceed that range as well), at point t.
     */
    fun interpolate(value: Double): Double

    /**
     * Interpolates between 0 and 1, using this interpolation function and an interpolation position t.
     * @param value relative value indicating the easing position, goes from 0 to 1.
     *          May go beyond 0 or 1 as well, but the result may vary depending on the interpolator used.
     * @return a value between 0 and 1 (may exceed that range as well), at point t.
     */
    fun interpolate(value: Float): Float = interpolate(value.toDouble()).toFloat()

    /**
     * Interpolates between targetStart and targetEnd, using this easing function and an interpolation position sourcePos between sourceStart and sourceEnd.
     * @param t value that goes from 0 to 1 (and optionally beyond)
     * @param startValue result value when [t] = 0
     * @param endValue result value when [t] = 1
     * @param clamp if true, [t] is clamped to the 0..1 range,
     *              if false, [t] may be outside that range and the result may be outside the startValue..endValue range (default).
     * @return the value between startValue and endValue (may exceed that range as well), at point t.
     */
    fun interpolate(t: Double,
                    startValue: Double,
                    endValue: Double,
                    clamp: Boolean = false): Double {

        // Clamp position to range if requested
        var pos = t
        if (clamp) {
            if (pos < 0.0) pos = 0.0
            if (pos > 1.0) pos = 1.0
        }

        // Apply interpolation
        pos = interpolate(pos)

        // Expand the position to the output range
        return startValue + pos * (endValue - startValue)
    }


    /**
     * Interpolates between targetStart and targetEnd, using this easing function and an interpolation position sourcePos between sourceStart and sourceEnd.
     * @param t value that goes from 0 to 1 (and optionally beyond)
     * @param startValue result value when [t] = 0
     * @param endValue result value when [t] = 1
     * @param clamp if true, [t] is clamped to the 0..1 range,
     *              if false, [t] may be outside that range and the result may be outside the startValue..endValue range (default).
     * @return the value between startValue and endValue (may exceed that range as well), at point t.
     */
    fun interpolate(t: Float,
                    startValue: Float,
                    endValue: Float,
                    clamp: Boolean = false): Float =
        interpolate(t.toDouble(), startValue.toDouble(), endValue.toDouble()).toFloat()


    /**
     * Interpolates between targetStart and targetEnd, using this easing function and an interpolation position sourcePos between sourceStart and sourceEnd.
     * @param sourcePos relative value indicating mix between targetStart and targetEnd, goes from sourceStart to sourceEnd (or beyond if clampSourcePos is false).
     * @param sourceStart value for sourcePos corresponding to targetStart (defaults to 0.0)
     * @param sourceEnd value for sourcePos corresponding to targetEnd (defaults to 1.0)
     * @param targetStart result value when sourcePos = sourceStart
     * @param targetEnd result value when sourcePos = sourceEnd
     * @param clampSourcePos if true, sourcePos is clamped to the sourceStart..sourceEnd range,
     *                       if false, sourcePos may be outside that range and the result may be outside the targetStart..targetEnd range (default).
     * @return the value between targetStart and targetEnd (may exceed that range as well), at point sourcePos.
     */
    fun interpolate(sourcePos: Double,
                    sourceStart: Double,
                    sourceEnd: Double,
                    targetStart: Double,
                    targetEnd: Double,
                    clampSourcePos: Boolean = false): Double {

        // Normalize position to 0..1 range
        var t = if (sourceEnd == sourceStart) 0.5 else (sourcePos - sourceStart) / (sourceEnd - sourceStart)

        // Clamp position to range if requested
        if (clampSourcePos) {
            if (t < 0.0) t = 0.0
            if (t > 1.0) t = 1.0
        }

        // Interpolate
        t = interpolate(t)

        // Expand the position to the output range
        return targetStart + t * (targetEnd - targetStart)
    }


    /**
     * Interpolates between targetStart and targetEnd, using this easing function and an interpolation position sourcePos between sourceStart and sourceEnd.
     * @param sourcePos relative value indicating mix between targetStart and targetEnd, goes from sourceStart to sourceEnd (or beyond if clampSourcePos is false).
     * @param sourceStart value for sourcePos corresponding to targetStart (defaults to 0.0)
     * @param sourceEnd value for sourcePos corresponding to targetEnd (defaults to 1.0)
     * @param targetStart result value when sourcePos = sourceStart
     * @param targetEnd result value when sourcePos = sourceEnd
     * @param clampSourcePos if true, sourcePos is clamped to the sourceStart..sourceEnd range,
     *                       if false, sourcePos may be outside that range and the result may be outside the targetStart..targetEnd range (default).
     * @return the value between targetStart and targetEnd (may exceed that range as well), at point sourcePos.
     */
    fun interpolate(sourcePos: Float,
                    sourceStart: Float,
                    sourceEnd: Float,
                    targetStart: Float,
                    targetEnd: Float,
                    clampSourcePos: Boolean = false): Float =
        interpolate(
            sourcePos.toDouble(),
            sourceStart.toDouble(),
            sourceEnd.toDouble(),
            targetStart.toDouble(),
            targetEnd.toDouble(),
            clampSourcePos
        ).toFloat()

    /**
     * Returns a reverse interpolation of this one ( f(1 - value) ) (value goes from 1 to 0 instead of 0 to 1)
     */
    fun reverse(): Easing = ReverseEasing(this)

    /**
     * Returns an inverted interpolation of this one ( 1 - f(value) ) (result goes from 1 to 0 instead of 0 to 1)
     */
    fun flip(): Easing = FlipEasing(this)

    /**
     * Returns a reversed and flipped interpolation of this one ( 1 - f(1 - value) )  (value and result goes from 1 to 0 instead of 0 to 1)
     */
    fun invert(): Easing = InvertEasing(this)

    /**
     * Uses this interpolator for at the start and the second interpolator at the end, using a mixer to mix them (by default a linear mix)
     */
    fun crossFade(other: Easing, mixer: Easing = LinearEasing): Easing = CrossFadeEasing(this, other, mixer)

    /**
     * Blends the result of this and the other interpolator, using the mixing value t (t=0 use this, t=1 use other, otherwise blend).
     */
    fun blend(other: Easing, t: Double = 0.5): Easing = BlendEasing(this, other, t)

    /**
     * Returns an interpolator that adds the results of this interpolator and the other interpolator.
     */
    fun add(other: Easing): Easing = AddEasing(this, other)

    /**
     * Returns an interpolator that multiplies the results of this interpolator and the other interpolator.
     */
    fun multiply(other: Easing): Easing = MultiplyEasing(this, other)

    /**
     * Return a part of this interpolator as a full interpolation, by default scaling the value of the part to output in the 0..1 range.
     */
    fun part(start: Double, end: Double, scaleValue: Boolean = true) = PartialEasing(this, start, end, scaleValue)

    /**
     * Uses this interpolator for the first part and the second interpolator for the second part.
     * Optionally the point where the other interpolator will be used can be defined.
     * @param compressStart if true, the whole of the start interpolator will be used before the split point
     * @param compressEnd if true, the whole of the  end interpolator will be used after the split point
     */
    fun combine(other: Easing, splitPoint: Double = 0.5, compressStart: Boolean = false, compressEnd: Boolean = false): Easing = CombineEasing(this, other, splitPoint, compressStart, compressEnd)

    /**
     * Returns an interpolation function that clamps the input to the start to end range (defaults to 0..1) and uses it with this interpolator.
     */
    fun clampInput(start: Double = 0.0, end: Double = 1.0): Easing = ClampEasing(this, start, end)

    /**
     * Maps the input range of the interpolator to 0..1 from the specified input start and end, and maps the output value
     * of the interpolator from the 0..1 range to the output start .. output end range.
     */
    fun map(inputStart: Double = 0.0, inputEnd: Double = 1.0, outputStart: Double = 0.0, outputEnd: Double = 1.0, clampInput: Boolean = false): Easing =
        MappedEasing(this, inputStart, inputEnd, outputStart, outputEnd, clampInput)

    /**
     * Combines this interpolator and a reversed copy of it, placing the midpoint at 0.5 (by default).
     * Normally the output of the created interpolator will start at 0, reach 1 at the midpoint, and go to 0 when the input is 1.
     * This is useful for various kinds of distributions, offsets, etc.
     * e.g. a mirror of a SmootherStep interpolator will give a bell-curve like shape.
     *
     * Optionally the interpolator to use for the reversed second half can also be specified (it is automatically reversed).
     *
     * If you want to re-map the created interpolator to e.g. use the -1..1 range, append .map(-1, 1) (see [map]).
     */
    fun pingPong(midpoint: Double = 0.5,
                 other: Easing? = null): Easing {
        return CombineEasing(
            this,
            other?.reverse() ?: this.reverse(),
            midpoint,
            compressStart = true,
            compressEnd = true
        )
    }

    /**
     * Combines this interpolator and an inverted copy of it, using linear cross-fading (by default).
     *
     * This can be used to create a start-stop type of interpolation from just a start interpolation
     * (or this interpolator as the start, and another interpolator as the stop).
     *
     * Optionally the interpolator to use for the inverted second half can also be specified (it is automatically inverted).
     */
    fun startStopFade(other: Easing? = null,
                      crossFadeEasing: Easing = LinearEasing
    ): Easing {
        return crossFade(
            other?.invert() ?: this.invert(),
            crossFadeEasing
        )
    }

    /**
     * Combines this interpolator and an inverted copy of it, placing the midpoint at 0.5 (by default).
     * Also scales this copy to end at the output value 0.5 (by default) at the midpoint, and the mirrored copy to
     * start from that value.
     *
     * This can be used to create a start-stop type of interpolation from just a start interpolation
     * (or this interpolator as the start, and another interpolator as the stop).
     *
     * Optionally the interpolator to use for the inverted second half can also be specified (it is automatically inverted).
     */
    fun startStopHalves(midpoint: Double = 0.5,
                        midValue: Double = 0.5,
                        other: Easing? = null): Easing {
        return CombineEasing(
            this.map(outputEnd = midValue),
            other?.invert()?.map(outputStart = midValue) ?: this.invert().map(outputStart = midValue),
            midpoint,
            compressStart = true,
            compressEnd = true
        )
    }


}
