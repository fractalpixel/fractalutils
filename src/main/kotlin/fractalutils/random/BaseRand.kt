package fractalutils.random

import fractalutils.checking.Check
import java.lang.Double.doubleToLongBits
import java.lang.Float.floatToIntBits
import java.util.*


/**
 * Common functionality for Random number generators.
 * @param seed random seed to use.
 * @param seedHashingFunction hashing function to use on a user provided seed before it is used for the random
 *                            number generator.  Helps make random sequences with adjacent seeds more independent
 *                            for many random number generators.
 */
abstract class BaseRand(seed: Long = System.nanoTime(),
                        protected val seedHashingFunction: Hash = DEFAULT_SEED_HASHING_FUNCTION
) : Rand {

    private var haveExtraGaussian: Boolean = false
    private var extraGaussian: Double = 0.0

    /**
     * Number of longs that the state uses.
     */
    protected abstract fun getStateSize(): Int

    /**
     * The current state of the random number generator.
     */
    protected val state = LongArray(getStateSize())

    init {
        setSeed(seed)
    }

    override fun setSeed(seed: Long, vararg additionalSeeds: Long) {
        val stateSize = getStateSize()

        // Hash the user provided seeds to spread them out over the long space.
        // Combine several seeds by hashing and xoring them together

        // First parameter and first seed state slot
        var s = seedHashingFunction.hash(seed)
        state[0] = s

        // Include all parameters
        val inputCount = 1 + additionalSeeds.size
        for (i in 1 until inputCount) {
            s = seedHashingFunction.hash(s) xor seedHashingFunction.hash(additionalSeeds[i - 1])
            state[i % stateSize] = state[i % stateSize] xor s
        }

        // Fill all seed state if not already filled
        if (inputCount < stateSize) {
            for (i in inputCount until stateSize) {
                s = seedHashingFunction.hash(s)
                state[i] = s
            }
        }

        // Many pseudorandom generators do not work with zero state, so change any zeroes in the state to a random constant (carefully selected by bashing the numpad).
        for (i in 0 until stateSize) {
            if (state[i] == 0L) state[i] = 687459134643853105L + i
        }

        // Clear any cached generated gaussian
        haveExtraGaussian = false
    }

    override fun setSeed(seed: Int, vararg additionalSeeds: Int) {
        setSeed(seed.toLong(),
            *additionalSeeds.map { it.toLong()}.toLongArray())
    }

    override fun setSeed(seed: Float, vararg additionalSeeds: Float) {
        setSeed(floatToIntBits(seed).toLong(),
            *additionalSeeds.map { floatToIntBits(it).toLong()}.toLongArray())
    }

    override fun setSeed(seed: Double, vararg additionalSeeds: Double) {
        setSeed(doubleToLongBits(seed),
            *additionalSeeds.map { doubleToLongBits(it)}.toLongArray())
    }

    override fun nextBoolean(probabilityForTrue: Double): Boolean {
        return nextDouble() < probabilityForTrue
    }

    override fun nextBoolean(probabilityForTrue: Float): Boolean {
        return nextFloat() < probabilityForTrue
    }

    override fun nextByte(): Byte {
        return (nextLong() shr 64 - 8).toByte() // Use signed right shift
    }

    override fun nextBytes(outputArray: ByteArray, startIndex: Int, count: Int) {
        Check.inRange(startIndex, "startIndex", 0, outputArray.size)
        Check.inRange(count, "count", 0, outputArray.size - startIndex)

        // NOTE: Could be optimized a bit by using all 8 bytes of the random long.
        for (i in startIndex..startIndex + count - 1) {
            outputArray[i] = nextByte()
        }
    }

    override fun nextInt(): Int {
        return (nextLong() shr 32).toInt() // Use signed right shift
    }

    override fun nextInt(max: Int): Int {
        return nextLong(max.toLong()).toInt()
    }

    override fun nextInt(min: Int, max: Int): Int {
        if (max < min) Check.greaterOrEqual(max, "max", min, "min")

        // Scale to min .. max range
        return min + nextInt(max - min)
    }

    override fun nextLong(max: Long): Long {
        if (max < 0) throw IllegalArgumentException("max should not be negative, but it was " + max)

        // Next gaussian should be reset, as the state of the random generator should change after each invocation.
        haveExtraGaussian = false

        // Handle special case of zero
        if (max == 0L) return 0

        // Randomize
        var result = nextLong()

        // Only return positive values
        if (result < 0) result = -result
        if (result == java.lang.Long.MIN_VALUE) result = 0 // Need to be tested separately, as -Long.MIN_VALUE == Long.MIN_VALUE.

        // Return remainder after dividing with max
        return result % max
    }

    override fun nextLong(min: Long, max: Long): Long {
        if (max < min) throw IllegalArgumentException("max ($max) should be greater or equal to min ($min)")

        return min + nextLong(max - min)
    }

    override fun nextFloat(): Float {
        return nextBits(24) / (1 shl 24).toFloat()
    }

    override fun nextFloat(max: Float): Float {
        if (max < 0) throw IllegalArgumentException("max should not be negative, but it was " + max)

        return nextFloat() * max
    }

    override fun nextFloat(min: Float, max: Float): Float {
        if (max < min) throw IllegalArgumentException("max ($max) should be greater or equal to min ($min)")

        return min + nextFloat() * (max - min)
    }

    override fun nextDouble(): Double {
        return nextBits(53) / (1L shl 53).toDouble()
    }

    override fun nextDouble(max: Double): Double {
        if (max < 0) throw IllegalArgumentException("max should not be negative, but it was " + max)

        return nextDouble() * max
    }

    override fun nextDouble(min: Double, max: Double): Double {
        if (max < min) throw IllegalArgumentException("max ($max) should be greater or equal to min ($min)")

        return min + nextDouble() * (max - min)
    }

    override fun nextGaussianFloat(): Float {
        return nextGaussian().toFloat()
    }

    override fun nextGaussianFloat(mean: Float, standardDeviation: Float): Float {
        return nextGaussian().toFloat() * standardDeviation + mean
    }

    override fun nextGaussian(): Double {
        if (haveExtraGaussian) {
            // Use earlier extra gaussian if available
            haveExtraGaussian = false
            return extraGaussian
        } else {
            var linearRandom1: Double
            var linearRandom2: Double
            var squaredDistance: Double
            do {
                // Generate two linear random numbers in -1 .. 1 range
                linearRandom1 = 2 * nextDouble() - 1
                linearRandom2 = 2 * nextDouble() - 1
                squaredDistance = linearRandom1 * linearRandom1 + linearRandom2 * linearRandom2
            } while (squaredDistance >= 1 || squaredDistance == 0.0)

            val multiplier = StrictMath.sqrt(-2 * StrictMath.log(squaredDistance) / squaredDistance)

            val gaussian = linearRandom1 * multiplier

            // Store the extra random gaussian for future use
            extraGaussian = linearRandom2 * multiplier
            haveExtraGaussian = true

            return gaussian
        }
    }

    override fun nextGaussian(mean: Double, standardDeviation: Double): Double {
        return nextGaussian() * standardDeviation + mean
    }

    override fun <T> nextElement(elements: List<T>): T {
        return elements[nextInt(elements.size)]
    }

    override fun <T> nextElement(elements: Array<T>): T {
        return elements[nextInt(elements.size)]
    }

    override fun <T> nextElementOrNull(elements: List<T>): T? {
        return if (elements.isEmpty()) null
        else elements[nextInt(elements.size)]
    }

    override fun <T> nextElementOrNull(elements: Array<T>): T? {
        return if (elements.isEmpty()) null
        else elements[nextInt(elements.size)]
    }

    override fun <T> shuffle(list: MutableList<T>) {
        // Uses Fisher-Yates ( https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle )
        fun swap(a: Int, b: Int) {
            val aValue = list[a]
            list[a] = list[b]
            list[b] = aValue
        }

        val n = list.size
        for (i in 0 .. n - 2) {
            val j = i + nextInt(n - i)
            swap(i, j)
        }
    }

    override fun <T> shuffle(array: Array<T>) {
        // Uses Fisher-Yates ( https://en.wikipedia.org/wiki/Fisher%E2%80%93Yates_shuffle )
        fun swap(a: Int, b: Int) {
            val aValue = array[a]
            array[a] = array[b]
            array[b] = aValue
        }

        val n = array.size
        for (i in 0 .. n - 2) {
            val j = i + nextInt(n - i)
            swap(i, j)
        }
    }

    override fun <T> shuffled(list: List<T>): ArrayList<T> {
        val copy = ArrayList(list)
        shuffle(copy)
        return copy
    }

    override fun <T> shuffled(array: Array<T>): Array<T> {
        val copy = Arrays.copyOf(array, array.size)
        shuffle(copy)
        return copy
    }

    override fun shuffled(s: CharSequence): String {
        // Convert to list
        val list = s.toMutableList()

        // Shuffle
        shuffle(list)

        // Join to string
        val result = StringBuilder(list.size)
        for (c in list) {
            result.append(c)
        }
        return result.toString()
    }

    private fun nextBits(numberOfBits: Int): Long {
        return nextLong().ushr(64 - numberOfBits) // Use unsigned right shift, most significant bits will be zero
        // and the number will be positive if numberOfBits > 0
    }

    companion object {

        private val DEFAULT_SEED_HASHING_FUNCTION = MurmurHash3()
    }
}
