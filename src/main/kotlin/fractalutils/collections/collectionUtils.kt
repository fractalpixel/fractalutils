package fractalutils.collections

import fractalutils.math.clampTo
import fractalutils.math.max
import fractalutils.random.Rand
import java.util.*

/**
 * Creates a map from this iterator to the specified iterator, pairing keys and values
 * at the same index, as long as there are both keys and values for an index.
 * @returns a linked map with mappings from keys from this iterator to values from the other iterator.
 */
fun <K, V> Iterator<K>.createMapWith(other: Iterator<V>): LinkedHashMap<K, V> {
    val map = LinkedHashMap<K, V>()

    while (this.hasNext() && other.hasNext()) {
        val key = this.next()
        val value = other.next()
        map[key] = value
    }

    return map
}

/**
 * Creates a map from this collection to the specified collection, pairing keys and values
 * at the same index, as long as there are both keys and values for an index.
 * @returns a linked map with mappings from keys from this collection to values from the other collection.
 */
fun <K, V> Iterable<K>.createMapWith(other: Iterable<V>): LinkedHashMap<K, V> {
    return this.iterator().createMapWith(other.iterator())
}

/**
 * Creates a map from this collection to the specified array, pairing keys and values
 * at the same index, as long as there are both keys and values for an index.
 * @returns a linked map with mappings from keys from this collection to values from the other array.
 */
fun <K, V> Iterable<K>.createMapWith(other: Array<V>): LinkedHashMap<K, V> {
    return this.iterator().createMapWith(other.iterator())
}

/**
 * Creates a map from this array to the specified collection, pairing keys and values
 * at the same index, as long as there are both keys and values for an index.
 * @returns a linked map with mappings from keys from this array to values from the other collection.
 */
fun <K, V> Array<K>.createMapWith(other: Iterable<V>): LinkedHashMap<K, V> {
    return this.iterator().createMapWith(other.iterator())
}

/**
 * Creates a map from this array to the specified array, pairing keys and values
 * at the same index, as long as there are both keys and values for an index.
 * @returns a linked map with mappings from keys from this array to values from the other array.
 */
fun <K, V> Array<K>.createMapWith(other: Array<V>): LinkedHashMap<K, V> {
    return this.iterator().createMapWith(other.iterator())
}

/**
 * Joins a map to a string, using the specified entry and key-value separators, and the specified prefix and postfix strings.
 * By default, the string will follow the pattern "{key1: value1, key2: value2, key3: value3}"
 */
fun <K, V> Map<K, V>.joinToString(entrySeparator: String = ", ",
                                  keyValueSeparator: String = ": ",
                                  prefix: String = "{",
                                  postfix: String = "}"): String {
    val sb = StringBuilder()

    // Prefix
    sb.append(prefix)

    var first = true
    for (entry in this.entries) {
        // Entry separator
        if (first) first = false
        else sb.append(entrySeparator)

        // Key and value
        sb.append(entry.key)
        sb.append(keyValueSeparator)
        sb.append(entry.value)
    }

    // Postfix
    sb.append(postfix)

    return sb.toString()
}

/**
 * @return a randomly picked element from this list.
 * @throws ArrayIndexOutOfBoundsException if this list is empty.
 */
fun <T>List<T>.randomElement(random: Rand = Rand.default): T = random.nextElement(this)

/**
 * @return a randomly picked element from this list, or null if this list is empty.
 */
fun <T>List<T>.randomElementOrNull(random: Rand = Rand.default): T? = random.nextElementOrNull(this)

/**
 * @return a randomly picked element from this array.
 * @throws ArrayIndexOutOfBoundsException if this array is empty.
 */
fun <T>Array<T>.randomElement(random: Rand = Rand.default): T = random.nextElement(this)

/**
 * @return a randomly picked element from this array , or null if this array is empty.
 */
fun <T>Array<T>.randomElementOrNull(random: Rand = Rand.default): T? = random.nextElementOrNull(this)

/**
 * Swaps two randomly selected elements in this and another list with each other.
 */
fun <T>MutableList<T>.swapRandomElement(otherList: MutableList<T>,
                                        random: Rand = Rand.default) {
    if (this.isNotEmpty() && otherList.isNotEmpty()) {
        val thisIndex = random.nextInt(this.size)
        val otherIndex = random.nextInt(otherList.size)
        val thisElement = this[thisIndex]
        val otherElement = otherList[otherIndex]
        this[thisIndex] = otherElement
        otherList[otherIndex] = thisElement
    }
}

/**
 * Shuffles this list randomly.
 */
fun <T>MutableList<T>.shuffle(random: Rand = Rand.default) {
    random.shuffle(this)
}

/**
 * Shuffles this array randomly.
 */
fun <T>Array<T>.shuffle(random: Rand = Rand.default) {
    random.shuffle(this)
}

/**
 * @return a shuffled copy of this list
 */
fun <T>List<T>.shuffled(random: Rand = Rand.default): ArrayList<T> {
    return random.shuffled(this)
}

/**
 * @return a shuffled copy of this array
 */
fun <T>Array<T>.shuffled(random: Rand = Rand.default): Array<T> {
    return random.shuffled(this)
}

/**
 * @return a shuffled copy of this string.
 */
fun CharSequence.shuffled(random: Rand = Rand.default): String {
    return random.shuffled(this)
}



/**
 * Iterate a list with some step interval
 */
inline fun <T>List<T>.iterateWithStepSize(stepSize: Int, visitor: (index: Int, item: T) -> Unit, startIndex: Int = 0, endIndex: Int = this.size) {
    if (isNotEmpty()) {
        val start = startIndex.clampTo(0, this.size)
        val end = endIndex.clampTo(0, this.size)
        val s = stepSize max 1
        var i = start
        while (i < end) {
            visitor(i, get(i))
            i += s
        }
    }
}

/**
 * Iterate a list with some number of steps (may be fewer steps if the count is too high, or the size doesn't divide evenly with the step count.
 */
inline fun <T>List<T>.iterateWithStepCount(stepCount: Int, visitor: (index: Int, item: T) -> Unit, startIndex: Int = 0, endIndex: Int = this.size) {
    this.iterateWithStepSize(size / stepCount, visitor, startIndex, endIndex)
}

