package fractalutils.collections

import fractalutils.checking.Check
import fractalutils.random.Rand
import java.util.*

/**
 * Contains entries and weights associated with them.  Can return a random entry with probability proportional to its weight.
 */
class WeightedMap<T>(initialEntries: Map<T, Double>? = null): RandomEntries<T> {

    private val defaultRandom = Rand.createDefault()
    private val entries_: MutableMap<T, Double> = LinkedHashMap()

    /**
     * Total sum of weights of entries in this map.
     */
    var totalWeight: Double = 0.0
        private set

    /**
     * Read-only list of entries in the weighted map.
     */
    val entries: Map<T, Double>
       get() = entries_

    @Deprecated("Replaced with 'entries'", ReplaceWith("entries"))
    val readOnlyEntries: Map<T, Double> get() = entries

    val isEmpty: Boolean get() = entries_.isEmpty()

    init {
        if (initialEntries != null) addEntries(initialEntries)
    }

    fun getRelativeWeight(entry: T): Double {
        val e = entries_[entry]
        return if (e == null || e <= 0.0) 0.0
               else e / totalWeight
    }

    fun setEntry(entry: T, relativeWeight: Double) {
        removeEntry(entry)

        if (relativeWeight > 0) {
            entries_[entry] = relativeWeight
            totalWeight += relativeWeight
        }
    }

    fun removeEntry(entry: T) {
        val removed = entries_.remove(entry)
        if (removed != null) totalWeight -= removed
    }

    fun addEntries(entries: Map<T, Double>) {
        entries.forEach { addEntry(it.key, it.value) }
    }

    fun addEntry(entry: T, relativeAdditionalWeight: Double) {
        Check.positiveOrZero(relativeAdditionalWeight, "relativeAdditionalWeight")
        val weight = Math.max(0.0, relativeAdditionalWeight + (entries_[entry] ?: 0.0))
        entries_[entry] = weight
        totalWeight += relativeAdditionalWeight
    }

    /**
     * Return a random weighted entry using the specified [Rand], or using an internal
     * random sequence by default if none specified.
     */
    fun randomEntry(random: Rand = defaultRandom): T {
        var randomPosition = random.nextDouble(totalWeight)

        for (entry in entries_) {
            randomPosition -= entry.value
            if (randomPosition <= 0) return entry.key
        }

        throw IllegalStateException("Random value was $randomPosition, total weight was $totalWeight, but did not find any entry.  Sum of weights: " + entries_.values.reduce { a, b ->  a+b})
    }

    /**
     * Seeds the random number generator that is used by randomEntry by default.
     */
    fun setSeed(seed: Long) {
        defaultRandom.setSeed(seed)
    }

    override fun nextRandomEntry(rand: Rand): T = randomEntry(rand)

    override fun toString(): String {
        return "WeightedMap(${entries_.joinToString()})"
    }
}