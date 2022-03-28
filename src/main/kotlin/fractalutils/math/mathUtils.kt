package fractalutils.math

import fractalutils.checking.Check
import fractalutils.interpolation.Easing
import fractalutils.math.MathUtils.bigDecimalRoundingMathContext
import fractalutils.random.Rand
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import java.text.DecimalFormat



/**
 * Tau is 2 Pi, see http://www.tauday.com
 */
const val Tau = Math.PI * 2.0

/**
 * Tau is 2 Pi, see http://www.tauday.com
 * Float version.
 */
const val TauFloat = Tau.toFloat()

/**
 * The golden ratio (1.618033...).
 * (1 + sqrt(5)) / 2
 */
const val GoldenRatio = 1.6180339887498948482

/**
 * The golden ratio (1.618033...).
 * (1 + sqrt(5)) / 2
 * Float version.
 */
const val GoldenRatioFloat = GoldenRatio.toFloat()


/**
 * Interpolate between the start and end values (and beyond).
 * @param t 0 corresponds to start, 1 to end.
 * @param clamp if true, [t] will be clamped to the start..end range (inclusive).  Defaults to false.
 * @param easing if provided, apply this function to the value after any clamping and before it is scaled to the start..end range.
 * @return mixed value
 */
fun mix(t: Float,
        start: Float = 0f,
        end: Float = 1f,
        clamp: Boolean = false,
        easing: Easing? = null): Float {

    // Apply clamping if wanted
    var v = if (clamp) t.clamp0To1() else t

    // Apply easing if provided
    v = if (easing != null) easing(v) else v

    // Scale to start .. end range
    return start + v * (end - start)
}


/**
 * Interpolate between the start and end values (and beyond).
 * @param t 0 corresponds to start, 1 to end.
 * @param clamp if true, [t] will be clamped to the start..end range (inclusive).  Defaults to false.
 * @param easing if provided, apply this function to the value after any clamping and before it is scaled to the start..end range.
 * @return interpolated value
 */
fun mix(t: Double,
        start: Double = 0.0,
        end: Double = 1.0,
        clamp: Boolean = false,
        easing: Easing? = null): Double {

    // Apply clamping if wanted
    var v = if (clamp) t.clamp0To1() else t

    // Apply easing if provided
    v = if (easing != null) easing(v) else v

    // Scale to start .. end range
    return start + v * (end - start)
}

/**
 * Interpolate using this value between the start and end values (and beyond).
 * If this value is 0 it corresponds to start, 1 to end.
 * @param clamp if true, input value will be clamped to the start..end range (inclusive).  Defaults to false.
 * @param easing if provided, apply this function to the value after any clamping and before it is scaled to the start..end range.
 * @return interpolated value
 */
fun Float.mixTo(start: Float = 0f,
                end: Float = 1f,
                clamp: Boolean = false,
                easing: Easing? = null): Float {
    return mix(this, start, end, clamp, easing)
}

/**
 * Interpolate using this value between the start and end values (and beyond).
 * If this value is 0 it corresponds to start, 1 to end.
 * @param clamp if true, input value will be clamped to the start..end range (inclusive).  Defaults to false.
 * @param easing if provided, apply this function to the value after any clamping and before it is scaled to the start..end range.
 * @return interpolated value
 */
fun Double.mixTo(start: Double = 0.0,
                 end: Double = 1.0,
                 clamp: Boolean = false,
                 easing: Easing? = null): Double {
    return mix(this, start, end, clamp, easing)
}


/**
 * @param clamp if true, the result is clamped to the 0..1 range (inclusive).  Defaults to false.
 * @return relative position of t between start and end.
 * *         if t == start, returns 0, if t == end, returns 1, etc.
 * *         In case start equals end, returns 0.5.
 */
fun relPos(t: Float, start: Float, end: Float, clamp: Boolean = false): Float {
    return if (end == start)
        0.5f
    else {
        val result = (t - start) / (end - start)
        if (!clamp) result
        else result.clamp0To1()
    }
}

/**
 * @param clamp if true, the result is clamped to the 0..1 range (inclusive).  Defaults to false.
 * @return relative position of t between start and end.
 * *         if t == start, returns 0, if t == end, returns 1, etc.
 * *         In case start equals end, returns 0.5.
 */
fun relPos(t: Double, start: Double, end: Double, clamp: Boolean = false): Double {
    return if (end == start)
        0.5
    else {
        val result = (t - start) / (end - start)
        if (!clamp) result
        else result.clamp0To1()
    }
}

/**
 * @param clamp if true, the result is clamped to the 0..1 range (inclusive).  Defaults to false.
 * @return relative position of t between start and end.
 * *         if t == start, returns 0, if t == end, returns 1, etc.
 * *         In case start equals end, returns 0.5.
 */
