package fractalutils.random

import fractalutils.checking.Check


/**
 * Interface for hash functions that produce a pseudorandom value based on one input.
 * Has utility functions for converting the result into a convenient type and range.
 */
interface Hash {

    /**
     * @return hash value for the specified input value.
     */
    fun hash(input: Long): Long

    /**
     * @return hash value for the specified input values.
     */
    fun hash(seed1: Long, seed2: Long, vararg additionalSeeds: Long): Long {
        var result = hash(seed1)
        result = hash(result xor seed2)
        for (s in additionalSeeds) {
            result = hash(result xor s)
        }
        return result
    }

    /**
     * @return hash value for the specified input values.
     */
    fun hash(seed1: Int, vararg additionalSeeds: Int): Long {
        var result = hash(seed1.toLong())
        for (s in additionalSeeds) {
            result = hash(result xor s.toLong())
        }
        return result
    }

    /**
     * @return hash value for the specified input values.
     */
    fun hash(seed1: Float, vararg additionalSeeds: Float): Long {
        var result = hash(java.lang.Float.floatToIntBits(seed1).toLong())
        for (s in additionalSeeds) {
            result = hash(result xor java.lang.Float.floatToIntBits(s).toLong())
        }
        return result
    }

    /**
     * @return hash value for the specified input values.
     */
    fun hash(seed1: Double, vararg additionalSeeds: Double): Long {
        var result = hash(java.lang.Double.doubleToLongBits(seed1))
        for (s in additionalSeeds) {
            result = hash(result xor java.lang.Double.doubleToLongBits(s))
        }
        return result
    }


    /**
     * @param probabilityForTrue probability in range 0 .. 1.
     *                           0 == 0% chance for true,
     *                           1 == 100% chance for true.
     * @return random boolean with the specified probability for true, based on the specified seed.
     */
    fun hashBoolean(seed: Long, probabilityForTrue: Double): Boolean {
        return hashDouble(seed) < probabilityForTrue
    }

    /**
     * @param probabilityForTrue probability in range 0 .. 1.
     *                           0 == 0% chance for true,
     *                           1 == 100% chance for true.
     * @return random boolean with the specified probability for true, based on the specified seed.
     */
    fun hashBoolean(seed: Long, probabilityForTrue: Float): Boolean {
        return hashFloat(seed) < probabilityForTrue
    }

    /**
     * @return random byte, based on the specified seed.
     */
    fun hashByte(seed: Long): Byte {
        return (hash(seed) shr 64 - 8).toByte() // Use signed right shift
    }


    /**
     * @return random long between 0 (inclusive) and max (exclusive), based on the specified seed.
     */
    fun hashLong(seed: Long, max: Long): Long {
        if (max < 0) throw IllegalArgumentException("max should not be negative, but it was " + max)

        // Handle special case of zero
        if (max == 0L) return 0

        // Randomize
        var result = hash(seed)

        // Only return positive values
        if (result < 0) result = -result
        if (result == java.lang.Long.MIN_VALUE) result = 0 // Need to be tested separately, as -Long.MIN_VALUE == Long.MIN_VALUE.

        // Return remainder after dividing with max
        return result % max
    }

    /**
     * @return random long between min (inclusive) and max (exclusive), based on the specified seed.
     */
    fun hashLong(seed: Long, min: Long, max: Long): Long {
        if (max < min) throw IllegalArgumentException("max ($max) should be greater or equal to min ($min)")

        return min + hashLong(seed, max - min)
    }

    /**
     * @return random integer between MIN_INTEGER and MAX_INTEGER, based on the specified seed.
     */
    fun hashInt(seed: Long): Int {
        return (hash(seed) shr 32).toInt() // Use signed right shift
    }

    /**
     * @return random integer between 0 (inclusive) and max (exclusive), based on the specified seed.
     */
    fun hashInt(seed: Long, max: Int): Int {
        return hashLong(seed, max.toLong()).toInt()
    }

    /**
     * @return random integer between min (inclusive) and max (exclusive), based on the specified seed.
     */
    fun hashInt(seed: Long, min: Int, max: Int): Int {
        if (max < min) Check.greaterOrEqual(max, "max", min, "min")

        // Scale to min .. max range
        return min + hashInt(seed, max - min)
    }

