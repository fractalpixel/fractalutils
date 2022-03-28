package fractalutils.collections

import fractalutils.random.Rand

/**
 * Something that produces random entries of a specific type using a provided random sequence.
 */
interface RandomEntries<T> {

    /**
     * @returns a new random entry using the specified random sequence.
     */
    fun nextRandomEntry(rand: Rand): T
}