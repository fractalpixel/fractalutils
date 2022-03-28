package fractalutils.collections

/**
 * Combines two immutable sets.
 * NOTE: Inefficient for iteration and size.
 */
class CombinedSet<T>(val primary: Set<T>,
                     val secondary: Set<T>): Set<T> {

    override val size: Int get() {
        val ps = primary.size
        var ss = secondary.size
        for (se in secondary) {
            if (primary.contains(se)) ss--
        }
        return ps + ss
    }

    override fun contains(element: T): Boolean {
        return primary.contains(element) || secondary.contains(element)
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (element in elements) {
            if (!contains(element)) return false
        }
        return true
    }

    override fun isEmpty(): Boolean {
        return primary.isEmpty() && secondary.isEmpty()
    }

    override fun iterator(): Iterator<T> {
        return (primary + secondary).iterator()
    }
}