    /**
     * @return random float between 0 (inclusive) and 1 (exclusive) using the specified seed.
     */
    fun hashFloat(seed: Long): Float {
        return hashBits(seed, 24) / (1 shl 24).toFloat()
    }

    /**
     * @return random float between 0 (inclusive) and max (exclusive) using the specified seed.
     */
    fun hashFloat(seed: Long, max: Float): Float {
        if (max < 0) throw IllegalArgumentException("max should not be negative, but it was " + max)

        return hashFloat(seed) * max
    }

    /**
     * @return random float between min (inclusive) and max (exclusive) using the specified seed.
     */
    fun hashFloat(seed: Long, min: Float, max: Float): Float {
        if (max < min) throw IllegalArgumentException("max ($max) should be greater or equal to min ($min)")

        return min + hashFloat(seed) * (max - min)
    }


    /**
     * @return random double between 0 (inclusive) and 1 (exclusive),
     *         using the specified seed.
     */
    fun hashDouble(seed: Long): Double {
        return hashBits(seed, 53) / (1L shl 53).toDouble()
    }

    /**
     * @return random double between 0 (inclusive) and max (exclusive),
     *         using the specified seed.
     */
    fun hashDouble(seed: Long, max: Double): Double {
        if (max < 0) throw IllegalArgumentException("max should not be negative, but it was " + max)

        return hashDouble(seed) * max
    }


    /**
     * @return random double between min (inclusive) and max (exclusive),
     *         using the specified seed.
     */
    fun hashDouble(seed: Long, min: Double, max: Double): Double {
        if (max < min) throw IllegalArgumentException("max ($max) should be greater or equal to min ($min)")

        return min + hashDouble(seed) * (max - min)
    }

    /**
     * @return random gaussian distributed float value with mean 0 and standard deviation 1,
     *         using the specified seed.
     */
    fun hashGaussianFloat(seed: Long): Float {
        return hashGaussian(seed).toFloat()
    }

    /**
     * @return random gaussian distributed float value with the specified mean and standard deviation,
     *         using the specified seed.
     */
    fun hashGaussianFloat(seed: Long, mean: Float, standardDeviation: Float): Float {
        return hashGaussian(seed).toFloat() * standardDeviation + mean
    }


    /**
     * @return random gaussian distributed value with mean 0 and standard deviation 1,
     *         using the specified seed.
     */
    fun hashGaussian(seed: Long): Double {
        var linearRandom1: Double
        var linearRandom2: Double
        var squaredDistance: Double
        do {
            // Generate two linear random numbers in -1 .. 1 range
            linearRandom1 = 2 * hashDouble(seed) - 1
            linearRandom2 = 2 * hashDouble(hash(seed)) - 1
            squaredDistance = linearRandom1 * linearRandom1 + linearRandom2 * linearRandom2
        } while (squaredDistance >= 1 || squaredDistance == 0.0)

        val multiplier = StrictMath.sqrt(-2 * StrictMath.log(squaredDistance) / squaredDistance)

        val gaussian = linearRandom1 * multiplier

        return gaussian
    }

    /**
     * @return random gaussian distributed value with the specified mean and standard deviation,
     *         using the specified seed.
     */
    fun hashGaussian(seed: Long, mean: Double, standardDeviation: Double): Double {
        return hashGaussian(seed) * standardDeviation + mean
    }

    /**
     * @return random element from the specified list, using the specified seed.
     */
    fun <T> hashElement(seed: Long, elements: List<T>): T {
        return elements[hashInt(seed, elements.size)]
    }

    /**
     * @return random element from the specified array, using the specified seed.
     */
    fun <T> hashElement(seed: Long, elements: Array<T>): T {
        return elements[hashInt(seed, elements.size)]
    }



    fun hashBits(seed: Long, numberOfBits: Int): Long {
        return hash(seed).ushr(64 - numberOfBits) // Use unsigned right shift, most significant bits will be zero
        // and the number will be positive if numberOfBits > 0
    }


    companion object {
        /**
         * Creates a new Hash using the default implementation.
         * The default implementation may change in future library versions, to use a specific implementation instantiate an implementing class.
         */
        fun createDefault(): Hash = MurmurHash3()

        /**
         * A default Hash instance.
         */
        val default: Hash = createDefault()

    }
}