fun relPos(t: Int, start: Int, end: Int, clamp: Boolean = false): Double {
    return if (end == start)
        0.5
    else {
        val result = (t - start).toDouble() / (end - start)
        if (!clamp) result
        else result.clamp0To1()
    }
}

/**
 * @param clamp if true, the result is clamped to the 0..1 range (inclusive).  Defaults to false.
 * @return relative position of t between start and end.
 * *         if t == start, returns 0, if t == end, returns 1, etc.
 * *         In case start equals end, returns 0.5.
 */
fun relPos(t: Long, start: Long, end: Long, clamp: Boolean = false): Double {
    return if (end == start)
        0.5
    else {
        val result = (t - start).toDouble() / (end - start)
        if (!clamp) result
        else result.clamp0To1()
    }
}


/**
 * Maps a value within a source range to the corresponding position in a target range.
 * @param clamp if true the result will be clamped to the targetStart..targetEnd range (inclusive)  Defaults to false.
 * @param smooth if true, the mapping will use cosine interpolation between the start and end values produced (also automatically clamps the result).  Defaults to false.
 * @param easing if provided, apply this function to the value after it has been scaled to the 0..1 range and before it is scaled to the start..end range.
 */
fun map(t: Float,
        sourceStart: Float = 0f,
        sourceEnd: Float =1f,
        targetStart: Float = 0f,
        targetEnd: Float =1f,
        clamp: Boolean = false,
        easing: Easing? = null): Float {
    val r = relPos(t, sourceStart, sourceEnd)
    return mix(r, targetStart, targetEnd, clamp, easing)
}

/**
 * Maps a value within a source range to the corresponding position in a target range.
 * @param clamp if true the result will be clamped to the targetStart..targetEnd range (inclusive)  Defaults to false.
 * @param easing if provided, apply this function to the value after it has been scaled to the 0..1 range and before it is scaled to the start..end range.
 */
fun map(t: Double,
        sourceStart: Double = 0.0,
        sourceEnd: Double =1.0,
        targetStart: Double = 0.0,
        targetEnd: Double =1.0,
        clamp: Boolean = false,
        easing: Easing? = null): Double {
    val r = relPos(t, sourceStart, sourceEnd)
    return mix(r, targetStart, targetEnd, clamp, easing)
}

/**
 * Maps the value from within a source range to the corresponding position in a target range.
 * @param clamp if true the result will be clamped to the targetStart..targetEnd range (inclusive)  Defaults to false.
 * @param easing if provided, apply this function to the value after it has been scaled to the 0..1 range and before it is scaled to the start..end range.
 */
fun Float.mapTo(sourceStart: Float = 0f,
                sourceEnd: Float =1f,
                targetStart: Float = 0f,
                targetEnd: Float =1f,
                clamp: Boolean = false,
                easing: Easing? = null): Float {
    return map(this, sourceStart, sourceEnd, targetStart, targetEnd, clamp, easing)
}

/**
 * Maps the value from within a source range to the corresponding position in a target range.
 * @param clamp if true the result will be clamped to the targetStart..targetEnd range (inclusive)  Defaults to false.
 * @param easing if provided, apply this function to the value after it has been scaled to the 0..1 range and before it is scaled to the start..end range.
 */
fun Double.mapTo(sourceStart: Double = 0.0,
                 sourceEnd: Double =1.0,
                 targetStart: Double = 0.0,
                 targetEnd: Double =1.0,
                 clamp: Boolean = false,
                 easing: Easing? = null): Double {
    return map(this, sourceStart, sourceEnd, targetStart, targetEnd, clamp, easing)
}

/**
 * @return the average of the parameters.
 */
fun average(v1: Float, v2: Float): Float {
    return 0.5f * (v1 + v2)
}

/**
 * @return the average of the parameters.
 */
fun average(v1: Double, v2: Double): Double {
    return 0.5 * (v1 + v2)
}

/**
 * @return the average of the parameters.
 */
fun average(v1: Float, v2: Float, v3: Float): Float {
    return (v1 + v2 + v3) / 3.0f
}

/**
 * @return the average of the parameters.
 */
fun average(v1: Double, v2: Double, v3: Double): Double {
    return (v1 + v2 + v3) / 3.0
}

/**
 * @return the average of the parameters.
 */
fun average(v1: Float, v2: Float, v3: Float, v4: Float, vararg additionalValues: Float): Float {
    var sum = v1 + v2 + v3 + v4
    for (additionalValue in additionalValues) {
        sum += additionalValue
    }
    return sum / (additionalValues.size + 4)
}

/**
 * @return the average of the parameters.
 */
fun average(v1: Double, v2: Double, v3: Double, v4: Double, vararg additionalValues: Double): Double {
    var sum = v1 + v2 + v3 + v4
    for (additionalValue in additionalValues) {
        sum += additionalValue
    }
    return sum / (additionalValues.size + 4)
}

