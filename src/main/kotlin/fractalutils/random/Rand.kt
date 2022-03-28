package fractalutils.random

import fractalutils.geometry.double3.Double3
import fractalutils.geometry.double3.MutableDouble3
import fractalutils.math.clamp
import fractalutils.math.fastFloor
import fractalutils.math.modPositive
import java.util.*

/**
 * Interface for random number generators.
 *
 * Not thread safe by default.
 */
interface Rand {

    /**
     * (Re)Initialize the random sequence with one or more long seeds.
     * The same seeds will always result in the same random sequence.
     */
    fun setSeed(seed: Long, vararg additionalSeeds: Long)

    /**
     * (Re)Initialize the random sequence with one or more int seeds.
     * The same seeds will always result in the same random sequence.
     */
    fun setSeed(seed: Int, vararg additionalSeeds: Int)

    /**
     * (Re)Initialize the random sequence with one or more float seeds.
     * The same seeds will always result in the same random sequence.
     */
    fun setSeed(seed: Float, vararg additionalSeeds: Float)

    /**
     * (Re)Initialize the random sequence with one or more double seeds.
     * The same seeds will always result in the same random sequence.
     */
    fun setSeed(seed: Double, vararg additionalSeeds: Double)

    /**
     * @param probabilityForTrue probability in range 0 .. 1.
     *                           0 == 0% chance for true,
     *                           1 == 100% chance for true.
     * @return random boolean with the specified probability for true.
     */
    fun nextBoolean(probabilityForTrue: Double = 0.5): Boolean

    /**
     * @param probabilityForTrue probability in range 0 .. 1.
     *                           0 == 0% chance for true,
     *                           1 == 100% chance for true.
     * @return random boolean with the specified probability for true.  Defaults to 50% chance.
     */
    fun nextBoolean(probabilityForTrue: Float = 0.5f): Boolean

    /**
     * @return random byte.
     */
    fun nextByte(): Byte

    /**
     * Fills a part of the specified output array with random bytes.
     * @param outputArray array to write to
     * @param startIndex start index to write from (inclusive)
     * @param count number of bytes to write.
     */
    fun nextBytes(outputArray: ByteArray, startIndex: Int = 0, count: Int = outputArray.size)

    /**
     * @return random integer between MIN_INTEGER and MAX_INTEGER.
     */
    fun nextInt(): Int

    /**
     * @return random integer between 0 (inclusive) and max (exclusive).
     */
    fun nextInt(max: Int): Int

    /**
     * @return random integer between min (inclusive) and max (exclusive).
     */
    fun nextInt(min: Int, max: Int): Int

    /**
     * @return random integer between MIN_LONG and MAX_LONG.
     */
    fun nextLong(): Long

    /**
     * @return random long between 0 (inclusive) and max (exclusive).
     */
    fun nextLong(max: Long): Long

    /**
     * @return random long between min (inclusive) and max (exclusive).
     */
    fun nextLong(min: Long, max: Long): Long

    /**
     * @return random float between 0 (inclusive) and 1 (exclusive).
     */
    fun nextFloat(): Float

    /**
     * @return random float between 0 (inclusive) and max (exclusive).
     */
    fun nextFloat(max: Float): Float

    /**
     * @return random float between min (inclusive) and max (exclusive).
     */
    fun nextFloat(min: Float, max: Float): Float

    /**
     * @return random double between 0 (inclusive) and 1 (exclusive).
     */
    fun nextDouble(): Double

    /**
     * @return random double between 0 (inclusive) and max (exclusive).
     */
    fun nextDouble(max: Double): Double

    /**
     * @return random double between min (inclusive) and max (exclusive).
     */
    fun nextDouble(min: Double, max: Double): Double

    /**
     * @return random gaussian distributed float value with mean 0 and standard deviation 1.
     */
    fun nextGaussianFloat(): Float

    /**
     * @return random gaussian distributed float value with the specified mean and standard deviation.
     */
    fun nextGaussianFloat(mean: Float, standardDeviation: Float): Float

    /**
     * @return random gaussian distributed float value with the specified mean and standard deviation
     * clamped to the range of min..max.  max should be >= min.
     */
    fun nextGaussianFloat(mean: Float, standardDeviation: Float, min: Float, max: Float): Float {
        return clamp(nextGaussianFloat(mean, standardDeviation), min, max)
    }

    /**
     * @return random gaussian distributed value with mean 0 and standard deviation 1.
     */
    fun nextGaussian(): Double

    /**
     * @return random gaussian distributed value with the specified mean and standard deviation.
     */
    fun nextGaussian(mean: Double, standardDeviation: Double): Double

    /**
     * @return random gaussian distributed value with the specified mean and standard deviation
     * clamped to the range of min..max.  max should be >= min.
     */
    fun nextGaussian(mean: Double, standardDeviation: Double, min: Double, max: Double): Double {
        return clamp(nextGaussian(mean, standardDeviation), min, max)
    }