/**
 * @return the maximum value of the parameters.
 */
fun max(v1: Double, v2: Double, v3: Double, vararg additionalValues: Double): Double {
    var v = Math.max(Math.max(v1, v2), v3)
    for (additionalValue in additionalValues) {
        v = Math.max(v, additionalValue)
    }
    return v
}

/**
 * @return the maximum value of the parameters.
 */
fun max(v1: Float, v2: Float, v3: Float, vararg additionalValues: Float): Float {
    var v = Math.max(Math.max(v1, v2), v3)
    for (additionalValue in additionalValues) {
        v = Math.max(v, additionalValue)
    }
    return v
}

/**
 * @return the maximum value of the parameters.
 */
fun max(v1: Int, v2: Int, v3: Int, vararg additionalValues: Int): Int {
    var v = Math.max(Math.max(v1, v2), v3)
    for (additionalValue in additionalValues) {
        v = Math.max(v, additionalValue)
    }
    return v
}

/**
 * @return the minimum value of the parameters.
 */
fun min(v1: Double, v2: Double, v3: Double, vararg additionalValues: Double): Double {
    var v = Math.min(Math.min(v1, v2), v3)
    for (additionalValue in additionalValues) {
        v = Math.min(v, additionalValue)
    }
    return v
}

/**
 * @return the minimum value of the parameters.
 */
fun min(v1: Float, v2: Float, v3: Float, vararg additionalValues: Float): Float {
    var v = Math.min(Math.min(v1, v2), v3)
    for (additionalValue in additionalValues) {
        v = Math.min(v, additionalValue)
    }
    return v
}

/**
 * @return the minimum value of the parameters.
 */
fun min(v1: Int, v2: Int, v3: Int, vararg additionalValues: Int): Int {
    var v = Math.min(Math.min(v1, v2), v3)
    for (additionalValue in additionalValues) {
        v = Math.min(v, additionalValue)
    }
    return v
}


/**
 * Converts an angle in radians to degrees.
 */
fun Float.toDegrees(): Float {
    return 360f * this / TauFloat
}

/**
 * Converts an angle in radians to degrees.
 */
fun Double.toDegrees(): Double {
    return 360.0 * this / Tau
}

/**
 * Converts an angle in degrees to radians.
 */
fun Float.toRadians(): Float {
    return this / 360f * TauFloat
}

/**
 * Converts an angle in degrees to radians.
 */
fun Double.toRadians(): Double {
    return this / 360.0 * Tau
}

/**
 * Wraps a value to the range zero (inclusive) to one (exclusive).
 * @return the fractional part of a value.
 */
fun Float.wrap0To1(): Float {
    var value = this
    return if (value >= 0f && value < 1f)
        value
    else {
        value %= 1f
        if (value < 0f) value += 1f
        value
    }
}

/**
 * Wraps a value to the range zero (inclusive) to one (exclusive).
 * @return the fractional part of a value.
 */
fun Double.wrap0To1(): Double {
    var value = this
    return if (value >= 0.0 && value < 1.0)
        value
    else {
        value %= 1.0
        if (value < 0.0) value += 1.0
        value
    }
}

/**
 * Wraps a value to be smaller than the specified maximum value.
 * @throws IllegalArgumentException if max is smaller than zero.
 */
fun Int.wrap0To(max: Int): Int = this.wrapTo(0, max)

/**
 * Wraps a value to be smaller than the specified maximum value.
 * @throws IllegalArgumentException if max is smaller than zero.
 */
fun Float.wrap0To(max: Float): Float = this.wrapTo(0f, max)

/**
 * Wraps a value to be smaller than the specified maximum value.
 * @throws IllegalArgumentException if max is smaller than zero.
 */
fun Double.wrap0To(max: Double): Double = this.wrapTo(0.0, max)


/**
 * Wraps a value to be larger or equal than the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun Int.wrapTo(min: Int, max: Int): Int = wrap(this, min, max)

/**
 * Wraps a value to be larger or equal than the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun wrap(value: Int, min: Int, max: Int): Int {
    if (max < min) throw IllegalArgumentException("The specified maximum value ($max) was smaller than the minimum value ($min) when trying to wrap the value ($value)")

    if (value >= min && value < max)
        return value
    else {
        var t = value - min
        val s = max - min

        if (s <= 0) return min

        t = t % s
        if (t < 0) t += s
        return min + t
    }
}

/**
 * Wraps a value to be larger or equal to the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun Float.wrapTo(min: Float, max: Float): Float = wrap(this, min, max)

/**
 * Wraps a value to be larger or equal to the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun wrap(value: Float, min: Float, max: Float): Float {
    if (max < min) throw IllegalArgumentException("The specified maximum value ($max) was smaller than the minimum value ($min) when trying to wrap the value ($value)")

    if (value >= min && value < max)
        return value
    else {
        var t = value - min
        val s = max - min

        if (s <= 0f) return min

        t = t % s
        if (t < 0f) t += s
        return min + t
    }
}

/**
 * Wraps a value to be larger or equal to the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun Double.wrapTo(min: Double, max: Double): Double = wrap(this, min, max)

/**
 * Wraps a value to be larger or equal to the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun wrap(value: Double, min: Double, max: Double): Double {
    if (max < min) throw IllegalArgumentException("The specified maximum value ($max) was smaller than the minimum value ($min) when trying to wrap the value ($value)")
    if (value >= min && value < max)
        return value
    else {
        var t = value - min
        val s = max - min

        if (s <= 0.0) return min

        t = t % s
        if (t < 0.0) t += s
        return min + t
    }
}


/**
 * Clamps a value to be larger than the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun Int.clampTo(min: Int, max: Int): Int = clamp(this, min, max)

/**
 * Clamps a value to be larger than the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun clamp(value: Int, min: Int, max: Int): Int {
    if (max < min) throw IllegalArgumentException("The specified maximum value ($max) was smaller than the minimum value ($min) when trying to clamp the value ($value)")

    if (value < min) return min
    if (value > max) return max
    return value
}

/**
 * Clamps a value to be larger than the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun Float.clampTo(min: Float, max: Float): Float = clamp(this, min, max)

/**
 * Clamps a value to be larger than the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun clamp(value: Float, min: Float, max: Float): Float {
    if (max < min) throw IllegalArgumentException("The specified maximum value ($max) was smaller than the minimum value ($min) when trying to clamp the value ($value)")

    if (value < min) return min
    if (value > max) return max
    return value
}

/**
 * Clamps a value to be larger than the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun Double.clampTo(min: Double, max: Double): Double = clamp(this, min, max)

/**
 * Clamps a value to be larger than the specified minimum value and smaller than the specified maximum value.
 * @throws IllegalArgumentException if maximum is smaller than minimum.
 */
fun clamp(value: Double, min: Double, max: Double): Double {
    if (max < min) throw IllegalArgumentException("The specified maximum value ($max) was smaller than the minimum value ($min) when trying to clamp the value ($value)")

    if (value < min) return min
    if (value > max) return max
    return value
}


/**
 * Clamps a value to the range formed by a and b.
 * The order that the range is specified does not matter,
 * the smallest of a and b is used as minimum, and the largest as maximum.
 */
fun Int.clampToRange(a: Int, b: Int): Int {
    var min = a
    var max = b
    if (a > b) {
        min = b
        max = a
    }

    if (this < min) return min
    if (this > max) return max
    return this
}

/**
 * Clamps a value to the range formed by a and b.
 * The order that the range is specified does not matter,
 * the smallest of a and b is used as minimum, and the largest as maximum.
 */
fun Float.clampToRange(a: Float, b: Float): Float {
    var min = a
    var max = b
    if (a > b) {
        min = b
        max = a
    }

    if (this < min) return min
    if (this > max) return max
    return this
}

/**
 * Clamps a value to the range formed by a and b.
 * The order that the range is specified does not matter,
 * the smallest of a and b is used as minimum, and the largest as maximum.
 */
fun Double.clampToRange(a: Double, b: Double): Double {
    var min = a
    var max = b
    if (a > b) {
        min = b
        max = a
    }

    if (this < min) return min
    if (this > max) return max
    return this
}

/**
 * @return the range clamped to the 0..1 range (inclusive).
 */
fun Float.clamp0To1(): Float {
    if (this < 0f) return 0f
    if (this > 1f) return 1f
    return this
}

/**
 * @return the range clamped to the 0..1 range (inclusive).
 */
fun Double.clamp0To1(): Double {
    if (this < 0.0) return 0.0
    if (this > 1.0) return 1.0
    return this
}

/**
 * @return the range clamped to the -1..1 range (inclusive).
 */
fun Float.clampMinus1To1(): Float {
    if (this < -1f) return -1f
    if (this > 1f) return 1f
    return this
}

/**
 * @return the range clamped to the -1..1 range (inclusive).
 */
fun Double.clampMinus1To1(): Double {
    if (this < -1.0) return -1.0
    if (this > 1.0) return 1.0
    return this
}

/**
 * @return distance between two points in a 2D coordinate system.
 */
fun distance(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    val x = x2 - x1
    val y = y2 - y1
    return Math.sqrt((x * x + y * y).toDouble()).toFloat()
}

/**
 * @return distance between two points in a 2D coordinate system.
 */
fun distance(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    val x = x2 - x1
    val y = y2 - y1
    return Math.sqrt(x * x + y * y)
}

/**
 * @return squared distance between two points in a 2D coordinate system.
 */
fun distanceSquared(x1: Float, y1: Float, x2: Float, y2: Float): Float {
    val x = x2 - x1
    val y = y2 - y1
    return x * x + y * y
}