    /**
     * Throws [count] dices with the specified number of [sides] and sums the results.
     */
    fun dice(count: Int = 1, sides: Int = 6): Int {
        var value = 0
        for (i in 1..count) {
            value += nextInt(sides) + 1
        }
        return value
    }

    /**
     * @return random element from the specified list.
     * @throws ArrayIndexOutOfBoundsException if the list is empty
     */
    fun <T> nextElement(elements: List<T>): T

    /**
     * @return random element from the specified array.
     * @throws ArrayIndexOutOfBoundsException if the array is empty
     */
    fun <T> nextElement(elements: Array<T>): T

    /**
     * @return random element from the specified list, or null if the list was empty.
     */
    fun <T> nextElementOrNull(elements: List<T>): T?

    /**
     * @return random element from the specified array, or null if the array was empty.
     */
    fun <T> nextElementOrNull(elements: Array<T>): T?

    /**
     * Generates a new Double3 using a linear distribution on the specified range, or -1..1 if unspecified.
     * Pass in a mutable double3 as [out] if you want to re-assign an existing Double3.
     */
    fun nextDouble3(min: Double3 = Double3.MINUS_ONES,
                    max: Double3 = Double3.ONES,
                    out: MutableDouble3 = MutableDouble3()
    ): MutableDouble3 {
        out.x = nextDouble(min.x, max.x)
        out.y = nextDouble(min.y, max.y)
        out.z = nextDouble(min.z, max.z)
        return out
    }

    /**
     * Generates a new Double3 using a gaussian distribution.
     * Pass in a mutable double3 as [out] if you want to re-assign an existing Double3.
     */
    fun nextGaussianDouble3(mean: Double = 0.0,
                            stdDev: Double = 1.0,
                            out: MutableDouble3 = MutableDouble3(),
                            min: Double = Double.NEGATIVE_INFINITY,
                            max: Double = Double.POSITIVE_INFINITY): MutableDouble3 {
        out.x = nextGaussian(mean, stdDev, min, max)
        out.y = nextGaussian(mean, stdDev, min, max)
        out.z = nextGaussian(mean, stdDev, min, max)
        return out
    }

    /**
     * Generates a new Double3 using a gaussian distribution.
     * Pass in a mutable double3 as [out] if you want to re-assign an existing Double3.
     * Takes vector arguments.
     */
    fun nextGaussianDouble3(mean: Double3,
                            stdDev: Double3 = Double3.ONES,
                            out: MutableDouble3 = MutableDouble3(),
                            min: Double3 = Double3.NEGATIVE_INFINITY,
                            max: Double3 = Double3.POSITIVE_INFINITY): MutableDouble3 {
        out.x = nextGaussian(mean.x, stdDev.x, min.x, max.x)
        out.y = nextGaussian(mean.y, stdDev.y, min.y, max.y)
        out.z = nextGaussian(mean.z, stdDev.z, min.z, max.z)
        return out
    }

    /**
     * Returns a new Rand seeded with this random sequence.
     */
    fun nextRand(): Rand

    /**
     * Shuffles the specified list in place.
     */
    fun <T> shuffle(list: MutableList<T>)

    /**
     * Shuffles the specified array in place.
     */
    fun <T> shuffle(array: Array<T>)

    /**
     * Returns a shuffled version of the specified list.
     */
    fun <T> shuffled(list: List<T>): ArrayList<T>

    /**
     * Returns a shuffled version of the specified array.
     */
    fun <T> shuffled(array: Array<T>): Array<T>

    /**
     * Returns a shuffled version of the specified string (characters are shuffled).
     */
    fun shuffled(s: CharSequence): String

    /**
     * Returns either integer value adjacent to the specified double value,
     * proportional to how close the double is to each value.
     */
    fun roundRandomly(v: Double): Int {
        if (v.isInfinite() || v.isNaN()) throw IllegalArgumentException("Can not randomly round $v")
        return v.fastFloor() + if (nextBoolean(v.modPositive(1.0))) 1 else 0
    }

    /**
     * Returns either integer value adjacent to the specified double value,
     * proportional to how close the double is to each value.
     */
    fun roundRandomly(v: Float): Int {
        return roundRandomly(v.toDouble())
    }

    companion object {

        /**
         * Creates a new Rand using the default implementation.
         * The default implementation may change in future library versions, to use a specific implementation instantiate an implementing class.
         * @param seed the random seed to use for the generator.  Defaults to current time in nanoseconds.
         */
        fun createDefault(seed: Long = System.nanoTime()): Rand = XoroShiro256PP(seed)

        private val threadLocalDefault: ThreadLocal<Rand> = object : ThreadLocal<Rand>() {
            override fun initialValue(): Rand {
                return createDefault(Thread.currentThread().id * 238747617 + System.nanoTime())
            }
        }

        /**
         * A default thread-local Rand instance.
         */
        val default: Rand get() = threadLocalDefault.get()
    }
}