/**
 * @return squared distance between two points in a 2D coordinate system.
 */
fun distanceSquared(x1: Double, y1: Double, x2: Double, y2: Double): Double {
    val x = x2 - x1
    val y = y2 - y1
    return x * x + y * y
}

/**
 * Returns the square of this value.
 */
val Double.squared: Double get() = this * this

/**
 * Returns the square of this value.
 */
val Float.squared: Float get() = this * this

/**
 * Returns the square of this value.
 */
val Int.squared: Int get() = this * this


/**
 * Returns the square of the specified value.
 */
fun squared(v: Double): Double = v * v

/**
 * Returns the square of the specified value.
 */
fun squared(v: Float): Float = v * v

/**
 * Returns the square of the specified value.
 */
fun squared(v: Int): Int = v * v


/**
 * Fast floor function (should be faster than Math.floor()).
 */
fun Float.fastFloor(): Int {
    return if (this < 0f) (this - 1f).toInt() else this.toInt()
}

/**
 * Fast floor function (should be faster than Math.floor()).
 */
fun Double.fastFloor(): Int {
    return if (this < 0.0) (this - 1).toInt() else this.toInt()
}

/**
 * Rounds to closest integer.
 */
fun Float.round(): Int {
    var value = this
    value += 0.5f

    // Use the fast floor formula
    return if (value < 0.0f) (value - 1).toInt() else value.toInt()
}

/**
 * Rounds to closest integer.
 */
fun Double.round(): Int {
    var value = this
    value += 0.5

    // Use the fast floor formula
    return if (value < 0.0f) (value - 1).toInt() else value.toInt()
}

/**
 * Returns the fractional part of this value
 */
fun Double.fraction(): Double {
    return this % 1
}

/**
 * Returns the fractional part of this value
 */
fun Float.fraction(): Float {
    return this % 1
}

/**
 * Returns the integral part of this value
 */
fun Float.integral(): Float {
    return this - (this % 1)
}

/**
 * Rounds the value to include at most the specified number of significant digits (non zero digits).
 * Not optimized for speed.
 * @param significantDigits max number of digits that should be non zero.
 */
fun Int.round(significantDigits: Int): Int {
    var value = this
    Check.positive(significantDigits, "significantDigits")

    val wasNegative = value < 0
    value = Math.abs(value)

    var digits = 1
    var max = 10
    while (value >= max) {
        max *= 10
        digits++
    }

    val divisor = Math.pow(10.0, (digits - significantDigits).toDouble()).toInt()
    value = (1.0 * value / divisor).round()
    value *= divisor
    return value * if (wasNegative) -1 else 1
}


/**
 * Rounds to specific number of significant digits
 */
fun Double.roundToNDigits(numberOfSignificantDigits: Int): Double {
    var value = this
    var digitCount = numberOfSignificantDigits

    if (digitCount < 1) {
        digitCount = 1
    }

    val digitsInValue = (Math.floor(Math.log10(Math.abs(value))).toInt() + 1).toDouble()
    val digitsToRemove = digitsInValue - digitCount

    val scale = Math.pow(10.0, digitsToRemove)

    value /= scale
    value = Math.round(value).toDouble()
    value *= scale

    return value
}

/**
 * Rounds to specific number of significant digits
 */
fun Float.roundToNDigits(numberOfSignificantDigits: Int): Float = this.toDouble().roundToNDigits(numberOfSignificantDigits).toFloat()


/**
 * @return this modulus b, with always a positive result (if the result would be negative, b is added to it).
 */
fun Int.modPositive(b: Int): Int {
    val result = this % b
    if (result >= 0) {
        return result
    } else {
        return result + b
    }
}

/**
 * @return this modulus b, with always a positive result (if the result would be negative, b is added to it).
 */
fun Long.modPositive(b: Long): Long {
    val result = this % b
    return if (result >= 0) {
        result
    } else {
        result + b
    }
}

/**
 * @return this modulus b, with always a positive result (if the result would be negative, b is added to it).
 */
fun Float.modPositive(b: Float): Float {
    val result = this % b
    return if (result >= 0) {
        result
    } else {
        result + b
    }
}

/**
 * @return this modulus b, with always a positive result (if the result would be negative, b is added to it).
 */
fun Double.modPositive(b: Double): Double {
    val result = this % b
    return if (result >= 0) {
        result
    } else {
        result + b
    }
}

/**
 * A tunable normalized sigmoid function that return values in the 0..1 range for inputs in the 0..1 range.
 * See http://www.slideshare.net/dinodini1/simplest-ai-trick-gdc2013-dino-v2-1 for reference.
 * @param value input, clamped to the 0 to 1 range.
 * @param sharpness the sharpness of the curve, in the range -1..1.
 *                  When 0, the result is equal to the input,
 *                  when it goes towards 1 the S shape gets sharper (more output directed towards 0 or 1, less towards 0.5).
 *                  If negative, it will produce an inverse S curve that gets shallower towards -1 (more output directed towards 0.5, less towards 0 or 1).
 * @return a value between 0 and 1, depending on the position of x between 0 and 1.
 */
fun sigmoidZeroToOne(value: Double, sharpness: Double = 0.5): Double {
    return 0.5 * (1.0 + sigmoid(2.0 * value - 1.0, sharpness))
}

/**
 * A tunable normalized sigmoid function that return values in the -1..1 range for inputs in the -1..1 range.
 * See http://www.slideshare.net/dinodini1/simplest-ai-trick-gdc2013-dino-v2-1 for reference.
 * @param value input, clamped to the -1 to 1 range.
 * @param sharpness the sharpness of the curve, in the range -1..1.
 *                  When 0, the result is equal to the input,
 *                  when it goes towards 1 the S shape gets sharper (more output directed towards -1 or +1, less towards 0).
 *                  If negative, it will produce an inverse S curve that gets shallower towards -1 (more output directed towards 0, less towards -1 or 1).
 * @return a value between -1 and 1, depending on the position of x between -1 and 1.
 */
fun sigmoid(value: Double, sharpness: Double = 0.5): Double {
    var x = value
    val k: Double

    // Clamp x
    if (x < -1.0)
        x = -1.0
    else if (x > 1.0) x = 1.0

    // Calculate k factor from a more intuitive sharpness parameter, and also handle edge cases where we would get divisions by zero.
    if (sharpness <= -1) {
        return when {
            x <= -1 -> -1.0
            x >= 1 -> 1.0
            else -> 0.0
        }
    } else if (sharpness >= 1) {
        return when {
            x < 0 -> -1.0
            x > 0 -> 1.0
            else -> 0.0
        }
    } else if (sharpness <= -0.5) {
        k = 2.0 + 2.0 * sharpness
    } else if (sharpness < 0) {
        k = -1.0 / (2.0 * sharpness)
    } else if (sharpness >= 0.5) {
        k = -3.0 + 2.0 * sharpness
    } else if (sharpness > 0) {
        k = -1.0 / sharpness
    } else
        return x  // Sharpness was zero

    // Tunable normalized sigmoid function:
    val absX = if (x < 0) -x else x
    val sign = (if (x >= 0) 1 else -1).toDouble()
    return sign * absX * k / (1.0 + k - absX)

}

/**
 * Calculates the logarithm of this value using an arbitrary log root.
 * This value should be larger than zero, or NaN will be returned.
 * @param base base of the logarithm. Should be larger than zero, or NaN will be returned.
 * @return logarithm of this value using a log with the specified base.
 */
fun Double.log(base: Double): Double {
    return Math.log(this) / Math.log(base)
}

/**
 * Calculates the logarithm of this value using an arbitrary log root.
 * This value should be larger than zero, or NaN will be returned.
 * @param base base of the logarithm. Should be larger than zero, or NaN will be returned.
 * @return logarithm of this value using a log with the specified base.
 */
fun Float.log(base: Float): Float {
    return (Math.log(this.toDouble()) / Math.log(base.toDouble())).toFloat()
}

infix fun Double.max(other: Double): Double = if (this >= other) this else other
infix fun Double.min(other: Double): Double = if (this <= other) this else other
infix fun Float.max(other: Float): Float = if (this >= other) this else other
infix fun Float.min(other: Float): Float = if (this <= other) this else other
infix fun Int.max(other: Int): Int = if (this >= other) this else other
infix fun Int.min(other: Int): Int = if (this <= other) this else other

fun Double.orZeroIfNegative(): Double = if (this >= 0) this else 0.0
fun Float.orZeroIfNegative(): Float = if (this >= 0) this else 0.0f
fun Int.orZeroIfNegative(): Int = if (this >= 0) this else 0

fun clampToZeroOrMore(value: Double): Double = if (value >= 0) value else 0.0
fun clampToZeroOrMore(value: Float): Float = if (value >= 0) value else 0.0f
fun clampToZeroOrMore(value: Int): Int = if (value >= 0) value else 0

fun clampToZeroOrLess(value: Double): Double = if (value <= 0) value else 0.0
fun clampToZeroOrLess(value: Float): Float = if (value <= 0) value else 0.0f
fun clampToZeroOrLess(value: Int): Int = if (value <= 0) value else 0

fun clampToOver(value: Double, min: Double): Double = if (value >= min) value else min
fun clampToOver(value: Float, min: Float): Float = if (value >= min) value else min
fun clampToOver(value: Int, min: Int): Int = if (value >= min) value else min

fun clampToUnder(value: Double, max: Double): Double = if (value <= max) value else max
fun clampToUnder(value: Float, max: Float): Float = if (value <= max) value else max
fun clampToUnder(value: Int, max: Int): Int = if (value <= max) value else max

/**
 * Returns the specified value if this value is NaN or infinite, otherwise this value.
 */
fun Double.orIfNotFinite(value: Double = 0.0): Double = if (this.isFinite()) this else value

/**
 * Returns the specified value if this value is NaN or infinite, otherwise this value.
 */
fun Float.orIfNotFinite(value: Float = 0f): Float = if (this.isFinite()) this else value


object MathUtils {

    /**
     * @return a value describing how many times larger a is than b, or the negative equivalent for b if b is larger.
     * If a and b are equal, zero is returned.
     * If a or b are zero or smaller, the specified [valueWhenComparedToZero] value is returned instead (positive if a is larger and negative is b is larger)
     */
    fun compareMagnitudes(a: Double, b: Double, valueWhenComparedToZero: Double = Double.POSITIVE_INFINITY): Double {
        return if (a == b) 0.0
        else if (a <= 0 || b <= 0) {
            if (a > b) valueWhenComparedToZero
            else -valueWhenComparedToZero
        } else {
            if (a > b) a / b - 1.0
            else -(b / a - 1.0)
        }
    }

    /**
     * @return the distance of [value] from the range defined by [rangeStart] and [rangeEnd]
     * (order of range values is not important).
     */
    fun distanceToRange(value: Double, rangeStart: Double, rangeEnd: Double): Double {
        val rangeMin = rangeStart min rangeEnd
        val rangeMax = rangeStart max rangeEnd

        return if (value > rangeMax) Math.abs(value - rangeMax)
        else if (value < rangeMin) Math.abs(rangeMin - value)
        else 0.0
    }

    /**
     * Scale the value to the -1 .. 1 range, so that 0 is at 0 and valueAtMidpoint is at 0.5 and -valueAtMidpoint is at -0.5.
     * @param valueAtMidpoint that should translate to 0.5.  Can not be zero.
     */
    fun squishToMinusOneToOne(value: Double, valueAtMidpoint: Double = 1.0): Double {
        val v = Math.abs(value)
        val squished = 1.0 - 1.0 / (1.0 + v / Math.abs(valueAtMidpoint))
        return if (value < 0) -squished else squished
    }


    /**
     * Changes the specified value, using a gaussian function whose standard deviation is derived from the range of the value,
     * or the current value of the value if the range is unlimited.
     */
    fun mutateValue(value: Double,
                    min: Double? = null,
                    max: Double? = null,
                    random: Rand = Rand.default,
                    changeScale: Double = 1.0,
                    changeAddition: Double = 0.000000001): Double {
        val minValue = min ?: java.lang.Double.MIN_VALUE
        val maxValue = max ?: java.lang.Double.MAX_VALUE

        // Determine variation range
        val rangeSize = if (minValue > Double.MIN_VALUE &&
            maxValue < Double.MAX_VALUE) {
            Math.abs(maxValue - minValue)
        } else if (value != 0.0) {
            Math.abs(value * (1.0 / GoldenRatio))
        } else {
            1.0
        }

        // Randomize value
        val randomVal = random.nextGaussian(value, rangeSize * changeScale + changeAddition)

        // Clamp value and return
        return randomVal.clampTo(minValue, maxValue)
    }


    /**
     * Gaussian / normal distribution / bell curve function
     *
     * See https://en.wikipedia.org/wiki/Gaussian_function
     *
     * @param x position to get the function value at
     * @param standardDeviation standard deviation of the gaussian function.  Defaults to 1
     * @param center position of the peak value, or center of the distribution.  Defaults to 0
     * @param peakValue highest value (value at center).
     *                  Defaults to 1/(stdDev*sqrt(2*Pi), which gives the function an integral of 1,
     *                  and thus acts as a normal distribution
     * @return value for the gaussian function at point x
     */
    fun gaussianFunction(x: Double,
                         standardDeviation: Double = 1.0,
                         center: Double = 0.0,
                         peakValue: Double = 1.0 / (standardDeviation * Math.sqrt(Tau))): Double {
        val offs = x - center
        return peakValue * Math.exp(-(offs*offs) / (2.0*standardDeviation*standardDeviation))
    }


    /**
     * @return two numbers a and b, such that a*b = multiplicationResult and t = b / (a+b).
     * @param t interpolates between contribution of first or second number in the pair to the result.
     *        When it is close to zero, the first number is close to multiplicationResult and the second is close to one,
     *        when it is close to one, the first number is close to one and the second one is close to multiplicationResult
     *        Must be between 0 and 1.  If zero or less, Pair(multiplicationResult, 1.0) is returned,
     *        if one or more, Pair(1.0, multiplicationResult) is returned.
     * @param multiplicationResult the number to divide up in two fractions of the specified relative size.
     *        Must be larger than zero.  If zero or less, a pair of zeroes are returned.
     * @param clampToResult if true, a or b can never be larger than multiplicationResult, if either is larger,
     *        it is set to multiplicationResult and the other is set to 1.0.
     *        Defaults to false.
     */
    fun interpolateFractions(t: Double,
                             multiplicationResult: Double,
                             clampToResult: Boolean = false): Pair<Double, Double> {
        if (multiplicationResult <= 0) return Pair(0.0, 0.0)
        else if (t <= 0.0) return Pair(multiplicationResult, 1.0)
        else if (t >= 1.0) return Pair(1.0, multiplicationResult)

        // The equation system can be solved e.g. with https://www.wolframalpha.com/input/?i=b*a%3Dr,+b%2F(b%2Ba)%3Dt,+solve+b+and+a

        val sqrtT = Math.sqrt(t)
        val sqrtOneMinusT = Math.sqrt(1.0 - t)
        val sqrtResult = Math.sqrt(multiplicationResult)

        val a = (sqrtResult - t * sqrtResult) / (sqrtOneMinusT * sqrtT)
        val b = (sqrtT * sqrtResult) / sqrtOneMinusT

        if (clampToResult) {
            if (a > multiplicationResult) return Pair(multiplicationResult, 1.0)
            else if (b > multiplicationResult) return Pair(1.0, multiplicationResult)
        }

        return Pair(a, b)
    }

    /**
     * @return three integers a, b, and c, such that a*b + c = multiplicationResult and t approximately equals b / (a+b).
     * @param t interpolates between contribution of first or second number in the pair to the result.
     *        When it is close to zero, the first number is close to multiplicationResult and the second is close to one,
     *        when it is close to one, the first number is close to one and the second one is close to multiplicationResult
     *        Must be between 0 and 1.  If zero or less, Triple(multiplicationResult, 1, 0) is returned,
     *        if one or more, Triple(1, multiplicationResult, 0) is returned.
     * @param multiplicationResult the number to divide up in two fractions of the specified relative size.
     *        Must be larger than zero.  If zero or less, a pair of zeroes are returned.
     */
    fun interpolateFractionsToInt(t: Double, multiplicationResult: Int, minA: Int = 1, minB: Int = 1): Triple<Int, Int, Int> {
        if (multiplicationResult <= 0) return Triple(0, 0, 0)
        else if (t <= 0.0) return Triple(multiplicationResult, 1, 0)
        else if (t >= 1.0) return Triple(1, multiplicationResult, 0)

        val (ad, bd) = interpolateFractions(t, multiplicationResult.toDouble(), true)

        val ai = ad.toInt() max minA
        val bi = bd.toInt() max minB
        val a: Int = if (ad <= bd) ai else multiplicationResult / bi
        val b: Int = if (ad >  bd) bi else multiplicationResult / ai
        val c = multiplicationResult - (a * b)
        return Triple(a, b, c)
    }

    val dropFractionsFormat = DecimalFormat("###")
    val bigDecimalRoundingMathContext = MathContext(4, RoundingMode.HALF_EVEN)
}

/**
 * Removes excessive fractions if [roundFractions] is true.
 */
fun Double.toFormattedString(roundFractions: Boolean = false,
                             epsilon: Double = 100 * Double.MIN_VALUE): String {
    // Drop fractions if we do not have them
    return if (isFinite() && roundFractions) BigDecimal(this, bigDecimalRoundingMathContext).stripTrailingZeros().toEngineeringString() // Clean up excessive decimals. if requested
    else if(isFinite() && ((fraction() <= epsilon) || (1.0 - fraction() <= epsilon))) MathUtils.dropFractionsFormat.format(this)
    else toString()
}

/**
 * Removes excessive fractions if [roundFractions] is true.
 */
fun Float.toFormattedString(roundFractions: Boolean = false,
                            epsilon: Float = 100 * Float.MIN_VALUE): String {
    return this.toDouble().toFormattedString(roundFractions, epsilon.toDouble())
}

/** Returns the absolute value of this number */
fun Int.abs() = Math.abs(this)

/** Returns the absolute value of this number */
fun Long.abs() = Math.abs(this)

/** Returns the absolute value of this number */
fun Float.abs() = Math.abs(this)

/** Returns the absolute value of this number */
fun Double.abs() = Math.abs(this)


/**
 * Shorthand for defining an easing function object.
 * The easing function is normally queried in the 0..1 range,
 * and normally has a value of 0 at input value 0, and 1 at input value 1,
 * and usually has values in the 0..1 range, but these are not absolute requirements.
 *
 * Easing objects also have various utility methods for applying the easing
 * function, and are used as parameters to various other classes and functions
 * that work with interpolation.
 *
 * @return an Easing instance, using the specified function.
 */
inline fun easing(crossinline f: (Double) -> Double): Easing {
    return object : Easing {
        override fun interpolate(value: Double): Double {
            return f(value)
        }
    }
}
